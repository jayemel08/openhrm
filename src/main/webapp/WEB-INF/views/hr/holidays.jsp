<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><fmt:message key="application-name" /> | HR Panel</title>
		<link rel="stylesheet" type="text/css" href="../styles/shared/shared-styles.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/tables.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/message.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/buttons.css">
		<link rel="stylesheet" type="text/css" href="../styles/hr/calenderLeaves.css">
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
		<style type="text/css">
			.tableRow {
				height: 35px;
			}
			
			.tableRow span {
				height: 35px;
				line-height: 35px;
			}
			
			.tableRow,
			.tableHeaderRow {
				width: 600px;
			}
			
			a {
				text-decoration: none;
			}
		</style>
	</head>
	<body><a name = 'pageTop'></a>
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
				<jsp:param value="Calender" name="activeMenuItem"/>
				<jsp:param value="4" name="noOfMenuItems"/> 
			</jsp:include>			
			<!-- Make this 1 less than actual value -->
		<div id = 'pageContent'>
			<jsp:include page="../shared/pageContentTitleBar.jsp" >
				<jsp:param value="../" name="brandLogoPath"/>
				<jsp:param value="HR Panel" name="panelName"/>
			</jsp:include>
			<div id = 'mainContent'>
				<c:if test="${not empty param.message}">
					<div id = "message">
						${param.message}
					</div>
				</c:if>
				<div id = 'addBar'>
					<a href = "addHoliday" class = "button">New</a>
					<div class = 'clearDiv'></div>
				</div>
				<div id = 'list'>
					<div class = 'tableHeaderRow'>
						<span class = 'date'>Date</span>
						<span class = 'description'>Description</span>
						<span class = 'delete'>&nbsp;</span>
					</div>
					<c:choose>
						<c:when test="${not empty listOfCalenderLeaves }">
						
							<c:forEach var="leave" items="${listOfCalenderLeaves}">
							<div class = 'tableRow'>
								<span class = 'date'>${leave.date}</span>
								<span class = 'description'> ${leave.description}</span>
								<span class = 'delete'>
								<a href="deleteHoliday?id=${leave.id}" onclick = 'return confirm("Confirm Delete?")'>
									<img src="../images/delete.jpg">
								</a></span>
							</div>
							</c:forEach>
							
						</c:when>
						<c:otherwise>
							
						</c:otherwise>
					</c:choose>
				</div>
			</div> 
			<div class = 'clearDiv'></div>			
		</div>
		<jsp:include page="../shared/footer.jsp" />
		<div class = 'clearDiv'></div>
	</body>
</html>