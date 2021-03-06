package com.icm.activity.picture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Base64OutputStream;

import com.icm.Constants;
import com.icm.pojo.UploadArgs;

/* package private */
class UploadPictureTask extends AsyncTask<UploadArgs, Void, Void> {

	@Override
	protected Void doInBackground(UploadArgs... args) {
		UploadArgs arg = args[0];

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Constants.UPLOAD_URL);

        try {
        	
            OutputStream os = new ByteArrayOutputStream();
            Base64OutputStream boss = new Base64OutputStream(os, Base64.DEFAULT);
            arg.image.compress(Bitmap.CompressFormat.JPEG, 90, boss);
            
            // apparently we need to flush?
            boss.flush(); boss = null;
            os.flush();
            
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);            
            nameValuePairs.add(new BasicNameValuePair("file", os.toString()));
            nameValuePairs.add(new BasicNameValuePair("username", arg.username));
            nameValuePairs.add(new BasicNameValuePair("question", arg.question));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            httpclient.execute(httppost);
            
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} 
        return null;
	}

}
