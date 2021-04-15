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
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference

/**
 * Instant delegate impl.
 *
 * @author verstsiu@126.com on 2018/4/19.
 * @version 1.0
 */
class InstantDelegateImpl: InstantDelegate {

  private var refCallback: WeakReference<InstantDelegate.Callback>? = null

  private var rootView: View? = null
  private var rootImpl: InstantView? = null
  private var viewInit = false
  private var isReleaseComplete = false

  internal var isForceClean = false

  override fun attach(callback: InstantDelegate.Callback) {
    refCallback = WeakReference(callback)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = rootView
    if (view != null) {
      return view
    }
    viewInit = false
    isReleaseComplete = false
    return refCallback?.get()?.onCreateInstantView(inflater, container, savedInstanceState)?.also { rootView = it }
  }

  override fun onActivityCreated(host: Fragment, savedInstanceState: Bundle?, lifecycle: Lifecycle, owner: LifecycleOwner) {
    val callback = refCallback?.get() ?: return
    prepareClean(host)

    if (!viewInit) {
      viewInit = true
      rootImpl = callback.onPrepareInstantViewImpl()?.also {
        val view = rootView
        if (view != null) {
          it.attach(host)
          it.onActivityCreated(view, lifecycle, owner)
        }
      }
      callback.onInitInstantView(savedInstanceState)
      InstantManager.add(this)
    }
  }

  override fun onResume() {
    rootImpl?.onResume()
  }

  override fun onPause() {
    rootImpl?.onPause()
  }

  override fun onDestroyView() {
    rootImpl?.detach()
  }

  override fun onDestroy() {
    rootView = null
    viewInit = false

    if (!isReleaseComplete) {
      onReleaseInstantView()
    }
  }

  override fun isInstantCleanRequired(): Boolean {
    return !isReleaseComplete && (isForceClean || rootImpl?.isCleanRequired() == true)
  }

  override fun onReleaseInstantView() {
    isReleaseComplete = true
    rootImpl?.onDestroy()
    rootImpl = null
  }

  /* Clean Observer */

  private var cleanObserver: CleanObserver? = null

  private fun prepareClean(host: Fragment) {
    val activity = host.activity ?: return
    this.cleanObserver?.let { activity.lifecycle.removeObserver(it) }
    this.cleanObserver = CleanObserver(this).also {
      activity.lifecycle.addObserver(it)
    }
  }

  private class CleanObserver(impl: InstantDelegateImpl) : LifecycleObserver {
    private val refImpl = WeakReference(impl)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
      refImpl.get()?.isForceClean = true
    }
  }

  /* Clean Observer :END */

}