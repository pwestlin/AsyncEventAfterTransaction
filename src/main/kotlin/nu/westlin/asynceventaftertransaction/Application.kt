package nu.westlin.asynceventaftertransaction

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@EnableAsync
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@RestController
@RequestMapping("/")
class Controller(
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    @GetMapping("/commit")
    fun commit() {
        eventPublisher.publishEvent(MyEvent("commit"))
    }

    @Transactional
    @GetMapping("/rollback")
    fun rollback() {
        eventPublisher.publishEvent(MyEvent("rollback"))
        throw RuntimeException("Hä gick på skit!")
    }

    @GetMapping("/none")
    fun none() {
        eventPublisher.publishEvent(MyEvent("none"))
    }
}

@Component
class MyEventSyncListener {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @EventListener
    fun onMyEvent(event: MyEvent) {
        logger.info("Fick event: $event")
    }
}

@Component
class MyEventAsyncListener {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onMyEvent(event: MyEvent) {
        logger.info("Fick event: $event")
    }
}

data class MyEvent(val function: String)