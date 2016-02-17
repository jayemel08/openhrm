<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><fmt:message key="application-name" /> | Employee List</title>
		<link rel="stylesheet" type="text/css" href="../styles/shared/shared-styles.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="../styles/hr/editEmployee.css">
		<script src="../scripts/jquery-1.8.3.js"></script>
		<script src="../scripts/jquery-ui.js"></script>
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
		<script>
		function setDOJ(date) {
			$( "#datePicker" ).datepicker();
			$( "#datePicker" ).datepicker( "option", "dateFormat", "DD, MM dd, yy" );
			$( "#datePicker" ).datepicker("setDate", date);
		}
		</script>
	</head>
	<body onload="setDOJ('${updatedEmployee.doj}')">
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
				<form:form modelAttribute="updatedEmployee" method="POST" action="editEmployee">
					<form:label path="empCode">EmpID</form:label>
					<form:input path="empCode" readonly="true" />
					<form:errors class = 'errorSpan' path="empCode" />						
					
					<form:label path="name">Name</form:label>
					<form:input path="name" />
					<form:errors class = 'errorSpan' path="name" />						
					
					<form:label path="email">Email</form:label>
					<form:input path="email" />
					<form:errors class = 'errorSpan' path="email" />
				
					<div class = 'radioButtonsDiv'>
						<form:label path="statusCode">Status</form:label>
						<c:forEach items="${statuses}" var="stat">
							<form:radiobutton class = 'radioButton' path="statusCode" value="${stat.id}" label="${stat.description}" />
						</c:forEach>
					</div>						
					
					<%-- <form:label path="mgrCode">Manager</form:label>
					<form:input path="mgrCode" />
					<form:errors class = 'errorSpan' path="mgrCode" /> --%>
					<form:label path="mgrCode">Manager</form:label>
					<form:select path = "mgrCode" style = "height: 40px; width: 492px;">
						<form:option value="" label = "No Manager"></form:option>
					<c:forEach var = "manager" items = "${listOfManagers}">
						<form:option value="${manager.empCode}" label = "${manager.empCode} - ${manager.name}"></form:option>
					</c:forEach>
					</form:select>
						
					<form:label path="doj">Date of Joining</form:label>
					<form:input path="doj" id="datePicker" readonly="true" />	
					
					
					<form:label path="leavesTotal">Total Leaves Allowed</form:label>
					<form:input path="leavesTotal" />
					<form:errors class = 'errorSpan' path="leavesTotal" />
					
					<div class = 'checkBoxesDiv'>
						<form:label path="roles">Additional Roles</form:label>
						<form:checkboxes class = 'checkBoxes' items="${additionalRoles}" path="roles" />
					</div>
					
					<input id = 'submit' type="submit" value="Update" />				
				</form:form>
			</div> 
			<div class = 'clearDiv'></div>			
			</div>
		<jsp:include page="../shared/footer.jsp" />
		<div class = 'clearDiv'></div>
	</body>
</html>