/**
 * 条件选择组件
 * 
 * @author JJF
 * @since Nov 25, 2014
 * @description 多条件选择组件
 */
Ext.define('Ext.components.ConditionChooserGroup', {
			extend : 'Ext.panel.Panel',
			alias : 'widget.conditionchoosergroup',
			getValue : function(spt) {
				var obj = this.getRecs(), val = {}, spt = spt || ',';
				for (var i in obj) {
					if (obj.hasOwnProperty(i)) {
						var vals = [];
						Ext.each(obj[i], function(rec, i) {
									vals.push(rec.value);
								});
						val[i] = vals.join(spt);
					}
				}
				return val;
			},
			setValue : function(name, value) {
				var me = this, cdt = me._cdts[name];
				cdt.setValue(value);
			},
			setConditionVisible : function(name, flag) {
				var me = this, p = me._cntpanels[name], cdt = me._cdts[name];
				if (p) {
					p.setVisible(flag);
				}
			},
			update : function(name, data, mode) {
				var me = this, cdt = me._cdts[name];
				if (cdt) {
					cdt.update(data, mode);
				}
			},
			setDisabled : function(name, disabled) {
				var me = this, cdt = me._cdts[name];
				if (cdt) {
					cdt.setDisabled(disabled);
				}
			},
			getRecs : function() {
				var me = this, obj = {};
				for (var i in me._cdts) {
					if (me._cdts.hasOwnProperty(i)) {
						obj[i] = me._cdts[i].getRecs();
					}
				}
				return obj;
			},
			reset : function() {
				var me = this;
				for (var i in me._cdts) {
					if (me._cdts.hasOwnProperty(i)) {
						me._cdts[i].reset();
					}
				}
			},
			conditions : [],
			_cdts : {},
			_cntpanels : {},
			listeners : {
				afterrender : function() {
					$('#' + this.id).css({
								'overflow' : 'visible'
							});
					$('#' + this.id + '-body').css({
								'overflow' : 'visible'
							});
				}
			},
			initComponent : function() {
				var me = this, items = [];
				if (me.conditions && me.conditions.length) {
					Ext.each(me.conditions, function(c, i) {
								var id = 'con-cnt' + i, p = Ext.create(
										'Ext.panel.Panel', {
											id : id + '-panel',
											border : false,
											hidden : c.hidden,
											html : '<div id="'
													+ id
													+ '" style="width:100%;"></div>',
											listeners : {
												afterrender : function() {
													$('#' + id + '-panel').css(
															{
																'overflow' : 'visible'
															});
													// console.log($('#' + id +
													// '-panel').length);
													$('#' + id + '-panel-body')
															.css({
																'overflow' : 'visible'
															});
													c.renderTo = id;
													var cdt = new ConditionChooser(c);
													me._cdts[c.name] = cdt;
													this.doLayout();
												}
											}
										});
								me._cntpanels[c.name] = p;
								items.push(p);
							});
				}
				me.items = items;
				me.callParent();
			}
		});