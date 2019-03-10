Ext.define('Ext.accredit.RoleMultiAccreditWin', {
	extend : 'Ext.window.Window',
	records : [],
	type : 'ADD',// ADD ||DEL
	modal : true,
	layout : 'border',
	setData : function(records, type) {
		var me = this;
		me.records = records;
		me.type = type;
		var tree = me.query('treepanel')[0], nodes;
		tree.collapseAll();
		nodes = tree.getChecked();
		if (nodes && nodes.length) {
			$.each(nodes, function(i, n) {
						n.set('checked', false);
					});
		}
	},
	items : [Ext.create('Ext.tree.Panel', {
		region : 'center',
		rootVisible : false,
		animate : false,
		setParentNode : function(node, checked) {
			if (node.get('checked') != checked) {
				node.set('checked', checked);
			}
			if (node.parentNode != null) {
				this.setParentNode(node.parentNode, checked);
			}
		},
		setChildNode : function(childNodes, checked) {
			for (var i = 0; i < childNodes.length; i++) {
				var node = childNodes[i];
				if (node.get('checked') != checked) {
					node.set('checked', checked);
				}
				if (node.childNodes.length > 0) {
					node.expand();
					this.setChildNode(node.childNodes, checked);
				}
			}
		},
		store : Ext.create('Ext.data.TreeStore', {
					model : 'Rescource',
					proxy : {
						type : 'ajax',
						actionMethods : 'POST',
						url : eastcom.baseURL
								+ '/sysmng/security/getRoleMultiAccreditTree',
						extraParams : {}
					},
					sorters : {
						property : 'oder',
						direction : 'DESC'
					}
				}),
		listeners : {
			afterlayout : function() {
				var me = this;
				if (me.store && me.store.isLoading() && me.getEl().mask) {
					me.getEl().mask(MSG_DATA_LOADING);
				}
			},
			load : function() {
				var me = this;
				if (me.getEl().unmask) {
					me.getEl().unmask();
				}
			},
			checkchange : function(node, checked) {
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
			}
		}
	})],
	initComponent : function() {
		var me = this;
		me.buttons = [{
			text : BUTTON_OK,
			handler : function() {
				var tree = me.query('treepanel')[0], nodes = tree.getChecked(), ids = [], roleIds = [], roleNames = [], param;
				if (nodes && nodes.length) {
					$.each(nodes, function(i, n) {
								if (n.get('id') != 'root') {
									ids.push(n.get('id'));
								}
							});
				} else {
					Ext.Msg.show({
								title : MSG_TITLE,
								msg : MSG_EMPTY_SELECTION,
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.ERROR
							});
					return
				}

				$.each(me.records, function(i, rec) {
							roleIds.push(rec.get('id'));
							roleNames.push(rec.get('name'));
						});

				param = {
					roleId : roleIds.join(','),
					roleName : roleNames.join(',')
				}

				if (me.type == 'ADD') {
					param.addIdStr = ids.join(',');
				} else if (me.type == 'DEL') {
					param.delIdStr = ids.join(',');
				}
				me.getEl().mask(MSG_DATA_SAVING);
				Ext.Ajax.request({
							url : eastcom.baseURL
									+ '/sysmng/security/updateRoleResourceRange',
							method : 'POST',
							params : param,
							success : function(form, action) {
								var result = Ext.JSON.decode(form.responseText);
								if (result.success == 'true') {
									Ext.Msg.show({
												title : MSG_SUCCESS,
												msg : result.msg,
												buttons : Ext.Msg.OK,
												icon : Ext.Msg.INFO
											});
									$.each(me.records, function(i, rec) {
												var rNum = parseInt(rec
														.get('resourceNum'));
												if (me.type == 'ADD') {
													rNum = rNum + ids.length;
												} else if (me.type == 'DEL') {
													rNum = rNum - ids.length;
													rNum = rNum < 0 ? 0 : rNum;
												}
												rec.set('resourceNum', rNum);
											});
									me.hide();
								} else {
									Ext.Msg.show({
												title : MSG_FAILURE,
												msg : result.msg,
												buttons : Ext.Msg.OK,
												icon : Ext.Msg.ERROR
											});
									me.hide();
								}
								me.getEl().unmask();
							},
							failure : function(form, action) {
								Ext.Msg.show({
											title : MSG_ERROR,
											msg : MSG_ERROR_PROMPT,
											buttons : Ext.Msg.OK,
											icon : Ext.Msg.ERROR
										});
								me.getEl().unmask();
							}
						});
			}
		}, {
			text : BUTTON_CANCEL,
			handler : function() {
				me.hide();
			}
		}];
		me.callParent();
	}
});