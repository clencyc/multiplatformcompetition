package org.example.kotlinconference101

import android.content.Context

/**
 * Simple context holder so platform services that need a Context can be constructed
 * from common code without threading it through every call.
 */
object AndroidContextHolder {
    lateinit var context: Context
        private set

    fun set(context: Context) {
        this.context = context.applicationContext
    }
}
