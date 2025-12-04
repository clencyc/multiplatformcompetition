package org.example.kotlinconference101

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform