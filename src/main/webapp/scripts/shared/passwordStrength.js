//Password Strength Checker: Hardware Geeks

function checkPwdStrength(x) {
	
	//alert("captured");
	if(x.length == 0) 
		document.getElementById("passwordErrorMsg").innerHTML = "";
	else if (x.length < 8) {
		document.getElementById("passwordErrorMsg").innerHTML = "Atleast 8 characters";
		document.getElementById("passwordErrorMsg").style.color = "#C01F2F";
	}
	else {
		if(hasLowerCase(x) && hasUpperCase(x) && hasSpecialChars(x) && hasNumbers(x)) {
			document.getElementById("passwordErrorMsg").innerHTML = "very strong";
			document.getElementById("passwordErrorMsg").style.color = "#91CE50";
		}
		else if ((hasLowerCase(x) && hasUpperCase(x) && hasNumbers(x)) ||
				 (hasLowerCase(x) && hasUpperCase(x) && hasSpecialChars(x)) ||
				 (hasLowerCase(x) && hasSpecialChars(x) && hasNumbers(x)) ||
				 (hasUpperCase(x) && hasSpecialChars(x) && hasNumbers(x)) ||
				 (x.length > 16)
				) {
			document.getElementById("passwordErrorMsg").innerHTML = "strong";
			document.getElementById("passwordErrorMsg").style.color = "#91CE50";
		}
		else if ((hasLowerCase(x) && hasUpperCase(x)) ||
				 (hasLowerCase(x) && hasSpecialChars(x)) ||
				 (hasSpecialChars(x) && hasUpperCase(x)) ||
				 (hasLowerCase(x) && hasNumbers(x)) ||
				 (hasNumbers(x) && hasUpperCase(x)) ||
				 (hasNumbers(x) && hasSpecialChars(x)) ||
				 (x.length > 10)
				) {
			document.getElementById("passwordErrorMsg").innerHTML = "weak";
			document.getElementById("passwordErrorMsg").style.color = "#C01F2F";
		}
		else {
			document.getElementById("passwordErrorMsg").innerHTML = "very weak";
			document.getElementById("passwordErrorMsg").style.color = "#C01F2F";
		}
	}
}

function hasLowerCase(value) {
	var charList = "abcdefghijklmnopqrstuvwxyz";
	for(var i = 0; i < value.length; i++)
		if(charList.indexOf(value.charAt(i)) != -1)
			return true;
	return false;
}

function hasUpperCase(value) {
	var charList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	for(var i = 0; i < value.length; i++)
		if(charList.indexOf(value.charAt(i)) != -1)
			return true;
	return false;
}

function hasSpecialChars(value) {
	var charList = "~`!@#$%^&*()_-+=|\{}[]\"\':;?/>.<,";
	for(var i = 0; i < value.length; i++)
		if(charList.indexOf(value.charAt(i)) != -1)
			return true;
	return false;
}

function hasNumbers(value) {
	var charList = "1234567890";
	for(var i = 0; i < value.length; i++)
		if(charList.indexOf(value.charAt(i)) != -1)
			return true;
	return false;
}