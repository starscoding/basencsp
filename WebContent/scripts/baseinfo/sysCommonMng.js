Ext.Loader.setConfig({enabled:true});Ext.Loader.setPath("Ext.ux",extDir+"/ux");Ext.Loader.setPath("Ext.components","../../static/commonjs/components");Ext.require(["Ext.form.*","Ext.data.*","Ext.grid.*","Ext.tree.*","Ext.ux.form.ValidatorTextField"]);eastcom.modules.sysCommonMng=(function(){var r="child";var b=false;var h=[];var n=[];var z=new Ext.util.HashMap();Ext.define("Common",{extend:"Ext.data.Model",fields:[{name:"id",type:"string"},{name:"name",type:"string"},{name:"label",type:"string"},{name:"value",type:"string"},{name:"order",type:"int"},{name:"attribute",type:"string"},{name:"desc",type:"string"},{name:"leaf",type:"boolean"},{name:"totalChildnum",type:"int"}]});var e=Ext.create("Ext.data.TreeStore",{model:"Common",proxy:{type:"ajax",actionMethods:"POST",url:eastcom.baseURL+"/sysmng/asynchronizeGetSysCommon",extraParams:{name:""}},sorters:{property:"order",direction:"ASC"},listeners:{load:function(){if(k&&k.getEl()){k.getEl().unmask()}}}});function v(H){var D=0;var G=e.getNodeById(H);if(G.hasChildNodes()){var E=G.childNodes.length;var F=G.getChildAt(E-1);var C=F.data.order;D=C+1}return D}var l=Ext.create("Ext.Action",{iconCls:"icon-add",hidden:eastcom.isPermitted("baseinfo","add"),text:BUTTON_NEW_CHILD,handler:function(F,E){var G=k.getSelectionModel().getSelection()[0];w.getComponent(0).getForm().reset();var C=0;if(G){var D=G.get("id");Ext.getCmp("pid").setValue(D);C=v(D)}else{C=v("root")}Ext.getCmp("order").setValue(C);r="child";Ext.getCmp("name").enable();w.setTitle(BUTTON_NEW);w.show()}});var y=Ext.create("Ext.Action",{iconCls:"icon-add",disabled:true,text:BUTTON_NEW_BROTHER,hidden:eastcom.isPermitted("baseinfo","add"),handler:function(G,F){var H=k.getSelectionModel().getSelection()[0];w.getComponent(0).getForm().reset();var D=0;if(H){var C=H.parentNode;if(C){if(!C.isRoot()){var E=C.get("id");Ext.getCmp("pid").setValue(E);D=v(E)}else{D=v("root")}}Ext.getCmp("order").setValue(D);r="brother";Ext.getCmp("name").enable();w.setTitle(BUTTON_NEW);w.show()}}});var d=Ext.create("Ext.Action",{iconCls:"icon-edit",disabled:true,text:BUTTON_EDIT,hidden:eastcom.isPermitted("baseinfo","update"),handler:function(G,F){var D=k.getView();var C=D.getSelectionModel().getLastSelected();if(C){var E=w.down("form").getForm();E.reset();E.loadRecord(C);Ext.getCmp("name").disable();w.setTitle(BUTTON_EDIT);w.show()}}});var g=Ext.create("Ext.Action",{iconCls:"icon-remove",disabled:true,text:BUTTON_DELETE,hidden:eastcom.isPermitted("baseinfo","delete"),handler:function(E,D){var F=k.getSelectionModel().getSelection()[0];var C=F.parentNode;if(F){Ext.MessageBox.confirm(MSG_SURE,DEL_BTN_INFO,function(G){if(G=="no"){return}var H=F.get("id");k.getEl().mask(MSG_DATA_OPTING);Ext.Ajax.request({url:eastcom.baseURL+"/sysmng/deleteSysCommon",timeout:60000,params:{id:H},success:function(J){var K=J.responseText;var I=Ext.JSON.decode(K);if(I.success=="true"){F.remove();if(C){if(!C.isRoot()&&!C.isExpandable()){C.set("leaf",true)}}Ext.Msg.show({title:MSG_SUCCESS,msg:I.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.INFO})}else{Ext.Msg.show({title:MSG_FAILURE,msg:I.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR})}k.getEl().unmask()},failure:function(){Ext.Msg.show({title:MSG_FAILURE,msg:rslt.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});k.getEl().unmask()}})})}}});var p=Ext.create("Ext.Action",{iconCls:"icon-order",disabled:false,text:BUTTON_CHANGE_ORDER,handler:function(J,H){if(b){k.getEl().mask(MSG_DATA_SAVING);var I="[";for(var G=0;G<h.length;G++){var F=h[G];var C="{";C+=('"id":"'+(F.data.id=="root"?"":F.data.id)+'"');if(F.childNodes.length>0){C+=',"childs":[';for(var D=0;D<F.childNodes.length;D++){var E=z.get(F.getChildAt(D).data.id);if(E){if(E=="root"){E=""}}else{E="none"}C+=('{"id":"'+F.getChildAt(D).data.id+'","order":"'+D+'","oldParentId":"'+E+'"},')}C=C.substr(0,C.length-1);C+="]"}else{C+=',"childs":[]'}C+="}";I+=(C+",")}if(I.length>1){I=I.substr(0,I.length-1)}I+="]";Ext.Ajax.request({url:eastcom.baseURL+"/sysmng/security/changeSysCommonParentAndOrder",timeout:60000,params:{jsonString:I},success:function(L){var K=Ext.JSON.decode(L.responseText);if(K.success=="true"){Ext.Msg.show({title:MSG_SUCCESS,msg:K.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});t()}else{Ext.Msg.show({title:MSG_FAILURE,msg:K.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR})}k.getEl().unmask()},failure:function(){Ext.Msg.show({title:MSG_FAILURE,msg:result.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});k.getEl().unmask()}})}else{c()}}});var u=Ext.create("Ext.Action",{iconCls:"icon-cancel",hidden:true,disabled:false,text:BUTTON_CANCEL_CHANGE_ORDER,handler:function(){k.getEl().mask(MSG_DATA_LOADING);e.load({callback:function(){a(k.getRootNode());t()}})}});var j=Ext.create("Ext.window.Window",{title:"导入",modal:true,height:110,width:400,layout:"fit",resizable:false,closeAction:"hide",items:[{bodyPadding:10,border:false,xtype:"form",items:[{xtype:"filefield",name:"file",fieldLabel:"文件名",labelWidth:45,msgTarget:"side",allowBlank:false,regex:/(xml)$/i,regexText:"请选择XML文件",anchor:"98%",buttonText:BUTTON_BROWSE}],buttons:[{text:BUTTON_OK,handler:function(){var E=this.up("form").getForm();if(E.isValid()){var F=k.getSelectionModel().getSelection()[0];var C=F?F.getId():"";var D=eastcom.baseURL+"/sysmng/importTree?rootId="+C;E.submit({url:D,waitMsg:"正在上传请稍后...",success:function(G,H){j.hide();if(H.result.success!=="true"){Ext.Msg.show({title:"执行结果",msg:H.result.data,buttons:Ext.Msg.OK,icon:Ext.Msg.INFO})}k.getEl().mask(MSG_DATA_LOADING);e.load({callback:function(){a(k.getRootNode());t()}})}})}}}]}]});var m=Ext.create("Ext.Action",{iconCls:"icon-import",hidden:false,disabled:false,text:"导 入",handler:function(){j.show()}});var B=Ext.create("Ext.Action",{iconCls:"icon-export",hidden:false,disabled:false,text:window.BUTTON_EXPORT,handler:function(){var E=k.getSelectionModel().getSelection()[0];var C=E?E.getId():"";var D=eastcom.baseURL+"/sysmng/exportTree?rootId="+C;window.open(encodeURI(D))}});var f=Ext.create("Ext.components.CommonSearchTrigger",{searchAction:function(D){var C=e.proxy;C.extraParams={name:D};k.getEl().mask(MSG_DATA_LOADING);e.load()}});function a(E){var F=E.childNodes;for(var D=0;D<F.length;D++){var C=F[D];if(x(C.data.id)){C.expand(false,function(){a(this)})}}}function x(E){var C=false;for(var D=0;D<n.length;D++){if(n[D]==E){C=true;break}}return C}function c(){u.show();l.disable();y.disable();d.disable();g.disable();p.setText(BUTTON_SAVE_CHANGE_ORDER);b=true}function t(){u.hide();l.enable();p.setText(BUTTON_CHANGE_ORDER);b=false;q()}function q(){h=[];n=[];z=new Ext.util.HashMap()}function o(I,G){var F={id:I.id,name:I.name,label:I.label,value:I.value,order:I.order,attribute:I.attribute,desc:I.desc,leaf:I.leaf,totalChildnum:I.totalChildnum};var D=k.getView();var H=D.getSelectionModel().getLastSelected();if(G==0){if(H){if(r=="child"){H.appendChild(F);if(H.isLeaf()){H.data.leaf=false}H.expand()}else{var C=H.parentNode;if(C){C.appendChild(F)}}}else{var E=k.getRootNode();E.appendChild(F)}}else{Ext.Object.each(F,function(J,L,K){H.set(J,L)})}}var A=Ext.create("Ext.menu.Menu",{items:[l,y,d,g,p]});function i(C){if(C=="0"){return'<span style="color:red;">'+MSG_NO+"</span>"}else{if(C=="1"){return'<span style="color:green;">'+MSG_YES+"</span>"}}return C}var k=Ext.create("Ext.tree.Panel",{region:"center",useArrows:true,fit:true,stateful:true,rootVisible:false,animate:false,store:e,autoHeight:true,enableColumnHide:eastcom.enableColumnHide,columns:[{xtype:"treecolumn",text:BASEINFO_LABEL,width:400,sortable:false,dataIndex:"label"},{text:BASEINFO_NAME,width:180,sortable:false,dataIndex:"name",align:"center"},{text:BASEINFO_VALUE,width:180,sortable:false,dataIndex:"value",align:"center"},{text:BASEINFO_ATTR,width:180,dataIndex:"attribute",sortable:false,align:"center"},{text:BASEINFO_DESC,flex:1,dataIndex:"desc",sortable:false,minWidth:200}],dockedItems:[{xtype:"toolbar",items:[l,"-",y,"-",d,"-",g,"-",p,u,"-",B,m,"->",f]}],viewConfig:{overflowY:"auto",overflowX:"hidden",stripeRows:true,listeners:{itemcontextmenu:function(C,G,E,D,F){F.stopEvent();A.showAt(F.getXY());return false}},plugins:{ptype:"treeviewdragdrop",containerScroll:true}},listeners:{afterlayout:function(){var C=this;if(C.store&&C.store.isLoading()){C.getEl().mask(MSG_DATA_LOADING)}},selectionchange:function(D,C){if(C.length){y.enable();d.enable();g.enable();p.enable()}else{y.disable();d.disable();g.disable();p.disable()}},select:function(F,C,D,E){if(C){}},itemmove:function(G,E,F,C,D){if(b==false){c()}h=h.distinctPush(F);h=h.distinctPush(E);if(E.data.id!=F.data.id){z.add(G.data.id,E.data.id)}},afteritemexpand:function(C){n=n.distinctPush(C.data.id)},afteritemcollapse:function(E){var C=0;for(var D=0;D<n.length;D++){if(E.data.id==n[D]){C=D;break}}n.splice(C,1)}}});var w=Ext.create("Ext.window.Window",{title:BUTTON_NEW,modal:true,layout:"fit",closeAction:"hide",resizable:false,width:350,items:[{xtype:"form",bodyPadding:5,border:false,defaultType:"textfield",defaults:{labelAlign:"right",labelWidth:90,width:300,msgTarget:"side"},items:[{xtype:"hiddenfield",id:"id",name:"id"},{xtype:"hiddenfield",id:"pid",name:"pid"},Ext.create("Ext.ux.form.ValidatorTextField",{labelAlign:"right",labelWidth:90,width:300,msgTarget:"side",fieldLabel:BASEINFO_NAME,name:"name",id:"name",allowBlank:false,controllerPath:eastcom.baseURL+"/sysmng/sysCommonNameCheck",errorMsg:MSG_NAME_EXSIST}),{fieldLabel:BASEINFO_LABEL,allowBlank:false,name:"label"},{fieldLabel:BASEINFO_VALUE,allowBlank:false,name:"value"},{fieldLabel:BASEINFO_ATTR,name:"attribute"},{xtype:"textareafield",fieldLabel:BASEINFO_DESC,name:"desc",height:120},{xtype:"hiddenfield",id:"order",name:"order",value:0,allowBlank:false}],buttons:[{text:BUTTON_OK,formBind:true,handler:function(){var D=this.up("form").getForm();var C=Ext.getCmp("id").getValue()==""?0:1;if(D.isValid()){var E=this.ownerCt.ownerCt.ownerCt;E.getEl().mask(MSG_DATA_SAVING);D.submit({clientValidation:true,url:eastcom.baseURL+"/sysmng/saveSysCommon?opt="+C,success:function(F,G){if(G.result.success=="true"){o(G.result.data,C);Ext.Msg.show({title:MSG_SUCCESS,msg:G.result.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.INFO})}else{Ext.Msg.show({title:MSG_FAILURE,msg:G.result.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR})}E.getEl().unmask();w.hide()},failure:function(F,G){switch(G.failureType){case Ext.form.action.Action.CLIENT_INVALID:Ext.Msg.alert("Failure","Form fields may not be submitted with invalid values");break;case Ext.form.action.Action.CONNECT_FAILURE:Ext.Msg.alert("Failure","Ajax communication failed");break;case Ext.form.action.Action.SERVER_INVALID:Ext.Msg.alert("Failure",G.result.msg)}Ext.Msg.show({title:MSG_FAILURE,msg:G.result.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});E.getEl().unmask();w.hide()}})}}},{text:BUTTON_CANCEL,handler:function(){this.up("form").getForm().reset();w.hide()}}]}]});function s(){Ext.create("Ext.Viewport",{layout:{type:"border",padding:5},items:[k]})}Array.prototype.distinctPush=function(G){var E=[],F=this,D=true;for(var C=0;C<F.length;C++){if(G!=F[C]){E.push(F[C])}else{D=false;E.push(G)}}if(D){E.push(G)}return E};return{initComponent:s}})();