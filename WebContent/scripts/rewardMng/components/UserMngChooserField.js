/**
 * 用户选择Trigger控件
 * 
 * @author cason 2009-02-11
 * @author JJF Jan 14 2014
 */

Ext.Loader.setConfig({
			enabled : true
		});
Ext.Loader.setPath('Ext.components', eastcom.baseURL
		+ '/static/commonjs/components');
Ext.define('Ext.user.UserMngChooserField', {
	extend : 'Ext.form.field.Trigger',
	trigger2Cls : 'x-form-search-trigger',
	trigger1Cls : 'x-form-clear-trigger',
	alias : 'widget.usermngchooserfield',
	editable : false,
	multiSelect : true,// 是否允许多选
	displayField : 'fullName',// 显示字段
	valueField : 'userName',// 值字段
	paramName : 'query',
	hiddenFieldName : '',
	width : 180,
	hiddenId : '',
	hiddenName : '',
	submitValueField : '',
	textForInit : '',
	triggerConfigs : [],
	detailValue : null,// 完整对象值
	simpleUserNames : {
		userNames : [],
		fullNames : []
	},
	_vals : {
		display : [],
		value : []
	},
	getDetailValue : function() {
		return this.detailValue;
	},
	getSimpleUserNames : function() {
		return this.simpleUserNames;
	},
	setTextForInit : function(text) {
		this.textForInit = text;
	},
	onTrigger2Click : function() {
		var me = this;
		if (!me.disabled) {
			if (!me.sys_common_userChooser) {
				me.sys_common_userChooser = Ext.create(
						'Ext.user.UserMngChooserWin', {
							title : '用户选择',
							width : 700,
							height : 400,
							closeAction : 'hide',
							multiSelect : me.multiSelect,
							submitHandler : function(val) {
								me.detailValue = val;
								var users = val.users;
								var fullNames = [], userNames = [], display = [], value = [];
								for (var i = 0; i < users.length; i++) {
									userNames.push(users[i].userName);
									fullNames.push(users[i].fullName);
									display.push(users[i][me.displayField]);
									value.push(users[i][me.valueField]);
								}
								var simpleNames = {
									userNames : userNames,
									fullNames : fullNames
								};
								me.simpleUserNames = simpleNames;
								me._vals = {
									display : display,
									value : value
								}
								me.setDisplayValue(display.join(','));
							}
						});
			}
			this.sys_common_userChooser.show();
		}
	},
	onTrigger1Click : function() {
		this.reset();
	},
	reset : function() {
		this.simpleUserNames = {
			userNames : [],
			fullNames : []
		};
		this._vals = {
			display : [],
			value : []
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
