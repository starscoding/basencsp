Ext.define('Ext.accredit.RoleRescourceTree', {
	extend : 'Ext.ux.tree.TreeWithDisabled',
	requires : ['Ext.data.*'],
	roleId : '',
	roleName : '',
	animate : false,
	tbar : [{
				xtype : 'button',
				iconCls : 'icon-open-all',
				text : BUTTON_EXPAND_ALL,
				handler : function() {
					this.ownerCt.ownerCt.expandAll();
				}
			}, '-', {
				xtype : 'button',
				iconCls : 'icon-close-all',
				text : BUTTON_COLLAPSE_ALL,
				handler : function() {
					this.ownerCt.ownerCt.collapseAll();
				}
			}],
	unEditableIds : [],// 不可编辑(没有权限)id集合
	toBeUnCheckedIds : [],// 当前取消勾选id集合
	toBeCheckedIds : [],// 当前增加的勾选集合
	setChildNode : function(childNodes, checked) {
		for (var i = 0; i < childNodes.length; i++) {
			var node = childNodes[i];
			if (node.get('checked') != checked) {
				node.set('checked', checked);
				this.setCheckedData(node, checked);
			}
			if (node.childNodes.length > 0) {
				node.expand();
				this.setChildNode(node.childNodes, checked);
			}
		}
	},
	setParentNode : function(node, checked) {
		if (node.get('checked') != checked) {
			node.set('checked', checked);
			this.setCheckedData(node, checked);
		}
		if (node.parentNode != null) {
			this.setParentNode(node.parentNode, checked);
		}

	},
	setCheckedData : function(node, checked) {
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
					if (node.data.id != 'root') {
						me.toBeCheckedIds.push(node.data.id);
					}

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
					if (node.data.id != 'root') {
						me.toBeUnCheckedIds.push(node.data.id);
					}
				}
			}
		}
	},
	listeners : {
		afterlayout : function() {
			var me = this;
			if (me.store && me.store.isLoading()) {
				me.getEl().mask(MSG_DATA_LOADING);
			}
		},
		load : function(theTree, node) {
			var theTree = this;
			theTree.toBeUnCheckedIds = [];
			theTree.toBeCheckedIds = [];
			theTree.unEditableIds = [];
			theTree.initUnEditableIds(theTree.getRootNode());
			theTree.getEl().unmask();
		},
		checkchange : function(node, checked) {
			this.setCheckedData(node, checked);
			if (checked) {
				if (node.parentNode != null
						&& node.parentNode.data.checked != null) {
					if (node.parentNode.data.checked != checked) {
						this.setParentNode(node.parentNode, checked);
					}
				}
				node.expand(false, function() {
							if (node.childNodes.length > 0) {
								this.setChildNode(node.childNodes, checked);
							}
						}, this);
			} else {
				if (node.childNodes.length > 0) {
					this.setChildNode(node.childNodes, checked);
				}
			}

			if (this.toBeUnCheckedIds.length > 0
					|| this.toBeCheckedIds.length > 0) {
				Ext.getCmp('saveRoleAccredit').resourceChange = true;
			} else {
				Ext.getCmp('saveRoleAccredit').resourceChange = false;
			}
			Ext.getCmp('saveRoleAccredit').fireChange();
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
		me.frame = false;
		me.rootVisible = false;
		me.useArrows = true;
		me.store = Ext.create('Ext.data.TreeStore', {
					model : 'Rescource',
					proxy : {
						type : 'ajax',
						actionMethods : 'POST',
						timeout : 120000,
						url : eastcom.baseURL
								+ '/sysmng/security/getRoleResourceAccreditTree',
						extraParams : {
							roleId : me.roleId,
							roleName : me.roleName,
							onlyShow : me.onlyShow
						}
					},
					sorters : {
						property : 'oder',
						direction : 'DESC'
					}
				});
		this.callParent();
	}
});