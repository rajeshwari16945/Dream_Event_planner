function selectTemplate(template) {
    document.getElementById("preview-template").src = template;

    // Highlight selected template
    document.querySelectorAll(".template").forEach(img => img.classList.remove("selected"));
    event.target.classList.add("selected");
}

function updatePreview() {
    document.getElementById("preview-eventName").innerText = document.getElementById("eventName").value || "Event Name";
    document.getElementById("preview-eventDate").innerText = document.getElementById("eventDate").value || "Date";
    document.getElementById("preview-eventLocation").innerText = document.getElementById("eventLocation").value || "Location";
}

function downloadInvitation() {
    html2canvas(document.getElementById("preview")).then(canvas => {
        let link = document.createElement("a");
        link.href = canvas.toDataURL("image/png");
        link.download = "invitation.png";
        link.click();
    });
}
