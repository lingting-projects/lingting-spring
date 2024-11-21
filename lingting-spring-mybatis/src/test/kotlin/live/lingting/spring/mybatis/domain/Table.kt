package live.lingting.spring.mybatis.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author lingting 2024-03-16 17:44
 */
@TableName("tables_priv")
public class Table {

	@TableField("Db")
	private String db;

	@TableField("Table_name")
	private String name;

	public String getDb() {
		return this.db;
	}

	public String getName() {
		return this.name;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public void setName(String name) {
		this.name = name;
	}

}
