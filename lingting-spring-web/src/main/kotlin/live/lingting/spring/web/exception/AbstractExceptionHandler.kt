package live.lingting.spring.web.exception

import live.lingting.framework.api.R
import live.lingting.framework.api.ResultCode
import live.lingting.framework.util.Slf4jUtils.logger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

/**
 * @author lingting 2024-03-29 11:10
 */
abstract class AbstractExceptionHandler {

    val log = logger()

    fun extract(code: ResultCode, e: Throwable): ResponseEntity<R<Unit>> {
        val message = if (e.message.isNullOrBlank()) code.i18nMessage() else e.message!!
        return extract(R.failed(code, message))
    }

    fun <T> extract(t: T): ResponseEntity<T> {
        return extract<T>(t, HttpStatus.OK)
    }

    fun <T> extract(t: T, status: HttpStatus): ResponseEntity<T> {
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body<T>(t)
    }

}
