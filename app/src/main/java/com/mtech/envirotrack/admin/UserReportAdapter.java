package com.mtech.envirotrack.admin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mtech.envirotrack.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserReportAdapter extends RecyclerView.Adapter<UserReportAdapter.ReportViewHolder> {
    private List<User> users;

    public UserReportAdapter(List<User> users) {
        this.users = users;
    }

    public void updateData(List<User> newReports) {
        this.users = newReports;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        User user = users.get(position);
        holder.reportNumber.setText(user.getReportNumber());
        holder.userEmail.setText(user.getUserEmail());
        holder.userName.setText(user.getUserName());
        holder.impactType.setText(user.getImpactType());
        holder.serialNumber.setText(String.valueOf(user.getSerialNumber()));
        holder.status.setText(user.getStatus());
        SpannableStringBuilder attachmentsBuilder = new SpannableStringBuilder();
        for (Map.Entry<String, String> entry : user.getAttachments().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            attachmentsBuilder.append(key);
            attachmentsBuilder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
                    widget.getContext().startActivity(intent);
                }
            }, attachmentsBuilder.length() - key.length(), attachmentsBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            attachmentsBuilder.append(", ");
        }
        holder.attachment.setText(attachmentsBuilder);
        holder.attachment.setMovementMethod(LinkMovementMethod.getInstance());
        holder.impactType.setPaintFlags(holder.impactType.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.impactType.setTextColor(Color.parseColor("#006400"));
        holder.impactType.setOnClickListener(v -> {
            String dataString = (user.getData() == null) ? "No data" : user.formatData();
            // Inflate the custom layout
            LayoutInflater inflater = LayoutInflater.from(v.getContext());
            View dialogView = inflater.inflate(R.layout.report_status_diaglog, null);

            // Create the AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setView(dialogView)
                    .setMessage(dataString)
                    .setCancelable(true);
            AlertDialog alert = builder.create();
            // Get the Spinner and Button from the custom layout
            Spinner statusSpinner = dialogView.findViewById(R.id.status_spinner);
            Button updateStatusButton = dialogView.findViewById(R.id.update_status_button);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                    R.array.status_array, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            statusSpinner.setAdapter(adapter);
            // Set an onClickListener for the Button
            statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedStatus = parent.getItemAtPosition(position).toString();
                    updateStatusButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Update the status in Firebase
                            DatabaseReference reportRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUserId()).child("reports").child(user.getReportNumber());
                            reportRef.child("status").setValue(selectedStatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Status was updated successfully
                                    Toast.makeText(v.getContext(), "Status updated successfully", Toast.LENGTH_SHORT).show();
                                    user.setStatus(selectedStatus); // Update the status in the local object
                                    notifyDataSetChanged(); // Notify the adapter that the data has changed

                                    // Send an email to the user
                                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                    emailIntent.setType("text/plain");
                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{user.getUserEmail()});
                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "EMRS Report Status Update");
                                    String emailBody = "Dear " + user.getUserName() + ",\n\nYour " + user.getImpactType() + " report with number " + user.getReportNumber() + " has been received. It is currently " + selectedStatus + ".\n\nThank you for your patience as we work on this.\n\nBest regards,\nEMRS Team";
                                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                                    v.getContext().startActivity(Intent.createChooser(emailIntent, "Send Email"));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Status update failed
                                    Toast.makeText(v.getContext(), "Status update failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing
                }
            });

        alert.show();
        });
    }
    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView reportNumber;
        TextView userEmail;
        TextView userName;
        TextView impactType;
        TextView serialNumber;
        TextView attachment;
        TextView status;


        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            reportNumber = itemView.findViewById(R.id.report_number);
            userEmail = itemView.findViewById(R.id.email);
            userName = itemView.findViewById(R.id.name);
            impactType = itemView.findViewById(R.id.impact_type);
            serialNumber = itemView.findViewById(R.id.serial_number);
            attachment = itemView.findViewById(R.id.attachment);
            status = itemView.findViewById(R.id.status);

        }
    }
}