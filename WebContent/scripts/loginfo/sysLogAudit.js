Ext.Loader.setConfig({enabled:true});Ext.Loader.setPath("Ext.ux",extDir+"/ux");Ext.Loader.setPath("Ext.components",eastcom.baseURL+"/static/commonjs/components");Ext.Loader.setPath("Ext.monitor","../../scripts/sysmonitor/components");Ext.require(["Ext.grid.*","Ext.data.*","Ext.ux.RowExpander","Ext.view.View","Ext.ux.DataView.DragSelector","Ext.ux.DataView.LabelEditor","Ext.components.BaseDataComboBox","Ext.components.DateTimeField","Ext.monitor.addServerWin","Ext.monitor.resourceConfDetailWin"]);eastcom.modules.sysLogAudit=(function(){var a=null;var f={};Ext.define("SysLogAuditModel",{extend:"Ext.data.Model",fields:[{name:"id",type:"string"},{name:"account",type:"string"},{name:"times",type:"string"},{name:"threshold",type:"string"},{name:"startTime",type:"string"},{name:"endTime",type:"string"},{name:"recordTime",type:"string"},{name:"type",type:"string"},{name:"msg",type:"string"}]});var g=Ext.create("Ext.data.Store",{id:"sysLogAuditStore",model:"SysLogAuditModel",pageSize:eastcom.pageSize,autoLoad:true,proxy:{type:"ajax",url:eastcom.baseURL+"/log/qrySysLogAudit",reader:{type:"json",idProperty:"oid",root:"data.elements",totalProperty:"data.total"},actionMethods:{read:"POST"},timeout:180000}});var e=Ext.create("Ext.Action",{icon:"../../static/images/common/icons/delete.png",text:BUTTON_DELETE,disabled:true,handler:function(n,m){var j=Ext.getCmp("sysLogAuditGrid").getSelectionModel().getSelection();var l=j[0].get("id");for(var k=1;k<j.length;k++){l=l+","+j[k].get("id")}Ext.Msg.show({title:MSG_SURE,msg:DEL_BTN_INFO,buttons:Ext.Msg.YESNO,icon:Ext.Msg.WARNING,fn:function(i){if(i=="yes"){Ext.Ajax.request({url:eastcom.baseURL+"/log/delSysLogAudit",method:"POST",params:{ids:l},success:function(p,q){var o=Ext.JSON.decode(p.responseText);if(o.success=="true"){g.remove(j);Ext.Msg.show({title:MSG_SUCCESS,msg:o.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.INFO})}else{Ext.Msg.show({title:MSG_FAILURE,msg:o.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR})}},failure:function(p,q){var o=Ext.JSON.decode(p.responseText);Ext.Msg.show({title:MSG_FAILURE,msg:o.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR})}})}}})}});var h=Ext.create("Ext.selection.CheckboxModel",{});var c=Ext.create("Ext.grid.Panel",{region:"center",tbar:Ext.create("Ext.toolbar.Toolbar",{items:[e]}),id:"sysLogAuditGrid",store:g,columnLines:true,selModel:h,columns:[{text:LOG_AUDIT_ACCOUNT,flex:1,sortable:false,dataIndex:"id",hidden:true},{text:LOG_AUDIT_ACCOUNT,flex:1,sortable:false,dataIndex:"account",width:"10%",align:"center"},{text:LOG_AUDIT_TIMES,flex:1,sortable:true,dataIndex:"times",width:"10%",align:"center"},{text:LOG_AUDIT_THRESHOLD,flex:1,sortable:false,dataIndex:"threshold",width:"10%",align:"center"},{text:LOG_AUDIT_STARTTIME,flex:1,sortable:true,dataIndex:"startTime",align:"center",width:"10%"},{text:LOG_AUDIT_ENDTIME,flex:1,sortable:true,dataIndex:"endTime",align:"center",width:"10%"},{text:LOG_AUDIT_RECORDTIME,flex:1,sortable:true,dataIndex:"recordTime",align:"center",width:"10%"},{text:LOG_AUDIT_TYPE,width:"10%",dataIndex:"type",sortable:false,align:"center"},{text:LOG_AUDIT_MSG,width:"30%",dataIndex:"msg",sortable:false,align:"center"}],bbar:Ext.create("Ext.components.BaseCommonPagingToolbar",{store:g,listeners:{change:function(j,i){a=i}}}),animCollapse:false,listeners:{selectionchange:{fn:function(k,j,i){if(j.length>0&&j[0].get("id")&&j[0].get("id").length){if(e.isDisabled()){e.setDisabled(false)}}else{if(e.isDisabled()==false){e.setDisabled(true)}}}}},renderTo:Ext.getBody()});function d(){var i=g.proxy;var j={account:Ext.getCmp("query.account").getValue(),type:Ext.getCmp("query.type").getValue(),startTime:Ext.getCmp("query.startTime").getValue(),endTime:Ext.getCmp("query.endTime").getValue()};i.extraParams=j;g.loadPage(1,{start:0,limit:eastcom.pageSize,callback:function(){currentParam=j}})}function b(){Ext.create("Ext.Viewport",{layout:{type:"border",padding:5},items:[{region:"north",padding:"0 0 5 0",xtype:"form",bodyPadding:10,items:[{xtype:"fieldcontainer",labelWidth:80,labelAlign:"right",layout:"hbox",items:[{xtype:"textfield",id:"query.account",name:"account",fieldLabel:LOG_AUDIT_ACCOUNT,editable:false,labelAlign:"right",anchor:"95%"},Ext.create("Ext.components.BaseDataComboBox",{parentName:"sysLogAuditType",id:"query.type",name:"type",fieldLabel:LOG_AUDIT_TYPE,allFlag:false,labelAlign:"right",emptyText:MSG_CHOOSE_ONE,anchor:"95%",value:""}),{xtype:"datetimefield",allowBlank:true,fieldLabel:"审计时间",labelAlign:"right",endTimeField:{id:"query.endTime"},name:"startTime",id:"query.startTime",timeConfig:{maxDate:new Date().format("yyyy-MM-dd 00:00:00"),dateFmt:"yyyy-MM-dd 00:00:00",}},{xtype:"label",text:"-",margins:"0 10 0 10"},{xtype:"datetimefield",allowBlank:true,labelAlign:"right",name:"endTime",id:"query.endTime",startTimeField:{id:"query.startTime"},timeConfig:{dateFmt:"yyyy-MM-dd 23:59:59",maxDate:new Date().format("yyyy-MM-dd 23:59:59"),}},{xtype:"button",style:"margin-left:20px",formBind:true,iconCls:"icon-search",width:70,text:BUTTON_SEARCH,handler:d},{xtype:"button",style:"margin-left:20px",formBind:true,iconCls:"icon-refresh",width:70,text:BUTTON_RESET,handler:function(){Ext.getCmp("query.account").setValue("");Ext.getCmp("query.type").setValue("");Ext.getCmp("query.startTime").setValue("");Ext.getCmp("query.endTime").setValue("")}}]}]},c]})}return{initComponent:b}})();