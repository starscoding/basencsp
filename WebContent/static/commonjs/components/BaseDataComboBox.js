Ext.Loader.setConfig({
			enabled : true
		});
Ext.Loader.setPath({
			'Ext.components' : eastcom.baseURL + '/static/commonjs/components'
		});
Ext.define('Ext.components.BaseDataComboBox', {
			extend : 'Ext.components.CustomDataComboBox',
			alias : 'widget.baseDataComboBox',
			parentName : '',// 父节点名称
			allFlag : true,// 是否显示全选
			getLoadParams : function() {
				return {
					type : this.parentName,
					allFlag : this.allFlag
				}
			},
			/** 更新数据 */
			updateData : function(parentName/* 父节点名称 */, allFlag/* 是否全选 */) {
				var me = this;
				me.parentName = parentName;
				me.allFlag = allFlag;
				me.callParent([{
							type : parentName,
							allFlag : allFlag
						}]);
			},
			loadUrl : eastcom.baseURL + '/common/loadCommonDatas',
			initComponent : function() {
				this.callParent(arguments);
			}
		});