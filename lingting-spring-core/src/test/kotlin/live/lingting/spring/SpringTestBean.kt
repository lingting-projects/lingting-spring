package live.lingting.spring;

import live.lingting.framework.context.ContextComponent;
import org.springframework.stereotype.Component;

/**
 * @author lingting 2024-02-02 15:27
 */
@Component
public class SpringTestBean implements ContextComponent {

	private final String value = "bean";

	private boolean start = false;

	@Override
	public void onApplicationStart() {
		start = true;
	}

	@Override
	public void onApplicationStop() {
		//
	}

	public String getValue() {
		return this.value;
	}

	public boolean isStart() {
		return this.start;
	}

}
