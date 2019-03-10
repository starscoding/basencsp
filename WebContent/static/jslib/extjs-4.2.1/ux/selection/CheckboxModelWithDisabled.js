/**
 * 可以使用checkbox的disabled属性的CheckboxModel
 * add By: jiangjf@eastcom
 * 
 * 使用方式：
 * 		grid定义:selModel:Ext.create('Ext.ux.selection.CheckboxModelWithDisabled',{})
 * 		json数据:需要disable的行record中加入disabled:true属性
 * 		获取当前grid选中的记录使用:grid.getSelectionModel().getRealSelection()方法;
 */
Ext.define('Ext.ux.selection.CheckboxModelWithDisabled', {
	alias : 'selection.checkboxmodelwithdisabled',
	extend : 'Ext.selection.CheckboxModel',
	ddChecked : [],
	ddUnChecked : [],
	doInitData : true,
	//重写renderer方法添加disabled支持
	renderer : function(value, metaData, record, rowIndex, colIndex, store,
			view) {
		var me = this;
		var baseCSSPrefix = Ext.baseCSSPrefix;
		metaData.tdCls = baseCSSPrefix + 'grid-cell-special ' + baseCSSPrefix
				+ 'grid-cell-row-checker';
		//me.initEditDisableData(store);
		if (record.get('disabled') != null) {
			if (record.get('disabled') == 'true') {
				if (record.get('checked') != null) {
					if (record.get('checked') == 'true') {
						return '<div class="'
								+ baseCSSPrefix
								+ 'grid-row-checker-checked-disabled">&#160;</div>';
					} else {
						return '<div class="' + baseCSSPrefix
								+ 'grid-row-checker-disabled">&#160;</div>';
					}
				} else {
					return '<div class="' + baseCSSPrefix
							+ 'grid-row-checker-disabled">&#160;</div>';
				}
			} else {
				return '<div class="' + baseCSSPrefix
						+ 'grid-row-checker">&#160;</div>';
			}
		} else {
			return '<div class="' + baseCSSPrefix
					+ 'grid-row-checker">&#160;</div>';
		}
	},
	initEditDisableData : function(store) {
		var records = store.getRange();
		var me = this;
		if (me.doInitData) {
			me.ddChecked = [];
			me.ddUnChecked = [];
			for (var i = 0; i < records.length; i++) {
				var record = records[i];
				if (record.get('disabled') != null) {
					if (record.get('disabled') == 'true') {
						if (record.get('checked') != null) {
							if (record.get('checked') == 'true') {
								me.ddChecked.push(record);
								//console.log('push to 不可用选中:');
							} else {
								me.ddUnChecked.push(record);
								//console.log('push to 不可用未选中:');
							}
						} else {
							me.ddUnChecked.push(record);
							//console.log('push to 不可用未选中:');
						}
					}
				}
			}
			this.doInitData = false;
		}
	},
	// 使用本columnModel请调用该方法获取当前选中的条目
	getRealSelection : function() {
		var me = this;
		var nowSelected = me.selected.getRange();
		var checkedTemp = me.ddChecked;
		var unCheckedTemp = me.ddUnChecked;
		var result = [];
		result = result.concat(checkedTemp);
		for (var i = 0; i < nowSelected.length; i++) {
			var rec = nowSelected[i];
			var addFlag = true;
			for (var j = 0; j < checkedTemp.length; j++) {
				if (checkedTemp[j] === rec) {
					addFlag = false;
					break;
				}
			}
			if (addFlag) {
				for (var k = 0; k < unCheckedTemp.length; k++) {
					if (unCheckedTemp[k] === rec) {
						addFlag = false;
						break;
					}
				}
			}
			if (addFlag) {
				result.push(rec);
			}
		}
		return result;
	}
});
