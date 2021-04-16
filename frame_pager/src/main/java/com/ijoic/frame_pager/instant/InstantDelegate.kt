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
package com.ijoic.frame_pager.instant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * Instant delegate.
 *
 * @author verstsiu@126.com on 2018/4/20.
 * @version 1.0
 */
interface InstantDelegate : InstantHost {

  /**
   * Delegate callback.
   */
  interface Callback : InstantHost {
    /**
     * Create instant view.
     *
     * @param inflater layout inflater.
     * @param container container.
     * @param savedInstanceState saved instance state.
     */
    fun onCreateInstantView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

    /**
     * Prepare instant view impl
     *
     * @since 1.1.2
     */
    fun onPrepareInstantViewImpl(): InstantView? = null

    /**
     * Initialize instant view.
     *
     * @param savedInstanceState saved instance state.
     */
    fun onInitInstantView(savedInstanceState: Bundle?) {}
  }

  /**
   * Attach [callback]
   *
   * @since 1.1.2
   */
  fun attach(callback: Callback)

  /**
   * Create content view.
   *
   * @param inflater layout inflater.
   * @param container container.
   * @param savedInstanceState saved instance state.
   */
  fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

  /**
   * Activity created.
   *
   * @param savedInstanceState saved instance state.
   */
  fun onActivityCreated(
    host: Fragment,
    savedInstanceState: Bundle?,
    lifecycle: Lifecycle = host.lifecycle,
    owner: LifecycleOwner = host.viewLifecycleOwner
  )

  /**
   * Fragment destroy view
   *
   * @since 1.1.2
   */
  fun onDestroyView()
}