package com.ijoic.frame_pager

import java.lang.ref.WeakReference

/**
 * lazy delegate.
 *
 * @author verstsiu@126.com on 2018/4/20.
 * @version 1.0
 */
class LazyDelegate(callback: Callback) {

  private val refCallback = WeakReference(callback)

  /**
   * Lazy callback.
   */
  interface Callback {
    /**
     * Returns user visible status.
     */
    fun getUserVisibleHint(): Boolean

    /**
     * Returns resume status.
     */
    fun isResumed(): Boolean

    /**
     * Lazy resume.
     */
    fun onLazyResume()

    /**
     * Lazy pause.
     */
    fun onLazyPause()
  }

  private var lazyResumeInit = false
  private var lazyPauseInit = false

  /**
   * Resume.
   */
  fun onResume() {
    val callback = refCallback.get() ?: return
    lazyPauseInit = false

    if (callback.getUserVisibleHint() && !lazyResumeInit) {
      lazyResumeInit = true
      callback.onLazyResume()
    }
  }

  /**
   * Pause.
   */
  fun onPause() {
    val callback = refCallback.get() ?: return
    lazyResumeInit = false

    if (callback.getUserVisibleHint() && !lazyPauseInit) {
      lazyPauseInit = true
      callback.onLazyPause()
    }
  }

  /**
   * Set user visible hint.
   *
   * @param isVisibleToUser visible status.
   */
  fun setUserVisibleHint(isVisibleToUser: Boolean) {
    val callback = refCallback.get() ?: return
    if (!callback.isResumed()) {
      return
    }

    if (isVisibleToUser) {
      lazyPauseInit = false

      if (!lazyPauseInit) {
        lazyResumeInit = true
        callback.onLazyResume()
      }
    } else {
      lazyResumeInit = false

      if (!lazyPauseInit) {
        lazyPauseInit = true
        callback.onLazyPause()
      }
    }
  }

}