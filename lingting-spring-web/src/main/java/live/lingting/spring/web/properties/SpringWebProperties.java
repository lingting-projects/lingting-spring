package live.lingting.spring.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2024-03-20 14:58
 */
@Data
@ConfigurationProperties(SpringWebProperties.PREFIX)
public class SpringWebProperties {

	public static final String PREFIX = "lingting.web";

	private String headerTraceId = "X-Trace-Id";

	private String headerRequestId = "X-Request-Id";

	private int scopeFilterOrder = Integer.MIN_VALUE;

	private Pagination pagination = new Pagination();

	@Data
	public static class Pagination {

		private long pageSize = 10;

		private long maxPageSize = 100;

		private String fieldSort = "sort";

		private String fieldPage = "page";

		private String fieldSize = "size";

	}

}
