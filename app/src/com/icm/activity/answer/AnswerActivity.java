package com.icm.activity.answer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.icm.Constants;
import com.icm.R;
import com.icm.pojo.AnswerBean;
import com.icm.pojo.AnswerResultBean;
import com.icm.pojo.BeanLoader;
import com.icm.pojo.BeanLoader.Callback;
import com.icm.pojo.ImageBean;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AnswerActivity extends SherlockActivity implements Callback<AnswerResultBean>{
	
	private ImageBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		String json = getIntent().getStringExtra("imagebean");
		this.bean = new Gson().fromJson(json, ImageBean.class);
		
		BeanLoader.loadBean(AnswerResultBean.class, Constants.ANSWERS_URL + bean.pic_id, this);
		
		ImageView imageView = (ImageView) findViewById(R.id.answer_imageView);
		ImageLoader.getInstance().displayImage(Constants.IMAGES_DIRECTORY + bean.path, imageView);
		
		final EditText answerEditText = (EditText) findViewById(R.id.editTextAnswer);

		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			
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

		TextView textView = (TextView) findViewById(R.id.answer_questionTextView);
		textView.setText(this.bean.question);

		final TableLayout tableLayout = (TableLayout) findViewById(R.id.answer_tablelayout);
		
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
