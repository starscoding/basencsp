/**
 * @class Ext.chooser.Window
 * @extends Ext.window.Window
 * @author Ed Spencer
 * 
 * This is a simple subclass of the built-in Ext.window.Window class. Although
 * it weighs in at 100+ lines, most of this is just configuration. This Window
 * class uses a border layout and creates a DataView in the central region and
 * an information panel in the east. It also sets up a toolbar to enable sorting
 * and filtering of the items in the DataView. We add a few simple methods to
 * the class at the bottom, see the comments inline for details.
 */
Ext.Loader.setConfig({
			enabled : true
		});
Ext.Loader.setPath('Ext.components', '../../../../static/commonjs/components');
Ext.define('Ext.chooser.Window', {
	extend : 'Ext.window.Window',
	uses : ['Ext.layout.container.Border', 'Ext.form.field.Text',
			'Ext.form.field.ComboBox', 'Ext.toolbar.TextItem',
			'Ext.button.Button', 'Ext.data.proxy.Ajax',
			'Ext.chooser.InfoPanel', 'Ext.chooser.IconBrowser',
			'Ext.chooser.Window', 'Ext.ux.DataView.Animated',
			'Ext.toolbar.Spacer', 'Ext.components.BaseDataComboBox'],
	modal : true,
	height : 400,
	width : 600,
	closeAction : 'destroy',
	title : BUTTON_CHOOSE,
	closeAction : 'hide',
	layout : 'border',
	// modal: true,
	border : false,
	bodyBorder : false,

	/**
	 * initComponent is a great place to put any code that needs to be run when
	 * a new instance of a component is created. Here we just specify the items
	 * that will go into our Window, plus the Buttons that we want to appear at
	 * the bottom. Finally we call the superclass initComponent.
	 */
	initComponent : function() {
		this.items = [{
					xtype : 'panel',
					region : 'center',
					autoScroll : true,

					items : {
						xtype : 'iconbrowser',
						id : 'img-chooser-view',
						listeners : {
							scope : this,
							selectionchange : this.onIconSelect,
							itemdblclick : this.fireImageSelected
						}
					},

					tbar : [Ext.create('Ext.components.BaseDataComboBox', {
										parentName : 'resourceIconTypes',
										fieldLabel : RES_ICON_TYPE,
										allFlag : true,
										labelWidth : 45,
										width : 200,
										labelAlign : 'right',
										anchor : '100%',
										value : '全选',
										listeners : {
											scope : this,
											select : this.typeFilter
										}
									}), ' ', {
								xtype : 'textfield',
								name : 'filter',
								fieldLabel : MSG_SEARCH_KEY,
								labelAlign : 'right',
								width : 200,
								labelWidth : 55,
								listeners : {
									scope : this,
									buffer : 50,
									change : this.filter
								}
							}]
				}, {
					xtype : 'infopanel',
					region : 'east',
					split : true
				}];

		this.buttons = [{
					text : BUTTON_OK,
					scope : this,
					handler : this.fireImageSelected
				}, {
					text : BUTTON_CANCEL,
					scope : this,
					handler : function() {
						this.hide();
					}
				}];

		this.callParent(arguments);

		/**
		 * Specifies a new event that this component will fire when the user
		 * selects an item. The event is fired by the fireImageSelected function
		 * below. Other components can listen to this event and take action when
		 * it is fired
		 */
		this.addEvents(
				/**
				 * @event selected Fired whenever the user selects an image by
				 *        double clicked it or clicking the window's OK button
				 * @param {Ext.data.Model}
				 *            image The image that was selected
				 */
				'selected');
	},

	/**
	 * @private Called whenever the user types in the Filter textfield. Filters
	 *          the DataView's store
	 */
	filter : function(field, newValue) {
		var store = this.down('iconbrowser').store, dataview = this
				.down('dataview');

		store.suspendEvents();
		store.clearFilter();
		dataview.getSelectionModel().clearSelections();
		store.resumeEvents();
		store.filter({
					property : 'name',
					anyMatch : true,
					value : newValue
				});
	},

	typeFilter : function() {
		var field = this.down('combobox').getValue();
		var store = this.down('iconbrowser').store, dataview = this
				.down('dataview');
		store.suspendEvents();
		store.clearFilter();
		dataview.getSelectionModel().clearSelections();
		store.resumeEvents();
		if (field != '_ALL_') {
			store.filter({
						property : 'type',
						anyMatch : false,
						value : field
					});
		} else {
			store.filter({
						property : 'type',
						anyMatch : true,
						value : ''
					});
		}
	},

	/**
	 * @private Called whenever the user changes the sort field using the top
	 *          toolbar's combobox
	 */
	sort : function() {
		var field = this.down('combobox').getValue();

		this.down('dataview').store.sort(field);
	},

	/**
	 * Called whenever the user clicks on an item in the DataView. This tells
	 * the info panel in the east region to display the details of the image
	 * that was clicked on
	 */
	onIconSelect : function(dataview, selections) {
		var selected = selections[0];

		if (selected) {
			this.down('infopanel').loadRecord(selected);
		}
	},

	/**
	 * Fires the 'selected' event, informing other components that an image has
	 * been selected
	 */
	fireImageSelected : function() {
		var selectedImage = this.down('iconbrowser').selModel.getSelection()[0];

		if (selectedImage) {
			this.fireEvent('selected', selectedImage);
			this.hide();
		}
	}
});