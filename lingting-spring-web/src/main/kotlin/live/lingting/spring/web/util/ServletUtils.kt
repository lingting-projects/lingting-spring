package live.lingting.spring.web.util

import jakarta.servlet.http.HttpServletResponse
import live.lingting.framework.api.R
import live.lingting.framework.api.ResultCode
import live.lingting.framework.http.HttpContentTypes
import live.lingting.framework.http.header.HttpHeaderKeys
import live.lingting.framework.jackson.JacksonUtils

/**
 * @author lingting 2025/8/29 17:22
 */
object ServletUtils {

    fun HttpServletResponse.write(o: Any) {
        val str = if (o is String) {
            o
        } else {
            setHeader(HttpHeaderKeys.CONTENT_TYPE, HttpContentTypes.JSON_UTF8)
            JacksonUtils.toJson(o)
        }

        writer.write(str)
    }

    fun HttpServletResponse.write(code: ResultCode) {
        val r = R.failed<Unit>(code)
        write(r)
    }

    fun HttpServletResponse.write(status: Int, code: ResultCode) {
        this.status = status
        write(code)
    }

}
