/**
 * 基础Grid
 */
Ext.define('Ext.components.BaseGrid', {
	extend : 'Ext.grid.Panel',
	require : ['Ext.ux.RowExpander'],
	initComponent : function() {
		var me = this, oriClumns = me.columns, oriStore = me.store;
		Ext.define('NoData', {
					extend : 'Ext.data.Model',
					fields : [{
								name : 'noDataColumn',
								type : 'string'
							}]
				});
		var noDataStore = Ext.create('Ext.data.Store', {
					model : 'NoData',
					data : [{
								noDataColumn : MSG_EMPTYRSLT
							}]
				});
		var noDataColumns = [{
					text : MSG_QUERYRSLT,
					sortable : false,
					dataIndex : 'noDataColumn',
					flex : 1,
					align : 'center'
				}];
		var oriSel, oriClick, oriDbClick;
		var switchMode = function(flag) {
			var blankFn = function() {
			};
			if (flag) {
				me.reconfigure(oriStore, oriClumns);
			} else {
				me.reconfigure(noDataStore, noDataColumns);
				// if (!oriSel && typeof me.events.beforeselect == 'object') {
				// oriSel = me.events.beforeselect.listeners[0].fn;
				// } else {
				// oriSel = blankFn;
				// }
				//
				// if (!oriClick && typeof me.events.beforecellclick ==
				// 'object') {
				// oriClick = me.events.beforecellclick.listeners[0].fn;
				// } else {
				// oriClick = blankFn;
				// }
				//
				// if (!oriDbClick
				// && typeof me.events.beforecelldblclick == 'object') {
				// oriDbClick = me.events.beforecelldblclick.listeners[0].fn;
				// } else {
				// oriDbClick = blankFn;
				// }
				//
				// me.un('beforeselect');
				// me.un('beforecellclick');
				// me.un('beforecelldblclick');
				//				
				// me.on('beforeselect', function() {// 禁止选中
				// return false;
				// });
				//
				// me.on('beforecellclick', function() {// 禁止选中
				// return false;
				// });
				// me.on('beforecelldblclick', function() {// 禁止选中
				// return false;
				// });
			}
		};
		var oriLoadEvent;
		if (me.store.listeners && typeof me.store.listeners.load == 'function') {
			oriLoadEvent = me.store.listeners.load;
		}

		var newLoad = function(st, records, successful, eOpts) {
			if (oriLoadEvent) {
				oriLoadEvent.call(me.store, st, records, successful, eOpts);
			}

			if (records.length == 0) {
				switchMode(false);
			}
		}
		me.store.on('load', newLoad);
		me.switchMode = switchMode;
		me.callParent();
	}
});