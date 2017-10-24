package com.hizmet.bluewhaleventures.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hizmet.bluewhaleventures.ExperimentActivity;
import com.hizmet.bluewhaleventures.R;
import com.hizmet.bluewhaleventures.classes.ClickListener;
import com.hizmet.bluewhaleventures.classes.Experiment;
import com.hizmet.bluewhaleventures.classes.ExperimentAdapter;
import com.hizmet.bluewhaleventures.classes.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExperimentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExperimentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExperimentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView textViewExperiments;
    Toolbar toolbar;
    private List<Experiment> experimentList = new ArrayList<>();
    FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String ventureId;

    private RecyclerView experimentsRecyclerView;
    private ExperimentAdapter adapter;
    private RecyclerView.LayoutManager experimentsLayoutManager;
    SwipeRefreshLayout refresher;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView textViewTitle;

    public ExperimentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExperimentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExperimentFragment newInstance(String param1, String param2) {
        ExperimentFragment fragment = new ExperimentFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_experiment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setRefreshLayout();
//        setExperimentsRecyclerView();
    }

    private void setRefreshLayout() {
        refresher = getView().findViewById(R.id.refreshLayout);
        refresher.setColorSchemeResources(
                R.color.colorPrimary
        );
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getExperimentData();
            }
        });
    }

    private void setExperimentsRecyclerView(){
        experimentsRecyclerView = getView().findViewById(R.id.experimentsRecycleView);
        adapter = new ExperimentAdapter(experimentList);
        experimentsLayoutManager = new LinearLayoutManager(this.getContext());
        experimentsRecyclerView.setLayoutManager(experimentsLayoutManager);
        experimentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        experimentsRecyclerView.setAdapter(adapter);

        experimentsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getContext(), experimentsRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Experiment experiment = experimentList.get(position);
                Toast.makeText(view.getContext(), experiment.getData().get("ExperimentName").toString() + " is selected!", Toast.LENGTH_SHORT).show();
                // Go to Experiment Activity which controls single Experiments etc.
                Intent intent = new Intent(getActivity(), ExperimentActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getUserData();
    }

    private void getUserData() {

        experimentList.clear();

        DocumentReference docRef = firestoreDb.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ventureId = (String) task.getResult().getData().get("ventureID");
                        getExperimentData();
                    } else {
                        Log.d("ventures", "No such document");
                    }
                }
            }
        });

    }

    private void getExperimentData() {
        experimentList.clear();
        if (ventureId.isEmpty()) {
            return;
        } else {
            CollectionReference experiments = firestoreDb.collection("Startups").document(ventureId).collection("Experiments");
            experiments.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        int i = 0;
                        for (DocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                Map experimentData = document.getData();
                                Log.d("ventures", document.getId() + " => " + document.getData());
                                String title = (String) experimentData.get("ExperimentName");
                                String desc = (String) experimentData.get("ExperimentSubtitle");
                                Date created = (Date) experimentData.get("DateCreated");

                                Experiment experiment = new Experiment(experimentData);
                                experimentList.add(experiment);
                                i++;
                            }

                        }
                        adapter.notifyDataSetChanged();
                        refresher.setRefreshing(false);
                    } else {
                        Log.d("ventures", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            Toast.makeText(context, "Experiment Fragment Attached", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
