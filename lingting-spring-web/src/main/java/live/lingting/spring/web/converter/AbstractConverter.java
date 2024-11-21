package live.lingting.spring.web.converter;

import org.slf4j.Logger;
import org.springframework.core.convert.ConversionService;

/**
 * @author lingting 2024-03-20 15:51
 */
public abstract class AbstractConverter<T> implements Converter<T> {

	private final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	protected ConversionService service;

	public <E> E convert(Object source, Class<E> target) {
		if (source == null) {
			return null;
		}
		if (!service.canConvert(source.getClass(), target)) {
			log.debug("无法将类型[{}]转为[{}]", source.getClass(), target);
			return null;
		}
		return service.convert(source, target);
	}

	public void setService(ConversionService service) {
		this.service = service;
	}

}
