function start() {
	//used the set interval to display random images in the image tag
	let images = ["food1.jpg", "photo1.jpg", "photo2.jpg"];
	let img = document.getElementById("scrollImage");
	img.setAttribute("src", "../images/" + images[0]);
	intervalSetter = window.setInterval(function() {
        // Generate a random number between 1 and 5
        let randNum = 0 + Math.floor(Math.random() * images.length);
        console.log("rand : " +randNum);
        img.setAttribute("src", "../images/" + images[randNum]);
	}, 2000);
		
	//create the event listener for submit button.
	document.getElementById("submitForm").addEventListener("click", function(event) { 
		document.getElementById("fsname").addEventListener("input", function(event) { updateError(event.target.value, "error-fname"); }, false);
		document.getElementById("lsname").addEventListener("input", function(event) { updateError(event.target.value, "error-lname"); }, false);
		document.getElementById("company").addEventListener("input", function(event) { updateError(event.target.value, "error-company"); }, false);
		document.getElementById("phone").addEventListener("input", function(event) { updateError(event.target.value, "error-phone"); }, false);
		document.getElementById("email").addEventListener("input", function(event) { updateError(event.target.value, "error-email"); }, false);
		document.getElementById("category").addEventListener("change", function(event) { updateError(event.target.value, "error-category"); }, false);
		document.getElementById("password").addEventListener("input", function(event) { updateError(event.target.value, "error-password"); }, false);
		document.getElementById("confirm-password").addEventListener("input", function(event) { updateError(event.target.value, "error-confirm-password"); }, false);
			
		registerFormSubmit(event); 
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
            const response = await fetch('/vendor/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestData)
            });
            const data = await response.json();
            console.log(data);
            if (data.success) {
				alert("Log in successful.");
				window.location.href = '../html/VendorDashboard.html'; // Redirect to dashboard page
            } else {
                document.getElementById("login-error").innerHTML = "Incorrect Credentials";
            }
        } catch (error) {
            console.log(error);
            alert(error.message);
        }
	}
}

async function registerFormSubmit(event) {
    event.preventDefault();

    //validating the fields.
    let fname = document.getElementById("fsname").value;
    let lname = document.getElementById("lsname").value;
    let company = document.getElementById("company").value;
    let category = document.getElementById("category").value;
    let phone = document.getElementById("phone").value;
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;
	let confirmPassword = document.getElementById("confirm-password").value;
    let validationSuccess = 0;

    // Field validations
    (!fname.trim()) ? document.getElementById("error-fname").innerHTML = "This Field is required." : validationSuccess++;
    (!lname.trim()) ? document.getElementById("error-lname").innerHTML = "This Field is required." : validationSuccess++;
    (!company.trim()) ? document.getElementById("error-company").innerHTML = "This Field is required." : validationSuccess++;
    (!category.trim()) ? document.getElementById("error-category").innerHTML = "This Field is required." : validationSuccess++;

    if (!phone.trim()) {
        document.getElementById("error-phone").innerHTML = "This Field is required.";
    } else if (!isValidPhoneNumber(phone)) {
        document.getElementById("error-phone").innerHTML = "This Field is not valid.";
    } else {
        // Make the AJAX call using fetch() to validate the phone number
        try {
            const requestData = { phone: phone };
            const response = await fetch('/vendor/verify-phone', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestData)
            });
            const data = await response.json();
            if (data.success) {
                validationSuccess++;
            } else {
                document.getElementById("error-phone").innerHTML = "Phone already exists.";
            }
        } catch (error) {
            console.log(error);
            alert(error.message);
        }
    }

    if (!email.trim()) {
        document.getElementById("error-email").innerHTML = "This Field is required.";
    } else if (!isValidEmail(email)) {
        document.getElementById("error-email").innerHTML = "This Field is not valid.";
    } else {
		// Make the AJAX call using fetch() to validate the email number
		try {
		  	const requestData = { email: email };
		    const response = await fetch('/vendor/verify-email', {
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
    
	if(!confirmPassword.trim()) document.getElementById("error-confirm-password").innerHTML = "This Field is required.";
	else if(password != confirmPassword) document.getElementById("error-confirm-password").innerHTML = "Doesn't match.";
	else validationSuccess++;
	console.log(validationSuccess);
    if (validationSuccess === 8) {
        // Proceed with the registration process after all validations pass
        const requestData = {
            firstName: fname,
            lastName: lname,
            company: company,
            category: category,
            phone: phone,
            email: email,
            password: password
        };

        try {
            const response = await fetch('/vendor/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestData)
            });
            const data = await response.json();
            console.log(data);

            if (data.success) {
                alert("Registration Completed Successfully.");
                document.getElementById("register-form-vendor").reset();
            } else {
                alert("Registration not Completed Successfully.");
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

function isValidPhoneNumber(phoneNumber) {
    // Regular expression for phone number validation
    const phoneRegex = /^[+]?[0-9]{10,15}$/; 
    // Allows optional "+" at the start and 10 to 15 digits
    return phoneRegex.test(phoneNumber);  // Returns true if valid, false otherwise
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