package live.lingting.spring.mybatis.domain.user;

import live.lingting.framework.mybatis.extend.ExtendServiceImpl;
import live.lingting.spring.mybatis.domain.User;
import org.springframework.stereotype.Service;

/**
 * @author lingting 2024-03-12 17:13
 */
@Service
public class UserServiceImpl extends ExtendServiceImpl<UserMapper, User> implements UserService {

}
