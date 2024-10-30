package my.rudione.tranquility

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform