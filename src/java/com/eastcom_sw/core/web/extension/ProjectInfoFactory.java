package com.eastcom_sw.core.web.extension;

import com.eastcom_sw.core.entity.baseinfo.Commondatas;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;

/**
 * 项目界面信息基础数据生产类
 * 
 * @author JJF
 * @date MAR 10 2013
 */
public class ProjectInfoFactory {
	/**
	 * 生成默认配置
	 * 
	 * @return
	 */
	public static Commondatas getProjectInfo() {
		Commondatas projectInfo = createCommonData("projectInfo", "项目信息维护",
				"projectInfo", "",
				"(自动生成请勿修改)该节点用于维护不同项目的登录界面和主界面的风格以及信息，请在项目信息维护界面修改此目录下配置");

		Commondatas customLoginPageURL = createCommonData("customLoginPageURL",
				"自定义登录页面URL", "login/default/index.jsp", "",
				"请将自定义页面存放在/pages/login目录下");

		Commondatas mainPageConfig = createCommonData("mainPageConfig",
				"主界面配置信息", "mainPageConfig", "", "保存主界面头部以及底部自定义信息");

		Commondatas mainHeadBackImg = createCommonData("mainHeadBackImg",
				"头部图片", "/images/themes/blue/main/headbg.jpg", "", "头部图片URL");

		Commondatas mainFootInfo = createCommonData("mainFootInfo", "底部项目信息",
				"浙江网络投诉平台体验版", "", "底部项目信息");

		Commondatas defaultLoginConfig = createCommonData("defaultLoginConfig",
				"默认登录界面配置信息", "defaultLoginConfig", "",
				"默认登录界面配置信息，值:背景图片位置。属性:文字信息。备注:位置信息");

		Commondatas loginPagePosition1 = createCommonData("loginPagePosition1",
				"位置1配置", "login/default/images/chinaMobile.jpg", "", "1-1  312*148");
		Commondatas loginPagePosition2 = createCommonData("loginPagePosition2",
				"位置2配置", "login/default/images/landScape.jpg", "", "1-2  312*148");
		Commondatas loginPagePosition3 = createCommonData("loginPagePosition3",
				"位置3配置", "login/default/images/purplishredBg_oneSixth.jpg", "中国移动通信集团<br>浙江有限公司", "1-3  150*148");
		Commondatas loginPagePosition4 = createCommonData("loginPagePosition4",
				"位置4配置", "login/default/images/greenBg_oneSixth.jpg", "", "1-4  150*148");
		Commondatas loginPagePosition5 = createCommonData("loginPagePosition5",
				"位置5配置", "login/default/images/servicer.jpg", "", "2-1  150*148");
		Commondatas loginPagePosition6 = createCommonData("loginPagePosition6",
				"位置6配置", "login/default/images/logo_aHalf.jpg", "", "2-2  474*148");
		Commondatas loginPagePosition7 = createCommonData("loginPagePosition7",
				"位置7配置", "login/default/images/mainRight.jpg", "", "2-3  312*308");
		Commondatas loginPagePosition8 = createCommonData("loginPagePosition8",
				"位置8配置", "login/default/images/flower.jpg", "", "3-1  150*148");
		Commondatas loginPagePosition9 = createCommonData("loginPagePosition9",
				"位置9配置", "login/default/images/purplishredBg_oneSixth.jpg", "杭州东方通信软件<br>技术有限公司<br>产品与技术服务", "3-2  150*148");
		Commondatas loginPagePosition10 = createCommonData(
				"loginPagePosition10", "位置10配置", "login/default/images/greenBg_oneThird.jpg", "", "3-3  312*148");
		Commondatas loginPageBackImg = createCommonData(
				"loginPageBackImg", "登录界面背景图片", "login/default/images/background.jpg", "", "1440*900");

		Set<Commondatas> childs1 = new HashSet<Commondatas>();
		childs1.add(loginPagePosition1);
		childs1.add(loginPagePosition2);
		childs1.add(loginPagePosition3);
		childs1.add(loginPagePosition4);
		childs1.add(loginPagePosition5);
		childs1.add(loginPagePosition6);
		childs1.add(loginPagePosition7);
		childs1.add(loginPagePosition8);
		childs1.add(loginPagePosition9);
		childs1.add(loginPagePosition10);
		childs1.add(loginPageBackImg);
		
		defaultLoginConfig.setChildCommons(childs1);
		
		Set<Commondatas> childs2 = new HashSet<Commondatas>();
		childs2.add(mainHeadBackImg);
		childs2.add(mainFootInfo);
		mainPageConfig.setChildCommons(childs2);
		
		Set<Commondatas> childs3 = new HashSet<Commondatas>();
		childs3.add(customLoginPageURL);
		childs3.add(mainPageConfig);
		childs3.add(defaultLoginConfig);
		
		projectInfo.setChildCommons(childs3);
		
		return projectInfo;
	}

	/**
	 * 生成配置
	 * 
	 * @return
	 */
	public static Map<String, List<Commondatas>> getProjectInfo(
			String jsonString) {
		JSONObject conf = JSONObject.fromObject(jsonString);
		List<Commondatas> N_V_list = new ArrayList<Commondatas>();
		List<Commondatas> N_V_A_list = new ArrayList<Commondatas>();
		JSONArray N_V = conf.getJSONArray("N_V");// 名称-值配对项
		JSONArray N_V_A = conf.getJSONArray("N_V_A");// 名称-值-属性配对项

		for (int i = 0; i < N_V.size(); i++) {
			Commondatas c = new Commondatas();
			c.setName(N_V.getJSONObject(i).getString("name"));
			c.setValue(N_V.getJSONObject(i).getString("value"));
			N_V_list.add(c);
		}

		for (int i = 0; i < N_V_A.size(); i++) {
			Commondatas c = new Commondatas();
			c.setName(N_V_A.getJSONObject(i).getString("name"));
			c.setValue(N_V_A.getJSONObject(i).getString("value"));
			c.setAttribute(N_V_A.getJSONObject(i).getString("attribute"));
			N_V_A_list.add(c);
		}

		Map<String, List<Commondatas>> map = new HashMap<String, List<Commondatas>>();
		map.put("N_V", N_V_list);
		map.put("N_V_A", N_V_A_list);

		return map;
	}

	/**
	 * 创建commondatas
	 * 
	 * @param name
	 * @param label
	 * @param value
	 * @param desc
	 * @return
	 */
	private static Commondatas createCommonData(String name, String label,
                                                String value, String attribute, String desc) {
		Commondatas c = new Commondatas();
		c.setName(name);
		c.setLabel(label);
		c.setDesc(desc);
		c.setValue(value);
		c.setAttribute(attribute);
		return c;
	}
}
