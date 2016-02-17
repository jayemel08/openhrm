<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Open HRM | Page not found</title>
		<link rel="stylesheet" type="text/css" href="<c:url value = '/styles/shared/shared-styles.css' />" />
		<link rel="shortcut icon" href="<c:url value="/favicon.ico" />" />
		<style>
			#message img {
				height: 150px;
				margin-left: auto; 
				margin-right: auto; 
				display: block;
				margin-top: 100px;
			}
			
			html, body {
				min-height: 450px;
				margin-bottom: 0px;
			}
			
			#message span, #message a {
				display: block;
				font: 25px Ubuntu;
				color: grey;
				text-align: center;
				width: 100%;
				margin-top: 50px;
			}
			
			#message a {
				text-decoration: none;
				margin-top: 20px;
			}
			
			#message a:hover {
				color: black;
			}
			
		</style>
	</head>
	<body>
		<div id = "message">
			<img src = '<c:url value="/images/brand-logo.png" />' />
			<span>We are sorry. The page you are trying to view does not exist.</span>
			<a href = "<c:url value = '/' />" >Take me home</a>
		</div>
		<jsp:include page="../shared/footer.jsp" />
	</body>
</html>