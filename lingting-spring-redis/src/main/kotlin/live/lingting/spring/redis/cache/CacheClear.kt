package live.lingting.spring.redis.cache

/**
 * @author Hccake
 * @version 1.0
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@JvmRepeatable(CacheBatchClear::class)
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheClear(
    /**
     * redis 存储的Key名
     */
    val key: String,
    /**
     * 如果需要在key 后面拼接参数 则传入一个拼接数据的 SpEL 表达式. 可以返回字符串或者数组或者集合
     * 如果返回数组获取集合则会按照顺序用分隔符拼接或者组装为多个key, 具体取决于 multi的值
     * 示例: #p0: 返回第一个参数
     * 示例: {#p0,#p1}: 返回数组: [参数1, 参数2]
     */
    val keyJoint: String = "",
    /**
     * 是否依据keyJoint的值组装为多个key, 默认false, 会拼接位单个key
     */
    val multi: Boolean = false
)
