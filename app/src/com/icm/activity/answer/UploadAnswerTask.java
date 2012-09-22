package com.icm.activity.answer;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;

import com.icm.Constants;

public class UploadAnswerTask extends AsyncTask<List<NameValuePair>, Void, HttpResponse> {
	
	public UploadAnswerTask(Context context) {
		super();
		/* prepare a dialog to inform user of status */
	}
	
	@Override
	protected HttpResponse doInBackground(List<NameValuePair>... pairs) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(Constants.SERVER_ROOT + "newanswer.php");
		
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs[0]));
			return client.execute(post);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(HttpResponse result) {
		/* log somehow or tell the user */
	}

}
