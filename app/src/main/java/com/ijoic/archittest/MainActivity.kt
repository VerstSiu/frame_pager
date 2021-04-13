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
package com.ijoic.archittest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ijoic.archittest.base.util.routeTo
import com.ijoic.archittest.fragment.base.constants.FragmentTestActions
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main activity.
 *
 * @author verstsiu@126.com on 2018/4/17.
 * @version 1.0
 */
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    action_fragment_test.setOnClickListener { routeTo(FragmentTestActions.tabTest) }
    action_nested_fragment_test.setOnClickListener { routeTo(FragmentTestActions.nestedTabTest) }
  }

}
