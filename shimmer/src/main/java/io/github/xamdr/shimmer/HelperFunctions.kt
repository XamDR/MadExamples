package io.github.xamdr.shimmer

import android.util.Log
import androidx.documentfile.provider.DocumentFile

val DocumentFile.simpleName: String?
	get() = this.name?.substringBeforeLast(".")

fun printError(tag: String, msg: Any?) = Log.e(tag, msg.toString())
