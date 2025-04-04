//Created By Ganamukkula
$(document).ready(function() {
	let intervalSetter = setInterval(async function () {
		let user = JSON.parse(sessionStorage.getItem("user"));
		if (user) {
			clearInterval(intervalSetter); // Stop checking once data is available
			// Get the service type from the URL
			const serviceId = getQueryParam('id');
			getEachServices(serviceId, user);
			if(user.role == "user") {
				// Wait for the async function to return data
                let data = await getBookingDatesByService(serviceId);

                console.log("Disabled Dates:", data);

                let today = new Date();

                // Ensure that date_array exists and is in the correct format
                let disabledDates = data && data.date_array ? data.date_array.split(",") : [];

                $("#event-date").datepicker({
                    dateFormat: "yy-mm-dd",
                    minDate: today,
                    beforeShowDay: function (date) {
                        let formattedDate = $.datepicker.formatDate("yy-mm-dd", date);
                        return [disabledDates.indexOf(formattedDate) === -1]; // Return false to disable the date
                    },
                });
				$("#requestButton").on("click", function(event) {
					$("#first_name").on("input", function(event) { updateError(event.target.value, "error-fname"); });
					$("#last_name").on("input", function(event) { updateError(event.target.value, "error-lname"); });
					$("#event-date").on("input", function(event) { updateError(event.target.value, "error-date"); });
					$("#guest").on("input", function(event) { updateError(event.target.value, "error-guest"); });
					$("#vision").on("input", function(event) { updateError(event.target.value, "error-vision"); });
					saveBooking(event, user, serviceId);
				});
			} else if(user.role == "vendor") {
				getBookingCount(serviceId);
			}
			multipleCarousel();
		}
	}, 1000);
});

async function saveBooking(event, user, serviceId) {
	event.preventDefault();
    //validating the fields.
    let fname = $("#first_name").val();
    let lname = $("#last_name").val();
    let date = $("#event-date").val();
    let guest = $("#guest").val();
    let vision = $("#vision").val();
	let validationSuccess = 0;
	
	// Field validations
    (!fname.trim()) ? $("#error-fname").html("This Field is required.") : validationSuccess++;
	(!lname.trim()) ? $("#error-lname").html("This Field is required.") : validationSuccess++;
	(!date.trim()) ? $("#error-date").html("This Field is required.") : validationSuccess++;
	(!guest.trim()) ? $("#error-guest").html("This Field is required.") : validationSuccess++;
	(!vision.trim()) ? $("#error-vision").html("This Field is required.") : validationSuccess++;
	if (validationSuccess === 5) {
		//after all validations pass save the data.
		// Make the AJAX call using fetch() to validate the phone number
        try {
			const requestData = {
	            firstName: fname,
	            lastName: lname,
	            eventDate: date,
	            guest: parseInt(guest),
	            vision: vision,
	            user: user.id,
	            service: parseInt(serviceId)
	        };
            const response = await fetch('/booking/save', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestData)
            });
            const data = await response.json();
            if (data.success) {
                alert('Booking request is made. Please wait till vendor approves your request.');
            } else {
				alert(data.message);
            }
			$("#booking-form").trigger("reset");
        } catch (error) {
            console.log(error);
            alert(error.message);
        }
	}
}

async function getBookingCount(serviceId) {
	const response = await fetch('/booking/count/' + serviceId, {
     	method: 'GET'
    });
   const data = await response.json();
   if(data.success) {
		$("#heading-for-bookings").html(`You have <strong>${data.data.bookingCount} bookings</strong> for this service. Some bookings might be pending approval. Please review them as soon as possible. Check the event details and either accept or reject based on availability.`);
		$("#link-for-bookings").attr("class", "go-to-bookings-button");
		$("#link-for-bookings").attr("href", "Bookings.html");
		$("#link-for-bookings").html("Manage Your Bookings");
   }
}

function updateError(value, errId) {
	if(!value.trim()) $(`#${errId}`).html("This Field is required.");
	else $(`#${errId}`).html("");
}

function multipleCarousel() {
	  const carouselInner = document.querySelector('.carousel-inner');
	  const prevBtn = document.querySelector('.prev');
	  const nextBtn = document.querySelector('.next');
	  const slides = carouselInner.children;
	  const totalSlides = slides.length;
	  let currentIndex = 0;
	  const visibleSlides = 2;

	  function showSlide() {
	    const start = currentIndex;
	    const end = Math.min(currentIndex + visibleSlides, totalSlides);
	    for (let i = 0; i < totalSlides; i++) {
	      slides[i].style.display = 'none';
	    }
	    for (let i = start; i < end; i++) {
	      slides[i].style.display = 'block';
	    }
	  }

	  function nextSlide() {
	    currentIndex = (currentIndex + visibleSlides) % totalSlides;
	    showSlide();
	  }

	  function prevSlide() {
	    currentIndex = (currentIndex - visibleSlides + totalSlides) % totalSlides;
	    showSlide();
	  }

	  prevBtn.addEventListener('click', prevSlide);
	  nextBtn.addEventListener('click', nextSlide);

	  showSlide(); // Show initial set of slides
}

// Function to get query parameters
function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

async function getBookingDatesByService(id) {
	try {
		const response = await fetch('/booking/getDatesByService/'+id, {
			method: 'GET'
		});
		const data = await response.json();
		if(data.success) {
			console.log(data);
			return data.data;
		} else {
			alert("Booking dates not fetched");
		}
	} catch (error) {
       console.log(error);
       alert(error.message);
    }
}

async function getEachServices(id, user) {
    try {
	   const response = await fetch('/vendor-service/'+id, {
	     	method: 'GET'
	    });
       const data = await response.json();
       if (data.success) {
           	console.log(data);
			if(user.role && user.role == "vendor") $("#backLink").attr("href", `service.html`);
			else $("#backLink").attr("href", `VendorList.html?type=${data.data.category}`);
			$("#service-name").html(data.data.name);
			$("#service-title").html(data.data.title);
			$("#service-price").html(`💲${data.data.price} avg.price`);
			$("#service-guest").html(`👥${data.data.guest}+ guests`);
			$("#vendor-name").html(data.data.vendor_name);
			$("#vendor-name-form").html(data.data.vendor_name);
			$("#service-summary").html(data.data.summary);
			if(data.data.vendor_image) {
				fileName = data.data.vendor_image.trim().split(/[/\\]/).pop();  
				$("#service-vendor-image").html(`<img src="/vendor-service/images/${fileName}" alt="Preview">`);
				$("#request-form-image").html(`<img src="/vendor-service/images/${fileName}" alt="Preview">`);
			}
			else {
				$("#service-vendor-image").html(`<img src="../images/profilepic1.jpg" alt="Preview">`);
				$("#request-form-image").html(`<img src="../images/profilepic1.jpg" alt="Preview">`);
			}
			let imageArray = (data.data.images || "").split(",").filter(img => img.trim() !== "");
			if (imageArray.length > 0) {  
			    imageArray.forEach(function(value, index) {
			        let img = document.createElement("img");
			        img.alt = "Image " + (index + 1);
			        // Normalize Windows (\) and Unix (/) paths
			        let fileName = value.trim().split(/[/\\]/).pop();  
			        // Set correct image source
			        img.src = `/vendor-service/images/${fileName}`;
			        $("#carousel-inner").append(img);
			    });
			} else {
			    console.warn(`No images found for service ID: ${this.id}`);
			}
       } else {
			alert("List not fetched.")
       }
   } catch (error) {
       console.log(error);
       alert(error.message);
   }
}




  