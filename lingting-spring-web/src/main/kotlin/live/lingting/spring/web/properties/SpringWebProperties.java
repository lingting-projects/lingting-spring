package live.lingting.spring.web.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2024-03-20 14:58
 */
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

	public String getHeaderTraceId() {
		return this.headerTraceId;
	}

	public String getHeaderRequestId() {
		return this.headerRequestId;
	}

	public int getScopeFilterOrder() {
		return this.scopeFilterOrder;
	}

	public Boolean getUseVirtualThread() {
		return this.useVirtualThread;
	}

	public Pagination getPagination() {
		return this.pagination;
	}

	public void setHeaderTraceId(String headerTraceId) {
		this.headerTraceId = headerTraceId;
	}

	public void setHeaderRequestId(String headerRequestId) {
		this.headerRequestId = headerRequestId;
	}

	public void setScopeFilterOrder(int scopeFilterOrder) {
		this.scopeFilterOrder = scopeFilterOrder;
	}

	public void setUseVirtualThread(Boolean useVirtualThread) {
		this.useVirtualThread = useVirtualThread;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public static class Pagination {

		private long pageSize = 10;

		private long maxPageSize = 100;

		private String fieldSort = "sort";

		private String fieldPage = "page";

		private String fieldSize = "size";

		public long getPageSize() {
			return this.pageSize;
		}

		public long getMaxPageSize() {
			return this.maxPageSize;
		}

		public String getFieldSort() {
			return this.fieldSort;
		}

		public String getFieldPage() {
			return this.fieldPage;
		}

		public String getFieldSize() {
			return this.fieldSize;
		}

		public void setPageSize(long pageSize) {
			this.pageSize = pageSize;
		}

		public void setMaxPageSize(long maxPageSize) {
			this.maxPageSize = maxPageSize;
		}

		public void setFieldSort(String fieldSort) {
			this.fieldSort = fieldSort;
		}

		public void setFieldPage(String fieldPage) {
			this.fieldPage = fieldPage;
		}

		public void setFieldSize(String fieldSize) {
			this.fieldSize = fieldSize;
		}

	}

}
