/**
 * 模块（资源）选择窗口控件
 * 
 * @author JJF Sep 14, 2014
 */

Ext.define('Ext.components.ModuleChooserWin', {
			extend : 'Ext.window.Window',
			alias : 'widget.moduleselectwin',
			layout : 'border',
			split : true,
			multiSelect : true,// 是否允许多选
			submitHandler : function(value) {// 确定按钮点击事件
			},
			items : [],
			getResPanel : function() {
				var me = this;
				Ext.define('Resource', {
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
										name : 'location',
										type : 'string'
									}, {
										name : 'status',
										type : 'string'
									}, {
										name : 'order',
										type : 'int'
									}, {
										name : 'kind',
										type : 'string'
									}, {
										name : 'isShowDesktop',
										type : 'string'
									}, {
										name : 'isWebpage',
										type : 'string'
									}, {
										name : 'leaf',
										type : 'boolean'
									}]
						});
				var store = Ext.create('Ext.data.TreeStore', {
							model : 'Resource',
							proxy : {
								type : 'ajax',
								actionMethods : 'POST',
								url : eastcom.baseURL
										+ '/sysmng/security/asynchronizeGetNodes'
							},
							listeners : {
								load : function(s, node, records) {
									Ext.each(records, function(rec, i) {
												if (rec.get('isWebpage') === '1') {
													rec.set('leaf', true);
												}
												if (me.multiSelect) {
													rec.set('checked', false);
												}
											});

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
										text : RES_FULLNAME,
										width : 200,
										sortable : false,
										dataIndex : 'nameCn'
									}, {
										text : RES_NAME,
										width : 160,
										sortable : false,
										dataIndex : 'name',
										align : 'left'
									}, {
										text : RES_LOCATION,
										flex : 1,
										sortable : false,
										dataIndex : 'location',
										align : 'left'
									}],
							listeners : {
								afterlayout : function() {
									var me = this;
									if (me.store && me.store.isLoading()) {
										me.getEl().mask(MSG_DATA_LOADING);
									}
								},
								checkchange : function(node, checked) {
									if (checked
											&& node.get('isWebpage') !== '1') {
										Ext.Msg.show({
													title : MSG_TITLE,
													msg : '请勿选择文件夹',
													buttons : Ext.Msg.OK,
													icon : Ext.Msg.INFO
												});
										if (me.multiSelect) {
											node.set('checked', false);
										}
									}
								}
							}
						});
				return treepanel;
			},
			initComponent : function() {
				var me = this, treepanel = me.getResPanel();
				me.items = [treepanel];
				me.buttons = [{
					text : BUTTON_OK,
					handler : function() {
						var _records = [];
						if (me.multiSelect) {
							_records = treepanel.getChecked();
						} else {
							_records = treepanel.getSelectionModel()
									.getSelection();

						}
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
