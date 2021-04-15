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

/**
 * Instant fragment.
 *
 * @author verstsiu@126.com on 2018/4/19.
 * @version 1.0
 */
abstract class InstantFragment: Fragment(), InstantDelegate.Callback {

  private val delegate by lazy { InstantDelegateImpl() }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    delegate.attach(this)
    return delegate.onCreateView(inflater, container, savedInstanceState)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    delegate.onActivityCreated(this, savedInstanceState)
  }

  override fun onResume() {
    super.onResume()
    delegate.onResume()
  }

  override fun onPause() {
    super.onPause()
    delegate.onPause()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    delegate.onDestroyView()
  }

  override fun onDestroy() {
    delegate.onDestroy()
    super.onDestroy()
  }

  override fun isInstantCleanRequired(): Boolean {
    return delegate.isInstantCleanRequired()
  }

  override fun onReleaseInstantView() {
    delegate.onReleaseInstantView()
  }

}