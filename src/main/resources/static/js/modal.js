/**
 * Delete Modal Handler
 * Manages the delete confirmation modal for player deletion
 */
document.addEventListener('DOMContentLoaded', function() {
    const deleteModal = document.getElementById('deleteModal');
    const playerNameModal = document.getElementById('playerNameModal');
    const deleteForm = document.getElementById('deleteForm');

    if (deleteModal) {
        deleteModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget; // Button that triggered the modal
            const playerId = button.getAttribute('data-player-id');
            const playerName = button.getAttribute('data-player-name');

            // Update modal content
            playerNameModal.textContent = playerName;

            // Set form action to the delete endpoint
            deleteForm.action = `/players/${playerId}`;
        });

        // Reset form when modal is closed
        deleteModal.addEventListener('hidden.bs.modal', function () {
            deleteForm.reset();
        });
    }
});
