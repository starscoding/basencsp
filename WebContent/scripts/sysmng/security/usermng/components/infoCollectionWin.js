Ext.define('Ext.user.infoCollectionWin', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.*', 'Ext.window.*', 'Ext.components.DateTimeField',
			'Ext.components.CustomDataComboBox',
			'Ext.components.BaseDataComboBox',
			'Ext.user.UserMngChooserField'],
	required : '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	initConf : {
		randomPsd : 0,// 是否采用随机密码，默认不采用
		showUserRegion : 0
		// 是否显示用户归属地市
	},
	userId : '',// 修改用户信息用
	currentRecord : '',// 修改用户信息用
	collapseDetailInfo : true,// 是否展开详细信息，新增默认不显示，编辑默认显示
	height : 410,
	refDeptId : [],
	loadRecord : function(record) {
		var me = this;
		me.getEl().mask(MSG_DATA_LOADING);
		me.userId = record.get('id');
		me.currentRecord = record;
		Ext.Ajax.request({
					url : eastcom.baseURL
							+ '/sysmng/security/querySingleUserInfo',
					method : 'POST',
					params : {
						id : me.userId,
						extensionFlag : true,
						chineseFlag : 'false'
					},
					success : function(form, action) {
						var result = Ext.JSON.decode(form.responseText).data;
						Ext.getCmp('username').setValue(result.username);
						Ext.getCmp('username').disable();
						Ext.getCmp('sex').setValue(result.sex);
						Ext.getCmp('password').setValue(result.password);
						Ext.getCmp('fullName').setValue(result.fullName);
						Ext.getCmp('email').setValue(result.email);
						Ext.getCmp('telNo').setValue(result.fixedNo);
						Ext.getCmp('mobileNo').setValue(result.mobileNo);
						Ext.getCmp('owner').setValue(result.owner);
						Ext.getCmp('userLevel').setValue(result.userLevel);
						Ext.getCmp('passwordExpridDays')
								.setValue(result.pwdExpiredDays);
						if (result.accoutExpiredStarttime) {
							Ext.getCmp('expridStartTime')
									.setValue(result.accoutExpiredStarttime);
						}
						if (result.accoutExpiredEndtime) {
							Ext.getCmp('expridEndTime')
									.setValue(result.accoutExpiredEndtime);
						}

						if (result.city != null && me.initConf.showUserRegion) {
							Ext.getCmp('city').setValue(result.city);
						}

						Ext.getCmp('department').setValue(result.deptName);
						me.refDeptId = result.deptId.split(',');
						// Ext.getCmp('role').setValue(result.roleName);
						if (Ext.getCmp('infoExtForm')) {
							Ext.getCmp('infoExtForm').setData(result.extension);
						}
						me.getEl().unmask();
					},
					failure : function(form, action) {
					}
				});
	},
	getDeptWin : function() {
		var me = this;
		var deptTree = Ext.create('Ext.user.userAccreditTree', {
					controllerName : 'getChangeDepartmentTreeByUser',
					userId : me.userId,
					buttons : [{
						text : BUTTON_OK,
						id : 'saveUserAccredit',
						disabled : true,
						deptChange : false,
						fireChange : function() {
							var me = this;
							if (me.deptChange == false) {
								me.disable();
							} else {
								me.enable();
							}
						},
						handler : function() {
							// this.up('form').getForm().isValid();
							var records = this.ownerCt.ownerCt.getChecked();
							if (records) {
								me.refDeptId = [records[0].get('id')];
								Ext.getCmp('department').setValue(records[0]
										.get('nameCn'));
								for (var i = 1; i < records.length; i++) {
									var record = records[i];
									me.refDeptId.push(record.get('id'));
									Ext.getCmp('department').setValue(Ext
											.getCmp('department').getValue()
											+ ',' + record.get('nameCn'));
								}
								this.ownerCt.ownerCt.ownerCt.close();
							} else {
								Ext.Msg.show({
											title : MSG_TITLE,
											msg : USER_SELECT_DEPT_INFO,
											buttons : Ext.Msg.OK,
											icon : Ext.Msg.INFO
										});
							}
						}
					}, {
						text : BUTTON_CANCEL,
						handler : function() {
							// this.up('form').getForm().reset();
							this.ownerCt.ownerCt.ownerCt.close();
						}
					}]
				});
		var deptWin = Ext.create('Ext.window.Window', {
					width : 500,
					height : 350,
					layout : 'fit',
					id : 'deptSelectWin',
					constrain : true,
					closeAction : 'hide',
					style : {
						zindex : 20001
					},
					// animateTarget : Ext.getCmp('department'),

					title : DEPARTMENTS,
					items : [deptTree],
					listeners : {
						show : function() {
							Ext.getCmp('infoCollectionWin').getEl().mask();
						},
						hide : function() {
							Ext.getCmp('infoCollectionWin').getEl().unmask();
						}
					}
				});
		return deptWin;
	},
	initComponent : function() {
		var me = this;
		me.title = BUTTON_DETAIL;
		me.frame = false;
		me.border = false;
		me.id = 'infoCollectionWin';
		// me.height = 200;
		me.width = 600;
		me.layout = 'fit';
		me.style = {
			zindex : 20000
		};
		var userRegion = me.initConf.showUserRegion ? {
			xtype : 'baseDataComboBox',
			parentName : 'region',
			id : 'city',
			allFlag : false,
			anchor : '100%',
			fieldLabel : USER_CITY
		} : null;
		var basicForm = Ext.create('Ext.form.Panel', {
			title : USER_INFO_BASIC_TAB,
			border : false,
			id : 'infoMainForm',
			bodyPadding : '5 5 0',
			fieldDefaults : {
				labelAlign : 'top',
				msgTarget : 'side'
			},
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
						fieldLabel : USERNAME,
						id : 'username',
						regex : new RegExp("^[a-zA-Z_][a-zA-Z0-9_]+$"),
						afterLabelTextTpl : me.required,
						invalidText : USER_INVALID_USERNAME,
						allowBlank : false,
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
						anchor : '95%',
						listeners : {
							blur : function(me) {
								if (me.getValue().length > 0) {
									Ext.Ajax.request({
										url : eastcom.baseURL
												+ '/sysmng/security/userExsistCheck',
										method : 'POST',
										params : {
											username : me.getValue()
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
									me.validate();
								}
							}
						}
					}, {
						xtype : 'textfield',
						fieldLabel : FULLNAME,
						afterLabelTextTpl : me.required,
						id : 'fullName',
						allowBlank : false,
						// regex : new
						// RegExp("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*"),
						anchor : '95%'
					}, {
						xtype : 'baseDataComboBox',
						parentName : 'sex',
						id : 'sex',
						allFlag : false,
						afterLabelTextTpl : me.required,
						anchor : '95%',
						value : '1',
						fieldLabel : SEX
					}, {
						xtype : 'customDataComboBox',
						id : 'userLevel',
						loadUrl : eastcom.baseURL
								+ '/sysmng/security/loadUserLevelCommonDatas',
						afterLabelTextTpl : me.required,
						anchor : '95%',
						value : '3',
						fieldLabel : USER_LEVEL
					}]
				}, {
					xtype : 'container',
					flex : 1,
					layout : 'anchor',
					items : [{
						xtype : 'textfield',
						fieldLabel : PASSWORD,
						inputType : 'password',
						id : 'password',
						afterLabelTextTpl : me.required,
						allowBlank : false,
						validateOnBlur : true,
						value : me.initConf.randomPsd == 1 ? '******' : '',
						anchor : '100%',
						validator : function(val) {
							if ((me.initConf.randomPsd == 1 
								|| me.userId.length) && val == '******') {
								return true;
							}
							/*if (val && val.length >= 8 && val.length <= 16) {
								var nums = val.match(/\d+/g);
								var chars = val.match(/[a-zA-Z]+/g);
								var specicals = val.match(/\W+/g);
								if (nums && chars && specicals) {
									return true;
								} else {
									return UE_PWD_NOTICE2;
								}
							} else {
								return UE_PWD_NOTICE1
							}*/
							var val_msg = validatePwd_util(Ext.getCmp('username').getValue(),val,Ext.getCmp('fullName').getValue());
							if(val_msg){
								return val_msg;
							}else{
								return true;
							}							
						}
					}, {
						xtype : 'textfield',
						id : 'mobileNo',
						fieldLabel : MOBILE,
						afterLabelTextTpl : me.required,
						allowBlank : false,
						regex : new RegExp("^0?\\d{11}$"),
						anchor : '100%'
					}, {
						xtype : 'numberfield',
						minValue : 0,
						anchor : '100%',
						id : 'passwordExpridDays',
						allowBlank : false,
						value : '90',
						afterLabelTextTpl : me.required,
						fieldLabel : USER_PWD_EXPRIED_DAYS
					}, {
						xtype : 'textfield',
						fieldLabel : DEPARTMENTS,
						id : 'department',
						afterLabelTextTpl : me.required,
						allowBlank : false,
						anchor : '100%',
						listeners : {
							focus : function() {
								if (Ext.getCmp('deptSelectWin') == null) {
									// me.getDeptWin().show();
									me.add(me.getDeptWin());
									Ext.getCmp('deptSelectWin').show();
								} else {
									Ext.getCmp('deptSelectWin').show();
								}
							}
						}
					}]
				}]
			}, {
				xtype : 'fieldset',
				id : 'moreInfo',
				// checkboxToggle : true,
				// collapsed : me.collapseDetailInfo,
				title : USER_DESC,
				layout : 'hbox',
				margin : '5 0 5 0',
				autoScroll : true,
				items : [{
							xtype : 'container',
							flex : 1,
							layout : 'anchor',
							defaults : {
								regexText : MSG_ILLEGAL_INPUT
							},
							items : [{
										xtype : 'textfield',
										fieldLabel : 'E-mail',
										id : 'email',
										vtype : 'email',
										anchor : '95%'
									}, {
										xtype : 'datetimefield',
										id : 'expridStartTime',
										endTimeField : {
											id : 'expridEndTime'
										},
										fieldLabel : USER_EXPRIED_STARTTIME,
										anchor : '95%'
									}]
						}, {
							xtype : 'container',
							flex : 1,
							layout : 'anchor',
							defaults : {
								regexText : MSG_ILLEGAL_INPUT
							},
							items : [{
										xtype : 'usermngchooserfield',
										id : 'owner',
										multiSelect : false,
										editable : false,
										displayField : 'userName',
										valueField : 'userName',
										fieldLabel : OWNER,
										anchor : '95%'
									}, {
										xtype : 'datetimefield',
										id : 'expridEndTime',
										startTimeField : {
											id : 'expridStartTime'
										},
										fieldLabel : USER_EXPRIED_ENDTIME,
										anchor : '95%'
									}]
						}, {
							xtype : 'container',
							flex : 1,
							layout : 'anchor',
							defaults : {
								regexText : MSG_ILLEGAL_INPUT
							},
							items : [{
								xtype : 'textfield',
								id : 'telNo',
								fieldLabel : TEL_NO,
								regex : new RegExp("^\\(?\\d{3,4}[-\\)]?\\d{7,8}$"),
								anchor : '100%'
							}, userRegion]
						}],
				listeners : {
					expand : function() {
						var left = Ext.getCmp('infoCollectionWin')
								.getPosition()[0];
						var top = Ext.getCmp('infoCollectionWin').getPosition()[1];
						if (top > 130) {
							Ext.getCmp('infoCollectionWin').setPosition(left,
									top - 100);
						}
					},
					collapse : function() {
						var left = Ext.getCmp('infoCollectionWin')
								.getPosition()[0];
						var top = Ext.getCmp('infoCollectionWin').getPosition()[1];
						if (top > 130) {
							Ext.getCmp('infoCollectionWin').setPosition(left,
									top + 100);
						}
					}
				}
			}]
		});
		var tabs = [basicForm];
		if (me.extConf && me.extConf.userExtEnabled == '1') {
			tabs.push(Ext.create('Ext.user.ExtensionPanel', {
						id : 'infoExtForm',
						title : USER_INFO_EXT_TAB,
						extConf : me.extConf
					}));
		}

		me.items = [{
					xtype : 'tabpanel',
					items : tabs
				}];
		me.buttons = [{
			text : BUTTON_OK,
			handler : function() {
				if (Ext.getCmp('infoMainForm').getForm().isValid()) {
					var username = Ext.getCmp('username').getValue();
					var password = Ext.getCmp('password').getValue();
					var sex = Ext.getCmp('sex').getValue();
					var passwordExpridDays = Ext.getCmp('passwordExpridDays')
							.getValue();
					var userLevel = Ext.getCmp('userLevel').getValue();
					var userLevelLable = Ext.getCmp('userLevel').getRawValue();
					var userDeptId = me.refDeptId[0];
					for (var i = 1; i < me.refDeptId.length; i++) {
						userDeptId = userDeptId + "," + me.refDeptId[i];
					}
					var fullName = Ext.getCmp('fullName').getValue();
					var owner = Ext.getCmp('owner').getValue();
					var email = Ext.getCmp('email').getValue();
					var telNo = Ext.getCmp('telNo').getValue();
					var mobileNo = Ext.getCmp('mobileNo').getValue();
					var expridStartTime = Ext.getCmp('expridStartTime')
							.getValue();
					var expridEndTime = Ext.getCmp('expridEndTime').getValue();
					var city = '';
					if (me.initConf.showUserRegion) {
						city = Ext.getCmp('city').getValue();
					}
					var params = {
						id : me.userId,
						username : username,
						password : password,
						sex : sex,
						passwordExpridDays : passwordExpridDays,
						userLevel : userLevel,
						userDeptId : userDeptId,
						fullName : fullName,
						owner : owner,
						email : email,
						telNo : telNo,
						mobileNo : mobileNo,
						accoutExpiredStarttime : expridStartTime,
						accoutExpiredEndtime : expridEndTime,
						city : city
					};
					if (Ext.getCmp('infoExtForm')) {
						if (!Ext.getCmp('infoExtForm').isValid()) {
							return
						}
						Ext
								.apply(params, Ext.getCmp('infoExtForm')
												.getValues());
					}
					me.getEl().mask(MSG_DATA_SAVING);
					var opt = (me.userId && me.userId.length) ? '0' : '1';
					Ext.Ajax.request({
								url : eastcom.baseURL
										+ '/sysmng/security/saveUser?opt='
										+ opt,
								method : 'POST',
								params : params,
								success : function(form, action) {
									var result = Ext.JSON
											.decode(form.responseText);
									if (result.success == 'true') {
										Ext.Msg.show({
													title : MSG_SUCCESS,
													msg : result.msg,
													buttons : Ext.Msg.OK,
													icon : Ext.Msg.INFO
												});
										me.close();
										if (me.userId.length > 0) {
											me.currentRecord.set('fullName',
													fullName);
											me.currentRecord.set('userLevel',
													userLevelLable);
											me.currentRecord.set(
													'accoutExpiredStarttime',
													expridStartTime);
											me.currentRecord.set(
													'accoutExpiredEndtime',
													expridEndTime);
										} else {
//											Ext.getCmp('userMainGrid').store
//													.insert(0, result.data);
											Ext.getCmp('userMainGrid').getStore().load();
										}
									} else {
										Ext.Msg.show({
													title : MSG_TITLE,
													msg : result.msg,
													buttons : Ext.Msg.OK,
													icon : Ext.Msg.ERROR
												});
									}
									if (me.getEl()) {
										me.getEl().unmask();
									}
								},
								failure : function(form, action) {
									Ext.Msg.show({
												title : MSG_TITLE,
												msg : result.msg,
												buttons : Ext.Msg.OK,
												icon : Ext.Msg.ERROR
											});
								}
							});

				} else {
					Ext.Msg.show({
								title : MSG_TITLE,
								msg : MSG_ALL_CONDITIONS,
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.INFO
							});
				}
			}
		}, {
			text : BUTTON_CANCEL,
			handler : function() {
				me.close();
			}
		}];
		me.listeners = {
			show : function() {
				Ext.getBody().mask();
			},
			close : function() {
				Ext.getBody().unmask();
				if (Ext.getCmp('deptSelectWin')) {
					Ext.getCmp('deptSelectWin').close();
				}
			}
		};
		this.callParent();
	}
});