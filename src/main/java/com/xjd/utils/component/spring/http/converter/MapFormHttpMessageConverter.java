package com.xjd.utils.component.spring.http.converter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 支持Map的FormHttpMessageConverter
 * @author elvis.xu
 * @since 2017-05-09 10:58
 */
public class MapFormHttpMessageConverter implements HttpMessageConverter<Map<String, ?>> {

	protected Utf8FormHttpMessageConverter formHttpMessageConverter;

	public MapFormHttpMessageConverter() {
		this.formHttpMessageConverter = new Utf8AllEncompassingFormHttpMessageConverter();
	}

	/**
	 * Set the list of {@link MediaType} objects supported by this converter.
	 */
	public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
		this.formHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		return formHttpMessageConverter.getSupportedMediaTypes();
	}

	/**
	 * Set the message body converters to use. These converters are used to
	 * convert objects to MIME parts.
	 */
	public void setPartConverters(List<HttpMessageConverter<?>> partConverters) {
		this.formHttpMessageConverter.setPartConverters(partConverters);
	}

	/**
	 * Add a message body converter. Such a converter is used to convert objects
	 * to MIME parts.
	 */
	public void addPartConverter(HttpMessageConverter<?> partConverter) {
		this.formHttpMessageConverter.addPartConverter(partConverter);
	}

	public void setCharset(Charset charset) {
		this.formHttpMessageConverter.setCharset(charset);
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		if (!Map.class.isAssignableFrom(clazz)) {
			return false;
		}
		if (mediaType == null) {
			return true;
		}
		for (MediaType supportedMediaType : getSupportedMediaTypes()) {
			// We can't read multipart....
			if (!supportedMediaType.equals(MediaType.MULTIPART_FORM_DATA) && supportedMediaType.includes(mediaType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		if (!Map.class.isAssignableFrom(clazz)) {
			return false;
		}
		if (mediaType == null || MediaType.ALL.equals(mediaType)) {
			return true;
		}
		for (MediaType supportedMediaType : getSupportedMediaTypes()) {
			if (supportedMediaType.isCompatibleWith(mediaType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Map<String, ?> read(Class<? extends Map<String, ?>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		 return formHttpMessageConverter.read(null, inputMessage);
	}

	public void write(Map<String, ?> map, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		MultiValueMap<String, Object> multiMap = null;
		if (map != null) {
			if (map instanceof MultiValueMap) {
				multiMap = (MultiValueMap<String, Object>) map;
			} else {
				multiMap = new LinkedMultiValueMap<>();
				for (Map.Entry<String, ?> entry : map.entrySet()) {
					multiMap.add(entry.getKey(), entry.getValue());
				}
			}
		}
		formHttpMessageConverter.write(multiMap, contentType, outputMessage);
	}

}
