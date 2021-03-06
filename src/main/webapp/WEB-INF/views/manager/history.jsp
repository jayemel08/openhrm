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
		<link rel="stylesheet" type="text/css" href="../styles/manager/common.css">
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
	</head>
	<body><a name = 'pageTop'></a>
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
				<jsp:param value="History" name="activeMenuItem"/>
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
				<div id = 'applicationsList'>
					<div class = 'tableHeaderRow'>
						<span class = 'id'>ID</span>
						<span class = 'empName'>Employee Name</span>
						<span class = 'fromToDate'>From/To</span>
					<!-- 	<span class = 'toDate'>To</span> -->
						<span class = 'appliedOnDate'>Applied On</span> 
						<span class = 'applicationReason'>Application Reason</span> 
						<span class = 'type'>Type</span>
						<span class = 'status'>Status</span>
					</div> 
					<c:forEach items = "${list}" var = "application">					
						<div class = 'tableRow'>
   							<span class = 'id'>${application.id}</span>
   							<span class = 'empName'>${application.empName}</span>
   							<span class = 'fromToDate'>${application.fromDate} to ${application.toDate}</span>
   							<span class = 'appliedOnDate'>${application.appliedOn}</span>
   							<span class = 'applicationReason'>${application.applicationReason}</span>   							
   							<span class = 'type'>${application.applicationType}</span>
   							<span class = 'status'>${application.applicationStatus}</span>
   							<%-- <span class = 'delete'><a href='deleteApplication?id=${application.id}&redirect=L'><img src = '../images/delete.jpg' height = 20 title = 'Delete' /></a></span> --%>
   							<div class = 'clear'></div>
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