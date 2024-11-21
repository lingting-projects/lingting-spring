package live.lingting.spring.spi;

import live.lingting.framework.sensitive.Sensitive;
import live.lingting.framework.sensitive.SensitiveProvider;
import live.lingting.framework.sensitive.SensitiveSerializer;
import live.lingting.spring.util.SpringUtils;
import org.springframework.beans.BeansException;

/**
 * @author lingting 2024-05-21 11:30
 */
public class SensitiveSpringProvider implements SensitiveProvider {

	@Override
	public SensitiveSerializer find(Sensitive sensitive) {
		try {
			return SpringUtils.getBean(sensitive.value());
		}
		catch (BeansException e) {
			return null;
		}
	}

	@Override
	public int getSequence() {
		return 100;
	}

}
