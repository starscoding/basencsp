/** tab容器模块 */
Eastcom.modules.TabContainer = function(containerId, tabsLeftScroll,
		tabsRightScroll, panelsContainer, secondTabsContainer) {
	this.containerId = containerId;
	this.tabsLeftScroll = tabsLeftScroll;
	this.tabsRightScroll = tabsRightScroll;
	this.panelsContainer = panelsContainer;
	this.secondTabsContainer = secondTabsContainer;
};

Eastcom.modules.TabContainer.prototype = {
	initContainer : function(callback) {
		var that = this;
		var tabContainer = $('#' + that.containerId).tabs({
					tabsLeftScroll : that.tabsLeftScroll,
					tabsRightScroll : that.tabsRightScroll,
					panelsContainer : that.panelsContainer,
					secondTabsContainer : that.secondTabsContainer
				});
		if (callback)
			callback();
		return tabContainer;
	},
	addTab : function(id, title, name, url, closable, selected, forceRefresh,
			callback) {
		var that = this;
		var isHttp = isHttpAdress(url);
		var addressUrl = isHttp ? url : (Eastcom.baseURL + url);
		if (!id)
			return;
		if (addressUrl.indexOf('target=newWindow') != -1) {
			if (addressUrl.indexOf('openInFrame=true') != -1) {
				if (typeof(indexPageId) != undefined && indexPageId != null
						&& indexPageId.length) {// 已经在该窗口中打开了
				} else {
					var rootMenuName = '';
					if (addressUrl.indexOf('rootMenuName=') != -1) {
						var idx = addressUrl.indexOf('rootMenuName=');
						var add2 = addressUrl.substr(idx);
						if (add2.indexOf('&') != -1) {
							rootMenuName = add2.substring(0, add2.indexOf('&'));
						} else {
							rootMenuName = add2.substring(0, add2.length);
						}
					}
					addressUrl = Eastcom.baseURL + '/pages/main.jsp?'
							+ rootMenuName;
					eastcom.openPostWindow(addressUrl, {
								indexPageId : id,
								indexPageTitle : title,
								indexPageName : name,
								indexPageUrl : url
							}, name);
					return;
				}
			} else {
				window.open(addressUrl);
				return;
			}
		}
		closable = (typeof(closable) == 'undefined') ? true : closable;
		selected = (typeof(selected) == 'undefined') ? true : selected;
		forceRefresh = (typeof(forceRefresh) == 'undefined')
				? false
				: forceRefresh;
		var exsist = $('#' + that.containerId).tabs('exists', id);
		if (exsist) {
			if (selected) {
				$('#' + that.containerId).tabs('select', id);
			}
			if (forceRefresh) {
				var iframe = document.getElementById('tabs_' + id + '_iframe');
				var _url = (url && url.length) ? addressUrl : iframe.src;
				iframe.src = _url;
			}
		} else {
			var tabsUnSel = $('#taskbar #taskbar_center #tabs_container div.unselected');
			var tabsSel = $('#taskbar #taskbar_center #tabs_container div.selected');
			if ((tabsUnSel.length + tabsSel.length) >= Eastcom.tabsNum) {
				alert(MAX_TAB_NUM_MSG + Eastcom.tabsNum);
				return false;
			}
			$('#' + that.containerId).tabs('add', {
				id : id,
				title : title,
				closable : closable,
				selected : selected,
				style : 'height:100%;',
				content : '<iframe id="tabs_'
						+ id
						+ '_iframe" name="tabs_'
						+ name
						+ '_iframe" '
						+ 'allowTransparency= "true" src="'
						+ addressUrl
						+ '" '
						+ 'onload="IframeLoaded(\''
						+ id
						+ '\','
						+ isHttp
						+ ','
						+ closable
						+ ','
						+ callback
						+ ');" '
						+ 'border="0" frameborder="0" framespacing="0" marginheight="0" '
						+ 'marginwidth="0" style="width:100%;height:100%;"></iframe>'
			});
		}
	},
	resizeContainer : function() {
		$('#' + this.containerId).tabs('resize');
	},
	selectTab : function(tabId) {
		var that = this;
		$('#' + that.containerId).tabs('select', tabId);
	},
	closeTab : function(tabId) {
		var that = this;
		$('#' + that.containerId).tabs('close', tabId);
	},
	getSelected : function() {
		var that = this;
		return $('#' + that.containerId).tabs('selected');
	},
	getActiveTabId : function() {
		var that = this;
		var activeId = null;
		var selectTab = $('#' + that.containerId + ' div.selected');
		if (selectTab.length > 0) {
			var id = $(selectTab).attr('id').slice(5);
			activeId = id;
		}
		return activeId;
	},
	refreshCurrentTab : function() {
		var me = this;
		me.addTab(me.getActiveTabId(), null, null, null, null, null, true);
	}
};

/** 判断是否是以http开头的地址 */
function isHttpAdress(address) {
	var patrn = /^(http:\/\/)/i;
	if (patrn.exec(address))
		return true;
	return false;
}
function IframeLoaded(id, isHttp, closable, callback) {
	if (callback && closable) {
		callback(id);
	}
	// 如果加载外部应用，那么需要清空随机码
	if (isHttp)
		Eastcom.delRandomParam(id);

	var iframe = document.getElementById('tabs_' + id + '_iframe').contentWindow;
	/*
	 * var htmlElement = iframe.document; var permissions =
	 * $(htmlElement).find('#permissionsData div'); if(permissions &&
	 * permissions.length>0){ $(permissions).each(function(i,r){ var key =
	 * $(r).attr('id'); var val = $(r).text() == '1' ? true : false;
	 * Eastcom.permissionsData.push({'permission':key,'isPermitted':val}); }); }
	 */
	if (iframe && $('tabs_link_' + id) && $('tabs_link_' + id).innerText == '') {
		$('tabs_link_' + id).innerText = !iframe.document.title
				? td_lang.inc.msg_27
				: iframe.document.title;// "无标题"
	}
}