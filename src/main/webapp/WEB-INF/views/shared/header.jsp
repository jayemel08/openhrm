<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div id='header'>		
	<a id = 'logoutButton' href = "${param.pathDepth}j_spring_security_logout">Log out</a>
	<a href = '#'>Welcome,&nbsp;&nbsp;<span>${sessionScope.username}</span></a>
	<sec:authorize access="hasRole('ROLE_MANAGER')">
		<a href = '${param.pathDepth}manager'>Manager Control Panel</a>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_HR')">
		<a href = '${param.pathDepth}hr'>HR Control Panel</a>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_EMPLOYEE')">
		<a href = '${param.pathDepth}employee'>Employee Panel</a>
	</sec:authorize>
	<div class = 'clearDiv'></div>
</div>