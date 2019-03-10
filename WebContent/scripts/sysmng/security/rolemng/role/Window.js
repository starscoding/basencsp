Ext.define('Ext.role.Window', {
	extend : 'Ext.window.Window',
	uses : ['Ext.layout.container.Border', 'Ext.form.field.Text',
			'Ext.form.field.ComboBox', 'Ext.toolbar.TextItem','Ext.Date.*'],
	modal : true,
	id : 'roleWindow',
	resizable : false,
	width : 450,
	border : false,
	bodyBorder : false,
	layout : 'fit',
	typeFlag : 0,// 新增或者修改标识，0：新增，1：修改
	currentId:'',//编辑角色时用
	currentRecord:null,//编辑角色时用
	required : '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	loadRecord : function(record) {
		var me = this;
		if (record) {
			me.currentId = record.get('id');
			me.currentRecord = record;
			Ext.getCmp('roleName').disable();
			Ext.getCmp('roleName').setValue(record.get('name'));
			Ext.getCmp('roleNameCn').oldValue = record.get('nameCn');
			Ext.getCmp('roleNameCn').validatorValue = true;
			Ext.getCmp('roleNameCn').setValue(record.get('nameCn'));
		//	Ext.getCmp('validEndtime').setValue(record.get('validEndtime'));
		//	Ext.getCmp('validStarttime').setValue(record.get('validStarttime'));
			Ext.getCmp('roleDescription').setValue(record.get('description'));
		}
	},
	initComponent : function() {
		var me = this;
		this.items = [{
			xtype : 'form',
			bodyPadding : 5,
			fieldDefaults : {
				labelAlign : 'top',
				msgTarget : 'side'
			},
			// defaults : {
			// labelAlign : 'top',
			// labelWidth : 90,
			// width : 300,
			// msgTarget : 'side'
			// },
			items : [{
				xtype : 'container',
				anchor : '100%',
				layout : 'hbox',
				items : [{
					xtype : 'container',
					flex : 1,
					layout : 'anchor',
					items : [{
						xtype : 'textfield',
						fieldLabel : ROLE_NAME,
						name : 'name',
						id : 'roleName',
						allowBlank : false,
						afterLabelTextTpl : me.required,
						anchor : '95%',
						validatorValue : false,
						validateOnBlur : false,
						validateOnChange : false,
						validator : function() {
							if (this.validatorValue) {
								return true;
							} else {
								if (this.getValue().length > 0) {
									return MSG_NAME_EXSIST;
								} else {
									return '';
								}
							}
						},
						listeners : {
							blur : function(me) {
								if (me.getValue().length > 0) {
									Ext.Ajax.request({
										url : eastcom.baseURL
												+ '/sysmng/security/roleNameExsistCheck',
										method : 'POST',
										params : {
											name : me.getValue(),
											type : 'name'
										},
										success : function(form, action) {
											var result = Ext.JSON
													.decode(form.responseText);
											if (result.success == 'true') {
												if (result.data == true) {
													me.validatorValue = false;
												} else {
													me.validatorValue = true;
												}
												me.validate();
											} else {
												Ext.Msg.show({
															title : MSG_FAILURE,
															msg : result.msg,
															buttons : Ext.Msg.OK,
															icon : Ext.Msg.ERROR
														});
												Ext.getBody().mask();
											}
										},
										failure : function(form, action) {
											Ext.Msg.show({
														title :MSG_ERROR,
														msg : MSG_ERROR_PROMPT,
														buttons : Ext.Msg.OK,
														icon : Ext.Msg.ERROR
													});
											Ext.getBody().mask();
										}
									});
								} else {
									me.validate();
								}
							}
						}
					}]
				}, {
					xtype : 'container',
					flex : 1,
					layout : 'anchor',
					items : [{
						xtype : 'textfield',
						fieldLabel : ROLE_FULLNAME,
						allowBlank : false,
						afterLabelTextTpl : me.required,
						name : 'nameCn',
						id : 'roleNameCn',
						anchor : '100%',
						hasChanged : false,// 编辑状态时用
						oldValue : '',// 编辑状态时用
						validatorValue : false,
						validateOnBlur : false,
						validateOnChange : false,
						validator : function() {
							if (this.validatorValue) {
								return true;
							} else {
								if (this.getValue().length > 0) {
									return MSG_NAME_EXSIST;
								} else {
									return '';
								}
							}
						},
						listeners : {
							change : function(thisObj, newValue) {
								if (newValue == thisObj.oldValue) {
									thisObj.hasChanged = false;
								} else {
									thisObj.hasChanged = true;
								}
							},
							blur : function(me) {
								if (me.hasChanged) {
									Ext.Ajax.request({
										url : eastcom.baseURL
												+ '/sysmng/security/roleNameExsistCheck',
										method : 'POST',
										params : {
											name : me.getValue(),
											type : 'nameCn'
										},
										success : function(form, action) {
											var result = Ext.JSON
													.decode(form.responseText);
											if (result.success == 'true') {
												if (result.data == true) {
													me.validatorValue = false;
												} else {
													me.validatorValue = true;
												}
												me.validate();
											} else {
												Ext.Msg.show({
															title : MSG_FAILURE,
															msg : result.msg,
															buttons : Ext.Msg.OK,
															icon : Ext.Msg.ERROR
														});
												Ext.getBody().mask();
											}
										},
										failure : function(form, action) {
											Ext.Msg.show({
														title : MSG_ERROR,
														msg : MSG_ERROR_PROMPT,
														buttons : Ext.Msg.OK,
														icon : Ext.Msg.ERROR
													});
											Ext.getBody().mask();
										}
									});
								} else {
									if(me.getValue().length>0){
										me.validatorValue = true;
									}
									me.validate();
								}
							}
						}
					}]
				}]
			}, {
				xtype : 'textareafield',
				fieldLabel : ROLE_DESC,
				name : 'description',
				id : 'roleDescription',
				height : 140,
				anchor : '100%',
				listeners : {
					scope : this
				}
			}]
		}];
		this.buttons = [{
					text : BUTTON_OK,
					scope : this,
					formBind : true,
					handler : this.submitForm
				}, {
					text : BUTTON_CANCEL,
					scope : this,
					handler : this.canel
				}];
		this.callParent(arguments);
		this.addEvents('validSuccess');
	},
	canel : function() {
		// this.down('form').getForm().reset();
		this.close();
	},
	submitForm : function() {
		var form = this.down('form').getForm();
		// 0:新增 1：修改
		form.currentId = this.currentId;
		var newRecord = this.currentRecord;
		if(newRecord){
			newRecord.set('nameCn',Ext.getCmp('roleNameCn').getValue());
		//	newRecord.set('validEndtime',Ext.Date.format(Ext.getCmp('validEndtime').getValue(),'Y-m-d H:i:s'));
		//	newRecord.set('validStarttime',Ext.Date.format(Ext.getCmp('validStarttime').getValue(),'Y-m-d H:i:s'));
			newRecord.set('description',Ext.getCmp('roleDescription').getValue());
		}
		var opt = this.typeFlag;
		if (form.isValid()) {
			this.getEl().mask(MSG_DATA_SAVING);
			this.fireEvent('validSuccess', opt, form);
		}
	}
});

// 时间验证
Ext.apply(Ext.form.field.VTypes, {
			daterange : function(val, field) {
				var date = field.parseDate(val);

				if (!date) {
					return false;
				}
				if (field.startDateField
						&& (!this.dateRangeMax || (date.getTime() != this.dateRangeMax
								.getTime()))) {
					var start = field.up('form').down('#'
							+ field.startDateField);
					start.setMaxValue(date);
					start.validate();
					this.dateRangeMax = date;
				} else if (field.endDateField
						&& (!this.dateRangeMin || (date.getTime() != this.dateRangeMin
								.getTime()))) {
					var end = field.up('form').down('#' + field.endDateField);
					end.setMinValue(date);
					end.validate();
					this.dateRangeMin = date;
				}
				/*
				 * Always return true since we're only using this vtype to set
				 * the min/max allowed values (these are tested for after the
				 * vtype test)
				 */
				return true;
			},

			daterangeText : 'Start date must be less than end date',

			password : function(val, field) {
				if (field.initialPassField) {
					var pwd = field.up('form').down('#'
							+ field.initialPassField);
					return (val == pwd.getValue());
				}
				return true;
			},

			passwordText : 'Passwords do not match'
		});