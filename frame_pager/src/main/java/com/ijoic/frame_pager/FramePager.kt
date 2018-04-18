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
import android.os.Bundle
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

  /**
   * Destroy.
   */
  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  internal fun onDestroy() {
    this.adapter = null
    this.manager = null
    this.frameId = 0
    destroyCacheFragmentItems()
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
    fun getItemKey(position: Int): String

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

  private val cacheFragmentItems = ArrayList<Fragment>()

  /**
   * Set current display item.
   *
   * @param position item position.
   */
  fun setCurrentItem(position: Int) {
    val manager = this.manager ?: return
    val frameId = this.frameId

    if (lastItemPosition == position) {
      return
    }
    lastItemPosition = position

    val transaction = manager.beginTransaction()
    val lastFragment = this.lastFragment
    this.lastFragment = null

    val adapter = this.adapter ?: return
    val itemKey = adapter.getItemKey(position)
    val itemTag = makeFragmentTag(itemKey)
    selectedItemTag = itemTag

    // hide last fragment
    lastFragment?.let {
      upgradeFragmentVisibleStatus(it, false)
      transaction.hide(it)
    }

    // show or create new fragment
    var fragment = manager.findFragmentByTag(itemTag)

    if (fragment != null) {
      upgradeFragmentVisibleStatus(fragment, true)

      // This fragment may comes from manager cache.
      transaction
          .show(fragment)
          .commitNowAllowingStateLoss()

      if (!isFragmentItemCached(fragment)) {
        addFragmentItemToCache(fragment)
      }

    } else {
      fragment = adapter.createItemInstance(position)
      upgradeFragmentVisibleStatus(fragment, true)

      transaction
          .add(frameId, fragment, itemTag)
          .commitNowAllowingStateLoss()

      addFragmentItemToCache(fragment)
    }

    // upgrade last fragment
    this.lastFragment = fragment
  }

  /**
   * Destroy cached fragment items.
   */
  private fun destroyCacheFragmentItems() {
    val manager = this.manager ?: return
    val items = popOutCacheFragmentItems()
    val transaction = manager.beginTransaction()

    items.forEach {
      transaction.detach(it)
    }
    transaction.commitNowAllowingStateLoss()
  }

  private fun popOutCacheFragmentItems(): List<Fragment> {
    synchronized(cacheFragmentItems) {
      val popResult = cacheFragmentItems.toMutableList()
      cacheFragmentItems.clear()
      return popResult
    }
  }

  /**
   * Returns fragment item cached status.
   *
   * @param fragment fragment item.
   */
  private fun isFragmentItemCached(fragment: Fragment): Boolean {
    synchronized(cacheFragmentItems) {
      return cacheFragmentItems.contains(fragment)
    }
  }

  /**
   * Add fragment item to cache.
   *
   * @param fragment fragment item.
   */
  private fun addFragmentItemToCache(fragment: Fragment) {
    synchronized(cacheFragmentItems) {
      cacheFragmentItems.add(fragment)
    }
  }

  private fun makeFragmentTag(itemKey: String) = "$itemTagPrefix$itemKey"

  /* <>-<>-<>-<>-<>-<>-<>-<>-<>-<> fragment state methods :start <>-<>-<>-<>-<>-<>-<>-<>-<>-<> */

  /**
   * Upgrade fragment visible status.
   *
   * @param fragment fragment.
   * @param visible visible status.
   */
  private fun upgradeFragmentVisibleStatus(fragment: Fragment, visible: Boolean) {
    fragment.setMenuVisibility(visible)
    fragment.userVisibleHint = visible
  }

  /* <>-<>-<>-<>-<>-<>-<>-<>-<>-<> fragment state methods :end <>-<>-<>-<>-<>-<>-<>-<>-<>-<> */

  /* <>-<>-<>-<>-<>-<>-<>-<>-<>-<> instance state :start <>-<>-<>-<>-<>-<>-<>-<>-<>-<> */

  private var selectedItemTag: String? = null

  private val keySelectedItemTag = "$itemTagPrefix::selected_tag"

  /**
   * Save instance state.
   *
   * @param outState state.
   */
  fun savedInstanceState(outState: Bundle?) {
    outState?.putString(keySelectedItemTag, selectedItemTag)
  }

  /**
   * Restore instance state.
   *
   * @param savedInstanceState state.
   */
  fun restoreInstanceState(savedInstanceState: Bundle?) {
    val itemTag = savedInstanceState?.getString(keySelectedItemTag) ?: return
    selectedItemTag = itemTag

    val manager = this.manager ?: return
    val fragments = manager.fragments

    if (fragments == null || fragments.isEmpty()) {
      return
    }
    val transaction = manager.beginTransaction()

    fragments.forEach {
      val tag = it.tag

      if (tag != null) {
        when {
          tag == itemTag -> {
            upgradeFragmentVisibleStatus(it, true)
            lastFragment = it
          }
          tag.startsWith(itemTagPrefix) -> {
            upgradeFragmentVisibleStatus(it, false)
            transaction.hide(it)
          }
        }
      }
    }
    transaction.commitNowAllowingStateLoss()
  }

  /* <>-<>-<>-<>-<>-<>-<>-<>-<>-<> instance state :end <>-<>-<>-<>-<>-<>-<>-<>-<>-<> */

}