Ext.Loader.setConfig({
			enabled : true
		});
Ext.Loader.setPath('Ext.components', eastcom.baseURL
				+ '/static/commonjs/components');
Ext.Loader.setPath('Ext.ux', extDir + '/ux');
Ext.Loader.setPath('Ext.user', eastcom.baseURL
				+ '/scripts/sysmng/security/usermng/components');
Ext.require(['Ext.form.*', 'Ext.data.*', 'Ext.grid.*', 'Ext.tip.*','Ext.components.DateTimeField']);
var nowDeptIdStr = '';// 记录当前选中的部门id及其子部门id集合
var totalDeptIdStr = '';// 记录所有可查询的部门id集合，若为超级管理员则为空字符串
var selUsers = [];//记录grid选择的记录
eastcom.modules.userMng = (function() {
	var currentUserType = '';// 当前用户类型
	var initConf = {// 默认初始化参数，初始化参数未配置或无法获取时使用
		randomPsd : 0,// 是否采用随机密码，默认不采用
		showUserRegion : true
		// 是否显示用户归属地市
	};
	var extConf = {// 用户扩展配置
		userExtEnabled : '0'
	};
	var initing = true;// 是否正在初始化数据
	Ext.define('Ext.ux.CustomTrigger', {
				extend : 'Ext.form.field.Trigger',
				alias : 'widget.customtrigger',
				trigger1Cls : Ext.baseCSSPrefix + 'form-search-trigger'
			});
	Ext.define('User', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'id',
							type : 'string'
						}, {
							name : 'userName',
							type : 'string'
						}, {
							name : 'deptId',
							type : 'string'
						}, {
							name : 'deptName',
							type : 'string'
						}, {
							name : 'fullName',
							type : 'string'
						}, {
							name : 'accountEnabled',
							type : 'string'
						}, {
							name : 'userLevel',
							type : 'string'
						}, {
							name : 'creator',
							type : 'string'
						}, {
							name : 'accoutCreateTime',
							type : 'string'
						}, {
							name : 'accoutExpiredStarttime',
							type : 'string'
						}, {
							name : 'accoutExpiredEndtime',
							type : 'string'
						}]
			});
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
						}]
			});
	Ext.define('NoData', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'noDataColumn',
							type : 'string'
						}]
			});

	var noDataStore = Ext.create('Ext.data.Store', {
				model : 'NoData',
				data : [{
							noDataColumn : MSG_EMPTYRSLT
						}]
			});
	var noDataColumn = [{
				text : MSG_QUERYRSLT,
				sortable : false,
				dataIndex : 'noDataColumn',
				flex : 1,
				align : 'center'
			}];
	var userStore = Ext.create('Ext.data.Store', {
				model : 'User',
				pageSize : eastcom.pageSize,
				id : 'userGridStore',
				autoLoad : false,
				proxy : {
					type : 'ajax',
					url : eastcom.baseURL + '/sysmng/security/queryUser',
					reader : {
						type : 'json',
						root : 'data.elements',
						totalProperty : 'data.total'
					},
					extraParams : {},
					actionMethods : {
						read : 'POST'
					},
					timeout : 180000
				},
				listeners : {
					beforeload : function() {
						if (!initing) {
							if (Ext.getCmp('userMainGrid').getEl()) {
								Ext.getCmp('userMainGrid').getEl()
										.mask(MSG_DATA_LOADING);
							}
						}
						return true;
					},
					load : function(me, records) {
						if (initing) {
							Ext.getBody().unmask();
						} else {
							if (Ext.getCmp('userMainGrid')) {
								Ext.getCmp('userMainGrid').getEl().unmask();
							}
							if (records.length == 0) {
								Ext.getCmp('userMainGrid').switchToNoDataMode();
							}
						}
						initing = false;
					}
				}
			});
	sm = Ext.create('Ext.selection.CheckboxModel', {});
	var userColumn = [{
				text : USERNAME,
				// flex : 1,
				width : 150,
				sortable : true,
				dataIndex : 'userName'
			}, {
				text : FULLNAME,
				sortable : true,
				dataIndex : 'fullName',
				width : 120,
				align : 'center'
			}, {
				text : DEPT_NAME,
				sortable : true,
				dataIndex : 'deptId',
				width : 120,
				hidden : true,
				align : 'center'
			}, {
				text : DEPT_NAME,
				sortable : true,
				dataIndex : 'deptName',
				width : 120,
				align : 'center'
			}, {
				text : IS_ENABLE,
				dataIndex : 'accountEnabled',
				width : 80,
				align : 'center'
			}, {
				text : USER_LEVEL,
				dataIndex : 'userLevel',
				width : 120,
				align : 'center'
			}, {
				text : USER_CREATOR,
				dataIndex : 'creator',
				width : 100,
				align : 'center'
			}, {
				text : USER_CREATE_TIME,
				dataIndex : 'accoutCreateTime',
				flex : 1,
				minWidth : 140,
				align : 'center'
			}, {
				text : USER_EXPRIED_STARTTIME,
				dataIndex : 'accoutExpiredStarttime',
				flex : 1,
				minWidth : 140,
				// width : 130,
				align : 'center'
			}, {
				text : USER_EXPRIED_ENDTIME,
				dataIndex : 'accoutExpiredEndtime',
				flex : 1,
				minWidth : 140,
				// width : 130,
				align : 'center'
			}];

	var addAction = Ext.create('Ext.Action', {
				iconCls : 'icon-add',
				text : BUTTON_NEW,
				hidden : eastcom.isPermitted('usermng', 'add'),
				handler : function(widget, event) {
					Ext.create('Ext.user.infoCollectionWin', {
								initConf : initConf,
								extConf : extConf
							}).show();
				}
			});
	// 批量新增按钮
	var multiAddAction = Ext.create('Ext.Action', {
				iconCls : 'icon-add',
				text : USER_MULTI_ADD,
				hidden : eastcom.isPermitted('usermng', 'batchSave'),
				handler : function(widget, event) {
					Ext.create('Ext.user.MultiAddWin', {}).show();
				}
			});
	var detailAction = Ext.create('Ext.Action', {
				iconCls : 'icon_preview_info',
				text : BUTTON_DETAIL,
				hidden : eastcom.isPermitted('usermng', 'queryAccountDetail'),
				disabled : true,
				handler : function(widget, event) {
					var records = Ext.getCmp('userMainGrid')
							.getSelectionModel().getSelection();
					var ids = [];
					for (var i = 0; i < records.length; i++) {
						ids.push(records[i].get('id'));
					}
					if (Ext.getCmp('userDetailWin') == null) {
						Ext.create('Ext.user.userDetailWin', {}).show();
					}
					Ext.getCmp('userDetailWin').setCurrentIds(ids);
				}
			});
	var editAction = Ext.create('Ext.Action', {
		iconCls : 'icon-edit',
		text : BUTTON_EDIT,
		hidden : eastcom.isPermitted('usermng', 'edit'),
		disabled : true,
		editPermission : true,
		handler : function(widget, event) {
			var records = Ext.getCmp('userMainGrid').getSelectionModel().getSelection();
			selUsers = records;
			if (records.length > 1) {
				Ext.Msg.show({
							title : MSG_TITLE,
							msg : USER_MUTIL_ERROR,
							buttons : Ext.Msg.OK,
							icon : Ext.Msg.INFO
						});
			} else {
				if (Ext.getCmp('infoCollectionWin') == null) {
					var editWin = Ext.create('Ext.user.infoCollectionWin', {
								initConf : initConf,
								collapseDetailInfo : false,
								extConf : extConf
							});
					editWin.show(undefined, function() {
								editWin.loadRecord(records[0]);
							});
				} else {
					Ext.Msg.show({
								title : MSG_TITLE,
								msg : USER_CLOSE_EDIT_WIN,
								buttons : Ext.Msg.YESNO,
								icon : Ext.Msg.QUESTION,
								fn : function(btn) {
									if (btn == 'yes') {
										Ext.getCmp('infoCollectionWin').close();
										var editWin = Ext.create(
												'Ext.user.infoCollectionWin', {
													initConf : initConf,
													collapseDetailInfo : false,
													extConf : extConf
												});
										editWin.show(undefined, function() {
													editWin
															.loadRecord(records[0]);
												});
									}
								}
							});
				}
			}
		}
	});
	var deleteAction = Ext.create('Ext.Action', {
		iconCls : 'icon-remove',
		text : BUTTON_DELETE,
		hidden : eastcom.isPermitted('usermng', 'delete'),
		disabled : true,
		handler : function(widget, event) {
			var records = Ext.getCmp('userMainGrid').getSelectionModel()
					.getSelection();
			var infoStr = '';
			for (var i = 0; i < records.length; i++) {
				infoStr += (records[i].get('fullName') + ':'
						+ records[i].get('userName') + ',');
			}
			infoStr = infoStr.substring(0, infoStr.length - 1);
			Ext.Msg.show({
				title : MSG_WARNING,
				msg : DEL_BTN_INFO,
				buttons : Ext.Msg.YESNO,
				icon : Ext.Msg.WARNING,
				fn : function(btn) {
					if (btn == 'yes') {
						userMainGrid.getEl().mask(DEL_DELETING);
						var ids = '';
						var usernames = '';
						for (var i = 0; i < records.length; i++) {
							ids += (records[i].get('id') + ',');
							usernames += (records[i].get('userName') + ',');
						}
						ids = ids.substring(0, ids.length - 1);
						usernames = usernames
								.substring(0, usernames.length - 1);
						Ext.Ajax.request({
							url : eastcom.baseURL
									+ '/sysmng/security/deleteUser',
							method : 'POST',
							params : {
								idStr : ids,
								usernames : usernames
							},
							success : function(form, action) {
								var result = Ext.JSON.decode(form.responseText);
								if (result.success == 'true') {
									Ext.Msg.show({
												title : MSG_SUCCESS,
												msg : result.msg,
												buttons : Ext.Msg.OK,
												icon : Ext.Msg.INFO
											});
									userStore.remove(records);
								} else {
									Ext.Msg.show({
												title : MSG_FAILURE,
												msg : result.msg,
												buttons : Ext.Msg.OK,
												icon : Ext.Msg.ERROR
											});
								}
								userMainGrid.getEl().unmask();
							},
							failure : function(form, action) {
							}
						});
					}
				}
			});
		}
	});
	var accreditAction = Ext.create('Ext.Action', {
		iconCls : 'icon-accredit',
		text : BUTTON_AUTHORIZE,
		hidden : eastcom.isPermitted('usermng', 'roleAccredit'),
		disabled : true,
		handler : function(widget, event) {
			var records = Ext.getCmp('userMainGrid').getSelectionModel()
					.getSelection();
			if (records.length > 1) {
				Ext.Msg.show({
							title : MSG_TITLE,
							msg : USER_MUTIL_ERROR,
							buttons : Ext.Msg.OK,
							icon : Ext.Msg.INFO
						});
			} else {
				if (Ext.getCmp('userAccreditWin') == null) {
					var accreditWin = Ext.create('Ext.user.userAccreditWin', {
								userId : records[0].get('id'),
								editUserFullName : records[0].get('fullName')
							});
					accreditWin.show();
				} else {
					Ext.Msg.show({
								title : MSG_TITLE,
								msg : USER_CLOSE_AUTH_WIN,
								buttons : Ext.Msg.YESNO,
								icon : Ext.Msg.QUESTION,
								fn : function(btn) {
									if (btn == 'yes') {
										Ext.getCmp('userAccreditWin').close();
										var accreditWin = Ext.create(
												'Ext.user.userAccreditWin', {
													userId : records[0]
															.get('id'),
													editUserFullName : records[0]
															.get('fullName')
												});
										accreditWin.show();
									}
								}
							});
				}
			}
		}
	});

	// 批量授权按钮
	var multiAccreditAction = Ext.create('Ext.Action', {
				iconCls : 'icon-accredit',
				text : USER_MULTI_AUTHORIZE,
				hidden : eastcom.isPermitted('usermng', 'roleAccredit'),
				disabled : true,
				handler : function(widget, event) {
					var records = Ext.getCmp('userMainGrid')
							.getSelectionModel().getSelection(), uids = [], unames = [];
					if (records.length > 1) {
						Ext.each(records, function(record) {
									uids.push(record.get('id'));
									unames.push(record.get('fullName'));
								});

						if (Ext.getCmp('userAccreditWin') == null) {
							var accreditWin = Ext.create(
									'Ext.user.userAccreditWin', {
										userId : uids.join(','),
										editUserFullName : unames.join(',')
									});
							accreditWin.show();
						} else {
							Ext.Msg.show({
										title : MSG_TITLE,
										msg : USER_CLOSE_AUTH_WIN,
										buttons : Ext.Msg.YESNO,
										icon : Ext.Msg.QUESTION,
										fn : function(btn) {
											if (btn == 'yes') {
												Ext.getCmp('userAccreditWin')
														.close();
												var accreditWin = Ext
														.create(
																'Ext.user.userAccreditWin',
																{
																	userId : uids
																			.join(','),
																	editUserFullName : unames
																			.join(',')
																});
												accreditWin.show();
											}
										}
									});
						}
					}
				}
			});

	var enableAction = Ext.create('Ext.Action', {
				iconCls : 'icon-ok',
				text : BUTTON_ENABLE,
				hidden : eastcom.isPermitted('usermng', 'enable/disable'),
				disabled : true,
				handler : function(widget, event) {
					setAccountEnabled(true);
				}
			});
	var disableAction = Ext.create('Ext.Action', {
				iconCls : 'icon-no',
				text : BUTTON_DISABLE,
				hidden : eastcom.isPermitted('usermng', 'enable/disable'),
				disabled : true,
				handler : function(widget, event) {
					setAccountEnabled(false);
				}
			});
	var unlockAction = Ext.create('Ext.Action', {
				iconCls : 'icon-user-examine',
				text : USER_UNLOCK_ACCOUNT,
				disabled : true,
				handler : function(widget, event) {
					userMainGrid.getEl().mask(MSG_DATA_OPTING);
					var records = Ext.getCmp('userMainGrid')
							.getSelectionModel().getSelection();
					var usernames = [];
					for (var i = 0; i < records.length; i++) {
						usernames.push(records[i].get('userName'));
					}
					Ext.Ajax.request({
								url : eastcom.baseURL
										+ '/sysmng/security/unlockAccounts',
								method : 'POST',
								params : {
									usernameStr : usernames.join(',')
								},
								success : function(form, action) {
									var result = Ext.JSON
											.decode(form.responseText);
									if (result.success == 'true') {
										Ext.Msg.show({
													title : '成功',
													msg : result.msg,
													buttons : Ext.Msg.OK,
													icon : Ext.Msg.INFO
												});
									} else {
										Ext.Msg.show({
													title : MSG_FAILURE,
													msg : result.msg,
													buttons : Ext.Msg.OK,
													icon : Ext.Msg.ERROR
												});
									}
									userMainGrid.getEl().unmask();
								},
								failure : function(form, action) {
								}
							});
				}
			});
	var contextMenu = Ext.create('Ext.menu.Menu', {
				items : [detailAction, editAction, accreditAction,
						enableAction, disableAction, unlockAction, deleteAction]
			});
	var userMainGrid = Ext.create('Ext.grid.Panel', {
		id : 'userMainGrid',
		fit : true,
		split : true,
		title : LABEL_ALL,
		region : 'center',
		border : true,
		store : userStore,
		selModel : sm,
		autoHeight : true,
		columnLines : true,
		enableColumnHide : eastcom.enableColumnHide,
		columns : userColumn,
		nomalStore : userStore,
		nomalColumns : userColumn,
		noDataStore : noDataStore,
		noDataColumns : noDataColumn,
		viewConfig : {
			loadMask : false
		},
		switchToNoDataMode : function() {
			var me = this;
			if (me.store != me.noDataStore) {
				me.reconfigure(me.noDataStore, me.noDataColumns);
			}

		},
		switchToNomalMode : function() {
			var me = this;
			if (me.store != me.nomalStore) {
				me.reconfigure(me.nomalStore, me.nomalColumns);
			}
		},
		tbar : Ext.create('Ext.toolbar.Toolbar', {
					items : [addAction, '-', multiAddAction, '-', detailAction,
							'-', editAction, '-', accreditAction, '-',
							multiAccreditAction, '-', enableAction, '-',
							disableAction, '-', unlockAction, '-',
							deleteAction, '->'
//							, {
//								xtype : 'customtrigger',
//								width : 200,
//								id : 'userSearchInput',
//								margin : '0 5 0 0',
//								emptyText : MSG_PLEASE_INSERT,
//								enableKeyEvents : true,
//								onTriggerClick : function() {
//									var proxy = userStore.proxy;
//									proxy.extraParams = {
//										input : this.getValue(),
//										deptId : nowDeptIdStr
//									};
//									Ext.getCmp('userMainGrid')
//											.switchToNomalMode();
//									userStore.loadPage(1, {
//												start : 0,
//												limit : eastcom.pageSize
//											});
//									Ext.getCmp('userMainGrid')
//											.setTitle(MSG_QUERYRSLT);
//								},
//								listeners : {
//									keypress : function(me, e) {
//										if (e.getKey() == 13) {
//											var proxy = userStore.proxy;
//											proxy.extraParams = {
//												input : this.getValue(),
//												deptId : nowDeptIdStr
//											};
//											Ext.getCmp('userMainGrid')
//													.switchToNomalMode();
//											userStore.loadPage(1, {
//														start : 0,
//														limit : eastcom.pageSize
//													});
//											Ext.getCmp('userMainGrid')
//													.setTitle(MSG_QUERYRSLT);
//										}
//									}
//								}
//							}

							]
				}),
		bbar : Ext.create('Ext.components.BaseCommonPagingToolbar', {
					store : userStore,
					listeners : {}
				}),
		listeners : {
			itemcontextmenu : function(view, rec, node, index, e) {
				e.stopEvent();
				contextMenu.showAt(e.getXY());
				return false;
			},
			selectionchange : function(me, selected, eOpts) {
				if (selected.length > 0 && !selected[0].get('noDataColumn')) {
					detailAction.enable();
					if (currentUserType != 'U') {
						editAction.enable();
						deleteAction.enable();
					}
					accreditAction.enable();
					unlockAction.enable();
					if (selected.length > 1) {
						multiAccreditAction.enable();
					} else {
						multiAccreditAction.disable();
					}
					var enableActionMenu = false;
					var disableActionMenu = false;
					for (i in selected) {
						if (selected[i].get('accountEnabled') == '可用') {
							disableActionMenu = true;
						} else {
							enableActionMenu = true;
						}
					}
					if (enableActionMenu) {
						enableAction.enable();
					} else {
						enableAction.disable();
					}
					if (disableActionMenu) {
						disableAction.enable();
					} else {
						disableAction.disable();
					}
				} else {
					detailAction.disable();
					editAction.disable();
					deleteAction.disable();
					accreditAction.disable();
					multiAccreditAction.disable();
					enableAction.disable();
					disableAction.disable();
					unlockAction.disable();
				}
			},
			afterrender : function() {
				Ext.tip.QuickTipManager.register({
							target : 'userN',
							title : LABEL_TIPS + '：',
							text : USER_SEARCH_TIPS,
							width : 100,
							dismissDelay : 50000
						});
			}
		}
	});
	var deptTreePanel = Ext.create('Ext.user.userRangeDeptTree', {
		region : 'west',
		title : DEPARTMENTS,
		width : 250,
		collapsible : true,
		border : true,
		split : true,
		layout : 'fit',
		border : false,
		positionNames : [],
		makePosition : function(node) {
			var me = this;
			me.positionNames.push(node.data.text);
			if (node.parentNode) {
				me.makePosition(node.parentNode);
			}
		},
		makeIdStr : function(node) {
			var childIds = [];
			if (node.data.id != 'root') {
				childIds.push(node.data.id);
			}
			childIds = findChildNodeIds(node, childIds);
			nowDeptIdStr = '';
			for (var i = 0; i < childIds.length; i++) {
				nowDeptIdStr += (childIds[i] + ',');
			}
			nowDeptIdStr = nowDeptIdStr.substr(0, nowDeptIdStr.length - 1);
		},
		listeners : {
			selectionchange : function(node, selected) {
				var me = this;
				if (selected) {
					var titleStr = '';
					me.positionNames = [];
					me.makePosition(selected[0]);
					for (var i = me.positionNames.length - 1; i >= 0; i--) {
						titleStr += me.positionNames[i] + '>';
					}
					titleStr = titleStr.substr(0, titleStr.length - 1);
					Ext.getCmp('userMainGrid').setTitle(titleStr);
					if (currentUserType == 'A') {
						if (selected[0].data.id == 'root') {
							nowDeptIdStr = '';
						} else {
							me.makeIdStr(selected[0]);
						}
					} else {
						me.makeIdStr(selected[0]);
					}
					//Ext.getCmp('userSearchInput').setValue('');
					var proxy = userStore.proxy;

					proxy.extraParams = {
						deptId : nowDeptIdStr
					};
					Ext.getCmp('userMainGrid').switchToNomalMode();
					userStore.loadPage(1, {
								start : 0,
								limit : eastcom.pageSize
							});
				}
			},
			load : function() {
				var me = this;
				me.getRootNode().expand();
				var proxy = userStore.proxy;
				if (currentUserType == 'A') {
					nowDeptIdStr = '';
					totalDeptIdStr = '';
					proxy.extraParams = {
						input : '',
						deptId : ''
					};
				}else{
					me.makeIdStr(me.getRootNode());
					totalDeptIdStr = nowDeptIdStr;
					proxy.extraParams = {
						input : '',
						deptId : nowDeptIdStr
					};
				}
				
				if (currentUserType == 'U') {
					Ext.getBody().unmask();
					Ext.Msg.show({
								title : MSG_ERROR,
								msg : USER_NONE_AUTH_ERROR,
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.ERROR,
								fn : function() {
									eastcom
											.closeTab('004000000000000000000000000000');
								}
							});

				}else {
					Ext.getCmp('userMainGrid').switchToNomalMode();
					userStore.loadPage(1, {
								start : 0,
								limit : eastcom.pageSize
						});
				}
			}
		}
	});

	// 循环取得所有子部门id
	function findChildNodeIds(node, childIds) {
		var children = node.childNodes;
		for (var i = 0; i < children.length; i++) {
			childIds.push(children[i].data.id);
			if (children[i].childNodes.length > 0) {
				findChildNodeIds(children[i], childIds);
			}
		}
		return childIds;
	}

	// 设置账户状态
	function setAccountEnabled(flag) {
		userMainGrid.getEl().mask(MSG_DATA_OPTING);
		var records = Ext.getCmp('userMainGrid').getSelectionModel()
				.getSelection();
		var ids = [];
		var usernames = [];
		for (var i = 0; i < records.length; i++) {
			ids.push(records[i].get('id'));
			usernames.push(records[i].get('userName'));
		}
		Ext.Ajax.request({
					url : eastcom.baseURL
							+ '/sysmng/security/setAccountEnabled',
					method : 'POST',
					params : {
						idStr : ids.join(','),
						usernames : usernames.join(','),
						flag : flag ? 'true' : 'false'
					},
					success : function(form, action) {
						var result = Ext.JSON.decode(form.responseText);
						if (result.success == 'true') {
							// Ext.Msg.show({
							// title : '成功',
							// msg : result.msg,
							// buttons : Ext.Msg.OK,
							// icon : Ext.Msg.INFO
							// });
							for (var i = 0; i < records.length; i++) {
								records[i].set('accountEnabled', flag
												? '可用'
												: '不可用');
							}
							enableAction.setDisabled(flag);
							disableAction.setDisabled(!flag);
						} else {
							Ext.Msg.show({
										title : MSG_FAILURE,
										msg : result.msg,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.ERROR
									});
						}
						userMainGrid.getEl().unmask();
					},
					failure : function(form, action) {
					}
				});
	}

//	var userSelectPanel = Ext.create('Ext.panel.Panel',{
//			region: 'north',
//	        padding : '0 0 5 0',
//	        height: 50,
//			layout : 'hbox',
//	        bodyPadding: 10,
//			defaults : {
//				labelAlign : 'right',
//				labelWidth: 80,
//				width :250,
//				msgTarget : 'side'
//			},
//			items: [{
//						xtype : 'textfield',
//						id: 'userN',
//						name : 'userN',
//						fieldLabel : USERNAME,
//						emptyText : MSG_PLEASE_INSERT,
//						editable : false,
//						labelAlign : 'right',
//						anchor : '95%'
//					},{
//						fieldLabel : USER_CREATE_TIME,
//						xtype : 'datetimefield',
//						id : 'startTime',
//						emptyText : MSG_CHOOSE_ONE,
//						endTimeField : {
//							id : 'endTime'
//						},
//						timeConfig : {
//							dateFmt : 'yyyy-MM-dd HH:mm:ss'
//						},
//						name : 'startTime',
//						labelAlign : 'right',
//						allowBlank : false,
//						anchor : '95%'
//					},{
//						xtype : 'label',
//						text : '-',
//						width : 6,
//						margins : '0 10 0 10'
//					},{
//						xtype : 'datetimefield',
//						id : 'endTime',
//						name : 'endTime',
//						width : 165,
//						emptyText : MSG_CHOOSE_ONE,
//						startTimeField : {
//							id : 'startTime'
//						},
//						timeConfig : {
//							dateFmt : 'yyyy-MM-dd HH:mm:ss'
//						},
//						labelAlign : 'right',
//						allowBlank : false,
//						anchor : '100%'
//					},{
//							xtype : 'radiogroup',
//							fieldLabel : HAVEPERMISSION,
//							labelAlign :'right',
//							id : 'havePermission',
//							anchor : '95%',
//							items : [{
//									    boxLabel: ALLPERMISSION ,
//									    name : 'havePermission',
//									    inputValue: '2',
//									    checked : true
//									},{
//									    boxLabel: YES ,
//									    name : 'havePermission',
//									    inputValue: '1'
//									},{
//									    boxLabel: NO ,
//									    name : 'havePermission',
//									    inputValue: '0'
//									}]
//					},{
//		                xtype: 'button',
//		                style : 'margin-left:20px',
//		                formBind: true,
//		                iconCls : 'icon-search',
//		                width : 60,
//		                text : BUTTON_SEARCH,
//		                handler : function(){
//		                	var proxy = userStore.proxy;
//							proxy.extraParams = {
//								input : Ext.getCmp('userN').getValue(),
//								startTime : Ext.getCmp('startTime').getValue(),
//								endTime : Ext.getCmp('endTime').getValue(),
//								havePermission : Ext.getCmp('havePermission').getValue(),
//								deptId : nowDeptIdStr
//							};
//							Ext.getCmp('userMainGrid')
//									.switchToNomalMode();
//							userStore.loadPage(1, {
//										start : 0,
//										limit : eastcom.pageSize
//									});
//							Ext.getCmp('userMainGrid')
//									.setTitle(MSG_QUERYRSLT);
//		                }
//
//		            },{
//		           		xtype : 'button',
//						style : 'margin-left:20px',
//						formBind : true,
//						iconCls : 'icon-refresh',
//						width : 70,
//						text : BUTTON_RESET,
//						handler : function(){
//							Ext.getCmp('userN').setValue('');
//							Ext.getCmp('startTime').setValue('');
//							Ext.getCmp('endTime').setValue('');
//							Ext.getCmp('havePermission').items.get(0).setValue(true);
//						}
//		            }]
//		});


//	var userMainPanel = Ext.create('Ext.Panel',{
//    	region : 'center',
//		id: 'userMainPanel',
//		layout :'border',
//		items : [userSelectPanel,userMainGrid]
//    });


	function initComponent() {
		Ext.create('Ext.Viewport', {
			layout : {
				type : 'border',
				padding : 5
			},
			items : [deptTreePanel, {
						region : 'center',
						id: 'userMainPanel',
						layout :'border',
						items :[{
							region : 'north',
							padding : '0 0 5 0',
							xtype : 'form',
							bodyPadding : 10,
							items : [{
								xtype : 'fieldcontainer',
								fieldLabel : USER_CREATE_TIME,
								labelWidth : 80,
								labelAlign : 'right',
								layout : 'hbox',
								items : [{
										xtype : 'datetimefield',
										//allowBlank : false,
										width : 200,
										endTimeField : {
											id : 'endTime'
										},
										name : 'startTime',
										id : 'startTime',
										timeConfig : {
											maxDate : '%y-%M-%d'
										}
									}, {
										xtype : 'label',
										text : '-',
										margins : '0 10 0 10'

									}, {
										xtype : 'datetimefield',
										//allowBlank : false,
										width : 200,
										name : 'endTime',
										id : 'endTime',
										startTimeField : {
											id : 'startTime'
										},
										timeConfig : {
											maxDate : '%y-%M-%d'
										}
									}, {
										xtype : 'splitter'
									}, {
										xtype : 'button',
										style : 'margin-left:20px',
										//formBind : true,
										iconCls : 'icon-search',
										width : 70,
										text : BUTTON_SEARCH,
								      	handler : function(){
						                	var proxy = userStore.proxy;
											proxy.extraParams = {
												input : Ext.getCmp('userN').getValue(),
												startTime : Ext.getCmp('startTime').getValue(),
												endTime : Ext.getCmp('endTime').getValue(),
												havePermission : Ext.getCmp('havePermission').getValue(),
												deptId : nowDeptIdStr
											};
											Ext.getCmp('userMainGrid').switchToNomalMode();
											userStore.loadPage(1, {
												start : 0,
												limit : eastcom.pageSize
											});
											Ext.getCmp('userMainGrid').setTitle(MSG_QUERYRSLT);
		                				}
									},{
									    xtype : 'button',
										style : 'margin-left:20px',
										formBind : true,
										iconCls : 'icon-refresh',
										width : 70,
										text : BUTTON_RESET,
										handler : function(){
											Ext.getCmp('userN').setValue('');
											Ext.getCmp('startTime').setValue('');
											Ext.getCmp('endTime').setValue('');
											Ext.getCmp('havePermission').items.get(0).setValue(true);
										}
									}]
							}, {
								xtype : 'fieldset',
								checkboxToggle : true,
								collapsed : true,
								title : MSG_MORE_CONDITION,
								layout : 'column',
								autoScroll : true,
								defaults : {
									layout : 'anchor',
									defaults : {
										anchor : '100%',
										labelAlign : 'right'
									}
								},
								items : [{
										columnWidth : 1 / 3,
										defaultType : 'textfield',
										baseCls : 'x-plain',
										items : [{
											xtype : 'textfield',
											id: 'userN',
											name : 'userN',
											fieldLabel : USERNAME,
											emptyText : MSG_PLEASE_INSERT,
											editable : false,
											labelAlign : 'right',
											anchor : '95%'
										}]
									}, {
										columnWidth : 1 / 3,
										defaultType : 'textfield',
										baseCls : 'x-plain',
										items : [{
											xtype : 'radiogroup',
											fieldLabel : HAVEPERMISSION,
											labelAlign :'right',
											id : 'havePermission',
											anchor : '95%',
											items : [{
												    boxLabel: ALLPERMISSION ,
												    name : 'havePermission',
												    inputValue: '2',
												    checked : true
												},{
												    boxLabel: YES ,
												    name : 'havePermission',
												    inputValue: '1'
												},{
												    boxLabel: NO ,
												    name : 'havePermission',
												    inputValue: '0'
												}]
										}]
									}]
								}]},userMainGrid]}],
			listeners : {
				afterrender : function() {
					Ext.getBody().mask(MSG_DATA_INIT);
					var callback = function(userInfo) {
						var uType = userInfo.userLevelType;
						if (uType == 'U') {
							addAction.disable();
						}
						currentUserType = uType;
						Ext.Ajax.request({
							url : eastcom.baseURL
									+ '/configurateExtension/getSysArguments',
							method : 'POST',
							params : {
								type : 'security'
							},
							success : function(form, action) {
								var result = Ext.JSON.decode(form.responseText);
								if (result.success == 'true') {
									var userArgs = $
											.parseJSON(result.data.security.userArgs);
									var userExtArgs = $
											.parseJSON(result.data.security.userExtArgs);
									Ext.apply(initConf, userArgs);
									if (userExtArgs) {
										Ext.apply(extConf, userExtArgs);
										if (extConf.userExtMappingRules
												&& extConf.userExtMappingRules.length) {
											extConf.userExtMappingRules = Ext.JSON
													.decode(extConf.userExtMappingRules);
										}
									}
									deptTreePanel.getStore().load();
								} else {
									Ext.Msg.show({
												title : MSG_FAILURE,
												msg : result.msg,
												buttons : Ext.Msg.OK,
												icon : Ext.Msg.ERROR
											});
								}
							},
							failure : function(form, action) {
							}
						});
					}
					eastcom.getCurrentUserBasicInfo(callback);
				}
			}
		});
	}
	return {
		initComponent : initComponent
	}
})();