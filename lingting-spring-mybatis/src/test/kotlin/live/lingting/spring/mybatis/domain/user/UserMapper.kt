package live.lingting.spring.mybatis.domain.user

import live.lingting.framework.mybatis.extend.ExtendMapper
import live.lingting.spring.mybatis.domain.User
import org.apache.ibatis.annotations.Mapper

/**
 * @author lingting 2024-03-12 17:13
 */
@Mapper
interface UserMapper : ExtendMapper<User>
