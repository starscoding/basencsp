Ext.define('Ext.user.userDetailWin', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.*', 'Ext.window.*', 'Ext.grid.*', 'Ext.data.*'],
	ids : [],
	totalCount : 0,
	nowCount : 0,
	setCurrentIds : function(ids) {
		var me = this;
		me.ids = ids;
		me.totalCount = ids.length;
		if (me.totalCount > 1) {
			Ext.getCmp('userDetailBottomBar').show();
		} else {
			Ext.getCmp('userDetailBottomBar').hide();
		}
		Ext.getCmp('userDetailPageLabel').setText((me.nowCount + 1) + '/'
				+ me.totalCount);
		me.loadUserInfo(ids[0]);
	},
	filterNull : function(str){
		if(str==null || str=='null'){
			return '';
		}else{
			return str;
		}
	},
	createUserDetailHtml : function(user) {
		var me = this;
		var HTML = "";
		HTML = '<div id="rfgcon">';
		HTML += '<table class="rfgtab1" cellpadding="0" cellspacing="0" width="100%" height="100%">';
		HTML += '    <tr>';
		HTML += '        <td class="rfgtd1">' + USERNAME + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.username) + '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + FULLNAME + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.fullName) + '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + SEX + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.sex) + '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + TEL_NO + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.fixedNo) + '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + MOBILE + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.mobileNo) + '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + EMAIL + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.email) + '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + USER_CITY + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.city) + '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + ROLES + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.roleName) + '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + DEPARTMENTS + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.deptName) + '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + USER_LEVEL + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.userLevel) + '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + IS_ENABLE + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.accountEnabled)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + USER_CREATE_TIME + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.accoutCreateTime)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + LAST_LOGIN_TIME + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.lastLoginTime)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + LOGIN_TIMES + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.times) + '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + USER_CREATOR + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.creator) + '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + OWNER + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.owner) + '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + USER_PWD_EXPRIED_DAYS + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.pwdExpiredDays)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + USER_EXPRIED_STARTTIME
				+ '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.accoutExpiredStarttime)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1">' + USER_EXPRIED_ENDTIME + '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.accoutExpiredEndtime)
				+ '&nbsp;</td>';
		HTML += '    </tr>';
		HTML += '</table>';
		HTML += '</div>';
		return HTML;
	},

	loadUserInfo : function(currentId) {
		var me = this;
		me.getEl().mask(MSG_DATA_LOADING);
		Ext.Ajax.request({
					url : eastcom.baseURL
							+ '/sysmng/security/querySingleUserInfo',
					method : 'POST',
					params : {
						id : currentId,
						chineseFlag : 'true'
					},
					success : function(form, action) {
						var result = Ext.JSON.decode(form.responseText).data;
						Ext.getCmp('basicInfoPanel').body.update(me
								.createUserDetailHtml(result));
						me.getEl().unmask();
					},
					failure : function(form, action) {
					}
				});
	},
	preview : function() {
		var me = this;
		if (me.nowCount - 1 >= 0) {
			me.loadUserInfo(me.ids[me.nowCount - 1]);
			me.nowCount -= 1;
			Ext.getCmp('userDetailPageLabel').setText((me.nowCount + 1) + '/'
					+ me.totalCount);
		}
	},
	next : function() {
		var me = this;
		if (me.nowCount + 1 < me.ids.length) {
			me.loadUserInfo(me.ids[me.nowCount + 1]);
			me.nowCount += 1;
			Ext.getCmp('userDetailPageLabel').setText((me.nowCount + 1) + '/'
					+ me.totalCount);
		}
	},
	initComponent : function() {
		var me = this;
		me.frame = false;
		me.id = 'userDetailWin';
		me.title = BUTTON_DETAIL;
		me.height = 350;
		me.border = false;
		me.width = 450;
		me.layout = 'fit';
		me.autoScroll = true;
		me.bodyStyle = 'overflow-y:hidden;overflow-x:hidden;';
		me.style = {
			zindex : 20000
		};
		me.items = [Ext.create('Ext.panel.Panel', {
			layout : {
				type : 'fit'
			},
			border : true,
			items : [Ext.create('Ext.panel.Panel', {
				id : 'basicInfoPanel',
				border : false,
				html : '',
				bodyStyle : 'overflow-y:auto;overflow-x:hidden;',
				bbar : Ext.create('Ext.toolbar.Toolbar', {
					id : 'userDetailBottomBar',
					hidden : true,
					items : ['->', {
						xtype : 'button',
						icon : '../../../../static/images/themes/blue/notification/page-prev-disabled.gif',
						text : BUTTON_PREVIOUS,
						handler : function() {
							me.preview();
						}
					}, {
						xtype : 'label',
						text : (me.nowCount + 1) + '/' + me.totalCount,
						id : 'userDetailPageLabel'
					}, {
						xtype : 'button',
						iconAlign : 'right',
						icon : '../../../../static/images/themes/blue/notification/page-prev-disabled01.gif',
						text : BUTTON_NEXT,
						handler : function() {
							me.next();
						}
					}, '->']
				})
			})]
		})];
		me.buttons = [{
					text : BUTTON_CLOSE,
					handler : function() {
						me.close();
					}
				}];
		this.callParent();
	}
});