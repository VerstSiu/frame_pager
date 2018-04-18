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

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import com.ijoic.archittest.R
import com.ijoic.archittest.base.fragment.ColorFragment
import com.ijoic.frame_pager.FramePager
import kotlinx.android.synthetic.main.activity_fragment_test.*

/**
 * Fragment test activity.
 *
 * @author verstsiu@126.com on 2018/4/17.
 * @version 1.0
 */
class FragmentTestActivity : AppCompatActivity() {

  private val framePager = FramePager()
  private val adapter = object : FramePager.Adapter {
    private val colors = arrayOf(Color.GRAY, Color.CYAN, Color.RED, Color.BLUE)

    override fun getItemKey(position: Int) = "$position"

    override fun createItemInstance(position: Int) = ColorFragment().apply {
      color = colors[Math.abs(position) % colors.size]
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_fragment_test)

    framePager.apply {
      init(lifecycle, R.id.page_content_frame, supportFragmentManager)
      adapter = this@FragmentTestActivity.adapter
    }
    navigation.setOnNavigationItemSelectedListener({ onTabSelectedChanged(it.itemId) })

    if (savedInstanceState == null) {
      // initialize pager position when savedInstanceState is empty
      onTabSelectedChanged(navigation.selectedItemId)
    }
  }

  private fun onTabSelectedChanged(@IdRes id: Int): Boolean {
    return when (id) {
      R.id.navigation_home -> {
        framePager.setCurrentItem(0)
        true
      }
      R.id.navigation_dashboard -> {
        framePager.setCurrentItem(1)
        true
      }
      R.id.navigation_notifications -> {
        framePager.setCurrentItem(2)
        true
      }
      else -> false
    }
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    super.onSaveInstanceState(outState)
    framePager.savedInstanceState(outState)
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    super.onRestoreInstanceState(savedInstanceState)
    framePager.restoreInstanceState(savedInstanceState)
  }
}
