package live.lingting.spring.web.resolve;

import jakarta.servlet.http.HttpServletRequest;
import live.lingting.framework.api.PaginationParams;
import live.lingting.framework.util.ArrayUtils;
import live.lingting.framework.util.StringUtils;
import live.lingting.spring.web.properties.SpringWebProperties;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.ValidationAnnotationUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author lingting 2024-03-20 16:06
 */
public class ApiPaginationParamsResolve implements HandlerMethodArgumentResolver {

	private final SpringWebProperties.Pagination pagination;

	public ApiPaginationParamsResolve(SpringWebProperties.Pagination pagination) {
		this.pagination = pagination;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return PaginationParams.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		PaginationParams params = resolve(request, parameter);
		validate(params, parameter, mavContainer, webRequest, binderFactory);
		return params;
	}

	public PaginationParams resolve(HttpServletRequest request, MethodParameter parameter) {
		if (request == null) {
			return new PaginationParams();
		}
		String pageParameterValue = request.getParameter(pagination.getFieldPage());
		String sizeParameterValue = request.getParameter(pagination.getFieldSize());

		PaginationParams params = instance(parameter);

		long pageValue = convert(pageParameterValue, 1);
		params.setPage(pageValue);

		long sizeValue = convert(sizeParameterValue, pagination.getPageSize());
		params.setSize(sizeValue);

		params.setSorts(sorts(request));
		return params;
	}

	protected PaginationParams instance(MethodParameter parameter) {
		try {
			return (PaginationParams) parameter.getParameterType().getDeclaredConstructor().newInstance();
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			return new PaginationParams();
		}
	}

	public List<PaginationParams.Sort> sorts(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		// 原始sort字符串
		List<String> strings = new ArrayList<>();
		// sort 可以传多个，所以同时支持 sort 和 sort[]
		String[] sortArray1 = parameterMap.get(pagination.getFieldSort());
		if (!ArrayUtils.isEmpty(sortArray1)) {
			strings.addAll(Arrays.asList(sortArray1));
		}
		String[] sortArray2 = parameterMap.get(pagination.getFieldSort() + "[]");
		if (!ArrayUtils.isEmpty(sortArray2)) {
			strings.addAll(Arrays.asList(sortArray2));
		}

		List<PaginationParams.Sort> sorts = new ArrayList<>();
		for (String string : strings) {
			if (!StringUtils.hasText(string)) {
				continue;
			}

			PaginationParams.Sort sort = sort(string);
			sorts.add(sort);
		}
		return sorts;
	}

	public PaginationParams.Sort sort(String raw) {
		String[] split = raw.split(",");
		String field = split[0];
		// 默认升序
		boolean isDesc = false;

		if (split.length > 1) {
			isDesc = split[1].equals("desc");
		}
		return new PaginationParams.Sort(field, isDesc);
	}

	public long convert(Object obj, long defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		try {
			return Long.parseLong(obj.toString());
		}
		catch (Exception e) {
			return defaultValue;
		}
	}

	public void validate(PaginationParams params, MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		if (binderFactory == null) {
			return;
		}
		String objectName = "paginationParams";
		WebDataBinder binder = binderFactory.createBinder(webRequest, params, objectName);
		validationAnnotation(parameter, binder);
		BindingResult bindingResult = binder.getBindingResult();

		long size = params.getSize();
		if (size > pagination.getMaxPageSize()) {
			bindingResult.addError(
					new ObjectError("size", "Pagination size cannot be greater than " + pagination.getMaxPageSize()));
		}

		if (bindingResult.hasErrors() && isBindExceptionRequired(parameter)) {
			throw new MethodArgumentNotValidException(parameter, bindingResult);
		}
		if (mavContainer != null) {
			mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + objectName, bindingResult);
		}
	}

	void validationAnnotation(MethodParameter parameter, WebDataBinder binder) {
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation ann : annotations) {
			Object[] validationHints = ValidationAnnotationUtils.determineValidationHints(ann);
			if (validationHints != null) {
				binder.validate(validationHints);
				break;
			}
		}
	}

	protected boolean isBindExceptionRequired(MethodParameter parameter) {
		int i = parameter.getParameterIndex();
		Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
		return !hasBindingResult;
	}

}
