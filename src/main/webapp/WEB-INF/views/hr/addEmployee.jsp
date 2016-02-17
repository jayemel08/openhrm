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
		<link rel="stylesheet" type="text/css" href="../styles/shared/message.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="../styles/hr/addEmployee.css">
		<script src="../scripts/jquery-1.8.3.js"></script>
		<script src="../scripts/jquery-ui.js"></script>
		<script src="../scripts/hr/addEmployee.js"></script>
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
		<script>
			function setDOJ(date) {
				$( "#datePicker" ).datepicker();
				$( "#datePicker" ).datepicker( "option", "dateFormat", "DD, MM dd, yy" );
				$( "#datePicker" ).datepicker("setDate", date);
			}
		</script>
	</head>
	<body onload="setDOJ('${newEmployee.doj}')">
		<div id = "progressOverlay"><img src = '../images/loading.gif' /></div>
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

			<div id = "pageContent">
				<jsp:include page="../shared/pageContentTitleBar.jsp" >
					<jsp:param value="../" name="brandLogoPath"/>
					<jsp:param value="HR Panel" name="panelName"/>
				</jsp:include>
				
					<c:choose>
					<c:when test="${param.status == 'success'}">
							<div id = "message">Employee has been added successfully</div>
					</c:when> 
					<c:when test="${param.status == 'mail_error'}">
							<div id = "message">Employee has been added successfully. However the welcome mail could not be sent.</div>
					</c:when> 
					<c:otherwise>
						
					</c:otherwise>
					</c:choose>
				
				<div id = 'mainContent'>				
					<h3>Add new employee</h3>
					<form:form modelAttribute="newEmployee" 
								method="POST" action="addEmployee" 
								onsubmit = '$("#progressOverlay").css("display", "block")'>
					
						<form:label path="empCode">EmpID</form:label>
						<form:input path="empCode" />
						<form:errors class = 'errorSpan' path="empCode"></form:errors>
		
						
						<form:label path="name">Name</form:label>
						<form:input path="name" />
						<form:errors class = 'errorSpan' path="name"></form:errors>
		
						
						<form:label path="email">Email</form:label>
						<form:input path="email" />
						<form:errors class = 'errorSpan' path="email"></form:errors>
						
						<div class = 'radioButtonsDiv'>
							<form:label path="statusCode">Status</form:label>
							<c:forEach items="${statuses}" var="stat">
								<form:radiobutton class = 'radioButton' path="statusCode" value="${stat.id}" label="${stat.description}" />
							</c:forEach>
						</div>
						
<%-- 						
						<form:label path="mgrCode">Manager</form:label>
						<input type = "text" id = 'mgrName' />
						<form:hidden id = "mgrCode" path="mgrCode" readonly="readonly" />
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
						<form:errors class = 'errorSpan' path="doj" />
		
						
						<form:label path="leavesTotal">Total Leaves Allowed</form:label>
						<form:input path="leavesTotal" />
						<form:errors class = 'errorSpan' path="leavesTotal" />
		
						<div class = 'checkBoxesDiv'>
							<form:label path="roles">Additional Roles</form:label>
							<form:checkboxes class = 'checkBoxes' items="${additionalRoles}" path="roles" />
							<div class = 'clearDiv'></div>
						</div>
						<input id = "submit" type="submit" value="Submit" />
						<div class = 'clearDiv'></div>	
					</form:form>
				</div> 
				<div class = 'clearDiv'></div>			
			</div>
		<jsp:include page="../shared/footer.jsp" />
		<div class = 'clearDiv'></div>
	</body>
</html>