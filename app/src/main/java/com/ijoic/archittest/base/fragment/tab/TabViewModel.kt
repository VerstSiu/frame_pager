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
package com.ijoic.archittest.base.fragment.tab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ijoic.archittest.base.fragment.tab.constants.TabPosition
import com.ijoic.archittest.base.util.map

/**
 * Tab view model.
 *
 * @author verstsiu@126.com on 2018/4/18.
 * @version 1.0
 */
class TabViewModel: ViewModel() {

  val selectedPosition by lazy { MutableLiveData<Int>() }

  val homeSelected: LiveData<Boolean> = selectedPosition.map { it == null || TabPosition.filterDisplayPosition(it) == TabPosition.HOME }
  val dashboardSelected: LiveData<Boolean> by lazy { selectedPosition.map { it == TabPosition.DASHBOARD } }
  val notificationsSelected: LiveData<Boolean> by lazy { selectedPosition.map { it == TabPosition.NOTIFICATIONS } }

}