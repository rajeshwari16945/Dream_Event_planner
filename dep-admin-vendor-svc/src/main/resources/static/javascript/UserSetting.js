$(document).ready(function() {
	let intervalSetter = setInterval(function () {
		let user = JSON.parse(sessionStorage.getItem("user"));
		if (user) {
			clearInterval(intervalSetter); // Stop checking once data is available
			if(user.name) $("#name").val(user.name);
			if(user.partnerName) $("#pname").val(user.partnerName);
			if(user.address) $("#address").val(user.address);
			$("#email").val(user.email);
			$("#emailid").val(user.email);
			//handle update button 
			$("#Update").on("click", function() { updateUser(user); });
			$("#UpdatePswd").on("click", function() { updateUserPassword(user); });
		}
	}, 1000);
});

async function updateUser(user) {
	event.preventDefault();
	let name = $("#name").val();
	let partnerName = $("#pname").val();
	let address = $("#address").val();
	let email = $("#email").val();
	let validationSuccess = 0;
	if(name.trim()) validationSuccess++; 
	if(partnerName.trim()) validationSuccess++;
	if(address.trim()) validationSuccess++;
	if (validationSuccess > 0) {
		const requestData = {
			id: user.id,
            name: name,
            partnerName: partnerName,
            address: address,
			email: email
        };
       try {
           const response = await fetch('/user/update', {
			method: 'POST',
	        headers: { 'Content-Type': 'application/json' },
	        body: JSON.stringify(requestData)
           });
           const data = await response.json();
           if (data.success) {
			   location.reload();
           } else {
               alert("Update Failed.");
           }
       } catch (error) {
           console.log(error);
           alert(error.message);
       }
   }
}

async function updateUserPassword(user) {
	event.preventDefault();
	let password = $("#password").val();
	let confirmPassword = $("#confirm-password").val();
	let validationSuccess = 0;
	if(!password.trim()) document.getElementById("error-password").innerHTML = "This Field is required.";
	else if(!isValidPassword(password)) document.getElementById("error-password").innerHTML = "This Field is not valid.";
	else validationSuccess++;
	
	if(!confirmPassword.trim()) document.getElementById("error-confirm-password").innerHTML = "This Field is required.";
	else if(password != confirmPassword) document.getElementById("error-confirm-password").innerHTML = "Doesn't match.";
	else validationSuccess++;
	if (validationSuccess == 2) {
		const requestData = {
			id: user.id,
            password: password,
            confirmPassword: confirmPassword
        };
       try {
           const response = await fetch('/user/update-password', {
			method: 'POST',
	        headers: { 'Content-Type': 'application/json' },
	        body: JSON.stringify(requestData)
           });
           const data = await response.json();
           if (data.success) {
			   location.reload();
           } else {
               alert("Update Failed.");
           }
       } catch (error) {
           console.log(error);
           alert(error.message);
       }
   }
}

function isValidPassword(password) {
    // Regular expression for password validation
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    // Test the password against the regex
    return passwordRegex.test(password);
}