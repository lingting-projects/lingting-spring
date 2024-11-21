package live.lingting.spring.web.undertow

import io.undertow.servlet.spec.ServletContextImpl
import jakarta.servlet.ServletContext
import java.io.File
import live.lingting.framework.thread.AbstractTimer
import live.lingting.framework.util.FileUtils.createDir
import live.lingting.framework.util.StringUtils.hasText

/**
 * @author lingting 2024-03-20 16:01
 */
class UndertowTimer(context: ServletContext) : AbstractTimer() {
    private val file = getFile(context)

    override fun process() {
        try {
            if (file != null) {
                createDir(file)
            }
        } catch (_: Exception) {
            //
        }
    }

    protected fun getFile(context: ServletContext): File? {
        var dir: File? = null
        if (context is ServletContextImpl) {
            val deployment = context.deployment
            val deploymentInfo = deployment.deploymentInfo
            val config = deploymentInfo.defaultMultipartConfig
            if (config != null && hasText(config.location)) {
                dir = File(config.location)
            } else {
                dir = deploymentInfo.tempDir
            }
        }
        return dir
    }
}
