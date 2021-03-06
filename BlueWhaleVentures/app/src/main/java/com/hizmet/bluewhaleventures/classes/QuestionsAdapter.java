package com.hizmet.bluewhaleventures.classes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.hizmet.bluewhaleventures.R;
import com.hizmet.bluewhaleventures.fragments.QuestionsFragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    private QuestionsFragment questionsFragment;
    private ClickListener listener;
    private Context context;
    private List<Question> questionsList;
    private Typeface Montserrat;
    private ProgressDialog dialog;
    private Question question;
    private FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();

    public QuestionsAdapter(QuestionsFragment questionsFragment, ClickListener listener, Context context, List<Question> questionsList) {
        this.questionsFragment = questionsFragment;
        this.listener = listener;
        this.context = context;
        this.questionsList = questionsList;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Montserrat = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/montserrat.ttf");
        return new QuestionViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final QuestionViewHolder holder, int position) {
        final Question question = questionsList.get(position);
        Log.d("ventures", question.toString());
        holder.title.setText(question.getQuestion());
        holder.number.setText(question.getIndex() + ".");
        if (Objects.equals(question.getAnswer(), "")){

        }
        else {
            holder.questionAnswerButton.setText("Change Answer");
            holder.answerTxt.setText(question.getAnswer());
            holder.answerEditText.setText(question.getAnswer());
            holder.editSwitch.setVisibility(View.VISIBLE);
        }

        if (Objects.equals(question.getNotes(), "")){

        }
        else {
            holder.notesTxt.setText(question.getNotes());
            holder.notesLayout.setVisibility(View.VISIBLE);
        }
        holder.editSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.answerSpinner.setVisibility(View.VISIBLE);
                addAnswer(holder.getAdapterPosition(), holder.answerEditText.getText().toString(), questionsList.get(holder.getAdapterPosition()), holder.answerSpinner);
                holder.editSaveLayout.setVisibility(View.GONE);
                holder.editSwitch.showNext();
                holder.answerEditText.clearFocus();
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        holder.editDiscardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.editSaveLayout.setVisibility(View.GONE);
                holder.answerEditText.setText(holder.answerTxt.getText());
                holder.editSwitch.showNext();
                holder.answerEditText.clearFocus();
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    private void addAnswer(final int index, final String answer, final Question question, final ProgressBar spinner) {

        final String ventureId = getLocalVentureId();
        final String experimentId = getLocalExperimentId();
        final String testerId = getLocalTesterId();
        final int questionNumber = question.getIndex();

        firestoreDb.collection("Startups").document(ventureId).collection("Experiments").document(experimentId).collection("people").document(testerId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.getData().get("questionData") != null){
                            Map<String, Map> data = (Map) document.getData().get("questionData");
                            Map<String, String> singleQuestionData = data.get(String.valueOf(questionNumber));
                            singleQuestionData.put("answer", answer);
                            data.put(String.valueOf(questionNumber), singleQuestionData);

                            Map<String, Map> databaseData = new HashMap<>();

                            databaseData.put("questionData", data);

                            firestoreDb.collection("Startups").document(ventureId).collection("Experiments").document(experimentId).collection("people").document(testerId)
                                    .set(databaseData, SetOptions.merge())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d("ventures", "onComplete: completed!");
                                            question.setAnswer(answer);
                                            questionsList.remove(index);
                                            questionsList.add(index, question);
                                            QuestionsAdapter.this.notifyItemChanged(index);
                                            spinner.setVisibility(View.GONE);
                                            QuestionsAdapter.this.notifyDataSetChanged();
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

    private String getLocalVentureId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("VentureId", "NULL");
    }

    private String getLocalExperimentId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("ExperimentID", "NULL");
    }

    private String getLocalTesterId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("TesterId", "NULL");
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView title, number, notesTxt, answerTxt;
        private EditText answer, answerEditText;
        private Button questionAnswerButton, editDiscardButton, editSaveButton;
        private ImageButton answerSaveButton;
        private ViewSwitcher editSwitch;
        private View answerView, notesLayout, editSaveLayout;
        private WeakReference<ClickListener> listenerRef;
        private boolean isValidAnswer = false;
        private ProgressBar answerSpinner;

        public QuestionViewHolder(View itemView) {
            super(itemView);

            listenerRef = new WeakReference<ClickListener>(listener);
            title = itemView.findViewById(R.id.question_title);
            number = itemView.findViewById(R.id.questionNumberTxt);
            notesTxt = itemView.findViewById(R.id.noteTxt);
            answerTxt = itemView.findViewById(R.id.answerTxt);
            answer = itemView.findViewById(R.id.answerTxtinput);
            answerEditText = itemView.findViewById(R.id.answerEditTxt);
            answerView = itemView.findViewById(R.id.answerLayout);
            editSaveLayout = itemView.findViewById(R.id.editSaveLayout);
            editSwitch = itemView.findViewById(R.id.editSwitch);
            notesLayout = itemView.findViewById(R.id.notesLayout);
            questionAnswerButton = itemView.findViewById(R.id.questionAnswerButton);
            editDiscardButton = itemView.findViewById(R.id.editDiscardButton);
            editSaveButton = itemView.findViewById(R.id.editSaveButton);
            answerSaveButton = itemView.findViewById(R.id.saveButton);
            answerSpinner = itemView.findViewById(R.id.answerSpinner);

            itemView.setOnClickListener(this);
            questionAnswerButton.setOnClickListener(this);
            answerSaveButton.setOnClickListener(this);

            answer.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() > 0) {
                        answerSaveButton.setImageDrawable(answerSaveButton.getContext().getResources().getDrawable(R.drawable.ic_save_black_24dp));
                        isValidAnswer = true;
                    }
                    else {
                        answerSaveButton.setImageDrawable(answerSaveButton.getContext().getResources().getDrawable(R.drawable.ic_clear_black_24dp));
                        isValidAnswer = false;
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

        // onClick listener for view
        @Override
        public void onClick(View view) {
            if (view.getId() == questionAnswerButton.getId()) {
                if (editSwitch.getVisibility() == View.VISIBLE) {
                    answerEditText.setSelection(answerEditText.length());
                    editSwitch.showNext();
                    if (answerEditText.getVisibility() == View.VISIBLE) {
                        editSaveLayout.setVisibility(View.VISIBLE);
                        answerEditText.requestFocus();
                        InputMethodManager lManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        lManager.showSoftInput(answerEditText, 0);

                    } else {
                        answerEditText.clearFocus();
                        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
                else {
                    // get the center for the clipping circle
                    int cy = answerView.getMeasuredHeight() / 2;

                    // get the final radius for the clipping circle
                    int finalRadius = Math.max(answerView.getWidth(), answerView.getHeight());

                    // create the animator for this view (the start radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(answerView, 300, cy, 0, finalRadius);

                    // make the view visible and start the animation
                    answerView.setVisibility(View.VISIBLE);
                    anim.start();
                    answer.setFocusableInTouchMode(true);
                    answer.requestFocus();

                    InputMethodManager lManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    lManager.showSoftInput(answer, 0);
                }

//                Toast.makeText(view.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
//                question = questionsList.get(getPosition());
//                PopupMenu popup = new PopupMenu(context, view);
//                MenuInflater inflater = popup.getMenuInflater();
//                inflater.inflate(R.menu.options_person, popup.getMenu());
//                popup.setOnMenuItemClickListener(QuestionsAdapter.this);
//                popup.show();
            }

            else if (view.getId() == answerSaveButton.getId()) {
                answerSpinner.setVisibility(View.VISIBLE);
                // get the center for the clipping circle
                int cx = answerView.getMeasuredWidth() - 50;
                int cy = answerView.getMeasuredHeight() / 2;

                // get the initial radius for the clipping circle
                int initialRadius = answerView.getWidth();

                // create the animation (the final radius is zero)
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(answerView, cx, cy, initialRadius, 0);

                // make the view invisible when the animation is done
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        answerView.setVisibility(View.INVISIBLE);
                    }
                });

                // start the animation
                anim.start();
                answer.clearFocus();
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                if (isValidAnswer) {
                    addAnswer(getAdapterPosition(), answer.getText().toString(), questionsList.get(getAdapterPosition()), answerSpinner);
                }
            }

        }

        // onLongClickListener for view
        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
