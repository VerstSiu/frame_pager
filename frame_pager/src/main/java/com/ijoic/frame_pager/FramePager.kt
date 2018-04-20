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

  /**
   * Destroy.
   */
  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  internal fun onDestroy() {
    this.adapter = null
    this.manager = null
    this.frameId = 0

    synchronized(cacheFragmentItems) {
      cacheFragmentItems.clear()
    }
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

  private val cacheFragmentItems = HashMap<String, Fragment>()

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
      return null
    }
    lastItemPosition = position

    val adapter = this.adapter ?: return null
    val itemKey = adapter.getItemKey(position)
    val itemTag = makeFragmentTag(itemKey)

    // show or create new fragment
    val fragment = getFragmentInstance(manager, adapter, position, itemTag)

    manager
        .beginTransaction()
        .replace(frameId, fragment, itemTag)
        .commitNowAllowingStateLoss()

    return fragment
  }

  /**
   * Returns fragment instance.
   */
  private fun getFragmentInstance(manager: FragmentManager, adapter: Adapter, position: Int, itemTag: String): Fragment {
    val fragment = manager.findFragmentByTag(itemTag)

    if (fragment != null) {
      return fragment
    }
    synchronized(cacheFragmentItems) {
      val cachedFragment = cacheFragmentItems[itemTag]

      if (cachedFragment != null) {
        return cachedFragment
      }
      val newFragment = adapter.createItemInstance(position)
      cacheFragmentItems[itemTag] = newFragment
      return newFragment
    }
  }

  private fun makeFragmentTag(itemKey: String) = "$itemTagPrefix$itemKey"

}