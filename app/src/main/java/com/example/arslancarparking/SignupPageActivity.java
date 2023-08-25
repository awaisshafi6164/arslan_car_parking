package com.example.arslancarparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignupPageActivity extends AppCompatActivity {

    private TextInputLayout fullNameTextInput;
    private TextInputLayout emailTextInput;
    private TextInputLayout passwordTextInput;
    private TextInputLayout confirmPasswordTextInput;

    ProgressBar progressBar;
    Button registerButton;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        fullNameTextInput = findViewById(R.id.fullname_text_input_layout);
        emailTextInput = findViewById(R.id.email_text_input_layout);
        passwordTextInput = findViewById(R.id.password_text_input_layout);
        confirmPasswordTextInput = findViewById(R.id.confirmPassword_text_input_layout);
        progressBar = findViewById(R.id.progressBar);
        registerButton = findViewById(R.id.register_button);

        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmInput()){
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(Objects.requireNonNull(emailTextInput.getEditText()).getText().toString(),
                                    Objects.requireNonNull(passwordTextInput.getEditText()).getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(SignupPageActivity.this, "Registered Successfully! Please check your E-mail for verification.", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(SignupPageActivity.this, LoginPageActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }else{
                                                    Toast.makeText(SignupPageActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(SignupPageActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }

                            });
                }
            }
        });

        ImageButton imageButton = findViewById(R.id.back_arrow_Button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupPageActivity.this, LoginPageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView login_textview = findViewById(R.id.login_textview);
        String text = login_textview.getText().toString();
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                openLoginPage();
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE);
                ds.setUnderlineText(false);
                ds.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        };

        ss.setSpan(clickableSpan, 24, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        login_textview.setText(ss);
        login_textview.setMovementMethod(LinkMovementMethod.getInstance());

    }

    public void openLoginPage() {
        Intent intent = new Intent(this, LoginPageActivity.class);
        startActivity(intent);
    }

    private boolean validateFullName() {
        String fullNameInput = fullNameTextInput.getEditText().getText().toString().trim();

        if (fullNameInput.isEmpty()) {
            fullNameTextInput.setError("Full Name cannot be empty");
            return false;
        } else {
            fullNameTextInput.setError(null);
            //emailTextInput.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String emailInput = emailTextInput.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            emailTextInput.setError("Email Cannot be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailTextInput.setError("Please enter a valid Email address.");
            return false;
        } else {
            emailTextInput.setError(null);
            //emailTextInput.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = passwordTextInput.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            passwordTextInput.setError("Password Cannot be empty");
            return false;
        } else {
            passwordTextInput.setError(null);
            //paswordTextInput.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String confirmPasswordInput = confirmPasswordTextInput.getEditText().getText().toString().trim();
        String passwordInput = passwordTextInput.getEditText().getText().toString().trim();

        if (confirmPasswordInput.isEmpty()) {
            confirmPasswordTextInput.setError("Confirm Password Cannot be empty");
            return false;
        } else if (!confirmPasswordInput.equals(passwordInput)) {
            confirmPasswordTextInput.setError("Confirm Password didn't match with Password");
            return false;
        } else {
            confirmPasswordTextInput.setError(null);
            //confirmPasswordTextInput.setErrorEnabled(false);
            return true;
        }
    }

    public boolean confirmInput() {
        if (!validateFullName() | !validateEmail() | !validatePassword() | !validateConfirmPassword()) {
            return false;
        }
        return true;
    }

    public void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}