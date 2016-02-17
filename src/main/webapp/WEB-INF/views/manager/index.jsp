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
		<link rel="stylesheet" type="text/css" href="styles/shared/shared-styles.css">
		<link rel="stylesheet" type="text/css" href="styles/manager/index.css">
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
	</head>
	<body><a name = 'pageTop'></a>
		<jsp:include page="../shared/header.jsp" />
		<jsp:include page="../shared/leftMenu.jsp" >
				<jsp:param value="Home" name="menuItemsList"/>
				<jsp:param value="Requests" name="menuItemsList"/>
				<jsp:param value="History" name="menuItemsList"/>
				<jsp:param value="Reports" name="menuItemsList"/>
				<jsp:param value="images/home_image.png" name="menuItemsImage"/>
				<jsp:param value="images/requests_image.png" name="menuItemsImage"/>
				<jsp:param value="images/history_image.png" name="menuItemsImage"/>
				<jsp:param value="images/report_image.png" name="menuItemsImage"/>
				<jsp:param value="Home" name="activeMenuItem"/>
				<jsp:param value="manager" name="menuItemsLink"/>
				<jsp:param value="manager/requests" name="menuItemsLink"/>
				<jsp:param value="manager/history" name="menuItemsLink"/>
				<jsp:param value="manager/reports" name="menuItemsLink"/>
				<jsp:param value="3" name="noOfMenuItems"/>
			</jsp:include>
		<div id = 'pageContent'>
			<jsp:include page="../shared/pageContentTitleBar.jsp" >
				<jsp:param value="Manager Panel" name="panelName"/>
			</jsp:include>
			<div id = 'mainContent'>
				<!-- TODO Show manager the following
							1. His team status
								a) Employees on leave today
								b) Employees on WFH today
				 -->
				<div id = 'teamStatus'>
					<div id = 'employeesOnLeave'>
						<div class = 'panelHeading'>
							<span>Employees on leave today</span>
						</div>
						<div class = 'panelData'>					
							<c:forEach var = "employee" items = "${listOfTeamMembersOnLeaveToday}" >
								<div class = 'row'><span class = 'empCode'>${employee.empCode}</span><span class = 'empName'>${employee.name}</span></div>
							</c:forEach>
						</div>
					</div>
					<div id = 'employeesOnWFH'>
						<div class = 'panelHeading'>
							<span>Employees working from home today</span>
						</div>
						<div class = 'panelData'>					
							<c:forEach var = "employee" items = "${listOfTeamMembersOnWFHToday}" >
								<div class = 'row'><span class = 'empCode'>${employee.empCode}</span><span class = 'empName'>${employee.name}</span></div>
							</c:forEach>
						</div>
					</div>
				</div>
				<div id = 'team'>
					<div class = 'panelHeading'>
						Team Details
					</div>
					<div class = 'panelData'>
						<c:forEach var = "teamMember" items = "${listOfTeamMembers}" >
							<div class = 'row'><span class = 'empCode'>${teamMember.empCode}</span><span class = 'empName'>${teamMember.name}</span></div>
						</c:forEach>
					</div>
				</div>
			</div> 
			<div class = 'clearDiv'></div>			
		</div>
		<jsp:include page="../shared/footer.jsp" />
		<div class = 'clearDiv'></div>
	</body>
</html>