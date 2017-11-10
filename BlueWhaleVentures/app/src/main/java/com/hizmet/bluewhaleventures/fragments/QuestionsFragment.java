package com.hizmet.bluewhaleventures.fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hizmet.bluewhaleventures.R;
import com.hizmet.bluewhaleventures.classes.ClickListener;
import com.hizmet.bluewhaleventures.classes.Question;
import com.hizmet.bluewhaleventures.classes.QuestionsAdapter;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    // Recording
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton backButton;
    private OnFragmentInteractionListener mListener;
    private List<Question> questionsList;
    private RecyclerView questionsRecyclerView;
    private QuestionsAdapter adapter;
    private SwipeRefreshLayout refresherLayout;
    private Context context;
    private String questionId;

    private FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference;

    private ImageButton playRecording;
    private FloatingActionButton recordFAB;
    private String mLocalFileName = null;
    private String mRemoteStorageFileName = null;
    String recordingFileName;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private boolean isRecording;
    private boolean isPlaying;
    private EditText newQuestionTxt;
    private View newQuestionView;
    private ProgressBar newQuestionSpinner;
    private ImageButton toolbarSaveQuestionButton;

    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    Toast.makeText(context, "Some permissions are denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
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
        requestPermissions(permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViews();

        newQuestionSpinner = getView().findViewById(R.id.newQuestionSpinner);
        newQuestionView = getView().findViewById(R.id.toolbarNewQuestionLayout);
        mLocalFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mLocalFileName += "/recorded_audio.aac";

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        recordFAB.bringToFront();
        recordFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRecording) {
//                    try {
                    startRecording();
                    Snackbar.make(view, "Recording...", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    recordFAB.setImageResource(R.drawable.ic_stop_24dp);
//                    } catch (Exception ex) {
//                        Log.d("ventures", "onClick FAB: Could not start recording " + ex);
//                    }
                } else {
//                    try {
                    stopRecording();
                    uploadAudio();
                    Toast.makeText(context, "Saved recording.", Toast.LENGTH_LONG).show();
                    recordFAB.setImageResource(R.drawable.ic_mic_24dp);
//                    } catch (Exception ex) {
//                        Log.d("ventures", "onClick FAB: Could not stop recording " + ex);
//                    }
                }
            }
        });

        playRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlaying) {
//                    try {
                    startPlaying();

//                    } catch (Exception ex) {
//                        Log.d("ventures", "onClick playRecording: Could not start playing " + ex);
//                    }
                } else {
//                    try {
                    stopPlaying();
                    playRecording.setImageResource(R.drawable.ic_play_circle_filled_30dp);
//                    } catch (Exception ex) {
//                        Log.d("ventures", "onClick FAB: Could not stop recording " + ex);
//                    }
                }
            }
        });
        ImageButton toolbarAddQuestionButton = getView().findViewById(R.id.toolbarAddQuestionButton);
        toolbarAddQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cx = newQuestionView.getWidth() - 60;
                int cy = newQuestionView.getMeasuredHeight() / 2;

                // get the final radius for the clipping circle
                int finalRadius = Math.max(newQuestionView.getWidth(), newQuestionView.getHeight());

                // create the animator for this view (the start radius is zero)
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(newQuestionView, cx, cy, 0, finalRadius);

                // make the view visible and start the animation
                newQuestionView.setVisibility(View.VISIBLE);
                anim.start();
            }
        });

        ImageButton toolbarDiscardQuestionButton = getView().findViewById(R.id.addQuestionBack);
        toolbarDiscardQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the center for the clipping circle
                int cx = 60;
                int cy = newQuestionView.getMeasuredHeight() / 2;

                // get the initial radius for the clipping circle
                int initialRadius = newQuestionView.getWidth();

                // create the animation (the final radius is zero)
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(newQuestionView, cx, cy, initialRadius, 0);

                // make the view invisible when the animation is done
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        newQuestionView.setVisibility(View.INVISIBLE);
                    }
                });

                // start the animation
                anim.start();
            }
        });

        toolbarSaveQuestionButton = getView().findViewById(R.id.saveNewQuestion);
        toolbarSaveQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText newQuestionTxt = getView().findViewById(R.id.newQuestionTxt);

                if (!newQuestionTxt.getText().toString().equals("")) {
                    toolbarSaveQuestionButton.setVisibility(View.INVISIBLE);
                    newQuestionSpinner.setVisibility(View.VISIBLE);
                    String questionTxt = newQuestionTxt.getText().toString();
                    int size = questionsList.size();
                    addQuestion(size + 1, questionTxt);
                } else {

                }
            }
        });

        setRefreshLayout();
        setQuestionsRecyclerView();
        refreshContent();
        this.context = view.getContext();
    }

    private void startPlaying() {
        // get firebase field with recording filename
        DocumentReference testerRef = firestoreDb.collection("Startups").document(getLocalVentureId()).collection("Experiments").document(getLocalExperimentId()).collection("people").document(getLocalTesterId());
        testerRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                recordingFileName = String.valueOf(documentSnapshot.get("recording"));
                // get file from storage
                try {
                    storageReference = firebaseStorage.getReference().child("Recordings").child(getLocalTesterId()).child(recordingFileName);
                    try {
                        File file = File.createTempFile(getLocalTesterId(), ".aac");
                        Uri uri = Uri.fromFile(file);
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(getContext(), uri);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        isPlaying = true;
                        playRecording.setImageResource(R.drawable.ic_stop_30dp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IllegalArgumentException ex) {
                    Toast.makeText(context, "No recording found", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void stopPlaying() {


        isPlaying = false;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mRecorder.setOutputFile(mLocalFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("ventures", "prepare() failed");
        }

        mRecorder.start();
        isRecording = true;
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        isRecording = false;
    }

    private void uploadAudio() {
        Uri uri = Uri.fromFile(new File(mLocalFileName));
        mRemoteStorageFileName = new Timestamp(System.currentTimeMillis()) + ".aac";

        storageReference = firebaseStorage.getReference().child("Recordings").child(getLocalTesterId()).child(mRemoteStorageFileName);

        // When recording file is successfully uploaded to the storage of the Firebase database
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Create a reference to this file at Experiments > people > 'recording'
                DocumentReference testerRef = firestoreDb.collection("Startups").document(getLocalVentureId()).collection("Experiments").document(getLocalExperimentId()).collection("people").document(getLocalTesterId());
                testerRef.update("recording", mRemoteStorageFileName).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Updating of recording field in people is done
                    }
                });
            }
        });
    }

    private void addQuestion(final int index, final String questionTxt) {

        final String ventureId = getLocalVentureId();
        final String experimentId = getLocalExperimentId();
        final String testerId = getLocalTesterId();

        firestoreDb.collection("Startups").document(ventureId).collection("Experiments").document(experimentId).collection("people").document(testerId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.getData().get("questionData") != null){
                            Map<String, Map> data = (Map) document.getData().get("questionData");
                            Map<String, String> singleQuestionData = new HashMap<>();
                            singleQuestionData.put("question", questionTxt);
                            singleQuestionData.put("answer", "");
                            singleQuestionData.put("notes", "");
                            singleQuestionData.put("questionNumber", String.valueOf(index));
                            data.put(String.valueOf(index), singleQuestionData);

                            Map<String, Map> databaseData = new HashMap<>();

                            databaseData.put("questionData", data);

                            Log.d("ventures", databaseData.toString());

                            firestoreDb.collection("Startups").document(ventureId).collection("Experiments").document(experimentId).collection("people").document(testerId)
                                    .set(databaseData, SetOptions.merge())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d("ventures", "onComplete: completed!");
                                            Question newQuestion = new Question(index, questionTxt);
                                            questionsList.add(index, newQuestion);
                                            adapter.notifyItemChanged(index);
                                            // get the center for the clipping circle
                                            int cx = newQuestionView.getMeasuredHeight() - 60;
                                            int cy = newQuestionView.getMeasuredHeight() / 2;

                                            // get the initial radius for the clipping circle
                                            int initialRadius = newQuestionView.getWidth();

                                            // create the animation (the final radius is zero)
                                            Animator anim =
                                                    ViewAnimationUtils.createCircularReveal(newQuestionView, cx, cy, initialRadius, 0);

                                            // make the view invisible when the animation is done
                                            anim.addListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    super.onAnimationEnd(animation);
                                                    newQuestionTxt.setText("");
                                                    toolbarSaveQuestionButton.setVisibility(View.VISIBLE);
                                                    newQuestionSpinner.setVisibility(View.INVISIBLE);
                                                    newQuestionView.setVisibility(View.INVISIBLE);

                                                }
                                            });

                                            // start the animation
                                            anim.start();

                                        }
                                    });
                        }

                    } else {
                        Log.d("ventures", "No such document");
                    }
                }
            }
        });

    }

    private void setViews() {
        questionsRecyclerView = getView().findViewById(R.id.QuestionsRecycleView);
        refresherLayout = getView().findViewById(R.id.refreshLayout);
        refresherLayout.setColorSchemeResources(R.color.colorPrimary);
        backButton = getView().findViewById(R.id.toolbarBack);
        recordFAB = getView().findViewById(R.id.fab);
        playRecording = getView().findViewById(R.id.toolbarPlayRecording);
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
        questionsList.clear();
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

        RecyclerView.LayoutManager questionsLayoutManager = new LinearLayoutManager(this.getContext());
        questionsRecyclerView.setLayoutManager(questionsLayoutManager);
        questionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        questionsRecyclerView.setAdapter(adapter);
    }

    private void getQuestionData() {
        String ventureId = getLocalVentureId();
        String experimentId = getLocalExperimentId();
        String testerId = getLocalTesterId();
        questionsList.clear();
        DocumentReference questionRef = firestoreDb.collection("Startups").document(ventureId).collection("Experiments").document(experimentId).collection("people").document(testerId);
        questionRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Map> questionsData = (Map) document.getData().get("questionData");
                        Log.d("ventures", questionsData.toString());
                        int numberOfQuestions = questionsData.size();
                        try {
                            int i = 0;
                            while (i < numberOfQuestions) {
                                Map<String, String> questionData = questionsData.get(String.valueOf(i + 1));
                                String questionTitle = questionData.get("question");
                                String questionAnswer = questionData.get("answer");
                                String questionNotes = questionData.get("notes");
                                Question question = new Question(i + 1, questionTitle);
                                if (!Objects.equals(questionAnswer, "")){
                                    question.setAnswer(questionAnswer);
                                }
                                if (!Objects.equals(questionNotes, "")){
                                    question.setNotes(questionNotes);
                                }
                                questionsList.add(i, question);
                                adapter.notifyItemChanged(i);
                                i++;
                            }
                        } catch (Exception e) {
                            Log.d("ventures", "error: " + e);
                        }

                        Log.d("ventures", String.valueOf(questionsData.size()));

                        //adapter.notifyDataSetChanged();
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

    private String getLocalTesterId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("TesterId", "NULL");
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
