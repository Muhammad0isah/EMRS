package com.mtech.envirotrack.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mtech.envirotrack.R;

import java.util.ArrayList;
import java.util.Map;

//public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

//    private ArrayList<User> users;
//
//    public ReportAdapter(ArrayList<User> users) {
//        this.users = users;
//    }
//
//    @NonNull
//    @Override
//    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
//        return new ReportViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
//        User user = users.get(position);
//        holder.bind(user);
//    }
//
//    @Override
//    public int getItemCount() {
//        return users.size();
//    }
//
//    static class ReportViewHolder extends RecyclerView.ViewHolder {
//
//        TextView userNameTextView;
//        TextView userReportTextView;
//
//        public ReportViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userNameTextView = itemView.findViewById(R.id.userNameTextView);
//            userReportTextView = itemView.findViewById(R.id.userReportTextView);
//        }
//
//        public void bind(User user) {
//            userNameTextView.setText(user.getName());
//            StringBuilder reportBuilder = new StringBuilder();
//            for (Map.Entry<String, String> entry : user.getReport().entrySet()) {
//                reportBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
//            }
//            userReportTextView.setText(reportBuilder.toString());
//        }
//    }
//}