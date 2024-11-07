package com.goryajnoff.messenger2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity.class";
    private MainActivityViewModel viewModel;
    private EditText editTextEmailAddress;
    private EditText editTextPassword;
    private Button buttonLogIn;
    private TextView textViewForgot;
    private TextView textViewRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        observeViewModel();
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editTextPassword.getText().toString().trim();
                String email = editTextEmailAddress.getText().toString().trim();
                if(password.isEmpty()||email.isEmpty()){
                    Toast.makeText(MainActivity.this,R.string.fill_in_all_the_fields,
                            Toast.LENGTH_SHORT).show();
                }else{viewModel.logIn(email, password);}
            }
        });
        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmailAddress.getText().toString().trim();
                Intent intent = ResetPasswordActivity.newIntent(MainActivity.this, email);
                startActivity(intent);
            }
        });
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RegistrationActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });


    }


    private void observeViewModel() {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                    Log.d(TAG,s);
                }
            }
        });
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    startActivity(UsersActivity.newIntent(MainActivity.this,firebaseUser.getUid()));
                    finish();
                }
            }
        });
    }


    private void initViews() {
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogIn = findViewById(R.id.buttonLogIn);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        textViewForgot = findViewById(R.id.textViewForgot);
        textViewRegister = findViewById(R.id.textViewRegister);
    }
    public static Intent newIntent(Context context){
        return new Intent(context,MainActivity.class);
    }
}