function start() {
	let intervalSetter = setInterval(function () {
		let user = JSON.parse(sessionStorage.getItem("user"));
		if (user) {
			clearInterval(intervalSetter); // Stop checking once data is available
			getDashboardData(user); // Call function only when user data exists
		}
	}, 1000);
}

async function getDashboardData(user) {
	try {
		const requestData = { id: user.id };
        const response = await fetch('/vendor/dashboard', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestData)
        });
        const data = await response.json();
        console.log(data);
        if (data.success) {
			document.getElementById("display-vendor-name").innerHTML = user.first_name + " " + user.last_name;
			document.getElementById("display-vendor-company").innerHTML = user.company;
			document.getElementById("service-name").innerHTML = "Your " + user.category;
			document.getElementById("service-count").innerHTML = data.data.serviceCount;
			document.getElementById("bookingCount").innerHTML = data.data.bookingCount;
			document.getElementById("completedWeddings").innerHTML = data.data.confirmedBookingCount;
			document.getElementById("totalUsers").innerHTML = data.data.userCount;
			document.getElementById("service-name-button").innerHTML = "Add " + user.category;
			if(user.image) {
				fileName = user.image.trim().split(/[/\\]/).pop();  
				$("#profilePic").html(`<img src="/vendor-service/images/${fileName}" alt="Preview">`);
			}
			else $("#profilePic").html(`<img src="../images/profilepic1.jpg" alt="Preview">`);   $("#image").trigger("click");
			
        } else {
			alert("cannot fetch dashboard data")
        }
    } catch (error) {
        console.log(error);
        alert(error.message);
    }
}

window.addEventListener("load", start, false);