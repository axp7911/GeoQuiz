package com.example.geoquiz;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {

	// private static final String TAG="QuizActivity";
	private static final String KEY_INDEX = "index";

	private Button mTrueButton;
	private Button mFalseButton;
	private Button mNextButton;
	// private Button mPrevButton;
	private Button mCheatButton;
	private TextView mQuestionTextView;
	private TrueFalse[] mQuestionBank = new TrueFalse[] {
			new TrueFalse(R.string.question_oceans, true),
			new TrueFalse(R.string.question_mideast, false),
			new TrueFalse(R.string.question_africa, false),
			new TrueFalse(R.string.question_americas, true),
			new TrueFalse(R.string.question_asia, true),

	};

	private int mCurrentIndex = 0;
	private boolean mIsCheater;

	private void updateQuestion() {
		// Log.d(TAG,"Update question text for question # " + mCurrentIndex,new
		// Exception());
		int question = mQuestionBank[mCurrentIndex].getQuestion();
		mQuestionTextView.setText(question);
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {

		if (data == null) {
			return;
		}
		mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN,
				false);
	}

	private void checkAnswer(boolean userPressedTrue) {
		boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();

		int messageResId = 0;

		if (mIsCheater) {
			messageResId = R.string.judgment_toast;
		} else {

			if (answerIsTrue == userPressedTrue) {
				messageResId = R.string.correct_toast;
			} else {
				messageResId = R.string.incorrect_toast;
			}
		}
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
	}
	
	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Log.d(TAG, "onCreate(Bundle) called");
		setContentView(R.layout.activity_quiz);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			ActionBar actionBar = getActionBar();
			actionBar.setSubtitle("Bodies of Water");
		}
		// Reference to Text view
		mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
		mQuestionTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
				mIsCheater = false;
				updateQuestion();
			}
		});

		mTrueButton = (Button) findViewById(R.id.true_button);
		mTrueButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(QuizActivity.this, R.string.correct_toast,
				// Toast.LENGTH_SHORT).show();
				checkAnswer(true);
			}
		});
		mFalseButton = (Button) findViewById(R.id.false_button);
		mFalseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(QuizActivity.this, R.string.incorrect_toast,
				// Toast.LENGTH_SHORT).show();
				checkAnswer(false);
			}
		});

		mNextButton = (Button) findViewById(R.id.next_button);
		mNextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
				updateQuestion();

			}
		});

		if (savedInstanceState != null) {
			mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
		}
		updateQuestion();

		mCheatButton = (Button) findViewById(R.id.cheat_button);
		mCheatButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Start CheatActivity
				// Intent i = new Intent(QuizActivity.this,
				// CheatActivity.class);
				// startActivity(i);

				Intent i = new Intent(QuizActivity.this, CheatActivity.class);
				boolean answerIsTrue = mQuestionBank[mCurrentIndex]
						.isTrueQuestion();
				i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
				// startActivity(i);
				startActivityForResult(i, 0);
			}
		});
		/*
		 * mPrevButton=(Button)findViewById(R.id.prev_button);
		 * mPrevButton.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { mCurrentIndex =
		 * (mCurrentIndex-1) % mQuestionBank.length; if(mCurrentIndex < 0){
		 * mCurrentIndex = (mQuestionBank.length-1); } updateQuestion(); } });
		 */
		updateQuestion();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		Log.i(KEY_INDEX, "onSaveInstanceState");
		savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
	}

	/*
	 * @Override public void onStart(){ super.onStart(); Log.d(TAG,
	 * "onStart() called"); }
	 * 
	 * @Override public void onResume(){ super.onResume();
	 * Log.d(TAG,"onResume() called"); }
	 * 
	 * @Override public void onPause(){ super.onPause();
	 * Log.d(TAG,"onPause() called"); }
	 * 
	 * @Override public void onStop(){ super.onStop();
	 * Log.d(TAG,"onStop() called"); }
	 * 
	 * @Override public void onDestroy(){ super.onDestroy();
	 * Log.d(TAG,"onDestroy() called"); }
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

}
