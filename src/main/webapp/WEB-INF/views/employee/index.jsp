<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><fmt:message key="application-name" /> | Employee Panel</title>
		<link rel="stylesheet" type="text/css" href="styles/shared/shared-styles.css">
		<link rel="stylesheet" type="text/css" href="styles/shared/tables.css">
		<link rel="stylesheet" type="text/css" href="styles/employee/styles.css">
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
		<style>
			#upcomingHolidays .tableRow {
				width: 348px;
				height: 40px;
			}
			
			#upcomingHolidays .tableRow span {
				line-height: 40px;
			}
			
			#upcomingHolidays .tableRow .leaveDate {
				border-left: none;
			}
			
			#details .tableRow {
				min-width: 200px;
				width: auto;
				height: 40px;
			}
			
			#details .tableRow span {
				line-height: 40px;
			}
			
			#details .tableRow .leftColumn {
				border-left: none;
			}
			
		</style>
	</head>
	<body>
		<jsp:include page="../shared/header.jsp" />
		<jsp:include page="../shared/leftMenu.jsp" >
			<jsp:param value="Home" name="menuItemsList"/>
			<jsp:param value="Leave" name="menuItemsList"/>
			<jsp:param value="Work from home" name="menuItemsList"/>
			<jsp:param value="Comp Off" name="menuItemsList"/>
			<jsp:param value="Reports" name="menuItemsList"/>
			<jsp:param value="images/home_image.png" name="menuItemsImage"/>
			<jsp:param value="images/leave_image.png" name="menuItemsImage"/>
			<jsp:param value="images/work_from_home_image.png" name="menuItemsImage"/>
			<jsp:param value="images/comp_off_image.png" name="menuItemsImage"/>
			<jsp:param value="images/report_image.png" name="menuItemsImage"/>
			<jsp:param value="Home" name="activeMenuItem"/>
			<jsp:param value="" name="menuItemsLink"/>
			<jsp:param value="employee/leaves" name="menuItemsLink"/>
			<jsp:param value="employee/workFromHome" name="menuItemsLink"/>
			<jsp:param value="employee/compensatoryOff" name="menuItemsLink"/>
			<jsp:param value="employee/reports" name="menuItemsLink"/>
			<jsp:param value="4" name="noOfMenuItems"/>
		</jsp:include>
		<div id = 'pageContent'>
			<jsp:include page="../shared/pageContentTitleBar.jsp" >
				<jsp:param value="Employee Panel" name="panelName"/>
			</jsp:include>
			<div id = 'mainContent'>
				<div id = 'upcomingHolidays'>					
					<div class = 'panelHeading'>
						Upcoming Holidays
					</div>
					<div class = 'panelData'>
						<c:forEach var = "leave" items = "${listOfCalenderLeaves}" varStatus="counter" >
							<%-- <c:if test="${counter.count%2 == 0}">
								<div class = 'evenRow'><span class = 'leaveDate'>${leave.date}</span>&nbsp;&nbsp;${leave.description}</div>
							</c:if>
							<c:if test="${counter.count%2 == 1}">
								<div class = 'oddRow'><span class = 'leaveDate'>${leave.date}</span>&nbsp;&nbsp;${leave.description}</div>
							</c:if> --%>
							<div class = "tableRow"><span class = 'leaveDate'>${leave.date}</span><span>${leave.description}</span></div>						
						</c:forEach>
					</div>
				</div>
				<div id = 'details'>
					<div class = 'panelHeading'>
						<span>Welcome</span>
						<a href = "employee/changePassword">Change Password</a>
					</div>
					<div class = 'panelData'>
						<div class = 'tableRow'><span class = 'leftColumn'>Name</span><span>${myDetails.name}</span></div>
						<div class = 'tableRow'><span class = 'leftColumn'>email</span><span>${myDetails.email}</span></div>
						<div class = 'tableRow'><span class = 'leftColumn'>Manager</span><span>${myDetails.mgrName}</span></div>
						<div class = 'tableRow'><span class = 'leftColumn'>Available Leaves</span><span>${myDetails.leavesRemaining}</span></div>
						<div class = 'tableRow'><span class = 'leftColumn'>Compensatory Offs</span><span>${compOffLeaveBalance}</span></div>
						
					</div>
				</div>
			</div> 
			<div class = 'clearDiv'></div>			
		</div>
		<jsp:include page="../shared/footer.jsp" />
		<div class = 'clearDiv'></div>
	</body>
</html>