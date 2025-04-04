$(document).ready(function() {
	let intervalSetter = setInterval(function () {
		let user = JSON.parse(sessionStorage.getItem("user"));
		if (user) {
			clearInterval(intervalSetter); // Stop checking once data is available
			getBookingByVendor(user);
		}
	});	
});

async function getBookingByVendor(user) {
	try {
	   const requestData = { id: user.id };
	   const response = await fetch('/booking/listByVendor', {
	     	method: 'POST',
	        headers: { 'Content-Type': 'application/json' },
	        body: JSON.stringify(requestData)
	    });
	    const data = await response.json();
	    if (data.success) {
        	console.log(data);
			getGridData(data.data, user);
	    } else {
			alert("List not fetched.")
	    }
	} catch (error) {
	    console.log(error);
	    alert(error.message);
	}
}

function getGridData(data, user) {
	$("#tbody").empty(); // Clear existing data before appending new rows
	$(data).each(function(index, item) { // Iterate over each booking entry
        let tr = document.createElement("tr");

        let tdInd = document.createElement("td");
        tdInd.innerText = index + 1; // Display serial number
        tr.appendChild(tdInd);

        let tdService = document.createElement("td");
        tdService.innerText = item.serviceName;
        tr.appendChild(tdService);

        let tdName = document.createElement("td");
        tdName.innerText = item.firstName + " " + item.lastName;
        tr.appendChild(tdName);

        let tdDate = document.createElement("td");
        tdDate.innerText = item.eventDate;
        tr.appendChild(tdDate);

        let tdGuest = document.createElement("td");
        tdGuest.innerText = item.guest;
        tr.appendChild(tdGuest);

        let tdStatus = document.createElement("td");
        tdStatus.innerText = item.status;
		tdStatus.classList.add("booking-status");
		switch (item.status.toLowerCase()) {
		    case "rejected":
		        tdStatus.classList.add("booking-status-rejected");
		        break;
		    case "requested":
		        tdStatus.classList.add("booking-status-requested");
		        break;
		    case "confirmed":
		        tdStatus.classList.add("booking-status-confirmed");
		        break;
		    case "booked":
		        tdStatus.classList.add("booking-status-booked");
		        break;
		}
		tr.appendChild(tdStatus);
		
		// View button with eye symbol
        let tdView = document.createElement("td");
        let viewLink = document.createElement("a");
        viewLink.href = `../html/EachBooking.html?id=${item.id}`; // Adjust URL as needed
        viewLink.innerHTML = "👁";
        viewLink.classList.add("view-icon"); // Add a class for styling
        tdView.appendChild(viewLink);
        tr.appendChild(tdView);

        $("#tbody").append(tr);
	});
}