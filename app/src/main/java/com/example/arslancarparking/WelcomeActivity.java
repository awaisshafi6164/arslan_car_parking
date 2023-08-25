package com.example.arslancarparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    Button get_started_btn;
    TextView create_account_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }


        get_started_btn = findViewById(R.id.lets_get_started_button);
        create_account_tv = findViewById(R.id.create_account_textview);

        get_started_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginPageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String text = create_account_tv.getText().toString();
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                openSignupPage();
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                int black_purple = ContextCompat.getColor(WelcomeActivity.this, R.color.white);
                ds.setColor(black_purple);
                ds.setUnderlineText(false);
            }
        };

        ss.setSpan(clickableSpan, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        create_account_tv.setText(ss);
        create_account_tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void openSignupPage(){
        Intent intent = new Intent(this, SignupPageActivity.class);
        startActivity(intent);
    }

}