<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><fmt:message key="application-name" /> | HR Panel</title>
		<link rel="stylesheet" type="text/css" href="styles/shared/shared-styles.css">
		<link rel="stylesheet" type="text/css" href="styles/hr/home.css">
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
	</head>
	<body><a name = 'pageTop'></a>
		<jsp:include page="../shared/header.jsp" />
		<jsp:include page="../shared/leftMenu.jsp" >
				<jsp:param value="Home" name="menuItemsList"/>
				<jsp:param value="Employees" name="menuItemsList"/>
				<jsp:param value="Reports" name="menuItemsList"/>
				<jsp:param value="Calender" name="menuItemsList"/>
				<jsp:param value="Cancellations" name="menuItemsList"/>
				<jsp:param value="images/home_image.png" name="menuItemsImage"/>
				<jsp:param value="images/employees_image.png" name="menuItemsImage"/>
				<jsp:param value="images/report_image.png" name="menuItemsImage"/>
				<jsp:param value="images/calender_image.png" name="menuItemsImage"/>
				<jsp:param value="images/cancellations_image.png" name="menuItemsImage"/>
				<jsp:param value="" name="menuItemsLink"/>
				<jsp:param value="hr/employees" name="menuItemsLink"/>
				<jsp:param value="hr/reports" name="menuItemsLink"/>
				<jsp:param value="hr/holidays" name="menuItemsLink"/>
				<jsp:param value="hr/cancellations" name="menuItemsLink"/>
				<jsp:param value="Home" name="activeMenuItem"/>
				<jsp:param value="4" name="noOfMenuItems"/> 
			</jsp:include>			
			<!-- Make this 1 less than actual value -->
		<div id = 'pageContent'>
			<jsp:include page="../shared/pageContentTitleBar.jsp" >
				<jsp:param value="" name="brandLogoPath"/>
				<jsp:param value="HR Panel" name="panelName"/>
			</jsp:include>
			<div id = 'mainContent'>
				<div id = 'employeesOnLeave'>
					<div class = 'panelHeading'>
						<span>Employees on Leave Today</span>
					</div>
					<div class = 'panelData' >
						<c:forEach var = "emp" items="${leaveList}">
							<div class = 'row'>
							<div class = 'empId'>
								${emp.empCode}
							</div>
							<div class = 'empName'>
								${emp.name} 
							</div>
						</div>
						</c:forEach>
					</div>
				</div>
				<div id = 'employeeInfo'>
					<div class = 'panelHeading'>
						<span>Information</span>
					</div>
					<div class = 'panelData'>
						<div class = 'row'>Total employees: ${total}</div>
						<div class = 'row'>Employees on Roles: ${onRoles}</div>
						<div class = 'row'>Employees in Notice Period: ${onNotice}</div>
						<div class = 'row'>Employees in Probation Period: ${onProbation}</div>
					</div>
				</div>
				<div id = 'employeesOnWFH'>
					<div class = 'panelHeading'>
						<span>Employees on Work From Home Today</span>
					</div>
					<div class = 'panelData' >
						<c:forEach var = "emp" items="${wfhList}">
							<div class = 'row'>
							<div class = 'empId'>
								${emp.empCode}
							</div>
							<div class = 'empName'>
								${emp.name} 
							</div>
						</div>
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