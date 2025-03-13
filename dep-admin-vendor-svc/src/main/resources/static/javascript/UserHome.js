function start() {
	
	document.getElementById("user-signup-submit").addEventListener("click", function(event) {
		document.getElementById("email").addEventListener("input", function(event) { updateError(event.target.value, "error-email"); }, false);
		document.getElementById("password").addEventListener("input", function(event) { updateError(event.target.value, "error-password"); }, false);
		registerUserSave(event);
	}, false);
	
	// Show the login Modal popup
	document.getElementById('loginBtn').addEventListener('click', function () {
	    document.getElementById('loginPopupModal').style.display = 'flex';
		document.getElementById("login-form").reset();
		document.getElementById("login-error").innerHTML = "";
		document.getElementById("error-email-input").innerHTML = "";
		document.getElementById("error-password-input").innerHTML = "";
		//login submit button
		document.getElementById("login-submit-btn").addEventListener("click", function(event) {
			document.getElementById("emailInput").addEventListener("input", function(event) { updateError(event.target.value, "error-email-input"); }, false);
			document.getElementById("passwordInput").addEventListener("input", function(event) { updateError(event.target.value, "error-password-input"); }, false);
			loginFormSubmit(event);
		}, false);
	});

	//hide the login modal popup
	document.getElementById('closeBtn').addEventListener('click', function () {
		document.getElementById('loginPopupModal').style.display = 'none';
	});
}

async function registerUserSave(event) {
	event.preventDefault();
	//validating the fields
	let email = document.getElementById("email").value;
	let password = document.getElementById("password").value;
	validationSuccess = 0;
	if (!email.trim()) {
	    document.getElementById("error-email").innerHTML = "This Field is required.";
	} else if (!isValidEmail(email)) {
	  	document.getElementById("error-email").innerHTML = "This Field is not valid.";
	} else {
		// Make the AJAX call using fetch() to validate the email number
		try {
		  	const requestData = { email: email };
		    const response = await fetch('/user/verify-email', {
		     	method: 'POST',
		        headers: { 'Content-Type': 'application/json' },
		        body: JSON.stringify(requestData)
		    });
           const data = await response.json();
           if (data.success) {
               validationSuccess++;
           } else {
               document.getElementById("error-email").innerHTML = "Email already exists.";
           }
       } catch (error) {
           console.log(error);
           alert(error.message);
       }
	}
  	if(!password.trim()) document.getElementById("error-password").innerHTML = "This Field is required.";
   	else if(!isValidPassword(password)) document.getElementById("error-password").innerHTML = "This Field is not valid.";
  	else validationSuccess++;
    console.log(validationSuccess);
	if (validationSuccess === 2) {
       // Proceed with the registration process after all validations pass
       const requestData = {
           email: email,
           password: password
       };
       try {
           const response = await fetch('/user/register', {
               method: 'POST',
               headers: { 'Content-Type': 'application/json' },
               body: JSON.stringify(requestData)
           });
           const data = await response.json();
           console.log(data);
           if (data.success) {
               alert("Registration Completed Successfully.");
               document.getElementById("form-controls").reset();
           } else {
               alert("Registration not Completed Successfully.");
           }
       } catch (error) {
           console.log(error);
           alert(error.message);
       }
   }
}

async function loginFormSubmit(event) {
	event.preventDefault()
	//validating the fields
	let email = document.getElementById("emailInput").value;
	let password = document.getElementById("passwordInput").value;
	validationSuccess = 0;
	(!email.trim())? document.getElementById("error-email-input").innerHTML = "This Field is required." : validationSuccess++; 
	(!password.trim())? document.getElementById("error-password-input").innerHTML = "This Field is required." : validationSuccess++;
	console.log(validationSuccess);
	if(validationSuccess == 2) {
		try {
			const requestData = { email: email, password: password };
            const response = await fetch('/user/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestData)
            });
            const data = await response.json();
            console.log(data);
            if (data.success) {
                alert("Log in successful.");
                window.location.href = '../html/dashboard.html'; // Redirect to dashboard page
            } else {
                document.getElementById("login-error").innerHTML = "Incorrect Credentials";
            }
        } catch (error) {
            console.log(error);
            alert(error.message);
        }
	}
}

function isValidEmail(email) {
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    return emailRegex.test(email);
}

function isValidPassword(password) {
    // Regular expression for password validation
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    // Test the password against the regex
    return passwordRegex.test(password);
}

function updateError(value, errId) {
	if(!value.trim()) document.getElementById(errId).innerHTML="This Field is required.";
	else document.getElementById(errId).innerHTML="";
}

// Close modal when clicking outside of the content
window.addEventListener('click', function (e) {
    const modal = document.getElementById('loginPopupModal');
    if (e.target === modal) {
        modal.style.display = 'none';
    }
});

window.addEventListener("load", start, false);