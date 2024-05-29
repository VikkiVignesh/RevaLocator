package com.reva.revalocator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private OnStudentClickListener listener;

    public StudentAdapter(List<Student> studentList, OnStudentClickListener listener) {
        this.studentList = studentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.srnTextView.setText(student.getSrn());
        holder.itemView.setOnClickListener(v -> listener.onStudentClick(student.getSrn()));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView srnTextView;

        public StudentViewHolder(View itemView) {
            super(itemView);
            srnTextView = itemView.findViewById(R.id.tvSrn);
        }
    }

    public interface OnStudentClickListener {
        void onStudentClick(String srn);
    }
}

