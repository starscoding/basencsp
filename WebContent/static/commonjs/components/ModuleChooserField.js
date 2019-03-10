/**
 * 模块（资源）选择Trigger控件
 * 
 * @author JJF Sep 14, 2014
 */

Ext.Loader.setConfig({
			enabled : true
		});
Ext.Loader.setPath('Ext.components', eastcom.baseURL
				+ '/static/commonjs/components');
Ext.define('Ext.components.ModuleChooserField', {
	extend : 'Ext.form.field.Trigger',
	trigger2Cls : 'x-form-search-trigger',
	trigger1Cls : 'x-form-clear-trigger',
	alias : 'widget.modulechooserfield',
	displayField : 'nameCn',
	multiSelect : true,
	editable : false,
	valueField : 'name',
	_vals : {
		display : [],
		value : []
	},
	detailValue : null,// 完整对象值
	simpleNames : {
		resNames : [],
		fullNames : []
	},
	getDetailValue : function() {
		return this.detailValue;
	},
	getSimpleNames : function() {
		return this.simpleNames;
	},
	setTextForInit : function(text) {
		this.textForInit = text;
	},
	onTrigger2Click : function() {
		var me = this;
		if (!me.disabled) {
			if (!me.sys_common_moduleChooser) {
				me.sys_common_moduleChooser = Ext.create(
						'Ext.components.ModuleChooserWin', {
							title : '模块选择',
							width : 700,
							height : 400,
							closeAction : 'hide',
							multiSelect : me.multiSelect,
							submitHandler : function(records) {
								me.detailValue = records;
								var fullNames = [], resNames = [], display = [], value = [];
								for (var i = 0; i < records.length; i++) {
									resNames.push(records[i].name);
									fullNames.push(records[i].nameCn);
									display.push(records[i][me.displayField]);
									value.push(records[i][me.valueField]);
								}
								var simpleNames = {
									resNames : resNames,
									fullNames : fullNames
								};
								me.simpleNames = simpleNames;
								me._vals = {
									display : display,
									value : value
								}
								me.setDisplayValue(display.join(','));
							}
						});
			}
			this.sys_common_moduleChooser.show();
		}
	},
	onTrigger1Click : function() {
		this.reset();
	},
	reset : function() {
		this.simpleNames = {
			resNames : [],
			fullNames : []
		};
		this.detailValue = null;
		this.setValue("");
	},
	setDisplayValue : function(value) {
		this.hiddenFieldName = value;
		var text;
		if (this.textForInit && this.textForInit != '' && value != null
				&& value != '') {
			text = this.textForInit;
		} else {
			text = value;
		}
		if (text != '') {
			// this.triggerConfigs[0].show();
		} else {
			// this.triggerConfigs[0].hide();
		}
		Ext.form.TriggerField.superclass.setValue.call(this, text);
		this.value = text;
	},
	setValue : function(value) {
		this.setDisplayValue(value);
		if (value && value.length) {
			if (typeof(value.join) == 'function') {
				this._vals.value = value;
			} else if (typeof(value.split) == 'function') {
				this._vals.value = value.split(',');
			}
		} else {
			this._vals.value = [];
		}
	},
	getValue : function() {
		return this._vals.value.join(',');
	},
	initComponent : function() {
		this.callParent();
	}
});
