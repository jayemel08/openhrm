<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><fmt:message key="application-name" /> | Leaves</title>
		<link rel="stylesheet" type="text/css" href="../styles/shared/shared-styles.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/buttons.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/message.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/tables.css">
		<link rel="stylesheet" type="text/css" href="../styles/employee/common.css">
		<script src="../scripts/jquery-1.8.3.js"></script>
  		<script src="../scripts/jquery-ui.js"></script>
  		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
  		<style>
			.tableHeaderRow,
			.tableRow {
				width: 1040px;
			}
		</style>
  		<script>
  			function checkIfErrors(value) {
  				if(value != null) {
  					$("#newApplicationArea").css("display", "block");
  					
  				}
  				else{
  					$("#newApplicationArea").css("display", "none");
  				}
  			}
			$(function() {
				$( "#datePicker1" ).datepicker();
				$( "#datePicker2" ).datepicker();
				
				$( "#datePicker1" ).datepicker( "option", "dateFormat", "DD, dd MM, yy");
				$( "#datePicker2" ).datepicker( "option", "dateFormat", "DD, dd MM, yy" );
				$("#newApplicationTrigger").click(function(){
				    $("#newApplicationArea").slideToggle("medium");
			  	});
			});
			function setDates(fromDate, toDate) {
				$( "#datePicker1" ).datepicker( "setDate", fromDate );
				$( "#datePicker2" ).datepicker( "setDate", toDate );
			}
		</script>
	</head>
	<body onload = "checkIfErrors(${q}); setDates('${newApplication.fromDate}', '${newApplication.toDate}')">
		<div id = "progressOverlay"><img src = '../images/loading.gif' /></div>
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
			<jsp:param value="Leave" name="activeMenuItem"/>
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
				<c:if test="${not empty param.message}">
					<div id = "message">
						${param.message}
					</div>
				</c:if>
				<div id = 'newApplicationTrigger'>New Application</div>
				<div id = 'newApplicationArea'>
					<form:form name = "newLeaveApplicationForm" 
								modelAttribute="newApplication" 
								method="POST" action="leaves" 
								onsubmit = '$("#progressOverlay").css("display", "block")'>
						<form:errors id = "errorDiv" path = "*" element="div"></form:errors>
						<div id = 'leaveType'>
							<form:label path="applicationType" >Leave Type</form:label>
							<form:select path="applicationType" items="${leaveTypes}" itemLabel="description" itemValue="id"></form:select>
							<div class = 'clearDiv'></div>
			    		</div>
			    		
			    		<div id = 'date'>
					        <form:label path="fromDate">From</form:label>
					        <form:input name = "fromDate" path="fromDate" id = "datePicker1" readonly="true"/>		
					        
					        <form:label path="toDate">To</form:label>
				        	<form:input path="toDate" id = "datePicker2" readonly="true" />			   		
			    		</div>
			    	
			   			<div id = 'reason'>
				        	<form:label path="applicationReason">Reason</form:label>
					        <form:textarea path = "applicationReason" />				     
				   			<%-- <form:errors class = 'errorSpan' path="applicationReason" /> --%>
				   			<div class = 'clearDiv'></div>
			   			</div>
			   					    
			    	    <input id = "submit" type="submit" value="Submit" class = "button"/>
				    	
					</form:form>
					<div class = 'clearDiv'></div>
				</div>
				<div id = 'leaveApplicationsList'>
					<div class = 'tableHeaderRow'>
						<span class = 'id'>ID</span>
						<span class = 'date'>From/To</span>
						<span class = 'date'>Applied On</span> 
						<span class = 'applicationReason'>Application Reason</span> 
						<span class = 'rejectionReason'>Rejection Reason</span> 
						<span class = 'date'>Ack. On</span>
						<span class = 'type'>Type</span>  
						<span class = 'status'>Status</span> 
						<span class = 'delete'>&nbsp;</span>
					</div> 
					<c:forEach items = "${applicationsList}" var = "application">
						<div class = 'tableRow'>
   							<span class = 'id'>${application.id}</span>
   							<span class = 'date'>${application.fromDate} to ${application.toDate}</span>
   							<span class = 'date'>${application.appliedOn}</span>
   							<span class = 'applicationReason'>${application.applicationReason}</span>   							
   							<span class = 'rejectionReason'>${application.rejectReason}</span>
   							<span class = 'date'>${application.acknowledgedOn}</span>
   							<span class = 'type'>${application.applicationType}</span>
   							<span class = 'status'>${application.applicationStatus}</span>
   							<c:choose>
	   							<c:when test="${application.applicationStatus == 'Pending'}">
	   								<span class = 'delete'><a href='deleteApplication?id=${application.id}&redirect=L' onclick = 'return confirm("Confirm Delete?")'><img src = '../images/delete.jpg' height = 20 title = 'Delete' /></a></span>
	   							</c:when>
	   							<c:otherwise>
	   								<span class = 'delete'>&nbsp;</span>
	   							</c:otherwise>
   							</c:choose>
   							<div class = 'clear'></div>
   						</div>
					</c:forEach>
					<div class = 'clearDiv'></div>
				</div>
			</div> 
			<div class = 'clearDiv'></div>			
		</div>
		<jsp:include page="../shared/footer.jsp" />
		<div class = 'clearDiv'></div>
	</body>
</html>