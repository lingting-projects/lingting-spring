package live.lingting.spring.web.resolve

import jakarta.servlet.http.HttpServletRequest
import java.lang.reflect.InvocationTargetException
import java.util.Arrays
import live.lingting.framework.api.PaginationParams
import live.lingting.framework.util.ArrayUtils.isEmpty
import live.lingting.framework.util.StringUtils.hasText
import live.lingting.spring.web.properties.SpringWebProperties.Pagination
import org.springframework.core.MethodParameter
import org.springframework.validation.BindingResult
import org.springframework.validation.Errors
import org.springframework.validation.ObjectError
import org.springframework.validation.annotation.ValidationAnnotationUtils
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * @author lingting 2024-03-20 16:06
 */
class ApiPaginationParamsResolve(private val pagination: Pagination) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return PaginationParams::class.java.isAssignableFrom(parameter.getParameterType())
    }


    override fun resolveArgument(
        parameter: MethodParameter, mavContainer: ModelAndViewContainer,
        webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory
    ): Any {
        val request = webRequest.getNativeRequest<HttpServletRequest>(HttpServletRequest::class.java)
        val params = resolve(request, parameter!!)
        validate(params, parameter, mavContainer, webRequest, binderFactory)
        return params
    }

    fun resolve(request: HttpServletRequest, parameter: MethodParameter): PaginationParams {
        if (request == null) {
            return PaginationParams()
        }
        val pageParameterValue = request.getParameter(pagination.getFieldPage())
        val sizeParameterValue = request.getParameter(pagination.getFieldSize())

        val params = instance(parameter)

        val pageValue = convert(pageParameterValue, 1)
        params.page = pageValue

        val sizeValue = convert(sizeParameterValue, pagination.getPageSize())
        params.size = sizeValue

        params.sorts = sorts(request)
        return params
    }

    protected fun instance(parameter: MethodParameter): PaginationParams {
        try {
            return parameter.getParameterType().getDeclaredConstructor().newInstance() as PaginationParams
        } catch (e: InstantiationException) {
            return PaginationParams()
        } catch (e: IllegalAccessException) {
            return PaginationParams()
        } catch (e: InvocationTargetException) {
            return PaginationParams()
        } catch (e: NoSuchMethodException) {
            return PaginationParams()
        }
    }

    fun sorts(request: HttpServletRequest): MutableList<PaginationParams.Sort> {
        val parameterMap = request.getParameterMap()
        // 原始sort字符串
        val strings: MutableList<String> = ArrayList<String>()
        // sort 可以传多个，所以同时支持 sort 和 sort[]
        val sortArray1: Array<String> = parameterMap.get(pagination.getFieldSort())!!
        if (!isEmpty<String>(sortArray1)) {
            strings.addAll(Arrays.asList<String>(*sortArray1))
        }
        val sortArray2: Array<String> = parameterMap.get(pagination.getFieldSort() + "[]")!!
        if (!isEmpty<String>(sortArray2)) {
            strings.addAll(Arrays.asList<String>(*sortArray2))
        }

        val sorts: MutableList<PaginationParams.Sort> = ArrayList<PaginationParams.Sort>()
        for (string in strings) {
            if (!hasText(string)) {
                continue
            }

            val sort = sort(string!!)
            sorts.add(sort)
        }
        return sorts
    }

    fun sort(raw: String): PaginationParams.Sort {
        val split = raw.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val field = split[0]
        // 默认升序
        var isDesc = false

        if (split.size > 1) {
            isDesc = split[1] == "desc"
        }
        return PaginationParams.Sort(field, isDesc)
    }

    fun convert(obj: Any, defaultValue: Long): Long {
        if (obj == null) {
            return defaultValue
        }
        try {
            return obj.toString().toLong()
        } catch (e: Exception) {
            return defaultValue
        }
    }


    fun validate(
        params: PaginationParams, parameter: MethodParameter, mavContainer: ModelAndViewContainer,
        webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory
    ) {
        if (binderFactory == null) {
            return
        }
        val objectName = "paginationParams"
        val binder = binderFactory.createBinder(webRequest, params, objectName)
        validationAnnotation(parameter, binder)
        val bindingResult = binder.getBindingResult()

        val size = params.size
        if (size > pagination.getMaxPageSize()) {
            bindingResult.addError(
                ObjectError("size", "Pagination size cannot be greater than " + pagination.getMaxPageSize())
            )
        }

        if (bindingResult.hasErrors() && isBindExceptionRequired(parameter)) {
            throw MethodArgumentNotValidException(parameter, bindingResult)
        }
        if (mavContainer != null) {
            mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + objectName, bindingResult)
        }
    }

    fun validationAnnotation(parameter: MethodParameter, binder: WebDataBinder) {
        val annotations = parameter.getParameterAnnotations()
        for (ann in annotations) {
            val validationHints = ValidationAnnotationUtils.determineValidationHints(ann)
            if (validationHints != null) {
                binder.validate(*validationHints)
                break
            }
        }
    }

    protected fun isBindExceptionRequired(parameter: MethodParameter): Boolean {
        val i = parameter.getParameterIndex()
        val paramTypes = parameter.getExecutable().getParameterTypes()
        val hasBindingResult = (paramTypes.size > (i + 1) && Errors::class.java.isAssignableFrom(paramTypes[i + 1]))
        return !hasBindingResult
    }
}
