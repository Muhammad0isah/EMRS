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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

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
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(dataString)
                    .setCancelable(true)
                    .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
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

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            reportNumber = itemView.findViewById(R.id.report_number);
            userEmail = itemView.findViewById(R.id.email);
            userName = itemView.findViewById(R.id.name);
            impactType = itemView.findViewById(R.id.impact_type);
            serialNumber = itemView.findViewById(R.id.serial_number);
            attachment = itemView.findViewById(R.id.attachment);
        }
    }
}