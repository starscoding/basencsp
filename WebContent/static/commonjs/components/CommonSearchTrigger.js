/**
 * @name 搜索栏组件
 * @author JJF
 * @since JUN 18 2013
 * @description 搜索栏组件
 * @param searchAction(key)
 *            查询触发函数(key为输入的关键字)
 *            @example
 *            var searchTrigger = Ext.create('Ext.components.CommonSearchTrigger', {
				searchAction : function(key){
						console.log(key);
					}
			});
 */
Ext.define('Ext.components.CommonSearchTrigger', {
			extend : 'Ext.form.field.Trigger',
			alias : 'widget.commonsearchtrigger',
			trigger1Cls : Ext.baseCSSPrefix + 'form-search-trigger',
			margin : '0 5 0 0',
			width : 200,
			emptyText : MSG_PLEASE_INSERT,
			enableKeyEvents : true,
			onTriggerClick : function() {
				var me = this;
				if (typeof(me.searchAction) == 'function') {
					me.searchAction(me.getValue());
				}
			},
			listeners : {
				keypress : function(me, e) {
					if (e.getKey() == 13) {
						if (typeof(me.searchAction) == 'function') {
							me.searchAction(me.getValue());
						}
					}
				}
			},
			searchAction : null,
			initComponent : function() {
				var me = this;
				me.callParent();
			}
		});