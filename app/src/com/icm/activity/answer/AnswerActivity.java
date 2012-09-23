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
import android.widget.TableLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.google.gson.Gson;
import com.icm.Constants;
import com.icm.R;
import com.icm.pojo.AnswerBean;
import com.icm.pojo.AnswerResultBean;
import com.icm.pojo.BeanLoader;
import com.icm.pojo.BeanLoader.Callback;
import com.icm.pojo.ImageBean;
import com.nostra13.universalimageloader.core.ImageLoader;

@ContentView(R.layout.activity_answer)
public class AnswerActivity extends RoboSherlockActivity implements Callback<AnswerResultBean>{

	@InjectView(R.id.ansImageView)		ImageView	imageView;
	@InjectView(R.id.ansQuestionText)	TextView	textView;
	@InjectView(R.id.ansEditAnswer)		EditText	answerEditText;
	@InjectView(R.id.ansButton)			Button		submitButton;
	@InjectView(R.id.ansTableLayout)	TableLayout tableLayout;
	
	//TODO: test this?
	private ImageBean bean;
	@InjectExtra("imagebean") void setImageBean(String json) {
		this.bean = new Gson().fromJson(json, ImageBean.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		BeanLoader.loadBean(AnswerResultBean.class, Constants.ANSWERS_URL + bean.pic_id, this);
		
		ImageLoader.getInstance().displayImage(Constants.IMAGES_DIRECTORY + bean.path, imageView);

		submitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			@SuppressWarnings("unchecked") 
			public void onClick(View v) {

				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("pic_id", String.valueOf(bean.pic_id)));
				pairs.add(new BasicNameValuePair("answer", answerEditText.getText().toString()));
				pairs.add(new BasicNameValuePair("user", "Anonymous"));

				UploadAnswerTask uploadTask = new UploadAnswerTask(AnswerActivity.this);
				uploadTask.execute(pairs);
				finish();
			}
		});
	}
	

	@Override
	public void beanLoaded(AnswerResultBean resultBean) {

		textView.setText(this.bean.question);
		
		for(AnswerBean answerBean : resultBean.result) {
			
			View row = getLayoutInflater().inflate(R.layout.answer_row, tableLayout, false);
			
			TextView answerView = (TextView) row.findViewById(R.id.answerText);
			answerView.setText(answerBean.user + " -- " + answerBean.answer);
			
			tableLayout.addView(row);
		}
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}

		return true;
	}

}
