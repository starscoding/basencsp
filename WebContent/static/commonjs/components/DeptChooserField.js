/**
 * 部门选择Trigger控件
 * 
 * @author JJF Oct 27, 2014
 */

Ext.Loader.setConfig({
			enabled : true
		});
Ext.Loader.setPath('Ext.components', eastcom.baseURL
				+ '/static/commonjs/components');
Ext.define('Ext.components.DeptChooserField', {
			extend : 'Ext.form.field.Trigger',
			trigger2Cls : 'x-form-search-trigger',
			trigger1Cls : 'x-form-clear-trigger',
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
					if (!me.sys_common_deptChooser) {
						me.sys_common_deptChooser = Ext.create(
								'Ext.components.DeptChooserWin', {
									title : '部门选择',
									width : 700,
									height : 400,
									closeAction : 'hide',
									submitHandler : function(records) {
										me.detailValue = records;
										var fullNames = [];
										var deptNames = [];
										for (var i = 0; i < records.length; i++) {
											deptNames.push(records[i].name);
											fullNames.push(records[i].nameCn);
										}
										var simpleNames = {
											deptNames : deptNames,
											fullNames : fullNames
										};
										me.simpleNames = simpleNames;
										me.setValue(fullNames.join(','));
									}
								});
					}
					this.sys_common_deptChooser.show();
				}
			},
			onTrigger1Click : function() {
				this.reset();
			},
			reset : function() {
				this.simpleNames = {
					deptNames : [],
					fullNames : []
				};
				this.detailValue = null;
				this.setValue("");
			},
			setValue : function(value) {
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
			getValue : function() {
				var _names = this.getSimpleNames(), names = '';
				if (_names && _names.deptNames && _names.deptNames.length) {
					names = _names.deptNames.join(',');
				}
				return names;
			},
			initComponent : function() {
				this.callParent();
			}
		});