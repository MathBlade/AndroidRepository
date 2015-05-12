    package com.pi_r_squared.Pinochle.dialogs;  
      
    import com.pi_r_squared.Pinochle.R;

import android.app.Dialog;  
import android.app.DialogFragment;  
//import android.graphics.Color;  
//import android.graphics.drawable.ColorDrawable;  
import android.os.Bundle;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.Window;  
import android.view.WindowManager;  
import android.widget.Button;   
import android.widget.EditText;
      
    public class BidDialog extends DialogFragment {  
     Button mPassButton;
     Button mPlusOneButton;
     Button mCustomBidButton;
     EditText eCustomBid;
      
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
      dialog.setContentView(R.layout.human_bid_prompt);  
      dialog.setCanceledOnTouchOutside(false);
      //dialog.getWindow().setBackgroundDrawable(  
       // new ColorDrawable(Color.TRANSPARENT));
      
      
      WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
      //params.x = 300;
      params.y = 225;
      dialog.getWindow().setAttributes(params);
      
      
      
      dialog.show();  
      mPassButton = (Button) dialog.findViewById(R.id.pass);  
      mPlusOneButton = (Button) dialog.findViewById(R.id.bid_plus_one); 
      mCustomBidButton = (Button) dialog.findViewById(R.id.custom_bid_submit);
      eCustomBid = (EditText) dialog.findViewById(R.id.custom_bid);
      
      mPlusOneButton.setOnClickListener(stringOnClickListener(readFromStringXML(R.string.plus_one_btn_response)));
      mPassButton.setOnClickListener(stringOnClickListener(readFromStringXML(R.string.pass_btn_response)));
      mCustomBidButton.setOnClickListener(stringOnClickListener(readFromStringXML(R.string.custom_bid_btn_response)));
      
      
      return dialog;  
     } 
     
     public OnClickListener stringOnClickListener(final String stringToSubmit){
   	  OnClickListener clickListener =  new OnClickListener() {  
   	      
   	       @Override  
   	       public void onClick(View v) {  
   	    	   String submitString = stringToSubmit;
   	    	   if (submitString.equals(readFromStringXML(R.string.custom_bid_btn_response))){
   	    		   try{
   	    			   if (Integer.parseInt(eCustomBid.getText().toString()) > 0){
   	    				   submitString=submitString+Integer.parseInt(eCustomBid.getText().toString());
   	    			   }
   	    			   else {
   	    				   submitString=readFromStringXML(R.string.invalid_bid);
   	    			   }
   	    		   }
   	    		   catch (Exception e) {
   	    			   submitString=readFromStringXML(R.string.invalid_bid);
   	    		   }
   	    	   }
   	        mListener.setOnSubmitListener(submitString);  
   	        dismiss();  
   	       }  
   	      };
   	      return clickListener;
     }
    
     
     
     public String readFromStringXML(int id){
 		return  getResources().getString(id);
 	}
     
     
    }  
    
    