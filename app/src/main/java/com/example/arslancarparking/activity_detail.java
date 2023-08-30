package com.example.arslancarparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class activity_detail extends AppCompatActivity {

    ImageView detailImage;
    TextView detailName, detailRegister, detailPrice, detailPhone, detailCateg;

    Button deleteButton, editButton;
    String key = "";
    String imageUrl = "";
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailName = findViewById(R.id.detailName);
        detailRegister = findViewById(R.id.detailRegistar);
        detailPrice = findViewById(R.id.detailPrice);
        detailPhone = findViewById(R.id.detailPhone);
        detailCateg = findViewById(R.id.detailCatg);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            detailName.setText(bundle.getString("Name"));
            detailRegister.setText(bundle.getString("Register"));
            detailPrice.setText(bundle.getString("Price"));
            detailPhone.setText(bundle.getString("Phone"));
            detailCateg.setText(bundle.getString("Category"));
            key = bundle.getString("Key");
        }

        //String cat = detailCateg.getText().toString();
        //ArrayList<String> categoryList = CategoryManager.getCategoryList(this);

//        int position = categoryList.indexOf(cat);
//        if (position != -1) {
//            String toastMessage = "Position of " + cat + " is " + position;
//            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Position: "+position+" Category not found", Toast.LENGTH_SHORT).show();
//        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity_detail.this, "Deleting...", Toast.LENGTH_SHORT).show();

                deleteData(key);

                ////////////////////// Delete Code
//                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Tutorials");
//                FirebaseStorage storage = FirebaseStorage.getInstance();
//
//                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
//                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        reference.child(key).removeValue();
//                        Toast.makeText(activity_detail.this, "Deleted", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        finish();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(activity_detail.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity_detail.this, "Editing...", Toast.LENGTH_SHORT).show();

                ////////////////////// Edit Code
                Intent intent = new Intent(activity_detail.this, activity_update.class)
                        .putExtra("Name", detailName.getText().toString())
                        .putExtra("Register", detailRegister.getText().toString())
                        .putExtra("Price", detailPrice.getText().toString())
                        .putExtra("Phone", detailPhone.getText().toString())
                        .putExtra("Category", detailCateg.getText().toString())
                        .putExtra("Key", key);
                startActivity(intent);
                finish();
            }
        });

    }

    public void deleteData(String registrationNo) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Arslan Car parking");
        databaseReference.child(registrationNo).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(activity_detail.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(activity_detail.this, "Failed to delete data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}