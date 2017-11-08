package com.hizmet.bluewhaleventures.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hizmet.bluewhaleventures.R;
import com.hizmet.bluewhaleventures.classes.ClickListener;
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
    private FloatingActionButton floatingActionButton;
    private String mFileName;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

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
    private Context context;
    private String questionId;
    Snackbar recordingSnackbar;

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
        setViews();

        floatingActionButton.bringToFront();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordingSnackbar = Snackbar.make(view, "Recording...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                recordingSnackbar.show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        setRefreshLayout();
        setQuestionsRecyclerView();
        refreshContent();
        this.context = view.getContext();

//        ImageButton buttonAddQuestion = getView().findViewById(R.id.toolbarNew);
//        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                questionId = ((QuestionActivity) getActivity()).getQuestionIdFromParent();
//                Intent intent = new Intent(getActivity(), NewQuestionActivity.class);
//                intent.putExtra("questionId", questionId);
//                startActivityForResult(intent, 1);
//            }
//        });
    }

    private void setViews() {
        questionsRecyclerView = getView().findViewById(R.id.QuestionsRecycleView);
        refresherLayout = getView().findViewById(R.id.refreshLayout);
        refresherLayout.setColorSchemeResources(R.color.colorPrimary);
        backButton = getView().findViewById(R.id.toolbarBack);
        floatingActionButton = getView().findViewById(R.id.fab);
    }

    private void setRefreshLayout() {
        refresherLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh questions
                refreshContent();
            }
        });
    }

    public void refreshContent() {
        refresherLayout.setRefreshing(true);
        getQuestionData();
    }

    private void setQuestionsRecyclerView() {
        adapter = new QuestionsAdapter(this, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {

            }

            @Override
            public void onLongClick(int position) {

            }
        }, getContext(), questionsList);

        questionsLayoutManager = new LinearLayoutManager(this.getContext());
        questionsRecyclerView.setLayoutManager(questionsLayoutManager);
        questionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        questionsRecyclerView.setAdapter(adapter);
    }

    private void getQuestionData() {
        String ventureId = getLocalVentureId();
        // TODO: 6-11-2017 check
        String experimentId = getLocalExperimentId();
//        String questionId = ((QuestionActivity) getActivity()).getQuestionIdFromParent();
        questionsList.clear();
        DocumentReference questionRef = firestoreDb.collection("Startups").document(ventureId).collection("Experiments").document(experimentId);
        questionRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("ventures QUESTION", document.getId() + " => " + document.getData());
                        ArrayList questions = (ArrayList) document.getData().get("questions");
                        Log.d("ventures", "onComplete: " + questions);

                        int i = 0;
                        try {
                            while (i < questions.size() && !questions.isEmpty()) {
                                Object q = questions.get(i);
                                Question question = new Question(q.toString());
                                questionsList.add(question);
                                i++;
                            }
                        } catch (Exception e) {
                            Log.d("ventures", "onComplete: questionsList is empty");
                        }


                        adapter.notifyDataSetChanged();
                        refresherLayout.setRefreshing(false);
                    } else {
                        Log.d("ventures", "No such document");
                    }
                }
            }
        });
    }

    private String getLocalExperimentId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("ExperimentID", "NULL");
    }

    private String getLocalVentureId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("VentureId", "NULL");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            Toast.makeText(context, "Questions Fragment Attached", Toast.LENGTH_SHORT).show();
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
