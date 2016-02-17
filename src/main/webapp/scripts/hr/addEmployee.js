var managers = new Array();

function loadManagers() {
	$.getJSON("managersList",{}, function(data) {
		$.each(data.managers, function(i,data)	{
				managers.push(data.id + " - " + data.name);
			});
   		});
}