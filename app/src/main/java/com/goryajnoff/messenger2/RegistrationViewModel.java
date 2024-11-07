package com.goryajnoff.messenger2;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationViewModel extends ViewModel {
    private FirebaseAuth auth;
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("Users");


    public LiveData<String> getError() {
        return error;
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public RegistrationViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user.setValue(firebaseAuth.getCurrentUser());
            }
        });
    }

    public void createUser(String email,String password,String name,String lastName,int age){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if(authResult.getUser()==null){
                            return;
                        }
                        String id =  authResult.getUser().getUid();
                        User user = new User(id,false,name,lastName,age);
                        databaseReference.child(id).setValue(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                error.setValue(e.getMessage());
            }
        });


    }
}
