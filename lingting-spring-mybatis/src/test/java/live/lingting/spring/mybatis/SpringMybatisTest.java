package live.lingting.spring.mybatis;

import live.lingting.spring.mybatis.domain.Table;
import live.lingting.spring.mybatis.domain.User;
import live.lingting.spring.mybatis.domain.table.TableService;
import live.lingting.spring.mybatis.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author lingting 2024-03-12 14:26
 */
@SpringBootTest
class SpringMybatisTest {

	@Autowired
	UserService userService;

	@Autowired
	TableService tableService;

	@Test
	void test() {
		List<User> users = userService.list();
		assertFalse(users.isEmpty());
		List<Table> tables = tableService.list();
		assertFalse(tables.isEmpty());
	}

}
