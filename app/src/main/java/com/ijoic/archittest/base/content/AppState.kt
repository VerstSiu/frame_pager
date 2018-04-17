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

import android.content.Context
import java.lang.ref.WeakReference

/**
 * App state.
 *
 * @author verstsiu@126.com on 2018/4/17.
 * @version 1.0
 */
object AppState {

  private var refAppContext: WeakReference<Context>? = null

  /**
   * Bind app context.
   *
   * @param context application context.
   */
  fun bindAppContext(context: Context) {
    refAppContext = WeakReference(context.applicationContext)
  }

  /**
   * Returns app context.
   */
  fun getAppContext(): Context? = refAppContext?.get()

}