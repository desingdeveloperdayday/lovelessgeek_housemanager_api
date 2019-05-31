package com.lovelessgeek.housemanager.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// All inputs: ms

fun Long.toSecond(): Int =
    this.div(1000).toInt()

fun Int.toMillis(): Long =
    this.times(1000).toLong()

fun Long.diff(current: Long = Date().time): Int =
    this.minus(current).toSecond()

fun Long.isOverDue(current: Long = Date().time): Boolean =
    diff(current) < 0

fun Long.lessThanOneDayLeft(current: Long = Date().time): Boolean =
    diff(current) in 1 until 86400

// TODO: 이쁘게 해주는 라이브러리 있음
fun Long.toReadableDateString(): String {
    val dateFormat = SimpleDateFormat("dd일", Locale.KOREA)
    return dateFormat.format(Date(this))
}