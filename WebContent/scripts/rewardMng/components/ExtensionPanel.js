Ext.define('Ext.user.ExtensionPanel', {
	extend : 'Ext.form.Panel',
	requires : ['Ext.components.BaseDataComboBox',
			'Ext.components.DateTimeField', 'Ext.components.UserChooserField',
			'Ext.components.ModuleChooserField'],
	fieldDefaults : {
		labelAlign : 'top',
		anchor : '95%'
	},
	bodyPadding : '5 5 0',
	multis : {},
	setData : function(fields) {
		var me = this, record = Ext.create('UserExt', {});
		Ext.each(fields, function(f, i) {
					var v = f.value;
					if (me.multis && me.multis[f.name]) {
						v = v.split(',');
					}
					record.set(f.name, v);
				});
		me.loadRecord(record);
	},
	getValues : function() {
		var me = this, vals = me.getForm().getFieldValues();
		for (var i in vals) {
			if (vals.hasOwnProperty(i)) {
				if (vals[i].join) {
					vals[i] = vals[i].join(',');
				}
			}
		}
		return vals;
	},
	initComponent : function() {
		var me = this, rules = me.extConf.userExtMappingRules, items1 = [], items2 = [], fields = [];
		Ext.each(rules, function(rule, i) {
					var regex = (rule.regExp && rule.regExp.length)
							? new RegExp(rule.regExp)
							: null;

					var item = {
						xtype : rule.type,
						fieldLabel : rule.nameCn,
						name : rule.name,
						regex : regex,
						parentName : rule.dataOrigin,
						multiSelect : rule.allowMulti
					};
					if (rule.allowMulti) {
						me.multis[rule.name] = true;
					}
					if (rule.type == 'customDataComboBox') {
						item.loadUrl = eastcom.baseURL + rule.dataOrigin
					}
					if (rule.valueField && rule.valueField.length) {
						item.valueField = rule.valueField;
						if (rule.type == 'modulechooserfield'
								|| rule.type == 'userchooserfield') {
							item.displayField = rule.valueField;
						}
					}
					if (i % 2 == 0) {
						items1.push(item);
					} else {
						item.anchor = '100%';
						items2.push(item);
					}

					fields.push({
								name : rule.name
							});
				});

		Ext.define('UserExt', {
					extend : 'Ext.data.Model',
					fields : fields
				});

		me.items = [{
					xtype : 'container',
					anchor : '100%',
					layout : 'hbox',
					items : [{
								xtype : 'container',
								flex : 1,
								layout : 'anchor',
								items : items1
							}, {
								xtype : 'container',
								flex : 1,
								layout : 'anchor',
								items : items2
							}]
				}];

		me.callParent();
	}
});