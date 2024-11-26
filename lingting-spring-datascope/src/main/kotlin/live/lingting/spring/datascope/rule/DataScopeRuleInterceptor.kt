package live.lingting.spring.datascope.rule

import live.lingting.framework.datascope.rule.DataScopeRule
import live.lingting.framework.datascope.rule.DataScopeRuleHolder
import live.lingting.framework.util.AnnotationUtils
import org.aopalliance.aop.Advice
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.aop.Pointcut
import org.springframework.aop.support.AbstractPointcutAdvisor
import org.springframework.aop.support.ComposablePointcut
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut

/**
 * @author lingting 2024/11/26 15:45
 */
class DataScopeRuleInterceptor : AbstractPointcutAdvisor(), MethodInterceptor {

    val p = AnnotationMatchingPointcut(DataScopeRule::class.java, true).let { cls ->
        val method = AnnotationMatchingPointcut(null, DataScopeRule::class.java)
        ComposablePointcut(cls).union(method)
    }

    override fun invoke(invocation: MethodInvocation): Any? {
        val method = invocation.method
        val rule = AnnotationUtils.findAnnotation(method, DataScopeRule::class.java)
        DataScopeRuleHolder.push(rule)
        try {
            return invocation.proceed()
        } finally {
            DataScopeRuleHolder.poll()
        }
    }


    override fun getAdvice(): Advice = this

    override fun getPointcut(): Pointcut = p
}
