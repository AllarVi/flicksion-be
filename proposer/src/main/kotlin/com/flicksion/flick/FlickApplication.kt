package com.flicksion.flick

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlickApplication

fun main(args: Array<String>) {
    runApplication<FlickApplication>(*args)
}
