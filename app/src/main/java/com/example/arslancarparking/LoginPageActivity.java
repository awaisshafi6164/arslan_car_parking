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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPageActivity extends AppCompatActivity {

    private TextInputLayout emailTextInput;
    private TextInputLayout passwordTextInput;
    Button loginButton;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        //Text Input Field Validation Checker.
        emailTextInput = findViewById(R.id.email_text_input_layout);
        passwordTextInput = findViewById(R.id.password_text_input_layout);

        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();


        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if(confirmInput()){

                    firebaseAuth.signInWithEmailAndPassword(emailTextInput.getEditText().getText().toString(),
                                    passwordTextInput.getEditText().getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);

                                    if(task.isSuccessful()){
                                        if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                            Intent intent = new Intent(LoginPageActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            progressBar.setVisibility(View.GONE);
                                            finish();
                                        }else{
                                            Toast.makeText(LoginPageActivity.this, "Please verify your email address.", Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(LoginPageActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        //Forget Password Button CLick
        TextView forgetPassword = findViewById(R.id.forgetPassword_TV);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPageActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });


        TextView sign_up_textview = findViewById(R.id.sign_up_textview);
        String text = sign_up_textview.getText().toString();
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                openSignupPage();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.YELLOW);
                ds.setUnderlineText(false);
                ds.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        };

        ss.setSpan(clickableSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sign_up_textview.setText(ss);
        sign_up_textview.setMovementMethod(LinkMovementMethod.getInstance());
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

    private boolean validatePassword(){
        String passwordInput = passwordTextInput.getEditText().getText().toString().trim();

        if(passwordInput.isEmpty()){
            passwordTextInput.setError("Password Cannot be empty");
            return false;
        } else {
            passwordTextInput.setError(null);
            //paswordTextInput.setErrorEnabled(false);
            return true;
        }
    }

    public boolean confirmInput(){
        if(!validateEmail() | !validatePassword()){
            return false;
        }

        return true;
    }

    public void openSignupPage(){
        Intent intent = new Intent(this, SignupPageActivity.class);
        startActivity(intent);
    }
}