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
		<link rel="stylesheet" type="text/css" href="../styles/shared/buttons.css">
		<link rel="stylesheet" type="text/css" href="../styles/shared/tables.css">
		<link rel="stylesheet" type="text/css" href="../styles/employee/reports.css">
		<script src="../scripts/jquery-1.8.3.js"></script>
		<script src="../scripts/employee/reports.js"></script>
		<link rel="stylesheet" type="text/css" href="../styles/shared/jquery-ui.css">
		<script src="../scripts/jquery-ui.js"></script>
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
		<script>
			$(function() {
				$( "#fromDate" ).datepicker({
				    dateFormat: "DD, MM dd, yy",
				    maxDate: 0
				});
				$( "#fromDate" ).datepicker('setDate', -30);
				$( "#toDate" ).datepicker({
				    dateFormat: "DD, MM dd, yy",
				    maxDate: 0
				});
				$( "#toDate" ).datepicker('setDate', new Date());
			});
			function switchTabs(div) {
				switch(div) {
				case "leaves" :
					$("#leaves").fadeIn("fast");
					$("#wfh").fadeOut("fast");
					$("#compOffsGranted").fadeOut("fast");
					$("#leavesTab").html("&laquo; Leaves &raquo;");
					$("#wfhTab").html("Work from home");
					$("#compOffsGrantedTab").html("Comp Offs granted");
					break;
				case "wfh" :
					$("#wfh").fadeIn("fast");
					$("#leaves").fadeOut("fast");
					$("#compOffsGranted").fadeOut("fast");
					$("#leavesTab").html("Leaves");
					$("#wfhTab").html("&laquo; Work from home &raquo;");
					$("#compOffsGrantedTab").html("Comp Offs granted");
					break;
				case "compOffsGranted" :
					$("#compOffsGranted").fadeIn("fast");
					$("#wfh").fadeOut("fast");
					$("#leaves").fadeOut("fast");
					$("#leavesTab").html("Leaves");
					$("#wfhTab").html("Work from home");
					$("#compOffsGrantedTab").html("&laquo; Comp Offs granted &raquo;");
					break;
				}
			}
		</script>
		<style>
			#employeeReports {
				position: relative;
				width: 1040px;
				margin-left: auto;
				margin-right: auto;
			}
			
			#tabs {
				height: 50px;
				width: 680px;
				margin-left: auto;
				margin-right: auto;
				margin-top: 20px;
				margin-bottom: 10px;
			}
			
			#leaves,
			#wfh,
			#compOffsGranted {
				position: absolute;
				left: 0;
				top: 0;
			}
			
			#leavesTab,
			#wfhTab,
			#compOffsGrantedTab {
				width: 160px;
				margin-right: 35px;
				/* float: left;
				
				font: 15px Ubuntu;
				background: # */
			}
			
			#compOffsGrantedTab {
				margin-right: 0px;
			}
			
			.tableHeaderRow {
				width: 1040px;
			}
			
			.tableRow {
				width: 1040px;
				height: 40px;
			}
			
			.tableHeaderRow .date,
			.tableRow .date {
				border-left: none;
			}
			
			.tableRow span {
				height: 40px;
				line-height: 40px;
			}
			
		</style>
	</head>
	<body onload = "getEmployeeReport()">
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
			<jsp:param value="Reports" name="activeMenuItem"/>
			<jsp:param value="../" name="menuItemsLink"/>
			<jsp:param value="../employee/leaves" name="menuItemsLink"/>
			<jsp:param value="../employee/workFromHome" name="menuItemsLink"/>
			<jsp:param value="../employee/compensatoryOff" name="menuItemsLink"/>
			<jsp:param value="../employee/reports" name="menuItemsLink"/>
			<jsp:param value="4" name="noOfMenuItems"/>
		</jsp:include>

			<div id = 'pageContent'>
			<jsp:include page="../shared/pageContentTitleBar.jsp" >
				<jsp:param value="../" name="brandLogoPath"/>
				<jsp:param value="Employee Panel" name="panelName"/>
			</jsp:include>
			
			<div id = 'dateSelector'>
				<label for="fromDate">From</label>
				<input id = 'fromDate' type = "text" onchange = 'getEmployeeReport();' readonly="readonly"/>
				<label for="toDate">To</label>
				<input id = 'toDate' type = "text" onchange = 'getEmployeeReport();' readonly="readonly"/>
			</div>
			
			<div id = 'mainContent'>
				<div id = "tabs">
					<div id = "leavesTab" class = "button" onclick = "switchTabs('leaves')">Leaves</div>
					<div id = "wfhTab" class = "button" onclick = "switchTabs('wfh')">Work from home</div>
					<div id = "compOffsGrantedTab" class = "button" onclick = "switchTabs('compOffsGranted')">Comp Offs granted</div>
					<div class = 'clearDiv'></div>
				</div>
				<div id = 'employeeReports'>
				</div>
			</div> 
			<div class = 'clearDiv'></div>			
		</div>
		<jsp:include page="../shared/footer.jsp" />
		<div class = 'clearDiv'></div>
	</body>
</html>