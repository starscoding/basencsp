Ext.define('Ext.user.userRangeDeptTree', {
			extend : 'Ext.tree.Panel',
			requires : ['Ext.data.*', 'Ext.tree.*'],
			userId : '',
			checkedFlag : '',
			animate : false,
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
									}]
						});
				me.frame = false;
				me.rootVisible = true;
				me.useArrows = true;
				me.border = true;
				me.tbar = [{
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
						}];
				me.store = Ext.create('Ext.data.TreeStore', {
							model : 'Deparment',
							autoLoad : false,
							proxy : {
								type : 'ajax',
								actionMethods : 'POST',
								url : eastcom.baseURL
										+ '/sysmng/security/getDepartmentByUser',
								extraParams : {
									userId : me.userId,
									checkedFlag : me.checkedFlag
								}
							},
							root : {
								id : 'root',
								expanded : false,
								text : DEPARTMENTS
							},
							sorters : {
								property : 'oder',
								direction : 'ASC'
							}
						});
				this.callParent();
			}
		});