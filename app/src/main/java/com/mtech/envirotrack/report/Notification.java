package com.mtech.envirotrack.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mtech.envirotrack.R;

import java.util.ArrayList;
import java.util.List;

public class Notification extends Fragment {

    private RecyclerView notificationRecyclerView;
    private static NotificationAdapter notificationAdapter;
    private static List<NotificationModel> notificationList = new ArrayList<>();
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationAdapter = new NotificationAdapter(notificationList);
        notificationRecyclerView.setAdapter(notificationAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        notificationList.clear();

        mDatabase.child("notifications").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NotificationModel notification = dataSnapshot.getValue(NotificationModel.class);
                addNotification(notification.getTitle(), notification.getMessage());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return view;
    }

    public void addNotification(String title, String message) {
        if (notificationAdapter != null && isAdded()) {
            NotificationModel notification = new NotificationModel(title, message);
            notificationList.add(0, notification); // Add the notification at the beginning of the list
            notificationAdapter.notifyItemInserted(0); // Notify the adapter that an item has been inserted at the beginning
            notificationRecyclerView.scrollToPosition(0); // Scroll to the top of the RecyclerView to show the latest notification
        }
    }

    private static class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
        private List<NotificationModel> notificationList;

        NotificationAdapter(List<NotificationModel> notificationList) {
            this.notificationList = notificationList;
        }

        @Override
        public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
            return new NotificationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NotificationViewHolder holder, int position) {
            NotificationModel notification = notificationList.get(position);
            holder.titleTextView.setText(notification.getTitle());
            holder.messageTextView.setText(notification.getMessage());
        }

        @Override
        public int getItemCount() {
            return notificationList.size();
        }
    }

    private static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView messageTextView;

        NotificationViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.notificationTitle);
            messageTextView = itemView.findViewById(R.id.notificationMessage);
        }
    }
}