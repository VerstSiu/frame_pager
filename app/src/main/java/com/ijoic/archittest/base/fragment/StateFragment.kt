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
package com.ijoic.archittest.base.fragment

import android.os.Bundle
import android.util.Log
import com.ijoic.frame_pager.instant.InstantFragment

/**
 * State fragment.
 *
 * @author verstsiu@126.com on 2018/4/18.
 * @version 1.0
 */
abstract class StateFragment: InstantFragment() {

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    logState("activity created")
  }

  override fun onPause() {
    super.onPause()
    logState("pause")
  }

  override fun onResume() {
    super.onResume()
    logState("resume")
  }

  override fun onStart() {
    super.onStart()
    logState("start")
  }

  override fun onStop() {
    super.onStop()
    logState("stop")
  }

  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
    super.setUserVisibleHint(isVisibleToUser)
    logState("set visible hint [$isVisibleToUser]")
  }

  override fun onDestroy() {
    super.onDestroy()
    logState("destroy")
  }

  private fun logState(state: String) {
    Log.e("fragment_state", "[$this] $state")
  }
}