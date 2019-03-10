Ext.Loader.setConfig({enabled:true});Ext.Loader.setPath("Ext.ux",extDir+"/ux");Ext.Loader.setPath("Ext.components","../../static/commonjs/components");var required='<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';var currentRecord=null;function resetData(){currentRecord=null}Ext.define("SysMonitorTarget",{extend:"Ext.data.Model",fields:[{name:"id",type:"string"},{name:"type",type:"string"},{name:"subType",type:"string"},{name:"operator",type:"string"},{name:"thresholdRef1",type:"string"},{name:"thresholdRef2",type:"string"},{name:"thresholdRef3",type:"string"},{name:"thresholdRef4",type:"string"},{name:"remark",type:"string"}]});function WarnLevelImage1(a){return'<img alt="" class="icon" src="../../static/images/themes/blue/notification/warnMsg.png" title="警告">'}Ext.define("Ext.monitor.addIndicatorWin",{extend:"Ext.window.Window",requires:["Ext.form.*","Ext.window.*","Ext.components.BaseDataComboBox","Ext.ux.RowExpander"],baseCommonData:null,title:BUTTON_NEW,id:"addIndicatorWin",frame:false,layout:"fit",border:false,resizable:false,autoScroll:true,width:610,height:currentUserTheme=="neptune"?345:325,listeners:{show:function(){Ext.getBody().mask()},close:function(){Ext.getBody().unmask()}},initAddTargetWinComponents:function(){var d;var c;var b;var a;c=Ext.create("Ext.components.BaseDataComboBox",{parentName:"indicatorType",id:"form.type",name:"type",fieldLabel:MONITOR_INDICATOR_TYPE,allowBlank:false,allFlag:false,afterLabelTextTpl:required,emptyText:MSG_CHOOSE_ONE,labelAlign:"right",anchor:"95%",listeners:{select:function(e,j,f,h){var i=e.lastSelection[0].data.value;var g=Ext.getCmp("form.subType").getStore().proxy;Ext.getCmp("form.subType").setValue("");if(i=="0"){var k={type:"indicatorType0",allFlag:false};g.extraParams=k;Ext.getCmp("form.subType").getStore().load()}else{if(i=="1"){var k={type:"indicatorType1",allFlag:false};g.extraParams=k;Ext.getCmp("form.subType").getStore().load()}else{if(i=="2"){var k={type:"indicatorType2",allFlag:false};g.extraParams=k;Ext.getCmp("form.subType").getStore().load()}else{if(i=="3"){var k={type:"indicatorType3",allFlag:false};g.extraParams=k;Ext.getCmp("form.subType").getStore().load()}else{if(i=="4"){var k={type:"indicatorType4",allFlag:false};g.extraParams=k;Ext.getCmp("form.subType").getStore().load()}else{if(i=="5"){var k={type:"indicatorType5",allFlag:false};g.extraParams=k;Ext.getCmp("form.subType").getStore().load()}else{if(i=="6"){var k={type:"indicatorType6",allFlag:false};g.extraParams=k;Ext.getCmp("form.subType").getStore().load()}}}}}}}}}});b=Ext.create("Ext.components.BaseDataComboBox",{parentName:"",id:"form.subType",name:"subType",fieldLabel:MONITOR_INDICATOR_SUBTYPE,allowBlank:false,allFlag:false,labelAlign:"right",afterLabelTextTpl:required,emptyText:MSG_CHOOSE_ONE,anchor:"95%",listeners:{load:function(){}}});a=Ext.create("Ext.components.BaseDataComboBox",{parentName:"operator",id:"form.operator",name:"operator",fieldLabel:MONITOR_OPERATOR,allowBlank:false,allFlag:false,labelAlign:"right",afterLabelTextTpl:required,emptyText:MSG_CHOOSE_ONE,anchor:"95%"});d=Ext.widget({xtype:"form",id:"addMainForm",region:"center",bodyStyle:{padding:"5px 5px 0px","overflow-x":"hidden","overflow-y":"auto"},items:[{xtype:"container",width:"100%",layout:"vbox",items:[{xtype:"container",width:"100%",flex:1,layout:"hbox",items:[{xtype:"fieldcontainer",flex:1,width:"100%",layout:"vbox",defaults:{width:"100%"},items:[{xtype:"textfield",id:"form.id",fieldLabel:THRESHOLD_REF_1,labelAlign:"right",hidden:true,anchor:"95%"},c,b,a]},{xtype:"container",flex:1,layout:"vbox",defaults:{width:"100%"},items:[{xtype:"textfield",id:"form.thresholdRef1",fieldLabel:THRESHOLD_REF_1,labelAlign:"right",anchor:"95%"},{xtype:"textfield",id:"form.thresholdRef2",fieldLabel:THRESHOLD_REF_2,labelAlign:"right",anchor:"95%"},{xtype:"textfield",id:"form.thresholdRef3",fieldLabel:THRESHOLD_REF_3,labelAlign:"right",anchor:"95%"},{xtype:"textfield",id:"form.editFlag",labelAlign:"right",allowBlank:false,hidden:true,value:"0",anchor:"95%"}]}]},{xtype:"fieldcontainer",flex:1,layout:"hbox",width:"100%",defaults:{width:"100%"},items:[{xtype:"textarea",id:"form.remark",fieldLabel:MONITOR_REMARK,labelAlign:"right",allowBlank:true,anchor:"95%"}]},{xtype:"fieldcontainer",flex:1,layout:"hbox",width:"100%",defaults:{width:"100%"},items:[{xtype:"label",id:"form.example",labelAlign:"center",allowBlank:false,margin:"0 0 0 60",text:"告警阀值填写说明：从一级到三级从大到小填写，一级为最严重告警；CPU使用率，内存使用率，磁盘使用率---小数，单位（%）;网络流量 --- 接收;发送 ，数字，单位（KB/S）;IO负载 --- 数字;系统负载均衡 --- 1分钟负载;5分钟负载;15分钟负载（数字）",anchor:"95%"}]}]}]});return d},loadServer:function(a){var c=this;resetData();currentRecord=a;c.setTitle(BUTTON_EDIT);var b=c.baseCommonData;Ext.Ajax.request({url:eastcom.baseURL+"/sysmonitor/queryMonitorIndicator",method:"POST",params:{id:a.get("id")},success:function(e,f){Ext.getCmp("form.editFlag").setValue("1");Ext.getCmp("form.type").setReadOnly(true);Ext.getCmp("form.subType").setReadOnly(true);var d=Ext.JSON.decode(e.responseText).data;var g={id:d.id,type:d.type,subType:d.subType,operator:d.operator,thresholdRef1:d.thresholdRef1,thresholdRef2:d.thresholdRef2,thresholdRef3:d.thresholdRef3,remark:d.remark};Ext.getCmp("form.id").setValue(g.id);Ext.getCmp("form.type").setValue(g.type);Ext.getCmp("form.subType").setValue(g.subType);Ext.getCmp("form.operator").setValue(g.operator);Ext.getCmp("form.thresholdRef1").setValue(g.thresholdRef1);Ext.getCmp("form.thresholdRef2").setValue(g.thresholdRef2);Ext.getCmp("form.thresholdRef3").setValue(g.thresholdRef3);Ext.getCmp("form.remark").setValue(g.remark)},failure:function(d,e){}})},initComponent:function(){var a=this;a.items=[a.initAddTargetWinComponents()];a.buttons=[{text:BUTTON_OK,handler:function(){var b=Ext.getCmp("addMainForm").getForm();if(b.isValid()==true){a.getEl().mask(MSG_DATA_SAVING);Ext.Ajax.request({url:eastcom.baseURL+"/sysmonitor/saveMonitorIndicator",method:"POST",params:{type:Ext.getCmp("form.type").getValue(),subType:Ext.getCmp("form.subType").getValue(),operator:Ext.getCmp("form.operator").getValue(),thresholdRef1:Ext.getCmp("form.thresholdRef1").getValue(),thresholdRef2:Ext.getCmp("form.thresholdRef2").getValue(),thresholdRef3:Ext.getCmp("form.thresholdRef3").getValue(),remark:Ext.getCmp("form.remark").getValue(),editFlag:Ext.getCmp("form.editFlag").getValue(),id:Ext.getCmp("form.id").getValue()},success:function(e,f){var c=Ext.JSON.decode(e.responseText);if(c.success=="true"){var g={type:Ext.getCmp("form.type").getRawValue(),subType:Ext.getCmp("form.subType").getRawValue(),operator:Ext.getCmp("form.operator").getRawValue(),thresholdRef1:c.data.thresholdRef1,thresholdRef2:c.data.thresholdRef2,thresholdRef3:c.data.thresholdRef3,remark:c.data.remark,id:c.data.id};if(currentRecord){currentRecord.set("type",Ext.getCmp("form.type").getRawValue());currentRecord.set("subType",Ext.getCmp("form.subType").getRawValue());currentRecord.set("operator",Ext.getCmp("form.operator").getRawValue());currentRecord.set("thresholdRef1",g.thresholdRef1);currentRecord.set("thresholdRef2",g.thresholdRef2);currentRecord.set("thresholdRef3",g.thresholdRef3);currentRecord.set("remark",g.remark);currentRecord.set("id",g.id)}else{var d=Ext.create("SysMonitorTarget",g);Ext.getCmp("sysMonitorTargetGrid").getStore().insert(0,d)}Ext.Msg.show({title:MSG_SUCCESS,msg:c.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.INFO})}else{Ext.Msg.show({title:MSG_FAILURE,msg:c.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR})}a.getEl().unmask();a.close()},failure:function(d,e){var c=Ext.JSON.decode(d.responseText);Ext.Msg.show({title:MSG_FAILURE,msg:c.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR})}})}else{Ext.Msg.show({title:MSG_TITLE,msg:MSG_ALL_CONDITIONS,buttons:Ext.Msg.OK,icon:Ext.Msg.INFO})}}},{text:BUTTON_CANCEL,handler:function(){Ext.getCmp("addIndicatorWin").close()}}];this.callParent()}});