package live.lingting.spring.redis.cache

/**
 * 删除
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class CacheClearBatch(vararg val value: CacheClear)
