package com.icm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.icm.pojo.AnswerResultBean;
import com.icm.pojo.BeanLoader;
import com.icm.pojo.BeanLoader.Callback;
import com.icm.pojo.ImageBean;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AnswerActivity extends SherlockActivity implements Callback<AnswerResultBean>{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		String path = getIntent().getStringExtra("path");
		final int id = getIntent().getIntExtra("id", 1);

		BeanLoader.loadBean(AnswerResultBean.class, BeanLoader.answersUrl + "?pic_id=" + id, this);
		
		ImageView imageView = (ImageView) findViewById(R.id.answer_imageView);
		ImageLoader.getInstance().displayImage(ImageBean.baseURL + path, imageView);
		
		final EditText answerEditText = (EditText) findViewById(R.id.editTextAnswer);

		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					@Override
					public void run() {

						HttpClient client = new DefaultHttpClient();
						HttpPost post = new HttpPost(Constants.SERVER_ROOT + "newanswer.php");

						try {
							List<NameValuePair> pairs = new ArrayList<NameValuePair>();
							pairs.add(new BasicNameValuePair("pic_id", String.valueOf(id)));
							pairs.add(new BasicNameValuePair("answer", answerEditText.getText().toString()));
							pairs.add(new BasicNameValuePair("user", "Anonymous"));
							post.setEntity(new UrlEncodedFormEntity(pairs));

							client.execute(post);
							Log.d("HTTP", "HTTP: OK");
						} catch (Exception e) {
							Log.e("HTTP", "Error in http connection "
									+ e.toString());

						}
					}
				}.start(); // lol
				AnswerActivity.this.finish();
			}
		});
	}
	


	@Override
	public void beanLoaded(AnswerResultBean resultBean) {
		final ListView listView = (ListView) findViewById(R.id.answer_list_view);


		TextView textView = (TextView) findViewById(R.id.answer_questionTextView);
		textView.setText(getIntent().getStringExtra("question"));
		
		if (resultBean != null && resultBean.result != null) {

			String array[] = new String[Math.min(resultBean.result.length, 3)];
			for (int i = 0; i < resultBean.result.length && i < 3; i++) {
				array[i] = resultBean.result[i].user + " -- " + resultBean.result[i].answer;
			}

			ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
			listView.setAdapter(adapter);
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
