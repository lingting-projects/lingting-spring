package live.lingting.spring.security.password

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * @author lingting 2024-03-28 20:19
 */
class SecurityDefaultPasswordTest {
    var key: String = "==Lingting-Key=="

    var raw: String = "lingting"

    var password: SecurityDefaultPassword = SecurityDefaultPassword(key)

    @Test
    fun test() {
        assertTrue(password.valid(raw))
        val encodeFront = password.encodeFront(raw)
        println("encodeFront: $encodeFront")
        val decodeFront = password.decodeFront(encodeFront)
        println("decodeFront: $decodeFront")

        assertEquals(raw, decodeFront)
        val encode = password.encode(raw)
        println("encode: $encode")
        assertTrue(password.match(raw, encode))
    }
}
