

function checkAvailability() {
	$.getJSON("mvc/availability", { name: $('#name').val() }, function(availability) {
		if (availability.available) {
			fieldValidated("name", { valid : true });
		} else {
			fieldValidated("name", { valid : false, message : $('#name').val() + " is not available, try " + availability.suggestions });
		}
	});
}