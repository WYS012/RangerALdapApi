package com.bms.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author YeChunBo
 * @time 2017��9��8��
 *
 *       ��˵��: ip ��������ֻ���������ļ��ж����˵�ip �ſ��Է��ʽӿ�
 */

public class URLInterceptor implements HandlerInterceptor {

	private static final Logger logger = Logger.getLogger(URLInterceptor.class);

	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * ��������֮ǰ���е��ã�Controller��������֮ǰ������,
	 *  ����true ����У� false ��ֱ����������
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
//		String ip = getIpAddr(request);
//		String ipStr = PropertyUtil.getProperty("ipWhiteList"); // ��ȡ���Է���ϵͳ�İ�����
//		String[] ipArr = ipStr.split("\\|");
//		List<String> ipList = java.util.Arrays.asList(ipArr);
//
//		if (ipList.contains(ip)) {
//			logger.info("the request ip is : " + ip);
//			return true;
//		} else {
//			logger.error(ip + " is not contains ipWhiteList .......");
//			response.setHeader("Content-type","text/html;charset=UTF-8");//�����������һ����Ӧͷ������������Ľ��뷽ʽΪUTF-8
//		    String data = "Sorry, ip " + ip + " ,there is no access right, please contact the administrator to open access.";
//		    OutputStream stream = response.getOutputStream();
//		    stream.write(data.getBytes("UTF-8"));
//			return false;
//		}
		// Ϊ���������û����ʽ�����Ȳ����а������Ĺ���
		String ip = getIpAddr(request);
		logger.info("The request ip is : " + ip);
		return true;
	}

	/**
	 * ��ȡ���ʵ�ip��ַ
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
