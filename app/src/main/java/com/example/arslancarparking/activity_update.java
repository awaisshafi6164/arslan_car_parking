package com.example.arslancarparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_update extends AppCompatActivity {

    Button updateButton;
    EditText updateName, updateRegister, updateFee, updatePhone;
    String name, register, fee, phone, categoryTemp, key;
    DatabaseReference databaseReference;
    Spinner spinner;
    ArrayAdapter<String> adapterCategory;

    int checkPaid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateButton = findViewById(R.id.updateButton);
        updateName = findViewById(R.id.updateName);
        updateRegister = findViewById(R.id.updateRegister);
        updateFee = findViewById(R.id.updateFee);
        updatePhone = findViewById(R.id.updatePhone);
        spinner = findViewById(R.id.spinner);

        ///////Spinner code Below
        ////////////Spinner Code Below
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryTemp = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                categoryTemp = null;
            }
        });
        ///////////////////////////

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            updateName.setText(bundle.getString("Name"));
            updateRegister.setText(bundle.getString("Register"));
            updateFee.setText(bundle.getString("Price"));
            updatePhone.setText(bundle.getString("Phone"));

            String defaultCategory = bundle.getString("Category");
            int defaultCategoryPosition = adapter.getPosition(defaultCategory);// category.indexOf(defaultCategory);
            if (defaultCategoryPosition != -1) {
                spinner.setSelection(defaultCategoryPosition);
                categoryTemp = defaultCategory;
            }

            key = bundle.getString("Key");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Arslan Car parking").child(key);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Message Send Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateData(){
        name = updateName.getText().toString().trim();
        register = updateRegister.getText().toString().trim();
        fee = updateFee.getText().toString().trim();
        phone = updatePhone.getText().toString().trim();
        String category = categoryTemp;

        if(category.equals("Paid")){
            String message = "You have Paid "+name+", "+register+" fees.\nArslan Car Parking"; // Customize the message
            sendSMS(phone, message);
        }

        DataClass dataClass = new DataClass(name, register, fee, phone, category);
        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(activity_update.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity_update.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

