package com.eastcom_sw.core.web;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * web-base模块配置文件读取
 * 
 * @author JJF
 * @since JUL 26 2013
 * 
 */
public class BaseBundle {
	private static ResourceBundle rb;
	private final static String BASE_BUNDLE_NAME = "i18n.BaseBundle";

	private static ResourceBundle getBundle() {
		if (rb == null) {
			// 为空时默认使用简体中文
			rb = ResourceBundle.getBundle(BASE_BUNDLE_NAME, new Locale("zh",
					"CN"));
		}
		return rb;
	}

	/**
	 * set the locale by locale String like 'zh_CN'
	 * 
	 * @param lc
	 */
	public static void setLc(String lc) {
		String[] l = lc.split("_");
		if (!(getBundle().getLocale().getLanguage().equals(l[0]) && getBundle()
				.getLocale().getCountry().equals(l[1]))) {
			rb = ResourceBundle.getBundle(BASE_BUNDLE_NAME, new Locale(l[0],
					l[1]));
		}
	}

	/**
	 * 通过指定key获取配置文件内容
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		ResourceBundle r = getBundle();
		return r.getString(key);
	}

	/**
	 * 通过指定key和语言区域获取配置文件内容
	 * 
	 * @param key
	 * @param locale
	 * @return
	 */
	public static String getString(String key, String locale) {
		setLc(locale);
		ResourceBundle r = getBundle();
		return r.getString(key);
	}
}
