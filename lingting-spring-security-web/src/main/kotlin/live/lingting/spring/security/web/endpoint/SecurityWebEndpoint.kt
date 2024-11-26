package live.lingting.spring.security.web.endpoint

import live.lingting.framework.security.SecurityEndpointService
import live.lingting.framework.security.annotation.Authorize
import live.lingting.framework.security.domain.AuthorizationVO
import live.lingting.framework.security.po.EndpointPasswordPO
import live.lingting.framework.security.po.EndpointTokenPO
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

/**
 * @author lingting 2024-03-21 19:31
 */
@Authorize
@ResponseBody
class SecurityWebEndpoint(
    val service: SecurityEndpointService,
) {
    @DeleteMapping("authorization/logout")
    fun logout(): AuthorizationVO {
        return service.logout()
    }

    @Authorize(anyone = true)
    @GetMapping("authorization/password")
    fun password(po: EndpointPasswordPO): AuthorizationVO {
        return service.password(po)
    }

    @Authorize(anyone = true)
    @GetMapping("authorization/refresh")
    fun refresh(po: EndpointTokenPO): AuthorizationVO {
        return service.refresh(po)
    }

    @GetMapping("authorization/resolve")
    fun resolve(po: EndpointTokenPO): AuthorizationVO {
        return service.resolve(po)
    }

}
