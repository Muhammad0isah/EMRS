package com.mtech.envirotrack.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mtech.envirotrack.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendNotification extends Fragment {

    private static final String TAG = "SendNotification";
    private static final String YOUR_SERVER_KEY = "AAAAGCJ1eWg:APA91bFlyo4tIEr2j8Ta2qOKdvj6a29uzjkIV00PYwSNeaw0jFIByF0ou00FGN_Eih0yVtie0_Fsf6VqIkw1ojYYitIgtHvlmqrS8F25_B3yTueK2_nUNigsyGac2ROO6-jqP7qRTdUg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_notification, container, false);

        EditText notificationTitle = view.findViewById(R.id.notificationTitle);
        EditText notificationBody = view.findViewById(R.id.notificationBody);
        Button sendButton = view.findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = notificationTitle.getText().toString();
                String body = notificationBody.getText().toString();
                sendNotification("weather", title, body);
            }
        });
        // Subscribe to topic
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscription failed";
                        }
                        Log.d(TAG, msg);
                    }
                });

        return view;
    }

    public void sendNotification(String to, String title, String body) {
        String FCM_API = "https://fcm.googleapis.com/fcm/send";
        String serverKey = "key=" + YOUR_SERVER_KEY;
        String contentType = "application/json";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();

        try {
            notifcationBody.put("title", title);
            notifcationBody.put("message", body);

            notification.put("to", "/topics/" + to);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }
}
