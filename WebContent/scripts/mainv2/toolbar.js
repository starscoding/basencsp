define(["./userinfo","./userslist"],function(e,a){var f={getBar:function(){return $("#toolbar")},getBtn:function(g){return $("#"+g+"-btn")},getFoot:function(){return $("#foot")}};var d={"go-top":function(){$("body,html").animate({scrollTop:0},200)},"user-info":function(){e.toggle()},"users-list":function(){a.toggle()},"user-guide":function(){location.href=eastcom.baseURL+"/userguide/download"}};var b={};var c=function(){var h=f.getBar(),i=f.getBtn("go-top"),g=f.getFoot().height();i.hide();$.each(d,function(j,k){f.getBtn(j).on("click",k)});$(window).scroll(function(o){var n=$(window).scrollTop(),j=$(document).height(),l=$(window).height(),m=h.height(),k=h.position().top;if(n>65){i.stop(true,true).fadeIn(500);if(j-(n+l)<g){h.css({bottom:g-(j-(n+l))})}else{h.css({bottom:0})}}else{i.stop(true,true).fadeOut(500)}})};b.init=function(){c()};return b});