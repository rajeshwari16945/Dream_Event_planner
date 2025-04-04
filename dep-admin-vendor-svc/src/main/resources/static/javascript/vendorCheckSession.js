$(document).ready(function() {
	console.log("dashboard");
	sessionStorage.removeItem("user");
	checkSession();
});

async function checkSession() {
    try {
	    const response = await fetch('/vendor/session', {
	     	method: 'GET'
	    });
       const data = await response.json();
       if (data.success) {
           	console.log(data);
			console.log(window.location.pathname);
			sessionStorage.setItem("user", JSON.stringify({ email: data.data.email, id: data.data.id, category: data.data.category, first_name: data.data.first_name, last_name: data.data.last_name, phone: data.data.phone, company: data.data.business, image: data.data.image, role: data.data.role}));
			console.log(sessionStorage);
			if(window.location.pathname == "/vendor.html") window.location.href = '../html/VendorDashboard.html';
       } else {
			if(window.location.pathname != "/vendor.html") {
				window.location.href = '../vendor.html'; // Redirect to home page
				alert('Session expired or unauthorized.');
			}
       }
   } catch (error) {
       console.log(error);
       alert(error.message);
   }
}

document.addEventListener("DOMContentLoaded", function () {
    setTimeout(() => {
        let navLinks = document.querySelectorAll(".user-nav-menu a");
        console.log("Nav links found:", navLinks.length); // Debugging log
        if (navLinks.length === 0) {
            console.error("No nav links found. Check the class names or script execution order.");
            return;
        }
        let currentPage = window.location.pathname.split("/").pop();
        navLinks.forEach(link => {
            let linkPath = link.getAttribute("href").split("/").pop();
            if (currentPage === linkPath) {
                navLinks.forEach(l => l.classList.remove("active"));
                link.classList.add("active");
            }
        });
    }, 100); // Small delay to ensure DOM is loaded
});