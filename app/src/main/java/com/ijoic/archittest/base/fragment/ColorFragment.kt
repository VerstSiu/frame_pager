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

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ijoic.archittest.R
import com.ijoic.archittest.base.util.ArgumentSource
import com.ijoic.archittest.base.util.bindArgsInt
import kotlinx.android.synthetic.main.fragment_simple_color.*

/**
 * Color fragment.
 *
 * @author verstsiu@126.com on 2018/4/17.
 * @version 1.0
 */
class ColorFragment: Fragment(), ArgumentSource {

  /**
   * Color.
   */
  var color by bindArgsInt("color", Color.WHITE)

  private var model: ColorViewModel? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_simple_color, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    Log.e("color_fragment", "[$this] fragment created, color: $color")

    if (this.model == null) {
      val model = ViewModelProviders.of(this).get(ColorViewModel::class.java)
      model.pageColor.observe(this, Observer { page_content.setBackgroundColor(it ?: Color.WHITE) })

      model.pageColor.value = color
      this.model = model
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    this.model = null
  }
}