function getConsolidatedReport() {
	$.getJSON("consolidated",{fromDate: $("#fromDate").val(), toDate: $("#toDate").val()}, function(data) {
		
		var div_data =  "<div class = 'tableHeaderRow'>" +
						"	<span class = 'id'>ID</span>" +
						"	<span class = 'empName'>Name</span>" +
						"	<span class = 'paidleaves'>Paid leaves</span>" +
						"	<span class = 'lwp'>LWP</span>" + 
						"	<span class = 'compoff'>Comp Off</span>" + 
						"	<span class = 'compoffgranted'>Comp Off Granted</span>" + 
						"	<span class = 'wfh'>Work from home</span>" +
						"</div>"; 
				$.each(data.employees, function(i,data)	{
				div_data = div_data + 
							"<div class = 'tableRow'>"+
							"	<span class = 'id'>" + data.id + "</span>" +
							"	<span class = 'empNameWithLink' onclick = 'getTeamwiseReportEffect(" + data.id + ")' >" + data.empname + "</span>" +
							"	<span class = 'paidleaves'>" + data.paidleaves + "</span>" +
							"	<span class = 'lwp'>" + data.lwp + "</span>" +
							"	<span class = 'compoff'>" + data.compoff + "</span>" +
							"	<span class = 'compoffgranted'>" + data.grantedcompoff + "</span>" +
							"	<span class = 'wfh'>" + data.wfh + "</span>" +
							"</div>";
   		});
		div_data = div_data + "</div>";
		$("#consolidatedReports").html(div_data);
		$("#consolidatedReports").fadeIn("fast");
		$("#consolidatedReportTrigger a").attr("href", "consolidatedReport?fromDate=" + $("#fromDate").val() + "&toDate=" + $("#toDate").val());
	}).error(function() { $("#consolidatedReports").html(""); $("#consolidatedReports").fadeIn("fast");});
}

function getTeamwiseReport(mgrCode){
	$.getJSON("teamwise",{mgrCode: mgrCode, fromDate: $("#fromDate").val(), toDate: $("#toDate").val()}, function(data) {
		
		var div_data =  "<div id = 'backButton' class = \"button\" onclick = \"getConsolidatedReportEffect()\">Back to View All</div>" + 
						"<div class = 'tableHeaderRow'>" +
						"	<span class = 'id'>ID</span>" +
						"	<span class = 'empName'>Name</span>" +
						"	<span class = 'paidleaves'>Paid leaves</span>" +
						"	<span class = 'lwp'>LWP</span>" + 
						"	<span class = 'compoff'>Comp Off</span>" + 
						"	<span class = 'compoffgranted'>Comp Off Granted</span>" + 
						"	<span class = 'wfh'>Work from home</span>" +
						"</div>"; 
		
		$.each(data.employees, function(i,data)	{
       		div_data = div_data + 
		       			"<div class = 'tableRow'>"+
						"	<span class = 'id'>" + data.id + "</span>" +
						"	<span class = 'empName'>" + data.empname + "</span>" +
						"	<span class = 'paidleaves'>" + data.paidleaves + "</span>" +
						"	<span class = 'lwp'>" + data.lwp + "</span>" +
						"	<span class = 'compoff'>" + data.compoff + "</span>" +
						"	<span class = 'compoffgranted'>" + data.grantedcompoff + "</span>" +
						"	<span class = 'wfh'>" + data.wfh + "</span>" +
						"</div>";
   		});
   	
		div_data = div_data + 
						"</div></div>";
		$("#consolidatedReports").html(div_data);
		$("#consolidatedReports").fadeIn("fast");
		$("#consolidatedReportTrigger a").attr("href", "teamWiseReport?mgrCode=" + mgrCode + "&fromDate=" + $("#fromDate").val() + "&toDate=" + $("#toDate").val());
	}).error(function() { $("#consolidatedReports").html(""); $("#consolidatedReports").fadeIn("fast");});
}

function getTeamwiseReportEffect(mgrCode) {
	$("#consolidatedReports").fadeOut("fast", function(){getTeamwiseReport(mgrCode);});
}

function getConsolidatedReportEffect() {
	fromDate = new Date($("#fromDate").val());
	toDate = new Date($("#toDate").val());
	if(fromDate.getTime() > toDate.getTime()) {
		$( "#fromDate" ).datepicker('setDate', toDate);
		alert("\"From\" date must be less than or equal to \"To\" date");
		return false;
	}
	$("#consolidatedReports").fadeOut("fast", function(){getConsolidatedReport();});
}

function getEmployeeReport() {
	var completeData = "";
	$.getJSON("employeeWise",{type: "0", empCode: $("input#empCode").val(), fromDate: $("#fromDate").val(), toDate: $("#toDate").val()}, function(data) {
		var div_data =  "<div id = \"leaves\">" +
						"<div class = 'tableHeaderRow'>" +
						"	<span class = 'date'>Date</span>" +
						"	<span class = 'type'>Type</span>" +
						"	<span class = 'status'>Status</span>" +
						"</div>"; 
		$.each(data.details, function(i,data) {
		
		div_data = div_data + 
					"<div class = 'tableRow'>"+
					"	<span class = 'date'>" + data.date + "</span>" +
					"	<span class = 'type'>" + data.type + "</span>" +
					"	<span class = 'status'>" + data.status + "</span>" +
					"</div>";
       		
   		});
		div_data = div_data + 
						"</div>";
		completeData = completeData + div_data;
		$.getJSON("employeeWise",{type: "3", empCode: $("input#empCode").val(), fromDate: $("#fromDate").val(), toDate: $("#toDate").val()}, function(data) {
			var div_data =  "<div id = \"wfh\">" +
							"<div class = 'tableHeaderRow'>" +
							"	<span class = 'date'>Date</span>" +
							"	<span class = 'type'>Type</span>" +
							"	<span class = 'status'>Status</span>" +
							"</div>"; 
					$.each(data.details, function(i,data)	{
					
					div_data = div_data + 
								"<div class = 'tableRow'>"+
								"	<span class = 'date'>" + data.date + "</span>" +
								"	<span class = 'type'>" + data.type + "</span>" +
								"	<span class = 'status'>" + data.status + "</span>" +
								"</div>";
	       		
	   		});
			div_data = div_data + 
							"</div>";
			completeData = completeData + div_data;
			$.getJSON("employeeWise",{type: "2", empCode: $("input#empCode").val(), fromDate: $("#fromDate").val(), toDate: $("#toDate").val()}, function(data) {
				var div_data =  "<div id = \"compOffsGranted\">" +
								"<div class = 'tableHeaderRow'>" +
								"	<span class = 'date'>Date</span>" +
								"	<span class = 'type'>Type</span>" +
								"	<span class = 'status'>Status</span>" +
								"</div>"; 
						$.each(data.details, function(i,data)	{
						
						div_data = div_data + 
									"<div class = 'tableRow'>"+
									"	<span class = 'date'>" + data.date + "</span>" +
									"	<span class = 'type'>" + data.type + "</span>" +
									"	<span class = 'status'>" + data.status + "</span>" +
									"</div>";
		       		
		   		});
				div_data = div_data + 
								"</div>";
				completeData = completeData + div_data + "<div class = 'clearDiv'></div>";
				$("#employeeReports").html(completeData);
				switchTabs('leaves');
				$("#employeeReportTrigger a").attr("href", "employeeWiseReport?empCode=" + $("input#empCode").val() + "&fromDate=" + $("#fromDate").val() + "&toDate=" + $("#toDate").val());
				/*$("#employeeReports").fadeIn("fast");*/
			}).error(function() { /*$("#employeeReports").html(""); $("#employeeReports").fadeIn("fast");*/});
			/*$("#employeeReports").fadeIn("fast");*/
		}).error(function() { /*$("#employeeReports").html(""); $("#employeeReports").fadeIn("fast");*/});
		/*$("#employeeReports").fadeIn("fast");*/
	}).error(function() { $("#employeeReports").html(""); /*$("#employeeReports").fadeIn("fast");*/});
	
	
}