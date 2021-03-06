/**
 * The Ext.grid.plugin.CellEditing plugin injects editing at a cell level for a Grid. Only a single
 * cell will be editable at a time. The field that will be used for the editor is defined at the
 * {@link Ext.grid.column.Column#editor editor}. The editor can be a field instance or a field configuration.
 *
 * If an editor is not specified for a particular column then that cell will not be editable and it will
 * be skipped when activated via the mouse or the keyboard.
 *
 * The editor may be shared for each column in the grid, or a different one may be specified for each column.
 * An appropriate field type should be chosen to match the data structure that it will be editing. For example,
 * to edit a date, it would be useful to specify {@link Ext.form.field.Date} as the editor.
 *
 *     @example
 *     Ext.create('Ext.data.Store', {
 *         storeId:'simpsonsStore',
 *         fields:['name', 'email', 'phone'],
 *         data:{'items':[
 *             {"name":"Lisa", "email":"lisa@simpsons.com", "phone":"555-111-1224"},
 *             {"name":"Bart", "email":"bart@simpsons.com", "phone":"555-222-1234"},
 *             {"name":"Homer", "email":"home@simpsons.com", "phone":"555-222-1244"},
 *             {"name":"Marge", "email":"marge@simpsons.com", "phone":"555-222-1254"}
 *         ]},
 *         proxy: {
 *             type: 'memory',
 *             reader: {
 *                 type: 'json',
 *                 root: 'items'
 *             }
 *         }
 *     });
 *
 *     Ext.create('Ext.grid.Panel', {
 *         title: 'Simpsons',
 *         store: Ext.data.StoreManager.lookup('simpsonsStore'),
 *         columns: [
 *             {header: 'Name',  dataIndex: 'name', editor: 'textfield'},
 *             {header: 'Email', dataIndex: 'email', flex:1,
 *                 editor: {
 *                     xtype: 'textfield',
 *                     allowBlank: false
 *                 }
 *             },
 *             {header: 'Phone', dataIndex: 'phone'}
 *         ],
 *         selType: 'cellmodel',
 *         plugins: [
 *             Ext.create('Ext.grid.plugin.CellEditing', {
 *                 clicksToEdit: 1
 *             })
 *         ],
 *         height: 200,
 *         width: 400,
 *         renderTo: Ext.getBody()
 *     });
 */
Ext.define('Ext.grid.plugin.CellEditing', {
    alias: 'plugin.cellediting',
    extend: 'Ext.grid.plugin.Editing',
    requires: ['Ext.grid.CellEditor', 'Ext.util.DelayedTask'],

    constructor: function() {
        /**
         * @event beforeedit
         * Fires before cell editing is triggered. Return false from event handler to stop the editing.
         *
         * @param {Ext.grid.plugin.CellEditing} editor
         * @param {Object} e An edit event with the following properties:
         *
         * - grid - The grid
         * - record - The record being edited
         * - field - The field name being edited
         * - value - The value for the field being edited.
         * - row - The grid table row
         * - column - The grid {@link Ext.grid.column.Column Column} defining the column that is being edited.
         * - rowIdx - The row index that is being edited
         * - colIdx - The column index that is being edited
         * - cancel - Set this to true to cancel the edit or return false from your handler.
         */
        /**
         * @event edit
         * Fires after a cell is edited. Usage example:
         *
         *     grid.on('edit', function(editor, e) {
         *         // commit the changes right after editing finished
         *         e.record.commit();
         *     };
         *
         * @param {Ext.grid.plugin.CellEditing} editor
         * @param {Object} e An edit event with the following properties:
         *
         * - grid - The grid
         * - record - The record that was edited
         * - field - The field name that was edited
         * - value - The value being set
         * - originalValue - The original value for the field, before the edit.
         * - row - The grid table row
         * - column - The grid {@link Ext.grid.column.Column Column} defining the column that was edited.
         * - rowIdx - The row index that was edited
         * - colIdx - The column index that was edited
         */
        /**
         * @event validateedit
         * Fires after a cell is edited, but before the value is set in the record. Return false from event handler to
         * cancel the change.
         *
         * Usage example showing how to remove the red triangle (dirty record indicator) from some records (not all). By
         * observing the grid's validateedit event, it can be cancelled if the edit occurs on a targeted row (for
         * example) and then setting the field's new value in the Record directly:
         *
         *     grid.on('validateedit', function(editor, e) {
         *       var myTargetRow = 6;
         *
         *       if (e.row == myTargetRow) {
         *         e.cancel = true;
         *         e.record.data[e.field] = e.value;
         *       }
         *     });
         *
         * @param {Ext.grid.plugin.CellEditing} editor
         * @param {Object} e An edit event with the following properties:
         *
         * - grid - The grid
         * - record - The record being edited
         * - field - The field name being edited
         * - value - The value being set
         * - originalValue - The original value for the field, before the edit.
         * - row - The grid table row
         * - column - The grid {@link Ext.grid.column.Column Column} defining the column that is being edited.
         * - rowIdx - The row index that is being edited
         * - colIdx - The column index that is being edited
         * - cancel - Set this to true to cancel the edit or return false from your handler.
         */
        /**
         * @event canceledit
         * Fires when the user started editing a cell but then cancelled the edit.
         * @param {Ext.grid.plugin.CellEditing} editor
         * @param {Object} e An edit event with the following properties:
         * 
         * - grid - The grid
         * - record - The record that was edited
         * - field - The field name that was edited
         * - value - The value being set
         * - row - The grid table row
         * - column - The grid {@link Ext.grid.column.Column Column} defining the column that was edited.
         * - rowIdx - The row index that was edited
         * - colIdx - The column index that was edited
         */

        this.callParent(arguments);
        this.editors = new Ext.util.MixedCollection(false, function(editor) {
            return editor.editorId;
        });
        this.editTask = new Ext.util.DelayedTask();
    },

    onReconfigure: function(){
        this.editors.clear();
        this.callParent();    
    },

    /**
     * @private
     * AbstractComponent calls destroy on all its plugins at destroy time.
     */
    destroy: function() {
        var me = this;
        me.editTask.cancel();
        me.editors.each(Ext.destroy, Ext);
        me.editors.clear();
        me.callParent(arguments);
    },
    
    onBodyScroll: function() {
        var me = this,
            ed = me.getActiveEditor(),
            scroll = me.view.el.getScroll();

        // Scroll happened during editing...
        if (ed && ed.editing) {
            // Terminate editing only on vertical scroll. Horiz scroll can be caused by tabbing between cells.
            if (scroll.top !== me.scroll.top) {
                if (ed.field) {
                    if (ed.field.triggerBlur) {
                        ed.field.triggerBlur();
                    } else {
                        ed.field.blur();
                    }
                }
            }
            // Horiz scroll just requires that the editor be realigned.
            else {
                 ed.realign();
            }
        }
        me.scroll = scroll;
    },

    // private
    // Template method called from base class's initEvents
    initCancelTriggers: function() {
        var me   = this,
            grid = me.grid,
            view = grid.view;
            
        view.addElListener('mousewheel', me.cancelEdit, me);
        me.mon(view, 'bodyscroll', me.onBodyScroll, me);
        me.mon(grid, {
            columnresize: me.cancelEdit,
            columnmove: me.cancelEdit,
            scope: me
        });
    },

    isCellEditable: function(record, columnHeader) {
        var me = this,
            context = me.getEditingContext(record, columnHeader);

        if (me.grid.view.isVisible(true) && context) {
            columnHeader = context.column;
            record = context.record;
            if (columnHeader && me.getEditor(record, columnHeader)) {
                return true;
            }
        }
    },

    /**
     * Starts editing the specified record, using the specified Column definition to define which field is being edited.
     * @param {Ext.data.Model} record The Store data record which backs the row to be edited.
     * @param {Ext.grid.column.Column} columnHeader The Column object defining the column to be edited.
     * @return `true` if editing was started, `false` otherwise.
     */
    startEdit: function(record, columnHeader) {
        var me = this,
            context = me.getEditingContext(record, columnHeader),
            value, ed;

        // Complete the edit now, before getting the editor's target
        // cell DOM element. Completing the edit causes a row refresh.
        // Also allows any post-edit events to take effect before continuing
        me.completeEdit();

        // Cancel editing if EditingContext could not be found (possibly because record has been deleted by an intervening listener), or if the grid view is not currently visible
        if (!context || !me.grid.view.isVisible(true)) {
            return false;
        }

        record = context.record;
        columnHeader = context.column;

        // See if the field is editable for the requested record
        if (columnHeader && !columnHeader.getEditor(record)) {
            return false;
        }

        value = record.get(columnHeader.dataIndex);
        context.originalValue = context.value = value;
        if (me.beforeEdit(context) === false || me.fireEvent('beforeedit', me, context) === false || context.cancel) {
            return false;
        }

        ed = me.getEditor(record, columnHeader);

        // Whether we are going to edit or not, ensure the edit cell is scrolled into view
        me.grid.view.cancelFocus();
        me.view.focusCell({
            row: context.row,
            column: context.colIdx
        });
        if (ed) {
            me.context = context;
            me.setActiveEditor(ed);
            me.setActiveRecord(record);
            me.setActiveColumn(columnHeader);

            // Defer, so we have some time between view scroll to sync up the editor
            me.editTask.delay(15, ed.startEdit, ed, [me.getCell(record, columnHeader), value]);
            me.editing = true;
            me.scroll = me.view.el.getScroll();
            return true;
        }
        return false;
    },

    completeEdit: function() {
        var activeEd = this.getActiveEditor();
        if (activeEd) {
            activeEd.completeEdit();
            this.editing = false;
        }
    },

    // internal getters/setters
    setActiveEditor: function(ed) {
        this.activeEditor = ed;
    },

    getActiveEditor: function() {
        return this.activeEditor;
    },

    setActiveColumn: function(column) {
        this.activeColumn = column;
    },

    getActiveColumn: function() {
        return this.activeColumn;
    },

    setActiveRecord: function(record) {
        this.activeRecord = record;
    },

    getActiveRecord: function() {
        return this.activeRecord;
    },

    getEditor: function(record, column) {
        var me = this,
            editors = me.editors,
            editorId = column.getItemId(),
            editor = editors.getByKey(editorId);

        if (editor) {
            return editor;
        } else {
            editor = column.getEditor(record);
            if (!editor) {
                return false;
            }

            // Allow them to specify a CellEditor in the Column
            if (!(editor instanceof Ext.grid.CellEditor)) {
                editor = new Ext.grid.CellEditor({
                    editorId: editorId,
                    field: editor,
                    ownerCt: me.grid
                });
            }
            editor.editingPlugin = me;
            editor.isForTree = me.grid.isTree;
            editor.on({
                scope: me,
                specialkey: me.onSpecialKey,
                complete: me.onEditComplete,
                canceledit: me.cancelEdit
            });
            editors.add(editor);
            return editor;
        }
    },
    
    // inherit docs
    setColumnField: function(column, field) {
        var ed = this.editors.getByKey(column.getItemId());
        Ext.destroy(ed, column.field);
        this.editors.removeAtKey(column.getItemId());
        this.callParent(arguments);
    },

    /**
     * Gets the cell (td) for a particular record and column.
     * @param {Ext.data.Model} record
     * @param {Ext.grid.column.Column} column
     * @private
     */
    getCell: function(record, column) {
        return this.grid.getView().getCell(record, column);
    },

    onSpecialKey: function(ed, field, e) {
        var me = this,
            grid = me.grid,
            sm;
            
        if (e.getKey() === e.TAB) {
            e.stopEvent();
            
            if (ed) {
                // Allow the field to act on tabs before onEditorTab, which ends
                // up calling completeEdit. This is useful for picker type fields.
                ed.onEditorTab(e);
            }
            
            sm = grid.getSelectionModel();
            if (sm.onEditorTab) {
                sm.onEditorTab(me, e);
            }
        }
    },

    onEditComplete : function(ed, value, startValue) {
        var me = this,
            grid = me.grid,
            activeColumn = me.getActiveColumn(),
            record;

        if (activeColumn) {
            record = me.context.record;

            me.setActiveEditor(null);
            me.setActiveColumn(null);
            me.setActiveRecord(null);
    
            if (!me.validateEdit()) {
                return;
            }
            // Only update the record if the new value is different than the
            // startValue. When the view refreshes its el will gain focus
            if (!record.isEqual(value, startValue)) {
                record.set(activeColumn.dataIndex, value);
            // Restore focus back to the view's element.
            } else {
                grid.getView().getEl(activeColumn).focus();
            }
            me.context.value = value;
            me.fireEvent('edit', me, me.context);
        }
    },

    /**
     * Cancels any active editing.
     */
    cancelEdit: function() {
        var me = this,
            activeEd = me.getActiveEditor(),
            viewEl = me.grid.getView().getEl(me.getActiveColumn());

        me.setActiveEditor(null);
        me.setActiveColumn(null);
        me.setActiveRecord(null);
        if (activeEd) {
            activeEd.cancelEdit();
            viewEl.focus();
            me.callParent(arguments);
        }
    },

    /**
     * Starts editing by position (row/column)
     * @param {Object} position A position with keys of row and column.
     */
    startEditByPosition: function(position) {
        var sm = this.grid.getSelectionModel();

        // Coerce the column position to the closest visible column
        position.column = this.view.getHeaderCt().getVisibleHeaderClosestToIndex(position.column).getIndex();
        if (sm.selectByPosition) {
            sm.selectByPosition(position);
        }
        return this.startEdit(position.row, position.column);
    }
});
