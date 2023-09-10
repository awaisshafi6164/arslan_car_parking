package com.example.arslancarparking;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UnpaidMessageService extends Service {
    List<DataClass> dataList;
    ValueEventListener eventListener;
    DatabaseReference databaseReference;
    private Handler handler;
    private Runnable runnable;
    private final int DELAY_SECONDS = 5 * 60 * 1000; // 36 hours in milliseconds --- 36 * 60 * 60 * 1000

    @Override
    public void onCreate() {
        super.onCreate();

        startForegroundService();

        Log.d("service", "Service onCreate");

        dataList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Arslan Car parking");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for(DataSnapshot itemSnapShot: snapshot.getChildren()){
                    DataClass dataClass = itemSnapShot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapShot.getKey());
                    dataList.add(dataClass);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("service", "runnable run function");
                sendUnpaidMessages();
                handler.postDelayed(this, DELAY_SECONDS);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "onStartCommand");
        handler.postDelayed(runnable, DELAY_SECONDS);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("service", "onDestroy");
        handler.removeCallbacks(runnable);
        Log.d("service", "onDestroy remove call back");

        // Stop the foreground service and remove the notification
        stopForeground(true);
    }

    private void sendUnpaidMessages() {
        Log.d("service", "sendUnPaidMessage Function called");
        //
//        final int DELAY_SECONDS = 10;// 129600 sec == 36 hrs
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
//                        String message = "You fees for "+carName+" Car,\n"+carRegister+" is due.\nYour fees is "+carFee+"rupees\n\nArslan Car Parking"; // Customize the message
//                        sendSMS(phoneNumber, message);
//                    }
//                }
//            }
//        };
//        handler.postDelayed(sendSmsRunnable, DELAY_SECONDS * 1000);//delay handler

        for (DataClass data : dataList) {
            if ("Un-Paid".equalsIgnoreCase(data.getProductCategory())) {
                String phoneNumber = data.getProductPhone(); // Get the user's phone number
                String carName = data.getProductName();
                String carRegister = data.getProductRegistration();
                String carFee = data.getProductPrice();
                String message = "You fees for "+carName+" Car,\n"+carRegister+" is due.\nYour fees is "+carFee+"rupees\n\nArslan Car Parking"; // Customize the message
                sendSMS(phoneNumber, message);
            }
        }

        //
    }

    private void sendSMS(String phoneNumber, String message) {
        Log.d("service", "sendSMS functions called");
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Message Send Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a notification channel for Android Oreo and higher
            String channelId = "unpaid_messages";
            CharSequence channelName = "Unpaid Messages";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            // Create the notification
            Notification notification = new Notification.Builder(this, channelId)
                    .setContentTitle("Unpaid Messages")
                    .setContentText("Service is running")
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .build();

            startForeground(1, notification);
        }
    }

}