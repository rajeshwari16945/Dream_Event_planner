$(document).ready(function() {
	let intervalSetter = setInterval(function () {
		let user = JSON.parse(sessionStorage.getItem("user"));
		if (user) {
			clearInterval(intervalSetter); // Stop checking once data is available
			$("#fsname").val(user.first_name);
			$("#lsname").val(user.last_name);
			$("#phone").val(user.phone);
			$("#email").val(user.email);
			$("#business").val(user.company);
			if(user.image) {
				fileName = user.image.trim().split(/[/\\]/).pop();  
				$("#preview").html(`<img src="/vendor-service/images/${fileName}" alt="Preview">`);
			}
			else $("#preview").html(`<img src="../images/profilepic1.jpg" alt="Preview">`);
			// Open file dialog when "Change Photo" button is clicked
		    $("#changePhoto").on("click", function () {
		        $("#image").trigger("click");
			});
			// Handle Image Selection and Preview
			$("#image").on("change", function (event) {
			    let file = event.target.files[0]; // Get the first selected file
			    if (file) {
			        let reader = new FileReader();
			        reader.onload = function (e) {
			            $("#preview").html(`<img src="${e.target.result}" alt="Preview">`); // Replace previous image
			        };
			        reader.readAsDataURL(file);
			    }
			});
			//handle update button 
			$("#Update").on("click", function() { updateVendor(user); });
		}
	}, 1000);
});

async function updateVendor(user) {
	event.preventDefault();
	let firstName = $("#fsname").val();
	let lastName = $("#lsname").val();
	let phone = $("#phone").val();
	let email = $("#email").val();
	let business = $("#business").val();
	let image = $("#image")[0].files;
	console.log($("#images"));
	let validationSuccess = 0;
	(!firstName.trim()) ? $("#error-firstName").html("This Field is required.") : validationSuccess++; 
	(!lastName.trim()) ? $("#error-lastName").html("This Field is required.") : validationSuccess++;
	(!phone.trim()) ? $("#error-phone").html("This Field is required.") : validationSuccess++;
	(!email.trim()) ? $("#error-email").html("This Field is required.") : validationSuccess++;
	(!business.trim()) ? $("#error-business").html("This Field is required.") : validationSuccess++;
	
	if (validationSuccess === 5) {
		const requestData = {
			id: user.id,
            firstName: firstName,
            lastName: lastName,
            email: email,
            phone: phone,
			company: business,
        };
        // Create FormData object
        let formData = new FormData();
        // Append JSON data as a separate form field
        formData.append("vendorDetails", new Blob([JSON.stringify(requestData)], { type: "application/json" }));
        // Append each selected image
		if (image.length > 0) {
		    formData.append("image", image[0]); // Append only the first file
		}
       try {
           const response = await fetch('/vendor/update', {
               method: 'POST',
               body: formData
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