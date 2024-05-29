package com.reva.revalocator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentFragment extends Fragment {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<Student> studentList;

    public StudentFragment(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StudentAdapter(studentList, this::openStudentDetailActivity);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void openStudentDetailActivity(String srn) {
        // Start your activity here with the SRN
        Intent intent = new Intent(getContext(), MainAdminMode.class);
        intent.putExtra("SRN", srn);
        startActivity(intent);
    }
}
