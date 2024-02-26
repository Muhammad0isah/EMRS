package com.mtech.envirotrack.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mtech.envirotrack.R;

import java.util.List;

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
        holder.serialNumber.setText(String.valueOf(user.getSerialNumber())); // Set serial number
        holder.attachment.setText(user.getAttachment()); // Set attachment URL
        holder.impactType.setOnClickListener(v -> {
            // Check if data is null before calling toString()
            String dataString = (user.getData() == null) ? "No data" : user.getData().toString();
            Toast.makeText(v.getContext(), dataString, Toast.LENGTH_LONG).show();
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
        TextView serialNumber; // Add this line
        TextView attachment; // Add this line

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            reportNumber = itemView.findViewById(R.id.report_number);
            userEmail = itemView.findViewById(R.id.email);
            userName = itemView.findViewById(R.id.name);
            impactType = itemView.findViewById(R.id.impact_type);
            serialNumber = itemView.findViewById(R.id.serial_number); // Add this line
            attachment = itemView.findViewById(R.id.attachment); // Add this line
        }
    }
}