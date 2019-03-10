Ext.define("Ext.projectConfig.BlockSetter",{extend:"Ext.button.Button",alias:"blocksetter",imgUrl:"",showText:"",textUpdate:true,textStyle:"color : #FFF;text-align : center;line-height : 24px;font-size : 16px;font-family:黑体,微软雅黑",getValue:function(){return{imgUrl:this.imgUrl,showText:this.showText}},loadConf:function(a){var b=this;b.updateImg(a.value);b.updateText(a.attribute)},updateImg:function(b){var a=this;a.getEl().setStyle({background:"url("+eastcom.baseURL+b+")"});a.imgUrl=b},updateText:function(c){var b=this;if(b.textUpdate){var a='<a style="'+b.textStyle+'">'+c+"</a>";b.setText(a);b.showText=c}},handler:function(){this.imgSel=this.imgSel||Ext.create("Ext.projectConfig.imgSelector",{textSelector:this.textUpdate,ownerCom:this});this.imgSel.show(null,this.imgSel.loadValue({img:this.imgUrl,text:this.showText}))},initComponent:function(){var a=this;if(a.imgUrl&&a.imgUrl.length){a.updateImg(a.imgUrl)}this.callParent()}});Ext.define("Ext.projectConfig.imgSelector",{extend:"Ext.window.Window",title:"请选择",ownerCom:null,textSelector:true,closeAction:"hide",currentValue:null,listeners:{show:function(){Ext.getBody().mask()},hide:function(){Ext.getBody().unmask()}},loadValue:function(b){var c=this;var a=c.down("[name=img]");var d=c.down("[name=text]");if(b){a.setValue(b.img);if(d){d.setValue(b.text)}}},buttons:[{text:"确定",handler:function(){var a=this.ownerCt.ownerCt;var c=a.down("[name=img]").getValue();var b="";if(a.down("[name=text]")){b=a.down("[name=text]").getValue()}if(a.ownerCom&&a.ownerCom.loadConf){a.ownerCom.loadConf({value:c,attribute:b})}a.hide()}},{text:"取消",handler:function(){this.ownerCt.ownerCt.hide()}}],initComponent:function(){var b=this;var a=[{xtype:"textfield",fieldLabel:"图片",labelWidth:30,labelAlign:"right",name:"img"}];if(b.textSelector){a.push({xtype:"textfield",fieldLabel:"文字",labelWidth:30,labelAlign:"right",name:"text"})}b.items=[{xtype:"form",bodyStyle:"padding : 10px",items:a}];b.callParent()}});