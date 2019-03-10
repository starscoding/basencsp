var roleWin = null;

/** 新增角色按钮 */
var addRoleAction = Ext.create('Ext.Action', {
			iconCls : 'icon-add',
			hidden : eastcom.isPermitted('rolemng', 'add'),
			text : BUTTON_NEW,
			handler : addRole
		});
/** 修改角色按钮 */
var editRoleAction = Ext.create('Ext.Action', {
			iconCls : 'icon-edit',
			hidden : eastcom.isPermitted('rolemng', 'edit'),
			disabled : true,
			text : BUTTON_EDIT,
			handler : editRole
		});
/** 删除角色按钮 */
var delRoleAction = Ext.create('Ext.Action', {
	iconCls : 'icon-remove',
	hidden : eastcom.isPermitted('rolemng', 'delete'),
	disabled : true,
	text : BUTTON_DELETE,
	handler : function(widget, event) {
		var records = Ext.getCmp('roleMainGrid').getSelectionModel()
				.getSelection();
		var infoStr = '';
		for (var i = 0; i < records.length; i++) {
			infoStr += (records[i].get('name') + ',');
		}
		infoStr = infoStr.substring(0, infoStr.length - 1);
		Ext.Msg.show({
			title : MSG_WARNING,
			msg : DEL_BTN_INFO,
			buttons : Ext.Msg.YESNO,
			icon : Ext.Msg.WARNING,
			fn : function(btn) {
				if (btn == 'yes') {
					Ext.getCmp('roleMainGrid').getEl().mask(DEL_DELETING);
					var ids = '';
					for (var i = 0; i < records.length; i++) {
						ids += (records[i].get('id') + ',');
					}
					ids = ids.substring(0, ids.length - 1);
					Ext.Ajax.request({
								url : eastcom.baseURL
										+ '/sysmng/security/deleteRole',
								method : 'POST',
								params : {
									idStr : ids,
									nameStr : infoStr
								},
								success : function(form, action) {
									var result = Ext.JSON
											.decode(form.responseText);
									if (result.success == 'true') {
										Ext.Msg.show({
													title : MSG_SUCCESS,
													msg : result.msg,
													buttons : Ext.Msg.OK,
													icon : Ext.Msg.INFO
												});
										Ext.getCmp('roleMainGrid').getStore()
												.remove(records);
									} else {
										Ext.Msg.show({
													title : MSG_FAILURE,
													msg : result.msg,
													buttons : Ext.Msg.OK,
													icon : Ext.Msg.ERROR
												});
									}
									Ext.getCmp('roleMainGrid').getEl().unmask();
									Ext.getCmp('roleMainGrid').rolesChanged = true;
								},
								failure : function(form, action) {
								}
							});
				}
			}
		});
	}
});
/** 授权按钮 */
var authorizationAction = Ext.create('Ext.Action', {
			iconCls : 'icon-accredit',
			hidden : eastcom.isPermitted('rolemng', 'resourceAccredit'),
			disabled : true,
			text : BUTTON_AUTHORIZE,
			handler : roleAccredit
		});

var createMultiAuthWin = function() {
	return Ext.create('Ext.accredit.RoleMultiAccreditWin', {
				id : 'multiAuthWin',
				height : 430,
				width : 350,
				closeAction : 'hide'
			})
}

/** 批量增加授权按钮 */
var multiAddAuthorizationAction = Ext.create('Ext.Action', {
			iconCls : 'icon-accredit',
			hidden : eastcom.isPermitted('rolemng', 'resourceAccredit'),
			disabled : true,
			text : ROLE_MULTI_AUTH_ADD,
			handler : function() {
				var sels = Ext.getCmp('roleMainGrid').getSelectionModel()
						.getSelection(), ids = [], names = [];
				var win = Ext.getCmp('multiAuthWin');
				win = win ? win : createMultiAuthWin();
				win.setTitle(ROLE_MULTI_AUTH_ADD);
				win.setData(sels, 'ADD');
				win.show();
			}
		});

/** 批量删除授权按钮 */
var multiDelAuthorizationAction = Ext.create('Ext.Action', {
			iconCls : 'icon-accredit',
			hidden : eastcom.isPermitted('rolemng', 'resourceAccredit'),
			disabled : true,
			text : ROLE_MULTI_AUTH_DEL,
			handler : function() {
				var sels = Ext.getCmp('roleMainGrid').getSelectionModel()
						.getSelection(), ids = [], names = [];
				var win = Ext.getCmp('multiAuthWin');
				win = win ? win : createMultiAuthWin();
				win.setTitle(ROLE_MULTI_AUTH_DEL);
				win.setData(sels, 'DEL');
				win.show();
			}
		});

var contextMenu = Ext.create('Ext.menu.Menu', {
			items : [editRoleAction, authorizationAction, delRoleAction]
		});

function addRole() {
	roleWin = Ext.create('Ext.role.Window', {
				listeners : {
					validSuccess : eastcom.modules.roleMng.saveRole
				}
			});
	roleWin.setTitle(BUTTON_NEW);
	roleWin.typeFlag = 0;
	roleWin.show();
}

function editRole() {
	roleWin = Ext.create('Ext.role.Window', {
				listeners : {
					validSuccess : eastcom.modules.roleMng.saveRole
				}
			});
	roleWin.setTitle(BUTTON_EDIT);
	roleWin.typeFlag = 1;
	var record = Ext.getCmp('roleMainGrid').getSelectionModel().getSelection()[0];
	roleWin.loadRecord(record);
	roleWin.show();
}

function roleAccredit() {
	Ext.create('Ext.accredit.RoleAccreditWin', {
		roleId : Ext.getCmp('roleMainGrid').getSelectionModel().getSelection()[0]
				.get('id'),
		roleName : Ext.getCmp('roleMainGrid').getSelectionModel()
				.getSelection()[0].get('name')
	}).show();
}

Ext.define('Ext.search.SearchGridPanel', {
			extend : 'Ext.ux.SimpleLiveSearchGridPanel',
			requires : ['Ext.toolbar.TextItem', 'Ext.form.field.Checkbox',
					'Ext.form.field.Text'],
			id : 'roleMainGrid',
			currentUsername : '',
			currentUserType : '',
			selModel : Ext.create('Ext.selection.CheckboxModel', {}),
			listeners : {
				itemcontextmenu : function(view, rec, node, index, e) {
					e.stopEvent();
					contextMenu.showAt(e.getXY());
					return false;
				},
				selectionchange : function(sm, selections) {
					var me = this, enabled = false, size = 0;
					if (selections.length) {
						if (me.currentUserType == 'A') {
							enabled = true;
						} else {
							enabled = true;
							// 只要有一条选择数据无权编辑，则不开启按钮
							$.each(selections, function(i, sel) {
										if (sel.get('creator') != me.currentUsername) {
											enabled = false;
										}
									});
						}
						size = selections.length;
					}
					me.enableActions(enabled, size);
				},
				afterrender : function() {
					this.getStore().load();
				}
			},
			enableActions : function(flag, size) {
				if (flag && size == 1) {
					editRoleAction.enable();
					delRoleAction.enable();
					authorizationAction.enable();

					multiAddAuthorizationAction.disable();
					multiDelAuthorizationAction.disable();
				} else {
					if (flag && size > 1) {// 可使用批量授权
						multiAddAuthorizationAction.enable();
						multiDelAuthorizationAction.enable();
					} else {
						multiAddAuthorizationAction.disable();
						multiDelAuthorizationAction.disable();
					}
					editRoleAction.disable();
					delRoleAction.disable();
					authorizationAction.disable();
				}
			},
			initComponent : function() {
				var me = this;
				me.tbar = [addRoleAction, '-', editRoleAction, '-',
						authorizationAction, '-', multiAddAuthorizationAction,
						'-', multiDelAuthorizationAction, '-', delRoleAction];
				me.callParent(arguments);
			}
		});