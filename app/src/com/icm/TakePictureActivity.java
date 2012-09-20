package com.icm;


import org.apache.http.HttpVersion;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.icm.pojo.UploadArgs;

public class TakePictureActivity extends SherlockActivity {
	private static final int CAMERA_REQUEST = 1000;
	private static final int RESULT_LOAD_IMAGE = 1001;
	private ImageView imageView;
	private EditText questionText;
	private Button submitQuestion;
	private TextView errorText;
	private EditText userName;
	private Bitmap uploadedImage;
	
	private DefaultHttpClient mHttpClient;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        uploadedImage = null;
        
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        mHttpClient = new DefaultHttpClient(params);
        
        setContentView(R.layout.activity_takepicture);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        this.questionText = (EditText)this.findViewById(R.id.textView);
        this.userName = (EditText)this.findViewById(R.id.userName);
        this.errorText = (TextView)this.findViewById(R.id.errorTextView);
        Button cameraButton = (Button) this.findViewById(R.id.newPictureButton);
        cameraButton.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});
        Button photoGalleryButton = (Button) this.findViewById(R.id.existingPictureButton);
        photoGalleryButton.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(cameraIntent, RESULT_LOAD_IMAGE);
			}
		});
        this.submitQuestion = (Button)this.findViewById(R.id.submitButton);
        submitQuestion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Upload();
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
             
            setImageInView(BitmapFactory.decodeFile(picturePath));
        }
    
		if(requestCode == CAMERA_REQUEST) {
			setImageInView((Bitmap) data.getExtras().get("data"));
		}
	}
	
	private void setImageInView(Bitmap photo){
		imageView.setImageBitmap(photo);
		uploadedImage = photo;
		
        imageView.setVisibility(View.VISIBLE);
		questionText.setVisibility(View.VISIBLE);
		submitQuestion.setVisibility(View.VISIBLE);
		errorText.setVisibility(View.INVISIBLE);
		userName.setVisibility(View.VISIBLE);
	}
	
	@Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
	  
		try {
			Parcelable image = savedInstanceState.getParcelable("SavedImage");
			if(image instanceof Bitmap)
			{
				this.uploadedImage = (Bitmap) image;
			}
		}
		catch(Exception e)
		{
			Log.e("TakePictureActivity", "Exception", e);
		}
	  
	  super.onRestoreInstanceState(savedInstanceState);
  }

	@Override
  protected void onSaveInstanceState(Bundle outState) {
	  super.onSaveInstanceState(outState);
	  
	  try {
	  	outState.putParcelable("SavedImage", uploadedImage);
	  }
	  catch(Exception e)
	  {
	  	Log.e("TakePictureActivity", "Exception", e);
	  }
  }

	public void Upload(){
		
		UploadArgs arg = new UploadArgs();
		arg.question = (String) questionText.getText().toString();
		
		String username = userName.getText().toString();
		if (username == null || username.equals("")) {
			username = "Anonymous";
		}
		arg.username = username;
		arg.image = uploadedImage;
		
		UploadPictureTask task= new UploadPictureTask();
		Log.w("test", "before execute");
		task.execute(arg);
		Log.w("test", "after execute");
		this.finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		
		return true;
	}
}
