package com.hizmet.bluewhaleventures.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hizmet.bluewhaleventures.R;
import com.hizmet.bluewhaleventures.classes.ClickListener;
import com.hizmet.bluewhaleventures.classes.Experiment;
import com.hizmet.bluewhaleventures.classes.ExperimentAdapter;
import com.hizmet.bluewhaleventures.classes.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

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
    TextView textViewExperiments;
    Toolbar toolbar;
    private List<Experiment> experimentList = new ArrayList<>();

    private RecyclerView experimentsRecyclerView;
    private ExperimentAdapter adapter;
    private RecyclerView.LayoutManager experimentsLayoutManager;
    SwipeRefreshLayout refresher;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ExperimentsFragment() {
        // Required empty public constructor
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCustomFontsOfElements();
        setRefreshLayout();
        setExperimentsRecyclerView();
    }

    private void setRefreshLayout(){
        refresher = getView().findViewById(R.id.refreshLayout);
        refresher.setColorSchemeResources(
                R.color.colorPrimary
        );
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                prepareData();
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
                Toast.makeText(view.getContext(), experiment.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareData();
    }

    private void prepareData() {

        experimentList.clear();

        Experiment experiment = new Experiment("Experiment Title", "This is a description", 0, "0", "NULL");
        experimentList.add(experiment);

        experiment = new Experiment("Experiment Title 2", "This is a description", 1, "1", "NULL");
        experimentList.add(experiment);

        experiment = new Experiment("Experiment Title 3", "This is a description", 2, "2", "NULL");
        experimentList.add(experiment);

        experiment = new Experiment("Experiment Title 4", "This is a description", 3, "3", "NULL");
        experimentList.add(experiment);

        experiment = new Experiment("Experiment Title 5", "This is a description", 4, "4", "NULL");
        experimentList.add(experiment);

        experiment = new Experiment("Experiment Title 6", "This is a description", 5, "5", "NULL");
        experimentList.add(experiment);

        adapter.notifyDataSetChanged();
        refresher.setRefreshing(false);
    }

    private void setCustomFontsOfElements() {
        TextView toolbarTitle = (TextView) getView().findViewById(R.id.toolbarTitle);
        toolbarTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/intro.ttf"));
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
}
