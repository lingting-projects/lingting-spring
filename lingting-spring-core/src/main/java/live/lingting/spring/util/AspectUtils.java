package live.lingting.spring.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 切面工具类
 *
 * @author lingting 2022/10/28 15:03
 */
public final class AspectUtils {

	private AspectUtils() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	/**
	 * 获取切入的方法
	 * @param point 切面
	 * @return java.lang.reflect.Method
	 */
	public static Method getMethod(ProceedingJoinPoint point) {
		Signature signature = point.getSignature();
		if (signature instanceof MethodSignature ms) {
			return ms.getMethod();
		}
		return null;
	}

	/**
	 * 获取切入点方法上的注解, 找不到则往类上找
	 * @param point 切面
	 * @param cls 注解类型
	 * @return T 注解类型
	 */
	public static <T extends Annotation> T getAnnotation(ProceedingJoinPoint point, Class<T> cls) {
		Method method = getMethod(point);
		T t = null;
		if (method != null) {
			t = AnnotationUtils.findAnnotation(method, cls);
		}

		if (t == null) {
			t = AnnotationUtils.findAnnotation(point.getTarget().getClass(), cls);
		}
		return t;
	}

}
