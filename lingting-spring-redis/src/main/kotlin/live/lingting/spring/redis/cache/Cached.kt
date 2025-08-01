package live.lingting.spring.redis.cache

import java.time.temporal.ChronoUnit

/**
 * 保存
 */
@MustBeDocumented
@JvmRepeatable(CachedBatch::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Cached(
    /**
     * redis 存储的Key名
     */
    val key: String,
    /**
     * 如果需要在key 后面拼接参数 则传入一个拼接数据的 SpEL 表达式. 可以返回字符串或者数组或者集合
     * 如果返回数组获取集合则会按照顺序用分隔符拼接
     * 示例: #p0: 返回第一个参数
     * 示例: {#p0,#p1}: 返回数组: [参数1, 参数2]
     */
    val keyJoint: String = "",
    /**
     * 是否依据keyJoint的值组装为多个key, 默认false, 会拼接位单个key
     */
    val keyMulti: Boolean = false,
    /**
     * 超时时间(S) ttl = 0 使用全局配置值 ttl < 0 : 不超时 ttl > 0 : 使用此超时间
     */
    val ttl: Long = 0,
    /**
     * 控制时长单位，默认为 SECONDS 秒
     */
    val ttlUnit: ChronoUnit = ChronoUnit.SECONDS,
    /**
     * 是否仅在缓存不存在时设置, 如果为false则会直接查询数据库并且设置到缓存
     */
    val ifAbsent: Boolean = true
)
