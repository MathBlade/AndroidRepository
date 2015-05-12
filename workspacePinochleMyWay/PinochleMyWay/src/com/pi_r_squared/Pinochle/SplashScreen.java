package com.pi_r_squared.Pinochle;


import com.pi_r_squared.Pinochle.properties.CommonFunctions;
import com.pi_r_squared.Pinochle.R;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.VideoView;



public class SplashScreen extends Activity {
	
	private Handler mHandler = new Handler();
	CommonFunctions commonFuncs = new CommonFunctions();

	

    @Override
	public void onBackPressed()
	{

	   // super.onBackPressed(); // Comment this super call to avoid calling finish()
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash_screen);
		videoThenMain();
	}

	private void videoThenMain(){
   		//playGif(readFromStringXML(R.string.loading_gif));
		
		playMovie(readFromStringXML(R.string.loading_movie));
		
    	pause(5000);
    	return;
    }
    
    private void playMovie(String videoFileName) {
		// TODO Auto-generated method stub
    	 RelativeLayout layout = new RelativeLayout(this);
    	 VideoView video = new VideoView(this);
    	 /*Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" 
    			 + R.raw.pi_r_squared);*/
    	 Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" 
    			 + getRawFileInt(videoFileName));
    	 video.setVideoURI(uri);
    	 RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	 params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    	 video.setLayoutParams(params);
    	 
    	 layout.addView(video);
    	 
    	 
    	 
    	 
    	 setContentView(layout);
    	 
    	 video.start();
	}

	private int getRawFileInt(String videoFileName) {
	
		return getResources().getIdentifier(videoFileName.substring(0, videoFileName.indexOf(".")), "raw", getPackageName());
	}

	private void pause(int pauseTime){
		//Log.d("DECK", "Start Pause Time " + getCurrentTime());
		mHandler.postDelayed(new Runnable() {
            public void run() {
            	startMain();
            }
        }, pauseTime);
		//Log.d("DECK", "End Pause Time " + getCurrentTime());
	}
	
	//Old way of playing animation
   /* private void playGif(String imageName){
   	 InputStream stream = null;
        try {
            stream = getAssets().open(imageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RelativeLayout layout = new RelativeLayout(this);
        GifMovieView view = new GifMovieView(this, stream);
      
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(view.getmMovie().width(),view.getmMovie().height());
     
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout.addView(view,params);
        
       
       
//        GifDecoderView view = new GifDecoderView(this, stream);
     
      
       setContentView(layout);
        //setContentView(view);
   }*/
    private void startMain(){
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    }
    
    public String readFromStringXML(int id){
 		return  getResources().getString(id);
 	}
}
