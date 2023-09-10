package com.example.arslancarparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class activity_upload extends AppCompatActivity {

    Button saveButton;
    EditText productNameET, productRegistrationNo, productPriceET, productPhoneET;

    String categoryTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        productNameET = findViewById(R.id.uploadName);
        productRegistrationNo = findViewById(R.id.uploadRegisterNo);
        productPriceET = findViewById(R.id.uploadfee);
        productPhoneET = findViewById(R.id.uploadPhoneNumber);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = productNameET.getText().toString();
                String registar = productRegistrationNo.getText().toString();
                String price = productPriceET.getText().toString();
                String phone = productPhoneET.getText().toString();
                if(!name.isEmpty() && !registar.isEmpty() && !price.isEmpty() && !phone.isEmpty()){
                    saveData();
                }else{
                    Toast.makeText(activity_upload.this, "Kindly Fill all the values", Toast.LENGTH_SHORT).show();
                }
                
            }
        });

        ////////////Spinner Code Below
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Optionally, you can set an item selected listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryTemp = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoryTemp = null;
            }
        });
        ////////////////////////

    }

    public void saveData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_upload.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        uploadData(); // Start uploading the data without the image
        dialog.dismiss();
    }

    public void uploadData() {
        String name = productNameET.getText().toString();
        String registar = productRegistrationNo.getText().toString();
        String price = productPriceET.getText().toString();
        String phone = productPhoneET.getText().toString();
        String category = categoryTemp;

        DataClass dataClass;
        dataClass = new DataClass(name, registar, price, phone, category);

        FirebaseDatabase.getInstance().getReference("Arslan Car parking").child(registar)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity_upload.this, "Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(activity_upload.this, activity_upload.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_upload.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}