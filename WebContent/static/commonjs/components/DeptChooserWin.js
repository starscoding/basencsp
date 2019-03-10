/**
 * 部门选择窗口控件
 * 
 * @author JJF Oct 27, 2014
 */

Ext.define('Ext.components.DeptChooserWin', {
			extend : 'Ext.window.Window',
			alias : 'widget.moduleselectwin',
			layout : 'border',
			split : true,
			submitHandler : function(value) {// 确定按钮点击事件
			},
			items : [],
			getPanel : function() {
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
										name : 'leaf',
										type : 'boolean'
									}]
						});
				var store = Ext.create('Ext.data.TreeStore', {
							model : 'Deparment',
							proxy : {
								type : 'ajax',
								actionMethods : 'POST',
								url : eastcom.baseURL
										+ '/sysmng/security/findAllDeptTreeData'
							},
							listeners : {
								load : function(s, node, records) {
									var setChilds = function(node) {
										var childs = node.childNodes;
										if (childs && childs.length) {
											Ext.each(childs, function(c, i) {
														c.data.checked = false;
														setChilds(c);
													});
										}
									}
									setChilds(node);

									if (treepanel && treepanel.getEl()) {
										treepanel.getEl().unmask();
									}
								}
							}
						});

				var treepanel = Ext.create('Ext.tree.Panel', {
							region : 'center',
							useArrows : true,
							fit : true,
							stateful : true,
							animate : false,
							rootVisible : false,
							store : store,
							autoHeight : true,
							enableColumnHide : eastcom.enableColumnHide,
							columns : [{
										xtype : 'treecolumn',
										text : NAME,
										flex : 1,
										sortable : false,
										dataIndex : 'nameCn'
									}, {
										text : NAME,
										flex : 1,
										sortable : false,
										dataIndex : 'name',
										align : 'center'
									}],
							listeners : {
								afterlayout : function() {
									var me = this;
									if (me.store && me.store.isLoading()) {
										me.getEl().mask(MSG_DATA_LOADING);
									}
								},
								checkchange : function(node, checked) {
								}
							}
						});
				return treepanel;
			},
			initComponent : function() {
				var me = this, treepanel = me.getPanel();
				me.items = [treepanel];
				me.buttons = [{
							text : BUTTON_OK,
							handler : function() {
								var _records = treepanel.getChecked();
								var recs = [];
								if (_records.length) {
									Ext.each(_records, function(node, i) {
												recs.push(node.data);
											});
								}
								if (typeof(me.submitHandler) == 'function') {
									me.submitHandler(recs);
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
