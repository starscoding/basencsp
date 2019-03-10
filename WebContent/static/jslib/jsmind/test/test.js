$(function() {
	var mind = {
		"meta" : {
			"name" : "jsMind remote",
			"author" : "hizzgdev@163.com",
			"version" : "0.2"
		},
		"format" : "node_tree",
		"data" : {
			"id" : "root",
			"topic" : "jsMind",
			"children" : [{
						"id" : "easy",
						"topic" : "Easy",
						"direction" : "left",
						"children" : [{
									"id" : "easy1",
									"topic" : "Easy to show",
									"children" : [{
												"id" : "x1",
												"topic" : "Easy to show1"
											}, {
												"id" : "x2",
												"topic" : "Easy to show2"
											}, {
												"id" : "x3",
												"topic" : "Easy to show3"
											}]
								}, {
									"id" : "easy2",
									"topic" : "Easy to edit"
								}, {
									"id" : "easy3",
									"topic" : "Easy to store"
								}, {
									"id" : "easy4",
									"topic" : "Easy to embed"
								}]
					}, {
						"id" : "open",
						"topic" : "Open Source",
						"direction" : "right",
						"children" : [{
									"id" : "open1",
									"topic" : "on GitHub"
								}, {
									"id" : "open2",
									"topic" : "BSD License"
								}]
					}, {
						"id" : "powerful",
						"topic" : "Powerful",
						"direction" : "right",
						"children" : [{
									"id" : "powerful1",
									"topic" : "Base on Javascript"
								}, {
									"id" : "powerful2",
									"topic" : "Base on HTML5"
								}, {
									"id" : "powerful3",
									"topic" : "Depends on you"
								}]
					}, {
						"id" : "other",
						"topic" : "test node",
						"direction" : "left",
						"children" : [{
									"id" : "other1",
									"topic" : "I'm from ajax",
									leaf : false
								}, {
									"id" : "other2",
									"topic" : "I can do everything"
								}]
					}]
		}
	};
	var options = {
		container : 'jsmind_container',
		theme : 'default',
		editable : false,
		loadingUrl : 'http://localhost:7080/web-common/static/jslib/jsmind/test/children.json',
		listeners : {
			afterInit : function(jm) {
				var ext_panel = jm.get_extpanel(), d = ext_panel.getDom();
				// console.log(d);
			},
			showExtensionPanel : function(jm, ext_panel) {
				var node = jm.get_selected_node();
				// console.log(node.getPath());
			},
			selectNode : function(jm, node) {
				var ext_panel = jm.get_extpanel();
				// ext_panel.show();
			}
		},
		extensionPanel : {
			align : 'right',
			width : '20%'
		}
	};
	var jm = jsMind.show(options, mind);
});