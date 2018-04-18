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
package com.ijoic.archittest.base.fragment.nested_tab

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ijoic.archittest.R
import com.ijoic.archittest.base.fragment.StateFragment
import com.ijoic.archittest.base.fragment.color.ColorFragment
import com.ijoic.archittest.base.fragment.tab.TabFragment
import com.ijoic.archittest.base.fragment.tab.TabViewModel
import com.ijoic.archittest.base.fragment.tab.constants.TabPosition
import com.ijoic.archittest.base.util.ArgumentSource
import com.ijoic.archittest.base.util.bindArgsString
import com.ijoic.frame_pager.FramePager
import kotlinx.android.synthetic.main.fragment_simple_tab.*

/**
 * Nested tab fragment.
 *
 * @author verstsiu@126.com on 2018/4/18.
 * @version 1.0
 */
class NestedTabFragment: StateFragment(), ArgumentSource {

  /**
   * Page name.
   */
  var pageName by bindArgsString("page_name")

  private val framePager = FramePager()
  private val adapter = object : FramePager.Adapter {
    private val items: Array<Fragment> = arrayOf(
        TabFragment().apply { pageName = "pos 0" },
        ColorFragment().apply { color = Color.YELLOW; colorText = "yellow" },
        ColorFragment().apply { color = Color.CYAN; colorText = "cyan" }
    )

    override fun getItemKey(position: Int) = "$position"

    override fun createItemInstance(position: Int) = items.getOrNull(position) ?: StateFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_simple_tab, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    framePager.apply {
      init(lifecycle, R.id.tab_page_frame, childFragmentManager)
      adapter = this@NestedTabFragment.adapter
    }
    val model = ViewModelProviders.of(this).get(TabViewModel::class.java)

    model.selectedPosition.observe(this, Observer { framePager.setCurrentItem(it ?: TabPosition.HOME) })
    model.homeSelected.observe(this, Observer { tab_item_home.isSelected = it == true })
    model.dashboardSelected.observe(this, Observer { tab_item_dashboard.isSelected = it == true })
    model.notificationsSelected.observe(this, Observer { tab_item_notifications.isSelected = it == true })

    if (savedInstanceState == null) {
      model.selectedPosition.value = 0
    }

    tab_item_home.setOnClickListener { model.selectedPosition.value = TabPosition.HOME }
    tab_item_dashboard.setOnClickListener { model.selectedPosition.value = TabPosition.DASHBOARD }
    tab_item_notifications.setOnClickListener { model.selectedPosition.value = TabPosition.NOTIFICATIONS }
  }

  override fun toString(): String {
    return "tab_$pageName"
  }

}