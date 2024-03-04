package com.mtech.envirotrack.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mtech.envirotrack.R;
import com.mtech.envirotrack.report.NotificationModel;

public class SendNotification extends Fragment {

    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_notification, container, false);

        EditText notificationTitle = view.findViewById(R.id.notificationTitle);
        EditText notificationBody = view.findViewById(R.id.notificationBody);
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
        mDatabase.child("notifications").push().setValue(notification);
    }
}