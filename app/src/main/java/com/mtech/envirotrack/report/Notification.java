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
    private NotificationAdapter notificationAdapter;
    private List<NotificationModel> notificationList = new ArrayList<>();
    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra(NotificationService.EXTRA_TITLE);
            String message = intent.getStringExtra(NotificationService.EXTRA_MESSAGE);
            FirebaseInAppMessaging inAppMessaging = FirebaseInAppMessaging.getInstance();
            addNotification(title, message, inAppMessaging);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationAdapter = new NotificationAdapter(notificationList);
        notificationRecyclerView.setAdapter(notificationAdapter);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(notificationReceiver,
                new IntentFilter(NotificationService.ACTION_NOTIFICATION_RECEIVED));
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(notificationReceiver);
    }

    public void addNotification(String title, String message, FirebaseInAppMessaging inAppMessaging) {
        NotificationModel notification = new NotificationModel(title, message, inAppMessaging);
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

        NotificationModel(String title, String message, FirebaseInAppMessaging inAppMessaging) {
            this.title = title;
            this.message = message;
            this.inAppMessaging = inAppMessaging;
        }

        String getTitle() {
            return title;
        }

        String getMessage() {
            return message;
        }

        FirebaseInAppMessaging getInAppMessaging() {
            return inAppMessaging;
        }
    }
}