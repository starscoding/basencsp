Ext.define('Ext.user.MultiAddWin', {
	extend : 'Ext.window.Window',
	title : USER_MULTI_ADD,
	width : 300,
	modal : true,
	items : [{
				xtype : 'form',
				padding : 5,
				border : false,
				items : [{
							xtype : 'filefield',
							name : 'uploadfile',
							allowBlank : false,
							anchor : '100%',
							emptyText : MSG_CHOOSE_ONE,
							buttonText : BUTTON_BROWSE
						}]
			}],
	buttons : [{
		text : USER_MULTI_ADD_TEMPLETE,
		handler : function() {
			window.open(eastcom.baseURL
					+ '/scripts/sysmng/security/usermng/templete.xls');
		}
	}, {
		text : BUTTON_OK,
		handler : function() {
			var win = this.ownerCt.ownerCt, form = win.query('form')[0];
			if (!form.isValid()) {
				return false
			}
			win.getEl().mask(MSG_DATA_OPTING);
			form.submit({
				url : eastcom.baseURL + '/sysmng/security/batchSaveUser',
				success : function(form, action) {
					var result = Ext.JSON.decode(action.response.responseText);
					if (result.success == 'true') {
						Ext.create('Ext.window.Window', {
							title : MSG_TITLE,
							height : 300,
							width : 600,
							modal : true,
							layout : 'fit',
							items : [{
								xtype : 'panel',
								border : false,
								bodyStyle : 'overflow:auto;font-size:15px;padding:5px;',
								html : result.data
							}]
						}).show();
						win.close();
					} else {
						Ext.Msg.show({
									title : MSG_FAILURE,
									msg : result.msg,
									buttons : Ext.Msg.OK,
									icon : Ext.Msg.ERROR
								});
					}
				},
				failure : function() {
					Ext.Msg.show({
								title : MSG_FAILURE,
								msg : result.msg,
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.ERROR
							});
				}
			});
		}
	}, {
		text : BUTTON_CANCEL,
		handler : function() {
			this.ownerCt.ownerCt.close();
		}
	}]
})