/**
 * 角色管理
 */
Ext.Loader.setConfig({
			enabled : true
		});
Ext.Loader.setPath('Ext.search',
		'../../../../scripts/sysmng/security/rolemng/search');
Ext.Loader.setPath('Ext.role',
		'../../../../scripts/sysmng/security/rolemng/role');
Ext.Loader.setPath('Ext.accredit',
		'../../../../scripts/sysmng/security/rolemng/accredit');
Ext.Loader.setPath('Ext.ux', extDir + '/ux');
Ext.Loader.setPath('Ext.tab', extDir + '/src/tab');

Ext.require(['Ext.grid.*', 'Ext.data.*', 'Ext.util.*',
		'Ext.tip.QuickTipManager', 'Ext.search.SearchGridPanel']);

eastcom.modules.roleMng = (function() {
	Ext.define('Role', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'id',
							type : 'string'
						}, {
							name : 'name',
							type : 'string'
						}, {
							name : 'nameCn',
							type : 'string'
						}, {
							name : 'creator',
							type : 'string'
						}, {
							name : 'inheritance',
							type : 'string'
						}, {
							name : 'description',
							type : 'string'
						}, {
							name : 'createTime',
							type : 'string'
						}, {
							name : 'temporarily',
							type : 'string'
						}, {
							name : 'validStarttime',
							type : 'string'
						}, {
							name : 'validEndtime',
							type : 'string'
						}, {
							name : 'userNum',
							type : 'int'
						}, {
							name : 'resourceNum',
							type : 'int'
						}]
			});

	Ext.define('Rescource', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'id',
							type : 'string'
						}, {
							name : 'name',
							type : 'string'
						}, {
							name : 'nameCn',
							type : 'string'
						}, {
							name : 'order',
							type : 'int'
						}, {
							name : 'desc',
							type : 'string'
						}, {
							name : 'userNum',
							type : 'int'
						}, {
							name : 'leaf',
							type : 'boolean'
						}, {
							name : 'totalChildnum',
							type : 'int'
						}, {
							name : 'text',
							type : 'string'
						}, {
							name : 'disabled',
							type : 'string'
						}]
			});
	function change(val) {
		if (val == '1') {
			return '<span style="color:green;">是</span>';
		} else if (val == '0') {
			return '<span style="color:red;">否</span>';
		}
		return val;
	}

	var store = Ext.create('Ext.data.Store', {
				model : 'Role',
				proxy : {
					type : 'ajax',
					url : eastcom.baseURL + '/sysmng/security/getAllRoleList'
				},
				autoLoad : false,
				listeners : {}
			});

	function saveRole(opt, form) {
		form.submit({
			clientValidation : true,
			url : eastcom.baseURL + '/sysmng/security/saveRole?opt=' + opt,
			params : {
				id : form.currentId
			},
			success : function(form, action) {
				if (action.result.success == 'true') {
					if (form.currentId.length > 0) {// 修改
					} else {// 新增
						store.insert(0, action.result.data);
					}
					Ext.Msg.show({
								title : MSG_SUCCESS,
								msg : action.result.msg,
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.INFO
							});
				}
				Ext.getCmp('roleWindow').getEl().unmask();
				Ext.getCmp('roleWindow').close();
				Ext.getCmp('roleMainGrid').rolesChanged = true;
			},
			failure : function(form, action) {
				switch (action.failureType) {
					case Ext.form.action.Action.CLIENT_INVALID :
						Ext.Msg
								.alert('Failure',
										'Form fields may not be submitted with invalid values');
						break;
					case Ext.form.action.Action.CONNECT_FAILURE :
						Ext.Msg.alert('Failure', 'Ajax communication failed');
						break;
					case Ext.form.action.Action.SERVER_INVALID :
						Ext.Msg.alert('Failure', action.result.msg);
				}
				from.reset();
			}
		});
	}

	var gridpanel = Ext.create('Ext.search.SearchGridPanel', {
				fit : true,
				store : store,
				region : 'center',
				columnLines : true,
				enableColumnHide : eastcom.enableColumnHide,
				columns : [{
							text : ROLE_FULLNAME,
							width : 200,
							sortable : false,
							dataIndex : 'nameCn',
							align : 'center'
						}, {
							text : ROLE_NAME,
							width : 200,
							sortable : false,
							dataIndex : 'name',
							align : 'center'
						}, {
							text : ROLE_USER_COUNT,
							width : 70,
							sortable : false,
							dataIndex : 'userNum',
							renderer : renderUserNum,
							align : 'center'
						}, {
							text : ROLE_RES_COUNT,
							width : 110,
							sortable : false,
							dataIndex : 'resourceNum',
							renderer : renderResource,
							align : 'center'
						}, {
							text : ROLE_CREATOR,
							width : 120,
							sortable : false,
							dataIndex : 'creator',
							align : 'center'
						}, {
							text : ROLE_CREATE_TIME,
							width : 140,
							sortable : false,
							dataIndex : 'createTime',
							align : 'center'
						}, {
							text : ROLE_DESC,
							flex : 1,
							sortable : false,
							dataIndex : 'description',
							minWidth : 60
						}],
				viewConfig : {
					stripeRows : true
				}
			});

	function renderResource(value, metaData, record, rowIndex) {
		var id = record.get('id');
		var name = record.get('name');
		if (parseInt(value) > 0) {
			return '<a style="cursor: pointer;color:blue;text-decoration:underline;" onclick="showRoleResource(\''
					+ id + '\',\'' + name + '\')">' + value + '</a>';
		} else {
			return value;
		}
	}
	function renderUserNum(value, metaData, record, rowIndex) {
		var id = record.get('id');
		if (parseInt(value) > 0) {
			return '<a style="cursor: pointer;color:blue;text-decoration:underline;" onclick="showRoleUser(\''
					+ id + '\')">' + value + '</a>';
		} else {
			return value;
		}
	}

	function showRoleResource(id, name) {
		Ext.create('Ext.accredit.RoleAccreditWin', {
					roleId : id,
					roleName : name,
					onlyShow : true
				}).show();
	}

	function showRoleUser(value) {
		var win = Ext.getCmp('roleUserWin');
		if (win) {
			win.close();
		}
		Ext.create('Ext.role.RoleUserWin', {
					title : ROLE_USER_LIST,
					roleId : value
				}).show();
	}

	/** 初始化资源面板 */
	function initComponent() {
		Ext.create('Ext.Viewport', {
			layout : {
				type : 'border',
				padding : 5
			},
			items : [],
			listeners : {
				afterrender : function() {
					var me = this;
					var callback = function(userInfo) {
						me.add(gridpanel);
						Ext.getCmp('roleMainGrid').currentUsername = userInfo.username;
						Ext.getCmp('roleMainGrid').currentUserType = userInfo.userLevelType;
					}
					eastcom.getCurrentUserBasicInfo(callback);
				}
			}
		});
	}

	return {
		initComponent : initComponent,
		saveRole : saveRole,
		showRoleUser : showRoleUser,
		showRoleResource : showRoleResource
	};
})();