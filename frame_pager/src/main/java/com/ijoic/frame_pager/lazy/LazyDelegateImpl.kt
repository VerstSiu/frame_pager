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
package com.ijoic.frame_pager.lazy

import java.lang.ref.WeakReference

/**
 * lazy delegate impl.
 *
 * @author verstsiu@126.com on 2018/4/20.
 * @version 1.0
 */
class LazyDelegateImpl(callback: LazyDelegate.Callback): LazyDelegate {

  private val refCallback = WeakReference(callback)

  private var lazyResumeInit = false
  private var lazyPauseInit = false

  override fun onResume() {
    val callback = refCallback.get() ?: return
    lazyPauseInit = false

    if (callback.getUserVisibleHint() && !lazyResumeInit) {
      lazyResumeInit = true
      callback.onLazyResume()
    }
  }

  override fun onPause() {
    val callback = refCallback.get() ?: return
    lazyResumeInit = false

    if (callback.getUserVisibleHint() && !lazyPauseInit) {
      lazyPauseInit = true
      callback.onLazyPause()
    }
  }

  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
    val callback = refCallback.get() ?: return
    if (!callback.isResumed()) {
      return
    }

    if (isVisibleToUser) {
      lazyPauseInit = false

      if (!lazyPauseInit) {
        lazyResumeInit = true
        callback.onLazyResume()
      }
    } else {
      lazyResumeInit = false

      if (!lazyPauseInit) {
        lazyPauseInit = true
        callback.onLazyPause()
      }
    }
  }

}