define(["./menu","./toolbar","./tab","./foot","./config","registerEastcom","./utils"],function(h,f,e,d,b,c,a){b.init();h.init();e.init();f.init();d.init();c();if(a.isNotBlank(__CONFIG.leftMenu)){$("#content").removeClass("hide-left-tree");var g="./leftplugins/"+__CONFIG.leftMenu;require([g],function(i){i.init($("#content > .left-tree"))})}});