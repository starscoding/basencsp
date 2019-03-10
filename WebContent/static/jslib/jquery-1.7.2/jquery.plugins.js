// -- 标签 --
(function($) {
	var scrollIncrement = 100;
	var scrollDuration = 400;
	var tabsContainer = null;
	var tabsLeftScroll = null;
	var tabsRightScroll = null;
	var panelsContainer = null;
	var secondTabsContainer = null;
	var ctxMenu = null;
	$.fn.tabs = function(options, param) {
		if (typeof options == 'string') {
			switch (options) {
				case 'add' :
					return addTab(param);
				case 'close' :
					return closeTab(param);
				case 'select' :
					return selectTab(param);
				case 'exists' :
					return exists(param);
				case 'selected' :
					return selected();
				case 'resize' :
					return resize();
				default :
					return;
			}
		}
		scrollIncrement = options.scrollIncrement || scrollIncrement;
		scrollDuration = options.scrollDuration || scrollDuration;
		tabsContainer = this;
		tabsLeftScroll = $('#' + options.tabsLeftScroll) || tabsLeftScroll;
		tabsRightScroll = $('#' + options.tabsRightScroll) || tabsRightScroll;
		panelsContainer = $('#' + options.panelsContainer) || panelsContainer;
		secondTabsContainer = $('#' + options.secondTabsContainer)
				|| panelsContainer;
		// 标签栏宽度变化
		tabsContainer.bind('_resize', function() {
			var tabsId = tabsContainer.attr('id');
			var scrollWidth = document.getElementById(tabsId).scrollWidth;
			$('#' + tabsId).attr('scrollWidth', scrollWidth)
			if (tabsContainer.outerWidth() < tabsContainer.attr('scrollWidth')) {
				tabsLeftScroll.show();
				tabsRightScroll.show();
			} else {
				tabsLeftScroll.hide();
				tabsRightScroll.hide();
			}
		});
		// tabsContainer.bind('contextmenu', function() {
		// return false;
		// });
		// 标签左右滚动事件
		tabsLeftScroll.hover(function() {
					$(this).addClass('active');
				}, function() {
					$(this).removeClass('active');
				});
		tabsRightScroll.hover(function() {
					$(this).addClass('active');
				}, function() {
					$(this).removeClass('active');
				});
		tabsLeftScroll.bind('click', function() {
					var scrollTo = tabsContainer.scrollLeft() - scrollIncrement;
					if (scrollTo < scrollIncrement)// 如果不够一个tab宽度，则滚动到头部
						scrollTo = 0;
					tabsContainer.animate({
								scrollLeft : scrollTo
							}, scrollDuration);
				});
		tabsRightScroll.bind('click', function() {
					var scrollTo = tabsContainer.scrollLeft() + scrollIncrement;
					if (scrollTo + scrollIncrement > tabsContainer
							.attr('scrollWidth'))
						scrollTo = tabsContainer.attr('scrollWidth');
					tabsContainer.animate({
								scrollLeft : scrollTo
							}, scrollDuration);
				});
		// 标签事件
		$('div > a.tab', tabsContainer).live('click', function() {
					selectTab($(this).attr('index'));
				});
		$('div > a.tab', tabsContainer).live('mousedown', function(event) {
			if ($.browser.msie && event.button == 4 || !$.browser.msie
					&& event.button == 1)
				if ($(this).attr('closable') == "true")
					closeTab($(this).attr('index'));
		});
		$('div > a.tab', tabsContainer).live('dblclick', function() {
					if ($(this).attr('closable') == "true")
						closeTab($(this).attr('index'));
				});
		$('div > a.close', tabsContainer).live('click', function() {
					closeTab($(this).attr('index'));
				});
		$('div > a.tab', tabsContainer).live('contextmenu', function(event) {
			var t = $(this);
			selectTab(t.attr('index'));
			var checkCloseOthersBtnShow = function() {
				var f = false;
				var tabs = $('div > a.tab', tabsContainer);
				for (var i = 0; i < tabs.length; i++) {
					var tt = $(tabs[i]);
					if (tt.attr('index') != t.attr('index')
							&& tt.attr('closable') == "true") {
						f = true;
						break;
					}
				}
				return f;
			};
			var initCtxM = function() {
				ctxMenu = new ContextMenu([{
							name : '关闭当前页',
							iconCls : 'cxt_close_icon cxt_icon',
							fn : function() {
								var d = ctxMenu.getEventDom();
								if (d.attr('closable') == "true") {
									closeTab(d.attr('index'));
								}
							}
						}, {
							name : '关闭所有其他页',
							iconCls : 'cxt_closeall_icon cxt_icon',
							fn : function() {
								var tabs = $('div > a.tab', tabsContainer);
								var d = ctxMenu.getEventDom();
								for (var i = 0; i < tabs.length; i++) {
									var tt = $(tabs[i]);
									if (tt.attr('index') != d.attr('index')
											&& tt.attr('closable') == "true") {
										closeTab(tt.attr('index'));
									}
								}
							}
						}]);
			};
			var showMenu = function() {
				ctxMenu.setEventDom(t);
				var f0 = t.attr('closable') == "true", f1 = checkCloseOthersBtnShow(), hideMenus = [];
				if (!f0) {
					hideMenus.push(0);
				}
				if (!f1) {
					hideMenus.push(1);
				}
				ctxMenu.setMenuItemsAvaliable(null, true);
				ctxMenu.setMenuItemsAvaliable(hideMenus, false);
				ctxMenu.show(event.pageX, event.pageY);
			};
			if (ctxMenu) {
				showMenu();
			} else {
				initCtxM();
				showMenu();
			}
			return false;
		});
	};
	function resize() {
		tabsContainer.triggerHandler('_resize');
	}
	function addTab(param) {
		if (!param.id)
			return;
		if (exists(param.id)) {
			selectTab(param.id);
			return;
		}
		var html = '<div id="tabs_' + param.id
				+ '" class="unselected"><a href="javascript:;" id="tabs_link_'
				+ param.id + '" class="tab" index="' + param.id
				+ '" closable="' + param.closable + '" hidefocus="hidefocus">'
				+ param.title + '</a>';
		if (param.closable)
			html += '<a href="javascript:;" class="close" index="' + param.id
					+ '" hidefocus="hidefocus"></a>';
		html += '</div>';
		$(html).appendTo(tabsContainer);
		$('<div id="tabs_' + param.id + '_panel" class="tabs-panel" style="'
				+ param.style + '">' + param.content + '</div>')
				.appendTo(panelsContainer);
		tabsContainer.triggerHandler('_resize');
		if (param.selected)
			selectTab(param.id);
	}
	function closeTab(id) {
		var iframe = window.frames['tabs_' + id + '_iframe'];
		if (iframe && typeof(iframe.onclose) == 'function')
			if (!iframe.onclose())
				return;
		var nextTab = $('#tabs_' + id, tabsContainer).next();
		$('#tabs_' + id, tabsContainer).remove();
		$('#tabs_' + id + '_panel', panelsContainer).remove();
		$('#second_tabs_' + id, secondTabsContainer).remove();
		if (!nextTab.is('div'))
			nextTab = $('div', tabsContainer).last();
		var nextId = nextTab.attr('id');
		tabsContainer.triggerHandler('_resize');
		tabsContainer.triggerHandler('_close');
		if (nextId) {
			selectTab(nextId.substr(5));
		}
	}
	function selectTab(id) {
		if (!exists(id))
			return;
		$('div', tabsContainer).removeClass('selected');
		$('div', tabsContainer).addClass('unselected');
		$('.tabs-panel', panelsContainer).removeClass('selected');
		$('#tabs_' + id, tabsContainer).addClass('selected');
		$('#tabs_' + id, tabsContainer).removeClass('unselected');
		$('#tabs_' + id + '_panel', panelsContainer).addClass('selected');
		var iframe = window.frames['tabs_' + id + '_iframe'];
		if (iframe && typeof(iframe.onactive) == 'function')
			iframe.onactive();
		scrollTabVisible(id);
	}
	function exists(id) {
		return $('#tabs_' + id, tabsContainer).length > 0;
	}
	function selected() {
		return $('div.selected:first', tabsContainer).attr('id').substring(5);
	}
	function scrollTabVisible(id) {
		var tabsOffsetLeft = $('#tabs_' + id, tabsContainer).offset().left;
		var tabsWidth = $('#tabs_' + id, tabsContainer).outerWidth();
		var containerOffsetLeft = tabsContainer.offset().left;
		var containerWidth = tabsContainer.outerWidth();
		var containerScrollLeft = tabsContainer.scrollLeft();
		if (tabsOffsetLeft > containerOffsetLeft
				&& tabsOffsetLeft + tabsWidth > containerOffsetLeft
						+ containerWidth) // 要选中的标签的左侧可见，右侧不可见
		{
			var scrollTo = (tabsOffsetLeft + tabsWidth)
					- (containerOffsetLeft + containerWidth)
					+ containerScrollLeft;
			tabsContainer.animate({
						scrollLeft : scrollTo
					}, scrollDuration);
		} else if (tabsOffsetLeft < containerOffsetLeft) // 要选中的标签的右侧可见，左侧不可见
		{
			var scrollTo = containerScrollLeft
					- (containerOffsetLeft - tabsOffsetLeft);
			tabsContainer.animate({
						scrollLeft : scrollTo
					}, scrollDuration);
		}
		$('div.second-tabs-container:visible', secondTabsContainer).hide();
		$('#second_tabs_' + id).show();
		var wWidth = (window.innerWidth || (window.document.documentElement.clientWidth || window.document.body.clientWidth));
		var maxWidth = wWidth - secondTabsContainer.next().outerWidth();
		var secondTabs = $('#second_tabs_' + id);
		var secondTabsWidth = secondTabs.outerWidth();
		var left = tabsOffsetLeft + Math.floor(tabsWidth / 2)
				- Math.floor(secondTabsWidth / 2);
		left = Math.min(left, maxWidth - secondTabsWidth);
		left = Math.max(0, left);
		secondTabs.animate({
					left : left
				}, scrollDuration);
	}
})(jQuery);

// -- 滚动条 --
(function($) {
	/**
	 * 
	 * Copyright (c) 2009 Jun(qq100015091) http://www.xlabi.com
	 * http://www.xlabi.com/tp/jscroll.html jun5091@gmail.com
	 */
	/*--------------------------------------------------------------------------------------------------*/
	$.fn.extend({// 添加滚轮事件//by jun
		mousewheel : function(Func) {
			return this.each(function() {
						var _self = this;
						_self.D = 0;// 滚动方向
						if ($.browser.msie || $.browser.safari) {
							_self.onmousewheel = function() {
								_self.D = event.wheelDelta;
								event.returnValue = false;
								Func && Func.call(_self);
							};
						} else {
							_self.addEventListener("DOMMouseScroll",
									function(e) {
										_self.D = e.detail > 0 ? -1 : 1;
										e.preventDefault();
										Func && Func.call(_self);
									}, false);
						}
					});
		}
	});
	$.fn.extend({
		jscroll : function(j) {
			return this.each(function() {
				j = j || {}
				j.Bar = j.Bar || {};// 2级对象
				j.Btn = j.Btn || {};// 2级对象
				j.Bar.Bg = j.Bar.Bg || {};// 3级对象
				j.Bar.Bd = j.Bar.Bd || {};// 3级对象
				j.Btn.uBg = j.Btn.uBg || {};// 3级对象
				j.Btn.dBg = j.Btn.dBg || {};// 3级对象
				var jun = {
					W : "15px",
					BgUrl : "",
					Bg : "#efefef",
					Bar : {
						Pos : "up",
						Bd : {
							Out : "#b5b5b5",
							Hover : "#ccc"
						},
						Bg : {
							Out : "#fff",
							Hover : "#fff",
							Focus : "orange"
						}
					},
					Btn : {
						btn : true,
						uBg : {
							Out : "#ccc",
							Hover : "#fff",
							Focus : "orange"
						},
						dBg : {
							Out : "#ccc",
							Hover : "#fff",
							Focus : "orange"
						}
					},
					Fn : function() {
					}
				}
				j.W = j.W || jun.W;
				j.BgUrl = j.BgUrl || jun.BgUrl;
				j.Bg = j.Bg || jun.Bg;
				j.Bar.Pos = j.Bar.Pos || jun.Bar.Pos;
				j.Bar.Bd.Out = j.Bar.Bd.Out || jun.Bar.Bd.Out;
				j.Bar.Bd.Hover = j.Bar.Bd.Hover || jun.Bar.Bd.Hover;
				j.Bar.Bg.Out = j.Bar.Bg.Out || jun.Bar.Bg.Out;
				j.Bar.Bg.Hover = j.Bar.Bg.Hover || jun.Bar.Bg.Hover;
				j.Bar.Bg.Focus = j.Bar.Bg.Focus || jun.Bar.Bg.Focus;
				j.Btn.btn = j.Btn.btn != undefined ? j.Btn.btn : jun.Btn.btn;
				j.Btn.uBg.Out = j.Btn.uBg.Out || jun.Btn.uBg.Out;
				j.Btn.uBg.Hover = j.Btn.uBg.Hover || jun.Btn.uBg.Hover;
				j.Btn.uBg.Focus = j.Btn.uBg.Focus || jun.Btn.uBg.Focus;
				j.Btn.dBg.Out = j.Btn.dBg.Out || jun.Btn.dBg.Out;
				j.Btn.dBg.Hover = j.Btn.dBg.Hover || jun.Btn.dBg.Hover;
				j.Btn.dBg.Focus = j.Btn.dBg.Focus || jun.Btn.dBg.Focus;
				j.Fn = j.Fn || jun.Fn;
				var _self = this;
				var Stime, Sp = 0, Isup = 0;
				$(_self).css({
							overflow : "hidden",
							position : "relative",
							padding : "0px"
						});
				var dw = $(_self).width(), dh = $(_self).height() - 1;
				var sw = j.W ? parseInt(j.W) : 21;
				var sl = dw - sw
				var bw = j.Btn.btn == true ? sw : 0;
				if ($(_self).children(".jscroll-c").height() == null) {// 存在性检测
					$(_self)
							.wrapInner("<div class='jscroll-c' style='top:0px;z-index:9999;zoom:1;position:relative'></div>");
					$(_self)
							.children(".jscroll-c")
							.prepend("<div style='height:0px;overflow:hidden'></div>");
					$(_self)
							.append("<div class='jscroll-e' unselectable='on' style=' height:100%;top:0px;right:0;-moz-user-select:none;position:absolute;overflow:hidden;z-index:10000;'><div class='jscroll-u' style='position:absolute;top:0px;width:100%;left:0;background:blue;overflow:hidden'></div><div class='jscroll-h'  unselectable='on' style='background:green;position:absolute;left:0;-moz-user-select:none;'></div><div class='jscroll-d' style='position:absolute;bottom:0px;width:100%;left:0;overflow:hidden;'></div></div>");
				}
				var jscrollc = $(_self).children(".jscroll-c");
				var jscrolle = $(_self).children(".jscroll-e");
				var jscrollh = jscrolle.children(".jscroll-h");
				var jscrollu = jscrolle.children(".jscroll-u");
				var jscrolld = jscrolle.children(".jscroll-d");
				if ($.browser.msie) {
					document.execCommand("BackgroundImageCache", false, true);
				}
				jscrollc.css({
							"padding-right" : sw
						});
				jscrolle.css({
							width : sw,
							background : j.Bg,
							"background-image" : j.BgUrl
						});
				jscrollh.css({
							top : bw,
							background : j.Bar.Bg.Out,
							"background-image" : j.BgUrl,
							"border-color" : j.Bar.Bd.Out,
							width : sw - 2
						});
				jscrollu.css({
							height : bw,
							background : j.Btn.uBg.Out,
							"background-image" : j.BgUrl
						});
				jscrolld.css({
							height : bw,
							background : j.Btn.dBg.Out,
							"background-image" : j.BgUrl
						});
				jscrollh.hover(function() {
							if (Isup == 0)
								$(this).css({
											background : j.Bar.Bg.Hover,
											"background-image" : j.BgUrl,
											"border-color" : j.Bar.Bd.Hover
										})
						}, function() {
							if (Isup == 0)
								$(this).css({
											background : j.Bar.Bg.Out,
											"background-image" : j.BgUrl,
											"border-color" : j.Bar.Bd.Out
										})
						})
				jscrollu.hover(function() {
							if (Isup == 0)
								$(this).css({
											background : j.Btn.uBg.Hover,
											"background-image" : j.BgUrl
										})
						}, function() {
							if (Isup == 0)
								$(this).css({
											background : j.Btn.uBg.Out,
											"background-image" : j.BgUrl
										})
						})
				jscrolld.hover(function() {
							if (Isup == 0)
								$(this).css({
											background : j.Btn.dBg.Hover,
											"background-image" : j.BgUrl
										})
						}, function() {
							if (Isup == 0)
								$(this).css({
											background : j.Btn.dBg.Out,
											"background-image" : j.BgUrl
										})
						})
				var sch = jscrollc.height();
				// var sh = Math.pow(dh,2) / sch ;//Math.pow(x,y)x的y次方
				// var sh = (dh-2*bw)*dh / sch
				var sh = 30;
				if (sh < 10) {
					sh = 10
				}
				var wh = sh / 6// 滚动时候跳动幅度
				// sh = parseInt(sh);
				var curT = 0, allowS = false;
				jscrollh.height(sh);
				if (sch <= dh) {
					jscrollc.css({
								padding : 0
							});
					jscrolle.css({
								display : "none"
							})
				} else {
					allowS = true;
				}
				if (j.Bar.Pos != "up") {
					curT = dh - sh - bw;
					setT();
				}
				jscrollh.bind("mousedown", function(e) {
							j['Fn'] && j['Fn'].call(_self);
							Isup = 1;
							jscrollh.css({
										background : j.Bar.Bg.Focus,
										"background-image" : j.BgUrl
									})
							var pageY = e.pageY, t = parseInt($(this)
									.css("top"));
							$(document).mousemove(function(e2) {
										curT = t + e2.pageY - pageY;// pageY浏览器可视区域鼠标位置，screenY屏幕可视区域鼠标位置
										setT();
									});
							$(document).mouseup(function() {
										Isup = 0;
										jscrollh.css({
													background : j.Bar.Bg.Out,
													"background-image" : j.BgUrl,
													"border-color" : j.Bar.Bd.Out
												})
										$(document).unbind();
									});
							return false;
						});
				jscrollu.bind("mousedown", function(e) {
							j['Fn'] && j['Fn'].call(_self);
							Isup = 1;
							jscrollu.css({
										background : j.Btn.uBg.Focus,
										"background-image" : j.BgUrl
									})
							_self.timeSetT("u");
							$(document).mouseup(function() {
										Isup = 0;
										jscrollu.css({
													background : j.Btn.uBg.Out,
													"background-image" : j.BgUrl
												})
										$(document).unbind();
										clearTimeout(Stime);
										Sp = 0;
									});
							return false;
						});
				jscrolld.bind("mousedown", function(e) {
							j['Fn'] && j['Fn'].call(_self);
							Isup = 1;
							jscrolld.css({
										background : j.Btn.dBg.Focus,
										"background-image" : j.BgUrl
									})
							_self.timeSetT("d");
							$(document).mouseup(function() {
										Isup = 0;
										jscrolld.css({
													background : j.Btn.dBg.Out,
													"background-image" : j.BgUrl
												})
										$(document).unbind();
										clearTimeout(Stime);
										Sp = 0;
									});
							return false;
						});
				_self.timeSetT = function(d) {
					var self = this;
					if (d == "u") {
						curT -= wh;
					} else {
						curT += wh;
					}
					setT();
					Sp += 2;
					var t = 500 - Sp * 50;
					if (t <= 0) {
						t = 0
					};
					Stime = setTimeout(function() {
								self.timeSetT(d);
							}, t);
				}
				jscrolle.bind("mousedown", function(e) {
							j['Fn'] && j['Fn'].call(_self);
							curT = curT + e.pageY - jscrollh.offset().top - sh
									/ 2;
							asetT();
							return false;
						});
				function asetT() {
					if (curT < bw) {
						curT = bw;
					}
					if (curT > dh - sh - bw) {
						curT = dh - sh - bw;
					}
					jscrollh.stop().animate({
								top : curT
							}, 100);
					var scT = -((curT - bw) * sch / (dh - 2 * bw));
					jscrollc.stop().animate({
								top : scT
							}, 1000);
				};
				function setT() {
					if (curT < bw) {
						curT = bw;
					}
					if (curT > dh - sh - bw) {
						curT = dh - sh - bw;
					}
					jscrollh.css({
								top : curT
							});
					var scT = -((curT - bw) * sch / (dh - 2 * bw));
					jscrollc.css({
								top : scT
							});
				};
				$(_self).mousewheel(function() {
							if (allowS != true)
								return;
							j['Fn'] && j['Fn'].call(_self);
							if (this.D > 0) {
								curT -= wh;
							} else {
								curT += wh;
							};
							setT();
						})
			});
		}
	});
})(jQuery);