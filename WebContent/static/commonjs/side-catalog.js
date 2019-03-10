
	var f = function(){
		var e = $('.side-catalog .catalog-scroller');
		var n = $('.side-catalog .go-down');
		var i = ($('.side-catalog .toggle-button'),$('.side-catalog .go-up'));
		
		var o = e.scrollTop();//获得该元素相对滚动条顶部的偏移
		var t = e.find('.catalog-list').height();
		var l = e.height();
		
		0>=o?i.addClass('disable'):i.removeClass('disable');
		o>=t-l?n.addClass('disable'):n.removeClass('disable');
	};
	
	var scrollUp = function(o){
		var e = $('.side-catalog .catalog-scroller');
		var n = $('.side-catalog .go-down');
		
		var t = 19*o;
		var i = e.scrollTop();
				
		e.stop().animate({scrollTop:i-t},200,function(){
			f();
			n.removeClass('disable');
		});
	};
	
	var scrollDown = function(o){
		var e = $('.side-catalog .catalog-scroller');
		var n = $('.side-catalog .go-down');
		var i = i = ($('.side-catalog .toggle-button'),$('.side-catalog .go-up'));
		
		var t = 19*o;
		var l = e.scrollTop();
		
		e.stop().animate({scrollTop:l+t},200,function(){
			i.removeClass('disable');
			f();
		})
	};
	
	var toDisplay = function(){
		var o = $('.side-content');
		var c = $('.content-wrapper');
		var t = $('.side-catalog');
		
//		var i = 1200;
//		var n = o.offset().top + o.height();
//		var s = Math.max(n,i);
		var s = 200;
		var e = $(window).scrollTop();
		
		//判断何时显示导航条
		e>=s?t.css('visibility','visible'):t.css('visibility','hidden');
		
		var a = c.offset().top+c.height();
		var r = $(window).height()+e;
		
		if(r>a){
			var d = r-a+10;
			t.css("bottom",d+"px");
		}else{
			t.css("bottom","10px");
		}
	};
	
	var scrollToWhere = function(o){
		var a = $('.side-catalog .catalog-scroller').find('.catalog-list');
		var e = $('.side-catalog .catalog-scroller');
		var s = $('.side-catalog .arrow');
		var t = $('.side-catalog');
		var i = t.find('[href="#'+o+'"]');
		
		if(i.length>0){
			i = i.parents(".catalog-title");
			
			var l = i.offset().top-a.offset().top+5;
			
			if(i.offset().top !== a.offset().top){
				e.stop().animate({scrollTop:l-176},300,function(){f()});
				s.stop().animate({top:l},300);
			}
		}
	};
	
	
	var u = function(){
		for(var o = $(document.body).find('.lemma-ancho.para-title'),
			t = $(window).scrollTop(),i=10000,n=o.eq(0),s=0;s<o.length;s++){
				
			var e = o.eq(s);
			var a = Math.abs(e.offset().top-t);
			
			if(i>a){
				i=a;
				n=e;
			}
				
		}
		return n.attr('name');
	};
	
	var p = null;
	var g = function(){
		if(p){
			clearTimeout(p);
			p=null;
		}
		
		p = setTimeout(function(){
			var o = u();
			scrollToWhere(o);
		},50);
		toDisplay();
		
		
	};
	
	var h = function(){
		var e = $('.side-catalog .catalog-scroller');
		var c = $('.right-wrap');
		var t = $('.side-catalog');
		
        var o = e.find('.catalog-list').height();
        var i = e.height();

        //判断导航条 右侧的图标是否显示
//        if(0 >= o-i){
//            c.hide();
//        }
		0>=o-i&&c.hide();
        
        //绑定事件
        t.on('click','.toggle-button',function(){
        	t.hasClass('collapse')?t.removeClass('collapse'):t.addClass('collapse');
        })

        
        e.bind('mouseover',function(){
    		e.bind('mousewheel',//鼠标滚轮事件
				function(event,delta){//delta可以获得鼠标滚轮的方向和速度 值为负代表向下滚动  为正代表向上滚动
					
					var i = -delta;
					return 0>i?scrollUp(7):scrollDown(7),
						   event.stopPropagation(),//通过使用 stopPropagation() 方法只阻止一个事件起泡
						   false;//通过返回false来取消默认的行为并阻止事件起泡。
			})
        }).bind('mouseout',function(){e.unbind('mousewheel')});

        $(window).bind('scroll',g);//绑定页面滚动事件
        
        $(document).bind('ready',function(){
			g();
			toDisplay();
		});
		
		t.on('click','.go-up',function(){scrollUp(5);}),
		t.on('click','.go-down',function(){scrollDown(5);}),
		t.on('click','.gotop-button',function(){$(window).scrollTop(0);})
    };
	

    
setTimeout(function(){h()},2500);