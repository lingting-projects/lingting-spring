package live.lingting.spring.actuator.health

import java.io.File
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.Status
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator
import org.springframework.util.unit.DataSize

/**
 * @author lingting 2023-11-23 21:42
 */
class DiskSpaceReadableHealthIndicator
/**
 * Create a new `DiskSpaceHealthIndicator` instance.
 * @param path the Path used to compute the available disk space
 * @param threshold the minimum disk space that should be available
 */(private val path: File, private val threshold: DataSize) : DiskSpaceHealthIndicator(path, threshold) {

    override fun doHealthCheck(builder: Health.Builder) {
        val diskFreeInBytes = path.getUsableSpace()
        val isUp = diskFreeInBytes >= threshold.toBytes()

        val total = DataSize.ofBytes(path.getTotalSpace())
        val free = DataSize.ofBytes(diskFreeInBytes)

        builder.status(if (isUp) Status.UP else Status.DOWN)
            .withDetail("Total", to(total))
            .withDetail("Free", to(free))
            .withDetail("Threshold", to(threshold))
            .withDetail("Exists", path.exists())
    }

    fun to(size: DataSize): String {
        if (size.toBytes() < 1024) {
            val prefix = "B"
            return String.format("%d%s", size.toBytes(), prefix)
        } else if (size.toKilobytes() < 1024) {
            val prefix = "KB"
            return String.format("%d%s", size.toKilobytes(), prefix)
        } else if (size.toMegabytes() < 1024) {
            val prefix = "MB"
            return String.format("%d%s", size.toMegabytes(), prefix)
        } else if (size.toGigabytes() < 1024) {
            val prefix = "GB"
            return String.format("%d%s", size.toGigabytes(), prefix)
        } else {
            val prefix = "TB"
            return String.format("%d%s", size.toTerabytes(), prefix)
        }
    }

}
