function CommonWindow(sendParam) {
	var thisWin = null;// 当前窗口jquery对象
	var winId = null;// 当前窗口id
	var callBeforeInit;// 执行init之前调用的函数
	var onDrag = false;// 是否正在被拖动
	var originWinPosition = {};// 原始窗口位置
	var originMousePosition = {};// 原始鼠标位置
	var logoCss = {// 提供选择的logo css集合
		defaultCls : 'seting_logo'
	};
	var rtButtons = {// 提供选择的右上角按钮html集合
		opt : '',
		max : '',
		min : '',
		close : '<a class="close" title="关闭" hidefocus="hidefocus"></a>'
	};
	var defaultParam = {// 默认参数
		id : 'CommonWindow-' + eastcom.commonWindowNum,
		title : '',
		buttonAlign : 'right',
		height : 200,
		width : 300,
		logoCls : logoCss.defaultCls,
		closable : true,
		closeAction : 'destroy',
		content : '内容html显示区域',
		rtButtons : ['close'],
		buttons : []
	};
	/** 初始化 */
	function init() {
		var param = sendParam
				? $.extend(defaultParam, sendParam)
				: defaultParam;// 整合参数，若用户未指定，则使用默认参数
		var id = param.id;// 当前window的id
		winId = id;
		var titleId = param.id + '-title';// 当前window的titleId
		var currentRtButtons = [];// 当前窗口需要创建的右上角按钮集合
		for (var i = 0; i < param.rtButtons.length; i++) {// 为currentRtButtons添加内容
			var b = param.rtButtons[i];
			switch (b) {
				case 'opt' :
					currentRtButtons.push(rtButtons.opt);
					break;
				case 'max' :
					currentRtButtons.push(rtButtons.max);
					break;
				case 'min' :
					currentRtButtons.push(rtButtons.min);
					break;
				case 'close' :
					currentRtButtons.push(rtButtons.close);
					break;
				default :// 用户自定义，传入html
					currentRtButtons.push(b);
					break;
			}
		}
		var centerHeight = param.height - 42;
		var centerwidth = param.width - 22;
		var html = '';
		html += '<table id="'
				+ id
				+ '" class="window" border="0" cellpadding="0" cellspacing="0">';
		html += '<tr>';
		html += '<td class="win_top_left"></td>';
		html += '<td class="win_top" id="' + titleId + '">';
		html += '<div class="window_title">' + param.title + '</div>';
		if (param.closable) {
			html += '<div class="window_close"></div>';
		}
		html += '</td>';
		html += '<td class="win_top_right"></td>';
		html += '</tr>';
		html += '<tr>';
		html += '<td colspan="3">';
		html += '<div class="win_left" style="height:' + centerHeight
				+ 'px"></div>';
		html += '<div class="win_center" style="height:' + centerHeight
				+ 'px;width:' + centerwidth + 'px">';
		html += '<div class="contentContainer">' + param.content + '</div>';
		html += '</div>';
		html += '<div class="win_right" style="height:' + centerHeight
				+ 'px"></div>';
		html += '</td>';
		// html += '<td class="win_left"></td>';
		// html += '<td class="win_center">';
		// ;
		// html += '</td>';
		// html += '<td class="win_right"></td>';
		html += '</tr>';

		/** 按钮自定义区域。未完成 */
		// html += '<tr>';
		// html += '<td class="win_left buttonContainer"></td>';
		// html += '<td class="win_center buttonContainer">';
		// html += '<input type="button" class="window_bton" value="关 闭" />';
		// html += '</td>';
		// html += '<td class="win_right buttonContainer"></td>';
		// html += '</tr>';
		html += '<tr>';
		html += '<td class="win_bottom_left"></td>';
		html += '<td class="win_bottom">';
		html += '</td>';
		html += '<td class="win_bottom_right"></td>';
		html += '</tr>';
		html += '</table>';

		// var html = '<div id="' + id + '" class="window">';
		// html += '<div id="' + titleId + '" class="desktop_box_tit">';
		// html += '<div class="logo_base ' + param.logoCls + '"></div>';
		// $.each(currentRtButtons, function(index, b) {
		// html += b;
		// });
		// html += '</div>';
		// html += '<div class="contentContainer">' + param.content + '</div>';
		// if (param.buttons.length > 0) {
		// html += '<div class="foot">'
		// var buttonNum = 1;
		// for (var i = param.buttons.length - 1; i >= 0; i--) {
		// var b;
		// switch (param.buttonAlign) {
		// case 'left' :
		// b = param.buttons[param.buttons.length - 1 - i];
		// break;
		// case 'right' :
		// b = param.buttons[i];
		// break;
		// default :
		// b = param.buttons[param.buttons.length - 1 - i];
		// break;
		// }
		// if (typeof(b) == 'object') {
		// var buttonId = id + '-b' + buttonNum;
		// html += '<button id="' + buttonId + '" class="button_'
		// + param.buttonAlign + '">';
		// html += b.text;
		// html += '</button>';
		// b.buttonId = buttonId;
		// buttonNum += 1;
		// }
		// };
		// html += '</div>';
		// }
		// html += '</div>';
		thisWin = $(html);
		thisWin.css({
					height : param.height,
					width : param.width,
					position : 'absolute',
					top : (document.body.clientHeight - param.height) / 2,
					left : (document.body.clientWidth - param.width) / 2
				});
		$('body').append(thisWin);
		thisWin.hide();
		$('#' + titleId).css({
					width : centerwidth
				});
		bindFunctions({
					titleId : titleId,
					buttons : param.buttons,
					closable : param.closable,
					closeAction : param.closeAction
				});
		eastcom.commonWindowNum += 1;// 序列自增
	}
	/** 绑定事件 */
	function bindFunctions(param) {
		// 右上角关闭按钮事件
		if (param.closable) {
			var closeBox = $('#' + param.titleId + ' .window_close');
			closeBox.bind('click', function() {
						if (param.closeAction == 'destroy') {
							close();
						} else if (param.closeAction == 'hide') {
							hide();
						}
					});
			closeBox.hover(function() {
						$(this).addClass('window_close_hover');
					}, function() {
						$(this).removeClass('window_close_hover');
					});
		}
		// 下方按钮事件
		$.each(param.buttons, function(index, b) {
					$('#' + b.buttonId).bind('click', b.handler);
				});
		// 拖动事件
		var titleBar = $('#' + param.titleId);
		titleBar.mousedown(function(event) {
					thisWin.fadeTo(20, 0.7);
					thisWin.css({
								zIndex : 99999
							});
					originPosition = thisWin.offset();
					originMousePosition = {
						top : event.pageY,
						left : event.pageX
					}
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
					onDrag = false;
					thisWin.fadeTo(20, 1);
				});
	}
	/**
	 * 更改显示内容(未实现)
	 * 
	 * @param {}
	 *            content 新内容
	 * @param {}
	 *            animate 动画方向，默认无动画
	 */
	function updateContent(content, animate) {
		var container = $('#' + winId + ' .contentContainer');

	}

	/** 显示窗口 */
	function show(callback) {
		thisWin.show();
		if (typeof(callback) == 'function') {
			callback();
		}
	}
	/** 隐藏窗口 */
	function hide(callback) {
		thisWin.hide();
		if (typeof(callback) == 'function') {
			callback();
		}
	}

	/** 销毁窗口 */
	function close(callback) {
		thisWin.remove();
		if (typeof(callback) == 'function') {
			callback();
		}
	}

	if (typeof(callBeforeInit) == 'function') {
		callBeforeInit();// 执行初始化之前函数
	}
	init();// 执行初始化函数

	/** public 区域 */
	this.show = show;
	this.hide = hide;
	this.close = close;
	this.updateContent = updateContent;
	this.winId = winId;
}