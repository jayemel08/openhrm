<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><fmt:message key="application-name" /> | Manager Panel</title>
		<link rel="stylesheet" type="text/css" href="../styles/shared/shared-styles.css">
		<link rel="stylesheet" type="text/css" href="../styles/manager/reports.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/jquery-ui.css">
		<script src="../scripts/jquery-1.8.3.js"></script>
		<script src="../scripts/jquery-ui.js"></script>
		<script src="../scripts/manager/reports.js"></script>
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
		<script>
			$(function() {
			  $( "#fromDate" ).datepicker({
				    dateFormat: "DD, MM dd, yy",
				    maxDate: 0
				});
			  $( "#fromDate" ).datepicker('setDate', -30);
			  $( "#toDate" ).datepicker({
				    dateFormat: "DD, MM dd, yy",
				    maxDate: 0
				});
			  $( "#toDate" ).datepicker('setDate', new Date());
			});
		</script>
	</head>
	<body onload = "getReport()"><a name = 'pageTop'></a>
		<jsp:include page="../shared/header.jsp" >
			<jsp:param value="../" name="pathDepth"/>
		</jsp:include>
		<jsp:include page="../shared/leftMenu.jsp" >
				<jsp:param value="Home" name="menuItemsList"/>
				<jsp:param value="Requests" name="menuItemsList"/>
				<jsp:param value="History" name="menuItemsList"/>
				<jsp:param value="Reports" name="menuItemsList"/>
				<jsp:param value="../images/home_image.png" name="menuItemsImage"/>
				<jsp:param value="../images/requests_image.png" name="menuItemsImage"/>
				<jsp:param value="../images/history_image.png" name="menuItemsImage"/>
				<jsp:param value="../images/report_image.png" name="menuItemsImage"/>
				<jsp:param value="Reports" name="activeMenuItem"/>
				<jsp:param value="../manager" name="menuItemsLink"/>
				<jsp:param value="../manager/requests" name="menuItemsLink"/>
				<jsp:param value="../manager/history" name="menuItemsLink"/>
				<jsp:param value="../manager/reports" name="menuItemsLink"/>
				<jsp:param value="3" name="noOfMenuItems"/>
			</jsp:include>
		<div id = 'pageContent'>
			<jsp:include page="../shared/pageContentTitleBar.jsp" >
				<jsp:param value="Manager Panel" name="panelName"/>
				<jsp:param value="../" name="brandLogoPath"/>
			</jsp:include>
			<div id = 'mainContent'>
				<div id = 'dateSelector'>
					<label for="fromDate">From</label>
					<input id = 'fromDate' type = "text" onchange = 'getReport()' readonly="readonly" />
					<label for="toDate">To</label>
					<input id = 'toDate' type = "text" onchange = 'getReport()' readonly="readonly" />
				</div>
				<div id = 'report'>
					<div class = 'tableHeaderRow'>
						<span class = 'id'>ID</span>
						<span class = 'empName'>Employee Name</span>
						<span class = 'paidleaves'>Paid Leaves</span>
						<span class = 'lwp'>LWPs</span> 
						<span class = 'compoff'>CompOff Leaves</span> 
						<span class = 'compoffgranted'>CompOffs Granted</span>
						<span class = 'wfh'>Work from home</span>
					</div> 
					<c:forEach items = "${list}" var = "row">					
						<div class = 'tableRow'>
   							<span class = 'id'>${row.key}</span>
   							<span class = 'empName'>${row.value[0]}</span>
   							<span class = 'paidleaves'>${row.value[1]}</span>
   							<span class = 'lwp'>${row.value[3]}</span>
   							<span class = 'compoff'>${row.value[2]}</span>
   							<span class = 'compoffgranted'>${row.value[4]}</span>
   							<span class = 'wfh'>${row.value[5]}</span>
   						</div>
					</c:forEach>
					<div class = 'clearDiv'></div>
				</div>
			</div> 
			<div class = 'clearDiv'></div>			
		</div>
		<jsp:include page="../shared/footer.jsp" />
		<div class = 'clearDiv'></div>
	</body>
</html>