package live.lingting.spring.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @author lingting 2024-02-02 15:59
 */
@Getter
@Component
public class TestObjectMapperCustomizer implements ObjectMapperCustomizer {

	private ObjectMapper objectMapper;

	@Override
	public ObjectMapper apply(ObjectMapper mapper) {
		objectMapper = mapper.copy();
		return objectMapper;
	}

}
