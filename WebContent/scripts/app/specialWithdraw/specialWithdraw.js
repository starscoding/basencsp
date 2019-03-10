Ext.Loader.setConfig({
    enabled: true
});
Ext.Loader.setPath('Ext.components', eastcom.baseURL + '/static/commonjs/components');
Ext.Loader.setPath('Ext.ux', extDir + '/ux');
Ext.Loader.setPath('Ext.user', eastcom.baseURL + '/scripts/sysmng/security/usermng/components');
Ext.require(['Ext.form.*', 'Ext.data.*', 'Ext.grid.*', 'Ext.tip.*', 'Ext.components.DateTimeField']);

var selUsers = [];//记录grid选择的记录
var rewardMng = (function () {
    Ext.define('Ext.ux.CustomTrigger', {
        extend: 'Ext.form.field.Trigger',
        alias: 'widget.customtrigger',
        trigger1Cls: Ext.baseCSSPrefix + 'form-search-trigger'
    });
    Ext.define('User', {
        extend: 'Ext.data.Model',
        fields: [{
            name: 'id',
            type: 'string'
        }, {
            name: 'money',
            type: 'float'
        }, {
            name: 'recordTime',
            type: 'string'
        }, {
            name: 'rewardTime',
            type: 'string'
        }, {
            name: 'videoId',
            type: 'string'
        }]
    });

    Ext.define('NoData', {
        extend: 'Ext.data.Model',
        fields: [{
            name: 'noDataColumn',
            type: 'string'
        }]
    });

    var noDataStore = Ext.create('Ext.data.Store', {
        model: 'NoData',
        data: [{
            noDataColumn: MSG_EMPTYRSLT
        }]
    });
    var noDataColumn = [{
        text: MSG_QUERYRSLT,
        sortable: false,
        dataIndex: 'noDataColumn',
        flex: 1,
        align: 'center'
    }];
    var userStore = Ext.create('Ext.data.Store', {
        model: 'User',
        pageSize: eastcom.pageSize,
        id: 'userGridStore',
        autoLoad: false,
        proxy: {
            type: 'ajax',
            url: eastcom.baseURL + '/rewardMng/getRewardInfo',
            reader: {
                type: 'json',
                root: 'data.elements',
                totalProperty: 'data.total'
            },
            extraParams: {},
            actionMethods: {
                read: 'POST'
            },
            timeout: 180000
        },
        listeners: {
            beforeload: function () {

            },
            load: function (me, records) {
            }
        }
    });
    sm = Ext.create('Ext.selection.CheckboxModel', {});
    var userColumn = [{
        xtype: 'rownumberer'
    }, {
        text: '用户名',
        width: 150,
        sortable: true,
        dataIndex: 'money'
    }, {
        text: '剩余欢乐豆',
        sortable: true,
        dataIndex: 'rewardTime',
        width: 120,
        align: 'center'
    }, {
        text: '操作',
        sortable: true,
        dataIndex: 'videoId',
        width: 120,
        align: 'center',
        renderer : function (value,metadata,record,rowIndex,colIndex,store,view) {
            return "<a href='#' onclick='alert()'>"+value+"</a>";
        }
    }];

    // var addAction = Ext.create('Ext.Action', {
    //     iconCls: 'icon-add',
    //     text: BUTTON_NEW,
    //     hidden: eastcom.isPermitted('usermng', 'add'),
    //     handler: function (widget, event) {
    //         Ext.create('Ext.user.infoCollectionWin', {
    //             initConf: initConf,
    //             extConf: extConf
    //         }).show();
    //     }
    // });

    var userMainGrid = Ext.create('Ext.grid.Panel', {
        id: 'userMainGrid',
        fit: true,
        split: true,
        // title: '查询结果',
        region: 'center',
        border: true,
        store: userStore,
        // selModel: sm,
        autoHeight: true,
        columnLines: true,
        enableColumnHide: eastcom.enableColumnHide,
        columns: userColumn,
        nomalStore: userStore,
        nomalColumns: userColumn,
        noDataStore: noDataStore,
        noDataColumns: noDataColumn,
        viewConfig: {
            loadMask: false
        },
        switchToNoDataMode: function () {
            var me = this;
            if (me.store != me.noDataStore) {
                me.reconfigure(me.noDataStore, me.noDataColumns);
            }

        },
        switchToNomalMode: function () {
            var me = this;
            if (me.store != me.nomalStore) {
                me.reconfigure(me.nomalStore, me.nomalColumns);
            }
        },
        tbar: Ext.create('Ext.toolbar.Toolbar', {
            // items: [addAction]
        }),
        bbar: Ext.create('Ext.components.BaseCommonPagingToolbar', {
            store: userStore,
            listeners: {}
        }),
        listeners: {}
    });

    function initComponent() {
        Ext.create('Ext.Viewport', {
            layout: {
                type: 'border',
                padding: 5
            },
            items: [{
                region: 'north',
                margin: '5 5 5 5',
                height: 50,
                layout: 'column',
                bodyPadding: 10,
                border: 0,
                items: [{
                 xtype: 'textfield',
                 id: 'userN',
                 name: 'userN',
                 fieldLabel: USERNAME,
                 emptyText: MSG_PLEASE_INSERT,
                 editable: false,
                 labelAlign: 'right',
                 labelWidth: 50
                 },{
                    xtype: 'button',
                    style: 'margin-left:20px',
                    //formBind : true,
                    iconCls: 'icon-search',
                    width: 70,
                    text: BUTTON_SEARCH,
                    handler: function () {
                        var stime = Ext.getCmp('startTime').getValue();
                        var etime = Ext.getCmp('endTime').getValue();
                        if (stime == null || etime == null || stime == '' || etime == '') {
                            Ext.Msg.alert("提示", "开始时间和结束时间不能为空！");
                        }
                        var proxy = userStore.proxy;
                        proxy.extraParams = {
                            startTime: Ext.getCmp('startTime').getValue(),
                            endTime: Ext.getCmp('endTime').getValue()
                        };
                        userStore.loadPage(1, {
                            start: 0,
                            limit: eastcom.pageSize
                        });
                    }
                }, {
                    xtype: 'button',
                    style: 'margin-left:20px',
                    formBind: true,
                    iconCls: 'icon-refresh',
                    width: 70,
                    text: BUTTON_RESET,
                    handler: function () {
                        Ext.getCmp('userN').setValue('');
                        Ext.getCmp('startTime').setValue('');
                        Ext.getCmp('endTime').setValue('');
                        Ext.getCmp('havePermission').items.get(0).setValue(true);
                    }
                }]
            }, {
                region: 'center',
                id: 'tabGrid',
                layout: 'fit',
                margin: '0 5 0 5',
                border: 0,
                items: [userMainGrid]
            }],
            listeners: {
                afterrender: function () {
                }
            }
        });
    }

    return {
        initComponent: initComponent
    }
})();