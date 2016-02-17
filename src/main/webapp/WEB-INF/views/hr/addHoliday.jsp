<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><fmt:message key="application-name" /> | HR Panel</title>
		<link rel="stylesheet" type="text/css" href="../styles/shared/shared-styles.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/buttons.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/message.css">
		<link rel="stylesheet" type="text/css" href="../styles/hr/addCalenderLeaves.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/jquery-ui.css">
		<script src="http://code.jquery.com/jquery-1.8.3.js"></script>
		<script src="http://code.jquery.com/ui/1.10.0/jquery-ui.js"></script>
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
		<style type="text/css">
			.button {
				width: 100px;
				margin-left: 240px;
				margin-top: 20px;
			}
		</style>
		<script>
			$(function() {
			  $( "#datePicker" ).datepicker();
			  $( "#datePicker" ).datepicker( "option", "dateFormat", "DD, MM dd, yy" );
			});
		</script>
	</head>
	<body>
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
				
				<c:if test="${not empty param.message }">
					<div id = "message">
						${param.message}
					</div>
				</c:if>
				
				<h3>Add a new holiday</h3>
				<form:form modelAttribute="newCalenderLeave" method="POST" action="addHoliday">
				
					<form:label path="date">Date</form:label>
					<form:input path="date" id = "datePicker" readonly = "true"/>
					<form:errors class = 'errorSpan' path = "date" />
				
					<form:label path="description">Description</form:label>
					<form:input path="description" />
					<form:errors class = 'errorSpan' path = "description" />
				          
				    <input id = 'submit' type="submit" value="Submit" class = "button" />
					    
				</form:form>
			</div> 
			<div class = 'clearDiv'></div>			
		</div>
		<jsp:include page="../shared/footer.jsp" />
		<div class = 'clearDiv'></div>
	</body>
</html>