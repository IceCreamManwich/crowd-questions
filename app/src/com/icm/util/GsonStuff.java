package com.icm.util;

import java.net.URL;

import android.util.Log;

import com.icm.Constants;
import com.icm.ResultBeanTask;
import com.icm.pojo.AnswerResultBean;
import com.icm.pojo.ResultBean;

public class GsonStuff {

	/* holy cow, this class is a mess of shit! */

	public static final String picturesUrl = Constants.SERVER_ROOT + "pictures.php";
	public static final String answersUrl = Constants.SERVER_ROOT + "answers.php";

	public static ResultBean initializeResultBean(String url) {

		try {
			ResultBeanTask task = new ResultBeanTask();
			task.execute(new URL(url));
			return task.get();
		} catch (Exception e) {
			Log.e("GsonStuff", "Exception getting ResultBean", e);
		}

		return null;
	}

	private static AnswerResultBean initializeAnswerResultBean(String url) {
		try {
			AnswerResultBeanTask task = new AnswerResultBeanTask();
			task.execute(new URL(url));
			return task.get();
		} catch (Exception e) {
			Log.e("GsonStuff", "Error getting AnswerResultBean", e);
		}

		Log.d("initialize", "that bean was null, son!");
		return null;
	}

	public static AnswerResultBean beanFromPictureId(int id) {
		String url = answersUrl + "?pic_id=" + id;
		return initializeAnswerResultBean(url);
	}
}
