(function(){var a={menuConfig:{idKey:"id",pIdKey:"pid",nameCn:"nameCn"},menuExpandDepth:0,floderStrong:false,specialMenu:[],menusData:[],isExistMenudata:function(d){var b=false;var c=a.specialMenu;jQuery.each(c,function(e,f){if(f.id==d){b=true;return false}});return b},showMenuPath:function(g,e,f){if(g!=undefined){var d=[];d=a.getMenuPath(g,[]);var c="";var b=d.length;jQuery.each(d,function(h,j){if(b==1||h==b-1){c+=j.name}else{c+='<a id="'+j.id+'" href="javascript:">'+j.name+"</a> > "}});e.html(c)}else{e.html("路径获取异常")}if(f){f()}},getMenuPath:function(d,c){var b=c;jQuery.each(a.specialMenu,function(e,f){if(f.id==d){b.unshift({id:f.id,name:f.nameCn});a.getMenuPath(f.pid,b);return false}});return b},readyMenuTree:function(d,s,f,o,h,l,b,p,e){var k={view:{showLine:true,strongFloder:this.floderStrong},data:{simpleData:{enable:true,idKey:this.menuConfig.idKey,pIdKey:this.menuConfig.pIdKey},key:{name:this.menuConfig.nameCn}},callback:{onClick:function(u,w,v,i){if(e&&v.status!=3){e(v)}}}};$.fn.zTree.init($("#"+d),k,s);var m=$.fn.zTree.getZTreeObj(d);if(o){var g=m.getNodesByParam("id",h,null);setTimeout(function(){m.expandNode(m.getNodeByTId($(g).attr("tId")),true,true,false)},1000)}if(l){var c=m.getNodesByParam(this.menuConfig.nameCn,b,null);m.selectNode(m.getNodeByTId($(c).attr("tId")))}if(a.menuExpandDepth>0){var n={};n.children=m.getNodes();var t=[n];var r=function(i){$.each(i.children,function(u,v){m.expandNode(v)})};for(var q=1;q<a.menuExpandDepth;q++){if(t&&t.length){var j=[];$.each(t,function(u,v){if(v.children&&v.children.length){r(v);j=j.concat(v.children)}});t=j}}}else{m.expandAll(true)}if(f){m.expandAll(true)}$("#tree_panel_"+d.substr(4)).data("treeObj",m).data("menuData",s)},getAllLowMenu:function(d,b){var c=a.getItemDataByPid(d);jQuery.each(c,function(e,f){if(f.status!="2"){b.push(f);if(f.isWebpage!="1"&&f.kind=="0"){a.getAllLowMenu(f.id,b)}}});return b},getItemDataByPid:function(d){var c=[];var b=a.menusData;jQuery.each(b,function(e,f){if(f.pid==d){c=f.items;return false}});return c},getMenuById:function(c){var b={};jQuery.each(a.specialMenu,function(d,e){if(e.id==c){b=e;return false}});return b},doAfterAnimation:function(e,c){var d=this;e.clearQueue();var b=e.data("isFlex");b=typeof(b)=="undefined"?false:b;if(b){setTimeout(function(){d.doAfterAnimation(e,c)},100)}else{c()}},showMenus:function(b,c,g){var f=$("#"+b),e=this;if(f.length){var d=function(){f.data("isFlex",true);f.css(c).slideDown("fast","swing",function(){f.data("isFlex",false);e.showMask(b);g()})};e.doAfterAnimation(f,d)}},hideMenuCb:function(){},hideMenu:function(e){var d=$("#"+e),c=this;if(d.length){var b=function(){d.data("isFlex",true);d.slideUp("fast","swing",function(){d.data("isFlex",false);c.hideMask();if(typeof c.hideMenuCb=="function"){c.hideMenuCb()}})};c.doAfterAnimation(d,b)}},showMask:function(f){var d=this,b=$("body").width(),c=$("body").height(),e=$("#naviMask");if(!e.length){e=$('<div id="naviMask"></div>');e.css({"z-index":100,position:"absolute",top:"0px",left:"0px"});$("#north_right").css({"z-index":101,position:"relative"});$("body").append(e)}e.click(function(){d.hideMenu.call(d,f)});$("#"+f).css({"z-index":101});e.width(b);e.height(c);e.show()},hideMask:function(){$("#naviMask").hide()},logUserVisiteMenuTimes:function(b){jQuery.ajax({type:"POST",async:true,dataType:"json",data:{menuId:b},url:Eastcom.baseURL+"/loginExtension/addUserMenuVisitTime",success:function(c){}})}};window.EastcomMenu=a})();