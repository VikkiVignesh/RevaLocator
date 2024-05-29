package com.reva.revalocator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {

    private List<Section> sectionList;
    private OnSectionClickListener listener;

    public SectionAdapter(List<Section> sectionList, OnSectionClickListener listener) {
        this.sectionList = sectionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        Section section = sectionList.get(position);
        holder.sectionTextView.setText(section.getName());
        holder.itemView.setOnClickListener(v -> listener.onSectionClick(section.getStudents()));
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        public TextView sectionTextView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            sectionTextView = itemView.findViewById(R.id.tvSection);
        }
    }

    public interface OnSectionClickListener {
        void onSectionClick(List<Student> students);
    }
}

