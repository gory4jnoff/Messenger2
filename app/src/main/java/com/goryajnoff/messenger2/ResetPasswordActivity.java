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

public class ResetPasswordActivity extends AppCompatActivity {

    private Button buttonReset;
    private ResetViewModel viewModel;
    private EditText editTextEmailReset;
    private static final String EXTRA_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initViews();
        observeViewModel();
        editTextEmailReset.setText(getIntent().getStringExtra(EXTRA_EMAIL));
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmailReset.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Fill in all the fields",
                                    Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.resetPassword(email);
                }
            }
        });

    }

    private void initViews() {
        buttonReset = findViewById(R.id.buttonReset);
        editTextEmailReset = findViewById(R.id.editTextEmailReset);
        viewModel = new ViewModelProvider(this).get(ResetViewModel.class);
    }

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }
    private void observeViewModel(){
        viewModel.getIsReset().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(ResetPasswordActivity.this, R.string.reset_link_sent,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(ResetPasswordActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        });
    }
}