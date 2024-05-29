package com.reva.revalocator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;

public class Admin_View extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        if (savedInstanceState == null) {
            List<Semester> semesters = getSampleData();
            SemesterFragment semesterFragment = new SemesterFragment(semesters);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, semesterFragment);
            transaction.commit();
        }
    }

    private List<Semester> getSampleData() {
        List<Student> studentsA = new ArrayList<>();
        studentsA.add(new Student("SRN001"));
        studentsA.add(new Student("SRN002"));
        studentsA.add(new Student("SRN001"));
        studentsA.add(new Student("SRN002"));
        studentsA.add(new Student("SRN001"));
        studentsA.add(new Student("SRN002"));
        studentsA.add(new Student("SRN001"));
        studentsA.add(new Student("SRN002"));
        studentsA.add(new Student("SRN001"));
        studentsA.add(new Student("SRN002"));
        studentsA.add(new Student("SRN001"));
        studentsA.add(new Student("SRN002"));


        List<Student> studentsB = new ArrayList<>();
        studentsB.add(new Student("SRN003"));
        studentsB.add(new Student("SRN004"));
        studentsB.add(new Student("SRN003"));
        studentsB.add(new Student("SRN004"));
        studentsB.add(new Student("SRN003"));
        studentsB.add(new Student("SRN004"));
        studentsB.add(new Student("SRN003"));
        studentsB.add(new Student("SRN004"));
        studentsB.add(new Student("SRN003"));
        studentsB.add(new Student("SRN004"));
        studentsB.add(new Student("SRN003"));
        studentsB.add(new Student("SRN004"));

        List<Section> sections1 = new ArrayList<>();
        sections1.add(new Section("Section A", studentsA));
        sections1.add(new Section("Section B", studentsB));
        sections1.add(new Section("Section A", studentsA));
        sections1.add(new Section("Section B", studentsB));
        sections1.add(new Section("Section A", studentsA));
        sections1.add(new Section("Section B", studentsB));
        sections1.add(new Section("Section A", studentsA));
        sections1.add(new Section("Section B", studentsB));
        sections1.add(new Section("Section A", studentsA));
        sections1.add(new Section("Section B", studentsB));
        sections1.add(new Section("Section A", studentsA));
        sections1.add(new Section("Section B", studentsB));
        sections1.add(new Section("Section A", studentsA));
        sections1.add(new Section("Section B", studentsB));

        List<Section> sections2 = new ArrayList<>();
        sections2.add(new Section("Section C", studentsA));
        sections2.add(new Section("Section D", studentsB));
        sections2.add(new Section("Section C", studentsA));
        sections2.add(new Section("Section D", studentsB));
        sections2.add(new Section("Section C", studentsA));
        sections2.add(new Section("Section D", studentsB));
        sections2.add(new Section("Section C", studentsA));
        sections2.add(new Section("Section D", studentsB));
        sections2.add(new Section("Section C", studentsA));
        sections2.add(new Section("Section D", studentsB));
        sections2.add(new Section("Section C", studentsA));
        sections2.add(new Section("Section D", studentsB));
        sections2.add(new Section("Section C", studentsA));
        sections2.add(new Section("Section D", studentsB));

        List<Semester> semesters = new ArrayList<>();
        semesters.add(new Semester("Semester 1", sections1));
        semesters.add(new Semester("Semester 2", sections2));
        semesters.add(new Semester("Semester 1", sections1));
        semesters.add(new Semester("Semester 2", sections2));
        semesters.add(new Semester("Semester 1", sections1));
        semesters.add(new Semester("Semester 2", sections2));
        semesters.add(new Semester("Semester 1", sections1));
        semesters.add(new Semester("Semester 2", sections2));
        semesters.add(new Semester("Semester 1", sections1));
        semesters.add(new Semester("Semester 2", sections2));
        semesters.add(new Semester("Semester 1", sections1));
        semesters.add(new Semester("Semester 2", sections2));
        semesters.add(new Semester("Semester 1", sections1));
        semesters.add(new Semester("Semester 2", sections2));
        semesters.add(new Semester("Semester 1", sections1));
        semesters.add(new Semester("Semester 2", sections2));

        return semesters;
    }
    }


