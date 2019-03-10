<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.lang.management.*"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>

<!DOCTYPE html>
<html>
<head>
<title>Jvm</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//Get JVM's system info
RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
Date startDate = new Date( bean.getStartTime()); //启动时间  
long upTime = (bean.getUptime()/1000/60/60);  //运行时长
String jvmName = bean.getName();            //JVM名称
// Get JVM Thread Info
ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
int threadCount = threadBean.getThreadCount();
int daemonThreadCount =threadBean.getDaemonThreadCount();
int peakThreadCount =threadBean.getPeakThreadCount();
long totalStartedThreadCount =threadBean.getTotalStartedThreadCount();

// The java runtime Memory
Runtime runtime = Runtime.getRuntime();
double freeMemory = (double) runtime.freeMemory() / (1024 * 1024);
double totalMemory = (double) runtime.totalMemory() / (1024 * 1024);
double usedMemory = totalMemory - freeMemory;
double percentFree = ((double) freeMemory / (double) totalMemory);
%>
</head>
<body>
	JVM信息<br/>
	<%="startDate: " + sdf.format(startDate) %> ---启动时间<br/>
	<%="upTime: " + upTime %> ---运行时长<br/>
	<%="jvmName: " + jvmName %> ---JVM名称<br/>
	<%="threadCount: " + threadCount %> ---活动线程数目包括守护线程和非守护线程<br/>
	<%="daemonThreadCount: " + daemonThreadCount %> ---活动守护线程数<br/>
	<%="peakThreadCount: " + peakThreadCount %> ---从Java虚拟机启动或峰值重置以来峰值活动线程计数<br/>
	<%="totalStartedThreadCount: " + totalStartedThreadCount %> ---从Java虚拟机启动以来创建和启动的线程总数<br/>
	<%="freeMemory: " + freeMemory %> ---空闲内存量(M)<br/>
	<%="totalMemory: " + totalMemory %> ---总内存量(M)<br/>
	<%="usedMemory: " + usedMemory %> ---已使用内存量(M)<br/>
	<%="percentFree: " + percentFree %> ---空闲内存占比<br/>	
	<%=percentFree <= 0.4 ? "JVM Memory used more than 60%":"" %><br/>	
</body>
</html>