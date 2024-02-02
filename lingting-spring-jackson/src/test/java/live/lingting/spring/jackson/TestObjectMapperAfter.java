package live.lingting.spring.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author lingting 2024-02-02 16:00
 */
@Getter
@Component
public class TestObjectMapperAfter implements ObjectMapperAfter {

	private boolean after = false;

	@Override
	public void apply(ObjectMapper mapper) {
		assertFalse(after);
		after = true;
	}

}
