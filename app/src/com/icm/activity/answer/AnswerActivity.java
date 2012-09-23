package com.icm.activity.answer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.icm.Constants;
import com.icm.R;
import com.icm.pojo.AnswerResultBean;
import com.icm.pojo.BeanLoader;
import com.icm.pojo.ImageBean;
import com.nostra13.universalimageloader.core.ImageLoader;

@ContentView(R.layout.activity_answer)
public class AnswerActivity extends RoboSherlockActivity {

	@InjectView(R.id.ansImageView)		ImageView	imageView;
	@InjectView(R.id.ansQuestionText)	TextView	textView;
	@InjectView(R.id.ansEditAnswer)		EditText	answerEditText;
	@InjectView(R.id.ansButton)			Button		submitButton;
	@InjectView(R.id.ansTableLayout)	AnswerTableLayout tableLayout;
	
	@InjectExtra("imagebean")	ImageBean	imageBean;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		textView.setText(imageBean.question);
		ImageLoader.getInstance().displayImage(Constants.IMAGES_DIRECTORY + imageBean.path, imageView);
		BeanLoader.loadBean(AnswerResultBean.class, Constants.ANSWERS_URL + imageBean.pic_id, tableLayout);
		
		submitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			@SuppressWarnings("unchecked") 
			public void onClick(View v) {

				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("pic_id", String.valueOf(imageBean.pic_id)));
				pairs.add(new BasicNameValuePair("answer", answerEditText.getText().toString()));
				pairs.add(new BasicNameValuePair("user", "Anonymous"));

				UploadAnswerTask uploadTask = new UploadAnswerTask(AnswerActivity.this);
				uploadTask.execute(pairs);
				finish();
			}
		});
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}

		return true;
	}

}
