package com.icm.pojo;

import java.io.Serializable;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.icm.Constants;
import com.icm.DownloadTask;


public class ImageBean implements Serializable {
	
	private static final long serialVersionUID = -6617064921026112609L;

	public static final String baseURL = Constants.SERVER_ROOT + "images/";

	public int pic_id;
	public String user;
	public String path;
	public String question;
	
	private Drawable cachedDrawable = null;
	public Drawable loadDrawable() {
		if (cachedDrawable == null) {
			try {
				DownloadTask task = new DownloadTask();
				task.execute(new URL(baseURL + path));

				cachedDrawable = task.get();
			} catch (Exception e) {
				Log.e("ImageBean", "Exception getting image", e);
			}
		}
	    return cachedDrawable;
	}
	
	
}
