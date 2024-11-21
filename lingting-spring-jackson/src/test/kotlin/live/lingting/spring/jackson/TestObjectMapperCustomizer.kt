package live.lingting.spring.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * @author lingting 2024-02-02 15:59
 */
@Component
public class TestObjectMapperCustomizer implements ObjectMapperCustomizer {

	private ObjectMapper objectMapper;

	@Override
	public ObjectMapper apply(ObjectMapper mapper) {
		objectMapper = mapper.copy();
		return objectMapper;
	}

	public ObjectMapper getObjectMapper() {
		return this.objectMapper;
	}

}
