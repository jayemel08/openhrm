function confirmPwd() {
	if(document.forms["changePassword"]["newPassword"].value.length < 8) {
		document.getElementById("passwordErrorMsg").innerHTML = "At least 8 characters";
		return false;
	}
	
	if(document.forms["changePassword"]["newPassword"].value != document.forms["changePassword"]["confirmPassword"].value) {
		document.getElementById("passwordMatchErrorMsg").innerHTML = "Passwords do not match";
		return false;
	}
}