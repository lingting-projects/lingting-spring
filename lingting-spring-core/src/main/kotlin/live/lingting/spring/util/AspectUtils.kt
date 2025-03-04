package live.lingting.spring.util

import java.lang.reflect.Method
import live.lingting.framework.util.AnnotationUtils
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.reflect.MethodSignature

/**
 * 切面工具类
 * @author lingting 2022/10/28 15:03
 */
object AspectUtils {

    /**
     * 获取切入的方法
     * @param point 切面
     * @return java.lang.reflect.Method
     */
    @JvmStatic
    fun findMethod(point: JoinPoint): Method? {
        val signature = point.signature
        if (signature is MethodSignature) {
            return signature.method
        }
        return null
    }

    /**
     * 获取切入点方法上的注解, 找不到则往类上找
     * @param point 切面
     * @param cls 注解类型
     * @return T 注解类型
     */
    @JvmStatic
    fun <T : Annotation> findAnnotation(point: JoinPoint, cls: Class<T>): T? {
        val method = findMethod(point)
        var t: T? = null
        if (method != null) {
            t = AnnotationUtils.findAnnotation<T>(method, cls)
        }

        if (t == null) {
            t = AnnotationUtils.findAnnotation<T>(point.target.javaClass, cls)
        }
        return t
    }

}

