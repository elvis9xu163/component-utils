package com.xjd.utils.component.spring.web.client;

import java.util.List;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.xjd.utils.component.spring.http.converter.MapFormHttpMessageConverter;
import com.xjd.utils.component.spring.http.converter.Utf8AllEncompassingFormHttpMessageConverter;

/**
 * AllEncompassingRestTemplate与原RestTemplate的区别在于:
 * 1. 前者能很好的支持文件名为非US-ASCII字符集的文件上传(使用UTF-8字符集)
 * 2. 前者支持MultipartFile类型的文件上传
 * 3. 前者支持把Map转换成表单
 * @author elvis.xu
 * @since 2017-08-16 14:21
 */
public class AllEncompassingRestTemplate extends RestTemplate{

	public AllEncompassingRestTemplate() {
		super();
		enhance(this);
	}

	public AllEncompassingRestTemplate(ClientHttpRequestFactory requestFactory) {
		super(requestFactory);
		enhance(this);
	}

	public AllEncompassingRestTemplate(List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
		enhance(this);
	}

	/**
	 * 增强一个RestTemplate对象，使其拥有AllEncompassingRestTemplate的功能
	 * @param restTemplate
	 * @return
	 */
	public static void enhance(RestTemplate restTemplate) {
		boolean utf8All = false, mapForm = false;
		for (int i = restTemplate.getMessageConverters().size() - 1; i >= 0; i--) {
			HttpMessageConverter<?> httpMessageConverter = restTemplate.getMessageConverters().get(i);
			if (httpMessageConverter instanceof FormHttpMessageConverter || httpMessageConverter instanceof AllEncompassingFormHttpMessageConverter) {
				restTemplate.getMessageConverters().remove(i);
			}
			if (httpMessageConverter instanceof Utf8AllEncompassingFormHttpMessageConverter) {
				utf8All = true;
			}
			if (httpMessageConverter instanceof MapFormHttpMessageConverter) {
				mapForm = true;
			}
		}
		if (!utf8All) {
			restTemplate.getMessageConverters().add(new Utf8AllEncompassingFormHttpMessageConverter());
		}
		if (!mapForm) {
			restTemplate.getMessageConverters().add(new MapFormHttpMessageConverter());
		}
	}
}
