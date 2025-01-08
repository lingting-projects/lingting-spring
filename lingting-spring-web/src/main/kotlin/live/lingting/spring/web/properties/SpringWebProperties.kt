package live.lingting.spring.web.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author lingting 2024-03-20 14:58
 */
@ConfigurationProperties(SpringWebProperties.PREFIX)
class SpringWebProperties {
    companion object {
        const val PREFIX: String = "lingting.web"
    }

    var headerTraceId: String = "X-Trace-Id"

    var headerRequestId: String = "X-Request-Id"

    var scopeFilterOrder: Int = Int.MIN_VALUE

    /**
     * 当spring启用虚拟线程时, web是否使用虚拟线程处理请求
     */
    var useVirtualThread: Boolean = true

    /**
     * 当上下文在关闭阶段时, 是否拒绝请求
     * @see live.lingting.framework.application.ApplicationHolder.isStop
     */
    var rejectRequestOnStop = true

    var pagination: Pagination = Pagination()

    class Pagination {
        var pageSize: Long = 10

        var maxPageSize: Long = 100

        var fieldSort: String = "sort"

        var fieldPage: String = "page"

        var fieldSize: String = "size"
    }

}
