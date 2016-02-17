function getEmployeeReport() {
	var completeData = "";
	$.getJSON("getReport",{type: "0", fromDate: $("#fromDate").val(), toDate: $("#toDate").val()}, function(data) {
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
		$.getJSON("getReport",{type: "3", fromDate: $("#fromDate").val(), toDate: $("#toDate").val()}, function(data) {
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
			$.getJSON("getReport",{type: "2", fromDate: $("#fromDate").val(), toDate: $("#toDate").val()}, function(data) {
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
				/*$("#employeeReports").fadeIn("fast");*/
			}).error(function() { /*$("#employeeReports").html(""); $("#employeeReports").fadeIn("fast");*/});
			/*$("#employeeReports").fadeIn("fast");*/
		}).error(function() { /*$("#employeeReports").html(""); $("#employeeReports").fadeIn("fast");*/});
		/*$("#employeeReports").fadeIn("fast");*/
	}).error(function() { $("#employeeReports").html(""); /*$("#employeeReports").fadeIn("fast");*/});
	
	
}