package live.lingting.spring.mybatis

import live.lingting.spring.mybatis.domain.Table
import live.lingting.spring.mybatis.domain.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author lingting 2024-03-12 14:26
 */
@SpringBootTest
class SpringMybatisTest {
    @Autowired
    var userService: UserService = null

    @Autowired
    var tableService: TableService = null

    @Test
    fun test() {
        val users: MutableList<User> = userService.list()
        Assertions.assertFalse(users.isEmpty())
        val tables: MutableList<Table> = tableService.list()
        Assertions.assertFalse(tables.isEmpty())
    }
}
