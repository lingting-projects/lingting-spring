package live.lingting.spring.actuator.health

import java.io.File
import live.lingting.framework.data.DataSize
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.Status
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator

/**
 * @author lingting 2023-11-23 21:42
 */
class DiskSpaceReadableHealthIndicator
/**
 * Create a new `DiskSpaceHealthIndicator` instance.
 * @param path the Path used to compute the available disk space
 * @param threshold the minimum disk space that should be available
 */(
    private val path: File,
    private val threshold: DataSize,
) : DiskSpaceHealthIndicator(path, org.springframework.util.unit.DataSize.ofBytes(threshold.bytes)) {

    override fun doHealthCheck(builder: Health.Builder) {
        val diskFreeInBytes = path.getUsableSpace()
        val isUp = diskFreeInBytes >= threshold.bytes

        val total = DataSize.ofBytes(path.getTotalSpace())
        val free = DataSize.ofBytes(diskFreeInBytes)

        builder.status(if (isUp) Status.UP else Status.DOWN)
            .withDetail("Total", total)
            .withDetail("Free", free)
            .withDetail("Threshold", threshold)
            .withDetail("Exists", path.exists())
    }

}
