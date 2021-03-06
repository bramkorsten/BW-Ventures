package com.hizmet.bluewhaleventures.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hizmet.bluewhaleventures.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InformationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Typeface Montserrat;
    private TextView age;
    private TextView gender;
    private TextView phone;
    private ProgressBar spinner;
    private ConstraintLayout spinnerLayout;
    private OnFragmentInteractionListener mListener;
    private FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();

    public InformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InformationFragment newInstance(String param1, String param2) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Montserrat = Typeface.createFromAsset(getActivity().getAssets(), "fonts/montserrat.ttf");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_information, container, false);
//        Button buttonInformation = (Button) view.findViewById(R.id.button_frag_information);
//        buttonInformation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(getActivity(), "Information Button Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            Toast.makeText(context, "Information Fragment Attached", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserInfo();
    }

    private void getUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            fillUserInfo(user);

        } else {
            // No user is signed in
        }
    }

    private void fillUserInfo(FirebaseUser user) {
        getAndSetUserDetails(user.getUid());
    }

    private void getAndSetUserDetails(String userId) {
        DocumentReference userRef = firestoreDb.collection("users").document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String age = document.getData().get("Age").toString();
                        String gender = document.getData().get("Gender").toString();
                        String phone = document.getData().get("Phone").toString();

                        String name = document.getData().get("Name").toString();
                        String surname = document.getData().get("Surname").toString();
                        String nameCharacters;
                        String email = document.getData().get("Email").toString();

                        Log.d("ventures", "onComplete: " + name + surname);

                        String lastName = "";
                        String firstName = "";

                        if (surname.split(" ").length > 0) {

                            lastName = surname.substring(surname.lastIndexOf(" ") + 1);
                            firstName = name.substring(0, 1);
                            nameCharacters = firstName.substring(0, 1) + lastName.substring(0, 1);
                        } else {
                            nameCharacters = name.substring(0, 1);
                        }

                        TextDrawable drawable = TextDrawable.builder()
                                .beginConfig()
                                .useFont(Montserrat)
                                .fontSize(50) /* size in px */
                                .toUpperCase()
                                .endConfig()
                                .buildRound(nameCharacters, Color.parseColor("#0099ff"));

                        setViews(name+" "+surname, email, drawable, age, gender, phone);

                        setSpinnerInvisible();
                    }
                }
            }
        });
    }

    private void setSpinnerInvisible() {
        spinner.setVisibility(View.INVISIBLE);
        spinnerLayout.setVisibility(View.INVISIBLE);
    }

    private void setViews(String name, String email, TextDrawable drawable, String ageS, String genderS, String phoneS) {
        ImageView image = getView().findViewById(R.id.userImage);
        image.setImageDrawable(drawable);
        TextView userName = getView().findViewById(R.id.userName);
        TextView userEmail = getView().findViewById(R.id.userEmail);
        age = getView().findViewById(R.id.age);
        gender = getView().findViewById(R.id.gender);
        phone = getView().findViewById(R.id.phone);
        spinner = getView().findViewById(R.id.answerSpinner2);
        spinnerLayout = getView().findViewById(R.id.spinnerLayout);

        userName.setText(name);
        userEmail.setText(email);

        age.setText(ageS);
        gender.setText(genderS);
        phone.setText(phoneS);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
