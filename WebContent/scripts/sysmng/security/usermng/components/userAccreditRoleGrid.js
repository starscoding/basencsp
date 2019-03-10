Ext.define('Ext.user.userAccreditRoleGrid', {
	extend : 'Ext.ux.SimpleLiveSearchGridPanel',
	requires : ['Ext.grid.*', 'Ext.data.*'],
	columns : [{
				text : NAME,
				flex : 1,
				dataIndex : 'nameCn',
				align : 'left'
			}],
	columnLines : true,
	enableColumnHide : eastcom.enableColumnHide,
	userId : '',
	unEditableIds : [],// 不可编辑(没有权限)id集合
	toBeUnCheckedIds : [],// 当前取消勾选id集合
	toBeCheckedIds : [],// 当前增加的勾选集合
	initUnEditableIds : function(records) {
		var me = this;
		for (i in records) {
			if (records[i].get('disabled') == 'true') {
				me.unEditableIds.push(records[i].get('id'));
			}
		}
	},
	initStore : function() {
		var me = this;
		Ext.define('Role', {
					extend : 'Ext.data.Model',
					fields : [{
								name : 'id',
								type : 'string'
							}, {
								name : 'nameCn',
								type : 'string'
							}, {
								name : 'desc',
								type : 'string'
							}, {
								name : 'name',
								type : 'string'
							}, {
								name : 'checked',
								type : 'string'
							}, {
								name : 'disabled',
								type : 'string'
							}]
				});
		me.store = Ext.create('Ext.data.Store', {
					model : 'Role',
					autoLoad : true,
					proxy : {
						type : 'ajax',
						url : eastcom.baseURL
								+ '/sysmng/security/getUserRoleAccreditData',
						reader : {
							type : 'json',
							root : 'data'
						},
						extraParams : {
							editUserId : me.userId
						},
						actionMethods : {
							read : 'POST'
						},
						timeout : 180000
					},
					listeners : {
						beforeload : function() {
							// me.getEl().mask(MSG_DATA_LOADING);
						},
						load : function(store, records) {
							if (records) {
								me.unEditableIds = [];
								me.initUnEditableIds(records);
								var selections = [];
								for (var i = 0; i < records.length; i++) {
									if (records[i].get('checked') == 'true') {
										selections.push(records[i]);
									}
								}
								me.getSelectionModel().select(selections);
								me.toBeUnCheckedIds = [];
								me.toBeCheckedIds = [];
								if (Ext.getCmp('saveUserAccredit')) {
									Ext.getCmp('saveUserAccredit').roleChange = false;
									Ext.getCmp('saveUserAccredit').fireChange();
								}
								// me.getEl().unmask();
							} else {
								Ext.Msg.show({
											title : MSG_ERROR,
											msg : USER_MULTI_AUTHORIZE_ROLE_NOT_SAME,
											buttons : Ext.Msg.OK,
											icon : Ext.Msg.ERROR
										});
								Ext.getCmp('userAccreditWin').close();
							}
						}
					}
				});
	},
	listeners : {
		select : function(obj, record) {
			this.changeSelectData(record.get('id'), true);
		},
		deselect : function(obj, record) {
			this.changeSelectData(record.get('id'), false);
		}
	},
	changeSelectData : function(id, isSelect) {
		var me = this;
		var illegal = false;// 是否非法操作(对不具有权限的id进行操作)
		for (i in me.unEditableIds) {
			if (id == me.unEditableIds[i]) {
				illegal = true;
				break;
			}
		}
		if (illegal == false) {
			if (isSelect) {// 勾选情况
				var exsistFlag = false;// 在取消勾选列表中是否存在该id
				var newToBeUnCheckedIds = [];
				for (i in me.toBeUnCheckedIds) {
					if (me.toBeUnCheckedIds[i] == id) {
						exsistFlag = true;
					} else {
						newToBeUnCheckedIds.push(me.toBeUnCheckedIds[i]);
					}
				}
				me.toBeUnCheckedIds = newToBeUnCheckedIds;
				if (exsistFlag == false) {
					me.toBeCheckedIds.push(id);
				}
			} else {// 取消勾选情况
				var exsistFlag = false;// 在勾选列表中是否存在该id
				var newToBeCheckedIds = [];
				for (i in me.toBeCheckedIds) {
					if (me.toBeCheckedIds[i] == id) {
						exsistFlag = true;
					} else {
						newToBeCheckedIds.push(me.toBeCheckedIds[i]);
					}
				}
				me.toBeCheckedIds = newToBeCheckedIds;
				if (exsistFlag == false) {
					me.toBeUnCheckedIds.push(id);
				}
			}
			if (me.toBeUnCheckedIds.length > 0 || me.toBeCheckedIds.length > 0) {
				Ext.getCmp('saveUserAccredit').roleChange = true;
			} else {
				Ext.getCmp('saveUserAccredit').roleChange = false;
			}
			Ext.getCmp('saveUserAccredit').fireChange();
		}
	},
	initComponent : function() {
		var me = this;
		me.selModel = Ext.create('Ext.ux.selection.CheckboxModelWithDisabled',
				{});
		me.initStore();
		this.callParent();
	}
});