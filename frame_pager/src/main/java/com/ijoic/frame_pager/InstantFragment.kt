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

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Instant fragment.
 *
 * @author verstsiu@126.com on 2018/4/19.
 * @version 1.0
 */
open class InstantFragment: Fragment() {

  private var cachedView: View? = null
  private var viewInit = false

  final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    var contentView = this.cachedView

    if (contentView == null) {
      contentView = onCreateInstantView(inflater, container, savedInstanceState)
      this.cachedView = contentView
    }
    return contentView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    if (!viewInit) {
      viewInit = true
      onInitInstantView(savedInstanceState)
    }
  }

  override fun onDestroy() {
    viewInit = false
    cachedView = null
    super.onDestroy()
  }

  /**
   * Create instant view.
   *
   * @param inflater layout inflater.
   * @param container container.
   * @param savedInstanceState saved instance state.
   */
  protected open fun onCreateInstantView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return super.onCreateView(inflater, container, savedInstanceState)
  }

  /**
   * Initialize instant view.
   *
   * @param savedInstanceState saved instance state.
   */
  protected open fun onInitInstantView(savedInstanceState: Bundle?) {
  }
}