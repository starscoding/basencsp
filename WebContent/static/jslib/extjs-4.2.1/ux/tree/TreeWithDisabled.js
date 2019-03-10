 /**
 * 可以使用节点checkbox的disabled属性的树
 * add By: jiangjf@eastcom
 * 
 * 使用方式：
 * 		实例化树:Ext.create('Ext.ux.tree.TreeWithDisabled',{})
 * 					也可以选择自定义树继承'Ext.ux.tree.TreeWithDisabled'即可
 * 		json数据:需要disable的行record中加入disabled:true属性
 * 		获取当前tree所有选中的数据使用:tree.getRealChecked()方法;
 */
Ext.define('Ext.ux.tree.TreeWithDisabled', {
			extend : 'Ext.tree.Panel',
			alias : 'widget.treewithdisabled',
			requires : ['Ext.ux.tree.TreeColumnWithDisabled'],
			totalRecords : [],
			ddChecked : [],
			ddUnChecked : [],
			initComponent : function() {
				var me = this;
				if (!me.columns) {// 修改treecolumn为修改的加了disable属性的自定义treecolumn
					if (me.initialConfig.hideHeaders === undefined) {
						me.hideHeaders = true;
					}
					me.addCls(Ext.baseCSSPrefix + 'autowidth-table');
					me.columns = [{
								xtype : 'treecolumnwithdisabled',
								text : 'Name',
								width : Ext.isIE6 ? null : 10000,
								dataIndex : me.displayField
							}];
				}

				me.initEditDisableData();

				this.callParent();
			},
			getRealChecked : function() {
				var me = this;
				var nowSelected = me.getChecked();
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
			},
			initEditDisableData : function() {
				var me = this;
				if (me.getRootNode()) {
					me.initRecords(me.getRootNode());
					var records = me.totalRecords;
					me.ddChecked = [];
					me.ddUnChecked = [];
					for (var i = 0; i < records.length; i++) {
						var record = records[i];
						if (record.get('disabled') != null) {
							if (record.get('disabled') == 'true') {
								if (record.get('checked') != null) {
									if (record.get('checked') == true) {
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
				} else {
					setTimeout(function() {
								me.initEditDisableData();
							}, 2000);
				}
			},
			initRecords : function(node) {
				var me = this;
				var childs = node.childNodes;
				if (childs) {
					for (var i = 0; i < childs.length; i++) {
						me.totalRecords.push(childs[i]);
						me.initRecords(childs[i]);
					}
				}
			}
		});