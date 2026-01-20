package com.project.lumipos

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform