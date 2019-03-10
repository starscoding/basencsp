/*
 * ! 自定义bootstrap的Modal方法，用于计算在ifame里弹出模态窗口的top值 -- by 2015-11-19 李永恩
 */

var EuiModal = (function() {
	function show_bs_modal(that) {
		if (top.location == self.location) {
			return;
		}
		var _top = 0;
		if (window.parent && (window.parent.document.body || window.parent.document.documentElement)) {
			_top = window.parent.document.body.scrollTop
					|| window.parent.document.documentElement.scrollTop;
		} else if (document.body || document.documentElement){
			_top = document.body.scrollTop
					|| document.documentElement.scrollTop;
		}else {
			_top = 0;
		}
		if (_top - 60 > 0) {
			_top = _top - 60;
		}
		$(that).css("top", _top)
	}
	function shown_bs_modal(that) {
		if (top.location == self.location) {
			return;
		}
		var _hg = $(that).children('div').height();
		var _ifhg = $(document.documentElement || document.body).parent()
				.height();
		var _top = $(that).position().top;
		if (_top + _hg > _ifhg) {
			_top = _ifhg - _hg - 65;
			$(that).css("top", _top)
		}
	}
	
	return {
		show_bs_modal:show_bs_modal,
		shown_bs_modal:shown_bs_modal
	};
})();
