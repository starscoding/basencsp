Ext.Loader.setConfig({
			enabled : true
		});
Ext.Loader.setPath('Ext.components', eastcom.baseURL
				+ '/static/commonjs/components');
Ext.onReady(function() {
			Ext.create('Ext.Viewport', {
						layout : {
							type : 'border',
							padding : 5
						},
						items : [
								Ext.create(
										'Ext.components.ConditionChooserGroup',
										{
											region : 'north',
											height : 200,
											id : 'gp',
											conditions : [{
												name : 'name',
												labelWidth : 90,
												showMore : false,
												multi : false,
												labelAlign : 'right',
												label : '指标名称',
												mode : 'local',
												datas : [{
															name : 'n1',
															label : '测试1',
															value : 'n1'
														}, {
															name : 'n2',
															label : '测试2',
															value : 'n2'
														}, {
															name : 'n3',
															label : '测试3',
															value : 'n3'
														}, {
															name : 'n4',
															label : '测试4',
															value : 'n4'
														}, {
															name : 'n5',
															label : '测试5',
															value : 'n5'
														},{
															name : 'n6',
															label : '测试6',
															value : 'n6'
														}, {
															name : 'n7',
															label : '测试7',
															value : 'n7'
														}, {
															name : 'n8',
															label : '测试8',
															value : 'n8'
														}, {
															name : 'n9',
															label : '测试9',
															value : 'n9'
														}, {
															name : 'n10',
															label : '测试10',
															value : 'n10'
														},{
															name : 'n11',
															label : '测试11',
															value : 'n11'
														}, {
															name : 'n12',
															label : '测试12',
															value : 'n12'
														}, {
															name : 'n13',
															label : '测试13',
															value : 'n13'
														}, {
															name : 'n14',
															label : '测试14',
															value : 'n14'
														}, {
															name : 'n15',
															label : '测试15',
															value : 'n15'
														}, {
															name : 'n16',
															label : '测试16',
															value : 'n16'
														}, {
															name : 'n17',
															label : '测试17',
															value : 'n17'
														}, {
															name : 'n18',
															label : '测试18',
															value : 'n18'
														}, {
															name : 'n19',
															label : '测试19',
															value : 'n19'
														}],
												onSelect : function(recs) {
													// var rec = recs[0], gp =
													// Ext
													// .getCmp('gp');
													// if (rec) {
													// switch (rec.value) {
													// case 'n1' :
													// gp.update('test','region');
													// break;
													// case 'n2' :
													// gp.update('test','userLevel');
													// break;
													// }
													// }

													var rec = recs[0], gp = Ext
															.getCmp('gp');
													if (rec) {
														gp
																.setConditionVisible(
																		'level',
																		rec.value == 'n1');
														gp
																.setDisabled(
																		'level2',
																		rec.value == 'n5');
													}
												}
											}, {
												name : 'level',
												labelWidth : 90,
												labelAlign : 'right',
												label : '等级',
												disabled : true,
												// hidden : true,
												mode : 'local',
												datas : [{
															name : 'n1',
															label : 'dengji11',
															value : 'n1'
														}, {
															name : 'n2',
															label : 'dengji112',
															value : 'n2'
														}, {
															name : 'n3',
															label : 'dengji113',
															value : 'n3'
														}, {
															name : 'n4',
															label : 'dengji114',
															value : 'n4'
														}, {
															name : 'n5',
															label : 'dengji115',
															value : 'n5'
														}],
												multi : true
											}, {
												name : 'level2',
												labelWidth : 90,
												labelAlign : 'right',
												label : '测试',
												disabled : true,
												mode : 'local',
												datas : [{
															name : 'tt1',
															label : 'tt1',
															value : 'tt1'
														}, {
															name : 'tt2',
															label : 'tt2',
															value : 'tt2'
														}, {
															name : 'tt3',
															label : 'tt3',
															value : 'tt3'
														}, {
															name : 'tt4',
															label : 'tt4',
															value : 'tt4'
														}, {
															name : 'tt5',
															label : 'tt5',
															value : 'tt5'
														}],
												multi : true
											}]
										}), Ext.create('Ext.panel.Panel', {
											region : 'center',
											buttons : [{
												text : 'getvalue',
												handler : function() {
													console.log(Ext
															.getCmp('gp')
															.getValue());
												}
											}, {
												text : 'setvalue',
												handler : function() {
													Ext.getCmp('gp').setValue(
															'name',
															['n3', 'n4']);
													var st = '2014年';
													var et = "2015年";

													Ext.getCmp('gp').update(
															'level2', [{
																		name : 'nn1',
																		label : 'nn1',
																		value : st
																				+ et
																	}, {
																		name : 'nn2',
																		label : 'nn2',
																		value : 'nn2'
																	}], 'local');

													Ext.getCmp('gp').setValue(
															'level2', st + et);

												}
											}, {
												text : 'reset',
												handler : function() {
													Ext.getCmp('gp').reset();
												}
											}]
										})]
					});
		});
