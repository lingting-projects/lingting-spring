package live.lingting.spring.mybatis.domain.table

import live.lingting.framework.mybatis.extend.ExtendMapper
import live.lingting.spring.mybatis.domain.Table
import org.apache.ibatis.annotations.Mapper

/**
 * @author lingting 2024-03-12 17:13
 */
@Mapper
interface TableMapper : ExtendMapper<Table>
