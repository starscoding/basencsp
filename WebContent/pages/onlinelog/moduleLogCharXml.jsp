<%@ page language="java" contentType="text/xml" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<anychart>
	<margin all='0' />
	<charts>
		<chart>
			<data_plot_settings default_series_type='bar'>
				<bar_series group_padding='0.2'>
					<tooltip_settings enabled='true'>
						<format><![CDATA[名称:{%Name}{numDecimals:0,thousandsSeparator:} 
模块操作次数:{%Value}{numDecimals:0}]]></format>
					</tooltip_settings>
				</bar_series>
			</data_plot_settings>
			<chart_settings>
				<chart_background enabled='false' />
				<title>
					<text></text>
				</title>
				<axes>
					<y_axis>
						<title>
							<text>模块操作次数（次）</text>
						</title>
						<labels>
							<format>{%Value}{numDecimals:0}</format>
						</labels>
					</y_axis>
					<x_axis>
						<title>
							<text></text>
						</title>
					</x_axis>
				</axes>
			</chart_settings>
			<data>
				<series name=''>
				<!-- 
				<attributes>
					<attribute name="xtype">${xtype}</attribute>
					<attribute name="timeType">${timeType}</attribute>
				</attributes>
				 -->
				<c:forEach var="item" items="${charDataList }">
					<point name='${item.xvalue}' y='${item.yvalue}'>
						<attributes>
							<attribute name="xvalue">${item.xvalue}</attribute>
						</attributes>
					</point>
				</c:forEach>
				</series>
			</data>
		</chart>
	</charts>
</anychart>
