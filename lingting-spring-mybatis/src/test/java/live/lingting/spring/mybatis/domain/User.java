package live.lingting.spring.mybatis.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author lingting 2024-03-12 14:26
 */
@Getter
@Setter
@TableName("user")
public class User {

	@TableField("Host")
	private String host;

	@TableField("User")
	private String username;

	@TableField("password_last_changed")
	private LocalDateTime updateTime;

}
