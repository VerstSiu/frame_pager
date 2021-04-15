package com.ijoic.frame_pager.instant

/**
 * Instant host
 *
 * @author verrstsiu created at 2021-04-15 09:43
 * @since 1.1.2
 */
interface InstantHost {
  /**
   * Returns instant clean required status
   */
  fun isInstantCleanRequired(): Boolean

  /**
   * Release instant view
   */
  fun onReleaseInstantView()
}