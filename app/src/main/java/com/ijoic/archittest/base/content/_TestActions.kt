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
package com.ijoic.archittest.base.content

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Test actions extension.
 *
 * @author verstsiu@126.com on 2018/4/17.
 * @version 1.0
 */

/**
 * Bind action.
 *
 * @param suffix action suffix.
 */
fun TestActions.bindAction(suffix: String?): ReadOnlyProperty<TestActions, String> = object: ReadOnlyProperty<TestActions, String> {

  private val actionPath by lazy {
    val appContext = AppState.getAppContext() ?: throw IllegalArgumentException("could not generate action path, application is not created")

    arrayOf(appContext.packageName, moduleName, suffix)
        .filter { it != null && !it.isEmpty() }
        .joinToString(".")
  }

  override fun getValue(thisRef: TestActions, property: KProperty<*>) = actionPath

}