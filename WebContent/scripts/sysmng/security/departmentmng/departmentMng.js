/**
 * 部门管理
 */
Ext.Loader.setConfig({
			enabled : true
		});
Ext.Loader.setPath('Ext.ux', extDir + '/ux');
Ext.Loader.setPath('Ext.components', '../../../../static/commonjs/components');
Ext.Loader.setPath('Ext.role',
		'../../../../scripts/sysmng/security/rolemng/role');
Ext.require(['Ext.form.*', 'Ext.data.*', 'Ext.grid.*', 'Ext.tree.*',
		'Ext.ux.form.ValidatorTextField']);

eastcom.modules.departmentMng = (function() {

	var newOptType = 'child'; // 新增类型：child:新增子节点；brother:新增兄弟节点；
	var nowKey = '';// 当前搜索关键字,默认为空
	var isMovingItem = false;// 标识当前是否在修改部门顺序
	var orderChangedParents = [];// 部门移动之后有更改的父部门集合
	var nodeExpandedIds = [];// 当前已展开的部门id
	var oldParentMap = new Ext.util.HashMap();// 记录当前移动节点原先的父部门，更改redis数据使用

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

	Ext.define('UserSimple', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'userNameCn',
							type : 'string'
						}]
			});

	var store = Ext.create('Ext.data.TreeStore', {
				model : 'Deparment',
				proxy : {
					type : 'ajax',
					actionMethods : 'POST',
					url : eastcom.baseURL
							+ '/sysmng/security/findAllDeptTreeData',
					extraParams : {
						name : '',
						expanded : false
					}
				},
				sorters : {
					property : 'order',
					direction : 'ASC'
				},
				listeners : {
					load : function() {
						if (treepanel && treepanel.getEl()) {
							treepanel.getEl().unmask();
						}
					}
				}
			});

	var deptUserStore = Ext.create('Ext.data.Store', {
				model : 'UserSimple',
				proxy : {
					type : 'ajax',
					url : eastcom.baseURL + '/sysmng/security/queryDeptUsers',
					reader : {
						type : 'json',
						root : 'data'
					},
					actionMethods : {
						read : 'POST'
					},
					timeout : 180000
				}
			});

	function getOrderNumber(id) {
		var index = 0;
		var node = store.getNodeById(id);
		if (node.hasChildNodes()) {
			var size = node.childNodes.length;
			var lastChild = node.getChildAt(size - 1);
			var order = lastChild.data.order;
			index = order + 1;
		}
		return index;
	}

	/** 新增子节点按钮 */
	var addDeptAction = Ext.create('Ext.Action', {
				iconCls : 'icon-add',
				text : BUTTON_NEW_CHILD,
				hidden : eastcom.isPermitted('departmentmng', 'add'),
				handler : function(widget, event) {
					var rec = treepanel.getSelectionModel().getSelection()[0];
					windowPanel.getComponent(0).getForm().reset();
					var order = 0;
					if (rec) {
						var pid = rec.get('id');
						Ext.getCmp('pid').setValue(pid);
						order = getOrderNumber(pid);
					} else {
						order = getOrderNumber('root');
					}
					Ext.getCmp('order').setValue(order);
					newOptType = 'child';
					Ext.getCmp('name').resetConfig();
					Ext.getCmp('nameCn').resetConfig();
					windowPanel.setTitle(BUTTON_ADD);
					windowPanel.enableName(true);
					windowPanel.show();
				}
			});

	/** 新增兄弟节点按钮 */
	var addBrotherAction = Ext.create('Ext.Action', {
				iconCls : 'icon-add',
				disabled : true,
				text : BUTTON_NEW_BROTHER,
				hidden : eastcom.isPermitted('departmentmng', 'add'),
				handler : function(widget, event) {
					var rec = treepanel.getSelectionModel().getSelection()[0];
					windowPanel.getComponent(0).getForm().reset();
					var order = 0;
					if (rec) {
						var parentNode = rec.parentNode;
						if (parentNode) {
							if (!parentNode.isRoot()) {
								var pid = parentNode.get('id');
								Ext.getCmp('pid').setValue(pid);
								order = getOrderNumber(pid);
							} else {
								order = getOrderNumber('root');
							}
						}
						Ext.getCmp('order').setValue(order);
						newOptType = 'brother';
						Ext.getCmp('name').resetConfig();
						Ext.getCmp('nameCn').resetConfig();
						windowPanel.setTitle(BUTTON_ADD);
						windowPanel.enableName(true);
						windowPanel.show();
					}
				}
			});

	/** 编辑节点按钮 */
	var editDeptAction = Ext.create('Ext.Action', {
				iconCls : 'icon-edit',
				disabled : true,
				hidden : eastcom.isPermitted('departmentmng', 'update'),
				text : BUTTON_EDIT,
				handler : function(widget, event) {
					var view = treepanel.getView();
					var record = view.getSelectionModel().getLastSelected();
					if (record) {
						var form = windowPanel.down('form').getForm();
						form.reset();
						Ext.getCmp('name').resetConfig();
						Ext.getCmp('nameCn').resetConfig();
						form.loadRecord(record);
						windowPanel.nowRecord = record;
						windowPanel.setTitle(BUTTON_EDIT);
						windowPanel.enableName(false);
						windowPanel.show();
					}
				}
			});

	/** 删除节点按钮 */
	var delDeptAction = Ext.create('Ext.Action', {
		iconCls : 'icon-remove',
		disabled : true,
		hidden : eastcom.isPermitted('departmentmng', 'delete'),
		text : BUTTON_DELETE,
		handler : function(widget, event) {
			var rec = treepanel.getSelectionModel().getSelection()[0];
			Ext.Msg.show({
				title : MSG_SURE,
				msg : DEP_DEL_WARN,
				buttons : Ext.Msg.YESNO,
				icon : Ext.Msg.WARNING,
				fn : function(btn) {
					if (btn == 'yes') {
						var parentNode = rec.parentNode;
						if (rec) {
							var id = rec.get('id');
							var label = rec.get('nameCn');
							treepanel.getEl().mask(DEL_DELETING);
							Ext.Ajax.request({
										url : eastcom.baseURL
												+ '/sysmng/security/deleteDepartment',
										timeout : 60000,
										params : {
											id : id,
											label : label
										},
										success : function(response) {
											var text = response.responseText;
											var rslt = Ext.JSON.decode(text);
											if (rslt.success == 'true') {
												unSelectNode();
												rec.remove();
												if (parentNode) {
													// 如果删除的节点的父节点没有其他子节点那么更新节点的属性:leaf:true
													if (!parentNode.isRoot()
															&& !parentNode
																	.isExpandable()) {
														parentNode.set('leaf',
																true);
													}
												}
												Ext.Msg.show({
															title : MSG_SUCCESS,
															msg : rslt.msg,
															buttons : Ext.Msg.OK,
															icon : Ext.Msg.INFO
														});
											} else {
												Ext.Msg.show({
															title : MSG_FAILURE,
															msg : rslt.msg,
															buttons : Ext.Msg.OK,
															icon : Ext.Msg.ERROR
														});
											}
											treepanel.getEl().unmask();
										},
										failure : function() {
											Ext.Msg.show({
														title : MSG_FAILURE,
														msg : rslt.msg,
														buttons : Ext.Msg.OK,
														icon : Ext.Msg.ERROR
													});
											treepanel.getEl().unmask();
										}
									});
						}
					}
				}
			});
		}
	});

	/** 更改排序按钮 */
	var orderDeptAction = Ext.create('Ext.Action', {
		iconCls : 'icon-order',
		disabled : false,
		text : BUTTON_CHANGE_ORDER,
		handler : function(widget, event) {
			if (isMovingItem) {
				treepanel.getEl().mask(MSG_DATA_SAVING);
				var sendStr = '[';// 组装json字符串
				for (var i = 0; i < orderChangedParents.length; i++) {
					var pNode = orderChangedParents[i];
					var jsonStr = '{';
					jsonStr += ('"id":"'
							+ (pNode.data.id == 'root' ? '' : pNode.data.id) + '"');
					if (pNode.childNodes.length > 0) {
						jsonStr += ',"childs":[';
						for (var j = 0; j < pNode.childNodes.length; j++) {
							var oldParentId = oldParentMap.get(pNode
									.getChildAt(j).data.id);
							if (oldParentId) {
								if (oldParentId == 'root') {
									oldParentId = '';
								}
							} else {
								oldParentId = 'none';
							}
							jsonStr += ('{"id":"' + pNode.getChildAt(j).data.id
									+ '","order":"' + j + '","oldParentId":"'
									+ oldParentId + '"},');
						}
						jsonStr = jsonStr.substr(0, jsonStr.length - 1);
						jsonStr += ']';
					} else {
						jsonStr += ',"childs":[]';
					}
					jsonStr += '}';
					sendStr += (jsonStr + ',');
				}
				if (sendStr.length > 1) {
					sendStr = sendStr.substr(0, sendStr.length - 1);
				}
				sendStr += ']';
				Ext.Ajax.request({
							url : eastcom.baseURL
									+ '/sysmng/security/changeParentAndOrder',
							timeout : 60000,
							params : {
								jsonString : sendStr
							},
							success : function(response) {
								var result = Ext.JSON
										.decode(response.responseText);
								if (result.success == 'true') {
									Ext.Msg.show({
												title : MSG_SUCCESS,
												msg : result.msg,
												buttons : Ext.Msg.OK,
												icon : Ext.Msg.INFO
											});
									endMoving();
								} else {
									Ext.Msg.show({
												title : MSG_FAILURE,
												msg : result.msg,
												buttons : Ext.Msg.OK,
												icon : Ext.Msg.ERROR
											});
								}
								treepanel.getEl().unmask();
							},
							failure : function() {
								Ext.Msg.show({
											title : MSG_FAILURE,
											msg : result.msg,
											buttons : Ext.Msg.OK,
											icon : Ext.Msg.ERROR
										});
								treepanel.getEl().unmask();
							}
						});
			} else {
				startMoving();
			}
		}
	});

	/**
	 * 取消部门顺序更改按钮
	 */
	var cancelOrderDeptAction = Ext.create('Ext.Action', {
				iconCls : 'icon-cancel',
				hidden : true,
				disabled : false,
				text : BUTTON_CANCEL_CHANGE_ORDER,
				handler : function() {
					treepanel.getEl().mask(MSG_DATA_LOADING);
					store.load({
								callback : function() {
									autoExpandNodes(treepanel.getRootNode());
									endMoving();
								}
							});
				}
			});

	/**
	 * 查询组件
	 */
	var searchTrigger = Ext.create('Ext.components.CommonSearchTrigger', {
				searchAction : searchDept
			});
	/**
	 * 重新加载树后自动展开当前已经展开的节点
	 */
	function autoExpandNodes(node) {
		var childs = node.childNodes;
		for (var i = 0; i < childs.length; i++) {
			var childNode = childs[i];
			if (idInArray(childNode.data.id)) {
				childNode.expand(false, function() {
							autoExpandNodes(this);
						});
			}
		}
	}

	function idInArray(id) {
		var flag = false;
		for (var i = 0; i < nodeExpandedIds.length; i++) {
			if (nodeExpandedIds[i] == id) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	function startMoving() {
		cancelOrderDeptAction.show();
		addDeptAction.disable();
		addBrotherAction.disable();
		editDeptAction.disable();
		delDeptAction.disable();
		orderDeptAction.setText(BUTTON_SAVE_CHANGE_ORDER);
		isMovingItem = true;
	}

	function endMoving() {
		cancelOrderDeptAction.hide();
		addDeptAction.enable();
		orderDeptAction.setText(BUTTON_CHANGE_ORDER);
		isMovingItem = false;
		resetMovingData();
	}

	function resetMovingData() {
		orderChangedParents = [];
		nodeExpandedIds = [];
		oldParentMap = new Ext.util.HashMap();
	}

	// 添加下级、修改节点
	function addNode(data, opt) {
		var attributes = {
			id : data.id,
			name : data.name,
			nameCn : data.nameCn,
			order : data.order,
			desc : data.desc,
			leaf : data.leaf
		};
		var view = treepanel.getView();
		var node = view.getSelectionModel().getSelection()[0];
		// 0:新增 1：修改
		if (opt == 0) {
			// 如果是叶子节点，那么先将它状态修改为leaf = false;
			if (node) {
				// 如果是新增子节点
				if (newOptType == 'child') {
					node.appendChild(attributes);
					if (node.isLeaf())
						node.data.leaf = false;
					node.expand();
				} else { // 新增兄弟节点
					var parentNode = node.parentNode;
					if (parentNode) {
						parentNode.appendChild(attributes);
					}
				}
			} else {
				var rootNode = treepanel.getRootNode();
				rootNode.appendChild(attributes);
			}
		} else {
			// 编辑时，只更新该节点的信息
			Ext.Object.each(attributes, function(key, value, myself) {
				node.set(key, value);
					// node.data[key] = value;
					// node.updateInfo({
					// key : value
					// });
				});
		}
	}

	// 显示部门账户窗口
	function showDeptUser(id) {
		var departmentUserWin = Ext.create('Ext.window.Window', {
					title : DEP_USER_LIST,
					layout : 'fit',
					id : 'departmentUserWin',
					height : 370,
					width : 300,
					resizable : false,
					items : [Ext.create('Ext.grid.Panel', {
								columns : [{
											text : FULLNAME,
											flex : 1,
											dataIndex : 'userNameCn',
											sortable : false,
											align : 'left'
										}],
								columnLines : true,
								enableColumnHide : eastcom.enableColumnHide,
								store : deptUserStore,
								listeners : {
									selectionchange : function(me) {
										var record = me.getSelection()[0];
										if (Ext.getCmp('userBasicInfoWin')) {
											Ext.getCmp('userBasicInfoWin')
													.loadUserInfo(record
															.get('id'));
										} else {
											var userInfoWin = Ext.create(
													'Ext.role.UserBasicInfo',
													{});
											var position = Ext
													.getCmp('departmentUserWin')
													.getPosition();
											Ext.getCmp('departmentUserWin')
													.setPosition(
															position[0] - 150,
															position[1]);
											userInfoWin.setPosition(position[0]
															+ 150, position[1]);
											userInfoWin.show();
											userInfoWin.loadUserInfo(record
													.get('id'));
										}
									}
								}
							})],
					buttons : [{
								text : BUTTON_CANCEL,
								handler : function() {
									this.ownerCt.ownerCt.close();
								}
							}],
					listeners : {
						move : function(me, x, y) {
							if (Ext.getCmp('userBasicInfoWin')) {
								Ext.getCmp('userBasicInfoWin').setPosition(
										x + 300, y);
							}
						},
						show : function() {
							Ext.getBody().mask();
						},
						close : function() {
							if (Ext.getCmp('userBasicInfoWin')) {
								Ext.getCmp('userBasicInfoWin').close();
							}
							Ext.getBody().unmask();
						}
					}
				});
		deptUserStore.getProxy().extraParams = {
			id : id
		};
		departmentUserWin.show(null, function() {
					deptUserStore.load();
				}, null);
	}

	/** 右键快捷操作 */
	var contextMenu = Ext.create('Ext.menu.Menu', {
				items : [addDeptAction, addBrotherAction, editDeptAction,
						delDeptAction, orderDeptAction]
			});

	/** 是否加载内存 */
	function change(val) {
		if (val == '0') {
			return '<span style="color:red;">否</span>';
		} else if (val == '1') {
			return '<span style="color:green;">是</span>';
		}
		return val;
	}

	function nameRender(value, metaData, record, rowIndex) {
		var reStr = '<span class="x-livesearch-match">' + nowKey + '</span>';
		return value.replace(nowKey, reStr);
	}

	// 取消选择所有节点
	function unSelectNode() {
		var view = treepanel.getView();
		view.getSelectionModel().deselectAll();
	}

	/** 查询部门 */
	function searchDept(name) {
		if (name.length == 0 || name.length >= 2) {
			var proxy = store.proxy;
			proxy.extraParams = {
				name : name,
				expanded : name.length >= 2
			};
			nowKey = name;
			treepanel.getEl().mask(MSG_DATA_LOADING);
			store.load();
		} else {
			Ext.Msg.show({
						title : MSG_TITLE,
						msg : DEP_KEY_LEAST2,
						buttons : Ext.Msg.OK,
						icon : Ext.Msg.INFO
					});
		}
	}

	var treepanel = Ext.create('Ext.tree.Panel', {
				region : 'center',
				useArrows : true,
				fit : true,
				animate : false,
				stateful : true,
				rootVisible : false,
				store : store,
				autoHeight : true,
				enableColumnHide : eastcom.enableColumnHide,
				columns : [{
							xtype : 'treecolumn',
							text : DEPT_LABEL,
							width : 450,
							sortable : false,
							dataIndex : 'nameCn',
							renderer : nameRender
						}, {
							text : NAME,
							width : 230,
							sortable : false,
							dataIndex : 'name',
							align : 'center'
						}, {
							text : DEPT_DESC,
							flex : 2,
							dataIndex : 'desc',
							sortable : false,
							align : 'center',
							minWidth : 200
						}],
				dockedItems : [{
					xtype : 'toolbar',
					items : [addDeptAction, '-', addBrotherAction, '-',
							editDeptAction, '-', delDeptAction, '-',
							orderDeptAction, '-', cancelOrderDeptAction, '->',
							searchTrigger]
				}],
				viewConfig : {
					overflowY : 'auto',
					overflowX : 'hidden',
					stripeRows : true,
					listeners : {
						itemcontextmenu : function(view, rec, node, index, e) {
							e.stopEvent();
							contextMenu.showAt(e.getXY());
							return false;
						}
					},
					plugins : {
						ptype : 'treeviewdragdrop',
						containerScroll : true
					}
				},
				listeners : {
					afterlayout : function() {
						var me = this;
						if (me.store && me.store.isLoading()) {
							me.getEl().mask(MSG_DATA_LOADING);
						}
					},
					selectionchange : function(sm, selections) {
						if (isMovingItem == false) {
							if (selections.length) {
								var depth = selections[0].data.depth;
								if(depth == 1){
									addBrotherAction.disable();
								}else{
									addBrotherAction.enable();
								}
								editDeptAction.enable();
								delDeptAction.enable();
								orderDeptAction.enable();
							} else {
								addBrotherAction.disable();
								editDeptAction.disable();
								delDeptAction.disable();
								orderDeptAction.disable();
							}
						}
					},
					select : function(rowModel, record, index, opts) {
						if (record) {
							// var store = this.getStore();
							// var node = store.getNodeById(record.data.id);
							// node.expand();
						}
					},
					itemmove : function(node, oldParent, newParent, index,
							eOpts) {
						if (isMovingItem == false) {
							startMoving();
						}
						orderChangedParents = orderChangedParents
								.distinctPush(newParent);
						orderChangedParents = orderChangedParents
								.distinctPush(oldParent);
						if (oldParent.data.id != newParent.data.id) {// 父节点更改，将oldParent加入map
							oldParentMap.add(node.data.id, oldParent.data.id);
						}
					},
					afteritemexpand : function(node) {
						nodeExpandedIds = nodeExpandedIds
								.distinctPush(node.data.id);
					},
					afteritemcollapse : function(node) {
						var index = 0;
						for (var i = 0; i < nodeExpandedIds.length; i++) {
							if (node.data.id == nodeExpandedIds[i]) {
								index = i;
								break;
							}
						}
						nodeExpandedIds.splice(index, 1);
					}
				}
			});

	/** 新增资源窗口 */
	var windowPanel = Ext.create('Ext.window.Window', {
		title : BUTTON_NEW,
		closeAction : 'hide',
		id : 'windowPanel',
		resizable : false,
		layout : 'fit',
		nowRecord : null,
		modal : true,
		height : currentUserTheme == 'neptune' ? 300 : 280,
		width : 400,
		enableName : function(flag) {
			flag ? Ext.getCmp('name').enable() : Ext.getCmp('name').disable();
		},
		items : [{
			xtype : 'form',
			bodyPadding : 5,
			border : false,
			defaultType : 'textfield',
			fieldDefaults : {
				labelAlign : 'top',
				msgTarget : 'side'
			},
			items : [{
						xtype : 'hiddenfield',
						id : 'id',
						name : 'id'
					}, {
						xtype : 'hiddenfield',
						id : 'pid',
						name : 'pid'
					}, {
						xtype : 'container',
						anchor : '100%',
						layout : 'hbox',
						items : [{
							xtype : 'container',
							flex : 1,
							layout : 'anchor',
							items : [Ext.create(
									'Ext.ux.form.ValidatorTextField', {
										fieldLabel : NAME,
										name : 'name',
										id : 'name',
										afterLabelTextTpl : '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
										allowBlank : false,
										anchor : '95%',
										controllerPath : eastcom.baseURL
												+ '/sysmng/security/deptNameExsistCheck',
										typeFlag : 'name',
										errorMsg : MSG_NAME_EXSIST
									})]
						}, {
							xtype : 'container',
							flex : 1,
							layout : 'anchor',
							items : [Ext.create(
									'Ext.ux.form.ValidatorTextField', {
										fieldLabel : DEPT_LABEL,
										id : 'nameCn',
										name : 'nameCn',
										afterLabelTextTpl : '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
										anchor : '100%',
										allowBlank : false,
										controllerPath : eastcom.baseURL
												+ '/sysmng/security/deptNameExsistCheck',
										typeFlag : 'nameCn',
										errorMsg : MSG_NAME_EXSIST
									})]
						}]
					}, {
						xtype : 'textareafield',
						fieldLabel : DEPT_DESC,
						name : 'desc',
						id : 'desc',
						anchor : '100%',
						height : 150
					}, {
						xtype : 'hiddenfield',
						id : 'order',
						name : 'order',
						value : 0,
						allowBlank : false
					}],
			buttons : [{
				text : BUTTON_OK,
				// formBind : true,
				handler : function() {
					var form = this.up('form').getForm();
					// 0:新增 1：修改
					var opt = Ext.getCmp('id').getValue() == '' ? 0 : 1;
					if (form.isValid()) {
						var win = this.ownerCt.ownerCt.ownerCt;
						win.getEl().mask(MSG_DATA_SAVING);
						form.submit({
							clientValidation : true,
							url : eastcom.baseURL
									+ '/sysmng/security/saveDepartment?opt='
									+ opt,
							params : {
								name : Ext.getCmp('name').getValue()
							},
							success : function(form, action) {
								if (action.result.success == 'true') {
									if (opt == 0) {
										addNode(action.result.data, opt);
									} else {
										var record = Ext.getCmp('windowPanel').nowRecord;
										record.set('name', Ext.getCmp('name')
														.getValue());
										record.set('nameCn', Ext
														.getCmp('nameCn')
														.getValue());
										record.set('desc', Ext.getCmp('desc')
														.getValue());
									}
									Ext.Msg.show({
												title : MSG_SUCCESS,
												msg : action.result.msg,
												buttons : Ext.Msg.OK,
												icon : Ext.Msg.INFO
											});
								} else {
									Ext.Msg.show({
												title : MSG_FAILURE,
												msg : action.result.msg,
												buttons : Ext.Msg.OK,
												icon : Ext.Msg.ERROR
											});
								}
								win.getEl().unmask();
								windowPanel.hide();
							},
							failure : function(form, action) {
								switch (action.failureType) {
									case Ext.form.action.Action.CLIENT_INVALID :
										Ext.Msg
												.alert('Failure',
														'Form fields may not be submitted with invalid values');
										break;
									case Ext.form.action.Action.CONNECT_FAILURE :
										Ext.Msg.alert('Failure',
												'Ajax communication failed');
										break;
									case Ext.form.action.Action.SERVER_INVALID :
										Ext.Msg.alert('Failure',
												action.result.msg);
								}
								Ext.Msg.show({
											title : MSG_FAILURE,
											msg : action.result.msg,
											buttons : Ext.Msg.OK,
											icon : Ext.Msg.ERROR
										});
								win.getEl().unmask();
								windowPanel.hide();
							}
						});
					}
				}
			}, {
				text : BUTTON_CANCEL,
				handler : function() {
					this.up('form').getForm().reset();
					windowPanel.hide();
				}
			}]
		}]
	});

	/** 初始化资源面板 */
	function initComponent() {
		Ext.create('Ext.Viewport', {
					layout : {
						type : 'border',
						padding : 5
					},
					items : [treepanel]
				});
	}

	/** 给数组添加去重加入方法（如数组中存在则不添加，不存在则添加） */
	Array.prototype.distinctPush = function(obj) {
		var newArr = [], me = this, pushFlag = true;
		for (var i = 0; i < me.length; i++) {
			if (obj != me[i]) {
				newArr.push(me[i]);
			} else {
				pushFlag = false;
				newArr.push(obj);
			}
		}
		if (pushFlag) {
			newArr.push(obj);
		}
		return newArr;
	}
	return {
		initComponent : initComponent,
		showDeptUser : showDeptUser
	};
})();