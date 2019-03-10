/**
 * 用户选择窗口控件
 * 
 * @author cason 2009-02-11
 * @author JJF Jan 14 2014
 */

Ext.define('Ext.user.UserMngChooserWin', {
	extend : 'Ext.window.Window',
	alias : 'widget.usermngselectwin',
	layout : 'border',
	split : true,
	multiSelect : true,// 是否允许多选
	deptTree : true,// 是否显示部门选择树
	authDeptTree : false,// 是否根据当前用户的权限部门显示部门树,用户将无法选择没有权限的部门下的用户
	submitHandler : function(value) {// 确定按钮点击事件
	},
	items : [],
	nowDeptIdStr : '',// 当前选中的部门id集合
	initDeptPanel : function(store) {
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
								name : 'leaf',
								type : 'boolean'
							}]
				});
		var queryDeptUrl = '';
		if (me.authDeptTree) {
			queryDeptUrl = eastcom.baseURL
					+ '/sysmng/security/getDepartmentByUser'
		} else {
			queryDeptUrl = eastcom.baseURL
					+ '/sysmng/security/findAllDeptTreeData'
		}

		var Dstore = Ext.create('Ext.data.TreeStore', {
					model : 'Deparment',
					proxy : {
						type : 'ajax',
						actionMethods : 'POST',
						url : queryDeptUrl,
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

		var treepanel = Ext.create('Ext.tree.Panel', {
					region : 'west',
					displayField : 'nameCn',
					useArrows : true,
					fit : true,
					split : true,
					animate : false,
					stateful : true,
					rootVisible : true,
					store : Dstore,
					autoHeight : true,
					width : 220,
					root : {
						id : 'root',
						expanded : true,
						nameCn : '部门'
					},
					enableColumnHide : eastcom.enableColumnHide,
					makeIdStr : function(node) {
						var childIds = [];
						if (node.data.id != 'root') {
							childIds.push(node.data.id);
						}else{
							return ''
						}
						childIds = this.findChildNodeIds(node, childIds);
						var nowDeptIdStr = '';
						for (var i = 0; i < childIds.length; i++) {
							nowDeptIdStr += (childIds[i] + ',');
						}
						nowDeptIdStr = nowDeptIdStr.substr(0,
								nowDeptIdStr.length - 1);
						me.nowDeptIdStr = nowDeptIdStr;
						return nowDeptIdStr;
					},
					findChildNodeIds : function(node, childIds) {
						var children = node.childNodes;
						for (var i = 0; i < children.length; i++) {
							childIds.push(children[i].data.id);
							if (children[i].childNodes.length > 0) {
								this.findChildNodeIds(children[i], childIds);
							}
						}
						return childIds;
					},
					viewConfig : {
						overflowY : 'auto',
						overflowX : 'auto',
						stripeRows : true,
						listeners : {}
					},
					listeners : {
						afterlayout : function() {
							var me = this;
							if (me.store && me.store.isLoading()) {
								me.getEl().mask(MSG_DATA_LOADING);
							}
						},
						selectionchange : function(row, selected) {
							if (selected[0]) {
								var proxy = store.proxy;
								proxy.extraParams = {
									input : '',
									deptId : this.makeIdStr(selected[0])
								};
								store.loadPage(1, {
											start : 0,
											limit : eastcom.pageSize
										});
							}
						}
					}
				});
		return treepanel;
	},
	initUserGrid : function() {
		var me = this;
		Ext.define('uc', {
					extend : 'Ext.data.Model',
					fields : [{
								name : 'id',
								type : 'string'
							}, {
								name : 'userName',
								type : 'string'
							}, {
								name : 'fullName',
								type : 'string'
							}, {
								name : 'userLevel',
								type : 'string'
							}]
				});
		var store = Ext.create('Ext.data.Store', {
					model : 'uc',
					autoLoad : !me.authDeptTree,
					proxy : {
						type : 'ajax',
						actionMethods : {
							create : "POST",
							read : "POST",
							update : "POST",
							destroy : "POST"
						},
						url : eastcom.baseURL + '/sysmng/security/queryUser',
						reader : {
							type : 'json',
							root : 'data.elements',
							totalProperty : 'data.total'
						},
						extraParams : {
							excludeSelf : true,
							selUserName : (typeof(selUsers) == 'undefined' || Ext.isEmpty(selUsers, true)) ? "" : selUsers[0].raw.userName//userMng.js中赋值
						},
						params : {}
					}
				});

		var selectmodel = me.multiSelect ? Ext
				.create('Ext.selection.CheckboxModel') : null;
		var userCM = [{
					header : '用户名',
					dataIndex : 'userName',
					flex : 1,
					sortable : true
				}, {
					header : '中文名称',
					dataIndex : 'fullName',
					align : 'center',
					width : 120,
					sortable : true
				}, {
					header : '用户级别',
					dataIndex : 'userLevel',
					align : 'center',
					width : 120,
					sortable : true
				}];
		// by default columns are sortable
		userCM.defaultSortable = false;

		var pagingbar = Ext.create('Ext.PagingToolbar', {
					pageSize : 20,
					store : store,
					displayInfo : true
				})
		var gridpanel = new Ext.grid.Panel({
					region : 'center',
					split : true,
					stripeRows : true,
					selModel : selectmodel,
					loadMask : {
						msg : MSG_DATA_LOADING
					},
					colModel : userCM,
					store : store,
					frame : false,
					border : true,
					viewConfig : {
						forceFit : true
					},
					tbar : {
						items : [
								'->',
								Ext.create(
										'Ext.components.CommonSearchTrigger', {
											searchAction : function(key) {
												var proxy = store.proxy;
												proxy.extraParams = {
													input : key,
													excludeSelf : true,
													selUserName : (typeof(selUsers) == 'undefined' || Ext.isEmpty(selUsers, true)) ? "" : selUsers[0].raw.userName,//userMng.js中赋值
													deptId : ''
												};
												store.loadPage(1, {
															start : 0,
															limit : eastcom.pageSize
														});
											}
										})]
					},
					// tbar : ['请输入用户名: ', ' ', new Ext.ux.SearchField({
					// paramName : 'input',
					// store : store,
					// width : 320
					// })],

					bbar : pagingbar
				});
		return gridpanel;
	},
	initComponent : function() {
		var me = this;
		var items = [];

		var userGrid = me.initUserGrid();
		var deptTree = null;

		if (me.deptTree) {
			deptTree = me.initDeptPanel(userGrid.getStore());
		}

		items.push(userGrid);
		if (deptTree) {
			items.push(deptTree);
		}

		me.items = items;

		me.buttons = [{
					text : BUTTON_OK,
					handler : function() {
						var _records = userGrid.getSelectionModel().selected;
						var users = [];
						var val = {
							users : [],
							dept : null
						};
						if (!_records || !_records.length) {
							Ext.Msg.show({
										title : MSG_TITLE,
										msg : '请选择用户',
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.INFO
									});
							return false;
						}
						for (var i = 0; i < _records.length; i++) {
							users.push(_records.items[i].data);
						}
						val.users = users;
						if (deptTree) {
							var _depts = deptTree.getSelectionModel().selected;
							if (_depts && _depts.items.length) {
								val.dept = _depts.items[0].data;
							}
						}

						if (typeof(me.submitHandler) == 'function') {
							me.submitHandler(val);
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
