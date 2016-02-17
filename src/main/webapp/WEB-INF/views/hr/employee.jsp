<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><fmt:message key="application-name" /> | Employee List</title>
		<link rel="stylesheet" type="text/css" href="../styles/shared/shared-styles.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/tables.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/buttons.css">
		<link rel="stylesheet" type="text/css" href="../styles/hr/employees.css">
		<!-- <link rel="stylesheet" type="text/css" href="../styles/hr/common.css"> -->
		<script src="../scripts/jquery-1.8.3.js"></script>
		<script src="../scripts/employees.js"></script>
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
		<style type="text/css">
			.tableRow,
			.tableHeaderRow {
				width: 1040px;
			}
			
			.tableRow {
				height: 35px;
			}
			
			.tableRow span {
				height: 35px;
				line-height: 35px;
			}
			
			.tableHeaderRow .id, 
			.tableRow .id {
				width: 70px;
				border-left: none;
			}
			
			a {
				text-decoration: none;
			}
			
			#info .heading {
				border-left: none;
			}
			
			.tableHeaderRow .label, 
			.tableRow .label {
				width: 200px;
				border-left: none;
			}
			
		</style>
	</head>
	<body onload="getEmployeesList('')"><a name = 'pageTop'></a>
		<jsp:include page="../shared/header.jsp" >
			<jsp:param value="../" name="pathDepth"/>
		</jsp:include>
		<jsp:include page="../shared/leftMenu.jsp" >
				<jsp:param value="Home" name="menuItemsList"/>
				<jsp:param value="Employees" name="menuItemsList"/>
				<jsp:param value="Reports" name="menuItemsList"/>
				<jsp:param value="Calender" name="menuItemsList"/>
				<jsp:param value="Cancellations" name="menuItemsList"/>
				<jsp:param value="../images/home_image.png" name="menuItemsImage"/>
				<jsp:param value="../images/employees_image.png" name="menuItemsImage"/>
				<jsp:param value="../images/report_image.png" name="menuItemsImage"/>
				<jsp:param value="../images/calender_image.png" name="menuItemsImage"/>
				<jsp:param value="../images/cancellations_image.png" name="menuItemsImage"/>
				<jsp:param value="../hr" name="menuItemsLink"/>
				<jsp:param value="../hr/employees" name="menuItemsLink"/>
				<jsp:param value="../hr/reports" name="menuItemsLink"/>
				<jsp:param value="../hr/holidays" name="menuItemsLink"/>
				<jsp:param value="../hr/cancellations" name="menuItemsLink"/>
				<jsp:param value="Employees" name="activeMenuItem"/>
				<jsp:param value="4" name="noOfMenuItems"/> 
			</jsp:include>
			<!-- Make this 1 less than actual value -->

			<div id = 'pageContent'>
			<jsp:include page="../shared/pageContentTitleBar.jsp" >
				<jsp:param value="../" name="brandLogoPath"/>
				<jsp:param value="HR Panel" name="panelName"/>
			</jsp:include>
			<div id = 'mainContent'>
				<c:if test="${not empty param.errorCode}">
					<div id = "message">
						<c:if test="${param.errorCode == 1}">
							You can't delete this employee. You may set his status as resigned.
						</c:if>
					</div>
				</c:if>
				<div id = 'filterBox'>
					<input type = "text" value = "Search by employee name, code or manager name" onkeyup = "getEmployeesList(this.value)" onfocus = "setContent(this.value)" onblur = "setContent(this.value)"/>
					<div class = 'clearDiv'></div>
				</div>
				<div id = 'dynamicArea'></div>
				<!--<div id = 'addBar'>
					<a href = "addEmployee">New</a>
					<div class = 'clearDiv'></div>
				</div>
				<div id = 'list'> -->
					<!-- This content will be replaced by content from AJAX calls 
				<div class = 'clearDiv'></div>
				</div>-->
			</div> 
			<div class = 'clearDiv'></div>			
			</div>
		<jsp:include page="../shared/footer.jsp" />
		<div class = 'clearDiv'></div>
	</body>
</html>