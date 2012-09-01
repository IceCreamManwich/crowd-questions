package com.icm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.icm.pojo.BeanLoader;
import com.icm.pojo.ImageBean;
import com.icm.pojo.ResultBean;

public class MainActivity extends SherlockListActivity {

	ImageBean beans[] = null;

	@Override
	protected void onResume() {
		super.onResume();
		
		new BeanLoader<ResultBean>() {
			@Override
			protected void beanLoaded(ResultBean result) {
				MainActivity.this.postPicturesLoad(result);		
			}
		}.loadBean(ResultBean.class, BeanLoader.picturesUrl);
		
	}



	void postPicturesLoad(ResultBean bean) {
		final Activity context = this;
        final ImageBean beans[];
        if (bean != null) { 
        	beans = bean.result;
        } else { 
        	Log.d("main", "bean was null!");
        	beans = null;
        }
        this.beans = beans;
        
        ListAdapter adapter = new ArrayAdapter<ImageBean>(this, R.layout.table_main_row, beans){
        	
        	@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View row = convertView;
				if (convertView == null) {
					LayoutInflater inflater = context.getLayoutInflater();
					row = inflater.inflate(R.layout.table_main_row, null);
				}
					
					ImageBean bean = getItem(position);
					ImageView imageView = (ImageView) row.findViewById(R.id.row_imageView);
					imageView.setImageDrawable(bean.loadDrawable());
				
					TextView textView = (TextView) row.findViewById(R.id.row_textView);
					textView.setText(bean.user + " -- " + bean.question);
					
				return row;
				
			}
        	
        };
        
        
        setListAdapter(adapter);
	}
    
    

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent intent = new Intent();
		intent.setClass(this, AnswerActivity.class);
		
		intent.putExtra("id", beans[(int)id].pic_id);
		intent.putExtra("question", beans[(int)id].question);
		intent.putExtra("path", beans[(int)id].path);
		
		
		startActivity(intent);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		final Context context = this;
        
		MenuItem item = menu.add("Take picture");
		//item.setIcon(R.drawable.ic_menu_camera);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT | MenuItem.SHOW_AS_ACTION_ALWAYS);
		//item.setTitleCondensed("New");
		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent();
		        intent.setClass(context, TakePictureActivity.class);
		        startActivity(intent);
				return true;
			}
		});
		
		return super.onCreateOptionsMenu(menu);
	}
}
