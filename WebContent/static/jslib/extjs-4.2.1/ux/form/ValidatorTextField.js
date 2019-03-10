/**
 * 带重名验证功能的输入框
 * 
 * @author JJF
 * @Date Nov 14 2012 使用方式: Ext.create('Ext.ux.form.ValidatorTextField', {
 *       controllerPath : eastcom.baseURL+
 *       '/sysmng/security/deptNameExsistCheck', typeFlag : 'nameCn', errorMsg :
 *       '中文名已存在，请重新输入！' })
 */
Ext.define('Ext.ux.form.ValidatorTextField', {
	extend : 'Ext.form.field.Text',
	alias : 'widget.validatortextfield',
	disableCheck:false,//禁用后台验证，需要时设为true
	controllerPath : '',// 后台controller地址
	typeFlag : '',// 选填项，指定字段
	otherInput:'',//预留字段
	errorMsg : '',// 重名错误提示信息
	hasChanged : false,
	oldValue : '',
	editing : false,
	validatorValue : true,
	validateOnBlur : false,
	validateOnChange : false,
	resetConfig : function() {
		var me = this;
		me.hasChanged = false;
		me.oldValue = '';
		me.editing = false;
		me.validatorValue = true;
		me.validateOnBlur = false;
		me.validateOnChange = false;
	},
	validator : function() {
		if (this.validatorValue) {
			return true;
		} else {
			if (this.getValue().length > 0) {
				return this.errorMsg;
			} else {
				return '';
			}
		}
	},
	listeners : {
		focus : function(me) {
			me.editing = true;
		},
		change : function(thisObj, newValue) {
			if (thisObj.editing) {
				if (newValue == thisObj.oldValue) {
					thisObj.hasChanged = false;
				} else {
					thisObj.hasChanged = true;
				}
			} else {
				thisObj.oldValue = newValue;
			}
		},
		blur : function(me) {
			me.editing = false;
			if (me.hasChanged && me.disableCheck==false) {
				Ext.Ajax.request({
							url : me.controllerPath,
							method : 'POST',
							params : {
								name : me.getValue(),
								type : me.typeFlag,
								otherInput : me.otherInput
							},
							success : function(form, action) {
								var result = Ext.JSON.decode(form.responseText);
								if (result.success == 'true') {
									if (result.data == true) {
										me.validatorValue = false;
									} else {
										me.validatorValue = true;
									}
									me.validate();
								} else {
									Ext.Msg.show({
												title : '错误',
												msg : result.msg,
												buttons : Ext.Msg.OK,
												icon : Ext.Msg.ERROR
											});
									Ext.getBody().mask();
								}
							},
							failure : function(form, action) {
								Ext.Msg.show({
											title : '错误',
											msg : '系统出错！请联系管理员',
											buttons : Ext.Msg.OK,
											icon : Ext.Msg.ERROR
										});
								Ext.getBody().mask();
							}
						});
			} else {
				if (me.getValue().length > 0) {
					me.validatorValue = true;
				} else {
					me.validatorValue = false;
				}
				me.validate();
			}
		}
	}
})