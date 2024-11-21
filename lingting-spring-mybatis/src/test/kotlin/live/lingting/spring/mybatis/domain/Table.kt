package live.lingting.spring.mybatis.domain

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName

/**
 * @author lingting 2024-03-16 17:44
 */
@TableName("tables_priv")
class Table {
    @TableField("Db")
    var db: String = null

    @TableField("Table_name")
    var name: String = null
}
