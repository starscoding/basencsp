package com.eastcom_sw.core.web;

import com.eastcom_sw.common.utils.OrderProperties;
import com.eastcom_sw.common.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.LinkedHashMap;

public class ShiroFilterChainDefinitionsMap extends
        LinkedHashMap<String, String> {
	Logger log = LoggerFactory.getLogger(getClass());

	public ShiroFilterChainDefinitionsMap() {
		initFilters();
	}

	private void initFilters() {
		OrderProperties p = PropertiesUtil
				.loadOrderdProperties("security/shiro.properties");
		if (!p.isEmpty()) {
			Enumeration e = p.propertyNames();
			while (e.hasMoreElements()) {
				String key = e.nextElement().toString();
				if (key.startsWith("/")) {
					this.put(key.toString(), p.getProperty(key.toString()));
				}
			}
		}
	}
}
