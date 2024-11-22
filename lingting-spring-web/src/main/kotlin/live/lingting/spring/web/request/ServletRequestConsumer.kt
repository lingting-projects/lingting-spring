package live.lingting.spring.web.request

import java.time.Duration
import live.lingting.spring.web.scope.WebScope
import live.lingting.spring.web.wrapper.RepeatBodyRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

/**
 * @author lingting 2024/11/22 14:24
 */
fun interface ServletRequestConsumer {

    /**
     * @param duration 耗时
     */
    fun consumer(duration: Duration, scope: WebScope, request: RepeatBodyRequestWrapper, response: ContentCachingResponseWrapper, t: Throwable?)

}
