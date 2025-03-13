function updateText(id, value) {
    document.getElementById(id).textContent = value;
}

function formatDate(dateStr) {
    if (!dateStr) return "Wedding Date"; // Default placeholder text
    const date = new Date(dateStr);
    const options = { day: '2-digit', month: 'long', year: 'numeric' };
    return date.toLocaleDateString('en-US', options);
}

function updateDate() {
    let dateInput = document.getElementById("weddingDate").value;
    document.getElementById("dateText").textContent = formatDate(dateInput);
}

function updateTextColor() {
    let color = document.getElementById('textColor').value;
    document.querySelectorAll('.invitation-draggable-text').forEach(el => el.style.color = color);
}

function updateFont() {
    let selectedFont = document.getElementById("fontSelector").value;
    document.querySelectorAll('.invitation-draggable-text').forEach(el => {
        el.style.fontFamily = selectedFont;
    });
}

function loadImage(event) {
    const file = event.target.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = function (e) {
        document.getElementById('invitationImage').src = e.target.result; 
    };
    reader.readAsDataURL(file);  // Convert file to base64 URL
}

function downloadInvitation() {
    html2canvas(document.getElementById('canvasContainer'), { useCORS: true }).then(canvas => {
        let link = document.createElement('a');
        link.download = 'invitation.png';
        link.href = canvas.toDataURL("image/png");
        link.click();
    }).catch(err => console.error("Error rendering image: ", err));
}

window.addEventListener("load", function() {
    document.getElementById("downloadInvitationButton").addEventListener("click", downloadInvitation, false);
    document.getElementById("uploadImage").addEventListener("change", loadImage, false);
    document.getElementById("coupleNames").addEventListener("input", function() {
        updateText('coupleText', this.value);
    }, false);
    document.getElementById("weddingDate").addEventListener("input", updateDate, false);
    document.getElementById("venue").addEventListener("input", function() {
        updateText('venueText', this.value);
    }, false);
    document.getElementById("textColor").addEventListener("input", updateTextColor, false);
    document.getElementById("fontSelector").addEventListener("change", updateFont, false);
}, false);
