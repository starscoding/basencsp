(function() {
	var sc = {
		theme : 'classic-compact',
		"root" : {
			"data" : {
				"text" : "百度产品"
			},
			"children" : [{
						"data" : {
							"id" : "news",
							"text" : "新闻"
						},
						"children" : [{
									"data" : {
										"text" : "11111111",
										"expandState" : "collapse"
									},
									"children" : [{
												"data" : {
													"text" : "2222"
												}
											}]
								}]
					}, {
						"data" : {
							"id" : "website",
							"text" : "网页"
						},
						"children" : [{
							"data" : {
								"text" : "11111111"
							},
							"children" : [{
										"data" : {
											"text" : "2222"
										},
										"children" : [{
													"data" : {
														"text" : "11111111"
													},
													"children" : [{
																"data" : {
																	"text" : "2222"
																}
															}]
												}]
									}]
						}]
					}, {
						"data" : {
							"text" : "贴吧",
							"priority" : 2
						}
					}, {
						"data" : {
							"text" : "知道",
							"priority" : 2
						}
					}, {
						"data" : {
							"text" : "音乐",
							"priority" : 3
						}
					}, {
						"data" : {
							"text" : "图片",
							"priority" : 3
						}
					}, {
						"data" : {
							"text" : "视频",
							"priority" : 3
						}
					}, {
						"data" : {
							"text" : "地图",
							"hyperlink" : "javascript:alert(1)",
							"priority" : 3
						}
					}, {
						"data" : {
							"text" : "111111",
							"note" : "21231111",
							"priority" : 3
						}
					}, {
						"data" : {
							"text" : "更多",
							"hyperlinkTitle" : "Title",
							"hyperlinkTarget" : "__new",
							"hyperlink" : "http://www.baidu.com/more"
						}
					}]
		}
	}
	var km = window.km = new kityminder.Minder();
	km.renderTo('div');
	km.importJson(sc);
	km.execCommand('Hand');
	km.useTemplate('right');
	var moveToLeft = function() {
		if (km._rc.transform.translate) {
			km._rc.transform.translate[0].x = 30;
			km._rc._applyTransform();
		} else {
			setTimeout(moveToLeft, 300);
		}
	}
	moveToLeft();

	$('#addNode').on('click', function() {
				var node = km.getNodeById('website');
				var subNode = km.createNode('aaaaa', node);
				km.refresh();// 增加多个node先所有子节点都增加完再进行refresh一次
			});

	$('#removeNode').on('click', function() {
				var node = km.getNodeById('news');
				node && km.removeNode(node);
			});

	km.on('contentchange', function(e) {
				var node = e.node;
				if (node) {
					console.log(node.isExpanded());
				}
			})

	km.on('editnoterequest', function(node) {
				console.log(node);
			})

	var currentHoverNode = null;
	km.on('mouseover', function(e) {
				var node = e.getTargetNode();
				if (node && node !== currentHoverNode) {
					console.log(node);// do sth....
					currentHoverNode = node;
				} else if (!node) {
					currentHoverNode = null;
				}
			})

})()