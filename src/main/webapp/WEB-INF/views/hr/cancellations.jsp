<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><fmt:message key="application-name" /> | Employee List</title>
		<link rel="stylesheet" type="text/css" href="../styles/shared/shared-styles.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/tables.css">
		<link rel="stylesheet" type="text/css" href="../styles/hr/cancellations.css">
		<script src="../scripts/jquery-1.8.3.js"></script>
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
		<style type="text/css">
			.tableRow,
			.tableHeaderRow {
				width: 1040px;
			}
			
			.tableRow span {
				height: 40px;
				line-height: 40px;
			}
			
			.tableRow {
				height: 40px;
			}
			
			.tableHeaderRow .id,
			.tableRow .id {
				border-left: none;
				width: 100px;
			}
			
		</style>
		<script>
			function cancel(row, empCode, type){
				if(confirm("Confirm Delete?") == false) {
					return false;
				}
				$.post("cancel", { id:empCode, type: type },
						function(data){
							if(data != 'Done')
								alert(data);
							$(row).siblings(".status").html("Cancelled");
							$(row).html("&nbsp;");
			  	});
			}
		</script>
	</head>
	<body><a name = 'pageTop'></a>
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
				<jsp:param value="Cancellations" name="activeMenuItem"/>
				<jsp:param value="4" name="noOfMenuItems"/> 
			</jsp:include>
			<!-- Make this 1 less than actual value -->

			<div id = 'pageContent'>
				<jsp:include page="../shared/pageContentTitleBar.jsp" >
					<jsp:param value="../" name="brandLogoPath"/>
					<jsp:param value="HR Panel" name="panelName"/>
				</jsp:include>
				<div id = 'mainContent'>
					<c:if test="${not empty message}">
						<div id = "message">
							${message}
						</div>
					</c:if>
					<div id = 'form'>
						<form name = "cancellationForm" method="POST" action="cancellations">
						<label>Employee Code</label>
						<input type = "text" name = "empCode" />
						<label>Application ID</label>
						<input type = "text" name = "applicationId" />
						<input id = "submit" type = "submit"/>
						</form>
						
					</div>
					<div id = 'dynamicArea'>
						<div class = 'tableHeaderRow'>
							<span class = 'id'>ID</span>
							<span class = 'date'>Date</span>
							<span class = 'type'>Type</span>
							<span class = 'status'>Status</span>
							<span class = 'delete'>&nbsp;</span>
						</div>
						<c:if test="${not empty data}">
							<c:forEach var = "item" items="${data}">
								<div class = 'tableRow'>
									<span class = 'id'>${item[0]}</span>
									<span class = 'date'>${item[1]}</span>
									<span class = 'type'>${item[2]}</span>
									<span class = 'status'>${item[3]}</span>
									<c:choose>
										<c:when test="${item[3] != 'Cancelled' }">
											<span class = 'delete' onclick="cancel(this, ${item[0]}, ${item[4]})">
												<img src="../images/delete.jpg" height = 20 /> 
											</span>
										</c:when>
										<c:otherwise>
											<span class = 'delete'>
												&nbsp; 
											</span>
										</c:otherwise>
									</c:choose>
								</div>
							</c:forEach>
						</c:if>
					</div>
				</div> 
				<div class = 'clearDiv'></div>			
			</div>
		<jsp:include page="../shared/footer.jsp" />
		<div class = 'clearDiv'></div>
	</body>
</html>