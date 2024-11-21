package live.lingting.spring.mybatis.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * @author lingting 2024-03-12 14:26
 */
@TableName("user")
public class User {

	@TableField("Host")
	private String host;

	@TableField("User")
	private String username;

	@TableField("password_last_changed")
	private LocalDateTime updateTime;

	public String getHost() {
		return this.host;
	}

	public String getUsername() {
		return this.username;
	}

	public LocalDateTime getUpdateTime() {
		return this.updateTime;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

}
