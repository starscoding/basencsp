/**
 * 多条件筛选窗口组件
 * 
 * @author JJF
 * @since JAN 04,2015
 */
Ext.define('Ext.components.FilterWin', {
	extend : 'Ext.window.Window',
	alias : 'widget.filterwin',
	symbols : ['=', 'like', '>', '>=', '<', '<=', '!='],
	combo : '',
	symbolDefaultValues : {},
	allowBlank : true,
	closeAction : 'hide', 
	submit : function(recs) {
	},
	allowSame : false,
	addItems : function(id) {
		var me = this, panel, grid, symbolValues = [], nameCombo, formId, nameComboId, symbolComboId, valueFieldId;
		formId = id + '-form';
		nameComboId = formId + '-namecombo';
		symbolComboId = formId + '-symbolcombo';
		valueFieldId = formId + '-valuefield';

		if (me.symbols && me.symbols.length) {
			Ext.each(me.symbols, function(s, i) {
						symbolValues.push({
									label : s,
									name : s
								});
					});
		}

		var onComboChange = function(combo, value) {
			var symbolCombo = Ext.getCmp(symbolComboId), valueField = Ext
					.getCmp(valueFieldId), symbolStr = '', valueStr = '';
			if (me.symbolDefaultValues && me.symbolDefaultValues[value]) {
				var v = me.symbolDefaultValues[value];
				if (typeof(v.symbol) != 'undefined') {
					symbolStr = v.symbol;
				}
				if (typeof(v.value) != 'undefined') {
					valueStr = v.value;
				}
			}
			symbolCombo.setValue(symbolStr);
			valueField.setValue(valueStr);
		};
		if (typeof(me.combo) == 'string') {
			nameCombo = Ext.create('Ext.components.BaseDataComboBox', {
						xtype : 'combo',
						fieldLabel : '字段',
						labelWidth : 40,
						labelAlign : 'right',
						valueField : "name",
						width : '100%',
						margin : '5 0 0 0',
						allowBlank : false,
						editable : false,
						allFlag : false,
						parentName : me.combo,
						id : nameComboId,
						listeners : {
							change : onComboChange
						}
					});
		} else {
			nameCombo = Ext.create('Ext.form.ComboBox', {
						xtype : 'combo',
						fieldLabel : '字段',
						displayField : 'label',
						valueField : 'name',
						labelWidth : 40,
						labelAlign : 'right',
						width : '100%',
						margin : '5 0 0 0',
						allowBlank : false,
						editable : false,
						id : nameComboId,
						store : Ext.create('Ext.data.Store', {
									fields : ['label', 'name'],
									data : me.combo
								}),
						listeners : {
							change : onComboChange
						}
					});
		}

		panel = Ext.create('Ext.panel.Panel', {
			region : 'west',
			layout : 'fit',
			split : true,
			width : 200,
			items : [{
				xtype : 'form',
				margin : 5,
				id : formId,
				border : false,
				defaults : {
					displayField : 'label',
					valueField : 'name',
					labelWidth : 40,
					labelAlign : 'right',
					width : '100%',
					margin : '5 0 0 0',
					allowBlank : false,
					editable : false
				},
				layout : {
					type : 'vbox',
					align : 'right'
				},
				items : [nameCombo, {
							xtype : 'combo',
							fieldLabel : '符号',
							id : symbolComboId,
							store : Ext.create('Ext.data.Store', {
										fields : ['label', 'name'],
										data : symbolValues
									})
						}, {
							xtype : 'textfield',
							id : valueFieldId,
							editable : true,
							fieldLabel : '值'
						}, {
							xtype : 'button',
							text : '添加',
							width : 70,
							handler : function() {
								var form = Ext.getCmp(formId), name, label, symbol, value, record;
								if (form.isValid()) {
									name = Ext.getCmp(nameComboId).getValue();
									label = Ext.getCmp(nameComboId)
											.getRawValue();
									symbol = Ext.getCmp(symbolComboId)
											.getValue();
									value = Ext.getCmp(valueFieldId).getValue();
									var flag = grid.addRec(name, label, symbol,
											value);

									if (!flag) {
										Ext.Msg.alert(MSG_TITLE, '已存在重复数据');
									}
								}
							}
						}]
			}]
		});
		grid = Ext.create('Ext.grid.Panel', {
			region : 'center',
			split : true,
			cached : {},
			addRec : function(name, label, symbol, value) {
				var that = this, display = [label, symbol, value].join(' '), namesymbol = name
						+ symbol;
				if (!that.cached[namesymbol] || me.allowSame) {
					that.cached[namesymbol] = true;
					this.getStore().add([{
										name : name,
										label : label,
										value : value,
										symbol : symbol,
										display : display,
										namesymbol : namesymbol
									}], true);
					return true;
				} else {
					return false;
				}
			},
			store : Ext.create('Ext.data.Store', {
						fields : ['name', 'label', 'symbol', 'value',
								'display', 'namesymbol'],
						proxy : {
							type : 'memory',
							reader : {
								type : 'json'
							}
						}
					}),
			listeners : {
			  cellclick:function(a, td, cellIndex, record, tr, rowIndex, e, eOpts){
	                nameCombo.setValue(record.data.name);
	                Ext.getCmp(symbolComboId).setValue(record.data.symbol);
	                Ext.getCmp(valueFieldId).setValue(record.data.value);
			    }
			},
			columns : [{
						text : '已选条件',
						dataIndex : 'display',
						flex : 1
					}, {
						xtype : 'actioncolumn',
						align : 'center',
						width : 30,
						items : [{
							icon : eastcom.baseURL
									+ '/static/images/common/icons/cancel.png',
							tooltip : BUTTON_DELETE,
							handler : function(g, rowIndex, colIndex) {
								var store = grid.getStore(), rec = store
										.getAt(rowIndex);
								store.remove(rec);
								if (!me.allowSame) {
									grid.cached[rec.get('namesymbol')] = false;
								}
							}
						}]
					}]
		});
		me.items = [panel, grid];
		me.buttons = [{
					text : '重置',
					handler : function() {
						grid.store.removeAll();
						nameCombo.setValue('');
		                Ext.getCmp(symbolComboId).setValue('');
		                Ext.getCmp(valueFieldId).setValue('');
		                grid.cached = {};
					}
				},{
					text : BUTTON_OK,
					handler : function() {
						if (!me.allowBlank && grid.store.getCount() == 0) {
							Ext.Msg.alert(MSG_TITLE, MSG_EMPTY_SELECTION);
							return
						}
						if (me.submit && typeof(me.submit) == 'function') {
							me.submit.call(me, grid.store.getRange());
						}
						me.close();
					}
				}, {
					text : BUTTON_CANCEL,
					handler : function() {
						me.close();
					}
				}];
	},
	initComponent : function() {
		var me = this;
		me.layout = 'border';
		if(me.id == '' || me.id == null){
			var id = Ext.id();
			me.id = id;
		}
		me.addItems(me.id);
		me.callParent();
	}
});