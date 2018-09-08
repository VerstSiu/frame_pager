package com.ijoic.frame_pager.lazy

import android.arch.lifecycle.LifecycleOwner

/**
 * Lazy lifecycle owner.
 *
 * @author verstsiu on 2018/5/22.
 * @version 2.0
 */
interface LazyLifecycleOwner {
  /**
   * Lazy owner.
   */
  val lazyOwner: LifecycleOwner
}