package org.jboycode

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform