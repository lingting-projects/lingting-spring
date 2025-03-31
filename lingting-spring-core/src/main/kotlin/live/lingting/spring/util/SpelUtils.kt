package live.lingting.spring.util

import org.springframework.context.expression.MethodBasedEvaluationContext
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.core.ParameterNameDiscoverer
import org.springframework.expression.Expression
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import java.lang.reflect.Method

/**
 * @author Hccake
 * @version 1.0
 */
@Suppress("SpellCheckingInspection")
object SpelUtils {
    /**
     * SpEL 解析器
     */
    @JvmField
    val PARSER: ExpressionParser = SpelExpressionParser()

    /**
     * 方法参数获取
     */
    @JvmField
    val PARAMETER_NAME_DISCOVERER: ParameterNameDiscoverer = DefaultParameterNameDiscoverer()

    /**
     * 支持 #p0 参数索引的表达式解析
     * @param rootObject 根对象, method 所在类的对象实例
     * @param spelExpression spel 表达式
     * @param method 目标方法
     * @param args 方法入参
     * @return 解析后的字符串
     */
    @JvmStatic
    fun parseValueToString(rootObject: Any, method: Method, args: Array<Any>, spelExpression: String): String? {
        val context: StandardEvaluationContext = getSpelContext(rootObject, method, args)
        return parseValueToString(context, spelExpression)
    }

    /**
     * 支持 #p0 参数索引的表达式解析
     * @param rootObject 根对象, method 所在的对象
     * @param method 目标方法
     * @param args 方法实际入参
     * @return StandardEvaluationContext spel 上下文
     */
    @JvmStatic
    fun getSpelContext(rootObject: Any, method: Method, args: Array<Any>): StandardEvaluationContext {
        // spel 上下文
        val context: StandardEvaluationContext = MethodBasedEvaluationContext(
            rootObject, method, args,
            PARAMETER_NAME_DISCOVERER
        )
        // 方法参数名数组
        val parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method)
        // 把方法参数放入 spel 上下文中
        if (parameterNames != null && parameterNames.size > 0) {
            for (i in parameterNames.indices) {
                context.setVariable(parameterNames[i], args[i])
            }
        }
        return context
    }

    @JvmStatic
    fun parse(context: StandardEvaluationContext, spelExpression: String): Expression {
        return PARSER.parseExpression(spelExpression)
    }

    /**
     * 解析 spel 表达式
     * @param context spel 上下文
     * @param spelExpression spel 表达式
     * @return String 解析后的字符串
     */
    @JvmStatic
    fun parseValue(context: StandardEvaluationContext, spelExpression: String): Any? {
        return parse(context, spelExpression).getValue(context)
    }

    @JvmStatic
    fun parseValueToString(context: StandardEvaluationContext, spelExpression: String): String? {
        return parse(context, spelExpression).getValue(context, String::class.java)
    }

}

