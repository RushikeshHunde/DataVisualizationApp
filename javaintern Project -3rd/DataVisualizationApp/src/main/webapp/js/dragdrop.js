const dropArea = document.getElementById("drop-area");
const fileInput = document.getElementById("fileInput");
const form = fileInput.closest("form");

// Prevent default behavior (Prevent file from opening)
["dragenter", "dragover", "dragleave", "drop"].forEach(eventName => {
    dropArea.addEventListener(eventName, e => {
        e.preventDefault();
        e.stopPropagation();
    });
});

// Highlight when file is dragged over
["dragenter", "dragover"].forEach(eventName => {
    dropArea.addEventListener(eventName, () => {
        dropArea.style.border = "3px dashed #4A90E2";  // Highlight border
    });
});
// Remove highlight on leave/drop
["dragleave", "drop"].forEach(eventName => {
    dropArea.addEventListener(eventName, () => {
        dropArea.style.border = "";  // Reset to default
    });
});

// Handle dropped files
dropArea.addEventListener("drop", (e) => {
    const files = e.dataTransfer.files;
    if (files.length > 0) {
        const dataTransfer = new DataTransfer();
        dataTransfer.items.add(files[0]); // Only first file
        fileInput.files = dataTransfer.files;
        form.submit(); // Auto-submit the form after dropping
    }
});
