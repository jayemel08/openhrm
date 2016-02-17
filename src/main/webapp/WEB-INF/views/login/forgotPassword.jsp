<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><fmt:message key="application-name" /> | Forgot password</title>
		<link rel="stylesheet" type="text/css" href="styles/shared/shared-styles.css">
		<link rel="stylesheet" type="text/css" href="styles/shared/buttons.css">
		<link rel="stylesheet" type="text/css" href="styles/login/common.css">
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
		<style>
			#buttonsDiv {
				margin-right: 35px;
				margin-bottom: 30px;
			}
		</style>
	</head>
	<body>
		<div id='forgotPasswordFormContainer'>
			<img id = 'logo' src = 'images/brand-logo.png' height = 40/>
			<div id='forgotPasswordForm'>
				<form name='forgotPasswordForm' action="forgotPassword" method='POST'>
					<div class = 'inputBar'>
						<img src = 'images/empcode.png' />
						<input type='text' name = 'empCode' value='' />
						<div class = 'clear'></div>
					</div>
					<c:if test="${not empty message}">
						<div id = 'errorBlock'>
							${message}
						</div>
					</c:if>
					<div id = "buttonsDiv">
						<input id = 'submit' name='submit' type="submit" class = "button" value="Send email" />
						<div class = 'clearDiv'></div>						
					</div>
				</form>
				
			</div>
			<div class = 'clearDiv'></div>
		</div>
		<div id = 'footer'>
			<div id='copyrightNotice'>
				&copy; 2013, Open HRM. All rights reserved.
			</div>
			<div class = 'clearDiv'></div>
		</div>
	</body>
</html>