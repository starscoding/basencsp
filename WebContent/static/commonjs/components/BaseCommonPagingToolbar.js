/**
 * 分页组件公共方式提供
 */
Ext.define('Ext.components.BaseCommonPagingToolbar',{
	extend : 'Ext.toolbar.Paging',
	pageSize: eastcom.pageSize,
    displayInfo: true,
    beforePageText : PAGING_INFO1,
	afterPageText : PAGING_INFO2,
	firstText : PAGING_FIRST,
	prevText : PAGING_PREVIOUS,
	nextText : PAGING_NEXT,
	lastText : PAGING_LAST,
	refreshText : PAGING_REFRESH,
	displayMsg : PAGING_DISPLAY_MSG,
	emptyMsg : PAGING_EMPTY_MSG
   // plugins: Ext.create('Ext.ux.ProgressBarPager', {})
});
