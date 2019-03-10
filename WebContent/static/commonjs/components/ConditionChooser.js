/**
 * 条件选择组件
 * 
 * @author JJF
 * @since Nov 25, 2014
 */
var ConditionChooser = (function() {
	var c, defConf, allRecord, mode, log;
	mode = {
		commondata : 'commondata',
		local : 'local'
	};
	defConf = {
		name : '', // 字段名称
		label : '', // 显示名称
		labelAlign : 'left',// align
		labelWidth : 110,// label宽度
		node : '', // 基础数据维护中父节点名称
		mode : mode.commondata, // 数据加载方式，默认从基础数据维护中获取
		datas : [], // mode为local时生效，使用本地自定义数据
		renderTo : '', // dom节点id
		allFlag : false, // 是否显示全选
		multi : true, // 是否开启多选
		showMore : true,// 是否显示more按钮
		defaultValue : '',// 默认选中值，已开启多选情况下支持多选
		onSelect : function(records) { // 选中选项时触发事件
		},
		disabled : false,// 是否禁用
		debug : false
		// 是否开启debug模式
	};

	allRecord = {
		label : "全选",
		name : "_all_",
		value : "_ALL_"
	};

	log = { // 日志打印器
		debug : false,
		info : function(msg) {
			if (this.debug && console && console.log) {
				console.log(msg);
			}
		},
		error : function(msg) {
			if (console && console.error) {
				console.error(msg);
			}
		}
	};

	c = function(conf) {
		var me = this, _cache = {}, currentSel = [], isExpand = false, disabled = false, isOnMulti = false, _d, _init, cnt, _doSel, _initCnt, _initData, _createCondition, _renderData, _update, _freshCurrentSel, _setDisabled, _reset, _log = $
				.extend({}, log);
		_init = function() {
			_log.debug = conf.debug;
			conf = $.extend({}, defConf, conf);
			disabled = conf.disabled;
			_d = $('#' + conf.renderTo);
			if (!_d || !_d.length) {
				_log.error('dom节点未找到，请检查renderTo参数');
				return
			}
			cnt = _initCnt(_d);
			_initData(conf.mode, conf.node);
		};

		_initData = function(_mode, _node) {
			if (_mode == mode.commondata) {
				if (_node && _node.length && !_cache[_node]) {
					$.ajax({
								type : 'POST',
								dataType : "json",
								url : eastcom.baseURL
										+ '/common/loadCommonDatas',
								data : {
									type : _node,
									allFlag : conf.allFlag
								},
								success : function(result) {
									if (result && result.length) {
										_cache[_node] = result;// 缓存当前key的返回值
										_renderData(result, cnt);
									}
								}
							});
				} else if (_cache[_node]) {// 从缓存中读取数据
					_renderData(_cache[_node], cnt);
				}
			} else if (_mode == mode.local) {
				if (conf.allFlag) {
					conf.datas = [allRecord].concat(conf.datas);
				}
				_renderData(conf.datas, cnt);
			}
		}

		_freshCurrentSel = function(container) {
			var cdts = container.find('.sel'), sels = [];
			if (cdts && cdts.length) {
				$.each(cdts, function(i, cdt) {
							sels.push($(cdt).data('record'));
						});
			}
			currentSel = sels;
		};

		_doSel = function(container, cdt) {
			var record = cdt.data('record');
			if (isOnMulti) {
				if (cdt.hasClass('sel')) {
					cdt.removeClass('sel');
				} else {
					if (conf.multi) {
						if (record.value == '_ALL_') {
							container.find('.sel').removeClass('sel');
						} else {
							container.find('.all-record').removeClass('sel');
						}
					}
					cdt.addClass('sel');
				}
			} else {
				container.find('.condition').removeClass('sel');
				cdt.addClass('sel');
			}
			_freshCurrentSel(container);
			if (!isOnMulti) {
				conf.onSelect(currentSel);
			}
		}

		_initCnt = function(dom) {
			var cnt = {}, _cdt_cache = {}, label, outer_cnt, inner_cnt, container, btns, foot_btns, ctrlFn;
			dom.empty().addClass('condition-chooser');
			if (disabled) {
				dom.addClass('condition-chooser-disabled');
			}
			label = $('<div class="label_eastcom"><span>' + conf.label
					+ '</span>:</div>');
			label.css({
						'width' : conf.labelWidth + 1,
						'text-align' : conf.labelAlign
					});
			outer_cnt = $('<div class="outer-cnt"></div>');
			outer_cnt.css({
						'margin-left' : conf.labelWidth
					});
			inner_cnt = $('<div class="inner-cnt"></div>');
			container = $('<div class="condition-cnt hidemulti"></div>');
			inner_cnt.append(container);
			outer_cnt.append(inner_cnt);
			container.on('click', '.condition', function(e) {
						if (disabled) {
							return
						}
						var cdt = $(this);
						_doSel(container, cdt);
					});
			btns = $('<div class="btns_eastcom"><a name="multi" class="btn_eastcom">多选</a><a name="more" class="btn_eastcom">更多<span class="icon-down"></span></a></div>');
			if (!conf.multi) {
				btns.find('[name=multi]').hide();
			}
			
			if(!conf.showMore){
				btns.find('[name=more]').hide();
			}
			btns.on('click', '.btn_eastcom', function(e) {
						if (disabled) {
							return
						}
						var b = $(this), name = b.attr('name');
						if (name == 'more') {
						} else if (name == 'multi') {
							isOnMulti = !isOnMulti;
						}

						isExpand = !isExpand;
						if (!isExpand) {
							isOnMulti = false;
						}
						ctrlFn();
					});

			foot_btns = $('<div class="foot-btn-cnt"><span class="btn_eastcom" name="ok">确定</span></div>');
			foot_btns.on('click', '.btn_eastcom', function(e) {
						if (disabled) {
							return
						}
						var b = $(this), name = b.attr('name');
						if (name == 'ok') {
							conf.onSelect(currentSel);// 触发选择事件
						} else if (name == 'cancel') {
						}
						isExpand = false;
						isOnMulti = false;
						ctrlFn();
					});

			inner_cnt.append(foot_btns);
			$.extend(cnt, {
						add : function(record, isSel) {
							var cdt = _createCondition(record)
							container.append(cdt);
							if (isSel) {
								_doSel(container, cdt);
							}
							_cdt_cache[record.value] = cdt;
						},
						setValue : function(val) {
							if (disabled) {
								return
							}
							if (val && val.length) {
								if (val.join) {// 数组
									var oriMulti = isOnMulti;
									isOnMulti = true;
									ctrlFn();
									for (var i = 0; i < val.length; i++) {
										this.setValue(val[i]);
									}
									isOnMulti = oriMulti;
									ctrlFn();
								} else if (val.split) {// 单个value
									if (_cdt_cache[val]) {
										_doSel(container, _cdt_cache[val]);
									}
								}
							}
						},
						reset : function() {
							container.empty();
						},
						setMultiMode : function(flag) {
							isOnMulti = flag;
						}
					});

			ctrlFn = function() {
				if (isExpand) {
					inner_cnt.addClass('inner-cnt-expand');
					label.addClass('expand-mask');
					btns.addClass('expand-mask');
				} else {
					inner_cnt.removeClass('inner-cnt-expand');
					label.removeClass('expand-mask');
					btns.removeClass('expand-mask');
				}

				if (isOnMulti) {
					foot_btns.show();
					container.removeClass('hidemulti');
				} else {
					container.find('.multi-on').removeClass('multi-on');
					foot_btns.hide();
					container.addClass('hidemulti');
				}
			}
			dom.append(label).append(outer_cnt).append(btns);
			return cnt;
		};

		_createCondition = function(record) {
			var cdt = $('<a class="condition"><div class="multi"></div><span class="condition-text">'
					+ record.label + '</span></a>');
			cdt.data('record', record);
			if (record.value == '_ALL_') {
				cdt.addClass('all-record');
			}
			return cdt;
		};
		_renderData = function(datas, cnt) {
			var dVal = conf.defaultValue, dVals = [], defaultValsObj = {};
			cnt.reset();
			if (datas && datas.length) {
				if (dVal && dVal.length && dVal.split) {
					dVals = dVal.split(',');
					for (var i = 0; i < dVals.length; i++) {
						defaultValsObj[dVals[i]] = true;
					}
				}

				if (conf.multi) {
					cnt.setMultiMode(true);
				}
				$.each(datas, function(i, data) {
							cnt.add(data, defaultValsObj[data.value]);
						});
				cnt.setMultiMode(false);
			}
		}
		_update = function(newData, _mode) {
			_mode = (_mode && _mode.length) ? _mode : conf.mode;
			if (_mode == mode.local) {
				conf.datas = newData;
				_initData(_mode, conf.node);
			} else if (conf.node != newData) {
				conf.node = newData;
				_initData(_mode, newData);
			}
		};

		_setDisabled = function(flag) {
			if (flag) {
				_reset();
				if (_d.find('.inner-cnt').hasClass('inner-cnt-expand')) {
					_d.find('a.btn_eastcom[name=multi]').trigger('click');
				}
				_d.addClass('condition-chooser-disabled');
			} else {
				_d.removeClass('condition-chooser-disabled');
			}
			disabled = flag;
		};

		_reset = function() {
			_d.find('.condition-cnt .sel').removeClass('sel');
			currentSel = [];
		}

		_init();
		$.extend(me, {
					getRecs : function() {
						return currentSel;
					},
					update : function(newData, _mode) {
						return _update(newData, _mode);
					},
					reset : function() {
						_reset();
					},
					setDisabled : function(flag) {
						_setDisabled(flag);
					},
					setValue : function(val) {
						_reset();
						cnt.setValue(val);
					}
				});
	};

	return c;
})();