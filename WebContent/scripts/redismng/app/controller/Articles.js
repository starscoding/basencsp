Ext.define("FV.controller.Articles",{extend:"Ext.app.Controller",stores:["Articles"],models:["Article"],views:["article.Grid","article.Preview"],refs:[{ref:"feedShow",selector:"feedshow"},{ref:"viewer",selector:"viewer"},{ref:"articlePreview",selector:"articlepreview"},{ref:"articleGrid",selector:"articlegrid"},{ref:"articleTab",xtype:"articlepreview",closable:true,forceCreate:true,selector:"articlepreview"}],init:function(){this.control({articlegrid:{selectionchange:this.previewArticle},"articlegrid > tableview":{itemdblclick:this.loadArticle,refresh:this.selectArticle},"articlegrid button[action=openall]":{click:this.openAllArticles},"articlegrid button[action=loadcustom]":{click:this.loadCustom},"articlepreview button[action=viewintab]":{click:this.viewArticle},"articlepreview button[action=deletekey]":{click:this.deleteKey}})},selectArticle:function(a){var b=this.getArticlesStore().getAt(0);if(b){a.getSelectionModel().select(b)}},previewArticle:function(a,b){var d=b[0],c=this.getArticlePreview();if(d){c.article=d;c.update(d.data)}},deleteKey:function(c){var e=this,d=e.getArticlesStore(),a=e.getArticleGrid();var b=c.up("articlepreview").article.get("key");if(b){Ext.Ajax.request({url:eastcom.baseURL+"/redis/deleteKey",params:{key:b},success:function(g){var i=g.responseText;var f=Ext.JSON.decode(i);if(f&&f.success=="true"){var h=a.getSelectionModel().getSelection()[0];d.remove(h);e.selectArticle(a);e.showResult(f.msg)}}})}else{Ext.MessageBox.show({title:MSG_TITLE,msg:MSG_EMPTY_SELECTION,buttons:Ext.MessageBox.OK,icon:Ext.MessageBox.WARNING})}},loadCustom:function(){var d=this,a=d.getArticlesStore(),c=Ext.getCmp("custom_fuzzy_key").getValue(),b=Ext.getCmp("custom_fuzzy_type").getValue();if(c.length){a.load({params:{type:b,regexp:c},callback:function(f,e,g){}})}},openAllArticles:function(){this.loadArticles(this.getArticlesStore().getRange())},viewArticle:function(a){this.loadArticle(null,a.up("articlepreview").article)},loadArticles:function(b){var c=this.getViewer(),a=[],d;Ext.Array.forEach(b,function(e){d=e.id;if(!c.down("[articleId="+d+"]")){tab=this.getArticleTab();tab.down("button[action=viewintab]").destroy();tab.setTitle(e.get("key"));tab.article=e;tab.articleId=d;tab.update(e.data);a.push(tab)}},this);c.add(a)},loadArticle:function(a,c){var e=this.getViewer(),d=c.get("key"),b=c.id;tab=e.down("[articleId="+b+"]");if(!tab){tab=this.getArticleTab();tab.down("button[action=viewintab]").destroy()}tab.setTitle(d);tab.article=c;tab.articleId=b;tab.update(c.data);e.add(tab);e.setActiveTab(tab);return tab},showResult:function(a){Ext.example.msg(MSG_TITLE,"{0}",a)}});