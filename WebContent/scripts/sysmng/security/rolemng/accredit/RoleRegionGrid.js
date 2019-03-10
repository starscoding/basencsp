Ext.define('Ext.accredit.RoleRegionGrid', {
	extend : 'Ext.grid.Panel',
	requires : ['Ext.grid.*', 'Ext.data.*'],
	columns : [{
				text : ROLE_AUTH_REGION,
				flex : 1,
				sortable : false,
				dataIndex : 'nameCn',
				align : 'left'
			}],
	columnLines : true,
	enableColumnHide : eastcom.enableColumnHide,
	roleId : '',
	onlyShow : false,
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
		Ext.define('Region', {
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
								name : 'checked',
								type : 'string'
							}, {
								name : 'disabled',
								type : 'string'
							}]
				});
		me.store = Ext.create('Ext.data.Store', {
					model : 'Region',
					autoLoad : true,
					proxy : {
						type : 'ajax',
						url : eastcom.baseURL
								+ '/sysmng/security/queryRoleRegionAccreditGridData',
						reader : {
							type : 'json',
							root : 'data'
						},
						extraParams : {
							roleId : me.roleId,
							onlyShow : me.onlyShow
						},
						actionMethods : {
							read : 'POST'
						},
						timeout : 180000
					},
					listeners : {
						load : function(store, records) {
							me.unEditableIds = [];
							me.initUnEditableIds(records);
							me.getSelectionModel().selectAll();
							for (var i = 0; i < records.length; i++) {
								if (records[i].get('checked') != 'true') {
									me.getSelectionModel().deselect(i);
								}
							}
							me.toBeUnCheckedIds = [];
							me.toBeCheckedIds = [];
							if (Ext.getCmp('saveRoleAccredit')) {
								Ext.getCmp('saveRoleAccredit').regionChange = false;
								Ext.getCmp('saveRoleAccredit').fireChange();
							}
							me.getEl().unmask();
						}
					}
				});
	},
	listeners : {
		afterlayout : function() {
			var me = this;
			if (me.store && me.store.isLoading()) {
				me.getEl().mask(MSG_DATA_LOADING);
			}
		},
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
				Ext.getCmp('saveRoleAccredit').regionChange = true;
			} else {
				Ext.getCmp('saveRoleAccredit').regionChange = false;
			}
			Ext.getCmp('saveRoleAccredit').fireChange();
		}
	},
	initComponent : function() {
		var me = this;
		if (me.onlyShow == false) {
			me.selModel = Ext.create(
					'Ext.ux.selection.CheckboxModelWithDisabled', {});
		}
		me.initStore();
		this.callParent();
	}
});