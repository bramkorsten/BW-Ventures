package com.hizmet.bluewhaleventures.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hizmet.bluewhaleventures.NewExperimentActivity;
import com.hizmet.bluewhaleventures.R;
import com.hizmet.bluewhaleventures.classes.ClickListener;
import com.hizmet.bluewhaleventures.classes.Experiment;
import com.hizmet.bluewhaleventures.classes.ExperimentAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExperimentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExperimentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExperimentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView textviewNumberOfExperiments;
    TextView textViewExperiments;
    Toolbar toolbar;
    ImageButton buttonAddExperiment;
    ImageButton experimentOptionButton;

    private List<Experiment> experimentsList;
    private FirebaseFirestore firestoreDb;
    private FirebaseUser user;
    private String ventureId;
    private String experimentId;
    private int experimentCount;
    int numberOfExperiments = 0;

    private RecyclerView experimentsRecyclerView;
    private ExperimentAdapter adapter;
    private RecyclerView.LayoutManager experimentsLayoutManager;
    private SwipeRefreshLayout refresherLayout;

    private Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ExperimentsFragment() {
        // Required empty public constructor
        experimentsList = new ArrayList<>();
        firestoreDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExperimentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExperimentsFragment newInstance(String param1, String param2) {
        ExperimentsFragment fragment = new ExperimentsFragment();
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
        View view = inflater.inflate(R.layout.fragment_experiments, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set views to variables.
        setViews();
        setRefreshLayout();
        setExperimentsRecyclerView();
        getUserData(view.getContext());
        this.context = view.getContext();

        buttonAddExperiment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewExperimentActivity.class);
                intent.putExtra("numberOfExperiments", numberOfExperiments);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setViews() {
        buttonAddExperiment = getView().findViewById(R.id.toolbarNew);
        experimentsRecyclerView = getView().findViewById(R.id.experimentsRecycleView);
        refresherLayout = getView().findViewById(R.id.refreshLayout);
        refresherLayout.setColorSchemeResources(R.color.colorPrimary);
        experimentOptionButton = getView().findViewById(R.id.experimentOptionButton);
    }

    private void setRefreshLayout() {
        refresherLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh experiments
                refreshContent();
            }
        });
    }

    public void refreshContent(){
        refresherLayout.setRefreshing(true);
        getExperimentData();
    }


    private void setExperimentsRecyclerView() {
        adapter = new ExperimentAdapter(this, experimentsList, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {

            }

            @Override
            public void onLongClick(int position) {

            }
        }, getContext());
        experimentsLayoutManager = new LinearLayoutManager(this.getContext());
        experimentsRecyclerView.setLayoutManager(experimentsLayoutManager);
        experimentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        experimentsRecyclerView.setAdapter(adapter);
    }


    private void getUserData(Context context) {
        final Context viewContext = context;
        DocumentReference docRef = firestoreDb.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ventureId = (String) task.getResult().getData().get("ventureID");
                        Log.d("ventures", ventureId);
                        setLocalVentureId(viewContext, ventureId);
                        refreshContent();
                    } else {
                        Log.d("ventures", "No such document");
                    }
                }
            }
        });
    }

    private void getExperimentData() {
        experimentsList.clear();
        adapter.notifyDataSetChanged();
        try {
            if (ventureId.isEmpty()) {
                return;
            } else {
                Query experiments = firestoreDb.collection("Startups").document(ventureId).collection("Experiments").orderBy("DateCreated", Query.Direction.ASCENDING);
                experiments.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            experimentCount = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Map experimentData = document.getData();
                                    Log.d("ventures", document.getId() + " => " + document.getData());
                                    experimentCount++;
                                    Experiment experiment = new Experiment(experimentData);
                                    experiment.setExperimentId(document.getId());
                                    experimentsList.add(experiment);

                                }
                            }
                            Collections.reverse(experimentsList);
                            numberOfExperiments = experimentCount;
                            adapter.notifyDataSetChanged();
                            refresherLayout.setRefreshing(false);
                        } else {
                            Log.d("ventures", "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        } catch (Exception e) {
            // ventureId is null
            e.printStackTrace();
        }

    }

    private void setLocalVentureId(Context context, String ventureId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("VentureId", ventureId);
        editor.apply();
    }

    private String getLocalVentureId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return preferences.getString("VentureId", "NULL");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            Toast.makeText(context, "Experiments Fragment Attached", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == 1) {
                refreshContent();
            }
        }
    }
}
