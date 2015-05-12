package com.pi_r_squared.Pinochle;

import android.app.Application;
import android.content.Context;

import org.acra.*;
import org.acra.annotation.*;

import com.pi_r_squared.Pinochle.properties.UsefulConstants;
import com.pi_r_squared.Pinochle.R;

@ReportsCrashes(
    formKey = "", // This is required for backward compatibility but not used
    //mailTo = "pi_r_squared@hotmail.com",
    mailTo = UsefulConstants.supportEmail, //Required to be like this as nothing has been initiated to get variable any other way.
    customReportContent = { ReportField.APP_VERSION_CODE,
    		ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.PACKAGE_NAME,
    		ReportField.CUSTOM_DATA, ReportField.STACK_TRACE/*,
    		ReportField.LOGCAT*/}, //
    		mode = ReportingInteractionMode.TOAST,
    		resToastText = R.string.acra_crash//
    		 
    
)
public class PinochleApplication extends Application{

	private static Context context;
	
	@Override
	public void onCreate(){
	    ACRA.init(this);
	    super.onCreate();
	    PinochleApplication.context = getApplicationContext();
	}
	
	public static Context getAppContext() {
        return PinochleApplication.context;
    }
	
}
