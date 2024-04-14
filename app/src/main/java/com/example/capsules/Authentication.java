package com.example.capsules;

import static android.app.Activity.RESULT_OK;
import static com.example.capsules.Constants.COLLECTION_USERS;
import static com.example.capsules.Constants.LOCAL_DB_NAME;
import static com.example.capsules.Constants.REQ_GOOGLE_AUTH;
import static com.example.capsules.Constants.USER_DEFAULT_AVATAR;
import static com.example.capsules.Constants.WEB_CLIENT_KEY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


class Authentication {

    private static final String KEY_USER_ID = "userId";

    private static final String KEY_USER_NAME = "userName";
    public final FirebaseAuth auth;
    public final FirebaseFirestore firestore;
    private final Context context;
    private final GoogleSignInClient signInClient;
    private UserAuthListener authListener;

    public Authentication(Context context) {
        this.context = context;
        this.firestore = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        this.signInClient = GoogleSignIn.getClient(context, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_KEY)
                .requestEmail()
                .build());
    }

    public void signInWithEmailPassword(@NonNull String email, @NonNull String password, UserAuthListener authListener) {
        this.authListener = authListener;
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    if (authResult.getUser() == null) {
                        onAuthFailure(null);
                        return;
                    }
                    String userId = authResult.getUser().getUid();
                    onAuthSuccess(userId);
                })
                .addOnFailureListener(this::onAuthFailure);
    }

    public void signUpWithEmailPassword(@NonNull String userName, @NonNull String email, @NonNull String password, UserAuthListener authListener) {
        this.authListener = authListener;
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    if (authResult.getUser() == null) {
                        onAuthFailure(null);
                        return;
                    }
                    String userId = authResult.getUser().getUid();
                    createNewUserAccount(userId, userName, email, null);
                })
                .addOnFailureListener(this::onAuthFailure);
    }

    public void sendPasswordResetEmail(@NonNull String email) { // TODO: Add a Callback
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> {
                })
                .addOnFailureListener(e -> {
                });
    }

    public void loginWithGoogle(@NonNull ActivityResultLauncher<Intent> googleAuthResultLauncher, UserAuthListener authListener) {
        this.authListener = authListener;
        signInClient.signOut()
                .addOnSuccessListener(result -> googleAuthResultLauncher.launch(signInClient.getSignInIntent()))
                .addOnFailureListener(this::onAuthFailure);
    }

    private void signInWithCredential(@NonNull AuthCredential authCredential) {
        auth.signInWithCredential(authCredential).addOnSuccessListener(authResult -> {
            if (authResult.getUser() == null || authResult.getAdditionalUserInfo() == null) {
                onAuthFailure(null);
                return;
            }
            boolean isNewUser = authResult.getAdditionalUserInfo().isNewUser();
            String userId = authResult.getUser().getUid();
            String userName = authResult.getUser().getDisplayName();
            String userEmail = authResult.getUser().getEmail();
            String userAvatar = String.valueOf(authResult.getUser().getPhotoUrl());
            if (isNewUser) {
                createNewUserAccount(userId, userName, userEmail, userAvatar);
            } else {
                onAuthSuccess(userId);
            }
        }).addOnFailureListener(this::onAuthFailure);
    }

    private void createNewUserAccount(@NonNull String userId, @NonNull String userName, @NonNull String userEmail, @Nullable String userAvatar) {
        HashMap<String, String> userData = new HashMap<>();
        userData.put("id", userId);
        userData.put("name", userName);
        userData.put("email", userEmail);
        userData.put("avatar", userAvatar == null ? USER_DEFAULT_AVATAR : userAvatar);
        userData.put("joined", String.valueOf(System.currentTimeMillis()));

        firestore.collection(COLLECTION_USERS).document(userId).set(userData)
                .addOnSuccessListener(unused -> onAuthSuccess(userId))
                .addOnFailureListener(this::onAuthFailure);
    }

    public void handleActivityLaunchResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_GOOGLE_AUTH) {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    // Proceeding to Sign In With Google
                    signInWithCredential(authCredential);

                } catch (ApiException e) {
                    onAuthFailure(e);
                }
            } else {
                onAuthFailure(null);
            }
        }
    }

    private void onAuthSuccess(@NonNull String userId) {
        firestore.collection(COLLECTION_USERS).document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot != null) {
                        SharedPreferences localDB = context.getSharedPreferences(LOCAL_DB_NAME, Context.MODE_PRIVATE);
                        localDB.edit().putString(KEY_USER_ID, userId).apply();
                        localDB.edit().putString(KEY_USER_NAME, documentSnapshot.getString("name")).apply();
                        authListener.onResult(new UserAuthListener.AuthResult(true, null, userId));
                    }
                })
                .addOnFailureListener(this::onAuthFailure);
    }

    private void onAuthFailure(Exception e) {
        authListener.onResult(new UserAuthListener.AuthResult(false, getErrorMessage(e), null));
    }


    private String getErrorMessage(@Nullable Exception e) {
        if (e instanceof FirebaseAuthException) {
            switch (((FirebaseAuthException) e).getErrorCode()) {
                case "ERROR_INVALID_CUSTOM_TOKEN":
                    return "Invalid custom token";
                case "ERROR_CUSTOM_TOKEN_MISMATCH":
                    return "Custom token mismatch";
                case "ERROR_INVALID_CREDENTIAL":
                    return "Invalid authentication credential";
                case "ERROR_INVALID_EMAIL":
                    return "Invalid email address format";
                case "ERROR_WRONG_PASSWORD":
                    return "Incorrect password";
                case "ERROR_USER_MISMATCH":
                    return "User credentials mismatch";
                case "ERROR_REQUIRES_RECENT_LOGIN":
                    return "Recent authentication required";
                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                case "ERROR_EMAIL_ALREADY_IN_USE":
                    return "Email already in use";
                case "ERROR_USER_DISABLED":
                    return "User account disabled";
                case "ERROR_USER_NOT_FOUND":
                    return "User not found";
                default:
                    return "Authentication failed";
            }
        } else {
            return "Something went wrong";
        }
    }

}
