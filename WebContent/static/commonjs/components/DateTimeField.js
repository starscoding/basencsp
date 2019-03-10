var currentLocale = eastcom.getCurrentLocale();// 获取当前语言语言
/**
 * @name 日期控件
 * @author JJF
 * @since JUN 09 2013
 * @description Ext整合my97时间控件
 * 
 * @param timeConfig(opt):my97时间空间配置项
 * @param startTimeField(opt):对应的开始时间控件配置。
 * @param endTimeField(opt):对应的结束时间控件配置。
 *            @example
 *            Ext.create('Ext.components.DateTimeField',{
 *												fieldLabel : LABEL_START_TIME,
 *												id : 'publishStartTime',
 *												endTimeField : {
 *													id : 'publishEndTime',//(必填)结束时间空间id
 *													overTime : '{M:0,d:-3}'//(可选)表示最晚日期为结束时间-3天
 *												},
 *												timeConfig : {},
 *												anchor : '100%',
 *												labelAlign : 'right',
 *												columnWidth : 1 / 3
 *											})
 *				Ext.create('Ext.components.DateTimeField',{
 *												id : 'publishEndTime',
 *												startTimeField : {
 *													id : 'publishStartTime',
 *													overTime : '{M:0,d:0}'
 *												},
 *												timeConfig : {
 *													maxDate : '%y-%M-%d'
 *												},
 *												fieldLabel : LABEL_END_TIME,
 *												labelAlign : 'right',
 *												anchor : '100%',
 *												columnWidth : 1 / 3
 *											})
 * 
 */
Ext.define('Ext.components.DateTimeField', {
			extend : 'Ext.form.field.Text',
			alias : 'widget.datetimefield',
			timeConfig : {},
			defaultTimeConfig : {// my97 default configs
				lang : currentLocale,
				dateFmt : 'yyyy-MM-dd HH:mm:ss',
				skin : 'twoer',
				readOnly : true
			},
			parseLang : function(lang) {
				if (lang == 'zh_CN') {
					return 'zh-cn';
				} else if (lang == 'en_US') {
					return 'en';
				}
			},
			readOnly : true,
			defaultLinsteners : {
				afterrender : function() {
					var me = this, c = Ext.apply({}, me.timeConfig,
							me.defaultTimeConfig), input = me.getEl().down(
							'input', true);

					c.el = input.id;

					if (me.startTimeField && me.startTimeField.id
							&& me.startTimeField.id.length) {
						var startInputId = me.startTimeField.id + '-inputEl';
						overTime = me.startTimeField.overTime
								? me.startTimeField.overTime
								: '{M:0,d:0}';
						if (c.minDate && c.minDate.length) {
							c.minDate = '#F{$dp.$D(\'' + startInputId + '\','
									+ overTime + ')' + '||\'' + c.minDate
									+ '\'}';
						} else {
							c.minDate = '#F{$dp.$D(\'' + startInputId + '\','
									+ overTime + ')}';
						}
					}

					if (me.endTimeField && me.endTimeField.id
							&& me.endTimeField.id.length) {
						var endInputId = me.endTimeField.id + '-inputEl';
						overTime = me.endTimeField.overTime
								? me.endTimeField.overTime
								: '{M:0,d:0}';
						if (c.maxDate && c.maxDate.length) {
							c.maxDate = '#F{$dp.$D(\'' + endInputId + '\','
									+ overTime + ')' + '||\'' + c.maxDate
									+ '\'}';
						} else {
							c.maxDate = '#F{$dp.$D(\'' + endInputId + '\','
									+ overTime + ')}';
						}
					}

					input.onfocus = function() {
						WdatePicker(c);
					};
					input.className = input.className + ' Wdate';
				}
			},
			initComponent : function() {
				var me = this;
				me.defaultTimeConfig.lang = me
						.parseLang(me.defaultTimeConfig.lang);
				me.listeners = Ext
						.apply({}, me.listeners, me.defaultLinsteners);
				if (!me.fieldStyle) {
					var theme = eastcom.getTheme();
					if (theme == 'access') {
						me.fieldStyle = 'background-color : #34383F;';
					} else {
						me.fieldStyle = 'background-color : #FFF;';
					}
				}
				this.callParent();
			}
		});