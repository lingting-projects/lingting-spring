package live.lingting.spring.spi;

import live.lingting.framework.sensitive.SensitiveProvider;
import live.lingting.framework.sensitive.SensitiveUtils;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2024-05-21 11:34
 */
class SpiTest {

	@Test
	void test() {
		Optional<SensitiveProvider> sensitiveProviderOptional = SensitiveUtils.providers()
			.stream()
			.filter(p -> SensitiveSpringProvider.class.isAssignableFrom(p.getClass()))
			.findAny();
		assertTrue(sensitiveProviderOptional.isPresent());
	}

}
