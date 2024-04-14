package com.example.capsules;

public interface UserAuthListener {
    class AuthResult {
        final boolean isSuccessful;
        final String error;
        final String userId;

        public AuthResult(boolean isSuccessful, String error, String userId) {
            this.isSuccessful = isSuccessful;
            this.error = error;
            this.userId = userId;
        }

        public boolean isSuccessful() {
            return isSuccessful;
        }

        public String getError() {
            return error;
        }

        public String getUserId() {
            return userId;
        }

        public boolean isSuccess() {
            return isSuccessful;
        }

        public String getErrorMsg() {
            return error;
        }
    }
    void onResult(AuthResult authResult);
}
