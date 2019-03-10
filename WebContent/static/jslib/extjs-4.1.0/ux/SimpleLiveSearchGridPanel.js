/**
 * @class Ext.ux.SimpleLiveSearchGridPanel
 * @extends Ext.grid.Panel
 *          <p>
 *          页面搜索grid简化版
 *          </p>
 * @author Nicolas Ferrero
 * @editor JJF
 */
Ext.define('Ext.ux.SimpleLiveSearchGridPanel', {
	extend : 'Ext.grid.Panel',
	requires : ['Ext.toolbar.TextItem', 'Ext.form.field.Text',
			'Ext.ux.statusbar.StatusBar'],

	/**
	 * @private search value initialization
	 */
	searchValue : null,

	searchValueBefore : null,

	/**
	 * @private The row indexes where matching strings are found. (used by
	 *          previous and next buttons)
	 */
	indexes : [],

	/**
	 * @private The row index of the first search, it could change if next or
	 *          previous buttons are used.
	 */
	currentIndex : null,

	/**
	 * @private The generated regular expression used for searching.
	 */
	searchRegExp : null,

	/**
	 * @private Case sensitive mode.
	 */
	caseSensitive : false,

	/**
	 * @private Regular expression mode.
	 */
	regExpMode : false,

	/**
	 * @cfg {String} matchCls The matched string css classe.
	 */
	matchCls : 'x-livesearch-match',

	defaultStatusText : MSG_EMPTYRSLT,

	dataChanged : false,// if data changed must research

	// Component initialization override: adds the top and bottom toolbars and
	// setup headers renderer.
	initComponent : function() {
		var me = this;
		if (me.tbar && me.tbar.length) {
			me.tbar.push('->');
			me.tbar.push({
						xtype : 'textfield',
						name : 'searchField',
						fieldLabel : MSG_SEARCH_KEY,
						labelWidth : 50,
						labelAlign : 'right',
						width : 200,
						listeners : {
							change : {
								fn : me.onTextFieldChange,
								scope : this,
								buffer : 100
							}
						}
					});
			me.tbar.push({
						xtype : 'button',
						text : '&lt;',
						tooltip : BUTTON_PREVIOUS,
						handler : me.onPreviousClick,
						scope : me
					});
			me.tbar.push({
						xtype : 'button',
						text : '&gt;',
						tooltip : BUTTON_NEXT,
						handler : me.onNextClick,
						scope : me
					});
		} else {
			me.tbar = ['->', {
						xtype : 'textfield',
						name : 'searchField',
						fieldLabel : MSG_SEARCH_KEY,
						labelWidth : 50,
						labelAlign : 'right',
						width : 200,
						listeners : {
							change : {
								fn : me.onTextFieldChange,
								scope : this,
								buffer : 100
							}
						}
					}, {
						xtype : 'button',
						text : '&lt;',
						tooltip : BUTTON_PREVIOUS,
						handler : me.onPreviousClick,
						scope : me
					}, {
						xtype : 'button',
						text : '&gt;',
						tooltip : BUTTON_NEXT,
						handler : me.onNextClick,
						scope : me
					}];
		}
		me.bbar = Ext.create('Ext.ux.StatusBar', {
					defaultText : '',
					name : 'searchStatusBar'
				});
		me.callParent(arguments);
		me.appendStoreListeners();
	},

	appendStoreListeners : function() {
		var me = this;
		var store = me.getStore();
		var appendDatachangedFn = function() {
			me.dataChanged = true;
		};
		var appendStoreLoad = function(s, recs) {
			me.setDefaultStatus();
		};
		if (store.listeners) {
			if (store.listeners.datachanged) {
				var oldFn = store.listeners.datachanged;

				store.on('datachanged', function(p1, p2) {
							oldFn(p1, p2);
							appendDatachangedFn(p1, p2);
						});
			} else {
				store.on('datachanged', appendDatachangedFn);
			}

			if (store.listeners.load) {
				var oldFn = store.listeners.load;
				store.on('load', function(p1, p2, p3, p4) {
							oldFn(p1, p2, p3, p4);
							appendStoreLoad(p1, p2, p3, p4);
						});
			} else {
				store.on('load', appendStoreLoad);
			}

		} else {
			store.on({
						load : appendStoreLoad,
						datachanged : appendDatachangedFn
					});
		}
		me.reconfigure(store);
	},

	setDefaultStatus : function() {
		var me = this;
		me.statusBar.setStatus({
					text : MSG_TOTAL_COUNT + ':'
							+ me.getStore().getTotalCount(),
					iconCls : 'x-status-valid'
				});
	},

	// afterRender override: it adds textfield and statusbar reference and start
	// monitoring keydown events in textfield input
	afterRender : function() {
		var me = this;
		me.callParent(arguments);
		me.textField = me.down('textfield[name=searchField]');
		me.statusBar = me.down('statusbar[name=searchStatusBar]');
	},
	// detects html tag
	tagsRe : /<[^>]*>/gm,

	// DEL ASCII code
	tagsProtect : '\x0f',

	// detects regexp reserved word
	regExpProtect : /\\|\/|\+|\\|\.|\[|\]|\{|\}|\?|\$|\*|\^|\|/gm,

	/**
	 * In normal mode it returns the value with protected regexp characters. In
	 * regular expression mode it returns the raw value except if the regexp is
	 * invalid.
	 * 
	 * @return {String} The value to process or null if the textfield value is
	 *         blank or invalid.
	 * @private
	 */
	getSearchValue : function() {
		var me = this, value = me.textField.getValue();

		if (value === '') {
			return null;
		}
		if (!me.regExpMode) {
			value = value.replace(me.regExpProtect, function(m) {
						return '\\' + m;
					});
		} else {
			try {
				new RegExp(value);
			} catch (error) {
				me.statusBar.setStatus({
							text : error.message,
							iconCls : 'x-status-error'
						});
				return null;
			}
			// this is stupid
			if (value === '^' || value === '$') {
				return null;
			}
		}

		return value;
	},

	/**
	 * Finds all strings that matches the searched value in each grid cells.
	 * 
	 * @private
	 */
	onTextFieldChange : function() {
		var me = this;
		me.searchValueBefore = me.getSearchValue();
		if (me.searchTimeout) {
			clearTimeout(me.searchTimeout);
		}
		// 延迟0.3秒进行查询保证用户已经停止输入
		me.searchTimeout = setTimeout(function() {
					me.doSearch()
				}, 300);
	},
	doSearch : function() {
		var me = this;
		me.searchValue = me.getSearchValue();
		if (me.searchValue == me.searchValueBefore) {
			var count = 0;
			me.view.refresh();
			me.indexes = [];
			me.currentIndex = null;

			if (me.searchValue !== null) {
				me.searchRegExp = new RegExp(me.searchValue, 'g'
								+ (me.caseSensitive ? '' : 'i'));

				me.store.each(function(record, idx) {
					var td = Ext.fly(me.view.getNode(idx)).down('td'), cell, matches, cellHTML;
					while (td) {
						cell = td.down('.x-grid-cell-inner');
						matches = cell.dom.innerHTML.match(me.tagsRe);
						cellHTML = cell.dom.innerHTML.replace(me.tagsRe,
								me.tagsProtect);

						// populate indexes array, set currentIndex, and replace
						// wrap matched string in a span
						cellHTML = cellHTML.replace(me.searchRegExp,
								function(m) {
									count += 1;
									if (Ext.Array.indexOf(me.indexes, idx) === -1) {
										me.indexes.push(idx);
									}
									if (me.currentIndex === null) {
										me.currentIndex = idx;
									}
									return '<span class="' + me.matchCls + '">'
											+ m + '</span>';
								});
						// restore protected tags
						Ext.each(matches, function(match) {
									cellHTML = cellHTML.replace(me.tagsProtect,
											match);
								});
						// update cell html
						cell.dom.innerHTML = cellHTML;
						td = td.next();
					}
				}, me);

				// results found
				if (me.currentIndex !== null) {
					me.getSelectionModel().select(me.currentIndex);
				}
				me.statusBar.setStatus({
							text : MSG_TOTAL_COUNT + ':'
									+ me.getStore().getTotalCount() + ','
									+ MSG_MATCHES + ':' + me.indexes.length,
							iconCls : 'x-status-valid'
						});
			} else {
				me.setDefaultStatus();
			}

			// no results found
			if (me.currentIndex === null) {
				me.getSelectionModel().deselectAll();
			}

			// force textfield focus
			me.textField.focus();
			me.dataChanged = false;
		}
	},

	/**
	 * Selects the previous row containing a match.
	 * 
	 * @private
	 */
	onPreviousClick : function() {
		var me = this, idx;

		if (me.dataChanged) {
			me.doSearch();
		}

		if ((idx = Ext.Array.indexOf(me.indexes, me.currentIndex)) !== -1) {
			me.currentIndex = me.indexes[idx - 1];
			if (me.currentIndex == null) {
				me.currentIndex = me.indexes[me.indexes.length - 1];
			}
			me.getSelectionModel().select(me.currentIndex);
		}
	},

	/**
	 * Selects the next row containing a match.
	 * 
	 * @private
	 */
	onNextClick : function() {
		var me = this, idx;

		if (me.dataChanged) {
			me.doSearch();
		}

		if ((idx = Ext.Array.indexOf(me.indexes, me.currentIndex)) !== -1) {
			me.currentIndex = me.indexes[idx + 1] || me.indexes[0];
			me.getSelectionModel().select(me.currentIndex);
		}
	}
});