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

import androidx.lifecycle.*
import java.lang.ref.WeakReference

/**
 * lazy delegate impl.
 *
 * @author verstsiu@126.com on 2018/4/20.
 * @version 1.0
 */
class LazyDelegateImpl: LazyDelegate, LifecycleObserver {

  private var refCallback: WeakReference<LazyDelegate.Callback>? = null

  private val internalOwner = WrapLifecycleOwner()
  private val internalLifecycle = internalOwner.lifecycle

  private var lazyResumeInit = false
  private var lazyPauseInit = false

  override val lazyOwner: LifecycleOwner = internalOwner

  override fun attachOnCreate(callback: LazyDelegate.Callback, lifecycle: Lifecycle) {
    refCallback = WeakReference(callback)
    lifecycle.addObserver(this)
    internalLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  internal fun onStart() {
    internalLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  internal fun onResume() {
    val callback = refCallback?.get() ?: return
    lazyPauseInit = false

    if (callback.getUserVisibleHint() && !lazyResumeInit) {
      lazyResumeInit = true
      performLazyResume(callback)
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  internal fun onPause() {
    val callback = refCallback?.get() ?: return
    lazyResumeInit = false

    if (callback.getUserVisibleHint() && !lazyPauseInit) {
      lazyPauseInit = true
      performLazyPause(callback)
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  internal fun onStop() {
    internalLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  internal fun onDestroy() {
    internalLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  }

  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
    val callback = refCallback?.get() ?: return
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
    internalLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
  }

  private fun performLazyPause(callback: LazyDelegate.Callback) {
    internalLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    callback.onLazyPause()
  }

  private class WrapLifecycleOwner: LifecycleOwner {
    private val registry = LifecycleRegistry(this)

    override fun getLifecycle() = registry
  }

}