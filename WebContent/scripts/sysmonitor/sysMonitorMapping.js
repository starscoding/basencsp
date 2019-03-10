Ext.Loader.setConfig({enabled:true});Ext.Loader.setPath("Ext.ux",extDir+"/ux");Ext.Loader.setPath("Ext.components",eastcom.baseURL+"/static/commonjs/components");Ext.Loader.setPath("Ext.monitor","../../scripts/sysmonitor/components");Ext.require(["Ext.grid.*","Ext.data.*","Ext.ux.RowExpander","Ext.view.View","Ext.ux.DataView.DragSelector","Ext.ux.DataView.LabelEditor","Ext.components.BaseDataComboBox","Ext.components.DateTimeField","Ext.monitor.addMonitorMappingWin","Ext.monitor.monitorHistoryInfoWin"]);eastcom.modules.sysMonitorMapping=(function(){var i=new Date(new Date()-1*60*60*1000);var j=Ext.util.Format.date(new Date(),"Y-m-d")+" 00:00:00";var l=Ext.util.Format.date(new Date(),"Y-m-d H:i:s");var n=null;var c={};Ext.define("SysMonitorMain",{extend:"Ext.data.Model",fields:[{name:"id",type:"string"},{name:"serverId",type:"string"},{name:"serverName",type:"string"},{name:"indicatorId",type:"string"},{name:"indicatorName",type:"string"},{name:"instanceName",type:"string"},{name:"threshold1",type:"string"},{name:"threshold2",type:"string"},{name:"threshold3",type:"string"},{name:"threshold4",type:"string"},{name:"param1",type:"string"},{name:"param2",type:"string"},{name:"param3",type:"string"},{name:"remark",type:"string"}]});var g=Ext.create("Ext.data.Store",{id:"monitorMStore",model:"SysMonitorMain",pageSize:eastcom.pageSize,autoLoad:true,proxy:{type:"ajax",url:eastcom.baseURL+"/sysmonitor/queryMonitorMappingList",reader:{type:"json",idProperty:"oid",root:"data.elements",totalProperty:"data.total"},actionMethods:{read:"POST"},timeout:180000}});Ext.regModel("ServerModel",{fields:[{name:"id",type:"string"},{name:"name",type:"string"}]});Ext.regModel("IndicatorModel",{fields:[{name:"id",type:"string"},{name:"indicatorName",type:"string"}]});Ext.define("Ext.components.ServerComboBox",{extend:"Ext.form.field.ComboBox",xtype:"serverComboBox",blankText:MSG_FIELD_NOTNULL,typeAhead:true,editable:false,displayField:"name",valueField:"id",queryMode:"local",initComponent:function(){this.store=Ext.create("Ext.data.Store",{model:"ServerModel",proxy:{type:"ajax",actionMethods:{read:"POST"},url:eastcom.baseURL+"/sysmonitor/getAllMonitorServer"},autoLoad:true});this.callParent(arguments)}});Ext.define("Ext.components.IndicatorComboBox",{extend:"Ext.form.field.ComboBox",xtype:"indicatorComboBox",blankText:MSG_FIELD_NOTNULL,typeAhead:true,editable:false,displayField:"indicatorName",valueField:"id",queryMode:"local",initComponent:function(){this.store=Ext.create("Ext.data.Store",{model:"IndicatorModel",proxy:{type:"ajax",actionMethods:{read:"POST"},url:eastcom.baseURL+"/sysmonitor/getAllMonitorIndicator"},autoLoad:true});this.callParent(arguments)}});var h=Ext.create("Ext.Action",{icon:"../../static/images/common/icons/new.png",id:"addAction",text:BUTTON_ADD,handler:function(q,p){if(Ext.getCmp("addMonitorMappingWin")==null){Ext.create("Ext.monitor.addMonitorMappingWin",{baseCommonData:c}).show()}else{Ext.Msg.show({title:MSG_TITLE,msg:MONITOR_EDITWIN_EXSIST_MSG,buttons:Ext.Msg.OK,icon:Ext.Msg.INFO})}}});var b=Ext.create("Ext.Action",{icon:"../../static/images/common/icons/edit.png",text:BUTTON_EDIT,hidden:eastcom.isPermitted("sysNotificationmng","edit"),disabled:true,handler:function(s,r){var q=Ext.getCmp("sysMonitorMainGrid").getSelectionModel().getSelection();if(q.length>1){Ext.Msg.show({title:MSG_TITLE,msg:MONITOR_MUTIL_ERR_MSG,buttons:Ext.Msg.OK,icon:Ext.Msg.INFO})}else{if(Ext.getCmp("addMonitorMappingWin")==null){var p=Ext.create("Ext.monitor.addMonitorMappingWin",{baseCommonData:c});p.show(undefined,function(){p.loadServer(q[0])})}else{Ext.Msg.show({title:MSG_TITLE,msg:NOTIFY_CALCEL_OPT,buttons:Ext.Msg.YESNO,icon:Ext.Msg.QUESTION,fn:function(u){if(u=="yes"){Ext.getCmp("addMonitorMappingWin").close();var t=Ext.create("Ext.monitor.addMonitorMappingWin",{baseCommonData:c});t.show(undefined,function(){t.loadServer(q[0])})}}})}}}});var f=Ext.create("Ext.Action",{icon:"../../static/images/common/icons/delete.png",text:BUTTON_DELETE,disabled:true,handler:function(t,s){var p=Ext.getCmp("sysMonitorMainGrid").getSelectionModel().getSelection();var r=p[0].get("id");for(var q=1;q<p.length;q++){r=r+","+p[q].get("id")}Ext.Msg.show({title:MSG_SURE,msg:DEL_BTN_INFO,buttons:Ext.Msg.YESNO,icon:Ext.Msg.WARNING,fn:function(u){if(u=="yes"){Ext.Ajax.request({url:eastcom.baseURL+"/sysmonitor/delMonitorMapping",method:"POST",params:{ids:r},success:function(w,x){var v=Ext.JSON.decode(w.responseText);if(v.success=="true"){g.remove(p);Ext.Msg.show({title:MSG_SUCCESS,msg:v.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.INFO})}else{Ext.Msg.show({title:MSG_FAILURE,msg:v.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR})}},failure:function(w,x){var v=Ext.JSON.decode(w.responseText);Ext.Msg.show({title:MSG_FAILURE,msg:v.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR})}})}}})}});var a=Ext.create("Ext.selection.CheckboxModel",{});var d=Ext.create("Ext.grid.Panel",{region:"center",tbar:Ext.create("Ext.toolbar.Toolbar",{items:[h,"-",b,"-",f]}),id:"sysMonitorMainGrid",store:g,height:200,columnLines:true,selModel:a,columns:[{text:INSTANCE_NAME,flex:1,sortable:false,dataIndex:"instanceName",width:150,renderer:e},{text:MONITOR_NAME,width:180,dataIndex:"serverName",sortable:false,align:"center"},{text:INDICATOR_NAME,sortable:false,width:180,dataIndex:"indicatorName",align:"center"},{text:THRESHOLD_REF_1,width:100,dataIndex:"threshold1",sortable:false,align:"center"},{text:THRESHOLD_REF_2,sortable:false,width:100,dataIndex:"threshold2",align:"center"},{text:THRESHOLD_REF_3,width:100,dataIndex:"threshold3",sortable:false,align:"center"},{text:MONITOR_PARAM_1,width:150,dataIndex:"param1",sortable:false,align:"center"},{text:MONITOR_PARAM_2,width:150,dataIndex:"param2",sortable:false,align:"center"},{text:MONITOR_PARAM_3,width:150,dataIndex:"param3",sortable:false,align:"center"},{text:MONITOR_REMARK,width:150,dataIndex:"remark",sortable:false,align:"center"}],bbar:Ext.create("Ext.components.BaseCommonPagingToolbar",{store:g,listeners:{change:function(r,q){n=q}}}),animCollapse:false,listeners:{selectionchange:{fn:function(r,q,p){if(q.length>0&&q[0].get("id")&&q[0].get("id").length){if(b.isDisabled()){b.setDisabled(false)}if(f.isDisabled()){f.setDisabled(false)}}else{if(b.isDisabled()==false){b.setDisabled(true)}if(f.isDisabled()==false){f.setDisabled(true)}}}}},renderTo:Ext.getBody()});function m(){var p=g.proxy;var q={serverId:Ext.getCmp("query.serverId").getValue(),indicatorId:Ext.getCmp("query.indicatorId").getValue(),instanceName:Ext.getCmp("query.instanceName").getValue()};p.extraParams=q;g.loadPage(1,{start:0,limit:eastcom.pageSize,callback:function(){currentParam=q}})}function e(s,q,p,u){var t=p.get("serverId");var r=p.get("id");return'<a style="cursor: pointer;color:blue;text-decoration:underline;" onclick="monitorHistoryInfo(\''+t+"','"+r+"')\">"+s+"</a>"}function k(s,q){var r=Ext.getCmp("monitorHistoryInfoWin");if(r){r.close()}var p=Ext.create("Ext.monitor.monitorHistoryInfoWin");p.show(undefined,function(){p.load({id:s,mappingId:q,startTime:j,endTime:l})})}function o(){Ext.create("Ext.Viewport",{layout:{type:"border",padding:5},items:[{region:"north",padding:"0 0 5 0",xtype:"form",bodyPadding:10,items:[{xtype:"fieldcontainer",labelWidth:80,labelAlign:"right",layout:"hbox",items:[Ext.create("Ext.components.ServerComboBox",{id:"query.serverId",name:"serverId",fieldLabel:MONITOR_NAME,allFlag:false,labelAlign:"right",emptyText:MSG_CHOOSE_ONE,anchor:"95%",hidden:true,value:""}),Ext.create("Ext.components.IndicatorComboBox",{id:"query.indicatorId",name:"indicatorId",fieldLabel:INDICATOR_NAME,allFlag:false,labelAlign:"right",emptyText:MSG_CHOOSE_ONE,anchor:"95%",value:""}),{xtype:"textfield",id:"query.instanceName",name:"instanceName",fieldLabel:INSTANCE_NAME,editable:false,labelAlign:"right",anchor:"95%"},{xtype:"button",style:"margin-left:20px",formBind:true,iconCls:"icon-search",width:70,text:BUTTON_SEARCH,handler:m},{xtype:"button",style:"margin-left:20px",formBind:true,iconCls:"icon-refresh",width:70,text:BUTTON_RESET,handler:function(){Ext.getCmp("query.serverId").setValue("");Ext.getCmp("query.indicatorId").setValue("");Ext.getCmp("query.instanceName").setValue("")}}]}]},d]})}return{initComponent:o,initMonitorHistoryInfo:k}})();