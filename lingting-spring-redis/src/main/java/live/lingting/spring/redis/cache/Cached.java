package live.lingting.spring.redis.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author Hccake
 * @version 1.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cached {

	/**
	 * redis 存储的Key名
	 */
	String key();

	/**
	 * 如果需要在key 后面拼接参数 则传入一个拼接数据的 SpEL 表达式. 可以返回字符串或者数组或者集合
	 * <p>
	 * 如果返回数组获取集合则会按照顺序用分隔符拼接
	 * </p>
	 * <p>
	 * 示例: #p0: 返回第一个参数
	 * </p>
	 * <p>
	 * 示例: {#p0,#p1}: 返回数组: [参数1, 参数2]
	 * </p>
	 */
	String keyJoint() default "";

	/**
	 * 超时时间(S) ttl = 0 使用全局配置值 ttl < 0 : 不超时 ttl > 0 : 使用此超时间
	 */
	long ttl() default 0;

	/**
	 * 控制时长单位，默认为 SECONDS 秒
	 * @return {@link TimeUnit}
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

	/**
	 * 是否仅在缓存不存在时设置, 如果为false则会直接查询数据库并且设置到缓存
	 */
	boolean ifAbsent() default true;

}
