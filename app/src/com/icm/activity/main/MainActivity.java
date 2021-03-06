package com.icm.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.icm.Constants;
import com.icm.R;
import com.icm.activity.answer.AnswerActivity;
import com.icm.activity.picture.TakePictureActivity;
import com.icm.pojo.BeanLoader;
import com.icm.pojo.ImageBean;
import com.icm.pojo.ResultBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MainActivity extends SherlockListActivity implements BeanLoader.Callback<ResultBean>{
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
	        .cacheInMemory()
	      //.cacheOnDisc()
	        .resetViewBeforeLoading()
	        .build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
			.defaultDisplayImageOptions(defaultOptions)
		  //.enableLogging()
			.build();
		
		ImageLoader.getInstance().init(config);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		BeanLoader.loadBean(ResultBean.class, Constants.PICTURES_URL, this);
	}

	@Override
	public void beanLoaded(ResultBean bean) {
		
        ListAdapter adapter = new MainActivityListAdapter(this, R.layout.table_main_row, bean.result);
        setListAdapter(adapter);
	}
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		ImageBean imageBean = (ImageBean) getListAdapter().getItem((int)id);
		
		Intent intent = new Intent(this, AnswerActivity.class);
		intent.putExtra("imagebean", imageBean);
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
			@Override
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
