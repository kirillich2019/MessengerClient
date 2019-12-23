package com.wg.messengerclient

import kotlinx.coroutines.*


fun sort(head: LinkedListNode, value: Int): LinkedListNode {
    var lessFirst: LinkedListNode? = null
    var moreOrEqualFirst: LinkedListNode? = null
    var less: LinkedListNode? = null
    var moreOrEqual: LinkedListNode? = null
    var first = head
    while (true) {
        if (first.value < value) {
            if (less == null) {
                lessFirst = first
            }
            less?.next = first
            less = first
        } else {
            if (moreOrEqual == null) {
                moreOrEqualFirst = first
            }
            moreOrEqual?.next = first
            moreOrEqual = first
        }
        first = first.next ?: break
    }

    moreOrEqual?.next = null
    if (lessFirst == null) {
        return moreOrEqualFirst!!
    } else {
        less!!.next = moreOrEqualFirst
        return lessFirst
    }
}
