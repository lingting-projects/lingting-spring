package live.lingting.spring.web

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import live.lingting.framework.api.PaginationParams
import live.lingting.framework.api.R
import live.lingting.spring.web.scope.WebScope
import live.lingting.spring.web.scope.WebScopeHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author lingting 2024-03-20 17:29
 */
@RestController
class SpringTestController {
    @GetMapping("hello")
    fun hello(): R<Void> {
        return R.ok()
    }

    @GetMapping("pagination")
    fun pagination(pagination: PaginationParams): R<PaginationParams> {
        return R.ok(pagination)
    }

    @GetMapping("exception")
    fun exception(): R<Void> {
        throw IllegalStateException()
    }

    @GetMapping("validation")
    fun validation(@Valid p: P): R<P> {
        return R.ok(p)
    }

    @GetMapping("scope")
    fun scope(): R<Scope> {
        return R.ok(Scope())
    }

    class P {
        @NotEmpty(message = MESSAGE)
        var name: String = ""

        companion object {
            const val MESSAGE: String = "name must not be empty."
        }
    }

    class Scope(ws: WebScope? = WebScopeHolder.get()) {

        val host = ws?.host

        val uri = ws?.uri

        val traceId = ws?.traceId

        val requestId = ws?.requestId

    }

}
