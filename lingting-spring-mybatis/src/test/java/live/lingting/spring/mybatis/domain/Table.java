package live.lingting.spring.mybatis.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lingting 2024-03-16 17:44
 */
@Getter
@Setter
@TableName("tables_priv")
public class Table {

	@TableField("Db")
	private String db;

	@TableField("Table_name")
	private String name;

}
