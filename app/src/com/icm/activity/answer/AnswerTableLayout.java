package com.icm.activity.answer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.icm.R;
import com.icm.pojo.AnswerBean;
import com.icm.pojo.AnswerResultBean;
import com.icm.pojo.BeanLoader.Callback;

public class AnswerTableLayout extends TableLayout implements Callback<AnswerResultBean> {

	public AnswerTableLayout(Context context) {
		super(context);
	}
	
	public AnswerTableLayout(Context context, AttributeSet set) {
		super(context, set);
	}

	@Override
	public void beanLoaded(AnswerResultBean bean) {
		new AsyncTask<AnswerBean, Void, List<View>>() {

			@Override
			protected List<View> doInBackground(AnswerBean... params) {
				
				List<View> views = new ArrayList<View>(params.length); 
				for(AnswerBean bean : params) {
					View row = inflate(getContext(), R.layout.answer_row, null);
					
					TextView answerView = (TextView) row.findViewById(R.id.answerText);
					answerView.setText(bean.user + " -- " + bean.answer);
					views.add(row);
				}
				return views;
			}
			
			@Override
			protected void onPostExecute(List<View> views) {
				for(View view : views) {
					AnswerTableLayout.this.addView(view);
				}
			}
			
		}.execute(bean.result);
	}

}
