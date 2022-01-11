package com.banana.telescope

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TelescopeApplication

fun main(args: Array<String>) {
    runApplication<TelescopeApplication>(*args)
}
