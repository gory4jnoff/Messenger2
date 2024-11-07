package com.goryajnoff.messenger2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    private Button buttonSignUp;
    private EditText editTextEmailRegistration;
    private EditText editTextPasswordRegistration;
    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private RegistrationViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
        observeViewModel();
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmailRegistration.getText().toString().trim();
                String password = editTextPasswordRegistration.getText().toString().trim();
                String name = editTextName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String age = editTextAge.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty() || name.isEmpty() || lastName.isEmpty()
                        || age.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, R.string.fill_in_all_the_fields,
                                    Toast.LENGTH_SHORT)
                            .show();
                } else {
                    viewModel.createUser(email, password,name,lastName,Integer.parseInt(age));
                }

            }
        });
    }


    private void initView() {

        buttonSignUp = findViewById(R.id.buttonSignUp);
        editTextEmailRegistration = findViewById(R.id.editTextEmailRegistration);
        editTextPasswordRegistration = findViewById(R.id.editTextPasswordRegistration);
        editTextName = findViewById(R.id.editTextName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextAge);
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(RegistrationActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Toast.makeText(RegistrationActivity.this, "success", Toast.LENGTH_SHORT)
                            .show();
                    startActivity(UsersActivity.newIntent(RegistrationActivity.this,firebaseUser.getUid()));
                    finish();

                }
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, RegistrationActivity.class);
    }
}