jQuery.fn.myFunction = function() {
	$("#div").getJSON("http://localhost:8081/yourpresenter/mvc/schedule/", function(schedule) {});
	return alert("som tu");
};

jQuery.fn.testFunction() = function() {
	/*$(document).getJSON("http://localhost:8081/yourpresenter/mvc/schedule", function(schedule) {});*/
	return alert("som tu");
};

