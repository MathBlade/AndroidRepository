package com.pi_r_squared.Pinochle;

import com.pi_r_squared.Pinochle.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;


public class RulesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rules);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rules, menu);
		return true;
	}

	public void openSettings(){
    	
	   	 // Load settings page
	   	Intent intent = new Intent(this, Settings.class);
	   	startActivity(intent);
	   	
	   }
	   public void createNewGame(){
	   	
	  	 // Load new game
	  	Intent intent = new Intent(this, PinochleGame.class);
	  	startActivity(intent);
	  	
	   }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			
			 this.finish();
			return true;
		case R.id.action_settings:
            openSettings();
            return true;
        case R.id.action_new_game:
            createNewGame();
            return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
