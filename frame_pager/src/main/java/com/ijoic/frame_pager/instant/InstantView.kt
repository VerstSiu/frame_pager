package com.ijoic.frame_pager.instant

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

/**
 * Instant view
 *
 * 避免在实现的过程中持有 Fragment 实例的强引用，并且没有添加释放处理
 *
 * @author verstsiu created at 2021-04-15 09:17
 * @since 1.1.2
 */
abstract class InstantView {

  private var isActive = false
  private var inactiveMs = 0L

  /**
   * Keep always if possible(ignore keepMs)
   */
  open val keepAlwaysIfPossible = true

  /**
   * Keep milliseconds after onDestroyView
   */
  open val keepMs = 10000L

  private var refHost: WeakReference<Fragment>? = null

  protected val host: Fragment?
    get() = refHost?.get()

  protected val context: Context?
    get() = refHost?.get()?.context

  /**
   * Attach to [host]
   */
  internal fun attach(host: Fragment) {
    isActive = true
    refHost = WeakReference(host)
  }

  /**
   * Detach from host
   */
  internal fun detach() {
    inactiveMs = System.currentTimeMillis()
    isActive = false
  }

  /**
   * Clean required status
   */
  internal fun isCleanRequired(): Boolean {
    return !isActive && !keepAlwaysIfPossible && (System.currentTimeMillis() - inactiveMs) > keepMs
  }

  /**
   * 活动实例已创建回调
   */
  abstract fun onActivityCreated(view: View, lifecycle: Lifecycle, owner: LifecycleOwner)

  open fun onDestroy() {
    isActive = false
    refHost = null
  }
}