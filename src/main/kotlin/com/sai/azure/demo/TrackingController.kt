package com.sai.azure.demo

import com.github.javafaker.Faker
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*
import java.util.stream.Stream

@RestController
class SimpleRoute {

    data class Tracking(val reg: String, val location: String, val timestamp: Long)

    val faker = Faker()

    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun index(): Map<String, String> {
        return mapOf("key" to UUID.randomUUID().toString())
    }

    @GetMapping("/api/v1/track", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun track(): Flux<Tracking> {
        val interval = Flux.interval(Duration.ofMillis(500))
        val events = Flux.fromStream { Stream.generate { sampleData() } }
        return Flux.zip(events, interval).map { it.t1 }
    }

    private fun sampleData(): Tracking =
            Tracking(faker.regexify("[A-Z1-9]{6}"), faker.address().fullAddress(), System.currentTimeMillis())
}