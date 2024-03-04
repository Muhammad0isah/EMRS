package com.mtech.envirotrack.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mtech.envirotrack.R;
import com.mtech.envirotrack.report.NotificationModel;

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
        NotificationModel notification = new NotificationModel(title, body);
        mDatabase.child("notifications").push().setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Clear the EditText fields
                notificationTitle.setText("");
                notificationBody.setText("");

                // Display a Toast message
                Toast.makeText(getActivity(), "Notification sent successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}