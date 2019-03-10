/**
 * 资源管理
 */
Ext.Loader.setConfig({
			enabled : true
		});
Ext.Loader.setPath('Ext.chooser',
		'../../../../scripts/sysmng/security/resourcemng/chooser');
Ext.Loader.setPath('Ext.ux', extDir + '/ux');
Ext.Loader.setPath('Ext.components', '../../../../static/commonjs/components');

Ext.require(['Ext.form.*', 'Ext.data.*', 'Ext.grid.*', 'Ext.tree.*']);

eastcom.modules.resourceMng = (function() {

	var isMovingItem = false;// 标识当前是否在修改部门顺序
	var orderChangedParents = [];// 部门移动之后有更改的父部门集合
	var nodeExpandedIds = [];// 当前已展开的部门id
	var oldParentMap = new Ext.util.HashMap();// 记录当前移动节点原先的父部门，更改redis数据使用

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
							name : 'image',
							type : 'string'
						}, {
							name : 'creator',
							type : 'string'
						}, {
							name : 'createTime',
							type : 'string'
						}, {
							name : 'remarks',
							type : 'string'
						}, {
							name : 'leaf',
							type : 'boolean'
						}, {
							name : 'totalChildnum',
							type : 'int'
						}]
			});

	var store = Ext.create('Ext.data.TreeStore', {
				model : 'Resource',
				proxy : {
					type : 'ajax',
					actionMethods : 'POST',
					timeout : 120000,
					url : eastcom.baseURL
							+ '/sysmng/security/asynchronizeGetNodes',
					extraParams : {
						name : ''
					}
				},
				listeners : {
					load : function() {
						if (treepanel && treepanel.getEl()) {
							treepanel.getEl().unmask();
						}
					}
				}
			});

	/** 新增资源按钮 */
	var addResourceAction = Ext.create('Ext.Action', {
				iconCls : 'icon-add',
				text : BUTTON_NEW,
				hidden : eastcom.isPermitted('resourcemng', 'add'),
				handler : function(widget, event) {
					var rec = treepanel.getSelectionModel().getSelection()[0];
					windowPanel.getComponent(0).getForm().reset();
					var order = 0, pid;
					if (rec) {
						pid = rec.get('id');
						Ext.getCmp('pid').setValue(pid);
						Ext.getCmp('name').otherInput = pid;
						order = getOrderNumber(pid);
					} else {
						order = getOrderNumber('root');
					}
					Ext.getCmp('order').setValue(order);
					Ext.getCmp('kind').setValue('0');
					Ext.getCmp('name').typeFlag = '0';
					Ext.getCmp('location').disableCheck = true;

					var isHidden = Ext.getCmp('containerid').isHidden();
					if (isHidden)
						Ext.getCmp('containerid').show();

					Ext.getCmp('name').enable();
					windowPanel.setTitle(BUTTON_NEW);
					windowPanel.show();
				}
			});

	/** 新增关联权限按钮 */
	var addPermissionAction = Ext.create('Ext.Action', {
				iconCls : 'icon-add',
				disabled : true,
				text : RES_NEW_AUTHORITY,
				hidden : eastcom.isPermitted('resourcemng', 'add'),
				handler : function(widget, event) {
					var rec = treepanel.getSelectionModel().getSelection()[0];
					windowPanel.getComponent(0).getForm().reset();
					var order = 0;
					if (rec) {
						var pid = rec.get('id');
						Ext.getCmp('pid').setValue(pid);
						Ext.getCmp('name').otherInput = pid;
						// order = rec.get('totalChildnum');
						order = getOrderNumber(pid);
						Ext.getCmp('order').setValue(order);
						Ext.getCmp('kind').setValue('1');
						Ext.getCmp('name').typeFlag = '1';
						Ext.getCmp('location').disableCheck = false;
						Ext.getCmp('location').otherInput = 'location';
						var isHidden = Ext.getCmp('containerid').isHidden();
						if (!isHidden)
							Ext.getCmp('containerid').hide();

						Ext.getCmp('name').enable();
						windowPanel.setTitle(RES_NEW_AUTHORITY);
						windowPanel.show();
					}
				}
			});

	/** 编辑资源按钮 */
	var editResourceAction = Ext.create('Ext.Action', {
				iconCls : 'icon-edit',
				disabled : true,
				text : BUTTON_EDIT,
				hidden : eastcom.isPermitted('resourcemng', 'update'),
				handler : function(widget, event) {
					var view = treepanel.getView();
					var record = view.getSelectionModel().getLastSelected();
					if (record) {
						var form = windowPanel.down('form').getForm();
						form.reset();
						// 如果编辑操作权限，那么隐藏containerid
						var kind = record.data.kind;
						var title = BUTTON_EDIT;
						if (kind == '1') {
							Ext.getCmp('containerid').hide();
							Ext.getCmp('location').disableCheck = false;
							Ext.getCmp('location').oldValue = record.data.location;
							Ext.getCmp('location').validatorValue = true;
							Ext.getCmp('location').otherInput = 'location';
							title = BUTTON_EDIT;
						} else {
							Ext.getCmp('containerid').show();
							Ext.getCmp('location').disableCheck = true;
							title = BUTTON_EDIT;
						}

						form.loadRecord(record);
						Ext.getCmp('name').otherInput = record.parentNode.data.id;
						Ext.getCmp('name').typeFlag = kind;

						Ext.getCmp('statusid').setValue({
									status : record.data.status
								});
						Ext.getCmp('isshowdesktopid').setValue({
									isshowdesktop : record.data.isShowDesktop
								});
						var showpage = record.data.isWebpage;
						Ext.getCmp('iswebpageid').setValue({
									iswebpage : showpage
								});
						changingImage.setSrc(getImgSrc(record.data.image));
						Ext.getCmp('name').disable();
						windowPanel.setTitle(title);
						windowPanel.show();
					}
				}
			});

	/** 删除资源按钮 */
	var delResourceAction = Ext.create('Ext.Action', {
		iconCls : 'icon-remove',
		disabled : true,
		text : BUTTON_DELETE,
		hidden : eastcom.isPermitted('resourcemng', 'delete'),
		handler : function(widget, event) {
			var rec = treepanel.getSelectionModel().getSelection()[0];
			var parentNode = rec.parentNode;
			if (rec) {
				Ext.MessageBox.confirm(MSG_SURE, DEL_BTN_INFO, function(btn) {
							if (btn != 'yes')
								return;
							var id = rec.get('id');
							treepanel.getEl().mask(MSG_DATA_OPTING);
							Ext.Ajax.request({
										url : eastcom.baseURL
												+ '/sysmng/security/deleteResource',
										timeout : 60000,
										params : {
											id : id
										},
										success : function(response) {
											var text = response.responseText;
											var rslt = Ext.JSON.decode(text);
											if (rslt.success == 'true') {
												rec.remove();
												if (parentNode) {
													// 如果删除的节点的父节点没有其他子节点那么更新节点的属性:leaf:true
													if (!parentNode.isRoot()
															&& !parentNode
																	.isExpandable()) {
														// parentNode.updateInfo({leaf:true});
														parentNode.set('leaf',
																true);// updated
														// by
														// jjf
														// @JAN
														// 14
														// 2013
													}
												}
												unSelectNode();
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
						});
			}
		}
	});

	/** 更改排序按钮 */
	var orderResourceAction = Ext.create('Ext.Action', {
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
									+ '/sysmng/security/changeResourceParentAndOrder',
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
	var cancelOrderResourceAction = Ext.create('Ext.Action', {
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
				searchAction : function(key) {
					var proxy = store.proxy;
					proxy.extraParams = {
						name : key
					};
					treepanel.getEl().mask(MSG_DATA_LOADING);
					store.load();
				}
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

	function startMoving() {
		cancelOrderResourceAction.show();
		addResourceAction.disable();
		addPermissionAction.disable();
		editResourceAction.disable();
		delResourceAction.disable();
		orderResourceAction.setText(BUTTON_SAVE_CHANGE_ORDER);
		isMovingItem = true;
	}

	function endMoving() {
		cancelOrderResourceAction.hide();
		unSelectNode();
		addResourceAction.enable();
		orderResourceAction.setText(BUTTON_CHANGE_ORDER);
		isMovingItem = false;
		resetMovingData();
	}

	function resetMovingData() {
		orderChangedParents = [];
		nodeExpandedIds = [];
		oldParentMap = new Ext.util.HashMap();
	}

	// 取消选择所有节点
	function unSelectNode() {
		var view = treepanel.getView();
		view.getSelectionModel().deselectAll();
		addResourceAction.enable();
	}
	// 添加下级、修改节点
	function addNode(data, opt) {
		var attributes = {
			id : data.id,
			name : data.name,
			nameCn : data.nameCn,
			location : data.location,
			status : data.status,
			order : data.order,
			kind : data.kind,
			isShowDesktop : data.isShowDesktop,
			isWebpage : data.isWebpage,
			image : data.image,
			creator : data.creator,
			createTime : data.createTime,
			remarks : data.remarks,
			leaf : data.leaf,
			totalChildnum : data.totalChildnum
		};
		var view = treepanel.getView();
		var node = view.getSelectionModel().getSelection()[0];
		// 0:新增 1：修改
		if (opt == 0) {
			// 如果是叶子节点，那么先将它状态修改为leaf = false;
			if (node) {
				node.appendChild(attributes);
				if (node.isLeaf()) {
					node.data.leaf = false;
					node.expand();
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

	/** 右键快捷操作 */
	var contextMenu = Ext.create('Ext.menu.Menu', {
				items : [addResourceAction, addPermissionAction,
						editResourceAction, delResourceAction,
						orderResourceAction]
			});

	/** 菜单状态渲染 */
	function changeStatus(val) {
		if (val == '0') {
			return '<span style="color:red;">弃用</span>';
		} else if (val == '1') {
			return '<span style="color:green;">启用</span>';
		} else if (val == '2') {
			return '<span style="color:tan;">隐藏</span>';
		}else if (val == '3') {
			return '<span style="color:tan;">可见</span>';
		}
		return val;
	}

	/** 资源类型渲染 */
	function changeKind(val) {
		if (val == '0') {
			return '资源菜单';
		} else if (val == '1') {
			return '<span style="color:#209FCA;">资源操作</span>';
		} 
		return val;
	}

	/** 是否渲染 */
	function changeWhether(val) {
		if (val == '0') {
			return '<span style="color:red;">否</span>';
		} else if (val == '1') {
			return '<span style="color:green;">是</span>';
		}
		return val;
	}

	/** 获取图片地址 */
	function getImgSrc(val) {
		val = (val && val.length) ? val : 'default.png';
		var url = eastcom.baseURL + '/static/images/common/appicons/';
		return url += val;
	}

	/** 显示快捷图标 */
	function showImage(val) {
		if (val) {
			return '<img id="preview" src="' + getImgSrc(val)
					+ '" height="15px" width="15px" style="diplay:block;" />';
		}
	}

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
							flex : 1,
							sortable : false,
							dataIndex : 'nameCn',
							minWidth : 240
						}, {
							text : RES_NAME,
							width : 160,
							sortable : false,
							dataIndex : 'name',
							align : 'center'
						}, {
							text : RES_LOCATION,
							width : 340,
							sortable : false,
							dataIndex : 'location',
							align : 'left'
						}, {
							text : RES_STATUS,
							width : 70,
							dataIndex : 'status',
							sortable : false,
							align : 'center',
							renderer : changeStatus
						}, {
							text : RES_TYPE,
							width : 100,
							dataIndex : 'kind',
							sortable : false,
							align : 'center',
							renderer : changeKind
						}, {
							text : RES_IS_IN_DESK,
							width : 90,
							dataIndex : 'isShowDesktop',
							sortable : false,
							align : 'center',
							renderer : changeWhether
						}, {
							text : RES_IS_WEB_PAGE,
							width : 80,
							dataIndex : 'isWebpage',
							sortable : false,
							align : 'center',
							renderer : changeWhether
						}, {
							text : RES_ICON,
							width : 75,
							sortable : false,
							dataIndex : 'image',
							align : 'center',
							renderer : showImage
						}, {
							text : RES_CREATOR,
							width : 70,
							dataIndex : 'creator',
							sortable : false,
							align : 'center',
							hidden : true
						},
						// {text: '创建时间',width:140,dataIndex:
						// 'createTime',sortable: false,align: 'center'},
						{
							text : RES_REMARKS,
							flex : 1,
							dataIndex : 'remarks',
							sortable : false,
							minWidth : 150
						}

				],
				dockedItems : [{
					xtype : 'toolbar',
					items : [addResourceAction, '-', addPermissionAction, '-',
							editResourceAction, '-', delResourceAction, '-',
							orderResourceAction, '-',
							cancelOrderResourceAction, '->', searchTrigger]
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
						if (selections.length) {
							editResourceAction.enable();
							delResourceAction.enable();
						} else {
							addPermissionAction.disable();
							editResourceAction.disable();
							delResourceAction.disable();
						}
					},
					select : function(rowModel, record, index, opts) {
						if (record) {
							var kind = record.data.kind;
							var isWebpage = record.data.isWebpage;
							var text = BUTTON_EDIT;
							// kind =='0'? text=BUTTON_EDIT:text=BUTTON_EDIT;
							editResourceAction.setText(text);
							kind == '1' || isWebpage == '1' ? addResourceAction
									.disable() : addResourceAction.enable();
							isWebpage == '0'
									? addPermissionAction.disable()
									: addPermissionAction.enable();

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
	/** 显示图片 */
	var changingImage = Ext.create('Ext.Img', {
				id : 'imgUrl',
				height : 55,
				width : 55,
				maxWidth : 60,
				cls : 'default-icon'
			});

	/** 选择图片将图片显示 */
	function insertSelectedImage(image) {
		var thumb = image.data.thumb;
		var src = getImgSrc(thumb);
		changingImage.setSrc(src);
		Ext.getCmp('image').setValue(thumb);
		changingImage.hide().show({
					duration : 5000
				});
		chooseWin.animateTarget = changingImage;
	}

	var chooseWin = null;
	/** 选择桌面图标按钮 */
	var chooseImageBtn = Ext.create('Ext.Button', {
				text : BUTTON_CHOOSE,
				scale : 'large',
				iconCls : 'choose-image',
				iconAlign : 'top',
				cls : 'x-btn-as-arrow',
				handler : function() {
					if (!chooseWin) {
						chooseWin = Ext.create('Ext.chooser.Window', {
									id : 'img-chooser-dlg',
									animateTarget : chooseImageBtn.getEl(),
									listeners : {
										selected : insertSelectedImage
									}
								});
					}
					chooseWin.show();
				}
			});

	/** 新增资源窗口 */
	var windowPanel = Ext.create('Ext.window.Window', {
		title : BUTTON_NEW,
		modal : true,
		closeAction : 'hide',
		resizable : false,
		// height : 400,
		width : 400,
		items : [{
			xtype : 'form',
			bodyPadding : 5,
			border : false,
			defaultType : 'textfield',
			defaults : {
				labelAlign : 'right',
				labelWidth : 85,
				width : 350,
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
						xtype : 'hiddenfield',
						id : 'image',
						name : 'image'
					}, Ext.create('Ext.ux.form.ValidatorTextField', {
								labelAlign : 'right',
								labelWidth : 85,
								msgTarget : 'side',
								fieldLabel : RES_NAME,
								name : 'name',
								id : 'name',
								allowBlank : false,
								controllerPath : eastcom.baseURL
										+ '/sysmng/security/checkResourceNameExsist',
								errorMsg : MSG_NAME_EXSIST,
								typeFlag : '',
								otherInput : ''
							}), {
						fieldLabel : RES_FULLNAME,
						name : 'nameCn'
					}, Ext.create('Ext.ux.form.ValidatorTextArea', {
								labelAlign : 'right',
								height : 50,
								labelWidth : 85,
								msgTarget : 'side',
								fieldLabel : RES_LOCATION,
								name : 'location',
								id : 'location',
								allowBlank : false,
								disableCheck : true,
								controllerPath : eastcom.baseURL
										+ '/sysmng/security/checkResourceNameExsist',
								errorMsg : MSG_NAME_EXSIST,
								typeFlag : '',
								otherInput : ''
							}), {
						xtype : 'radiogroup',
						fieldLabel : RES_MENU_STATUS,
						columns : 4,
						id : 'statusid',
						vertical : false,
						allowBlank : false,
						items : [{
									boxLabel : BUTTON_DISABLE,
									name : 'status',
									inputValue : '0'
								}, {
									boxLabel : BUTTON_ENABLE,
									name : 'status',
									inputValue : '1',
									checked : true
								}, {
									boxLabel : BUTTON_HIDDEN,
									name : 'status',
									inputValue : '2'
								}, {
									boxLabel : BUTTON_VISIBLE,
									name : 'status',
									inputValue : '3'
								}]
					}, {
						xtype : 'fieldcontainer',
						msgTarget : 'side',
						id : 'containerid',
						layout : 'column',
						defaults : {
							layout : 'anchor',
							defaults : {
								anchor : '100%',
								labelAlign : 'right',
								labelWidth : 84
							}
						},
						items : [{
							columnWidth : 3 / 5,
							baseCls : 'x-plain',
							items : [{
								xtype : 'radiogroup',
								id : 'iswebpageid',
								allowBlank : false,
								fieldLabel : RES_IS_WEB_PAGE,
								columns : 2,
								vertical : false,
								items : [{
											boxLabel : MSG_FALSE,
											name : 'iswebpage',
											inputValue : '0',
											checked : true
										}, {
											boxLabel : MSG_TRUE,
											name : 'iswebpage',
											inputValue : '1'
										}],
								listeners : {
									change : function(field, newValue,
											oldValue, opts) {
										var image = Ext.getCmp('image');
										if (newValue.iswebpage == '0') {
											image.setValue('');
										}
									}
								}
							}, {
								xtype : 'radiogroup',
								id : 'isshowdesktopid',
								fieldLabel : RES_IS_IN_DESK,
								columns : 2,
								allowBlank : false,
								vertical : false,
								items : [{
											boxLabel : MSG_FALSE,
											name : 'isshowdesktop',
											inputValue : '0',
											checked : true
										}, {
											boxLabel : MSG_TRUE,
											name : 'isshowdesktop',
											inputValue : '1'
										}]
							}]
						}, {
							columnWidth : 1 / 5,
							baseCls : 'x-plain',
							items : [changingImage]
						}, {
							columnWidth : 1 / 5,
							baseCls : 'x-plain',
							items : [chooseImageBtn]
						}]
					}, {
						xtype : 'textareafield',
						fieldLabel : RES_REMARKS,
						name : 'remarks',
						height : 90
					}, {
						xtype : 'hiddenfield',
						id : 'order',
						name : 'order',
						value : 0,
						allowBlank : false
					}, {
						xtype : 'hiddenfield',
						id : 'kind',
						name : 'kind',
						value : '0',
						allowBlank : false
					}],
			buttons : [{
				text : BUTTON_OK,
				formBind : true,
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
									+ '/sysmng/security/saveResource?opt='
									+ opt,
							success : function(form, action) {
								if (action.result.success == 'true') {
									addNode(action.result.data, opt);
									unSelectNode();
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
		initComponent : initComponent
	};
})();