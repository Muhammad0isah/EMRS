package com.mtech.envirotrack.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mtech.envirotrack.R;
import com.mtech.envirotrack.report.NotificationModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SendNotification extends Fragment {

    private DatabaseReference mDatabase;
    private EditText notificationTitle;
    private EditText notificationBody;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_notification, container, false);

        notificationTitle = view.findViewById(R.id.notificationTitle);
        notificationBody = view.findViewById(R.id.notificationBody);
        Button sendButton = view.findViewById(R.id.sendButton);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = notificationTitle.getText().toString();
                String body = notificationBody.getText().toString();
                sendNotificationToDatabase(title, body);
            }
        });

        return view;
    }

    public void sendNotificationToDatabase(String title, String body) {
        // Get current date and time
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String dateTimeString = formatter.format(currentDate);

        // Append date and time to the notification body
        body = body + "\n" + dateTimeString;
        NotificationModel notification = new NotificationModel(title, body);
        mDatabase.child("notifications").push().setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Clear the EditText fields
                notificationTitle.setText("");
                notificationBody.setText("");

                // Display a success dialog
                new AlertDialog.Builder(getContext())
                        .setTitle("Success")
                        .setMessage("Notification sent successfully")
                        .setPositiveButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.checkbox_on_background)
                        .show();
            }
        });
    }
}