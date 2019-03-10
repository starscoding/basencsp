<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	response.setHeader("X-UA-Compatible", "IE=edge");
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/lib.jsp"%>
<script type="text/javascript">
	function sync() {
		eastcom.syncIframeHeight();
	}
	var t = false;
	function tonggle() {
		eastcom.tonggleLeftTree(t, function() {
			t = !t;
		});
	}

	function tab() {
		eastcom.createTab({
			id : 'test',
			nameCn : '111',
			location : '/pages/a.html',
			closable : true
		})
	}

	function closeTab() {
		eastcom.closeTab()
	}

	function getCurrent() {
		//var tab = eastcom.getEastcom().getCurrentTab();
		var tab = eastcom.getEastcom().getTab('ff8080814a73d370014a75dca78c0000');
		tab.callIframeFn('tonggle')
		console.log(tab.getWindowObj());
		console.log(tab);
	}
</script>
</head>
<body style="background: #fff;">

	<div style="height: 1500px">
		<button onclick="sync()">sync</button>
		<button onclick="tab()">add tab</button>
		<button onclick="closeTab()">close myself</button>
		<button onclick="getCurrent()">getCurrent</button>
		<button onclick="tonggle()">tonggle</button>
	</div>

</body>
</html>