package com.example.arslancarparking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    ImageButton refresh;
    Button fab;
    Dialog dialogPaidtoUnpaid;
    Button paidToUnpaidBTN;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    SearchView searchView;
    Spinner spinner;
    ArrayAdapter<String> adapterCategory;
    String spinnerCat;
    MyAdapter adapter;

    AlertDialog dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Start the UnpaidMessageService
        Intent serviceIntent = new Intent(this, UnpaidMessageService.class);
        startService(serviceIntent);
        //

        firebaseAuth = FirebaseAuth.getInstance();
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Other code for your HomeActivity
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();


        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        adapter = new MyAdapter(HomeActivity.this, dataList);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Arslan Car parking");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for(DataSnapshot itemSnapShot: snapshot.getChildren()){
                    DataClass dataClass = itemSnapShot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapShot.getKey());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                searchList(s);
                return true;
            }
        });

        //Spinner Code

        ArrayList<String> spinnerCategory = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.spinner_options)));

        // Your existing code to add "All" category


        spinner = findViewById(R.id.spinner);
        adapterCategory = new ArrayAdapter<String>(this, R.layout.list_category, spinnerCategory);
        spinner.setAdapter(adapterCategory);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerCat = adapterView.getItemAtPosition(i).toString();

                ArrayList<DataClass> spinnerList = new ArrayList<>();
                if (spinnerCat.equalsIgnoreCase("All")) {
                    spinnerList.addAll(dataList); // Show all data when "All" category is selected
                } else {
                    for (DataClass dataClass : dataList) {
                        // Filter based on "Paid" or "Un-Paid" category
                        if (dataClass.getProductCategory().equalsIgnoreCase(spinnerCat)) {
                            spinnerList.add(dataClass);
                        }
                    }
                }
                adapter.searchDataList(spinnerList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerCat = null;
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog2.show();
                Intent intent = new Intent(HomeActivity.this,activity_upload.class);
                startActivity(intent);
            }
        });

        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshMethod();
            }
        });

        //Create the Dialog here
        dialogPaidtoUnpaid = new Dialog(this);
        dialogPaidtoUnpaid.setContentView(R.layout.dialog_paidtounpaid);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogPaidtoUnpaid.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialogPaidtoUnpaid.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogPaidtoUnpaid.setCancelable(false); //Optional
        dialogPaidtoUnpaid.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button Yes = dialogPaidtoUnpaid.findViewById(R.id.btn_yes);
        Button Cancel = dialogPaidtoUnpaid.findViewById(R.id.btn_cancel);

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change all "Paid" category items to "Un-Paid"
                for (DataClass data : dataList) {
                    if ("Paid".equalsIgnoreCase(data.getProductCategory())) {
                        databaseReference.child(data.getKey()).child("productCategory").setValue("Un-Paid");
                    }
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(HomeActivity.this, "Changed Paid to Un-Paid", Toast.LENGTH_SHORT).show();
                dialogPaidtoUnpaid.dismiss();
            }
        });


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                dialogPaidtoUnpaid.dismiss();
            }
        });

        paidToUnpaidBTN = findViewById(R.id.paidToUnpaid);
        paidToUnpaidBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPaidtoUnpaid.show();
            }
        });

        //
        //sendUnpaidMessages();
        //

    }

//    private void sendUnpaidMessages() {
//        //
//        final int DELAY_SECONDS = 30;// 129600 sec == 36 hrs
//        final Handler handler = new Handler();
//        Runnable sendSmsRunnable = new Runnable() {
//            @Override
//            public void run() {
//                for (DataClass data : dataList) {
//                    if ("Un-Paid".equalsIgnoreCase(data.getProductCategory())) {
//                        String phoneNumber = data.getProductPhone(); // Get the user's phone number
//                        String carName = data.getProductName();
//                        String carRegister = data.getProductRegistration();
//                        String carFee = data.getProductPrice();
//                        String message = "You didn't Paid "+carName+", "+carRegister+" fees.\nYour car fees is "+carFee+" \nArslan Car Parking"; // Customize the message
//
//                        sendSMS(phoneNumber, message);
//                    }
//                }
//            }
//        };
//        handler.postDelayed(sendSmsRunnable, DELAY_SECONDS * 1000);//delay handler
//        //
//    }
//
//    private void sendSMS(String phoneNumber, String message) {
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
//            Toast.makeText(this, "Message Send Successfully", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void refreshMethod() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void searchList(String text){
        ArrayList<DataClass> searchList = new ArrayList<>();
        for(DataClass dataClass: dataList){
            if( (dataClass.getProductName().toLowerCase().contains(text.toLowerCase())) || (dataClass.getProductRegistration().contains(text) ) && dataClass.getProductCategory().toLowerCase().contains(spinnerCat.toLowerCase()) ){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }

    private void logout() {
        // Sign out the user
        firebaseAuth.signOut();

        // After signing out, navigate to your login or welcome screen
        Intent intent = new Intent(HomeActivity.this, LoginPageActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}