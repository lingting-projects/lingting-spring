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
    private val file: File

    init {
        this.file = getFile(context)
    }


    override fun process() {
        try {
            createDir(file)
        } catch (e: Exception) {
            //
        }
    }

    protected fun getFile(context: ServletContext): File {
        var dir: File = null
        if (context is ServletContextImpl) {
            val deployment = context.getDeployment()
            val deploymentInfo = deployment.getDeploymentInfo()
            val config = deploymentInfo.getDefaultMultipartConfig()
            if (config != null && hasText(config.getLocation())) {
                dir = File(config.getLocation())
            } else {
                dir = deploymentInfo.getTempDir()
            }
        }
        return dir
    }
}
