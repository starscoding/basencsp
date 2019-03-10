Ext.Loader.setConfig({enabled:true});Ext.Loader.setPath("Ext.app",eastcom.baseURL+"/scripts/portal/classes");Ext.Loader.setPath("Ext.ux",extDir+"/ux");Ext.Loader.setPath("Ext.portletfactory",eastcom.baseURL+"/scripts/portal/portletfactory");Ext.require(["Ext.layout.container.*","Ext.resizer.Splitter","Ext.fx.target.Element","Ext.fx.target.Component","Ext.window.Window","Ext.app.PortalColumn","Ext.app.userMainPortal","Ext.app.Portlet","Ext.app.PortalDropZone","Ext.app.PortalGroupTabPanel"]);Ext.define("Ext.app.Portal",{extend:"Ext.container.Viewport",addWin:null,changeLayoutWin:null,columnObjects:null,createGadgets:function(g,e,d){var a=g.gadgets.classUrl;var c=g.gadgets.className;var f;if(a&&c){var b=eastcom.baseURL+a;var i=new Number(g.height);var h=c.substr(0,c.lastIndexOf("."));Ext.Loader.setPath(h,b);f=Ext.create(c,{id:d?g.id:"",gadgetId:g.id,currentPortalId:e,closable:d,collapsible:d,showTools:d,columnNumber:g.columnNumber,positionSeq:g.positionSeq,refreshTime:new Number(g.refreshTime),title:g.gadgets.name,height:d?i+1-1:null,cls:d?"x-portlet":"x-portlet-full",frame:d});if(d==false){f.draggable=false}}return f},showAddItemWin:function(b){var a=this;if(a.addWin){if(b!=a.addWin.getPortalId()){a.addWin.setPortalId(b)}a.addWin.show()}else{a.addWin=new GadgetsWin(b);a.addWin.show()}},showChangeLayoutWin:function(b){var a=this;if(a.changeLayoutWin){if(b!=a.changeLayoutWin.getPortalId()){a.changeLayoutWin.setPortalId(b)}a.changeLayoutWin.show()}else{a.changeLayoutWin=new LayoutWin(b);a.changeLayoutWin.show()}},initComponent:function(){var b=this;Ext.getBody().mask(MSG_DATA_LOADING);var a={xtype:"portalgrouptabpanel",activeGroup:0,id:"tabGroup",findNodeById:function(e,h){var g=null;var c=this;if(e.childNodes.length){for(var d=0;d<e.childNodes.length;d++){var f=e.childNodes[d];if(h==f.data.gadgetId){g=f}else{g=c.findNodeById(f,h)}if(g){break}}}return g},removeById:function(f){var c=this,d=c.down("treepanel").getRootNode();var e=c.findNodeById(d,f);e.remove(true);if(b.addWin){console.log(123);b.addWin.setNeedReload()}},addPortal:function(f,g){if(f=="ok"){var e=this;var d=b.down("treepanel").getRootNode();var c=d.childNodes.length;Ext.Ajax.request({url:eastcom.baseURL+"/portal/addNewPortal",method:"POST",params:{order:c,name:g},success:function(j,l){var o=Ext.JSON.decode(j.responseText);if(o.success=="true"){var i=o.data;var m=e.prepareItems([{xtype:"panel",title:g,portalId:i,items:[{xtype:"panel",region:"north",border:false,margin:"0 9 0 8",layout:"fit",html:'<div class="poratlNavi"><button class="poratlButton" onClick="showAddItemWin(\''+i+"')\">"+PORT_ADD_GADGETS+'</button><button class="poratlButton" onClick="showChangeLayoutWin(\''+i+"')\">"+PORT_CHOOSE_MODEL+"</button></div>"},{id:i,portalId:i,portalIndex:c,region:"center",border:false,xtype:"usermainportal",region:"center"}],children:[]}])[0];var n={id:m.id,portalId:i,leaf:false,text:m.title,children:[]};delete m.title;var p=b.down("treepanel");var k=p.getRootNode();k.insertChild(k.childNodes.length-1,n);p.getSelectionModel().select(p.getTotalLength()-2);e.cards.push(m);var h={columnWidth:"50,50",columnNums:2,name:g,id:i,columnPortlets:[{columnNumber:0,confs:[],totalHeight:0},{columnNumber:1,confs:[],totalHeight:0}]};Ext.getCmp(i).loadPortal(h)}else{Ext.Msg.show({title:MSG_FAILURE,msg:o.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});Ext.getBody().mask()}},failure:function(h,i){Ext.Msg.show({title:MSG_FAILURE,msg:MSG_FAILURE,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});Ext.getBody().mask()}})}},newPortalAction:function(){Ext.MessageBox.prompt(BUTTON_NEW,NAME+":",function(c,d){Ext.getCmp("tabGroup").addPortal(c,d)})},removePortalAction:function(){var c=b.down("treepanel");var d=c.getSelectionModel().getSelection()[0];if(c.getRootNode().childNodes.length<=2){Ext.Msg.show({title:MSG_TITLE,msg:"每个用户必须至少保留一个Potral",buttons:Ext.Msg.OK,icon:Ext.Msg.INFO})}else{Ext.Msg.show({title:MSG_CONFIRM,msg:PORT_DEL_CONFIRM,buttons:Ext.Msg.YESNO,icon:Ext.Msg.QUESTION,fn:function(e){if(e=="yes"){Ext.Ajax.request({url:eastcom.baseURL+"/portal/removePortalAndConfs",method:"POST",params:{portalId:d.data.portalId},async:false,success:function(g,h){var f=Ext.JSON.decode(g.responseText);if(f.success=="true"){d.remove();c.getSelectionModel().select(0)}else{Ext.Msg.show({title:MSG_FAILURE,msg:f.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR})}},failure:function(f,g){Ext.Msg.show({title:MSG_ERROR,msg:MSG_ERROR_PROMPT,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR})}})}}})}},items:[]};Ext.apply(b,{id:"app-viewport",layout:{type:"fit",padding:"0 5 5 5"},items:[a],listeners:{afterrender:function(){Ext.each(b.columnObjects,function(c){Ext.getCmp(c.id).loadPortal(c)});$(".poratlButton").hover(function(){$(this).addClass("poratlButton-on")},function(){$(this).removeClass("poratlButton-on")})},afterlayout:function(){}}});Ext.Ajax.request({url:eastcom.baseURL+"/portal/loadUserPortal",method:"POST",params:{},async:false,success:function(d,e){var c=Ext.JSON.decode(d.responseText);if(c.success=="true"){b.columnObjects=c.data;Ext.each(c.data,function(f,m){var q=new Number(f.columnNums);var g=f.columnWidth.split(",");var o={items:[{title:f.name,id:"portal-"+f.id,portalId:f.id,xtype:"panel",border:false,layout:"border",items:[{xtype:"panel",region:"north",border:false,margin:"0 9 0 8",layout:"fit",html:'<div class="poratlNavi"><button class="poratlButton" onClick="showAddItemWin(\''+f.id+"')\">"+PORT_ADD_GADGETS+'</button><button class="poratlButton" onClick="showChangeLayoutWin(\''+f.id+"')\">"+PORT_CHOOSE_MODEL+"</button></div>"},{id:f.id,portalIndex:m,portalId:f.id,region:"center",border:false,xtype:"usermainportal",region:"center"}]}]};for(var k=0;k<q;k++){var p=f.columnPortlets[k].confs;for(var h=0;h<p.length;h++){var n=p[h];var l=b.createGadgets(n,f.id,false);o.items.push(l)}}a.items.push(o)})}else{Ext.Msg.show({title:MSG_FAILURE,msg:c.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR})}},failure:function(c,d){Ext.Msg.show({title:MSG_ERROR,msg:MSG_ERROR_PROMPT,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR})}});Ext.getBody().unmask();b.callParent()}});