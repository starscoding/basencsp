Ext.define('Ext.accredit.RoleAccreditWin', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.*', 'Ext.window.*'],
	roleId : '',
	roleName : '',
	modal : true,
	onlyShow : false,// 是否只是显示没有权限授权，true：只显示 false：可授权
	submitResourceUpdate : function(roleId, addIds, delIds, callback) {
		var me = this;
		me.getEl().mask(MSG_DATA_SAVING);
		Ext.Ajax.request({
					url : eastcom.baseURL
							+ '/sysmng/security/updateRoleResourceRange',
					method : 'POST',
					params : {
						addIdStr : addIds.join(','),
						delIdStr : delIds.join(','),
						roleId : roleId,
						roleName : me.roleName
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
							var record = Ext.getCmp('roleMainGrid')
									.getSelectionModel().getSelection()[0];
							var rNum = parseInt(record.get('resourceNum'));
							record.set('resourceNum', rNum + addIds.length
											- delIds.length);
							callback;
							me.close();
						} else {
							Ext.Msg.show({
										title : MSG_FAILURE,
										msg : result.msg,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.ERROR
									});
							me.close();
						}
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
	},
	submitRegionUpdate : function(roleId, addIds, delIds, callback) {
		var me = this;
		Ext.Ajax.request({
					url : eastcom.baseURL
							+ '/sysmng/security/updateRoleRegionRange',
					method : 'POST',
					params : {
						addIdStr : addIds.join(','),
						delIdStr : delIds.join(','),
						roleId : roleId,
						roleName : me.roleName
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
							callback;
							me.close();
						} else {
							Ext.Msg.show({
										title : MSG_FAILURE,
										msg : result.msg,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.ERROR
									});
							me.close();
						}
					},
					failure : function(form, action) {
						Ext.Msg.show({
									title : MSG_ERROR,
									msg : MSG_ERROR_PROMPT,
									buttons : Ext.Msg.OK,
									icon : Ext.Msg.ERROR
								});
					}
				});
	},
	initComponent : function() {
		var me = this;
		var roleId = me.roleId;
		var roleName = me.roleName;
		var onlyShow = me.onlyShow;
		me.frame = false;
		me.layout = 'fit';
		me.autoScroll = true;
		me.title = BUTTON_AUTHORIZE;
		me.bodyStyle = 'overflow-y:hidden;overflow-x:hidden;';
		me.style = {
			zindex : 20000
		};
		me.width = 350;
		me.height = 430;
		me.items = [Ext.create('Ext.tab.Panel', {
					items : [{
								title : ROLE_AUTH_RES,
								layout : 'fit',
								items : [Ext.create(
										'Ext.accredit.RoleRescourceTree', {
											id : 'roleResourceAccreditTree',
											roleId : roleId,
											border : false,
											onlyShow : onlyShow,
											roleName : roleName
										})]
							}, {
								title : ROLE_AUTH_REGION,
								layout : 'fit',
								items : []
							}],
					listeners : {
						tabchange : function(tab, newCard) {
							if (newCard.title == ROLE_AUTH_REGION) {
								if (Ext.getCmp('roleRegionAccreditGrid') == null) {
									newCard.add(Ext.create(
											'Ext.accredit.RoleRegionGrid', {
												id : 'roleRegionAccreditGrid',
												roleId : roleId,
												border : false,
												onlyShow : onlyShow
											}));
								}
							}
						}
					}
				})];
		if (me.onlyShow == false) {
			me.buttons = [{
				xtype : 'button',
				text : BUTTON_OK,
				id : 'saveRoleAccredit',
				disabled : true,
				resourceChange : false,
				regionChange : false,
				fireChange : function() {
					var me = this;
					if (me.resourceChange == false && me.regionChange == false) {
						me.disable();
					} else {
						me.enable();
					}
				},
				handler : function() {
					if (this.resourceChange) {
						var resourceTree = Ext
								.getCmp('roleResourceAccreditTree');
						var resourceAddIds = resourceTree.toBeCheckedIds;
						var resourceDelIds = resourceTree.toBeUnCheckedIds;
						if (this.regionChange) {
							var regionGrid = Ext
									.getCmp('roleRegionAccreditGrid');
							var regionAddIds = regionGrid.toBeCheckedIds;
							var regionDelIds = regionGrid.toBeUnCheckedIds;
							var callback = me.submitRegionUpdate(roleId,
									regionAddIds, regionDelIds);
							me.submitResourceUpdate(roleId, resourceAddIds,
									resourceDelIds, callback);
						} else {
							me.submitResourceUpdate(roleId, resourceAddIds,
									resourceDelIds, callback);
						}
					} else {
						var regionGrid = Ext.getCmp('roleRegionAccreditGrid');
						var regionAddIds = regionGrid.toBeCheckedIds;
						var regionDelIds = regionGrid.toBeUnCheckedIds;
						me.submitRegionUpdate(roleId, regionAddIds,
								regionDelIds);
					}
				}
			}, {
				text : BUTTON_CANCEL,
				handler : function() {
					me.close();
				}
			}];
		} else {
			me.title = ROLE_AUTH_VIEW;
			me.buttons = [{
						text : BUTTON_CLOSE,
						handler : function() {
							me.close();
						}
					}];
		}
		this.callParent();
	}
});