/**
	用户下拉列表框
 */
//Define the model for a baseData
Ext.regModel('User', {
    fields: [
		{name: 'name',     type: 'string'},
		{name: 'value',     type: 'string'},
		{name: 'order',     type: 'string'}
    ]
});

Ext.define('Ext.components.UserComboBox',{
	extend : 'Ext.form.field.ComboBox',
	xtype : 'userComboBox',
	blankText : MSG_FIELD_NOTNULL,
	typeAhead : true,
	editable : false,
	displayField : "name",
	valueField : "name",
	queryMode : 'local',
	initComponent : function(){
		this.store = Ext.create('Ext.data.Store', {
		    model: 'User',
			proxy : {
				type : 'ajax',
				actionMethods : {
					read : 'POST'
				},
				url : eastcom.baseURL + '/sysmng/security/getAllUserInfo'
			},
	        sorters : [{
	        	 property : 'order',
	             direction: 'asc'
	        }],
			autoLoad : true
		});
		this.callParent(arguments);
	}
});