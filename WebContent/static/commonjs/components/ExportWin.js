/**
 * 导出数据窗口
 * 
 * @author JJF
 * @date Jan 23 2014
 */
Ext.define('Ext.components.ExportWin', {
	extend : 'Ext.window.Window',
	alias : 'widget.exportwin',
	title : BUTTON_EXPORT,
	modal : true,
	height : 140,
	width : 300,
	layout : 'fit',
	draggable : true,
	resizable : false,
	closeAction : 'hide',
	doExport : function(dataScope, fileType) {// 导出方法
	},
	getTotal : function() {// 获取总数量方法
		return 0;
	},
	listeners : {
		show : function() {
			var me = this;
			// 触发change事件
			var dataScopeRadioGroup = Ext
					.getCmp(me.id + '_dataScopeRadioGroup');
			dataScopeRadioGroup.setValue({
						'dataScope' : 'page'
					});
			dataScopeRadioGroup.setValue({
						'dataScope' : 'all'
					});
		}
	},
	initComponent : function() {
		var me = this;

		var createUUID = (function(uuidRegEx, uuidReplacer) {
			return function() {
				return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(
						uuidRegEx, uuidReplacer).toUpperCase();
			};
		})(/[xy]/g, function(c) {
					var r = Math.random() * 16 | 0, v = c == "x"
							? r
							: (r & 3 | 8);
					return v.toString(16);
				});

		var id = createUUID();
		me.id = id;

		me.items = [{
			xtype : 'form',
			border : false,
			defaults : {
				xtype : 'radiogroup',
				vertical : true,
				labelAlign : 'right',
				columns : 2,
				margin : 10,
				labelWidth : 65
			},
			items : [{
				id : id + '_dataScopeRadioGroup',
				fieldLabel : '数据范围',
				listeners : {
					change : function(rg, newVal) {
						var xlsBtn = Ext.getCmp(id + '_fileType-xls');
						if (newVal.dataScope == 'all') {
							if (me.getTotal() >= 65535) {
								var fileTypeRadioGroup = Ext.getCmp(id
										+ '_fileTypeRadioGroup');
								fileTypeRadioGroup.setValue({
											'fileType' : 'csv'
										});
								xlsBtn.setDisabled(true);
							} else {
								xlsBtn.setDisabled(false);
							}
						} else {
							xlsBtn.setDisabled(false);
						}
					}
				},
				items : [{
							boxLabel : '所有数据',
							name : 'dataScope',
							inputValue : 'all',
							checked : true
						}, {
							boxLabel : '单页数据',
							name : 'dataScope',
							inputValue : 'page'
						}]
			}, {
				id : id + '_fileTypeRadioGroup',
				fieldLabel : '导出类型',
				items : [{
					id : id + '_fileType-xls',
					boxLabel : '<img style="height:14px;width:14px;margin-right:2px" src="'
							+ eastcom.baseURL
							+ '/static/images/common/icons/xls.gif"></img>xls',
					name : 'fileType',
					inputValue : 'xls',
					checked : true
				}, {
					id : id + '_fileType-csv',
					boxLabel : '<img style="height:14px;width:14px;margin-right:2px" src="'
							+ eastcom.baseURL
							+ '/static/images/common/icons/csv.gif"></img>csv',
					name : 'fileType',
					inputValue : 'csv'
				}]
			}]
		}];

		me.buttons = [{
			text : BUTTON_OK,
			handler : function() {
				var dataScope = Ext.getCmp(id + '_dataScopeRadioGroup')
						.getChecked()[0].inputValue;
				var fileType = Ext.getCmp(id + '_fileTypeRadioGroup')
						.getChecked()[0].inputValue;
				if (me.getTotal() > 0) {
					me.doExport(dataScope, fileType);
				} else {
					Ext.Msg.alert(MSG_TITLE, MSG_EMPTYRSLT);
					return;
				}
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