$(document).ready(function() {
	$(document).on("click", "#logoutButton", function() {
        console.log("Logout button clicked");
        logout();
    });
});

async function logout() {
    try {
	    const response = await fetch('/user/logout', {
	     	method: 'GET'
	    });
       const data = await response.json();
       if (data.success) {
           	console.log(data);
			sessionStorage.removeItem("user");
			window.location.href = '../home.html'; // Redirect to home page
       } else {
			alert('Logout failed.');
       }
   } catch (error) {
       console.log(error);
       alert(error.message);
   }
}