package ru.lingstra.avitocopy.system

import android.content.Context
import java.io.InputStream
import javax.inject.Inject

class ResourceManager @Inject constructor(private val context: Context) {
    fun getString(resId: Int): String = context.getString(resId)
    fun getString(resId: Int, vararg formatArgs: Any?): String = context.getString(resId, *formatArgs)
    fun getAsset(name: String): InputStream = context.assets.open(name)
}