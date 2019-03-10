/**
 * 角色选择窗口控件
 * 
 * @author JJF Mar 16, 2015
 */

Ext.define('Ext.components.ModuleChooserWin', {
	extend : 'Ext.window.Window',
	alias : 'widget.moduleselectwin',
	layout : 'border',
	split : true,
	byCurrentUser : true,// 根据当前用户权限加载，false为加载系统内的所有角色
	multiSelect : true,// 是否允许多选
	submitHandler : function(value) {// 确定按钮点击事件
	},
	items : [],
	getPanel : function() {
		var me = this, loadUrl;
		if (me.byCurrentUser) {
			url = '/sysmng/security/getAllRoleList';
		} else {
			url = '/sysmng/security/findAllRole';
		}

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
							}]
				});
		var store = Ext.create('Ext.data.TreeStore', {
					model : 'Role',
					proxy : {
						type : 'ajax',
						actionMethods : 'POST',
						url : eastcom.baseURL + url
					},
					listeners : {
						load : function(s, node, records) {
							// Ext.each(records, function(rec, i) {
							// });
						}
					}
				});

		var panel = Ext.create('Ext.ux.SimpleLiveSearchGridPanel', {
					store : store,
					selModel : Ext.create('Ext.selection.CheckboxModel', {})
				});

		return panel;
	},
	initComponent : function() {
		var me = this, panel = me.getPanel();
		me.items = [panel];
		me.buttons = [{
					text : BUTTON_OK,
					handler : function() {
						var _records = [];
						_records = treepanel.getSelectionModel().getSelection();
						if (typeof(me.submitHandler) == 'function') {
							me.submitHandler(_records);
						}
						me.hide();
					}
				}, {
					text : BUTTON_CANCEL,
					handler : function() {
						me.hide();
					}
				}]
		this.callParent();
	}
});
