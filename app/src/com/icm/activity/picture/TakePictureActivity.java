package com.icm.activity.picture;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
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

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.icm.R;
import com.icm.pojo.UploadArgs;

@ContentView(R.layout.activity_takepicture)
public class TakePictureActivity extends RoboSherlockActivity {
	
	private static final int CAMERA_REQUEST = 1000;
	private static final int RESULT_LOAD_IMAGE = 1001;
	
	private Bitmap uploadedImage = null;

	@InjectView(R.id.imageView1) 		ImageView 	imageView;
	@InjectView(R.id.userName) 			EditText 	userName;
	@InjectView(R.id.questionTextView) 	EditText 	questionText;
	@InjectView(R.id.errorTextView) 	TextView 	errorText;
	@InjectView(R.id.submitButton) 		Button 		submitButton;
	@InjectView(R.id.newPictureButton) 	Button 		cameraButton;
	@InjectView(R.id.existingPictureButton) Button  photoGalleryButton;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
        cameraButton.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});
        
        photoGalleryButton.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(cameraIntent, RESULT_LOAD_IMAGE);
			}
		});

        submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Upload();
				finish();
			}
		});
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(
            		selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
             
            setImageInView(BitmapFactory.decodeFile(picturePath));
            
            //TODO: test with ImageLoader library
            //ImageLoader.getInstance().displayImage(picturePath, imageView);
        }
    
		if(requestCode == CAMERA_REQUEST) {
			setImageInView((Bitmap) data.getExtras().get("data"));
		}
	}
	
	private void setImageInView(Bitmap photo){
		//TODO: test with ImageLoader library
		imageView.setImageBitmap(photo);
		uploadedImage = photo;
		
        imageView.setVisibility(View.VISIBLE);
		questionText.setVisibility(View.VISIBLE);
		submitButton.setVisibility(View.VISIBLE);
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

}
