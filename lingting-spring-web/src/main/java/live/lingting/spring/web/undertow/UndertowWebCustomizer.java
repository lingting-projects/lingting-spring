package live.lingting.spring.web.undertow;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import live.lingting.framework.thread.VirtualThread;
import live.lingting.framework.util.BooleanUtils;
import live.lingting.spring.util.EnvironmentUtils;
import live.lingting.spring.web.properties.SpringWebProperties;
import org.slf4j.Logger;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;

import java.util.concurrent.ExecutorService;

/**
 * @author lingting 2024-03-20 16:01
 */
public class UndertowWebCustomizer implements UndertowDeploymentInfoCustomizer {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(UndertowWebCustomizer.class);

	private final SpringWebProperties properties;

	public UndertowWebCustomizer(SpringWebProperties properties) {
		this.properties = properties;
	}

	@Override
	public void customize(DeploymentInfo info) {
		WebSocketDeploymentInfo websocket = websocket();
		info.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", websocket);
		ExecutorService executor = executor();
		if (executor != null) {
			info.setExecutor(executor);
			info.setAsyncExecutor(executor);
		}
	}

	protected WebSocketDeploymentInfo websocket() {
		DefaultByteBufferPool buffers = new DefaultByteBufferPool(false, 2048, -1, 24, 0);

		WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
		webSocketDeploymentInfo.setBuffers(buffers);
		return webSocketDeploymentInfo;
	}

	protected ExecutorService executor() {
		String property = EnvironmentUtils.getProperty("spring.threads.virtual.enabled");
		boolean enabled = BooleanUtils.isTrue(property);
		return enabled && properties.getUseVirtualThread() ? VirtualThread.executor() : null;
	}

}
