package live.lingting.spring.web.undertow;

import io.undertow.servlet.api.Deployment;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.spec.ServletContextImpl;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletContext;
import live.lingting.framework.thread.AbstractTimer;
import live.lingting.framework.util.FileUtils;
import live.lingting.framework.util.StringUtils;

import java.io.File;

/**
 * @author lingting 2024-03-20 16:01
 */
public class UndertowTimer extends AbstractTimer {

	private final File file;

	public UndertowTimer(ServletContext context) {
		this.file = getFile(context);
	}

	@Override
	protected void process() throws Exception {
		try {
			FileUtils.createDir(file);
		}
		catch (Exception e) {
			//
		}
	}

	protected File getFile(ServletContext context) {
		File dir = null;
		if (context instanceof ServletContextImpl impl) {
			Deployment deployment = impl.getDeployment();
			DeploymentInfo deploymentInfo = deployment.getDeploymentInfo();
			MultipartConfigElement config = deploymentInfo.getDefaultMultipartConfig();
			if (config != null && StringUtils.hasText(config.getLocation())) {
				dir = new File(config.getLocation());
			}
			else {
				dir = deploymentInfo.getTempDir();
			}
		}
		return dir;
	}

}
