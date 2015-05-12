    package com.pi_r_squared.Pinochle.dialogs;  
      
    import com.pi_r_squared.Pinochle.R;

import static com.pi_r_squared.Pinochle.properties.UsefulConstants.*;

import android.app.Dialog;  
import android.app.DialogFragment;  
//import android.graphics.Color;  
//import android.graphics.drawable.ColorDrawable;  
import android.os.Bundle;  
import android.util.Log;
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.Window;  
import android.view.WindowManager;  
import android.widget.Button;   
      
    public class TrumpDialog extends DialogFragment {  
     Button mSpadesButton;
     Button mHeartsButton;
     Button mDiamondsButton;
     Button mClubsButton;
     
     public onSubmitListener mListener;  
     public String text = "";  
      
     public interface onSubmitListener {  
      void setOnSubmitListener(String arg);  
     }  
      
     @Override  
     public Dialog onCreateDialog(Bundle savedInstanceState) {  
      final Dialog dialog = new Dialog(getActivity());  
      dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
      dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
        WindowManager.LayoutParams.FLAG_FULLSCREEN);  
      dialog.setContentView(R.layout.human_trump_prompt); 
      dialog.setCanceledOnTouchOutside(false);
      //dialog.getWindow().setBackgroundDrawable(  
       // new ColorDrawable(Color.TRANSPARENT));
      
      
      WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
      //params.x = 300;
      params.y = 225;
      dialog.getWindow().setAttributes(params);
      
      
      
      dialog.show();  
      mSpadesButton = (Button) dialog.findViewById(R.id.spades);  
      mHeartsButton = (Button) dialog.findViewById(R.id.hearts);
      mDiamondsButton = (Button) dialog.findViewById(R.id.diamonds);
      mClubsButton = (Button) dialog.findViewById(R.id.clubs);
      
      mSpadesButton.setOnClickListener(stringOnClickListener(spades));
      mHeartsButton.setOnClickListener(stringOnClickListener(hearts));
      mDiamondsButton.setOnClickListener(stringOnClickListener(diamonds));
      mClubsButton.setOnClickListener(stringOnClickListener(clubs));
      
      
      
      return dialog;  
     } 
     
     public OnClickListener stringOnClickListener(final String stringToSubmit){
   	  OnClickListener clickListener =  new OnClickListener() {  
   	      
   	       @Override  
   	       public void onClick(View v) {  
   	    	   Log.d("DECK", "onclick for " + stringToSubmit+ "done");
   	        mListener.setOnSubmitListener(stringToSubmit);  
   	        dismiss();  
   	       }  
   	      };
   	      return clickListener;
     }
     
     
    }  