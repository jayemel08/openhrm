function getEmployeesList(value) {
	$.getJSON("listEmployees",{query: value}, function(data) {
		
		var div_data =  "<div id = 'addBar'><a class = \"button\" href = \"addEmployee\">New</a>"+
						"<div class = 'clearDiv'></div></div><div id = 'list'>";
		div_data = div_data + "<div class = 'tableHeaderRow'>" +
						"	<span class = 'id'>ID</span>" +
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
   						"	<span class = 'id'>" + data.id + "</span>" +
   						"	<span class = 'name' onclick = 'viewEmployeeDetailsEffect(" + data.id + ")' >" + data.name + "</span>" +
   						"	<span class = 'manager'>" + data.mgrname + "</span>" +
   						"	<span class = 'status'>" + data.status + "</span>" +
   						"	<span class = 'email'>" + data.email + "</span>" +
   						"	<span class = 'edit'><a href='editEmployee?id=" + data.id + "'><img src = '../images/edit.jpg' height = 20 title = 'Edit' /></a></span>" +
   						"	<span class = 'delete'><a href='deleteEmployee?id=" + data.id + "' onclick = 'return confirm(\"Confirm Delete?\")'><img src = '../images/delete.jpg' height = 20 title = 'Delete' /></a></span>" +
   						"</div>";
       		
   		});
		div_data = div_data + 
						"</div>";
		$("#dynamicArea").html(div_data);
		$("#dynamicArea").fadeIn("fast");
	}).error(function() { $("#dynamicArea").html("<div id = 'addBar'><a class = \"button\" href = \"addEmployee\">New</a>"+
						"<div class = 'clearDiv'></div></div>"); $("#dynamicArea").fadeIn("fast");});
}

function viewEmployeeDetails(value){
	$.getJSON("viewEmployee",{empCode: value}, function(data) {
		var div_data =  "<div id = 'addBar'><span class = \"button\" id = 'backButton' onclick = \"getEmployeesListEffect(\'\')\">Back to View All</span><div class = 'clearDiv'></div></div><div id = 'info'>";
		div_data =  div_data + 
						"<div class = 'tableHeaderRow'>" +
						"	<span class = 'heading'>Employee Details</span></div>";
						/*"	<span class = 'name'>Name</span>" +
						"	<span class = 'manager'>Manager</span>" +
						"	<span class = 'status'>Status</span>" + 
						"	<span class = 'email'>Email</span>" + 
						"	<span class = 'edit'>&nbsp;</span>" + 
						"	<span class = 'delete'>&nbsp;</span>" +
						"</div>"; */
		
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
	}).error(function() { $("#dynamicArea").html("<div id = 'addBar'><span class = \"button\" id = 'backButton' onclick = \"getEmployeesListEffect(\'\')\">Back to View All</span><div class = 'clearDiv'></div>"); $("#dynamicArea").fadeIn("fast");});
}

function viewEmployeeDetailsEffect(value) {
	$("#dynamicArea").fadeOut("fast", function(){viewEmployeeDetails(value);});
}

function getEmployeesListEffect(value) {
	$("#dynamicArea").fadeOut("fast", function(){getEmployeesList(value);});
}

function setContent(value) {
	if(value == '') {
		$("#filterBox input").val("Search by employee name, code or manager name");
	}
	else if(value == 'Search by employee name, code or manager name') {
		$("#filterBox input").val("");
	}
}