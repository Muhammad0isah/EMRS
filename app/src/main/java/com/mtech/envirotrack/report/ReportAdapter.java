package com.mtech.envirotrack.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mtech.envirotrack.R;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private Context mContext;
    private List<UserReport> mReportList;

    public ReportAdapter(Context context, List<UserReport> reportList) {
        this.mContext = context;
        this.mReportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.report_item, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        UserReport report = mReportList.get(position);
        holder.impactTypeTextView.setText(report.getImpactType());
        holder.dateSubmittedTextView.setText(report.getReportNumber());
    }

    @Override
    public int getItemCount() {
        return mReportList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        public TextView dateSubmittedTextView;
        public TextView impactTypeTextView;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            impactTypeTextView = itemView.findViewById(R.id.impact_type);
            dateSubmittedTextView = itemView.findViewById(R.id.date_submitted);
        }
    }
}