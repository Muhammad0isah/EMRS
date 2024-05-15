package com.mtech.envirotrack.report;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mtech.envirotrack.MainActivity;
import com.mtech.envirotrack.R;

import java.util.ArrayList;
import java.util.List;

public class Notification extends Fragment {

    private RecyclerView notificationRecyclerView;
    private static NotificationAdapter notificationAdapter;
    private static List<NotificationModel> notificationList = new ArrayList<>();
    private DatabaseReference mDatabase;
    private OnNewNotificationListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewNotificationListener) {
            mListener = (OnNewNotificationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewNotificationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationAdapter = new NotificationAdapter(notificationList, mListener);
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
        // Set the OnClickListener on the ImageButton
        ImageButton goBackButton = view.findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go back to main activity
                startActivity(new Intent(getActivity(), MainActivity.class));

            }
        });
        ImageView notificationIcon = view.findViewById(R.id.notificationIcon);
        boolean isNotificationOpen = false; // replace this with the actual condition
        notificationIcon.setSelected(isNotificationOpen);


        return view;
    }

    public void addNotification(String title, String message) {
        if (notificationAdapter != null && isAdded()) {
            NotificationModel notification = new NotificationModel(title, message);
            notificationList.add(0, notification); // Add the notification at the beginning of the list
            notificationAdapter.notifyItemInserted(0); // Notify the adapter that an item has been inserted at the beginning
            notificationRecyclerView.scrollToPosition(0); // Scroll to the top of the RecyclerView to show the latest notification

            // Notify the MainActivity that a new notification has arrived
            if (mListener != null) {
                mListener.onNewNotification();
            }
        }
    }

    private static class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
        private List<NotificationModel> notificationList;
        private OnNewNotificationListener mListener;

        NotificationAdapter(List<NotificationModel> notificationList, OnNewNotificationListener mListener) {
            this.notificationList = notificationList;
            this.mListener = mListener;
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
            holder.notificationIcon.setSelected(notification.isOpen());


            // Set an OnClickListener for the item view
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open a new fragment and pass the clicked notification details to it
                    if (mListener != null) {
                        mListener.onNotificationClicked(notification);
                    }
                }
            });
            // Set an OnClickListener for the item view
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open a new fragment and pass the clicked notification details to it
                    if (mListener != null) {
                        mListener.onNotificationClicked(notification);
                    }

                    // Update the isOpen field and notify the adapter
                    notification.setOpen(true);
                    notifyItemChanged(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return notificationList.size();
        }
    }

    private static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView messageTextView;
        ImageView notificationIcon;


        NotificationViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.notificationTitle);
            messageTextView = itemView.findViewById(R.id.notificationMessage);
            notificationIcon = itemView.findViewById(R.id.notificationIcon);

        }
    }

    public interface OnNewNotificationListener {
        void onNewNotification();
        void onNotificationClicked(NotificationModel notification);
    }
}