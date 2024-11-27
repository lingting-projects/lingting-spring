package live.lingting.spring.web.undertow

import io.undertow.server.DefaultByteBufferPool
import io.undertow.servlet.api.DeploymentInfo
import io.undertow.websockets.jsr.WebSocketDeploymentInfo
import java.util.concurrent.ExecutorService
import live.lingting.framework.thread.ThreadPool
import live.lingting.framework.thread.VirtualThread
import live.lingting.framework.util.BooleanUtils.isTrue
import live.lingting.spring.util.EnvironmentUtils.getProperty
import live.lingting.spring.web.properties.SpringWebProperties
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer

/**
 * @author lingting 2024-03-20 16:01
 */
class UndertowWebCustomizer(private val properties: SpringWebProperties) : UndertowDeploymentInfoCustomizer {
    override fun customize(info: DeploymentInfo) {
        val websocket = websocket()
        info.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", websocket)
        val executor = executor()
        info.executor = executor
        info.asyncExecutor = executor
    }

    protected fun websocket(): WebSocketDeploymentInfo {
        val buffers = DefaultByteBufferPool(false, 2048, -1, 24, 0)
        val webSocketDeploymentInfo = WebSocketDeploymentInfo()
        webSocketDeploymentInfo.buffers = buffers
        return webSocketDeploymentInfo
    }

    protected fun executor(): ExecutorService {
        val property = getProperty("spring.threads.virtual.enabled")
        val enabled = property.isTrue()
        if (enabled && properties.useVirtualThread) {
            return VirtualThread.executor()
        }
        return ThreadPool.executor()
    }

}
