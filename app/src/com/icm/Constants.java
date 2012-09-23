package com.icm;

//abstract cause why would you instantiate this?
public abstract class Constants
{
	//can't subclass nor instantiate
	private Constants(){}

	// private cause no one else should care about it.
	private static final String SERVER_ROOT = "http://www.msenn.com/cq";

	public static final String UPLOAD_URL = SERVER_ROOT + "/upload.php";
	public static final String NEW_ANSWER_URL = SERVER_ROOT + "/newanswer.php";
	public static final String PICTURES_URL = SERVER_ROOT + "/pictures.php";
	public static final String ANSWERS_URL = SERVER_ROOT + "/answers.php?pic_id=";
	public static final String IMAGES_DIRECTORY = SERVER_ROOT + "/images/";
	

}
