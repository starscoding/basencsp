function updateContent(g){var f="";var e="";var c="";var a="";var d="";var b="";Ext.Ajax.request({url:eastcom.baseURL+"/sysmng/notification/queryNotificationWithRefInfo",method:"POST",params:{id:g},success:function(j,k){var i=Ext.JSON.decode(j.responseText).data;f=i.title;e=i.publishTime;c=i.content;if(c==null||c=="null"){c=""}a=i.file;d=i.fileReal;b=i.refStr;if(a==null){a="";d=""}Ext.getCmp("showNotificationDetailPanel").update(makeHTML(f,e,c,a,d,b));var h=document.getElementById("showNotificationDetailPanel").offsetHeight;document.getElementById("notice_details_content").style.height=h-document.getElementById("notice_details_head").offsetHeight-30+"px";document.getElementById("notice_details_content").innerHTML="<p1>"+c+"</p1>"},failure:function(h,i){}})}function makeHTML(k,f,g,j,h,d){var c="";var l=[];if(j.length>0){l=h.split(",");var a=j.split(",");for(var e in a){c=c+'<a class="attachment" onClick="downFile(\''+l[e]+"','"+a[e]+"')\">"+a[e]+"</a>,&nbsp"}}var b="";b+='<div class="notice_details" id="notice_details">';b+='<div class="notice_details_head" id="notice_details_head">';b+="<h2>"+k+"</h2>";b+="<p><font>"+NOTIFY_TARGETS+"：</font>"+d+"</p>";b+="<p><font>"+NOTIFY_ATTACHMENT+"：</font>"+c+"</p>";b+="<p><font>"+TIME+"：</font>"+f+"</p>";b+="</div>";b+='<div class="notice_details_content" id="notice_details_content">';b+="</div>";b+='<div class="notice_details_foot">';b+="</div>";b+="</div>";return b}function downFile(a,b){location.href=eastcom.baseURL+"/sysmng/notification/attachmentDownload?mngFileName="+encodeURI(encodeURI(b))+"&&mngFileRealName="+a}Ext.define("Ext.notify.showNotificationDetailPanel",{extend:"Ext.panel.Panel",requires:["Ext.form.*","Ext.window.*"],initComponent:function(){var a=this;a.frame=false;a.id="showNotificationDetailPanel";a.layout="fit";a.autoScroll=true;a.bodyStyle="overflow-y:hidden;overflow-x:hidden;";a.style={zindex:20000};a.html="";a.minWidth=370;a.listeners={afterlayout:function(){}};this.callParent()},updateContent:function(a){updateContent(a)}});