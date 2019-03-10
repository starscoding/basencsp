<%@ include file="/common/base.jsp"%>
<script src="${jslib}/echarts/echarts.js"></script>
<script type="text/javascript">
	require.config({
	    paths: {
	    	'echarts': jslib+'/echarts',
	    	'jquery' : jslib+'/jquery-lib/jquery.min',
			'scripts' : eastcom.baseURL + '/scripts',
			'eui' : jslib+'/eui/0.1',
			'text' : jslib+'/requireJs/plugins/text'//text plugin
	    }
	});
</script>