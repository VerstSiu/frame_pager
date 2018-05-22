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

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleRegistry
import java.lang.ref.WeakReference

/**
 * lazy delegate impl.
 *
 * @author verstsiu@126.com on 2018/4/20.
 * @version 1.0
 */
class LazyDelegateImpl(callback: LazyDelegate.Callback): LazyDelegateLive {

  private val refCallback = WeakReference(callback)
  private val lifecycle = LifecycleRegistry(this)

  private var lazyResumeInit = false
  private var lazyPauseInit = false

  override fun getLifecycle() = lifecycle
  override fun onInit() = lifecycle.markState(Lifecycle.State.INITIALIZED)
  override fun onCreate() = lifecycle.markState(Lifecycle.State.CREATED)
  override fun onStart() = lifecycle.markState(Lifecycle.State.STARTED)
  override fun onStop() = lifecycle.markState(Lifecycle.State.CREATED)
  override fun onDestroy() = lifecycle.markState(Lifecycle.State.DESTROYED)

  override fun onResume() {
    val callback = refCallback.get() ?: return
    lazyPauseInit = false

    if (callback.getUserVisibleHint() && !lazyResumeInit) {
      lazyResumeInit = true
      performLazyResume(callback)
    }
  }

  override fun onPause() {
    val callback = refCallback.get() ?: return
    lazyResumeInit = false

    if (callback.getUserVisibleHint() && !lazyPauseInit) {
      lazyPauseInit = true
      performLazyPause(callback)
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
        performLazyResume(callback)
      }
    } else {
      lazyResumeInit = false

      if (!lazyPauseInit) {
        lazyPauseInit = true
        performLazyPause(callback)
      }
    }
  }

  private fun performLazyResume(callback: LazyDelegate.Callback) {
    callback.onLazyResume()
    lifecycle.markState(Lifecycle.State.RESUMED)
  }

  private fun performLazyPause(callback: LazyDelegate.Callback) {
    callback.onLazyPause()
    lifecycle.markState(Lifecycle.State.STARTED)
  }

}