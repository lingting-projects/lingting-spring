package live.lingting.spring.redis.cache

/**
 * 删除
 *
 * @author lishangbu
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheBatchClear(vararg val value: CacheClear)
