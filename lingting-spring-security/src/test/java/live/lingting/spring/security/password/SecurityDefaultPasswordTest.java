package live.lingting.spring.security.password;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2024-03-28 20:19
 */
class SecurityDefaultPasswordTest {

	String key = "==Lingting-Key==";

	String raw = "lingting";

	SecurityDefaultPassword password = new SecurityDefaultPassword(key);

	@Test
	void test() {
		assertTrue(password.valid(raw));
		String encodeFront = password.encodeFront(raw);
		System.out.println("encodeFront: " + encodeFront);
		String decodeFront = password.decodeFront(encodeFront);
		System.out.println("decodeFront: " + decodeFront);

		assertEquals(raw, decodeFront);
		String encode = password.encode(raw);
		System.out.println("encode: " + encode);
		assertTrue(password.match(raw, encode));
	}

}
