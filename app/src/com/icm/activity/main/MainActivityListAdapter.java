package com.icm.activity.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.icm.R;
import com.icm.pojo.ImageBean;
import com.nostra13.universalimageloader.core.ImageLoader;

/* package private */ 
class MainActivityListAdapter extends ArrayAdapter<ImageBean> {
	
	final LayoutInflater layoutInflater;

	public MainActivityListAdapter(Activity context, int textViewResourceId, ImageBean[] objects) {
		super(context, textViewResourceId, objects);
		this.layoutInflater = context.getLayoutInflater();
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		
		if (row == null) {
			row = this.layoutInflater.inflate(R.layout.table_main_row, null);
		}
		
		int maxWidth = (int) (((float)parent.getWidth()) * 0.4);
			
		ImageView imageView = (ImageView) row.findViewById(R.id.row_imageView);
		imageView.setMaxWidth(maxWidth);
		
		ImageBean bean = getItem(position);
		ImageLoader.getInstance().displayImage(ImageBean.baseURL + bean.path, imageView);
	
		TextView textView = (TextView) row.findViewById(R.id.row_textView);
		textView.setText(bean.user + " -- " + bean.question);
			
		return row;
		
	}
}
