Ext.define('Ext.user.userAccreditTree', {
	extend : 'Ext.ux.tree.TreeWithDisabled',
	requires : ['Ext.data.*'],
	animate : false,
	border : false,
	userId : '',
	controllerName : '',
	unEditableIds : [],// 不可编辑(没有权限)id集合
	toBeUnCheckedIds : [],// 当前取消勾选id集合
	toBeCheckedIds : [],// 当前增加的勾选集合
	id : 'userAccreditTree',
	showMask : false,
	showLoadMask : function() {
		var me = this;
		if (me.showMask) {
			me.getEl().mask(MSG_DATA_LOADING);
		} else {
			setTimeout(function() {
						me.showLoadMask()
					}, 300);
		}
	},
	listeners : {
		afterrender : function() {
			this.showMask = true;
		},
		beforeload : function() {
			this.showLoadMask();
		},
		load : function(theTree, node) {
			var theTree = this;
			theTree.toBeUnCheckedIds = [];
			theTree.toBeCheckedIds = [];
			theTree.unEditableIds = [];
			theTree.initUnEditableIds(theTree.getRootNode());
			theTree.getEl().unmask();
			theTree.showMask = false;
		},
		checkchange : function(node, checked) {
			var me = this;
			var illegal = false;// 是否非法操作(对不具有权限的id进行操作)
			for (i in me.unEditableIds) {
				if (node.data.id == me.unEditableIds[i]) {
					illegal = true;
					break;
				}
			}
			if (illegal == false) {
				if (checked) {// 勾选情况
					var exsistFlag = false;// 在取消勾选列表中是否存在该id
					var newToBeUnCheckedIds = [];
					for (i in me.toBeUnCheckedIds) {
						if (me.toBeUnCheckedIds[i] == node.data.id) {
							exsistFlag = true;
						} else {
							newToBeUnCheckedIds.push(me.toBeUnCheckedIds[i]);
						}
					}
					me.toBeUnCheckedIds = newToBeUnCheckedIds;
					if (exsistFlag == false) {
						me.toBeCheckedIds.push(node.data.id);
					}
				} else {// 取消勾选情况
					var exsistFlag = false;// 在勾选列表中是否存在该id
					var newToBeCheckedIds = [];
					for (i in me.toBeCheckedIds) {
						if (me.toBeCheckedIds[i] == node.data.id) {
							exsistFlag = true;
						} else {
							newToBeCheckedIds.push(me.toBeCheckedIds[i]);
						}
					}
					me.toBeCheckedIds = newToBeCheckedIds;
					if (exsistFlag == false) {
						me.toBeUnCheckedIds.push(node.data.id);
					}
				}
				if (me.toBeUnCheckedIds.length > 0
						|| me.toBeCheckedIds.length > 0) {
					Ext.getCmp('saveUserAccredit').deptChange = true;
				} else {
					Ext.getCmp('saveUserAccredit').deptChange = false;
				}
				Ext.getCmp('saveUserAccredit').fireChange();
			}
		}
	},
	initUnEditableIds : function(node) {
		var me = this;
		var childs = node.childNodes;
		if (childs) {
			for (var i = 0; i < childs.length; i++) {
				if (childs[i].data.disabled == 'true') {
					me.unEditableIds.push(childs[i].data.id);
				}
				me.initUnEditableIds(childs[i]);
			}
		}
	},
	initComponent : function() {
		var me = this;
		Ext.define('Deparment', {
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
		me.frame = false;
		me.border = false;
		me.rootVisible = false;
		me.useArrows = true;
		me.tbar = [{
					xtype : 'button',
					iconCls : 'icon-open-all',
					text : BUTTON_EXPAND_ALL,
					handler : function() {
						me.expandAll();
					}
				}, '-', {
					xtype : 'button',
					iconCls : 'icon-close-all',
					text : BUTTON_COLLAPSE_ALL,
					handler : function() {
						me.collapseAll();
					}
				}];
		me.store = Ext.create('Ext.data.TreeStore', {
			model : 'Deparment',
			proxy : {
				type : 'ajax',
				actionMethods : 'POST',
				url : eastcom.baseURL + '/sysmng/security/' + me.controllerName,
				extraParams : {
					userId : me.userId
				}
			},
			sorters : {
				property : 'oder',
				direction : 'ASC'
			}
		});
		this.callParent();
	}
});