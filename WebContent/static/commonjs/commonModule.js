/**
 * 公共方法封装
 * 
 * @author SCM modules : 配置模块的公共对象
 */
var eastcom = window.eastcom || (function() {

	return {
		modules : {},
		enableColumnHide : false,
		pageSize : 20,
		timeOut : 120000,
		baseURL : getBaseURL(),
		pFunc : function() { // 获取main的对象
			var pWindow = window.parent;
			return pWindow;
		},
		commonWindowNum : 1,// 公共窗口数
		getCurrentLocale : getCurrentLocale,
		getCurrentUserBasicInfo : getCurrentUserBasicInfo,
		isNotNull : function(obj) {
			if (typeof(obj) != 'undefined' && obj != null) {
				return true;
			} else {
				return false;
			}
		},
		createTab : function(id, title, name, url, path, closable, selected,// 新建tab页
				forceRefresh) {
			var eastcomObj = this.getEastcom();
			if (this.isNotNull(eastcomObj)) {
				eastcomObj.createTab(id, title, name, url, path, closable,
						selected, forceRefresh);
			} else {
				window.open(this.baseURL + url);
			}
		},
		refreshCurrentTab : function() {
			var eastcomObj = this.getEastcom();
			if (this.isNotNull(eastcomObj)) {
				eastcomObj.getTabContainerObj().refreshCurrentTab();
			}
		},
		closeTab : function(id) {
			var eastcomObj = this.getEastcom();
			if (this.isNotNull(eastcomObj)) {
				eastcomObj.closeTab(id);
			}
		},
		showMsg : function(type, msg, isHide) {
			var eastcomObj = this.getEastcom();
			if (eastcomObj && eastcomObj.showMsg) {
				eastcomObj.showMsg(type, msg, isHide);
			} else {
				var msgCnt = $('#msg-cnt');
				if (!msgCnt.length) {
					msgCnt = $('<div id="msg-cnt"></div>');
					msgCnt.css({
								position : 'fixed',
								left : '0px',
								bottom : '10px',
								width : '100%',
								padding : '0px 70px',
								'z-index' : 9999
							});
					$('body').append(msgCnt);
				}
				var msgDomStr = '<div class="alert alert-'
						+ type
						+ ' alert-dismissible" role="alert"><button type="button" class="close" data-dismiss="alert"aria-label="Close"><span aria-hidden="true">&times;</span></button>'
						+ msg + '</div>';
				var msgD = $(msgDomStr).hide();
				msgCnt.append(msgD);
				msgD.slideDown(500, function() {
						});
				if (isHide === true) {
					msgD.delay(5000).fadeOut(500);
				}
			}
		},
		tonggleLeftTree : function(flag, complete) {
			var eastcomObj = this.getEastcom();
			(eastcomObj && eastcomObj.tonggleLeftTree) ? eastcomObj
					.tonggleLeftTree(flag, complete) : []
		},
		syncIframeHeight : function() {
			var eastcomObj = this.getEastcom();
			eastcomObj ? eastcomObj.syncIframeHeight() : []
		},
		getEastcom : function() {
			var eastcomObj = null;
			try {
				eastcomObj = this.pFunc().Eastcom;// 跨越iframe访问可能遇到权限错误。返回null
			} catch (e) {
			}
			if (this.isNotNull(eastcomObj)) {
				return eastcomObj;
			} else {
				return null;
			}
		},
		getTheme : function() {
			var theme = 'blue';
			if (typeof currentUserTheme != 'undefined') {
				theme = currentUserTheme;
			}
			return theme;
		},
		getUserInfo : function() {
			var userInfo = null;
			var eastcomObj = this.getEastcom();
			if (this.isNotNull(eastcomObj)) {
				userInfo = eastcomObj.userInfo;
			}
			return userInfo;
		},
		// isPermitted : function(module, permission) {
		// var permissionsData = [];
		// var isP = true;
		// var eastcomObj = this.getEastcom();
		// if (this.isNotNull(eastcomObj)) {
		// permissionsData = eastcomObj.permissionsData;
		// }
		// // edit by jjf MAY 22 2013 去除jquery依赖
		// for (var i = 0; i < permissionsData.length; i++) {
		// var item = permissionsData[i];
		// var module_ = item.module;
		// if (module_ == module) {
		// var permissions_ = item.permissions;
		// isP = false;
		// for (var j = 0; j < permissions_.length; j++) {
		// var p = permissions_[j];
		// var pm = p.permission;
		// if (pm == permission) {
		// isP = true;
		// break;
		// }
		// }
		// break;
		// }
		// }
		// return !isP;
		// },

		isPermitted : function(module, permission) {/* 仅供适配以前旧的调用方式 */
			return !this.allowPermission(module, permission);
		},

		allowPermission : function(module, permission) {
			var allow = true, permissionObjects = {};
			var eastcomObj = this.getEastcom();
			if (this.isNotNull(eastcomObj)) {
				permissionObjects = eastcomObj.permissionObjects;
			}
			// 无罪推定，默认选择allow，但是如果存在菜单名称，又不具有制定的permission名称时，返回false
			if (permissionObjects && permissionObjects[module]
					&& !permissionObjects[module][permission]) {
				allow = false;
			}
			return allow;
		},

		sprintf : function(str) {
			var array = str.split("%s");
			if (array.length == 1 || array.length != arguments.length)
				return str;

			str = array[0];
			for (var i = 1; i < array.length; i++) {
				str += arguments[i] + array[i];
			}
			return str;
		},

		showWaitDialog : function(targetId) {
			Ext.MessageBox.show({
						msg : '正在保存您的操作,请等待...',
						progressText : '保存中...',
						width : 300,
						wait : true,
						waitConfig : {
							interval : 200
						},
						icon : 'ext-mb-download', // custom class
						animateTarget : targetId
					});
		},
		hideDialog : function() {
			Ext.MessageBox.hide();
		},
		/**
		 * 通过post方式打开窗口，可传值 例：openPostWindow('a.html',{a:1,b:2},'test');
		 */
		openPostWindow : function(url, data, name) {
			var tempForm = document.createElement("form");
			tempForm.method = "post";
			tempForm.action = url;
			tempForm.target = name;
			if (data) {
				for (var i in data) {
					var hideInput = document.createElement("input");
					hideInput.type = "hidden";
					hideInput.name = i
					hideInput.value = data[i];
					tempForm.appendChild(hideInput);
				}
			}
			document.body.appendChild(tempForm);
			window.open('about:blank', name);
			tempForm.submit();
			document.body.removeChild(tempForm);
		}
	};

	/** 获取基本的项目路径 */
	function getBaseURL() {
		var baseDir = "";
		var __head = document.getElementsByTagName("head")[0];
		var __nodes = __head.childNodes;
		for (var i = 0; i < __nodes.length; ++i) {
			var src = __nodes.item(i).src;
			if (src) {
				var index = src.indexOf("/static/commonjs/commonModule.js");
				if (index >= 0) {
					baseDir = src.substring(0, index);
				}
			}
		}
		return baseDir;
	}

	/** 获取当前国际化语言配置 */
	function getCurrentLocale() {
		//base.jsp中赋值
		if (typeof hidden_base_locale != 'undefined'){
			return hidden_base_locale;
		}
		var locale;
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome,
			// Opera,Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				locale = xmlhttp.responseText;
			}
		}
		xmlhttp.open("POST", getBaseURL() + '/common/getCurrentLocale', false);
		xmlhttp.send();
		return locale;
	}

	/**
	 * 获取当前登录用户基本信息，请使用callback回调函数调用用户信息 使用方法: var callback =
	 * function(userInfo){ console.log(userInfo.id); //用户id
	 * console.log(userInfo.username);//用户名
	 * console.log(userInfo.userLevel);//用户级别
	 * console.log(userInfo.userLevelType);//用户级别类型（“A”：代表超级管理员，“M”：代表一般管理员，“U”：代表一般用户） }
	 * eastcom.getCurrentUserBasicInfo(callback);
	 * 
	 */
	function getCurrentUserBasicInfo(callback) {
		var topLevel = 1;// 超级管理员用户等级请在此设置
		var bottomLevel = 3;// 一般操作员用户等级在此设置
		Ext.Ajax.request({
			url : eastcom.baseURL + '/sysmng/security/getCurrentUserBaseInfo',
			method : 'POST',
			success : function(form, action) {
				var result = Ext.JSON.decode(form.responseText);
				if (result.success == 'true') {
					var userLevel = result.data.userLevel;
					var userLevelType = '';
					if (typeof(userLevel) == "string") {
						userLevel = parseInt(userLevel);
					}
					if (userLevel == topLevel) {
						userLevelType = 'A';
					} else if (userLevel > topLevel && userLevel < bottomLevel) {
						userLevelType = 'M';
					} else if (userLevel == bottomLevel) {
						userLevelType = 'U';
					}
					result.data.userLevelType = userLevelType;
					callback(result.data);
				} else {
					Ext.Msg.show({
								title : '错误',
								msg : result.msg,
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.ERROR
							});
					Ext.getBody().mask();
				}
			},
			failure : function(form, action) {
				Ext.Msg.show({
							title : '错误',
							msg : '获取当前登录用户信息失败！请联系管理员',
							buttons : Ext.Msg.OK,
							icon : Ext.Msg.ERROR
						});
				Ext.getBody().mask();
			}
		});
	}
})();

// 日期格式化函数
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
		// millisecond
	}
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
						- RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1
							? o[k]
							: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}