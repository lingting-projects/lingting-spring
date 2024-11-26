package live.lingting.spring.web.wrapper

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.BufferedReader
import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import live.lingting.framework.util.FileUtils.createTemp
import live.lingting.framework.util.FileUtils.createTempDir
import live.lingting.framework.util.FileUtils.delete
import live.lingting.framework.util.StreamUtils.close
import live.lingting.framework.util.StreamUtils.write

/**
 * @author lingting 2024-03-20 15:08
 */
class RepeatBodyRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request), Closeable {
    val bodyFile: File = createTemp(".repeat", TMP_DIR)

    private val paramsMap: MutableMap<String, Array<String>> = request.parameterMap

    val closeableList: MutableList<Closeable>

    init {
        FileOutputStream(bodyFile).use { outputStream ->
            write(request.inputStream, outputStream)
        }
        closeableList = ArrayList<Closeable>()
    }

    override fun getReader(): BufferedReader {
        val bufferedReader = BufferedReader(FileReader(bodyFile))
        closeableList.add(bufferedReader)
        return bufferedReader
    }

    override fun getInputStream(): ServletInputStream {
        val stream = FileInputStream(bodyFile)
        val servletInputStream: ServletInputStream = object : ServletInputStream() {
            override fun isFinished(): Boolean {
                return false
            }

            override fun isReady(): Boolean {
                return false
            }

            override fun setReadListener(readListener: ReadListener) {
                //
            }

            override fun read(): Int {
                return stream.read()
            }

            override fun close() {
                stream.close()
            }
        }
        closeableList.add(servletInputStream)
        return servletInputStream
    }

    override fun getParameterMap(): MutableMap<String, Array<String>> {
        return paramsMap
    }

    override fun close() {
        for (closeable in this.closeableList) {
            close(closeable)
        }
        delete(this.bodyFile)
    }

    companion object {
        @JvmField
        val TMP_DIR: File = createTempDir("request")

        @JvmStatic
        fun of(request: HttpServletRequest): RepeatBodyRequestWrapper {
            if (request !is RepeatBodyRequestWrapper) {
                return RepeatBodyRequestWrapper(request)
            }
            return request
        }
    }
}
