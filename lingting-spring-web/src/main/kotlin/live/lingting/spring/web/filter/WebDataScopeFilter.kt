package live.lingting.spring.web.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import live.lingting.framework.datascope.rule.DataScopeRuleHolder
import org.springframework.web.filter.OncePerRequestFilter

/**
 * @author lingting 2025/8/18 16:58
 */
class WebDataScopeFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } finally {
            DataScopeRuleHolder.clear()
        }
    }

}
