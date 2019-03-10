/**
 * 简单遮罩层插件
 * 
 * @author JJF@eastcom-sw
 * 
 * @date Mar 21 2013
 * 
 * @param msg
 *            提示信息(可选参数)
 * 
 * @description 为某个jquery对象添加或者删除遮罩
 * 
 * @example
 * $('#maskTest').mask('test');//添加
 * $('#maskTest').unmask();//删除
 */
$.fn.mask = function(msg, conf) {
	var me = this;
	var maskDom = $('<div name="mask"></div>');
	var zindex = parseInt(me.css('z-index'));
	zindex = zindex ? (zindex + 1) : 90000
	var defaultConf = {
		zindex : zindex
	};
	if (!conf) {
		conf = {};
	}
	conf = $.extend(defaultConf, conf);
	var domHeight = me.innerHeight() == 0 ? $(window).height() : me
			.innerHeight();
	maskDom.css({
				'filter' : 'alpha(opacity=30)',
				'-moz-opacity' : '0.3',
				'-khtml-opacity' : '0.3',
				'opacity' : '0.3',
				'top' : '0',
				'left' : '0',
				'position' : 'absolute',
				'width' : me.innerWidth(),
				'height' : domHeight,
				'background' : 'black',
				'z-index' : conf.zindex
			});
	var textContainerDom = null;
	if (msg != null) {
		textContainerDom = $('<div name="textContainer"><div>');
		textContainerDom.css({
					'background-color' : '#DFE9F6',
					'background-image' : 'none',
					'border' : '1px solid #99BCE8',
					'padding' : '2px',
					'position' : 'absolute',
					'z-index' : conf.zindex + 1
				});
		var textDom = $('<div>' + msg + '</div>');
		textDom.css({
					'background-color' : '#EEEEEE',
					'background-image' : 'url("' + eastcom.baseURL
							+ '/static/images/common/icons/loading.gif")',
					'background-position' : '5px center',
					'background-repeat' : 'no-repeat',
					'border' : '1px solid #A3BAD9',
					'color' : '#222222',
					'cursor' : 'wait',
					'font' : '11px tahoma,arial,verdana,sans-serif',
					'padding' : '5px 10px 5px 25px'
				});
		textContainerDom.append(textDom);
		me.append(textContainerDom);
	}
	me.append(maskDom);
//	me.data("mask-overflow", {
//				"overflow-x" : me.css("overflow-x"),
//				"overflow-y" : me.css("overflow-y")
//			});
//	me.css({
//				"overflow" : "hidden"
//			});
	if (textContainerDom) {
		textContainerDom.css({
					'left' : (me.width() - textContainerDom.width() - 4) / 2,
					'top' : (domHeight - textContainerDom.height() - 4) / 2
				});
	}
};

$.fn.unmask = function() {
	var me = this;
	var mask = me.children('[name=mask]');
	var textContainer = me.children('[name=textContainer]');
	mask.remove();
	textContainer.remove();
	//var mask_overflow = me.data("mask-overflow");
	//me.css(mask_overflow);
}