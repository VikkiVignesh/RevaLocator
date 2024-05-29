package com.reva.revalocator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.SemesterViewHolder> {

    private List<Semester> semesterList;
    private OnSemesterClickListener listener;

    public SemesterAdapter(List<Semester> semesterList, OnSemesterClickListener listener) {
        this.semesterList = semesterList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SemesterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_semester, parent, false);
        return new SemesterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SemesterViewHolder holder, int position) {
        Semester semester = semesterList.get(position);
        holder.semesterTextView.setText(semester.getName());
        holder.itemView.setOnClickListener(v -> listener.onSemesterClick(semester.getSections()));
    }

    @Override
    public int getItemCount() {
        return semesterList.size();
    }

    public static class SemesterViewHolder extends RecyclerView.ViewHolder {
        public TextView semesterTextView;

        public SemesterViewHolder(View itemView) {
            super(itemView);
            semesterTextView = itemView.findViewById(R.id.tvSemester);
        }
    }

    public interface OnSemesterClickListener {
        void onSemesterClick(List<Section> sections);
    }
}
