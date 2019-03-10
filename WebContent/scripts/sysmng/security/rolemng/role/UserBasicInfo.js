Ext.define('Ext.role.UserBasicInfo', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.*', 'Ext.window.*', 'Ext.grid.*', 'Ext.data.*'],
	filterNull : function(str) {
		if (str == null || str == 'null') {
			return '';
		} else {
			return str;
		}
	},
	createUserDetailHtml : function(user) {
		var me = this;
		var HTML = "";
		HTML = '<div id="rfgcon">';
		HTML += '<table class="rfgtab1" cellpadding="0" cellspacing="0" width="100%" height="100%">';
		HTML += '    <tr>';
		HTML += '        <td class="rfgtd1 userInfoLabelWidth">' + USERNAME
				+ '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.username)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1 userInfoLabelWidth">' + FULLNAME
				+ '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.fullName)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1 userInfoLabelWidth">' + SEX
				+ '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.sex)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1 userInfoLabelWidth">' + TEL_NO
				+ '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.fixedNo)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1 userInfoLabelWidth">' + MOBILE
				+ '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.mobileNo)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1 userInfoLabelWidth">' + EMAIL
				+ '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.email)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1 userInfoLabelWidth">' + ROLES
				+ '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.roleName)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1 userInfoLabelWidth">' + DEPARTMENTS
				+ '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.deptName)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1 userInfoLabelWidth">' + USER_LEVEL
				+ '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.userLevel)
				+ '&nbsp;</td>';
		HTML += '    </tr><tr>';
		HTML += '        <td class="rfgtd1 userInfoLabelWidth">' + OWNER
				+ '</td>';
		HTML += '        <td class="rfgtd2">' + me.filterNull(user.owner)
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
						chineseFlag : 'true',
						basicFlag : true
					},
					success : function(form, action) {
						var result = Ext.JSON.decode(form.responseText).data;
						Ext.getCmp('basicInfoPanel').body.update(me
								.createUserDetailHtml(result));
						me.getEl().unmask();
					},
					failure : function(form, action) {
						me.getEl().unmask();
					}
				});
	},
	initComponent : function() {
		var me = this;
		me.frame = false;
		me.id = 'userBasicInfoWin';
		me.closable = false;
		me.draggable = false;
		me.title = UE_TITLE;
		me.height = 370;
		me.border = false;
		me.width = 300;
		me.layout = 'fit';
		me.autoScroll = true;
		me.bodyStyle = 'overflow-y:hidden;overflow-x:hidden;';
		me.items = [Ext.create('Ext.panel.Panel', {
					bodyStyle : 'overflow-y:auto;overflow-x:hidden;',
					id : 'basicInfoPanel',
					layout : {
						type : 'fit'
					},
					border : false,
					html : ''
				})];
		this.callParent();
	}
});