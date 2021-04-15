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
package com.ijoic.frame_pager.instantlazy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.ijoic.frame_pager.lazy.LazyLifecycleOwner

/**
 * Instant lazy fragment.
 *
 * @author verstsiu@126.com on 2018/4/20.
 * @version 1.0
 */
abstract class InstantLazyFragment: Fragment(), InstantLazy.Callback, LazyLifecycleOwner {

  private val delegate by lazy { InstantLazy() }

  override val lazyOwner: LifecycleOwner
    get() = delegate.lazyOwner

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    delegate.attach(this)
    delegate.attachLazy(this)
    return delegate.onCreateView(inflater, container, savedInstanceState)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    delegate.onActivityCreated(this, savedInstanceState, delegate.lazyOwner.lifecycle, delegate.lazyOwner)
    delegate.onCreate()
  }

  override fun onStart() {
    super.onStart()
    delegate.onStart()
  }

  override fun onStop() {
    delegate.onStop()
    super.onStop()
  }

  override fun onResume() {
    super.onResume()
    delegate.onResume()
  }

  override fun onPause() {
    super.onPause()
    delegate.onPause()
  }

  override fun onLazyResume() {
    delegate.onLazyResume()
  }

  override fun onLazyPause() {
    delegate.onLazyPause()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    delegate.onDestroyView()
  }

  override fun onDestroy() {
    super.onDestroy()
    delegate.onDestroy()
  }

  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
    super.setUserVisibleHint(isVisibleToUser)
    delegate.setUserVisibleHint(isVisibleToUser)
  }
}