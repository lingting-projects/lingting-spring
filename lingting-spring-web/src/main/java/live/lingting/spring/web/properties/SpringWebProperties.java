package live.lingting.spring.web.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2024-03-20 14:58
 */
@Getter
@Setter
@ConfigurationProperties(SpringWebProperties.PREFIX)
public class SpringWebProperties {

	public static final String PREFIX = "lingting.web";

	private String headerTraceId = "X-Trace-Id";

	private String headerRequestId = "X-Request-Id";

	private int scopeFilterOrder = Integer.MIN_VALUE;

	/**
	 * 当spring启用虚拟线程时, web是否使用虚拟线程处理请求
	 */
	private Boolean useVirtualThread = true;

	private Pagination pagination = new Pagination();

	@Getter
	@Setter
	public static class Pagination {

		private long pageSize = 10;

		private long maxPageSize = 100;

		private String fieldSort = "sort";

		private String fieldPage = "page";

		private String fieldSize = "size";

	}

}
