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

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Frame pager.
 *
 * @author verstsiu@126.com on 2018/4/17.
 * @version 1.0
 */
class FramePager(
    @IdRes private val frameId: Int,
    private val manager: FragmentManager) {

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

  /**
   * Set current display item.
   *
   * @param position item position.
   */
  fun setCurrentItem(position: Int) {
    adapter?.let {
      manager
          .beginTransaction()
          .replace(frameId, it.createItemInstance(position))
          .commit()
    }
  }

}