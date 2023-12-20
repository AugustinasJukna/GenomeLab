User
<!-- ConfirmationModal.vue -->

<template>
  <div class="modal fade" id="confirmationModal" tabindex="-1" role="dialog" aria-labelledby="confirmationModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="confirmationModalLabel">Confirmation</h5>
          <button type="button" class="close" @click="dismissModal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <p>{{ message }}</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="dismissModal">Cancel</button>
          <button type="button" class="btn btn-danger" @click="confirm">Confirm</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    message: String,
    onConfirm: Function,
  },
  methods: {
    confirm() {
      this.onConfirm();
      this.dismissModal();
    },
    dismissModal() {
      const confirmationModal = document.getElementById('confirmationModal');
      if (confirmationModal) {
        confirmationModal.classList.remove('show');
        confirmationModal.style.display = 'none';
      }
    },
  },
};
</script>