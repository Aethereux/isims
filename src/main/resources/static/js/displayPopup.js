document.addEventListener('DOMContentLoaded', function() {
    const successPopup = document.getElementById('successPopup');
    const confirmationPopup = document.getElementById('confirmationPopup');
    const okBtn = document.getElementById('okBtn');
    const cancelDeleteBtn = document.getElementById('cancelDeleteBtn');
    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
    const popupIcon = document.querySelector('.successPopup .popupIcon');
    const popupTitle = document.getElementById('popupTitle');
    const popupText = document.getElementById('popupText');

    let pendingDeleteId = null;

    // Show success/error popup
    function showPopup(message, type) {
        if (message) {
            // Set popup content based on type
            if (type === 'success') {
                popupIcon.className = 'popupIcon success';
                popupTitle.textContent = 'Success!';
                popupText.textContent = message;
            } else if (type === 'error') {
                popupIcon.className = 'popupIcon error';
                popupTitle.textContent = 'Error!';
                popupText.textContent = message;
            }

            successPopup.style.display = 'flex';
        }
    }

    function showDeleteConfirmation(studentId) {
        pendingDeleteId = studentId;
        confirmationPopup.style.display = 'flex';
    }

    if (okBtn) {
        okBtn.addEventListener('click', function() {
            successPopup.style.display = 'none';
        });
    }

    successPopup.addEventListener('click', function(e) {
        if (e.target === successPopup) {
            successPopup.style.display = 'none';
        }
    });

    if (cancelDeleteBtn) {
        cancelDeleteBtn.addEventListener('click', function() {
            confirmationPopup.style.display = 'none';
            pendingDeleteId = null;
        });
    }

    if (confirmDeleteBtn) {
        confirmDeleteBtn.addEventListener('click', function() {
            if (pendingDeleteId) {
                // UPDATED: Redirect to Spring Boot Delete Controller
                window.location.href = '/delete/' + pendingDeleteId;
            }
            confirmationPopup.style.display = 'none';
        });
    }

    confirmationPopup.addEventListener('click', function(e) {
        if (e.target === confirmationPopup) {
            confirmationPopup.style.display = 'none';
            pendingDeleteId = null;
        }
    });

    // Check if there's a session message and show popup
    const messageElement = document.querySelector('.sessionMessage .message');
    if (messageElement) {
        const message = messageElement.textContent.trim();
        const type = messageElement.classList.contains('success') ? 'success' : 'error';

        messageElement.style.display = 'none';

        showPopup(message, type);
    }

    window.showDeleteConfirmation = showDeleteConfirmation;

    window.updateStudent = function(id) {
        window.location.href = '/update/' + id;
    };

    window.deleteStudent = function(id) {
        showDeleteConfirmation(id);
    };
});