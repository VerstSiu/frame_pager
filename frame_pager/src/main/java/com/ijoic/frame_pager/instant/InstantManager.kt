package com.ijoic.frame_pager.instant

import kotlinx.coroutines.*
import java.lang.ref.WeakReference

/**
 * Instant manager
 *
 * @author verstsiu created at 2021-04-15 09:37
 * @since 1.1.2
 */
object InstantManager {

  private val refItems = mutableListOf<WeakReference<InstantHost>>()
  private val editLock = Object()

  private val scope = CoroutineScope(Dispatchers.Main.immediate)
  private var checkJob: Job? = null

  private const val CHECK_INTERVAL_MS = 5000L

  fun add(host: InstantHost) {
    synchronized(editLock) {
      refItems.add(WeakReference(host))
      prepareCheckTask()
    }
  }

  private fun prepareCheckTask() {
    val currentJob = checkJob
    if (currentJob != null && currentJob.isActive) {
      return
    }
    checkJob = scope.launch(Dispatchers.IO) {
      while (true) {
        delay(CHECK_INTERVAL_MS)
        trimUnusedRefItems()
      }
    }
  }

  private fun trimUnusedRefItems() {
    synchronized(editLock) {
      val removedItems = mutableListOf<WeakReference<InstantHost>>()
      for (ref in refItems) {
        val host = ref.get()

        if (!isKeepRequired(host)) {
          removedItems.add(ref)
        }
      }
      if (removedItems.isNotEmpty()) {
        refItems.removeAll(removedItems)
      }
    }
  }

  private fun isKeepRequired(host: InstantHost?): Boolean {
    if (host == null) {
      return false
    }
    if (host.isInstantCleanRequired()) {
      host.onReleaseInstantView()
      return false
    }
    return true
  }

}