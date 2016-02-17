<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><fmt:message key="application-name" /> | Change Password</title>
		<link rel="stylesheet" type="text/css" href="../styles/shared/shared-styles.css">
		<link rel="stylesheet" type="text/css" href="../styles/employee/changePassword.css">
		<link rel="shortcut icon" href='<c:url value = "/favicon.ico" />' />
		<script src="../scripts/shared/passwordStrength.js"></script>
		<script src="../scripts/employee/changePassword.js"></script>
	</head>
	<body><a name = 'pageTop'></a>
		<jsp:include page="../shared/header.jsp" >
			<jsp:param value="../" name="pathDepth"/>
		</jsp:include>
		<jsp:include page="../shared/leftMenu.jsp" >
			<jsp:param value="Home" name="menuItemsList"/>
			<jsp:param value="Leave" name="menuItemsList"/>
			<jsp:param value="Work from home" name="menuItemsList"/>
			<jsp:param value="Comp Off" name="menuItemsList"/>
			<jsp:param value="Reports" name="menuItemsList"/>
			<jsp:param value="../images/home_image.png" name="menuItemsImage"/>
			<jsp:param value="../images/leave_image.png" name="menuItemsImage"/>
			<jsp:param value="../images/work_from_home_image.png" name="menuItemsImage"/>
			<jsp:param value="../images/comp_off_image.png" name="menuItemsImage"/>
			<jsp:param value="../images/report_image.png" name="menuItemsImage"/>
			<jsp:param value="" name="activeMenuItem"/>
			<jsp:param value="../" name="menuItemsLink"/>
			<jsp:param value="../employee/leaves" name="menuItemsLink"/>
			<jsp:param value="../employee/workFromHome" name="menuItemsLink"/>
			<jsp:param value="../employee/compensatoryOff" name="menuItemsLink"/>
			<jsp:param value="../employee/reports" name="menuItemsLink"/>
			<jsp:param value="4" name="noOfMenuItems"/>
		</jsp:include>
		<div id = 'pageContent'>
			<jsp:include page="../shared/pageContentTitleBar.jsp" >
				<jsp:param value="Employee Panel" name="panelName"/>
				<jsp:param value="../" name="brandLogoPath"/>
			</jsp:include>
			<div id = 'mainContent'>
				
				<h3>Change password</h3>
				<form name = 'changePassword' method="POST" action="changePassword" onsubmit = "return confirmPwd()">
			        <label for="oldPassword">Current Password</label>
			        <input type = "password" name="oldPassword" />
			        <span id = "wrongPassword">
				        <c:if test="${not empty message}">
							${message}
						</c:if>
					</span>
			    
			        <label for="newPassword">New Password</label>
			        <input type = "password" name="newPassword" onkeyup = "checkPwdStrength(this.value)"/>
			        <span id = "passwordErrorMsg"></span>
			    
			        <label for = "confirmPassword">Confirm Password</label>
			        <input type = "password" id = "confirmPassword" name = "confirmPassword" />
			        <span id = "passwordMatchErrorMsg"></span>
		            <input id = "submit" type="submit" value="Submit"/>
				</form>
			</div> 
			<div class = 'clearDiv'></div>			
		</div>
		<jsp:include page="../shared/footer.jsp" />
		<div class = 'clearDiv'></div>
	</body>
</html>