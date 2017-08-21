package com.xjd.utils.component.http;

import javax.servlet.http.HttpServletRequest;

import com.xjd.utils.basic.StringUtils;

/**
 * @author elvis.xu
 * @since 2017-08-16 16:03
 */
public abstract class HttpUtils {
	/**
	 * 获取客户端真实IP(有代理的请况)
	 *
	 * @param request
	 * @return
	 */
	public static String getRealRemoteAddr(HttpServletRequest request) {
		String ip = StringUtils.trimToNull(request.getHeader("x-forwarded-for"));
		if (ip == null || "unknown".equalsIgnoreCase(ip)) {
			ip = StringUtils.trimToNull(request.getHeader("Proxy-Client-IP"));
		}
		if (ip == null || "unknown".equalsIgnoreCase(ip)) {
			ip = StringUtils.trimToNull(request.getHeader("WL-Proxy-Client-IP"));
		}
		if (ip == null || "unknown".equalsIgnoreCase(ip)) {
			ip = StringUtils.trimToNull(request.getRemoteAddr());
		}
		return ip;
	}
}
