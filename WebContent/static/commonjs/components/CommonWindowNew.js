/**
 * html公共窗口
 * 
 * @author JJF
 * @since OCT 17 2013
 */
var CommonWindow = function(conf) {
	var thisWin = null;// 当前窗口jquery对象
	var currentCss = null;// 动画效果之前位置和大小,用于还原
	var minCss = null;// 最小化时css
	var isAnimating = false;// 是否正在进行动画效果
	var isShow = false;// 是否已经显示
	var originWinPosition = {};// 原始窗口位置
	var originMousePosition = {};// 原始鼠标位置
	var onDrag = false;// 是否正在被拖动

	var createUUID = (function(uuidRegEx, uuidReplacer) {
		return function() {
			return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(uuidRegEx,
					uuidReplacer).toUpperCase();
		};
	})(/[xy]/g, function(c) {
				var r = Math.random() * 16 | 0, v = c == "x" ? r : (r & 3 | 8);
				return v.toString(16);
			});
	var defaultConf = {// 默认参数
		id : 'CommonWindow-' + createUUID(),
		title : '',
		height : 200,
		width : 300,
		flex : true,
		animationPosition : null,
		animationTarget : null,
		dragable : true,
		closable : true,
		module : false,
		modal : false,
		style : {},
		position : {},
		buttons : [],
		titleAlign : 'left',
		buttonAlign : 'right',
		closeAction : 'destroy',
		splitLine : true,
		content : '内容html显示区域'
	};
	var config = null;// 最终参数
	var init = function() {
		config = conf ? $.extend(defaultConf, conf) : defaultConf;// 整合参数，若用户未指定，则使用默认参数

		var mainDomId = config.id;// id
		var titleId = config.id + '-title';// 当前window的titleId
		var overlayImgId = config.id + '-overlayImg';// 外层边框id
		var contentContainerId = config.id + '-contentContainer';// 外层边框id
		var btnContainerId = config.id + '-btnContainer';// 按钮容器id

		// 定义参数
		var moduleHeight = 24 / 250 * config.height;
		var moduleWidth = 22 / 300 * config.width;

		var overlayHeight = config.height + moduleHeight;
		var overlayWidth = config.width + moduleWidth;

		var windowTop = ($(window).height() - config.height) / 2;
		var windowLeft = ($(window).width() - config.width) / 2;

		var titleHeight = 30;

		// dom定义
		var windowMaindom = $('<div id="' + mainDomId
				+ '" class="eastcom_commonWindow"></div>');

		currentCss = {
			height : config.height - 10,
			width : config.width - 10,
			left : config.position.left || windowLeft,
			top : config.position.top || windowTop
		};

		initMinCss();

		if (config.flex) {
			windowMaindom.css(minCss);
		} else {
			windowMaindom.css(currentCss);
		}

		windowMaindom.css(config.style);
		if (config.module) {
			windowMaindom.css({
						'z-index' : 99999
					});
		}

		var overlayImg = $('<img class="overlay" id="' + overlayImgId
				+ '" src="' + eastcom.baseURL + '/static/images/themes/'
				+ eastcom.getTheme() + '/commonWindow/transparent1.png">');
		overlayImg.css({
					height : overlayHeight,
					width : overlayWidth,
					top : -(moduleHeight / 2) + 'px',
					left : -(moduleWidth / 2) + 'px',
					display : config.flex ? 'none' : 'block',
					'-webkit-user-select' : 'none',
					'-moz-user-select' : 'none',
					'-ms-user-select' : 'none',
					'user-select' : 'none'
				});

		var title = $('<div id="' + titleId
				+ '" class="title" style="text-align:' + config.titleAlign
				+ ';"><a class="titleLabel"></a></div>');
		title.find('.titleLabel').append(config.title);
		title.css({
					height : titleHeight - 5,
					display : config.flex ? 'none' : 'block'
				});
		if (!conf.splitLine) {
			title.css({
						border : 'none'
					});
		}
		if (config.closable) {
			var closeIcon = $('<a class="close"></a>');
			title.append(closeIcon);
		}
		var contentContainer = $('<div id="' + contentContainerId
				+ '" class="contentContainer"></div>');
		contentContainer.append($(config.content));
		contentContainer.css({
					width : config.width - 10,
					height : config.height - titleHeight - 10
							- (config.buttons.length ? 30 : 0),
					display : config.flex ? 'none' : 'block'
				});

		windowMaindom.append(title);
		windowMaindom.append(contentContainer);
		windowMaindom.append(overlayImg);

		if (config.buttons.length) {
			var btnContainer = $('<div class="btnContainer" id="'
					+ btnContainerId + '"></div>');
			$.each(config.buttons, function(i, btn) {
						var b = $('<button class="btn">' + btn.text
								+ '</button>');
						b.click(function() {
									if (typeof(btn.handler) == 'function') {
										btn.handler();
									}
								});
						if (btn.style) {
							b.css(style);
						}
						btnContainer.append(b);
					});
			btnContainer.css({
						'text-align' : config.buttonAlign,
						'display' : config.flex ? 'none' : 'block'
					});
			windowMaindom.append(btnContainer);
		}

		var body = $('body');
		body.append(windowMaindom);

		// 遮罩
		var fm = $('<div id="commonWinFakeMask"></div>');
		var b = $('body');
		fm.css({
					'z-index' : 9999,
					position : 'absolute',
					top : 0,
					left : 0,
					width : $(window).width(),
					height : $(window).height()
				});
		if (config.modal) {
			fm.css({
						'background' : '#ccc',
						'opacity' : '0.6'
					});
		} else {
			fm.css({
						'display' : 'none'
					});
		}
		body.append(fm);
		
		
		thisWin = windowMaindom;
		bindEvents();
		eastcom.commonWindowNum += 1;// 序列自增
	};

	var initMinCss = function() {
		minCss = {
			top : document.body.clientHeight / 2,
			left : document.body.clientWidth / 2,
			width : 0,
			height : 0
		}

		if (config.animationPosition) {
			minCss.top = config.animationPosition.top;
			minCss.left = config.animationPosition.left;
			config.flex = true;
		} else {
			if (config.animationTarget) {
				minCss.top = config.animationTarget.position().top;
				minCss.left = config.animationTarget.position().left;
				minCss.width = config.animationTarget.width();
				minCss.height = config.animationTarget.height();
				config.flex = true;
			}
		}
	};

	var bindEvents = function() {
		if (config.closable) {
			// 关闭按钮
			$('#' + config.id + ' a.close').bind('click', function() {
						if (config.closeAction == 'destroy') {
							close();
						} else if (config.closeAction == 'hide') {
							hide();
						}
					});
		}
		if (config.dragable) {
			// 拖动事件
			var titleBar = $('#' + config.id + ' div.title');
			titleBar.mousedown(function(event) {
						var fm = $('#commonWinFakeMask');
						fm.show();
						//thisWin.fadeTo(20, 0.7);
						originPosition = thisWin.offset();
						originMousePosition = {
							top : event.pageY,
							left : event.pageX
						}
						titleBar.css({
									'cursor' : 'move'
								});
						onDrag = true;
					});
			$('body').mousemove(function(event) {
				if (onDrag) {
					thisWin.css({
								top : originPosition.top
										+ (event.pageY - originMousePosition.top),
								left : originPosition.left
										+ (event.pageX - originMousePosition.left)
							});
				}
			});
			titleBar.mouseup(function(event) {
						if (!config.modal) {
							$('#commonWinFakeMask').hide();
						}
						onDrag = false;
						//thisWin.fadeTo(20, 1);
						currentCss.top = thisWin.position().top;
						currentCss.left = thisWin.position().left;
						titleBar.css({
									'cursor' : 'auto'
								});
					});
		}
		var btns = getBtnContainer().find('button');
		btns.live('mouseenter', function() {
					$(this).addClass('btn-over');
				});
		btns.live('mouseleave', function() {
					$(this).removeClass('btn-over');
					$(this).removeClass('btn-pressed');
				});
		btns.live('mousedown', function() {
					$(this).addClass('btn-pressed');
				});
		btns.live('mouseup', function() {
					$(this).removeClass('btn-pressed');
				});
	};

	/**
	 * 更改显示内容
	 * 
	 * @param {}
	 *            content 新内容
	 * @param {}
	 *            animate 动画方向，默认无动画
	 */
	function updateContent(content, animate) {
		var container = getBody();
		container.empty();
		container.append(content);
	}

	/** 获取主体 */
	function getBody() {
		var container = $('#' + config.id + '-contentContainer');
		return container;
	};

	function getTitle() {
		var container = $('#' + config.id + '-title');
		return container;
	};

	function getOverlayImg() {
		return $('#' + config.id + '-overlayImg')
	};

	function getBtnContainer() {
		var container = $('#' + config.id + '-btnContainer');
		return container;
	};

	/** 设置window标题 */
	function setTitle(title) {
		if (title && title.length) {
			var t = getTitle().find('a.titleLabel');
			t.empty();
			t.append(title);
		}
	}

	/** 设置位置 */
	function setPosition(pos) {
		currentCss.left = pos.left;
		currentCss.top = pos.top;
		thisWin.css({
					left : pos.left,
					top : pos.top
				});
	}

	var setVisible = function(flag, callback) {
		if (config.flex && !isAnimating) {
			if (flag) {
				if (!isShow) {
					isAnimating = true;
					thisWin.show();
					getOverlayImg().show();
					initMinCss();
					thisWin.css(minCss);
					thisWin.animate(currentCss, 500, function() {
								isShow = true;
								isAnimating = false;
								getBody().show();
								getTitle().show();
								getBtnContainer().show();
								if (typeof(callback) == 'function') {
									callback();
								}
							});
				} else {
					if (typeof(callback) == 'function') {
						callback();
					}
				}
			} else {
				if (isShow) {
					isAnimating = true;
					getBody().hide();
					getTitle().hide();
					getBtnContainer().hide();
					initMinCss();
					thisWin.animate(minCss, 200, function() {
								isShow = false;
								isAnimating = false;
								thisWin.hide();
								getOverlayImg().hide();
								if (typeof(callback) == 'function') {
									callback();
								}
							});
				} else {
					if (typeof(callback) == 'function') {
						callback();
					}
				}
			}
		} else {
			if (flag) {
				thisWin.show(0, function() {
							if (typeof(callback) == 'function') {
								callback();
							}
						});
			} else {
				thisWin.hide(0, function() {
							if (typeof(callback) == 'function') {
								callback();
							}
						});
			}
		}
	};

	/** 显示窗口 */
	function show(callback) {
		var cb = function() {
			if (config.module) {
				if ($('body').mask) {
					$('body').mask();
				}
			}
			$('#commonWinFakeMask').show();
			if (typeof(callback) == 'function') {
				callback();
			}
		};
		setVisible(true, cb);
	}
	/** 隐藏窗口 */
	function hide(callback) {
		var cb = function() {
			if (config.module) {
				if ($('body').unmask) {
					$('body').unmask();
				}
			}

			$('#commonWinFakeMask').hide();
			if (typeof(callback) == 'function') {
				callback();
			}
		};
		setVisible(false, cb);
	}

	/** toggle */
	function slideToggle(duration, callback) {
		isAnimating = true;
		var c = function() {
			isAnimating = false;
			if (typeof(callback) == 'function') {
				callback();
			}
		}
		thisWin.stop().slideToggle({
					duration : duration || 200,
					complete : c
				});
	}
	/** 销毁窗口 */
	function close(callback) {
		hide(function() {
					thisWin.remove();
					$('#commonWinFakeMask').remove();
					if (typeof(callback) == 'function') {
						callback();
					}
				});
	}
	/** 执行初始化 */
	init();
	/** public 区域 */
	this.show = show;
	this.hide = hide;
	this.close = close;
	this.slideToggle = slideToggle;
	this.updateContent = updateContent;
	this.winId = config.id;
	this.getBody = getBody;
	this.setTitle = setTitle;
	this.getTitleDom = getTitle;
	this.setPosition = setPosition;
	this.isShow = function() {
		return isShow;
	};
}