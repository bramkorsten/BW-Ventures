package com.hizmet.bluewhaleventures.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hizmet.bluewhaleventures.NewPersonActivity;
import com.hizmet.bluewhaleventures.R;
import com.hizmet.bluewhaleventures.classes.Question;
import com.hizmet.bluewhaleventures.classes.QuestionsAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageButton backButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<Question> questionsList;

    private RecyclerView questionsRecyclerView;
    private QuestionsAdapter adapter;
    private RecyclerView.LayoutManager questionsLayoutManager;
    private SwipeRefreshLayout refresherLayout;
    private FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();

    public QuestionsFragment() {
        questionsList = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionsFragment newInstance(String param1, String param2) {
        QuestionsFragment fragment = new QuestionsFragment();
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
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButton = getView().findViewById(R.id.toolbarBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        setViews();
        setRefreshLayout();
        setPeopleRecyclerView();
        getQuestionData();

        ImageButton buttonAddPerson = getView().findViewById(R.id.toolbarNew);
        buttonAddPerson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPersonActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setViews() {
        questionsRecyclerView = getView().findViewById(R.id.PeopleRecycleView);
        refresherLayout = getView().findViewById(R.id.refreshLayout);
        refresherLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    private void setRefreshLayout() {
        refresherLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh people
                getQuestionData();
            }
        });
        refresherLayout.setRefreshing(true);
    }

    private void setPeopleRecyclerView() {
//        adapter = new QuestionsAdapter(questionsList);
//        questionsLayoutManager = new LinearLayoutManager(this.getContext());
//        questionsRecyclerView.setLayoutManager(questionsLayoutManager);
//        questionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        questionsRecyclerView.setAdapter(adapter);
//
//        questionsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getContext(), questionsRecyclerView, new ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Question question = questionsList.get(position);
//                Map questionData = question.getData();
//                // Go to Question Activity which controls single Questions etc.
//                Intent intent = new Intent(getActivity(), QuestionActivity.class);
//                intent.putExtra("map", (Serializable) questionData);
//                intent.putExtra("id", question.getQuestionId());
//                startActivity(intent);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));

    }

    private void getQuestionData() {
//        String ventureId = getLocalVentureId();
//        String questionId = ((QuestionActivity) getActivity()).getQuestionIdFromParent();
//        questionsList.clear();
//        CollectionReference questionRef = firestoreDb.collection("Startups").document(ventureId).collection("Experiments").document(personId).collection("people");
//        questionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot document : task.getResult()) {
//                        if (document.exists()) {
//                            DocumentReference question = (DocumentReference) document.getData().get("question");
//                            Log.d("ventures", document.getId() + " => " + document.getData().get("question"));
//                            question.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        DocumentSnapshot document = task.getResult();
//                                        if (document.exists()) {
//                                            Log.d("ventures QUESTION", document.getId() + " => " + document.getData());
//                                            Question question = new Question(document.getData());
//                                            question.setQuestionId(document.getId());
//                                            questionsList.add(question);
//                                            adapter.notifyDataSetChanged();
//                                        } else {
//                                            Log.d("ventures", "No such document");
//                                        }
//                                    }
//                                }
//                            });
//                        }
//                    }
//                    Log.d("ventures", "Finished getting people");
//                    adapter.notifyDataSetChanged();
//                    refresherLayout.setRefreshing(false);
//                } else {
//                    Log.d("ventures", "Error getting documents: ", task.getException());
//                }
//            }
//        });
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
