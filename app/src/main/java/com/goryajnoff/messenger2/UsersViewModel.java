package com.goryajnoff.messenger2;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends ViewModel {
    private FirebaseAuth auth;
    private static final String TAG = "UsersViewModel.class";
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference referenceUsers = firebaseDatabase.getReference("Users");
    private MutableLiveData<List<User>> users = new MutableLiveData<>();

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public UsersViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user.setValue(firebaseAuth.getCurrentUser());

            }
        });
        referenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser == null) {
                    return;
                }
                List<User> listUser = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user == null) {
                        return;
                    }
                    if (!user.getId().equals(currentUser.getUid())) {
                        listUser.add(user);
                    }
                }
                users.setValue(listUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        });

    }

    public void logOut() {
        setUserOnline(false);
        auth.signOut();
    }

    public void setUserOnline(Boolean status) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        referenceUsers.child(firebaseUser.getUid()).child("online").setValue(status);
    }


}
