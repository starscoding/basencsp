/**
 * @name EXT上传控件
 * @author JJF
 * @since JUN 17 2013
 * @description Ext上传控件
 *              @example
 * 
 * Ext.create('Ext.components.CommonUploadPanel', {
						uploadConfig : {
							url : eastcom.baseURL
									+ '/sysmng/notification/attachmentOperate',
							maskId : 'addNotificationWin',
							callback : function(name) {
							},
							params : {
								type : 'upload'
							}
						},
						deleteConfig : {
							url : eastcom.baseURL
									+ '/sysmng/notification/attachmentOperate',
							maskId : 'addNotificationWin',
							callback : function(realNames) {
							},
							params : {
								type : 'delete'
							}
						}
					})
 */
Ext.define('Ext.components.CommonUploadPanel', {
	extend : 'Ext.panel.Panel',
	layout : 'anchor',
	showClearButton : true,
	alias : 'widget.commonuploadpanel',
	currentNames : [],// 当前存在的名称集合
	oriNames : [],// 加载数据之后存在的名称，判断本次编辑时是否新增或删除了附件
	isValid : function() {
		var me = this;
		if (me.down('[name=uploadfile]').getValue().length) {
			Ext.Msg.show({
						title : MSG_TITLE,
						msg : NOTIFY_ACCACHMENT_NOT_UPLOAD,
						buttons : Ext.Msg.OK,
						icon : Ext.Msg.INFO
					});
			return false;
		} else {
			return true;
		}
	},
	getChangesThisTime : function() {
		var add = [];
		var del = [];
		var me = this;
		var map = new Ext.util.HashMap();
		for (var i = 0; i < me.currentNames.length; i++) {
			map.add(me.currentNames[i].realName, me.currentNames[i].name);
		}
		for (var i = 0; i < me.oriNames.length; i++) {
			if (!map.removeAtKey(me.oriNames[i].realName)) {
				del.push(me.oriNames[i]);
			}
		}
		map.each(function(key, val, len) {
					add.push({
								name : val,
								realName : key
							});
				});
		return {
			add : add,
			del : del
		}
	},
	/**
	 * 外部执行保存操作之后将原始数据更新为当前数据
	 */
	doSave : function() {
		this.oriNames = this.currentNames;
	},
	loadData : function(names) {
		var me = this;
		var displayField = me.down('panel[name=displayField]');
		if (names && names.length && names != 'null') {
			displayField.show();
			for (var i = 0; i < names.length; i++) {
				displayField.addAttachment(names[i]);
				me.oriNames.push(names[i]);
				me.currentNames.push(names[i]);
			}
		}
	},
	checkIsShow : function() {
		var me = this;
		var displayPanel = me.down('panel[name=displayField]');
		if (displayPanel) {
			displayPanel.setVisible(me.currentNames.length);
		}

	},
	uploadConfig : {
		url : '',
		maskId : '',
		params : {},
		callback : function(name) {
		}
	},
	deleteConfig : {
		url : '',
		maskId : '',
		params : {},
		callback : function(names) {
		}
	},
	checkSame : function(item) {
		var array = this.currentNames;
		var flag = false;
		for (var i = 0; i < array.length; i++) {
			if (array[i].name == item) {
				flag = true;
			}
		}
		return !flag;
	},
	removeName : function(realNames) {
		var me = this;
		var currentNames = [];
		var removeNames = [];
		for (var i = 0; i < me.currentNames.length; i++) {
			var exsist = false;
			for (var j = 0; j < realNames.length; j++) {
				if (me.currentNames[i].realName == realNames[j]) {
					exsist = true;
					break;
				}
			}
			if (!exsist) {
				currentNames.push(me.currentNames[i]);
			}else{
				removeNames.push(me.currentNames[i]);
			}
		}
		me.currentNames = currentNames;
		return removeNames;
	},
	saveFile : function() {
		var me = this;
		var form = me.down('form[name=uploadForm]');
		if (me.uploadConfig && me.uploadConfig.url
				&& me.uploadConfig.url.length) {
			var uploadfileValue = form.down('[name=uploadfile]').getValue();
			if (uploadfileValue && uploadfileValue.length) {
				if (me.checkSame(uploadfileValue)) {
					var mask;
					if (me.uploadConfig.maskId && me.uploadConfig.maskId.length) {
						mask = new Ext.LoadMask(Ext
										.getCmp(me.uploadConfig.maskId), {
									msg : MSG_DATA_SAVING
								});
					} else {
						mask = new Ext.LoadMask(me, {
									msg : MSG_DATA_SAVING
								});
					}
					mask.show();
					form.submit({
						url : me.uploadConfig.url,
						method : 'POST',
						timeout : 120000,
						params : me.uploadConfig.params,
						success : function(form, action) {
							var name = {
								name : action.result.msg,
								realName : action.result.data
							};
							me.currentNames.push(name);
							me.down('[name=displayField]').addAttachment(name);
							if (me.uploadConfig.callback
									&& typeof(me.uploadConfig.callback) == 'function') {
								me.uploadConfig.callback(name);
							}
							mask.hide();
							me.checkIsShow();
						},
						failure : function(form, action) {
							Ext.Msg.show({
										title : MSG_FAILURE,
										msg : NOTIFY_ATTACHMENT + ':'
												+ action.result.msg,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.ERROR
									});
							mask.hide();
						}
					});
				} else {
					Ext.Msg.show({
								title : MSG_TITLE,
								msg : NOTIFY_SAME_ATTACHMENT,
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.INFO
							});
				}
			} else {
				Ext.Msg.show({
							title : MSG_TITLE,
							msg : NOTIFY_PLEASE_BROWSE,
							buttons : Ext.Msg.OK,
							icon : Ext.Msg.INFO
						});
			}
		} else {
			alert('Illegal uploadConfig');
		}
	},
	deleteFile : function(realNames, callbackAction) {
		var me = this;
		if (me.deleteConfig && me.deleteConfig.url
				&& me.deleteConfig.url.length) {
			var mask;
			if (me.deleteConfig.maskId && me.deleteConfig.maskId.length) {
				mask = new Ext.LoadMask(Ext.getCmp(me.deleteConfig.maskId), {
							msg : DEL_DELETING
						});
			} else {
				mask = new Ext.LoadMask(me, {
							msg : DEL_DELETING
						});
			}

			mask.show();
			var p = me.deleteConfig.params;
			p.realFileNamesStr = realNames.join(',');
			Ext.Ajax.request({
				url : me.deleteConfig.url,
				method : 'POST',
				timeout : 120000,
				params : p,
				success : function(form, action) {
					var removeNames = me.removeName(realNames);
					if (me.deleteConfig.callback
							&& typeof(me.deleteConfig.callback) == 'function') {
						me.deleteConfig.callback(removeNames);
					}
					if (callbackAction && typeof(callbackAction) == 'function') {
						callbackAction(removeNames);
					}
					mask.hide();
					me.checkIsShow();
				},
				failure : function(form, action) {
					var result = Ext.JSON.decode(form.responseText);
					Ext.Msg.show({
								title : MSG_FAILURE,
								msg : result.msg,
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.ERROR
							});
					mask.hide();
				}
			});
		} else {
			alert('Illegal deleteConfig');
		}
	},
	removeAll : function() {
		var me = this;
		me.down('[name=uploadfile]').reset();
		if (me.currentNames.length) {
			Ext.MessageBox.confirm('Confirm', BUTTON_DELETE + ' ' + LABEL_ALL
							+ '?', function(btn) {
						if (btn == 'yes') {
							var realNames = [];
							for (var i = 0; i < me.currentNames.length; i++) {
								realNames.push(me.currentNames[i].realName);
							}
							me.deleteFile(realNames);
							me.down('[name=displayField]').clearPanel();
						}
					});
		}
	},
	initComponent : function() {
		var me = this;
		me.oriNames = [];
		me.currentNames = [];
		if (!me.id || !me.id.length) {
			var myId = 'uploadField' + new Date().getTime();
			me.id = myId;
		}
		var disPlayPanel = Ext.create('Ext.panel.Panel', {
					xtype : 'panel',
					region : 'center',
					height : me.height ? me.height - 40 : 40,
					hidden : true,
					name : 'displayField',
					margin : '5 0 0 0',
					html : '<div class="displayField"></div>',
					bodyStyle : 'overflow-y : auto;',
					clearPanel : function() {
						var displayField = $('#' + me.id + ' .displayField');
						displayField.empty();
					},
					createSingleLabel : function(name) {// 创建标签
						var html = '<div class="uploadFile-label">';
						html += name.name;
						html += '</div>';
						var label = $(html);

						var removeIcon = $('<a class="uploadFile-removeIcon"></a>');
						removeIcon.click(function() {// 点击删除按钮事件
									Ext.MessageBox.confirm('Confirm',
											BUTTON_DELETE + '?', function(btn) {
												if (btn == 'yes') {
													me.deleteFile([label
															.data('realName')]);
													label.remove();
												}
											});
								});
						removeIcon.hover(function() {// 鼠标划过效果
									removeIcon.css({
												'background-position' : '-16px center'
											});
								}, function() {
									removeIcon.css({
												'background-position' : '0px center'
											});
								});
						label.append(removeIcon);
						label.hover(function() {// 鼠标划过效果
									removeIcon.show();
								}, function() {
									removeIcon.hide();
								});
						label.data(name);
						return label;
					},
					addAttachment : function(name) {// 添加
						var me = this;
						if (name) {
							var label = me.createSingleLabel(name);
							var displayField = $('#' + me.id + ' .displayField');
							displayField.append(label);
						}
					}
				});

		me.items = [{
					xtype : 'form',
					name : 'uploadForm',
					// bodyCls : 'x-border-layout-ct',
					border : false,
					layout : {
						type : 'hbox',
						align : 'middle'
					},
					region : 'north',
					defaults : {
						margin : '0 5 0 0'
					},
					items : [{
								name : 'uploadfile',
								xtype : 'filefield',
								flex : 1,
								buttonText : NOTIFY_BROWSE
							}, {
								xtype : 'button',
								text : BUTTON_UPLOAD,
								width : 50,
								handler : function() {
									me.saveFile();
								}
							}, {
								xtype : 'button',
								text : BUTTON_RESET,
								hidden : !me.showClearButton,
								margin : 0,
								width : 50,
								handler : function() {
									me.removeAll();
								}
							}]
				}, disPlayPanel];
		me.callParent();
	}
});