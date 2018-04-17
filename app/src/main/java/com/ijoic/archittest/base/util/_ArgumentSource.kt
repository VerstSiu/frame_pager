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
package com.ijoic.archittest.base.util

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Argument source extension.
 *
 * @author verstsiu@126.com on 2018/4/17.
 * @version 1.0
 */

/**
 * Bind arguments string field.
 *
 * @param field field name.
 * @param defValue default value.
 */
fun ArgumentSource.bindArgsInt(field: String, defValue: Int = 0): ReadWriteProperty<ArgumentSource, Int> = object : ReadWriteProperty<ArgumentSource, Int> {

  private var argsInit = false
  private var insideValue = defValue

  override fun getValue(thisRef: ArgumentSource, property: KProperty<*>): Int {
    if (!argsInit) {
      return thisRef.getArguments()?.getInt(field, defValue) ?: defValue
    }
    return insideValue
  }

  override fun setValue(thisRef: ArgumentSource, property: KProperty<*>, value: Int) {
    insideValue = value
    argsInit = true

    var args = thisRef.getArguments()
    if (args == null) {
      args = Bundle()
      thisRef.setArguments(args)
    }
    args.putInt(field, value)
  }

}