define(["./utils","./tabitem","./menuitem","./config"],function(o,k,r,p){var l=true,d,i=10,h=[],q=null,j=null,b={},c={getTabsRow:function(){return $("#tabs-row")},getTabsCnt:function(){return $("#tabs")},getIframeCnt:function(){return $("#tabs-iframe-cnt")},getLogo:function(){return $("#logo")}},g={id:"index",image:null,isShowDesktop:"0",isWebpage:"1",kind:"0",location:"/pages/globalview/leaderShipViewNew.jsp",name:"index",nameCn:"门户",order:0,pid:"",status:"1",childs:[]},n;k.setCallback("show",function(s){j=s});var a=function(s){var u=/^(http:\/\/)/i,t=/^(https:\/\/)/i;return u.exec(s)||t.exec(s)};var e=function(s){return a(s)?s:(eastcom.baseURL+s)};var m=function(){var u;for(u=0;u<h.length;u++){var s=h[u];if(s.destroyable()){s.destroy();break}}h.splice(u,1)};b.close=function(s){if(!s||!s.length){s=j._menu.id}$.each(h,function(v,u){if(u.matchId(s)){u.destroy();h.splice(v,1);return false}})};b.getCurrent=function(){return j};b.getTab=function(s){var t;$.each(h,function(v,u){if(u.matchId(s)){t=u;return false}});return t};b.add=function(t){var y=t.menu,x=t.forceRefresh,u=t.closable,s=false;y.location=e(y.location);if(y.location.indexOf("target=newWindow")!=-1){window.open(y.location);return}if(y.location.indexOf("target=self")!=-1){window.location.href=y.location;return}if(p.theme=="blue"&&p.showTabBar==true){if(t.menu.id=="index"){c.getTabsRow().addClass("hide")}else{c.getTabsRow().removeClass("hide")}}$("#fakeiframe").remove();var v=c.getTabsCnt();var w=c.getIframeCnt();if(l){$.each(h,function(B,A){if(A.matchId(y.id)){if(A.isDestroyed()){h.splice(B,1)}else{s=true;if(x){A.setLocation(y.location)}A.show();j=A}return false}});if(!s){if(h.length>=i){m()}var z=k.create(y,{previousTab:j,closable:u,destroyable:t.destroyable});z.render(v,w);z.show();j=z;h.push(z)}q=y}else{if(h[0]&&h[0].matchId(y.id)){if(x){h[0].setLocation(y.location);q=y}}else{v.empty();if(j){j.destroy()}var z=k.create(y);z.render(v,w);z.show();q=y;j=z;h=[z]}}};var f=function(){c.getLogo().on("click",function(){b.add({menu:d,destroyable:false})})};b.init=function(){i=p.tabMaxNum*1;if(p.showTabBar==true){c.getTabsRow().removeClass("hide")}f();g=$.extend(g,{location:p.defaultPageLocation,nameCn:p.defaultPageName});d=r.create(g);if(!(p.menu&&p.menu.length)){this.add({menu:d,destroyable:false})}};return b});