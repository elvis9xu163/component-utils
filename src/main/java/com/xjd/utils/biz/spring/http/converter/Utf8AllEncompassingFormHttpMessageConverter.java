package com.xjd.utils.biz.spring.http.converter;

import javax.xml.transform.Source;

import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;

/**
 * 该类从AllEncompassingFormHttpMessageConverter复制而来，修改父类为Utf8FormHttpMessageConverter，用以支持文件名非US-ASCII字符集的文件上传
 * 使用
 */
public class Utf8AllEncompassingFormHttpMessageConverter extends Utf8FormHttpMessageConverter {

	private static final boolean jaxb2Present =
			ClassUtils.isPresent("javax.xml.bind.Binder", Utf8AllEncompassingFormHttpMessageConverter.class.getClassLoader());

	private static final boolean jackson2Present =
			ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", Utf8AllEncompassingFormHttpMessageConverter.class.getClassLoader()) &&
					ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", Utf8AllEncompassingFormHttpMessageConverter.class.getClassLoader());

	private static final boolean jackson2XmlPresent =
			ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", Utf8AllEncompassingFormHttpMessageConverter.class.getClassLoader());

	private static final boolean gsonPresent =
			ClassUtils.isPresent("com.google.gson.Gson", Utf8AllEncompassingFormHttpMessageConverter.class.getClassLoader());


	public Utf8AllEncompassingFormHttpMessageConverter() {
		addPartConverter(new SourceHttpMessageConverter<Source>());

		if (jaxb2Present && !jackson2XmlPresent) {
			addPartConverter(new Jaxb2RootElementHttpMessageConverter());
		}

		if (jackson2Present) {
			addPartConverter(new MappingJackson2HttpMessageConverter());
		}
		else if (gsonPresent) {
			addPartConverter(new GsonHttpMessageConverter());
		}

		if (jackson2XmlPresent) {
			addPartConverter(new MappingJackson2XmlHttpMessageConverter());
		}
	}

}
