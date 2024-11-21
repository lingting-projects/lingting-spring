package live.lingting.spring.mybatis.domain

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

/**
 * @author lingting 2024-03-12 14:26
 */
@TableName("user")
class User {
    @TableField("Host")
    var host: String = null

    @TableField("User")
    var username: String = null

    @TableField("password_last_changed")
    var updateTime: LocalDateTime = null
}
