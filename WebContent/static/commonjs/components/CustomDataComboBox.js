/**
 * 自定义数据来源下拉框
 * 
 * @author JJF
 * @date JAN 20, 2015
 */
Ext.define('Ext.components.CustomDataComboBox', {
			extend : 'Ext.form.field.ComboBox',
			alias : 'widget.customDataComboBox',
			blankText : MSG_FIELD_NOTNULL,
			typeAhead : true,
			editable : false,
			loadUrl : '',// 加载路径
			displayField : "label",// 显示字段
			valueField : "value",// 值字段
			getLoadParams : function() {// 获取加载参数方法
				return {};
			},
			queryMode : 'local',
			loaded : false,
			/** 更新数据 */
			updateData : function(newParam, callback) {
				var me = this;
				store = me.store, proxy = store.proxy;
				proxy.extraParams = newParam;
				store.removeAll();
				me.loaded = false;
				store.load({
							callback : callback
						});
			},
			waitingSetVal : null,
			/** 重写setValue */
			setValue : function(val) {
				var me = this;
				if (!me.loaded) {
					me.waitingSetVal = val;
				} else {
					me.callParent([val]);
				}
			},
			initComponent : function() {
				var me = this;
				Ext.regModel('BaseData', {
							fields : [{
										name : 'id',
										type : 'string'
									}, {
										name : 'name',
										type : 'string'
									}, {
										name : 'label',
										type : 'string'
									}, {
										name : 'value',
										type : 'string'
									}, {
										name : 'order',
										type : 'int'
									}, {
										name : 'attribute',
										type : 'string'
									}]
						});
				me.store = Ext.create('Ext.data.Store', {
							model : 'BaseData',
							proxy : {
								type : 'ajax',
								actionMethods : {
									read : 'POST'
								},
								url : me.loadUrl,
								extraParams : me.getLoadParams()
							},
							sorters : [{
										property : 'order',
										direction : 'asc'
									}],
							autoLoad : true,
							listeners : {
								load : function() {
									me.loaded = true;
									if (me.waitingSetVal != null) {
										me.setValue(me.waitingSetVal);
									}
								}
							}
						});
				this.callParent(arguments);
			}
		});