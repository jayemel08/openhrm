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
		<link rel="stylesheet" type="text/css" href="../styles/hr/reports.css">
		<!-- <link rel="stylesheet" type="text/css" href="../styles/manager/reports.css"> -->
		<script src="../scripts/jquery-1.8.3.js"></script>
		<script src="../scripts/hr/reports.js"></script>
		<link rel="stylesheet" type="text/css" href="../styles/shared/jquery-ui.css">
		<script src="../scripts/jquery-ui.js"></script>
		<link rel="shortcut icon" href="<c:url value = "/favicon.ico" />" />
		<style type="text/css">
			#employeeReports {
				/* position: relative; */
				width: 900px;
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
			
		/* 	#leaves,
			#wfh,
			#compOffsGranted {
				position: absolute;
				left: 0;
				top: 0;
			} */
			
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
				margin-top: 20px;
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
			
			.tableHeaderRow .id,
			.tableRow .id {
				border-left: none;
			}
			
			#employeeReports .tableHeaderRow {
				width: 900px;
			}
			
			#employeeReports .tableRow {
				width: 900px;
				height: 40px;
			}
			
			#employeeReports .date,
			#employeeReports .type,
			#employeeReports .status {
				width: 280px;
			}
			
			.exportButton {
				float: right;
				width: 150px;
				text-decoration: none;
				text-align: right;
				color: #484848;
				padding-right: 20px;
			}
			
			.exportButton:hover {
				color: green;
			}
			
			#backButton {
				margin-left: auto;
				margin-right: auto;
				float: none;
				width: 150px;
			}
			
		</style>
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
				$("#consolidatedReportTrigger span").click(function(){
					if($("#consolidatedReports").css("display") == "none") {
						$("#consolidatedReportTrigger img").attr("src", "../images/downArrow.png");
					}
					else {
						$("#consolidatedReportTrigger img").attr("src", "../images/rightArrow.png");
					}
					$("#consolidatedReports").slideToggle("medium");
				});
				$("#employeeReportTrigger span").click(function(){
					if($("#employeeReports").css("display") == "none") {
						$("#employeeReportTrigger img").attr("src", "../images/downArrow.png");
					}
					else {
						$("#employeeReportTrigger img").attr("src", "../images/rightArrow.png");
					}
				  	$("#employeeReports").slideToggle("medium");
				});
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
	</head>
	<body onload = 'getConsolidatedReport("")'>
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
				<jsp:param value="Reports" name="activeMenuItem"/>
				<jsp:param value="4" name="noOfMenuItems"/> 
			</jsp:include>
			<!-- Make this 1 less than actual value -->

			<div id = 'pageContent'>
			<jsp:include page="../shared/pageContentTitleBar.jsp" >
				<jsp:param value="../" name="brandLogoPath"/>
				<jsp:param value="HR Panel" name="panelName"/>
			</jsp:include>
			
			<div id = 'dateSelector'>
				<label for="fromDate">From</label>
				<input id = 'fromDate' type = "text" onchange = 'getConsolidatedReportEffect(); getEmployeeReport();' readonly="readonly"/>
				<label for="toDate">To</label>
				<input id = 'toDate' type = "text" onchange = 'getConsolidatedReportEffect(); getEmployeeReport();' readonly="readonly"/>
			</div>
			
			<div id = 'mainContent'>
				<div id = 'consolidatedReportTrigger' >
					<img src = "../images/downArrow.png" />
					<span>Consolidated Reports</span>
					<a class = "exportButton" href = "">Export to excel</a>
				</div>
				<div id = 'consolidatedReports' style = "display: none;"></div>
				<div id = 'employeeReportTrigger'>
					<img src = "../images/downArrow.png" />
					<span>Employee Reports</span>
					<a class = "exportButton" href = "">Export to excel</a>
				</div>
				<input id = 'empCode' type = "text" onkeyup="getEmployeeReport();" />
				<div id = "tabs">
					<div id = "leavesTab" class = "button" onclick = "switchTabs('leaves')">Leaves</div>
					<div id = "wfhTab" class = "button" onclick = "switchTabs('wfh')">Work from home</div>
					<div id = "compOffsGrantedTab" class = "button" onclick = "switchTabs('compOffsGranted')">Comp Offs granted</div>
					<div class = 'clearDiv'></div>
				</div>
				<div id = 'employeeReports'></div>
				<div class = 'clearDiv'></div>	
			</div> 
			<div class = 'clearDiv'></div>			
		</div>
		<jsp:include page="../shared/footer.jsp" />
		<div class = 'clearDiv'></div>
	</body>
</html>