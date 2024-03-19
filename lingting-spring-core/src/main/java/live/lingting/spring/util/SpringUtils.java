package live.lingting.spring.util;

import jakarta.annotation.Resource;
import live.lingting.framework.domain.ClassField;
import live.lingting.framework.util.ClassUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author lingting 2022/10/15 15:21
 */
@UtilityClass
@SuppressWarnings("unchecked")
public class SpringUtils {

	@Setter
	@Getter
	private static ApplicationContext context;

	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}

	public static <T> T getBean(String name, Class<T> clazz) {
		return context.getBean(name, clazz);
	}

	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return context.getBeansOfType(type);
	}

	public static String[] getBeanNamesForType(Class<?> type) {
		return context.getBeanNamesForType(type);
	}

	public static <T> T ofBean(Class<T> cls)
			throws InvocationTargetException, InstantiationException, IllegalAccessException {
		Constructor<T>[] constructors = ClassUtils.constructors(cls);
		return ofBean(constructors[0], SpringUtils::getBean);
	}

	public static <T> T ofBean(Constructor<T> constructor, Function<Class<?>, Object> getArgument)
			throws InvocationTargetException, InstantiationException, IllegalAccessException {
		Class<?>[] types = constructor.getParameterTypes();
		List<Object> arguments = new ArrayList<>();

		for (Class<?> cls : types) {
			Object argument = getArgument.apply(cls);
			arguments.add(argument);
		}

		T t = constructor.newInstance(arguments.toArray());
		// 自动注入
		ClassField[] cfs = ClassUtils.classFields(t.getClass());
		for (ClassField cf : cfs) {
			if (!cf.hasField() || cf.isFinalField()) {
				continue;
			}
			boolean isAutowired = cf.getAnnotation(Autowired.class) != null || cf.getAnnotation(Resource.class) != null;
			if (isAutowired) {
				Class<?> cls = cf.getValueType();
				Object arg = getArgument.apply(cls);
				cf.set(t, arg);
			}
		}
		return t;
	}

}
