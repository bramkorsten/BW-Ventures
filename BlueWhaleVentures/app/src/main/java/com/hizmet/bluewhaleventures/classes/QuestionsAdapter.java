package com.hizmet.bluewhaleventures.classes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hizmet.bluewhaleventures.R;
import com.hizmet.bluewhaleventures.fragments.QuestionsFragment;

import java.lang.ref.WeakReference;
import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> implements PopupMenu.OnMenuItemClickListener {

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
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        final Question question = questionsList.get(position);
        holder.title.setText(question.getName());
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.edit:
                Log.d("ventures", "onMenuItemClick: EDIT");
                return true;

            case R.id.delete:
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(context);

                builder.setTitle("Delete Question")
                        .setMessage("Are you sure you want to delete this question? This cannot be undone!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteQuestion();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                builder.show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
                return true;
        }
        return false;
    }

    private void deleteQuestion() {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Deleting Question...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String ventureId = getLocalVentureId();
        Log.d("ventures", "deleteQuestion: " + question.getName());
//        DocumentReference questionRef = firestoreDb.collection("Startups").document(ventureId).collection("Experiments").document(getLocalExperimentId()).collection("people").document(person.getPersonId());
//        Log.d("ventures", "Startups -> " + ventureId + " -> Experiments -> " + getLocalExperimentId() + " -> people -> " + person.getPersonId());
//        questionRef.delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("ventures", "Person Reference was deleted successfully!");
//                        dialog.dismiss();
//                        questionsFragment.refreshContent();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("ventures", "Error deleting Person ", e);
//                    }
//                });
    }

    private String getLocalVentureId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("VentureId", "NULL");
    }

    private String getLocalExperimentId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("ExperimentID", "NULL");
    }

    private String getLocalQuestionId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("QuestionID", "NULL");
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView title, number, notes;
        private EditText answer;
        private ImageView image;
        private Button questionAnswerButton;
        private ImageButton answerSaveButton;
        private View answerView;
        private WeakReference<ClickListener> listenerRef;

        public QuestionViewHolder(View itemView) {
            super(itemView);

            listenerRef = new WeakReference<ClickListener>(listener);
            title = itemView.findViewById(R.id.question_title);
            number = itemView.findViewById(R.id.questionNumberTxt);
            notes = itemView.findViewById(R.id.questionNotesText);
            answer = itemView.findViewById(R.id.answerTxt);
            answerView = itemView.findViewById(R.id.answerLayout);
            questionAnswerButton = itemView.findViewById(R.id.questionAnswerButton);
            answerSaveButton = itemView.findViewById(R.id.saveButton);

            itemView.setOnClickListener(this);
            questionAnswerButton.setOnClickListener(this);
            answerSaveButton.setOnClickListener(this);
        }

        // onClick listener for view
        @Override
        public void onClick(View view) {
            if (view.getId() == questionAnswerButton.getId()) {
                // get the center for the clipping circle
                int cy = answerView.getMeasuredHeight() / 2;

                // get the final radius for the clipping circle
                int finalRadius = Math.max(answerView.getWidth(), answerView.getHeight());

                // create the animator for this view (the start radius is zero)
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(answerView, 50, cy, 0, finalRadius);

                // make the view visible and start the animation
                answerView.setVisibility(View.VISIBLE);
                anim.start();
                answer.setFocusableInTouchMode(true);
                answer.requestFocus();

                InputMethodManager lManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(answer, 0);

                Log.d("ventures", "Answerbutton is clicked!");
//                Toast.makeText(view.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
//                question = questionsList.get(getPosition());
//                PopupMenu popup = new PopupMenu(context, view);
//                MenuInflater inflater = popup.getMenuInflater();
//                inflater.inflate(R.menu.options_person, popup.getMenu());
//                popup.setOnMenuItemClickListener(QuestionsAdapter.this);
//                popup.show();
            }

            else if (view.getId() == answerSaveButton.getId()) {
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
            }

            else {
//                Toast.makeText(view.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
//                Question questions = questionsList.get(getPosition());
//                Map questionData = questions.getData();
//                // Go to People Activity which controls single persons etc.
//                Intent intent = new Intent(questionsFragment.getActivity(), QuestionActivity.class);
//                intent.putExtra("map", (Serializable) questionData);
//                intent.putExtra("id", questions.getName());
//                setLocalQuestionId(context, question.getName());
//                questionsFragment.startActivityForResult(intent, 1);
            }

            //
            //            listenerRef.get().onPositionClicked(getAdapterPosition());
        }

        // onLongClickListener for view
        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    private void setLocalQuestionId(Context context, String questionId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("QuestionID", questionId);
        editor.apply();
    }
}
