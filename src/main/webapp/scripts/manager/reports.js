function getReport() {
	
	$.getJSON("getReports",{fromDate: $("#fromDate").val(), toDate: $("#toDate").val()}, function(data) {
		
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
   						"	<span class = 'empName'>" + data.empname + "</span>" +
   						"	<span class = 'paidleaves'>" + data.paidleaves + "</span>" +
   						"	<span class = 'lwp'>" + data.lwp + "</span>" +
   						"	<span class = 'compoff'>" + data.compoff + "</span>" +
   						"	<span class = 'compoffgranted'>" + data.grantedcompoff + "</span>" +
   						"	<span class = 'wfh'>" + data.wfh + "</span>" +
   						"</div>";
       		
   		});
		$("#report").html(div_data);
		/*$("#report").fadeIn("fast");*/
	}).error(function() { $("#report").html(""); /*$("#report").fadeIn("fast");*/});
}

/*function viewEmployeeDetails(value){
	$.getJSON("viewEmployee",{empCode: value}, function(data) {
		var div_data =  "<div id = 'addBar'><span id = 'backButton' onclick = \"getEmployeesListEffect(\'\')\">Back to View All</span><div class = 'clearDiv'></div></div><div id = 'info'>";
		div_data =  div_data + 
						"<div class = 'tableHeaderRow'>" +
						"	<span class = 'heading'>Employee Details</span></div>";
						"	<span class = 'name'>Name</span>" +
						"	<span class = 'manager'>Manager</span>" +
						"	<span class = 'status'>Status</span>" + 
						"	<span class = 'email'>Email</span>" + 
						"	<span class = 'edit'>&nbsp;</span>" + 
						"	<span class = 'delete'>&nbsp;</span>" +
						"</div>"; 
		
		$.each(data.employees, function(i,data)	{
       		div_data = div_data + 
       					"<div class = 'tableRow'>"+
   						"	<span class = 'label'>Employee Code</span>" +
   						"	<span class = 'value'>" + data.id + "</span>" +
   						"</div>" + 
   						"<div class = 'tableRow'>"+
   						"	<span class = 'label'>Name</span>" +
   						"	<span class = 'value'>" + data.name + "</span>" +
   						"</div>" +
   						"<div class = 'tableRow'>"+
   						"	<span class = 'label'>Manager Code</span>" +
   						"	<span class = 'value'>" + data.managerCode + "</span>" +
   						"</div>"+
   						"<div class = 'tableRow'>"+
   						"	<span class = 'label'>Manager Name</span>" +
   						"	<span class = 'value'>" + data.mgrname + "</span>" +
   						"</div>"+
   						"<div class = 'tableRow'>"+
   						"	<span class = 'label'>Status</span>" +
   						"	<span class = 'value'>" + data.status + "</span>" +
   						"</div>"+
   						"<div class = 'tableRow'>"+
   						"	<span class = 'label'>email</span>" +
   						"	<span class = 'value'>" + data.email + "</span>" +
   						"</div>"+
   						"<div class = 'tableRow'>"+
   						"	<span class = 'label'>Date of Joining</span>" +
   						"	<span class = 'value'>" + data.doj + "</span>" +
   						"</div>"+
   						"<div class = 'tableRow'>"+
   						"	<span class = 'label'>Total leaves</span>" +
   						"	<span class = 'value'>" + data.leavesTotal + "</span>" +
   						"</div>"+
   						"<div class = 'tableRow'>"+
   						"	<span class = 'label'>Leaves remaining</span>" +
   						"	<span class = 'value'>" + data.leavesRemaining + "</span>" +
   						"</div>"+
   						"<div class = 'tableRow'>"+
   						"	<span class = 'label'>Additional Roles</span>" +
   						"	<span class = 'value'>" + data.roles + "</span>" +
   						"</div>";
   						
       		
   		});
   	
		div_data = div_data + 
						"</div>";
		$("#dynamicArea").html(div_data);
		$("#dynamicArea").fadeIn("fast");
	}).error(function() { $("#dynamicArea").html(""); $("#dynamicArea").fadeIn("fast");});
}

function viewEmployeeDetailsEffect(value) {
	$("#dynamicArea").fadeOut("fast", function(){viewEmployeeDetails(value);});
}

function getEmployeesListEffect(value) {
	$("#dynamicArea").fadeOut("fast", function(){getEmployeesList(value);});
}*/