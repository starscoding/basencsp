/**
 * @class Ext.chooser.IconBrowser
 * @extends Ext.view.View
 * @author Ed Spencer
 * 
 * This is a really basic subclass of Ext.view.View. All we're really doing here
 * is providing the template that dataview should use (the tpl property below),
 * and a Store to get the data from. In this case we're loading data from a JSON
 * file over AJAX.
 */
Ext.define('Ext.chooser.IconBrowser', {
	extend : 'Ext.view.View',
	alias : 'widget.iconbrowser',

	uses : 'Ext.data.Store',

	singleSelect : true,
	overItemCls : 'x-view-over',
	itemSelector : 'div.thumb-wrap',
	tpl : [
			// '<div class="details">',
			'<tpl for=".">',
			'<div class="thumb-wrap">',
			'<div class="thumb">',
			(!Ext.isIE6
					? '<img src="../../../../static/images/common/appicons/{thumb}" style="width:60px;height:60px;"/>'
					: '<div style="width:74px;height:74'
							+ 'px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'../../../../static/images/common/appicons/{thumb}\')"></div>'),
			'</div>', '<span style="width:60px;">{name}</span>', '</div>',
			'</tpl>'
	// '</div>'
	],

	initComponent : function() {
		this.store = Ext.create('Ext.data.Store', {
					autoLoad : true,
					fields : ['name', 'thumb', 'type', 'typeCn'],
					proxy : {
						type : 'ajax',
						actionMethods : 'POST',
						url : eastcom.baseURL
								+ '/sysmng/security/queryResourceIcons',
						reader : {
							type : 'json',
							root : 'data'
						}
					}
				});

		this.callParent(arguments);
		this.store.sort();
	}
});