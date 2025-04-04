$(document).ready(function() {
	let intervalSetter = setInterval(function () {
		let user = JSON.parse(sessionStorage.getItem("user"));
		if (user) {
			clearInterval(intervalSetter); // Stop checking once data is available
			if(user.image) {
				fileName = user.image.trim().split(/[/\\]/).pop();  
				$("#user-image-preview").html(`<img src="/vendor-service/images/${fileName}" alt="Preview">`);
			}
			else $("#user-image-preview").html(`<img src="../images/profilepic2.jpeg" alt="Preview">`);
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
			            $("#user-image-preview").html(`<img src="${e.target.result}" alt="Preview">`); // Replace previous image
			        };
			        reader.readAsDataURL(file);
			    }
				updateVendor(user, event);
			});
		}
	}, 1000);
});

async function updateVendor(user, event) {
	event.preventDefault();
	let image = $("#image")[0].files;
	console.log($("#image"));
	const requestData = {
		id: user.id
    };
    // Create FormData object
    let formData = new FormData();
    // Append JSON data as a separate form field
    formData.append("userDetails", new Blob([JSON.stringify(requestData)], { type: "application/json" }));
    // Append each selected image
	if (image.length > 0) {
	    formData.append("image", image[0]); // Append only the first file
	}
   try {
       const response = await fetch('/user/update-image', {
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