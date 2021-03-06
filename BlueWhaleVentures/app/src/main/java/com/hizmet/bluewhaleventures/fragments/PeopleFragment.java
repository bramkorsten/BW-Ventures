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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hizmet.bluewhaleventures.ExperimentActivity;
import com.hizmet.bluewhaleventures.NewPersonActivity;
import com.hizmet.bluewhaleventures.R;
import com.hizmet.bluewhaleventures.classes.ClickListener;
import com.hizmet.bluewhaleventures.classes.PeopleAdapter;
import com.hizmet.bluewhaleventures.classes.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PeopleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PeopleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeopleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageButton backButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<Person> personsList;

    private RecyclerView peopleRecyclerView;
    private PeopleAdapter adapter;
    private RecyclerView.LayoutManager peopleLayoutManager;
    private SwipeRefreshLayout refresherLayout;
    private FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
    private Context context;
    private String experimentId;

    public PeopleFragment() {
        personsList = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PeopleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PeopleFragment newInstance(String param1, String param2) {
        PeopleFragment fragment = new PeopleFragment();
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
        return inflater.inflate(R.layout.fragment_people, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set views to variables.
        setViews();
        setRefreshLayout();
        setPeopleRecyclerView();
        refreshContent();
        this.context = view.getContext();

        backButton = getView().findViewById(R.id.toolbarBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        ImageButton buttonAddPerson = getView().findViewById(R.id.toolbarNew);
        buttonAddPerson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                experimentId = ((ExperimentActivity) getActivity()).getExperimentIdFromParent();
                Intent intent = new Intent(getActivity(), NewPersonActivity.class);
                intent.putExtra("experimentId", experimentId);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void setViews() {
        peopleRecyclerView = getView().findViewById(R.id.PeopleRecycleView);
        refresherLayout = getView().findViewById(R.id.refreshLayout);
        refresherLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    private void setRefreshLayout() {
        refresherLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh people
                refreshContent();
            }
        });
    }

    public void refreshContent(){
        refresherLayout.setRefreshing(true);
        getPersonData();
    }

    private void setPeopleRecyclerView() {
        adapter = new PeopleAdapter(this, personsList, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {

            }

            @Override
            public void onLongClick(int position) {

            }
        }, getContext());

        peopleLayoutManager = new LinearLayoutManager(this.getContext());
        peopleRecyclerView.setLayoutManager(peopleLayoutManager);
        peopleRecyclerView.setItemAnimator(new DefaultItemAnimator());
        peopleRecyclerView.setAdapter(adapter);
    }

    private void getPersonData() {
        String ventureId = getLocalVentureId();
        personsList.clear();
        adapter.notifyDataSetChanged();
        String experimentId = ((ExperimentActivity) getActivity()).getExperimentIdFromParent();
        CollectionReference personRef = firestoreDb.collection("Startups").document(ventureId).collection("Experiments").document(experimentId).collection("people");
        personRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            DocumentReference person = (DocumentReference) document.getData().get("person");
                            Log.d("ventures", document.getId() + " => " + document.getData().get("person"));
                            person.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("ventures PERSON", document.getId() + " => " + document.getData());
                                            Person person = new Person(document.getData());
                                            person.setPersonId(document.getId());
                                            personsList.add(person);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Log.d("ventures", "No such document");
                                        }
                                    }
                                }
                            });
                        }
                    }
                    Log.d("ventures", "Finished getting people");
                    adapter.notifyDataSetChanged();
                    refresherLayout.setRefreshing(false);
                } else {
                    Log.d("ventures", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private String getLocalVentureId(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("VentureId", "NULL");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            Toast.makeText(context, "People Fragment Attached", Toast.LENGTH_SHORT).show();
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
