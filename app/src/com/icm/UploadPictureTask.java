package com.icm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.icm.pojo.UploadArgs;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

public class UploadPictureTask extends AsyncTask<UploadArgs, Void, Void> {

	@Override
	protected Void doInBackground(UploadArgs... args) {
		UploadArgs arg = args[0];

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Constants.SERVER_ROOT + "upload.php");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            arg.image.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte [] ba = bao.toByteArray();
            int flags = Base64.DEFAULT;
            String ba1=Base64.encodeToString(ba, flags);
            
            nameValuePairs.add(new BasicNameValuePair("file", ba1));
            nameValuePairs.add(new BasicNameValuePair("username", arg.username));
            nameValuePairs.add(new BasicNameValuePair("question", arg.question));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return null;
	}

}
