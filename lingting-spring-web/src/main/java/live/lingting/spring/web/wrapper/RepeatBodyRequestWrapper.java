package live.lingting.spring.web.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import live.lingting.framework.util.FileUtils;
import live.lingting.framework.util.StreamUtils;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lingting 2024-03-20 15:08
 */
public class RepeatBodyRequestWrapper extends HttpServletRequestWrapper implements Closeable {

	public static final File TMP_DIR = FileUtils.createTempDir("request");

	@Getter
	private final File bodyFile;

	private final Map<String, String[]> paramsMap;

	@Getter
	private final List<Closeable> closeableList;

	public RepeatBodyRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		paramsMap = request.getParameterMap();
		bodyFile = FileUtils.createTemp(".repeat", TMP_DIR);
		try (FileOutputStream outputStream = new FileOutputStream(bodyFile)) {
			StreamUtils.write(request.getInputStream(), outputStream);
		}
		closeableList = new ArrayList<>();
	}

	public static RepeatBodyRequestWrapper of(HttpServletRequest request) throws IOException {
		if (!(request instanceof RepeatBodyRequestWrapper)) {
			return new RepeatBodyRequestWrapper(request);
		}
		return (RepeatBodyRequestWrapper) request;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(bodyFile));
		closeableList.add(bufferedReader);
		return bufferedReader;
	}

	@Override
	@SuppressWarnings("java:S2095")
	public ServletInputStream getInputStream() throws IOException {
		FileInputStream stream = new FileInputStream(bodyFile);
		ServletInputStream servletInputStream = new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				//
			}

			@Override
			public int read() throws IOException {
				return stream.read();
			}

			@Override
			public void close() throws IOException {
				stream.close();
			}
		};
		closeableList.add(servletInputStream);
		return servletInputStream;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return paramsMap;
	}

	@Override
	public void close() throws IOException {
		for (Closeable closeable : getCloseableList()) {
			StreamUtils.close(closeable);
		}
		FileUtils.delete(getBodyFile());
	}

}
