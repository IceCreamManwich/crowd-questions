package com.icm.pojo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.icm.Constants;

public abstract class BeanLoader<T> extends AsyncTask<Void, Void, T> {

	public static final String picturesUrl = Constants.SERVER_ROOT + "pictures.php";
	public static final String answersUrl = Constants.SERVER_ROOT + "answers.php";
	
	private String urlString = null;
	private Class<T> beanClass = null;
	
	public void loadBean(Class<T> beanClass, String urlString) {
		this.urlString = urlString;
		this.beanClass = beanClass;
		this.execute();
	}

	@Override
	protected T doInBackground(Void... args) {
		try {
			InputStream is = new URL(urlString).openStream();
			InputStreamReader reader = new InputStreamReader(is);
			return new Gson().fromJson(reader, beanClass);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/* Although onPostExecute could have simply been abstract,
	 * I didn't like the name "onPostExecute", hence the code below */
	
	@Override
	protected void onPostExecute(T result) {
		if (result != null) { //while I'm here, might as well do some error checking
			beanLoaded(result);
		}
	}

	protected abstract void beanLoaded(T result);
}

