Ext.define('Ext.user.userAccreditWin', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.*', 'Ext.window.*', 'Ext.user.userAccreditTree',
			'Ext.user.userAccreditRoleGrid'],
	userId : '',// id集合
	editUserFullName : '',// 中文名集合
	isIn : function(array, id) {
		var flag = false;
		for (var i = 0; i < array.length; i++) {
			if (array[i] == id) {
				flag = true;
			}
		}
		return flag;
	},
	submitRoleUpdate : function(userId, addIds, delIds, callback) {
		var me = this;
		var records = Ext.getCmp('userAccreditRoleGrid').getStore().getRange();
		var addNamesStr = '';
		var delNamesStr = '';
		var addNameCnsStr = '';
		var delNameCnsStr = '';
		for (var i = 0; i < records.length; i++) {
			if (me.isIn(addIds, records[i].get('id'))) {
				addNamesStr += (records[i].get('name') + ',');
				addNameCnsStr += (records[i].get('nameCn') + ',');
			} else if (me.isIn(delIds, records[i].get('id'))) {
				delNamesStr += (records[i].get('name') + ',');
				delNameCnsStr += (records[i].get('nameCn') + ',');
			}
		}
		addNamesStr = addNamesStr.length > 0 ? addNamesStr.substr(0,
				addNamesStr.length - 1) : '';
		delNamesStr = delNamesStr.length > 0 ? delNamesStr.substr(0,
				delNamesStr.length - 1) : '';
		addNameCnsStr = addNameCnsStr.length > 0 ? addNameCnsStr.substr(0,
				addNameCnsStr.length - 1) : '';
		delNameCnsStr = delNameCnsStr.length > 0 ? delNameCnsStr.substr(0,
				delNameCnsStr.length - 1) : '';

		Ext.Ajax.request({
					url : eastcom.baseURL
							+ '/sysmng/security/changeUserRoleRange',
					method : 'POST',
					params : {
						addIdStr : addIds.join(','),
						delIdStr : delIds.join(','),
						addNamesStr : addNamesStr,
						delNamesStr : delNamesStr,
						addNameCnsStr : addNameCnsStr,
						delNameCnsStr : delNameCnsStr,
						editUserId : userId,
						editUserFullName : me.editUserFullName
					},
					success : function(form, action) {
						var result = Ext.JSON.decode(form.responseText);
						if (result.success == 'true') {
							Ext.Msg.show({
										title : MSG_SUCCESS,
										msg : result.msg,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.INFO
									});
							me.close();
						} else {
							Ext.Msg.show({
										title : MSG_FAILURE,
										msg : result.msg,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.ERROR
									});
							me.close();
						}
					},
					failure : function(form, action) {
						Ext.Msg.show({
									title : MSG_ERROR,
									msg : MSG_ERROR_PROMPT,
									buttons : Ext.Msg.OK,
									icon : Ext.Msg.ERROR
								});
					}
				});
	},
	submitDeptUpdate : function(userId, addIds, delIds, callback) {
		var me = this;
		Ext.Ajax.request({
					url : eastcom.baseURL
							+ '/sysmng/security/changeUserDeptRange',
					method : 'POST',
					params : {
						addIdStr : addIds.join(','),
						delIdStr : delIds.join(','),
						editUserId : userId,
						editUserFullName : me.editUserFullName
					},
					success : function(form, action) {
						var result = Ext.JSON.decode(form.responseText);
						if (result.success == 'true') {
							Ext.Msg.show({
										title : MSG_SUCCESS,
										msg : result.msg,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.INFO
									});
							me.close();
						} else {
							Ext.Msg.show({
										title : MSG_FAILURE,
										msg : result.msg,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.ERROR
									});
							me.close();
						}
					},
					failure : function(form, action) {
						Ext.Msg.show({
									title : MSG_ERROR,
									msg : MSG_ERROR_PROMPT,
									buttons : Ext.Msg.OK,
									icon : Ext.Msg.ERROR
								});
					}
				});
	},
	getDeptAccreditPanel : function() {
		var me = this;
		var userDeptTree = Ext.create('Ext.user.userAccreditTree', {
					userId : me.userId,
					layout : 'fit',
					border : false,
					controllerName : 'getDepartmentAccreditTree'
				});
		return userDeptTree;
	},
	initComponent : function() {
		var me = this;
		me.frame = false;
		me.id = 'userAccreditWin';
		me.layout = 'fit';
		me.title = BUTTON_AUTHORIZE;
		me.autoScroll = true;
		me.bodyStyle = 'overflow-y:hidden;overflow-x:hidden;';
		me.style = {
			zindex : 20000
		};
		me.width = 500;
		me.height = 350;
		me.minWidth = 500;
		me.maxWidth = 500;
		me.minHeight = 350;
		// me.maxHeight = 350;
		me.listeners = {
			afterlayout : function() {
			}
		}
		if (me.userId.indexOf(',') != -1) {// 多个用户一起授权，关闭部门授权
			me.items = [Ext.create('Ext.tab.Panel', {
						border : false,
						items : [{
							xtype : 'panel',
							title : ROLES,
							layout : 'fit',
							border : false,
							items : [Ext.create(
									'Ext.user.userAccreditRoleGrid', {
										id : 'userAccreditRoleGrid',
										border : false,
										userId : me.userId
									})]
						}]
					})];
		} else {
			me.items = [Ext.create('Ext.tab.Panel', {
						border : false,
						items : [{
							xtype : 'panel',
							title : ROLES,
							layout : 'fit',
							border : false,
							items : [Ext.create(
									'Ext.user.userAccreditRoleGrid', {
										id : 'userAccreditRoleGrid',
										border : false,
										userId : me.userId
									})]
						}, {
							xtype : 'panel',
							title : DEPARTMENTS,
							border : false,
							layout : 'fit',
							items : []
						}],
						listeners : {
							tabchange : function(tab, newCard) {
								if (newCard.title == DEPARTMENTS) {
									if (Ext.getCmp('userAccreditTree') == null) {
										newCard.add(me.getDeptAccreditPanel());
									}
								}
							}
						}
					})];
		}
		me.buttons = [{
			xtype : 'button',
			text : BUTTON_OK,
			id : 'saveUserAccredit',
			disabled : true,
			roleChange : false,
			deptChange : false,
			fireChange : function() {
				var me = this;
				if (me.roleChange == false && me.deptChange == false) {
					me.disable();
				} else {
					me.enable();
				}
			},
			handler : function() {
				var userId = me.userId;
				if (this.roleChange) {
					var roleGrid = Ext.getCmp('userAccreditRoleGrid');
					var roleAddIds = roleGrid.toBeCheckedIds;
					var roleDelIds = roleGrid.toBeUnCheckedIds;
					if (this.deptChange) {
						var deptTree = Ext.getCmp('userAccreditTree');
						var deptAddIds = deptTree.toBeCheckedIds;
						var deptDelIds = deptTree.toBeUnCheckedIds;
						var callback = me.submitDeptUpdate(userId, deptAddIds,
								deptDelIds);
						me.submitRoleUpdate(userId, roleAddIds, roleDelIds,
								callback);
					} else {
						me.submitRoleUpdate(userId, roleAddIds, roleDelIds);
					}
				} else {
					var deptTree = Ext.getCmp('userAccreditTree');
					var deptAddIds = deptTree.toBeCheckedIds;
					var deptDelIds = deptTree.toBeUnCheckedIds;
					me.submitDeptUpdate(userId, deptAddIds, deptDelIds);
				}
			}
		}, {
			text : BUTTON_CANCEL,
			handler : function() {
				me.close();
			}
		}];
		me.listeners = {
			show : function() {
				Ext.getBody().mask();
			},
			close : function() {
				Ext.getBody().unmask();
			}
		}
		this.callParent();
	}
});