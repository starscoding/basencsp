Ext.define('Ext.role.RoleUserWin', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.*', 'Ext.window.*'],
	roleId : '',
	layout : 'fit',
	initGrid : function() {
		var me = this;
		Ext.define('RoleUser', {
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
							}]
				});
		var roleUserStore = Ext.create('Ext.data.Store', {
					model : 'RoleUser',
					autoLoad : true,
					proxy : {
						type : 'ajax',
						url : eastcom.baseURL
								+ '/sysmng/security/queryUserInfoByRole',
						reader : {
							type : 'json',
							root : 'data'
						},
						extraParams : {
							roleId : me.roleId
						},
						actionMethods : {
							read : 'POST'
						},
						timeout : 180000
					}
				});
		var roleUserGrid = Ext.create('Ext.ux.SimpleLiveSearchGridPanel', {
			columns : [{
						text : USERNAME,
						flex : 1,
						dataIndex : 'name',
						sortable : false,
						align : 'left'
					}, {
						text : FULLNAME,
						flex : 1,
						dataIndex : 'nameCn',
						sortable : false,
						align : 'left'
					}],
			columnLines : true,
			enableColumnHide : eastcom.enableColumnHide,
			border : false,
			store : roleUserStore,
			listeners : {
				selectionchange : function(me) {
					var record = me.getSelection()[0];
					if (Ext.getCmp('userBasicInfoWin')) {
						Ext.getCmp('userBasicInfoWin').loadUserInfo(record
								.get('id'));
						Ext.getCmp('userBasicInfoWin').show();
					} else {
						var userInfoWin = Ext.create('Ext.role.UserBasicInfo',
								{});
						var position = Ext.getCmp('roleUserWin').getPosition();
						Ext.getCmp('roleUserWin').setPosition(
								position[0] - 150, position[1]);
						userInfoWin.setPosition(position[0] + 150, position[1]);
						userInfoWin.show();
						userInfoWin.loadUserInfo(record.get('id'));
					}
				}
			}
		});
		return roleUserGrid;

	},
	initComponent : function() {
		var me = this;
		me.frame = false;
		me.layout = 'fit';
		me.autoScroll = true;
		me.resizable = false;
		// me.modal = true;
		me.id = 'roleUserWin';
		me.bodyStyle = 'overflow-y:hidden;overflow-x:hidden;';
		me.items = [me.initGrid()];
		me.width = 300;
		me.height = 370;
		me.buttons = [{
					text : BUTTON_CLOSE,
					handler : function() {
						me.close();
					}
				}];
		me.listeners = {
			move : function(me, x, y) {
				if (Ext.getCmp('userBasicInfoWin')) {
					Ext.getCmp('userBasicInfoWin').setPosition(x + 300, y);
				}
			},
			close : function() {
				if (Ext.getCmp('userBasicInfoWin')) {
					Ext.getCmp('userBasicInfoWin').close();
				}
			}
		}
		this.callParent();
	}
});