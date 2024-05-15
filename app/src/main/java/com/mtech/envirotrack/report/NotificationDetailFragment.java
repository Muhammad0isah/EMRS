package com.mtech.envirotrack.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.mtech.envirotrack.R;

public class NotificationDetailFragment extends Fragment {

    private TextView titleTextView;
    private TextView messageTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_detail, container, false);

        // Initialize the TextViews
        titleTextView = view.findViewById(R.id.titleTextView);
        messageTextView = view.findViewById(R.id.messageTextView);

        // Retrieve the notification details from the arguments
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("title");
            String message = args.getString("message");

            // Display the notification details
            titleTextView.setText(title);
            messageTextView.setText(message);
        }

        // Set the OnClickListener on the ImageButton
        ImageButton goBackButton = view.findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the NotificationFragment
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the Up button press
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}