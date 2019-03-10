/**
 * ExtJs4实现下拉树选择框,可以实现单选和多选
 */
Ext.define('Common', {
	extend : 'Ext.data.Model',
	fields : [{
		name : 'id',
		type : 'string'
	}, {
		name : 'text',
		type : 'string'
	}, {
		name : 'value',
		type : 'string'
	}, {
		name : 'order',
		type : 'int'
	}, {
		name : 'leaf',
		type : 'boolean'
	}]
});

Ext.define("Ext.components.comboboxtree", {
	extend : "Ext.form.field.Picker",
	requires : ["Ext.tree.Panel"],
	alias : 'widget.treecombo',
	treeUrl : this.treeUrl,
	multiSelect : this.multiSelect || false,
	initComponent : function() {
		var self = this;
		Ext.apply(self, {
			fieldLabel : self.fieldLabel,
			labelWidth : self.labelWidth
		});
		self.callParent();
	},
	createPicker : function() {
		var self = this;
		var store = Ext.create('Ext.data.TreeStore', {
			model : 'Common',
			proxy : {
				type : 'ajax',
				url : self.treeUrl
			},
			sorters : [{
				property : 'leaf',
				direction : 'ASC'
			}, {
				property : 'text',
				direction : 'ASC'
			}],
			root : {
				id : self.rootId,
				text : self.rootText
			},
			nodeParameter : self.treeNodeParameter
		});
		self.picker = new Ext.tree.Panel({
			height : 300,
			autoScroll : true,
			floating : true,
			focusOnToFront : false,
			shadow : true,
			ownerCt : this.ownerCt,
			useArrows : true,
			store : store,
			animate : true,
			rootVisible : false
		});
		self.picker.on({
			checkchange : function(node, checked, obj) {
				var records = self.picker.getView().getChecked(), names = [], values = [];
				// 多选实现
				if (self.multiSelect) {
					Ext.Array.each(records, function(rec) {
						names.push(rec.get('text'));
						values.push(rec.get('value'));
					});
					self.setRawValue(values.join(';'));// 隐藏值
					self.setValue(names.join(';'));// 显示值
					//console.info(values);
				} else {
					/** 此处为单选 */
					var checkedNodes = self.picker.getView().getChecked();
					// alert(node.get("id"));
					if (node.get("checked")) {
						self.setRawValue(node.get("value"));// 隐藏值
						self.setValue(node.get("text"));// 显示值
					}
					for (var i = 0; i < checkedNodes.length; i++) {
						var n = checkedNodes[i];
						if (node.get("id") != n.get("id")) {
							n.set("checked", false);
						}
					}
				}
			}
		});
		return self.picker;
	},
	alignPicker : function() {
		var me = this, picker, isAbove, aboveSfx = '-above';
		if (this.isExpanded) {
			picker = me.getPicker();
			if (me.matchFieldWidth) {
				picker.setWidth(me.bodyEl.getWidth());
			}
			if (picker.isFloating()) {
				picker.alignTo(me.inputEl, "", me.pickerOffset);
				isAbove = picker.el.getY() < me.inputEl.getY();
				me.bodyEl[isAbove ? 'addCls' : 'removeCls'](me.openCls
						+ aboveSfx);
				picker.el[isAbove ? 'addCls' : 'removeCls'](picker.baseCls
						+ aboveSfx);
			}
		}
	}
});
