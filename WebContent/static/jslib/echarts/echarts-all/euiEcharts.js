/**
 * echarts公共创建类
 *
 * @since 0.3
 * @author lyn
 */

define(['echarts'], function(_echarts) {
	var _zrender = _echarts.zrender;
	var zrColor = _zrender.tool.color;
	
	var _euiResizeTO = null;
	//缓存chart对象
	var _CacheFun = {
		__cache : {},
		//获取所有缓存对象
		_getCacheObj: function() {
            if (!this.__cache) {this.__cache = {};}
            return this.__cache;
        },
        //新增一个对象到缓存里
        _bindCache: function(id, data) {
            var cache = this._getCacheObj();
            cache[id] = data;
        },
        //删除一个对象从缓存里
        _unbindCache: function(id) {
            var cache = this._getCacheObj();
            if (cache) {delete(cache[id])}
        },
        //获取一个对象从缓存里
        _getCache: function(id) {
            var cache = this._getCacheObj();
            if (cache) {
                if (id && id.length) {return cache[id]} else {return cache}
            }
        },
        //清空缓存
        _clearCache: function() {
            this.__cache = {};
        }
	};
	
	//全局option属性
	var _getOption = function(_customOption,chartType) {//获取option对象
		var option = {
			backgroundColor : "#ffffff",// 背景色，设置成全白，不设置属性，则透明
			color : euiColorList,//数值系列的颜色列表
			calculable : false,//禁止拖拽
			tooltip : {//提示内容
				trigger : 'item'
			},
			noDataLoadingOption : {//无数据是的加载动画
				text : "暂无数据",
				effect : 'whirling',
				textStyle : {
			        fontSize : 16
			    }
			},
			toolbox: {//工具栏
		        show : true,
		        feature : {
		            mark : {show: false},
		            dataView : {show: false, readOnly: false},
		            magicType : {show: false},
		            dataZoom : {
				        show : false,
				        title : {
				            dataZoom : '区域缩放',
				            dataZoomReset : '区域缩放后退'
				        }
				    },
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    }
		};
		(function($){
			/**
			 * 为option增加默认设置，全局的默认设置
			 */
			var __option = _getOptionByType(chartType);
			$.extend(true,option,__option,_customOption);
		})(jQuery);
		return option;
	};
	//根据特定的图形，提供特殊的option对象，现在较少，预留，后期添加
	var _getOptionByType = function(_type) {//获取option对象
		var _option = {};
		if(_type == "line"){
			_option = {
				grid :{
		    		/*x : 40,
		    		x2 : 40,*/
		    		y : 50,
		    		y2 : 30
			    },
			    xAxis : [{
		            type : 'category',
		            boundaryGap : false
		        }]
			};
		}else if(_type == "bar"){
			_option = {
			    xAxis : [{//x轴的默认样式
		            type : 'category',
		            boundaryGap : false
		        }]
			};
		}else if(_type == "pie"){
			_option = {};
		}else if(_type == "gauge"){
			_option = {};
		}else if(_type == "scatter"){
			_option = {};
		}else if(_type == "k"){
			_option = {};
		}else if(_type == "radar"){
			_option = {};
		}else if(_type == "map"){
			_option = {};
		}else if(_type == "force"){
			_option = {};
		}else if(_type == "chord"){
			_option = {};
		}else if(_type == "funnel"){
			_option = {};
		}else if(_type == "eventRiver"){
			_option = {};
		}
		if(_type == "line" || _type == "bar"){
			var __option = {
				grid :{
					/*x : 40,
		    		x2 : 40,*/
		    		y : 50,
		    		y2 : 30
			    },
			    xAxis : [{
		            type : 'category',
		            boundaryGap : false
		        }]
			};
			(function($){
				$.extend(true,_option,__option);
			})(jQuery);
		}
		return _option;
	};
	//对数组的继承
	var _extend = function(_oArr,_tArr){
		if(_oArr && _tArr){
			for (var i = 0; i < _tArr.length; i++) {
				(function($){$.extend(true,_oArr[i]=_oArr[i]||{},_tArr[i]=_tArr[i]||{});})(jQuery);
			}
			return _oArr;
		}else if(_oArr){
			return _oArr;
		}else if(_tArr){
			return _tArr;
		}
	}
	//初始化方法
	var _init = function(_co,_callFun){//初始化方法
		var chartId = _co.chartId;
		var option = _co.option;
		var chartType = _co.chartType;
		var other = _co.other;
		var _cache = _CacheFun._getCache(chartId);
		if (_cache) {//判断全局变量数组里是否有chart对象，有的话加载，没有的话初始化chart，此处提高加载速度
			_loadEchart(chartId,option,chartType);//加载echarts
			if (_callFun && typeof _callFun == 'function') {//回调函数，可以在生成图形之后执行其他内容。
				_callFun.call(this,chartId);
			}
		}else{
			/*var _e = ['echarts','echarts/chart/bar','echarts/chart/line', 
					'echarts/chart/pie', 'echarts/chart/gauge'];*/
			require(['echarts','echarts/../theme/'+other.theme+'','echarts/../chart/bar','echarts/../chart/line', 
					'echarts/../chart/pie', 'echarts/../chart/gauge',
					'echarts/../chart/scatter', 'echarts/../chart/k',
					'echarts/../chart/radar', 'echarts/../chart/map',
					'echarts/../chart/force', 'echarts/../chart/chord',
					'echarts/../chart/funnel', 'echarts/../chart/eventRiver',
					'echarts/../chart/heatmap', 'echarts/../chart/treemap',
					'echarts/../chart/venn', 'echarts/../chart/tree', 'echarts/../chart/wordCloud'], function(ec,theme, bar, line, pie, gauge) {
					var myChart = ec.echarts.init(document.getElementById(chartId),theme);
					_CacheFun._bindCache(chartId,myChart);//缓存起来
					_loadEchart(chartId,option,chartType);//加载echarts
					//_initEvent();
					_cache = myChart;
					if (_callFun && typeof _callFun == 'function') {//回调函数，可以在生成图形之后执行其他内容。
						_callFun.call(this,chartId);
					}
				});
		}
		return _cache;
	};
	var _initEvent = function(chartId) {//加载chart的事件方法
		/*var _chart = _CacheFun._getCache(chartId);
		if (_chart != null && _chart) {
			_chart.on(ecConfig.EVENT.RESIZE, function(){
				console.log(chartId)
			}
			);
		}*/
	}
	//load chart，生成echarts
	var _loadEchart = function(chartId,option,chartType) {//加载chart的方法
		var myChart = _CacheFun._getCache(chartId);
		if (option == null || option == undefined || option == "" || option == {}) {
			option = {'series':[{}]};
		}
		var _option = _getOption(option,chartType);//获得option对象
		if (_option == null) {//为空这提示div里的内容，类似not data
			myChart.dispose();
			document.getElementById(chartId).innerHTML = chartHtml;
			_CacheFun._unbindCache(chartId);//清空全局变量里的chart对象
		}else{
			myChart.hideLoading();
			myChart.clear();
			/*myChart.showLoading({
			    text : "数据加载中...",
			    effect : 'spin',//whirling
			    textStyle : {
			        fontSize : 16
			    }
			});*/
			myChart.setOption(_option);
			window.onresize = function () {
				if(_euiResizeTO){
					clearTimeout(_euiResizeTO);
				}
				_euiResizeTO = setTimeout(function(){
					var CacheObj = _CacheFun._getCacheObj();
					for (var p in CacheObj) {
						if ($("#"+p).is(":visible")) {//如果图形是隐藏的，则不需要刷新图形
							var name = p;// 属性名称
							var _chart = CacheObj[p];
							if (_chart != null && _chart) {
								_chart.resize();
							}
						}
					}
				},700);
		    };
		    //myChart.hideLoading();
		}
	};
	//没有数据的html样式
	var noDataHtml = "<div style='width: 100%;height: 100%;text-align:center;position:relative;top:50%;'>" +
				"<font size='3'>暂无数据</font></div>";//存储chart div里初始化时候的内容，类似not data提示
	////蓝色的渐变色数组
	var blueStepColorList = ['#bbbbff','#bbbdff','#bbc0ff','#bbc2ff','#bbc5ff',
					'#bbc8ff','#bbcaff','#bbcdff','#bbd0ff','#bbd2ff',
					'#bbd5ff','#bbd8ff','#bbdaff','#bbddff','#bbe0ff',
					'#bbe2ff','#bbe5ff','#bbe8ff','#bbeaff','#bbedff'];//蓝色的渐变色
	////自定义颜色数值数组
	var euiColorList = ["#62bdff","#c09ce8","#f5dc5c","#fd6500","#5db75d",
					"#ffbacf","#7e9fcc","#88f1eb","#f9f88c","#9969aa",
					"#b9c600","#fcb247","#2d81cf","#b3f171","#ec9df5",
					"#ffe700","#20b0a9","#f67b98","#8b74c4","#d8e428"];//张叶春自定义的颜色数值，20个
	//颜色值 _i number
	var getColor = function(_i){
		if(isNaN(_i)){
			_i = 0;
		}
		return zrColor.getColor(_i);
	}
	//自定义的颜色值 _i number，最大19，总共20个颜色值
	var getEuiColor = function(_i){
		if(isNaN(_i)){
			_i = 0;
		}
		if (_i>=20) {
			_i = _i%20;
		}
		return euiColorList[_i];
	}
	//显示加载动画效果
	var showLoading = function(chartId){
		var _chart = _CacheFun._getCache(chartId);
		_chart.showLoading({
		    text : "数据加载中...",
		    effect : 'spin',
		    textStyle : {
		        fontSize : 16
		    }
		});
	}
	//隐藏加载动画效果
	var hideLoading = function(chartId){
		var _chart = _CacheFun._getCache(chartId);
		_chart.hideLoading();
	}
	//create方法
	//chartType：bar/line/pie/gauge/scatter/k/radar/map/force/chord/funnel/eventRiver支持所有图形，复合图传入一个即可
	var create = function(chartId,option,chartType,other,callFun){
		var _other = {};
		(function($){$.extend(_other,{theme:'macarons'},other);})(jQuery);
		var _co = {
			other:_other,
			chartId:chartId,
			option:option,
			chartType:chartType
		}
		return _init(_co,callFun);//初始化方法
	};
	
	var getCacheObj = function() {
		return _CacheFun._getCacheObj();
    }
    //新增一个对象到缓存里
    var addCache = function(chartId, chartObj) {
        _CacheFun._bindCache(chartId, chartObj);
    }
    //删除一个对象从缓存里
    var delCache = function(chartId) {
    	_CacheFun._unbindCache(chartId);
    }
    //获取一个对象从缓存里
    var getCache = function(chartId) {
    	return _CacheFun._getCache(chartId);
    }
    //清空缓存
    var clearCache = function() {
    	_CacheFun._clearCache();
    }
	return {
			echarts:_echarts.echarts,
			zrender:_zrender,//echarts 的zrender对象
			//config : config,//echarts 的config对象
			zrColor : zrColor,//echarts 的color对象
			noDataHtml : noDataHtml,//没有数据的html样式
			blueStepColorList : blueStepColorList,//蓝色的渐变色数组
			getColor : getColor,//颜色值 _i number
			getEuiColor : getEuiColor, //获取颜色值
			create : create,//create方法
			showLoading : showLoading,//显示加载动画效果
			hideLoading : hideLoading,//隐藏加载动画效果
			getCacheObj : getCacheObj,//获取缓存
			addCache : addCache,//新增一个对象到缓存里 chartId,chartObj
			delCache : delCache,//删除一个对象从缓存里 chartId
			getCache : getCache,//获取一个对象从缓存里 chartId
			clearCache : clearCache//清空缓存
		}
});
