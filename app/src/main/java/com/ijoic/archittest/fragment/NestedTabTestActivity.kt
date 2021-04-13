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
package com.ijoic.archittest.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ijoic.archittest.R
import com.ijoic.archittest.base.fragment.nested_tab.NestedTabFragment

/**
 * Nested tab test activity.
 *
 * @author verstsiu@126.com on 2018/4/17.
 * @version 1.0
 */
class NestedTabTestActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_simple_frame)

    if (savedInstanceState == null) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.root_page_frame, NestedTabFragment())
          .commit()
    }
  }

}
