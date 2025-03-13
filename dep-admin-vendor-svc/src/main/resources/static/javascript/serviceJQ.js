let selectedImages = [];

$(document).ready(function() {
	let intervalSetter = setInterval(function () {
		let user = JSON.parse(sessionStorage.getItem("user"));
		if (user) {
			clearInterval(intervalSetter); // Stop checking once data is available
			if(user.role && user.role == "vendor") {
				//calling the function to get the list of services
				getByVendorServicesList(user);
				$("#addNewServiceBtn").html("Add New " + user.category);
				// Show the login Modal popup
				$('#addNewServiceBtn').click(function () {
					$("#form-heading").html("Enter " + user.category + " Details");
				    $('#serviceFormPopupModal').css('display', 'flex');
					$("#vendorServiceForm")[0].reset(); // Corrected to reset the form
					$("#preview").html("");
				});
				//hide the login modal popup
				$('#closeBtn').on('click', function () {
					$('#serviceFormPopupModal').css('display', 'none');
				});
			} else {
				// Get the service type from the URL
				const serviceType = getQueryParam('type');
				//Display it on the page.
				console.log("Selected Service:", serviceType);
				getByCategoryServicesList(serviceType, user);
			}

			// Handle Image Selection and Preview
			 	$("#images").on("change", function (event) {
			     selectedImages = Array.from(event.target.files);
			     $("#preview").empty();

			     selectedImages.forEach(file => {
			         let reader = new FileReader();
			         reader.onload = function (e) {
			             $("#preview").append(`<img src="${e.target.result}" alt="Preview">`);
			         };
			         reader.readAsDataURL(file);
			   	});
			})
			 
			// Handle Upload Button Click
			$("#serviceFormSubmit").on("click", function (event) { saveService(event, user); });
		}
	}, 1000);
});

// Function to get query parameters
function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

async function saveService(event, user) {
	event.preventDefault();
	let name = $("#name").val();
	let title = $("#title").val();
	let address = $("#address").val();
	let summary = $("#summary").val();
	let price = parseInt($("#price").val()) || 0;  // Ensure it's a number
	let guest = parseInt($("#guest").val()) || 0; 
	let images = $("#images")[0].files;
	console.log($("#images"));
	let validationSuccess = 0;
	(!name.trim())? $("#error-name").html("This Field is required.") : validationSuccess++; 
	(!title.trim())? $("#error-title").html("This Field is required.") : validationSuccess++;
	(!address.trim())? $("#error-address").html("This Field is required.") : validationSuccess++;
	(!summary.trim())? $("#error-summary").html("This Field is required.") : validationSuccess++;
	(images.length === 0)? $("#error-summary").html("This Field is required.") : validationSuccess++;
	
	if (validationSuccess === 5) {
		const requestData = {
            name: name,
            title: title,
            address: address,
            summary: summary,
            price: price,
            guest: guest,
            vendor: {
                id: user.id,
                email: user.email
            }
        };
        // Create FormData object
        let formData = new FormData();
        // Append JSON data as a separate form field
        formData.append("serviceDetails", new Blob([JSON.stringify(requestData)], { type: "application/json" }));
        // Append each selected image
        for (let i = 0; i < images.length; i++) {
            formData.append("images", images[i]);
        }
       try {
           const response = await fetch('/vendor-service/save', {
               method: 'POST',
               body: formData
           });
           const data = await response.json();
           if (data.success) {
               $("#vendorServiceForm")[0].reset();
			   location.reload();
           } else {
               alert("Save Failed.");
           }
       } catch (error) {
           console.log(error);
           alert(error.message);
       }
   }
}

async function getByVendorServicesList(user) {
    try {
	   const requestData = { id: user.id };
	   const response = await fetch('/vendor-service/listByVendor', {
	     	method: 'POST',
	        headers: { 'Content-Type': 'application/json' },
	        body: JSON.stringify(requestData)
	    });
       const data = await response.json();
       if (data.success) {
           	console.log(data);
			getCardsData(data.data, user);
       } else {
			alert("List not fetched.")
       }
   } catch (error) {
       console.log(error);
       alert(error.message);
   }
}

async function getByCategoryServicesList(category, user) {
	let searchText = $("#search-content").val();
	let sortText = $("#sort").val();
    try {
	   const requestData = { 
			category: category,
			search : searchText,
			sort: sortText
	   };
	   const response = await fetch('/vendor-service/listByCategory', {
	     	method: 'POST',
	        headers: { 'Content-Type': 'application/json' },
	        body: JSON.stringify(requestData)
	    });
       const data = await response.json();
       if (data.success) {
           	console.log(data);
			getCardsData(data.data, user);
       } else {
			alert("List not fetched.")
       }
   } catch (error) {
       console.log(error);
       alert(error.message);
   }
}

function getCardsData(data, user) {
    $("#serviceListSection2").empty();
	$("#countMessage").html("Total Count : " + data.length);
    $(data).each(function() {
        //created the div element for cards.
        let card = document.createElement("div");
        card.setAttribute("class", "card");
        //created the div container element for the carousel images and button.
        let container = document.createElement("div");
        container.setAttribute("class", "carousel-container");
        //created the div element for the carousel images
        let carousel = document.createElement("div");
        carousel.setAttribute("class", "carousel");
        //created the each image element for the each service images and appending it to the carousel.
		// Ensure images are not undefined/null before splitting
		let imageArray = (this.images || "").split(",").filter(img => img.trim() !== "");
		if (imageArray.length > 0) {  
		    imageArray.forEach(function(value, index) {
		        let img = document.createElement("img");
		        img.setAttribute("class", "card-carousel-image");
		        img.alt = "Image " + (index + 1);
		        // Normalize Windows (\) and Unix (/) paths
		        let fileName = value.trim().split(/[/\\]/).pop();  
		        // Set correct image source
		        img.src = `/vendor-service/images/${fileName}`;
		        $(carousel).append(img);
		    });
		} else {
		    console.warn(`No images found for service ID: ${this.id}`);
		}
        //appending the carousel images to the container.
        container.appendChild(carousel);
        //create the buttons to go to the next and previous image and append the buttons to the container.
        let prevButton = document.createElement("button");
        prevButton.setAttribute("class", "prev");
        $(prevButton).html("&#10094;");
        $(container).append(prevButton);
        let nextButton = document.createElement("button");
        nextButton.setAttribute("class", "next");
        $(nextButton).html("&#10095;");
        $(container).append(nextButton);
        //append the container to the card.
        $(card).append(container);

        //create the div element to display the details of the pet.
        let cardInfo = document.createElement("div");
        cardInfo.setAttribute("class", "card-info");
        let name = document.createElement("p");
		name.setAttribute("class", "list-service-name");
        $(name).text(this.name);
        let title = document.createElement("p");
		title.setAttribute("class", "list-service-title");
        $(title).text(this.title);
        let address = document.createElement("p");
		address.setAttribute("class", "list-service-address");
        $(address).text('📍'+this.address);
        //appending the name, breed and gender of each pet to cardInfo element.
		$(cardInfo).append(address);
        $(cardInfo).append(name);
        $(cardInfo).append(title);
		let path="";
		if(user.role == "vendor") path = "../html/EachService.html?id="+(this.id); 
		else path = "../html/EachVendorService.html?id="+(this.id); 
        let infoTag = document.createElement("a");
        infoTag.setAttribute("id", "readHouseInfo");
        infoTag.setAttribute("class", "card-readmore-link");
        infoTag.setAttribute("href", path);
        infoTag.textContent="Read More";
        $(cardInfo).append(infoTag);
        //appending the cardInfo to the card.
        $(card).append(cardInfo);
        
        $("#serviceListSection2").append(card);

        //the event listeners for the next and the previous button on the images carousel.
        let slideIndex = 0;

        $(prevButton).on('click', function() {
            slideIndex--;  // Decrease the slide index.
            showSlide(carousel, slideIndex);  // Show the previous image.
        });

        $(nextButton).on('click', function() {
            slideIndex++;  // Increase the slide index.
            showSlide(carousel, slideIndex);  // Show the next image.
        });

        // Show the first slide for this card when it's loaded.
        showSlide(carousel, slideIndex);
    })
}

function showSlide(carousel, slideIndex) {
    const slides = carousel.querySelectorAll('.card-carousel-image');
    if (slideIndex >= slides.length) {
        slideIndex = 0;  // Loop to the first image if it goes beyond
    } else if (slideIndex < 0) {
        slideIndex = slides.length - 1;  // Loop to the last image if it goes below
    }

    // Hide all slides
    slides.forEach(function(slide) {
        slide.style.display = 'none';
    });

    // Show the current slide
    slides[slideIndex].style.display = 'block';
}