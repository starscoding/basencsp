Ext.Loader.setConfig({enabled:true});Ext.Loader.setPath("Ext.portletfactory",eastcom.baseURL+"/scripts/portal/portletfactory");Ext.define("Ext.gadgets.gadgetsDemo2",{extend:"Ext.portletfactory.PortalItem",reload:function(){var a=this;a.getEl().mask("正在加载数据，请稍后...");setTimeout(function(){a.getEl().unmask()},2000)},initComponent:function(){var a=this;var b=Ext.create("Ext.panel.Panel",{border:false,html:"我是demo2!"});a.items=[b];this.callParent()}});