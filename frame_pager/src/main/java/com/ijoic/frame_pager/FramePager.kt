/*
 *
 *  Copyright(c) 2018 VerstSiu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.ijoic.frame_pager

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Frame pager.
 *
 * @author verstsiu@126.com on 2018/4/17.
 * @version 1.0
 */
class FramePager(pagerName: String = ""): LifecycleObserver {

  private var frameId = 0
  private var manager: FragmentManager? = null

  private val itemTagPrefix = "frame_pager:$pagerName:"

  /**
   * Initialize.
   *
   * @param lifecycle lifecycle.
   * @param frameId frame id.
   * @param manager fragment manager.
   */
  fun init(lifecycle: Lifecycle, @IdRes frameId: Int, manager: FragmentManager) {
    this.frameId = frameId
    this.manager = manager
    lifecycle.addObserver(this)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  internal fun onStop() {
    val manager = this.manager ?: return
    val fragments = manager.fragments?.toMutableList() ?: return
    val lastFragment = this.lastFragment

    if (fragments.size > 1) {
      val transaction = manager.beginTransaction()

      fragments.forEach {
        val itemTag = it.tag

        if (it != lastFragment && itemTag != null && itemTag.startsWith(itemTagPrefix)) {
          transaction.detach(it)
        }
      }
      transaction.commitNowAllowingStateLoss()
    }
  }

  /**
   * Destroy.
   */
  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  internal fun onDestroy() {
    this.adapter = null
    this.manager = null
    this.frameId = 0
  }

  /* <>-<>-<>-<>-<>-<>-<>-<>-<>-<> adapter :start <>-<>-<>-<>-<>-<>-<>-<>-<>-<> */

  /**
   * Adapter.
   */
  interface Adapter {
    /**
     * Returns item key.
     *
     * @param position item position.
     */
    fun getItemKey(position: Int) = "adapter_$position"

    /**
     * Returns new created item instance.
     *
     * @param position item position.
     */
    fun createItemInstance(position: Int): Fragment
  }

  /**
   * Adapter.
   */
  var adapter: Adapter? = null

  /* <>-<>-<>-<>-<>-<>-<>-<>-<>-<> adapter :end <>-<>-<>-<>-<>-<>-<>-<>-<>-<> */

  private var lastItemPosition = -1
  private var lastFragment: Fragment? = null

  /**
   * Set current display item.
   *
   * <p>Returns current display fragment item or null.</p>
   *
   * @param position item position.
   */
  fun setCurrentItem(position: Int): Fragment? {
    val manager = this.manager ?: return null
    val frameId = this.frameId

    if (lastItemPosition == position) {
      return getCachedFragmentInstance(manager, position)
    }
    lastItemPosition = position

    val transaction = manager.beginTransaction()
    val lastFragment = this.lastFragment
    this.lastFragment = null

    if (lastFragment != null) {
      transaction.hide(lastFragment)
      lastFragment.tag?.let { toggleFragmentVisibleState(lastFragment, it, false) }
    }

    val adapter = this.adapter ?: return null
    val itemKey = adapter.getItemKey(position)
    val itemTag = makeFragmentTag(itemKey)

    // show or create new fragment
    var fragment = manager.findFragmentByTag(itemTag)

    if (fragment != null) {
      if (fragment.isDetached) {
        transaction.attach(fragment)
      }
      toggleFragmentVisibleState(fragment, itemTag, true)
      transaction.show(fragment)

    } else {
      fragment = adapter.createItemInstance(position)
      transaction.add(frameId, fragment, itemTag)
    }
    transaction.commitNowAllowingStateLoss()
    this.lastFragment = fragment
    return fragment
  }

  /**
   * Returns cached fragment instance.
   *
   * @param manager fragment manager.
   * @param position item position.
   */
  private fun getCachedFragmentInstance(manager: FragmentManager, position: Int): Fragment? {
    val adapter = this.adapter ?: return null
    val itemKey = adapter.getItemKey(position)
    val itemTag = makeFragmentTag(itemKey)

    return manager.findFragmentByTag(itemTag)
  }

  private fun makeFragmentTag(itemKey: String) = "$itemTagPrefix$itemKey"

  /* <>-<>-<>-<>-<>-<>-<>-<>-<>-<> fragment state :start <>-<>-<>-<>-<>-<>-<>-<>-<>-<> */

  /**
   * Toggle fragment visible state.
   *
   * <p>Toggle current and current child fragments' visible status.</p>
   *
   * @param fragment fragment.
   * @param itemTag item tag.
   * @param visible visible.
   */
  private fun toggleFragmentVisibleState(fragment: Fragment, itemTag: String, visible: Boolean) {
    fragment.setMenuVisibility(visible)
    fragment.userVisibleHint = visible
    val manager = fragment.childFragmentManager
    val childItems = manager.fragments ?: return

    if (visible) {
      val resumeItems = popResumeChildItems(itemTag)

      resumeItems?.forEach {
        it.setMenuVisibility(true)
        it.userVisibleHint = true
      }
    } else {
      val resumeItems = childItems.filter { it.userVisibleHint }

      resumeItems.forEach {
        it.setMenuVisibility(false)
        it.userVisibleHint = false
      }
      if (!resumeItems.isEmpty()) {
        pushResumeChildItems(itemTag, resumeItems)
      }
    }
  }

  /* <>-<>-<>-<>-<>-<>-<>-<>-<>-<> fragment state :end <>-<>-<>-<>-<>-<>-<>-<>-<>-<> */

  /* <>-<>-<>-<>-<>-<>-<>-<>-<>-<> resume child :start <>-<>-<>-<>-<>-<>-<>-<>-<>-<> */

  private val resumeChildItems = HashMap<String, List<Fragment>>()

  /**
   * Push resume child items.
   *
   * @param itemTag item tag.
   * @param items items.
   */
  private fun pushResumeChildItems(itemTag: String, items: List<Fragment>) {
    synchronized(resumeChildItems) {
      resumeChildItems[itemTag] = items
    }
  }

  /**
   * Pop resume child items.
   *
   * @param itemTag item tag.
   */
  private fun popResumeChildItems(itemTag: String): List<Fragment>? {
    synchronized(resumeChildItems) {
      val items = resumeChildItems[itemTag]
      resumeChildItems.remove(itemTag)
      return items
    }
  }

  /* <>-<>-<>-<>-<>-<>-<>-<>-<>-<> resume child :end <>-<>-<>-<>-<>-<>-<>-<>-<>-<> */

}