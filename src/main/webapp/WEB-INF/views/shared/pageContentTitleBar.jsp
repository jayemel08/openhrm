<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div id = 'headerBar'>
	<div id = 'brandLogo'><img src = '${param.brandLogoPath}images/brand-logo.png' /></div>
	
	<div id = 'empCode'>Employee Code: <sec:authentication property="principal.username" /></div>
	<div id = 'title'>${param.panelName}</div>
</div>