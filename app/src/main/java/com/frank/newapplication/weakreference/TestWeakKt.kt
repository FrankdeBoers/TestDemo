package com.frank.newapplication.weakreference

import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

fun main() {
    val referenceQueue = ReferenceQueue<Any?>()
    var target: Any? = Object()
    val weakReference = WeakReference(target, referenceQueue)
    println("ReferenceQueue before target:${weakReference.get()}")
//    target = null
    System.gc()
    Thread.sleep(2000)
    println("ReferenceQueue after target:${weakReference.get()}")
    var queueRef: Any? = null
    do {
        queueRef = referenceQueue.poll()
        println("ReferenceQueue queueRef queueRef:$queueRef, weakReference:$weakReference")
    } while (queueRef != null)
}