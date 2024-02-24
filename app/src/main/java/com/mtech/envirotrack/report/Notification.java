package com.mtech.envirotrack.report;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.mtech.envirotrack.R;

import java.util.ArrayList;
import java.util.List;

public class Notification extends Fragment {


        private RecyclerView notificationRecyclerView;
        private static NotificationAdapter notificationAdapter;
        private static List<NotificationModel> notificationList = new ArrayList<>();


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_notification, container, false);
            notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);
            notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            notificationAdapter = new NotificationAdapter(notificationList);
            notificationRecyclerView.setAdapter(notificationAdapter);
            addNotification("Welcome to EnviroTrack", "You will receive notifications here");
            return view;
        }
        public static void addNotification(String title, String message) {
            NotificationModel notification = new NotificationModel(title, message);
            notificationList.add(notification);
            notificationAdapter.notifyItemInserted(notificationList.size() - 1);
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

    private static class NotificationModel {

        private String title;
        private String message;
        private FirebaseInAppMessaging inAppMessaging;

        NotificationModel(String title, String message) {
            this.title = title;
            this.message = message;
        }

        String getTitle() {
            return title;
        }

        String getMessage() {
            return message;
        }

    }
}