package com.rcl.nextshiki.elements

fun String.upper() = replaceFirstChar(Char::titlecase)
fun String.supper() = replaceFirstChar(Char::lowercase)