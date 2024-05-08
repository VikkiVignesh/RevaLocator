package com.example.revalocator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link My_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class My_profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public My_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        profileImageView = view.findViewById(R.id.profile_image_view);
        usernameTextView = view.findViewById(R.id.username_text_view);
        uploadImageButton = view.findViewById(R.id.upload_image_button);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users Data");
        String uId=getActivity().getIntent().getStringExtra("UserId");
        if (uId != null) {
            // Get user details from Firebase Realtime Database
            mDatabase.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        StringBuilder userDetails = new StringBuilder();
                        // Retrieve the user's details from the dataSnapshot
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String srn = dataSnapshot.child("srn").getValue(String.class);
                        String password = dataSnapshot.child("pass").getValue(String.class);
                        String dob = dataSnapshot.child("dob").getValue(String.class);
                        String department = dataSnapshot.child("school").getValue(String.class);
                        String sem=dataSnapshot.child("sem").getValue(String.class);
                        String mId=dataSnapshot.child("mail").getValue(String.class);
                        String city=dataSnapshot.child("city").getValue(String.class);
                        String sex=dataSnapshot.child("gender").getValue(String.class);


                        // Append the formatted user details to the StringBuilder
                        userDetails.append("Name: ").append(name).append("\n");
                        userDetails.append("\nSRN: ").append(srn).append("\n");
                        userDetails.append("\nMail: ").append(mId).append("\n");
                        userDetails.append("\nPassword: ").append(password).append("\n");
                        userDetails.append("\nDOB: ").append(dob).append("\n");
                        userDetails.append("\nGender: ").append(sex).append("\n");
                        userDetails.append("\nSemester: ").append(sem).append("\n");
                        userDetails.append("\nDepartment: ").append(department).append("\n");
                        userDetails.append("\nCity: ").append(city).append("\n");

                        usernameTextView.setText(userDetails.toString());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to load user details", Toast.LENGTH_SHORT).show();
                }
            });
        }

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }
}