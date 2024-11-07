package com.goryajnoff.messenger2;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Closeable;

public class ResetViewModel extends ViewModel {
    private FirebaseAuth auth;
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Boolean> isReset = new MutableLiveData<>();

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getIsReset() {
        return isReset;
    }

    public ResetViewModel() {
        auth = FirebaseAuth.getInstance();
    }

    public void resetPassword(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        isReset.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isReset.setValue(false);
                        error.setValue(e.getMessage());
                    }
                });
    }
}
