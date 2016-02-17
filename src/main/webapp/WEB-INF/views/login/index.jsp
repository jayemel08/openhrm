<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><fmt:message key="application-name" /> | Login</title>
		<link rel="stylesheet" type="text/css" href="styles/shared/shared-styles.css">
		<link rel="stylesheet" type="text/css" href="styles/shared/buttons.css">
		<link rel="stylesheet" type="text/css" href="styles/login/common.css">
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
	</head>
	<body>
		<div id='loginFormContainer'>
			<img id = 'logo' src = 'images/brand-logo.png' height = 40/>
			<h3>Employee Portal</h3>
			<div id='loginForm'>
				<form name='f' action="j_spring_security_check" method='POST'>
					<div class = 'inputBar'>
					<img src = 'images/empcode.png' />
					<input type='text' name='j_username' value='' />
					<div class = 'clear'></div>
				</div>
				<div class = 'inputBar'>
				<img src = 'images/password.png' />
					<input type='password' name='j_password' />
					<div class = 'clear'></div>
					</div> 
					<c:if test="${not empty param.error}">
						<div id = 'errorBlock'>
							Invalid employee code or password
						</div>
					</c:if>
					<a id = 'forgotPass' href = 'forgotPassword'>Forgot Password?</a>
					<div id = 'buttons'>
						<input id = 'submit'  class = "button" name='submit' type="submit" value="Sign In" />
						
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