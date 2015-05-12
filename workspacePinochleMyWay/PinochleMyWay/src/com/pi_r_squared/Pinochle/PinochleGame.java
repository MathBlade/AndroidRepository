package com.pi_r_squared.Pinochle;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.pi_r_squared.Pinochle.dialogs.BidDialog;
import com.pi_r_squared.Pinochle.dialogs.TrumpDialog;
import com.pi_r_squared.Pinochle.dto.Card;
import com.pi_r_squared.Pinochle.dto.Deck;
import com.pi_r_squared.Pinochle.dto.Player;
import com.pi_r_squared.Pinochle.dto.PublicKnowledge;
import com.pi_r_squared.Pinochle.properties.CommonFunctions;
import com.pi_r_squared.Pinochle.properties.UsefulConstants;
import com.pi_r_squared.Pinochle.R;

import static com.pi_r_squared.Pinochle.properties.UsefulConstants.*;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import android.widget.FrameLayout;

import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.NavUtils;

public class PinochleGame extends Activity implements com.pi_r_squared.Pinochle.dialogs.BidDialog.onSubmitListener, com.pi_r_squared.Pinochle.dialogs.TrumpDialog.onSubmitListener {

	
	
	
	//Real work
	 Deck deck = new Deck();
	 List<Player> players = new ArrayList<Player>();
	 List<Integer> cardsCurrentlyDealt = new ArrayList<Integer>();
	 Random random = new Random(System.currentTimeMillis());
	 int dealer = 0;// This way dealing starts with player 1.
	 List<Integer> maximumCPUBids = new ArrayList<Integer>();
	 List<Boolean> playersPassed = new ArrayList<Boolean>();
	 List<Boolean> playersPlayedCard = new ArrayList<Boolean>();
	 int playerToStartBid =0;
	 int currentBid = 0;
	 String trump = "";
	 List<View> popupViews = new ArrayList<View>();
	 CommonFunctions commonFuncs = new CommonFunctions();
	 Boolean humanBiddingDone = false;
	 Boolean humanPassedCards = false;
	 List<Card> cardsHumanPassed = new ArrayList<Card>();
	 int bidWinner = 0;
	 int leaderOfThisTrick = 0;
	 Locale currentLocale;
	 //List<Integer> currentMelds = new ArrayList<Integer>();
	 List<Card> meldInPlayerHand = new ArrayList<Card>();
	 int northSouthMeld = 0;
	 int eastWestMeld = 0;
	 int northSouthPointers = 0;
	 int eastWestPointers = 0;
	 int eastWestScore = 0;
	 int northSouthScore = 0;
	 Boolean humanChoseCardToPlay = true;
	 List<Card> cardsInTrick = new ArrayList<Card>();
	 List<List<Card>> playerMelds = new ArrayList<List<Card>>();
	 int suitLed=0;
	 List<Card> cardsPlayedThisHand = new ArrayList<Card>();
	 List<Card> cardsInLastTrick = new ArrayList<Card>();
	 Boolean shootTheMoonCalled = Boolean.FALSE;
	 Boolean humanIsBidderOrDummy;
	 Boolean massClaimIsOn;
	 Boolean massClaimMessageHasBeenDisplayed;
	 
	FrameLayout player2HandLayout;
	FrameLayout player3HandLayout;
	FrameLayout player4HandLayout;
	PublicKnowledge publicKnowledge = new PublicKnowledge();
		
	//RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);
		
	GridLayout handLayout ;
	Bitmap myImg ;
	long lastClickTime = 0;
	private Handler mHandler = new Handler();
	UsefulConstants constants = new UsefulConstants();
	
	@Override
	public void onBackPressed()
	{

	   // super.onBackPressed(); // Comment this super call to avoid calling finish()
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pinochle_game);
		this.currentLocale =getResources().getConfiguration().locale;
		// Show the Up button in the action bar.
		setupActionBar();
		
		
		Matrix matrix = new Matrix();
		ImageView cardBackingPlayer2 = new ImageView(this);
		ImageView cardBackingPlayer3 = new ImageView(this);
		ImageView cardBackingPlayer4 = new ImageView(this);
		
		//int cardBackImageInt = getResources().getIdentifier("drawable/" + "img_card_back_96", "", getPackageName());
		//cardBackingPlayer2.setImageResource(cardBackImageInt);	
		
		
		this.myImg = BitmapFactory.decodeResource(getResources(), R.drawable.img_card_back_96);

		
		matrix.postRotate(90);

		Bitmap rotated = Bitmap.createBitmap(this.myImg, 0, 0, this.myImg.getWidth(), this.myImg.getHeight(),
		        matrix, true);

		cardBackingPlayer2.setImageBitmap(rotated);
		
		
		matrix.postRotate(-90);

		Bitmap rotatedPlayer3 = Bitmap.createBitmap(this.myImg, 0, 0, this.myImg.getWidth(), this.myImg.getHeight(),
		        matrix, true);

		cardBackingPlayer3.setImageBitmap(rotatedPlayer3);
		
		
		matrix.postRotate(-90);

		Bitmap rotatedPlayer4 = Bitmap.createBitmap(this.myImg, 0, 0, this.myImg.getWidth(), this.myImg.getHeight(),
		        matrix, true);

		cardBackingPlayer4.setImageBitmap(rotatedPlayer4);
		
		
		
		//We do not wish to show this yet.
		hideOrShowFrame(R.id.passing_card_prompt, View.INVISIBLE);
		
	
		this.player2HandLayout =(FrameLayout) findViewById(R.id.player2_grid);
		this.player3HandLayout =(FrameLayout) findViewById(R.id.player3_grid);
		this.player4HandLayout =(FrameLayout) findViewById(R.id.player4_grid);
			
		//RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);
			
		this.handLayout =(GridLayout) findViewById(R.id.hand_grid);
		
		
		
		
		
	    
	    
	
	    this.cardsCurrentlyDealt.clear();
	   
	    newGame();
	   
	    
		
	}
	
	
	private void newGame() {
		// This is simple so I like it. :) Round method is for threading.
		this.eastWestScore = 0;
		this.northSouthScore = 0;
		this.massClaimMessageHasBeenDisplayed = Boolean.FALSE;
		((TextView) findViewById(R.id.east_west_score)).setText("" + this.eastWestScore);
		((TextView) findViewById(R.id.north_south_score)).setText("" + this.northSouthScore);
		
		newRound();
			
		
			
	}

	
	
	private void newRound(){
		
		//if (!checkVictoryConditions()){
		newHand();
		this.currentBid = this.constants.minBid;
		//int playerBidding = this.playerToStartBid;
		
		doBidding(this.playerToStartBid);
		
	}

	private String determineWinner() {
	
		if ((this.eastWestScore > this.constants.winningScore) && (this.northSouthScore <= this.constants.winningScore)){
			//return "East West";
			return readFromStringXML(R.string.east_west_spelled_out);
		}
		else if ((this.northSouthScore > this.constants.winningScore) && (this.eastWestScore <= this.constants.winningScore)){
			return readFromStringXML(R.string.north_south_spelled_out);
		}
		else{
			
			//We are checking if this a mercy win or not.
			//If regular win
			if ((this.eastWestScore>this.constants.winningScore)||(this.northSouthScore>this.constants.winningScore)){
				//If both are above the required threshold, then default rules dictate the bidWinner gets the victory, even if the other team has a higher score.
				//This is configuarable though.
				if (this.constants.biddingTeamCrossing150AlwaysWins){
					if (this.bidWinner % 2 == 0){
						return readFromStringXML(R.string.east_west_spelled_out);
					}
					else {
						return readFromStringXML(R.string.north_south_spelled_out);
					}
				}
				else {
					if (this.eastWestScore > this.northSouthScore){
						return readFromStringXML(R.string.east_west_spelled_out);
					}
					else if (this.eastWestScore < this.northSouthScore) {
						return readFromStringXML(R.string.north_south_spelled_out);
					}
					else {
						//The two scores are equal. Give benefit to human for now.
						return readFromStringXML(R.string.north_south_spelled_out);
					}
				}
			}
			else {
				//this is a mercy win.
				if (this.eastWestScore > this.northSouthScore){
					return readFromStringXML(R.string.east_west_spelled_out);
				}
				else  {
					return readFromStringXML(R.string.north_south_spelled_out);
				}
				
			}
		}
	}


	private boolean checkVictoryConditions() {
		
		if (this.eastWestScore > this.constants.winningScore){
			return true;
		}else {
			if (this.northSouthScore > this.constants.winningScore){
				return true;
			}
		}
		
		if (this.constants.mercyRuleInEffect){
			if (this.eastWestScore > (this.northSouthScore + this.constants.winningScore)){
				return true;
			}
			else if (this.northSouthScore > (this.eastWestScore + this.constants.winningScore)){
				return true;
			}
		}
		// If here no victory conditions met.
		
		return false;
	}


	private void newHand() {
		
		Log.d("MTITUS", "New hand starting");
		this.eastWestMeld =0;
		this.northSouthMeld =0;
		this.eastWestPointers=0;
		this.northSouthPointers=0;
		this.currentBid=0;
		this.bidWinner=0;
		this.humanBiddingDone = false;
		this.humanPassedCards = false;
		this.cardsHumanPassed = new ArrayList<Card>();
		this.cardsPlayedThisHand.clear();
		this.shootTheMoonCalled = Boolean.FALSE;
		this.massClaimMessageHasBeenDisplayed = Boolean.FALSE;
		resetCardPassedImages();
		
		
		hideOrShowFrame(R.id.btn_show_last_trick, View.INVISIBLE);
		((TextView) findViewById(R.id.north_south_pointers_needed)).setText("");
		((TextView) findViewById(R.id.east_west_pointers_needed)).setText("");
		if (this.players.size()<1){
		    for (int playerInt=1; playerInt < numberOfPlayers + 1; playerInt++){
		    	this.players.add(new Player(playerInt));
		    	this.playersPassed.add(Boolean.FALSE);
		    	this.playersPlayedCard.add(Boolean.FALSE);
		    	if (this.popupViews.size() < playerInt){
		    		this.popupViews.add(getPopupView(playerInt));
		    	}
		    	if (this.playerMelds.size() < playerInt){
		    		this.playerMelds.add(new ArrayList<Card>());
		    	} else{
		    		this.playerMelds.set(playerInt -1, new ArrayList<Card>());
		    	}
		    	
		    	if (playerInt == 2){
		    		this.players.get(playerInt-1).setBidStyle(this.constants.player2Personality);
		    	}
		    	else if (playerInt==3){
		    		this.players.get(playerInt-1).setBidStyle(this.constants.player3Personality);
		    	}
		    	else if (playerInt==4){
		    		this.players.get(playerInt-1).setBidStyle(this.constants.player4Personality);
		    	}
		    }
		    //this.dealer=1;
		    
		   
	    }
		else {
			for (int playerInt=1; playerInt < numberOfPlayers + 1; playerInt++){
		    	this.playersPassed.set(playerInt -1, Boolean.FALSE);
		    	this.playersPlayedCard.set(playerInt -1, Boolean.FALSE);
		    	this.players.get(playerInt -1).setRole("");
		    }
		}
		
		this.dealer++;
	    if (this.dealer == numberOfPlayers + 1){
	    	this.dealer = 1;
	    }
		
		
		for (int playerId = 1; playerId < numberOfPlayers+1; playerId++){
			
			if (playerId == 1){
				((ImageView) findViewById(R.id.player1CardPlayedImage)).setImageBitmap(this.myImg);
			}
			else if (playerId == 2){
				((ImageView) findViewById(R.id.player2CardPlayedImage)).setImageBitmap(this.myImg);
			}
			else if (playerId == 3){
				((ImageView) findViewById(R.id.player3CardPlayedImage)).setImageBitmap(this.myImg);
			}
			else if (playerId == 4){
				((ImageView) findViewById(R.id.player4CardPlayedImage)).setImageBitmap(this.myImg);
			}
			
			//hideOrShowFrame(getResources().getIdentifier("@id/player" + playerId + "CardPlayedImage" , "", getPackageName()),View.INVISIBLE);
			hideOrShowFrame(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + playerId + readFromStringXML(R.string.card_played_image) , "", getPackageName()),View.INVISIBLE);
			
				
				
		}
		
		hidePromptForPlayerToPlayCard();
		
		this.publicKnowledge = new PublicKnowledge();
		dealCards();
		hideOrShowFrame(R.id.hand_title, View.VISIBLE);
		hideOrShowFrame(this.handLayout.getId(), View.VISIBLE);
	    
	    Collections.sort(this.players.get(humanPlayer -1).getCardsInHand(),commonFuncs.cardSorter);
	    
	    
	    ImageView newCardButton = new ImageView(this);
	    int imageId=0;
	    for (Card cardInHand : this.players.get(humanPlayer -1).getCardsInHand()){
	    	
	    	newCardButton = new ImageView(this);
		    //imageId = getResources().getIdentifier("drawable/" + "img_card_back_96", "", getPackageName());
			imageId = getResources().getIdentifier(cardInHand.getImageFileName(), "", getPackageName());
		    newCardButton.setImageResource(imageId);
		    Log.d("MTITUS", "D1");
		    newCardButton.setOnClickListener(getOnClickListener(cardInHand));
		    this.handLayout.addView(newCardButton);
		    //newCardButton.setId(cardsDealt+1);
		    
	    	
	    }
	    
	   
	    
	    for (Player player : this.players){
	    	if (player.getPlayerId() == 2){
	    		for (int cardNumber = 1; cardNumber <= player.getCardsInHand().size(); cardNumber++){
	    			
	    			ImageView newCard =rotateCardBacking(myImg,(float)90);
	    			newCard.setTop(200-(30*(cardNumber-1)));
	    			newCard.setY(0);
	    			this.player2HandLayout.addView(newCard);
	    			
	    			
	    		}
	    		
	    	}
	    	if (player.getPlayerId() == 3){
	    		
	    		for (int cardNumber = 1; cardNumber <= player.getCardsInHand().size(); cardNumber++){
	    			ImageView newCard =rotateCardBacking(myImg,(float)0);
	    			newCard.setLeft(160-(30*(cardNumber -1)));
	    			newCard.setX(0);
	    			this.player3HandLayout.addView(newCard);
	    			
	    			
	    			//	player3HandLayout.addView(rotateCardBacking(myImg,(float)0));
	    		}
	    		//player3HandLayout.addView(cardBackingPlayer3);
	    		//player3HandLayout.addView(rotateCardBacking(myImg,(float)0));
	    	}
	    	if (player.getPlayerId() == 4){
	    		for (int cardNumber = 1; cardNumber <= player.getCardsInHand().size(); cardNumber++){
	    			ImageView newCard=rotateCardBacking(myImg,(float)-90);
	    			newCard.setTop(200-(30*(cardNumber-1)));
	    			newCard.setY(0);
	    			this.player4HandLayout.addView(newCard);
	    			
	    			//player4HandLayout.addView(rotateCardBacking(myImg,(float)-90));
	    		}
	    		//player4HandLayout.addView(cardBackingPlayer4);
	    	}
	    }
	    
	    determineMaxBids();
	    
	    //initializePopups();
	    //setHelloText();
	    //initializeDialogs();
	    
	    if (this.dealer +1 > numberOfPlayers){
	    	this.playerToStartBid = 1;
	    }
	    else {
	    	this.playerToStartBid = this.dealer + 1;
	    }
	    
	    
	}

	private void resetCardPassedImagesOpponentStyle(){
		List<ImageView> preFinalPassingImages = new ArrayList<ImageView>();
		 final int defaultTag = R.drawable.img_card_back_96;
		 for (int cardImageView=1; cardImageView < this.constants.cardsPassBetweenOpponentsToBid + 1; cardImageView++){
			 if (preFinalPassingImages.size() < cardImageView){
				 preFinalPassingImages.add((ImageView) findViewById(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_human_pass_card_underscore) + cardImageView, "", getPackageName())));
			 }
			 
			 preFinalPassingImages.get(cardImageView -1).setTag(defaultTag);
			 //preFinalPassingImages.get(cardImageView -1).setImageResource(getResources().getIdentifier("drawable/" + "img_card_back_96", "", getPackageName()));
			 preFinalPassingImages.get(cardImageView -1).setImageResource(getResources().getIdentifier(readFromStringXML(R.string.drawable_slash) + readFromStringXML(R.string.img_card_back_96), "", getPackageName()));
			 hideOrShowFrame(preFinalPassingImages.get(cardImageView -1).getId(), View.VISIBLE);
		 }
		 
		 //Hide irrelevant cards.
		 for (int nonCardImageView = this.constants.cardsPassBetweenOpponentsToBid + 1;nonCardImageView < UsefulConstants.maxCardPass +1; nonCardImageView++ ){
			 
			 String resString = readFromStringXML(R.string.at_id_slash_human_pass_card_underscore)  + nonCardImageView;
			 int id =getResources().getIdentifier(resString, "", getPackageName());
			 ImageView cardImageViewToNotBeSeen = (ImageView) findViewById(id);
			 if (preFinalPassingImages.size() < nonCardImageView){
				 preFinalPassingImages.add(cardImageViewToNotBeSeen);
			 }
			 preFinalPassingImages.get(nonCardImageView -1).setTag(defaultTag);
			 //preFinalPassingImages.get(cardImageView -1).setImageResource(getResources().getIdentifier("drawable/" + "img_card_back_96", "", getPackageName()));
			 preFinalPassingImages.get(nonCardImageView -1).setImageResource(getResources().getIdentifier(readFromStringXML(R.string.drawable_slash) + readFromStringXML(R.string.img_card_back_96), "", getPackageName()));
			 hideOrShowFrame(cardImageViewToNotBeSeen.getId(), View.GONE);
		 }
		 
		 //Modify the playing card message.
		 TextView textToPlayer = (TextView) findViewById(R.id.passing_card_text);
		 textToPlayer.setText(textToPlayer.getText().toString().replace(Integer.valueOf(this.constants.cardsPassBetweenDummyAndBidder).toString(), Integer.valueOf(this.constants.cardsPassBetweenOpponentsToBid).toString()));
		 TextView messageToPlayer = (TextView) findViewById(R.id.passing_card_message);
		 messageToPlayer.setText(messageToPlayer.getText().toString().replace(Integer.valueOf(this.constants.cardsPassBetweenDummyAndBidder).toString(), Integer.valueOf(this.constants.cardsPassBetweenOpponentsToBid).toString()));
	}
	

	private void resetCardPassedImages() {

		List<ImageView> preFinalPassingImages = new ArrayList<ImageView>();
		 final int defaultTag = R.drawable.img_card_back_96;
		 for (int cardImageView=1; cardImageView < this.constants.cardsPassBetweenDummyAndBidder + 1; cardImageView++){
			 //preFinalPassingImages.add((ImageView) findViewById(getResources().getIdentifier("@id/human_pass_card_" + cardImageView, "", getPackageName())));
			 if (preFinalPassingImages.size() < cardImageView){
				 preFinalPassingImages.add((ImageView) findViewById(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_human_pass_card_underscore) + cardImageView, "", getPackageName())));
			 }
			 preFinalPassingImages.get(cardImageView -1).setTag(defaultTag);
			 //preFinalPassingImages.get(cardImageView -1).setImageResource(getResources().getIdentifier("drawable/" + "img_card_back_96", "", getPackageName()));
			 preFinalPassingImages.get(cardImageView -1).setImageResource(getResources().getIdentifier(readFromStringXML(R.string.drawable_slash) + readFromStringXML(R.string.img_card_back_96), "", getPackageName()));
			 hideOrShowFrame(preFinalPassingImages.get(cardImageView -1).getId(), View.VISIBLE);
		 }
		 
		 //Hide irrelevant cards.
		 for (int nonCardImageView = this.constants.cardsPassBetweenDummyAndBidder + 1;nonCardImageView < UsefulConstants.maxCardPass +1; nonCardImageView++ ){
			 //@id/human_pass_card_2
			 String resString = readFromStringXML(R.string.at_id_slash_human_pass_card_underscore)  + nonCardImageView;
			 int id =getResources().getIdentifier(resString, "", getPackageName());
			 ImageView cardImageViewToNotBeSeen = (ImageView) findViewById(id);
			 //Since this is ran at the start have to initalize values.
			 if (preFinalPassingImages.size() < nonCardImageView){
				 preFinalPassingImages.add(cardImageViewToNotBeSeen);
			 }
			 preFinalPassingImages.get(nonCardImageView -1).setTag(defaultTag);
			 //preFinalPassingImages.get(cardImageView -1).setImageResource(getResources().getIdentifier("drawable/" + "img_card_back_96", "", getPackageName()));
			 preFinalPassingImages.get(nonCardImageView -1).setImageResource(getResources().getIdentifier(readFromStringXML(R.string.drawable_slash) + readFromStringXML(R.string.img_card_back_96), "", getPackageName()));
			 hideOrShowFrame(cardImageViewToNotBeSeen.getId(), View.GONE);
		 }
		 
		 //Modify the playing card message.
		 TextView textToPlayer = (TextView) findViewById(R.id.passing_card_text);
		 textToPlayer.setText(textToPlayer.getText().toString().replace(Integer.valueOf(4).toString(), Integer.valueOf(this.constants.cardsPassBetweenDummyAndBidder).toString()));
		 textToPlayer.setText(textToPlayer.getText().toString().replace(Integer.valueOf(this.constants.cardsPassBetweenOpponentsToBid).toString(), Integer.valueOf(this.constants.cardsPassBetweenDummyAndBidder).toString()));
		 TextView messageToPlayer = (TextView) findViewById(R.id.passing_card_message);
		 messageToPlayer.setText(messageToPlayer.getText().toString().replace(Integer.valueOf(4).toString(), Integer.valueOf(this.constants.cardsPassBetweenDummyAndBidder).toString()));
		 messageToPlayer.setText(messageToPlayer.getText().toString().replace(Integer.valueOf(this.constants.cardsPassBetweenOpponentsToBid).toString(), Integer.valueOf(this.constants.cardsPassBetweenDummyAndBidder).toString()));
	}


	public void hideOrShowFrame(int frameId, int visibility){
		View viewToHideOrShow = (View) findViewById(frameId);
		viewToHideOrShow.setVisibility(visibility);
	}
	
	
	public void showTrumpDialog(){
		TrumpDialog fragment1 = new TrumpDialog(); 
		fragment1.setCancelable(false);
		fragment1.setShowsDialog(true);
	    fragment1.mListener = PinochleGame.this;  
	    
	    fragment1.show(getFragmentManager(), ""); 
		
	}
	public void showBidDialog(){		
		
		BidDialog fragment1 = new BidDialog(); 
		fragment1.setCancelable(false);
		fragment1.setShowsDialog(true);
	    fragment1.mListener = PinochleGame.this;  
	    fragment1.show(getFragmentManager(), ""); 
	
	}


	

	private void setTextAndBeVisible(int playerInt, String text){
		View popupView = this.popupViews.get(playerInt-1);
		TextView displayText = (TextView) popupView.findViewById(R.id.bid_text);
		displayText.setText(text);
		popupView.setVisibility(View.VISIBLE);
	}



	private View getPopupView(int playerInt) {
	
		//if (playerInt != humanPlayer){
		int idOfPopupView = 0;
		//idOfPopupView = getResources().getIdentifier("@id/" + "player" + playerInt + "_popup_frame", "", getPackageName());
		idOfPopupView = getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + playerInt + readFromStringXML(R.string.underscore_popup_frame), "", getPackageName());
		View popupView = (View) findViewById(idOfPopupView);
		popupView.setVisibility(View.INVISIBLE);
			if ((playerInt != 3)&&(playerInt!=humanPlayer)){
				popupView.setY((float)-110);
			}
			return popupView;
		
	}





	public void onStart(){
		super.onStart();
		
	}
	
	
	
	
	
	

	private void doBidding(int playerBidding) {
		
		
		do {
			
			//if this player passed no reason to do work.
			if (!this.playersPassed.get(playerBidding -1)){
				
				if (playerBidding == humanPlayer){
					
					
					//This has a listener at the bottom that handles what the user did on the dialog.
					showBidDialog();
					break;// This break is intentional to read the user response then continue;
					
					
				}
				else {
					
					int partnerIndex =(playerBidding -1 + 2) % 4;
					int othersPlus2Index = (playerBidding +2) %4;
					int othersIndex = (playerBidding) %4;
					
					
					if (this.playersPassed.get(partnerIndex) && !this.playersPassed.get(othersIndex) && !this.playersPassed.get(othersPlus2Index) && this.currentBid < this.constants.stopComm){
						//put in stopcomm if not in place regardless of maxBid;
						
						this.currentBid = this.constants.stopComm;
						setTextAndBeVisible(playerBidding,""+this.currentBid);
						//displayCPUPopupafterDelay(playerBidding,""+currentBid,100);
						this.currentBid = this.currentBid + this.constants.pointerBaseUnit;
					}
					
					else if (this.playersPassed.get(partnerIndex) && (!this.playersPassed.get(othersIndex) || !this.playersPassed.get(othersPlus2Index))  && this.currentBid < Math.floor((this.constants.minBid +this.constants.stopComm)/2) ) {
						this.currentBid = (int) Math.floor((this.constants.minBid +this.constants.stopComm)/2);
						setTextAndBeVisible(playerBidding,""+this.currentBid);
						//displayCPUPopupafterDelay(playerBidding,""+currentBid,100);
						this.currentBid = this.currentBid + this.constants.pointerBaseUnit;
					}
					else if (this.currentBid <= this.maximumCPUBids.get(playerBidding-1)){
						setTextAndBeVisible(playerBidding,""+this.currentBid);
						//displayCPUPopupafterDelay(playerBidding,""+currentBid,100);
						if (checkPlayersPassed() < numberOfPlayers -1 ){//human passing forces auto loop.
							this.currentBid = this.currentBid + this.constants.pointerBaseUnit;
						}
					}					
					else {
						//displayCPUPopupafterDelay(playerBidding,""+"Pass",100);
						setTextAndBeVisible(playerBidding,readFromStringXML(R.string.pass).toUpperCase(Locale.US));
						this.playersPassed.set(playerBidding-1, true);
					}
				} 
			    
			}
			
				playerBidding++;
				 if (playerBidding == 5){
			    	 playerBidding = 1;
			     }
		
		} while (checkPlayersPassed() < numberOfPlayers-1); 
		
		//This is needed for when the human is the winner.
		if (checkPlayersPassed() == numberOfPlayers -1){
			this.humanBiddingDone = true;
		}
		
		
		if (this.humanBiddingDone){
			//No one bid at currentBid so currentBid is that minus one.
			this.currentBid = this.currentBid -(1 * this.constants.pointerBaseUnit);
			int playerWhoWonBid = getPlayerWhoWonBid();
			
			//Just in case of a looping problem with players 1 and 2.
			if (playerWhoWonBid == 0){
				playerWhoWonBid = playerBidding;
			}
			//Log.d("DECK","Player "+ playerWhoWonBid + " won the bid at: " + this.currentBid);
			//Log.d("DECK","Bid to set: " + this.currentBid+ this.players.get(playerWhoWonBid-1).getPlayerDesignation());
			TextView displayBidOnScreen = (TextView) findViewById(R.id.final_bid);
			Log.d("DECK", "" +this.currentBid);
			Log.d("DECK", this.players.get(playerWhoWonBid-1).getPlayerDesignation());
			displayBidOnScreen.setText(this.currentBid+ this.players.get(playerWhoWonBid-1).getPlayerDesignation());
			this.leaderOfThisTrick = playerWhoWonBid;
			this.bidWinner = playerWhoWonBid;
			
			//Set player roles.
			for (Player player: this.players){
				player.setRole(determinePlayerRole(player.getPlayerId()));
			}
			
			if (playerWhoWonBid == humanPlayer){
				showTrumpDialog();
			}else {
				this.trump = generateTrumpSuit(playerWhoWonBid);
				//setTextAndBeVisible(playerWhoWonBid,"Trump is " + this.trump);
				setTextAndBeVisible(playerWhoWonBid,readFromStringXML(R.string.trump_is) + this.trump);
				this.playersPassed.set(playerBidding-1, true);
				displayTrumpAndContinue();
			}
			
			
			
			
		}
	}



	private void displayTrumpAndContinue() {
	
		TextView displayTrumpOnScreen = (TextView) findViewById(R.id.final_trump);
		displayTrumpOnScreen.setText(this.trump);
		
		int dummy = this.leaderOfThisTrick +2;
		if (dummy > numberOfPlayers){
			dummy = dummy - numberOfPlayers;
		}
		
		if (this.constants.cardsPassBetweenDummyAndBidder > 0){
			if(dummy==humanPlayer){
				
				if (!this.humanPassedCards){
				this.humanIsBidderOrDummy=true;
				displayPromptForHumanToChooseCardsToPass();
				return;
					
				}else {
					
					passCards(humanPlayer,determineBidWinnerPassCardsToDummy(this.leaderOfThisTrick));
					Collections.sort(this.players.get(humanPlayer -1).getCardsInHand(),commonFuncs.cardSorter);
					GridLayout handLayout =(GridLayout) findViewById(R.id.hand_grid);
					handLayout.removeAllViews();
					ImageView newCardButton = new ImageView(this);
					int imageId=0;
					
				    for (Card cardInHand : this.players.get(humanPlayer -1).getCardsInHand()){
				    	
				    	newCardButton = new ImageView(this);
						imageId = getResources().getIdentifier(cardInHand.getImageFileName(), "", getPackageName());
					    newCardButton.setImageResource(imageId);
					    newCardButton.setOnClickListener(getOnClickListener(cardInHand));
					    handLayout.addView(newCardButton);
					
				    	
				    }
					
				}
			}
			else{
				
				if (!this.humanPassedCards){
					passCards(this.leaderOfThisTrick,determineDummyPassCardsToBidWinner(dummy) );
				}
				
				if (this.leaderOfThisTrick == humanPlayer){
					//Refresh the screen after receiving cards from dummy.
					if (!this.humanPassedCards){
					Collections.sort(this.players.get(humanPlayer -1).getCardsInHand(),commonFuncs.cardSorter);
					GridLayout handLayout =(GridLayout) findViewById(R.id.hand_grid);
					handLayout.removeAllViews();
					handLayout.setColumnCount(this.players.get(humanPlayer -1).getCardsInHand().size()/2);
					ImageView newCardButton = new ImageView(this);
					int imageId=0;
				    for (Card cardInHand : this.players.get(humanPlayer -1).getCardsInHand()){
				    	
				    	newCardButton = new ImageView(this);
						imageId = getResources().getIdentifier(cardInHand.getImageFileName(), "", getPackageName());
					    newCardButton.setImageResource(imageId);
					    newCardButton.setOnClickListener(getOnClickListener(cardInHand));
					    handLayout.addView(newCardButton);
				    	
				    }
					
					this.humanIsBidderOrDummy=true;
				    displayPromptForHumanToChooseCardsToPass();
					return;
					}
					else {
				    //Send those cards back;
				    //Remove those cards from the display for the final list.
					
					//Refresh the list.
						GridLayout handLayout =(GridLayout) findViewById(R.id.hand_grid);
						Collections.sort(this.players.get(humanPlayer -1).getCardsInHand(),commonFuncs.cardSorter);
						handLayout.removeAllViews();
						handLayout.setColumnCount(this.players.get(humanPlayer -1).getCardsInHand().size()/2);
						ImageView newCardButton = new ImageView(this);
						int imageId=0;
					    for (Card cardInHand : this.players.get(humanPlayer -1).getCardsInHand()){
					    	
					    	newCardButton = new ImageView(this);
							imageId = getResources().getIdentifier(cardInHand.getImageFileName(), "", getPackageName());
						    newCardButton.setImageResource(imageId);
						    newCardButton.setOnClickListener(getOnClickListener(cardInHand));
						    handLayout.addView(newCardButton);
					    	
					    }
					}
				}
				else {
					
					passCards(dummy, determineBidWinnerPassCardsToDummy(this.leaderOfThisTrick));
				}
			}
		} else {
			if (!((dummy!=humanPlayer)&&(this.bidWinner!=humanPlayer))){
				this.humanPassedCards=true;
			}
			
			
		}
		
		//see if done looping and bid and dummy pass complete.
		if ((dummy!=humanPlayer)&&(this.bidWinner!=humanPlayer)){
			doOpponentPassing();
		}
		else if(this.humanPassedCards){
			doOpponentPassing();
		}
		//displayMeld();
		
		
	}

	
	private void doOpponentPassing(){
		
		int dummy = (this.bidWinner +2) % numberOfPlayers;
		if (dummy == 0){
			dummy = 4;
		}
		if (this.constants.cardsPassBetweenOpponentsToBid > 0){
			if ((humanPlayer != dummy) && (humanPlayer != this.bidWinner)){
				if (!this.humanPassedCards){
					this.humanIsBidderOrDummy=false;
					displayPromptForHumanToChooseCardsToPass();
					return;
				}
				else {
					int playerOppositeOfHuman = humanPlayer +2;
					if (playerOppositeOfHuman > numberOfPlayers){
						playerOppositeOfHuman = playerOppositeOfHuman -numberOfPlayers;
					}
					passCards(humanPlayer, determineOppCardsToPass(playerOppositeOfHuman));
					//Refresh the list.
					GridLayout handLayout =(GridLayout) findViewById(R.id.hand_grid);
					Collections.sort(this.players.get(humanPlayer -1).getCardsInHand(),commonFuncs.cardSorter);
					handLayout.removeAllViews();
					handLayout.setColumnCount(this.players.get(humanPlayer -1).getCardsInHand().size()/2);
					ImageView newCardButton = new ImageView(this);
					int imageId=0;
				    for (Card cardInHand : this.players.get(humanPlayer -1).getCardsInHand()){
				    	
				    	newCardButton = new ImageView(this);
						imageId = getResources().getIdentifier(cardInHand.getImageFileName(), "", getPackageName());
					    newCardButton.setImageResource(imageId);
					    newCardButton.setOnClickListener(getOnClickListener(cardInHand));
					    handLayout.addView(newCardButton);
				    	
				    }
				}
			}
			else {
				int opp1 = (this.bidWinner + 1);
				int opp3 = (this.bidWinner + 3);
				//Note CANNOT use mod else it sets player 4 = 0; BAD!
				if (opp1 > numberOfPlayers){
					opp1 = opp1 - numberOfPlayers;
				}
				if (opp3 > numberOfPlayers){
					opp3 = opp3 - numberOfPlayers;
				}
				
				passCards(opp1, determineOppCardsToPass(opp3));
				passCards(opp3, determineOppCardsToPass(opp1));
			}
		}
		else {
			this.humanPassedCards = true;
		}
		
		if (this.humanPassedCards){
			displayMeld();
		}
	}

	private void displayMeld() {

		//This is required for setting listener correctly later.
		this.humanPassedCards = true;
		
		setTextAndBeVisible(this.leaderOfThisTrick,"");
		this.northSouthMeld = 0;
		this.eastWestMeld = 0;
		TextView eastWestMeldText = (TextView) findViewById(R.id.east_west_meld);
		TextView northSouthMeldText = (TextView) findViewById(R.id.north_south_meld);
		for (Player player : this.players){
			hideOrShowFrame(this.popupViews.get(player.getPlayerId() -1).getId(), View.INVISIBLE);
			
			this.meldInPlayerHand = determineCardsUsedInMeld(player.getCardsInHand());			
			this.playerMelds.set(player.getPlayerId()-1, this.meldInPlayerHand);
			Log.d("DECK", "Player " + player.getPlayerId() + " Cards In Meld: "+ this.meldInPlayerHand.toString());
			addMelds(player.getPlayerId(), determineMeld(this.meldInPlayerHand, commonFuncs.getSuitInt(this.trump)));
			this.publicKnowledge.getKnowledgeAboutPlayers().get(player.getPlayerId()-1).setCardsInMeld(this.meldInPlayerHand);
			
		}
		
		northSouthMeldText.setText(""+ this.northSouthMeld);
		eastWestMeldText.setText(""+ this.eastWestMeld);
		displayCardsInMeldOnScreen();
		//passOrPlay();
	}

	private void passOrPlay(){
		
		if (this.bidWinner != humanPlayer){
			if (betterToPlayHand()){
				playCards();
			}
			else {
				doNotPlayThisHand();
			}
		}
		else {
			displayPassPlayDialog();
			return;
		}
	}
	
	
	private void doNotPlayThisHand() {
		
		Toast.makeText(this, readFromStringXML(R.string.bid_winner_did_not_play), Toast.LENGTH_LONG).show();
		
		if (this.bidWinner % 2 == 0){
			this.eastWestScore = this.eastWestScore - this.currentBid;
			this.northSouthScore = this.northSouthScore + this.northSouthMeld;
		}
		else {
			this.northSouthScore = this.northSouthScore - this.currentBid;
			this.eastWestScore = this.eastWestScore + this.eastWestMeld;
		}
		clearMeldPointersBid();
		((TextView) findViewById(R.id.east_west_score)).setText("" + this.eastWestScore);
		((TextView) findViewById(R.id.north_south_score)).setText("" + this.northSouthScore);
		
		if (!checkVictoryConditions()){
			newRound();
		}
		else {
			doVictoryWork();
		}
	}


	private void displayPassPlayDialog() {
		
		AlertDialog.Builder alertDialogBuilder = getNewDialogBuilder();
		//alertDialogBuilder.setTitle("New Game?");
		alertDialogBuilder.setTitle(readFromStringXML(R.string.play_or_pass));
		alertDialogBuilder.setCancelable(false);//Disables back button from breaking the game.
		alertDialogBuilder
		//.setMessage("Do you wish to play another game of Pinochle?")
		.setMessage(readFromStringXML(R.string.play_or_pass_question))
		.setIcon(R.drawable.img_card_back_96)
		//.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
		.setPositiveButton(readFromStringXML(R.string.play),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				playCards();
			}
		  })
		  .setNegativeButton(readFromStringXML(R.string.pass), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					//This is negative button.
					doNotPlayThisHand();
				}
			  });
			if(this.constants.shootTheMoonIsAvailable){
				alertDialogBuilder.setNeutralButton(readFromStringXML(R.string.shoot_the_moon), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						shootTheMoonCalled(); //Use the method to get around stuck in a dialog interface.
						playCards();
					}
				  });
			}
		
			
		
		//http://stackoverflow.com/questions/14439538/how-can-i-change-the-color-of-alertdialog-title-and-the-color-of-the-line-under
				//Borrowed solution from mmrmartin 
			Dialog alert = alertDialogBuilder.show();
			int dividerId = alert.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
			View divider = alert.findViewById(dividerId);
			divider.setBackgroundColor(Color.RED);
		
	}


	protected void shootTheMoonCalled() {
		this.shootTheMoonCalled = Boolean.TRUE;
		Toast.makeText(this,  readFromStringXML(R.string.shoot_the_moon_has_been_called), Toast.LENGTH_LONG).show();
		
	}


	private void doVictoryWork() {
		// TODO Write something flashy for victory.
		String winner = determineWinner();
		Log.d("DECK", "The " + winner + " team won.");
		this.northSouthScore = 0;
		this.eastWestScore = 0;
		startNewGamePrompt();
	}


	private boolean betterToPlayHand() {
		
		
		int maxBidPlayable = 0;
		if (this.bidWinner % 2 == 0){
			maxBidPlayable = this.constants.maxPointers + this.eastWestMeld;
		}
		else {
			maxBidPlayable = this.constants.maxPointers + this.northSouthMeld;
		}
		
		if (this.currentBid > maxBidPlayable){
			return false;
		}
		else {
			
			int countCardsTrump = getCardsOfSuit(this.players.get(this.bidWinner - 1).getCardsInHand(), commonFuncs.getSuitInt(this.trump)).size();
			int nonTrumpAces = getCardsOfRankNonTrump(this.players.get(this.bidWinner - 1).getCardsInHand(), commonFuncs.getSuitInt(this.trump),ace).size();
			int countOfTrumpAces = countNumberEqual((ArrayList<Card>)this.players.get(this.bidWinner - 1).getCardsInHand(), new Card(ace, commonFuncs.getSuitInt(this.trump)));
			if (countCardsTrump + nonTrumpAces >= this.players.get(this.bidWinner - 1).getCardsInHand().size()){
				if (countOfTrumpAces == 2){
					
					if (this.constants.shootTheMoonIsAvailable){
						shootTheMoonCalled();
					}
					else {
						this.shootTheMoonCalled = Boolean.FALSE;
					}
					return true;
				}
				else {
					this.shootTheMoonCalled = Boolean.FALSE;
				}
			}
			else {
				this.shootTheMoonCalled = Boolean.FALSE;
			}
			return true; //enhance this part later to find "losers" and subtract from total for non shoot hands.
		}
		
		
		
		//return betterToPlayHand;
	}


	private List<Card> getCardsOfRankNonTrump(List<Card> cardsInHand, int currentTrump,
			int rank) {
		
		List<Card> cardsThatMeetCriteria = new ArrayList<Card>();
		for (Card card: cardsInHand){
			if ((card.getRankOfCard() == rank) && (card.getCardSuit() != currentTrump)){
				cardsThatMeetCriteria.add(card);
			}
		}
		
		return cardsThatMeetCriteria;
	}


	private void playCards() {
		
		Boolean bidWinnerIsEastWest = (this.bidWinner %2 == 0);
		TextView northSouthGoalPointersNeeded = (TextView) findViewById(R.id.north_south_pointers_needed);
		TextView eastWestGoalPointersNeeded = (TextView) findViewById(R.id.east_west_pointers_needed);
		
		
		if (bidWinnerIsEastWest){
			if ((this.currentBid - this.eastWestMeld)>0){
				eastWestGoalPointersNeeded.setText("" + (this.currentBid - this.eastWestMeld));
			}
			else {
				eastWestGoalPointersNeeded.setText(readFromStringXML(R.string.made));
			}
			
			if ( (this.constants.maxPointers - (this.currentBid - this.eastWestMeld) + 1 ) >0){
				northSouthGoalPointersNeeded.setText("" + (this.constants.maxPointers - (this.currentBid - this.eastWestMeld) + this.constants.pointerBaseUnit ));
			}
			else {
				northSouthGoalPointersNeeded.setText(readFromStringXML(R.string.made));
				eastWestGoalPointersNeeded.setText(readFromStringXML(R.string.set));
			}
		}
		else {
			
			if ((this.currentBid - northSouthMeld) > 0){
				northSouthGoalPointersNeeded.setText("" + (this.currentBid - northSouthMeld));
			}
			else {
				northSouthGoalPointersNeeded.setText(readFromStringXML(R.string.made));
			}
			if ((this.constants.maxPointers - (this.currentBid - this.northSouthMeld) + 1) > 0){
				eastWestGoalPointersNeeded.setText("" + (this.constants.maxPointers - (this.currentBid - this.northSouthMeld) + this.constants.pointerBaseUnit));
			}
			else {
				eastWestGoalPointersNeeded.setText(readFromStringXML(R.string.made));
				northSouthGoalPointersNeeded.setText(readFromStringXML(R.string.set));
			}
		}
		
		while (this.players.get(this.leaderOfThisTrick -1).getCardsInHand().size() > 0){
			if (this.humanChoseCardToPlay){
				this.suitLed = 0;
				playATrick();
				return;
			}
		}
		
		
		
		
		
	}


	private void playATrick() {
	
		//int currentPlayerThatNeedsToAct = this.leaderOfThisTrick;
		Log.d("MTITUS","Cards In Hand start of trick " + this.players.get(humanPlayer -1).getCardsInHand().toString());
		if (this.playersPlayedCard.get(humanPlayer -1)){
			this.humanChoseCardToPlay = true;
		}else {
			this.humanChoseCardToPlay = false;
		}
		
		for (int playerId = 1; playerId < numberOfPlayers+1; playerId++){
			
			if (playerId == 1){
				((ImageView) findViewById(R.id.player1CardPlayedImage)).setImageBitmap(this.myImg);
			}
			else if (playerId == 2){
				((ImageView) findViewById(R.id.player2CardPlayedImage)).setImageBitmap(this.myImg);
			}
			else if (playerId == 3){
				((ImageView) findViewById(R.id.player3CardPlayedImage)).setImageBitmap(this.myImg);
			}
			else if (playerId == 4){
				((ImageView) findViewById(R.id.player4CardPlayedImage)).setImageBitmap(this.myImg);
			}
			
			//hideOrShowFrame(getResources().getIdentifier("@id/player" + playerId + "CardPlayedImage" , "", getPackageName()),View.VISIBLE);
			hideOrShowFrame(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + playerId + readFromStringXML(R.string.card_played_image) , "", getPackageName()),View.VISIBLE);
			
				
				
		}
		
		
		//List<Card> cardsInThisTrick = new ArrayList<Card>();
		//Integer suitLed = 0;
		
		//reset cards in trick.
		this.cardsInTrick.clear();
		if (this.cardsInTrick.size() < 1){
			for (Integer player=1; player < numberOfPlayers+1; player++){
				this.cardsInTrick.add(null);
			}
		}
		
		
		Log.d("DECK", "Start of trick:");
		for (int player=1; player < numberOfPlayers +1; player++){
			
			Log.d("DECK", "Player " + player + "'s hand count: " + this.players.get(player-1).getCardsInHand().size() + " Cards: " + this.players.get(player -1).getCardsInHand().toString());
		}
		this.suitLed = 0;
		doTrickLoop();
		
		
		
	}
	
	
	
	
	private void returnToTrickFlow(){
		doTrickLoop();
		Log.d("DECK","Check players have played " + checkPlayersHavePlayed());
		pauseAfterTrickWork(4000);
		
	}
	
	
	private void clearTextOnTextView(int idOfTextView){
		((TextView) findViewById(idOfTextView)).setText("");
	}
	
	private void clearMeldPointersBid(){
		clearTextOnTextView(R.id.final_bid);
		clearTextOnTextView(R.id.final_trump);
		clearTextOnTextView(R.id.east_west_meld);
		clearTextOnTextView(R.id.east_west_pointers);
		clearTextOnTextView(R.id.north_south_meld);
		clearTextOnTextView(R.id.north_south_pointers);
	}
	
	
	private void updateScore() {
	
		
		
		//Determine if they were set or not.
		int pointsEarned = 0;
		if (this.bidWinner % 2 == 0){
			pointsEarned = this.eastWestPointers;
			if (this.currentBid > pointsEarned + this.eastWestMeld){
				if ((this.shootTheMoonCalled)){
					//shoot the moon was set.
					this.eastWestScore = this.eastWestScore - this.currentBid -this.constants.maxPointers ;
				}
				else {
					//Bidder was set.
					this.eastWestScore = this.eastWestScore - this.currentBid;
				}
			}
			else {
				
				if ((this.shootTheMoonCalled) && (pointsEarned < this.constants.maxPointers)){
					//shoot the moon was set.
					this.eastWestScore = this.eastWestScore - this.currentBid -this.constants.maxPointers ;
				}
				else if (this.shootTheMoonCalled){
					//shoot the moon was made.
					//shoot the moon was set.
					this.eastWestScore = this.eastWestScore + this.eastWestMeld + this.eastWestPointers +this.constants.maxPointers ;
				}
				else {
					//Bidder made their bid.
					this.eastWestScore = this.eastWestScore + this.eastWestPointers + this.eastWestMeld;
				}
				
			}
			if (this.northSouthPointers > (this.constants.minimumPointersToTakeMeld-1)){
				this.northSouthScore = this.northSouthScore + this.northSouthMeld + this.northSouthPointers;
			}
		}
		else {
			pointsEarned = this.northSouthPointers;
			if (this.currentBid > pointsEarned + this.northSouthMeld){
				if ((this.shootTheMoonCalled)){
					//shoot the moon was set.
					this.northSouthScore = this.northSouthScore - this.currentBid -this.constants.maxPointers ;
				}
				else {
					//Bidder was set.
					this.northSouthScore = this.northSouthScore - this.currentBid;
				}
			}
			else {
				
				if ((this.shootTheMoonCalled) && (pointsEarned < this.constants.maxPointers)){
					//shoot the moon was set.
					this.northSouthScore = this.northSouthScore - this.currentBid -this.constants.maxPointers ;
				}
				else if (this.shootTheMoonCalled){
					//shoot the moon was made.
					
					this.northSouthScore = this.northSouthScore + this.northSouthMeld + this.northSouthPointers +this.constants.maxPointers ;
				}
				else {
				
				
					//Bidder made their bid.
					this.northSouthScore = this.northSouthScore + this.northSouthPointers + this.northSouthMeld;
				}
				
			}
			if (this.eastWestPointers >(this.constants.minimumPointersToTakeMeld-1)){
				this.eastWestScore = this.eastWestScore + this.eastWestMeld + this.eastWestPointers;
			}
		}
		Log.d("DECK", "NS Score: " + this.northSouthScore);
		Log.d("DECK", "EW Score: " + this.eastWestScore);
		
		((TextView) findViewById(R.id.east_west_score)).setText("" + this.eastWestScore);
		((TextView) findViewById(R.id.north_south_score)).setText("" + this.northSouthScore);
		
	}


	private void doTrickLoop(){
		
		
		
		
		
		
		for (Integer currentPlayerThatNeedsToAct=this.leaderOfThisTrick; currentPlayerThatNeedsToAct < numberOfPlayers+this.leaderOfThisTrick; currentPlayerThatNeedsToAct++){
			
			
			int currentPlayerThatNeedsToActModded = currentPlayerThatNeedsToAct % numberOfPlayers;
			if (currentPlayerThatNeedsToActModded == 0){
				currentPlayerThatNeedsToActModded = 4;
			}
			
			if(this.massClaimIsOn == null){
				this.massClaimIsOn = Boolean.FALSE;
			}
			
			if(!this.massClaimIsOn){
				if (this.leaderOfThisTrick == currentPlayerThatNeedsToActModded){
					//Need to put a check here for if this is a mass claim situation.
					if (massClaimIsAccurate(currentPlayerThatNeedsToActModded, this.players.get(currentPlayerThatNeedsToActModded -1).getCardsInHand())){
						this.massClaimIsOn = Boolean.TRUE;
						
					}
					else {
						this.massClaimIsOn = Boolean.FALSE;
					}
				}
				//else {
					//Keep massClaim at current status.
					//this.massClaimIsOn = Boolean.FALSE;
				//}
			}
			
			if (!this.massClaimIsOn){
				Log.d("DECK", "Obtaining player " + currentPlayerThatNeedsToActModded + "'s card to be put at " + (currentPlayerThatNeedsToActModded-1) +  " index.");
				//if (currentPlayerThatNeedsToActModded != humanPlayer){
				if (!this.playersPlayedCard.get(currentPlayerThatNeedsToActModded -1)){
					if (currentPlayerThatNeedsToActModded != humanPlayer){
						this.cardsInTrick.set(currentPlayerThatNeedsToActModded-1, playACard(currentPlayerThatNeedsToActModded, this.suitLed, this.cardsInTrick));
					}
					else {
						displayPromptForPlayerToPlayCard();
						recreateCardsToBeClickedOn(); //This is needed because the cards may still have the first creation click listener which is card pass.
						return;
					}
				}
				
				if (currentPlayerThatNeedsToAct == this.leaderOfThisTrick){
					this.suitLed = this.cardsInTrick.get(currentPlayerThatNeedsToAct-1).getCardSuit();
				}
			}
			else {
				
				if (!this.massClaimMessageHasBeenDisplayed){
					Toast.makeText(this, readAndReplaceStringXML(R.string.mass_claim_text, "" + currentPlayerThatNeedsToActModded), Toast.LENGTH_LONG).show();
					this.massClaimMessageHasBeenDisplayed = Boolean.TRUE;
				}
				
				if (!this.playersPlayedCard.get(currentPlayerThatNeedsToActModded -1)){
					if (currentPlayerThatNeedsToActModded != humanPlayer){
						this.cardsInTrick.set(currentPlayerThatNeedsToActModded-1, playACard(currentPlayerThatNeedsToActModded, this.suitLed, this.cardsInTrick));
					}
					else {
						this.handLayout.setEnabled(Boolean.FALSE);//Prevent user from clicking on anything.
						playAHumanCardRobotically();
					}
				}
			
				if (currentPlayerThatNeedsToAct == this.leaderOfThisTrick){
					this.suitLed = this.cardsInTrick.get(currentPlayerThatNeedsToAct-1).getCardSuit();
				}
				
				
				
				
			}
		}
	}


	private void playAHumanCardRobotically() {
		
		if (this.leaderOfThisTrick!=humanPlayer){
			//need to trick the last click counter...Had it set that way in order to stop double click but hurts mass claims.
			this.lastClickTime = 0;
			
		}
		
		//return playCard(currentPlayerThatNeedsToAct, determineComputerCardToPlay(currentPlayerThatNeedsToAct,suitLed, cardsInTrick));
		Card cardToPlay = eligibleCardsToChoose(humanPlayer).get(0);
		OnClickListener fakeButtonOnClick = getOnClickListener(cardToPlay);
		Button fakeButton = new Button(this);
		fakeButton.setVisibility(View.GONE);
		fakeButton.setOnClickListener(fakeButtonOnClick);
		fakeButton.performClick();
	}


	
	private boolean massClaimIsAccurate(int playerId, List<Card> cardsInHand) {
		
		//place holder to turn off mass claim if needed.
		return Boolean.FALSE;
		
		/*if (cardsInHand.size() < 2){
			return Boolean.FALSE;
		}
		
		
		int amountOfCardsLeftOfTrump = this.publicKnowledge.getKnowledgeAboutDeck().cardsRemainingOfSuit(commonFuncs.getSuitInt(this.trump)).size();
		int amountOfTrumpInPlayerHand = getCardsOfSuit(cardsInHand,commonFuncs.getSuitInt(this.trump)).size();
		int trumpDifference = amountOfCardsLeftOfTrump - amountOfTrumpInPlayerHand;
		if (trumpDifference == 0){
			for (Card card : cardsInHand){
				if (card.getCardSuit() == commonFuncs.getSuitInt(this.trump) ){
					Log.d("DECK", "TRUMP CARD");
				}
				else {
					if (card.getRankOfCard() < ace){
						return Boolean.FALSE;
					}
				}
			}
		}
		else {
			//Don't bother mass claiming as trump still outstanding.
			return Boolean.FALSE;
		}
		
		//If we get here it is all trump and ace of non trump.
		return Boolean.TRUE;*/
		
		
		
	}


	private void recreateCardsToBeClickedOn() {
		this.handLayout.removeAllViews();
		Log.d("MTITUS", "cards passed boolean:" + this.humanPassedCards);
		ImageView newCardButton = new ImageView(this);
		int imageId=0;
	    for (Card cardInHand : this.players.get(humanPlayer -1).getCardsInHand()){
	    	
	    	newCardButton = new ImageView(this);
		    //imageId = getResources().getIdentifier("drawable/" + "img_card_back_96", "", getPackageName());
			imageId = getResources().getIdentifier(cardInHand.getImageFileName(), "", getPackageName());
		    newCardButton.setImageResource(imageId);
		    Log.d("MTITUS", "recreation");
		    newCardButton.setOnClickListener(getOnClickListener(cardInHand));
		    this.handLayout.addView(newCardButton);
		    //newCardButton.setId(cardsDealt+1);
		    
	    	
	    }
		
	}


	private int checkPlayersHavePlayed() {
		
		int playersHavePlayedACard = 0;
		int i=1;
		for (Boolean hasPlayedACard : this.playersPlayedCard){
			Log.d("DECK", " Player " + i + " and has played a card is " + hasPlayedACard);
			if (hasPlayedACard){
				playersHavePlayedACard = playersHavePlayedACard + 1;
			}
			i++;
		}
		return playersHavePlayedACard;
		
	}


	private void determineTrickWinnerAndAddPoints(List<Card> cardsInThisTrick, int suitLed) {
	
		//0 indexed
		
		//update player knowledge.
		for(Player player: this.players){
			this.publicKnowledge.addCardToPlayedListAndRefreshPossibilities(player.getPlayerId(), this.leaderOfThisTrick,this.cardsInTrick, commonFuncs.getSuitInt(this.trump));
		}
		this.cardsInLastTrick.clear();
		this.cardsInLastTrick.addAll(this.cardsInTrick);
		Card winningCard = determineWinnerOfTwoCards(determineWinnerOfTwoCards(determineWinnerOfTwoCards(cardsInThisTrick.get(this.leaderOfThisTrick-1), cardsInThisTrick.get(this.leaderOfThisTrick% numberOfPlayers), suitLed), cardsInThisTrick.get((this.leaderOfThisTrick+1) % numberOfPlayers), suitLed), cardsInThisTrick.get((this.leaderOfThisTrick+2) % numberOfPlayers), suitLed);
		TextView eastWestPointerText = (TextView) findViewById(R.id.east_west_pointers);
		TextView northSouthPointerText = (TextView) findViewById(R.id.north_south_pointers);
		TextView eastWestPointerNeededText = (TextView) findViewById(R.id.east_west_pointers_needed);
		TextView northSouthPointerNeededText = (TextView) findViewById(R.id.north_south_pointers_needed);
		for (Integer playerNumber=this.leaderOfThisTrick; playerNumber < numberOfPlayers+1; playerNumber++){
			Log.d("DECK", " Seeing if Player " + playerNumber + " won the trick");
			if (cardsInThisTrick.get(playerNumber-1) == winningCard){
				Log.d("DECK", "Player " + playerNumber + " won the trick");
				this.leaderOfThisTrick = playerNumber;
				break;
			}
			else if(playerNumber == numberOfPlayers){
				playerNumber = 0;//This will restart the loop.
			}
		}
		
		if (this.leaderOfThisTrick % 2 == 0){
			this.eastWestPointers = this.eastWestPointers + determinePointers(cardsInThisTrick);
			
			try {
				int currentPointersNeeded = Integer.parseInt(eastWestPointerNeededText.getText().toString());
				currentPointersNeeded = currentPointersNeeded - determinePointers(cardsInThisTrick);
				if (currentPointersNeeded < 1){
					eastWestPointerNeededText.setText(readFromStringXML(R.string.made));
				}
				else {
					eastWestPointerNeededText.setText("" + currentPointersNeeded);
				}
			}
			catch(NumberFormatException e){
				//Goal was already met do nothing.
			}
			
		} else {
			this.northSouthPointers = this.northSouthPointers + determinePointers(cardsInThisTrick);
			try {
				int currentPointersNeeded = Integer.parseInt(northSouthPointerNeededText.getText().toString());
				currentPointersNeeded = currentPointersNeeded - determinePointers(cardsInThisTrick);
				if (currentPointersNeeded < 1){
					northSouthPointerNeededText.setText(readFromStringXML(R.string.made));
				}
				else {
					northSouthPointerNeededText.setText("" + currentPointersNeeded);
				}
			}
			catch(NumberFormatException e){
				//Goal was already met do nothing.
			}
		}
		
		if (this.bidWinner % 2 == 0){
			if (northSouthPointerNeededText.getText().equals(readFromStringXML(R.string.made))){
				eastWestPointerNeededText.setText(readFromStringXML(R.string.set));
			}
		}
		if (this.bidWinner % 2 == 1){
			if (eastWestPointerNeededText.getText().equals(readFromStringXML(R.string.made))){
				northSouthPointerNeededText.setText(readFromStringXML(R.string.set));
			}
		}
		
		
		Log.d("DECK", "EW Pointers: " + this.eastWestPointers);
		Log.d("DECK", "NS Pointers: " + this.northSouthPointers);
		
		eastWestPointerText.setText("" + this.eastWestPointers);
		northSouthPointerText.setText("" + this.northSouthPointers);
		
		
		//Set up for next trick.
		for (int playerInt=1; playerInt < numberOfPlayers + 1; playerInt++){
	    	this.playersPlayedCard.set(playerInt -1, Boolean.FALSE);
	    	
	    }
		
		hideOrShowFrame(R.id.btn_show_last_trick, View.VISIBLE);
		
		//playATrick();
		if (this.players.get(0).getCardsInHand().size() > 0){
			playATrick();
		}
		else{
			pointerForLastTrick();
		}
		
		
		
		
		
	}
	
	private void pauseToRedealCards(int pauseTime){
		//Log.d("DECK", "Start Pause Time " + getCurrentTime());
		mHandler.postDelayed(new Runnable() {
            public void run() {
                dealCards();
            }
        }, pauseTime);
		//Log.d("DECK", "End Pause Time " + getCurrentTime());
	}

	private void pauseAfterTrickWork(int pauseTime){
		//Log.d("DECK", "Start Pause Time " + getCurrentTime());
		mHandler.postDelayed(new Runnable() {
            public void run() {
                doRemainingStuff();
            }
        }, pauseTime);
		//Log.d("DECK", "End Pause Time " + getCurrentTime());
	}
	
	 private void doRemainingStuff() {
	        //Toast.makeText(this, "Delayed Toast!", Toast.LENGTH_SHORT).show();
	        if (checkPlayersHavePlayed() == numberOfPlayers){
				//Log.d("DECK", "Cards of this trick:" + cardsInThisTrick.toString());
				determineTrickWinnerAndAddPoints(this.cardsInTrick, this.suitLed);
			}
			if (this.northSouthPointers + this.eastWestPointers == this.constants.maxPointers){
				clearMeldPointersBid();
				hidePromptForPlayerToPlayCard();
				hideOrShowFrame(handLayout.getId(), View.INVISIBLE);
				hideOrShowFrame(R.id.hand_title, View.INVISIBLE);
				for (int playerInt=1; playerInt < numberOfPlayers+1; playerInt++){
					//hideOrShowFrame(getResources().getIdentifier("@id/player" + playerInt + "CardPlayedImage" , "", getPackageName()),View.INVISIBLE);
					hideOrShowFrame(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + playerInt + readFromStringXML(R.string.card_played_image) , "", getPackageName()),View.INVISIBLE);
				}
				
				updateScore();
				
				displayScoreOnScreen();
			}
	 }
	 
	 
	 private void victoryOrNewHand(){
		 if(!checkVictoryConditions()){
				newRound();
			}
			else {
				doVictoryWork();
			}
		 
	 }

	private void pointerForLastTrick() {
		
		
		//Log.d("DECK", "NS Pointers: " + this.northSouthPointers );
		//Log.d("DECK", "EW Pointers: " + this.eastWestPointers);
		Log.d("DECK", "Final trick boost");
		//Winner of the last trick gets one additional pointer.
		if (this.leaderOfThisTrick % 2 == 0){
			this.eastWestPointers = this.eastWestPointers + this.constants.pointerBaseUnit;
		}
		else {
			this.northSouthPointers = this.northSouthPointers + this.constants.pointerBaseUnit;
		}
		Log.d("DECK", "NS Pointers: " + this.northSouthPointers );
		Log.d("DECK", "EW Pointers : " + this.eastWestPointers);
		((TextView) findViewById(R.id.east_west_pointers)).setText("" + this.eastWestPointers);
		((TextView) findViewById(R.id.north_south_pointers)).setText("" + this.northSouthPointers);
		
	}


	private int determinePointers(List<Card> cardsInThisTrick) {
		
		int pointers = 0;
		for (Card card : cardsInThisTrick){
			if ((card.getRankOfCard() == king) || (card.getRankOfCard() == ace) || (card.getRankOfCard() == ten)) {
				pointers = pointers + this.constants.pointerBaseUnit;
			}
		}
		return pointers;
	}


	private Card determineWinnerOfTwoCards(Card card1, Card card2, int suitLed) {
		
		
		if ((card1== null) || (card2==null)){
			if (card1== null && card2== null){
				return null;
			}
			else if(card1!=null){
				return card1;
			}
			else if (card2!=null){
				return card2;
			}
		
		}
		if (suitLed == 0){
			Log.d("DECK", "ERROR occurred");
		}
		if (card1.getCardSuit() == commonFuncs.getSuitInt(this.trump)){
			if (card2.getCardSuit() != commonFuncs.getSuitInt(this.trump)){
				return card1;
			}
		}
		else {
			if (card2.getCardSuit() == commonFuncs.getSuitInt(this.trump)){
				return card2;
			}
		}
		
		//If here is reached neither card is trump.
		//Check if what was led was something other than trump. 
		//If something other than trump was led, then see if only one person followed suit.
		if (commonFuncs.getSuitInt(this.trump) != suitLed){
			if (card1.getCardSuit() == suitLed){
				if (card2.getCardSuit() != suitLed){
					return card1;
				}
			}
			else {
				if (card2.getCardSuit() == suitLed){
					return card2;
				}
			}
		}
		
		//This will only be reached in the following conditions:
		
		//1) Both cards are trump.
		//2) Both cards are the lead suit which is not trump.
		//3) Therefore comparisons can begin based on rank.
		
		if (card1.getRankOfCard()> card2.getRankOfCard()){
			return card1;
		}
		else if (card2.getRankOfCard() > card1.getRankOfCard()){
			return card2;
		}
		else {
			//This means that both the cards are the same and the first occurrence since starting from the leader of the trick is the right one.
			//So returning a card here has no meaning since they are the same object.
			return card1;
		}
		
	}


	//This method is for Computer players only but can have pieces uncommented and leveraged for simulation.
	//The if statement that calls this function would also need to be changed.
	private Card playACard(Integer currentPlayerThatNeedsToAct, int suitLed, List<Card> cardsInTrick) {
	
		
		Log.d("DECK", "Player " + currentPlayerThatNeedsToAct.toString() + " to play a card");
		//if (currentPlayerThatNeedsToAct != humanPlayer){
			//Log.d("DECK", "Player " + currentPlayerThatNeedsToAct.toString() + " to play " + determineComputerCardToPlay(currentPlayerThatNeedsToAct));
			return playCard(currentPlayerThatNeedsToAct, determineComputerCardToPlay(currentPlayerThatNeedsToAct,suitLed, cardsInTrick));
		//}
		//For simulation purposes only.
		/*else {
			return playCard(currentPlayerThatNeedsToAct, determineHumanCardToPlay(humanPlayer, suitLed, cardsInTrick));
		}*/
		
	}


	


	private Card playCard(Integer currentPlayerThatNeedsToAct,
			Card cardToPlay) {
	
		//Log.d("DECK", "Player " + currentPlayerThatNeedsToAct + " to play " + cardToPlay.toString());
		//Log.d("DECK", cardToPlay.toString());
		if (currentPlayerThatNeedsToAct == 1){
			((ImageView) findViewById(R.id.player1CardPlayedImage)).setImageResource(getResources().getIdentifier(cardToPlay.getImageFileName(), "", getPackageName()));
		}
		else if (currentPlayerThatNeedsToAct == 2){
			((ImageView) findViewById(R.id.player2CardPlayedImage)).setImageResource(getResources().getIdentifier(cardToPlay.getImageFileName(), "", getPackageName()));
		}
		else if (currentPlayerThatNeedsToAct == 3){
			((ImageView) findViewById(R.id.player3CardPlayedImage)).setImageResource(getResources().getIdentifier(cardToPlay.getImageFileName(), "", getPackageName()));
		}
		else if (currentPlayerThatNeedsToAct == 4){
			((ImageView) findViewById(R.id.player4CardPlayedImage)).setImageResource(getResources().getIdentifier(cardToPlay.getImageFileName(), "", getPackageName()));
		
		}
		
		
		return cardToPlay;
		
	}

	
	private List<Card> eligibleCardsToChoose(int playerId){
		Integer currentPlayerThatNeedsToAct=playerId;
		Log.d("DECK", "ECTC: Player " + currentPlayerThatNeedsToAct + " to play a card");
		
		List<Card> eligibleCardsToPlay = new ArrayList<Card>();
		List<Card> eligibleCardsThatWillWinHand = new ArrayList<Card>();
		List<Card> cardsOfThatSuitPlayed = new ArrayList<Card>();
		int maxRankOfCardsPlayed = 0;
		//No suit was led more freedom.
		if (suitLed == 0){
			eligibleCardsToPlay = this.players.get(currentPlayerThatNeedsToAct -1).getCardsInHand();
		} else {
			
			//Suit led was determined.
			eligibleCardsToPlay = getCardsOfSuit(this.players.get(currentPlayerThatNeedsToAct -1).getCardsInHand(), this.suitLed);
			cardsOfThatSuitPlayed = getCardsOfSuit(this.cardsInTrick, this.suitLed);
			
			
			//Check to see if the player has any of the led suit. If yes go down if statement. If not else statement.
			if (eligibleCardsToPlay.size() > 0){
				maxRankOfCardsPlayed = getMaxRankOfCards(cardsOfThatSuitPlayed);
				
				//For cards that are of the appropriate suitLed, see which if any will win the trick.
				for (Card eligibleCard : eligibleCardsToPlay){
					//If the suit led is trump then the winner will be the one that is higher rank than any trump currently played.
					if(this.suitLed == commonFuncs.getSuitInt(this.trump)){
						if (eligibleCard.getRankOfCard() > maxRankOfCardsPlayed){
							eligibleCardsThatWillWinHand.add(eligibleCard);
						}
					//If the suit led is not trump then we must investigate if trump has been played.
					} else {
						//If trump has not been played then must check rank. Else any card of that suit is valid.
						if (!(getCardsOfSuit(this.cardsInTrick,commonFuncs.getSuitInt(this.trump)).size() > 0)){
							if (eligibleCard.getRankOfCard() > maxRankOfCardsPlayed){
								eligibleCardsThatWillWinHand.add(eligibleCard);
							}
						}
					}
				}
				if (eligibleCardsThatWillWinHand.size() > 0){
					eligibleCardsToPlay = eligibleCardsThatWillWinHand;
				}
				//Else they can play anything of that suit.
			}
			//See if the player has trump to play.
			else {
				eligibleCardsToPlay = getCardsOfSuit(this.players.get(currentPlayerThatNeedsToAct -1).getCardsInHand(), commonFuncs.getSuitInt(this.trump));
				cardsOfThatSuitPlayed = getCardsOfSuit(this.cardsInTrick, commonFuncs.getSuitInt(this.trump));
				
				
				if (eligibleCardsToPlay.size() > 0){
					//Player has trump. See what trump beats highest played so far.
					maxRankOfCardsPlayed = getMaxRankOfCards(cardsOfThatSuitPlayed);
					for (Card eligibleCard : eligibleCardsToPlay){
						if (eligibleCard.getRankOfCard() > maxRankOfCardsPlayed){
							eligibleCardsThatWillWinHand.add(eligibleCard);
						}
					}
					if (eligibleCardsThatWillWinHand.size() > 0){
						eligibleCardsToPlay = eligibleCardsThatWillWinHand;
					}
				}
				else {
					//Any card played will lose so player may play whatever they like.
					eligibleCardsToPlay = this.players.get(currentPlayerThatNeedsToAct -1).getCardsInHand();
				}
				
			}
			
		}
		return eligibleCardsToPlay;
	}
	
	//for simulation only. Uncomment when needed.
	/*private Card determineHumanCardToPlay(int humanplayer, int suitLed, List<Card> cardsCurrentlyInTrick) {
	
		
		//Card cardToPlay=this.players.get(humanplayer -1).getCardsInHand().get(0);
		Integer currentPlayerThatNeedsToAct=humanplayer;
		Log.d("DECK", "DCCHP: Player " + currentPlayerThatNeedsToAct + " to play a card");
		
		List<Card> eligibleCardsToPlay = new ArrayList<Card>();
		List<Card> eligibleCardsThatWillWinHand = new ArrayList<Card>();
		List<Card> cardsOfThatSuitPlayed = new ArrayList<Card>();
		int maxRankOfCardsPlayed = 0;
		//No suit was led more freedom.
		if (suitLed == 0){
			eligibleCardsToPlay = this.players.get(currentPlayerThatNeedsToAct -1).getCardsInHand();
		} else {
			
			//Suit led was determined.
			eligibleCardsToPlay = getCardsOfSuit(this.players.get(currentPlayerThatNeedsToAct -1).getCardsInHand(), suitLed);
			cardsOfThatSuitPlayed = getCardsOfSuit(cardsCurrentlyInTrick, suitLed);
			
			if (eligibleCardsToPlay.size() > 0){
				maxRankOfCardsPlayed = getMaxRankOfCards(cardsOfThatSuitPlayed);
				
				for (Card eligibleCard : eligibleCardsToPlay){
					if (eligibleCard.getRankOfCard() > maxRankOfCardsPlayed){
						eligibleCardsThatWillWinHand.add(eligibleCard);
					}
				}
				if (eligibleCardsThatWillWinHand.size() > 0){
					eligibleCardsToPlay = eligibleCardsThatWillWinHand;
				}
				//Else they can play anything of that suit.
			}
			else {
				eligibleCardsToPlay = getCardsOfSuit(this.players.get(currentPlayerThatNeedsToAct -1).getCardsInHand(), commonFuncs.getSuitInt(this.trump));
				cardsOfThatSuitPlayed = getCardsOfSuit(cardsCurrentlyInTrick, commonFuncs.getSuitInt(this.trump));
				
				//Player will lose the trick can play what they want.
				if (eligibleCardsToPlay.size() > 0){
					maxRankOfCardsPlayed = getMaxRankOfCards(cardsOfThatSuitPlayed);
					for (Card eligibleCard : eligibleCardsToPlay){
						if (eligibleCard.getRankOfCard() > maxRankOfCardsPlayed){
							eligibleCardsThatWillWinHand.add(eligibleCard);
						}
					}
					if (eligibleCardsThatWillWinHand.size() > 0){
						eligibleCardsToPlay = eligibleCardsThatWillWinHand;
					}
				}
				else {
					//Any card played will lose so player may play whatever they like.
					eligibleCardsToPlay = this.players.get(currentPlayerThatNeedsToAct -1).getCardsInHand();
				}
				
			}
			
		}
	
		
		Card cardToPlay = eligibleCardsToPlay.get(0);
		
		Log.d("DECK", "DHCTP: Player " + humanplayer + " to play " + cardToPlay.toString());
		
		this.players.get(humanplayer -1).getCardsInHand().remove(cardToPlay);
		this.playersPlayedCard.set(humanplayer -1, Boolean.TRUE);
		Collections.sort(this.players.get(humanPlayer -1).getCardsInHand(),commonFuncs.cardSorter);
		GridLayout handLayout =(GridLayout) findViewById(R.id.hand_grid);
		handLayout.removeAllViews();
		ImageView newCardButton = new ImageView(this);
		int imageId=0;
		
	    for (Card cardInHand : this.players.get(humanPlayer -1).getCardsInHand()){
	    	
	    	newCardButton = new ImageView(this);
			imageId = getResources().getIdentifier(cardInHand.getImageFileName(), "", getPackageName());
		    newCardButton.setImageResource(imageId);
		    newCardButton.setOnClickListener(getOnClickListener(cardInHand));
		    handLayout.addView(newCardButton);
		
	    	
	    }
		
		return cardToPlay;
		
	}*/


	private Card determineComputerCardToPlay(
			Integer currentPlayerThatNeedsToAct, int suitLed, List<Card> cardsCurrentlyInTrick) {
		
		Log.d("DECK", "DCCTP: Player " + currentPlayerThatNeedsToAct + " to play a card");
		
		
		
		List<Card> eligibleCardsToPlay = eligibleCardsToChoose(currentPlayerThatNeedsToAct);
		
	
		
		Card cardToPlay = bestPlayForPlayer(eligibleCardsToPlay,this.players.get(currentPlayerThatNeedsToAct-1));
		
		
		
		//The cardToPlay is an instance of the card but is not the card that is in eligible cards.
		//We need to determine its location and "extract" the cardToPlay.
		//We are safe to return the first instance as we don't care what position the card is in as long as it is extracted.
		cardToPlay = eligibleCardsToPlay.get(getCardLocationEqual((ArrayList<Card>)eligibleCardsToPlay, cardToPlay).get(0));
		
		if (cardToPlay == null){
			Log.d("DECK", "ERROR of the worst kind!!!!");
			return null; //This will bubble to an exception down the line.
		}
		
		//Leave this alone.
		Log.d("DECK", "DCCTP: Player " + currentPlayerThatNeedsToAct + " to play " + cardToPlay.toString());
		this.players.get(currentPlayerThatNeedsToAct -1).getCardsInHand().remove(cardToPlay);
		removeACardPictureFromScreen(currentPlayerThatNeedsToAct);
		this.playersPlayedCard.set(currentPlayerThatNeedsToAct -1, Boolean.TRUE);
		this.cardsPlayedThisHand.add(cardToPlay);
		return cardToPlay;
		
		
		//return null;
	}


	private Card bestPlayForPlayer(List<Card> eligibleCards, Player player) {
		
		
		int partnerInt = (player.getPlayerId() +2) % numberOfPlayers;
		if (partnerInt == 0){partnerInt = 4;}
		int opp1 = (player.getPlayerId() +1) % numberOfPlayers;
		if (opp1 == 0){opp1 = 4;}
		int opp3 = (player.getPlayerId() +3) % numberOfPlayers;
		if (opp3 == 0){opp1 = 4;}
		
		if (player.getRole().equals(winnerRoleString)){
			
			//if leading the trick determine what to lead.
			
			
			//No need for eligibility check as all cards are eligible when the leader of the trick.
			if (this.leaderOfThisTrick == player.getPlayerId()){
				//Smoke out trump if applicable.
				int amountOfCardsLeftOfTrump = this.publicKnowledge.getKnowledgeAboutDeck().cardsRemainingOfSuit(commonFuncs.getSuitInt(this.trump)).size();
				int amountOfTrumpInPlayerHand = getCardsOfSuit(this.players.get(player.getPlayerId() -1).getCardsInHand(),commonFuncs.getSuitInt(this.trump)).size();
				int trumpDifference = amountOfCardsLeftOfTrump - amountOfTrumpInPlayerHand;
				if (trumpDifference <= 0){
					//Trump is smoked out or NPC was very unlucky and could not hold it's trump. Trump are all winners play a non trump that will win if it exists.
					Card highestNonTrump = determineHighestRemainingNonTrump(this.players.get(player.getPlayerId() - 1).getCardsInHand());
					//Sometimes NonTrump are winners or none are left. Return highest trump as it is a guaranteed winner.
					if (highestNonTrump == null){
						return determineHighestRemainingTrump(this.players.get(player.getPlayerId() - 1).getCardsInHand());
					}
					List<Boolean> playersThatMightBeatThatCard = this.publicKnowledge.possiblePlayersWithCardsThatMightTakeTrickIfThisLed(highestNonTrump,commonFuncs.getSuitInt(this.trump), trumpDifference);
					//Set yourself to false;
					playersThatMightBeatThatCard.set(player.getPlayerId() -1, Boolean.FALSE);
					Boolean aPlayerMightTakeTrick = Boolean.FALSE;
					for(boolean theyCould: playersThatMightBeatThatCard){
						  if(theyCould){ aPlayerMightTakeTrick = Boolean.TRUE;break;}
					}
					if (!aPlayerMightTakeTrick){
						return highestNonTrump;
					}
					
					
					//We need to loop through the remaining nonTrump. Maybe another suit is a viable play that will end up with a "sure win".
					Card sureWinNonTrumpCard = loopAndGiveMeSureNonTrumpWin(player.getPlayerId(), this.players.get(player.getPlayerId() - 1).getCardsInHand());
					if (sureWinNonTrumpCard!=null){
						return sureWinNonTrumpCard;
					}
					else {
						//See if we have more trump try to lure out the card that will beat yours.
						//Do this until you have one trump if you have to.
						if (amountOfCardsLeftOfTrump > 1){
							return determineHighestRemainingTrump(this.players.get(player.getPlayerId() - 1).getCardsInHand());
						}
						//If this doesn't work you'll have to return the highestNonTrump and hope it wins. Most likely it will but maybe not.
						else {
								return highestNonTrump;
							
						}
					}
					
				}
				else {
					//We need to smoke out trump.
					Card highestTrumpCardInHand = determineHighestRemainingTrump(this.players.get(player.getPlayerId() - 1).getCardsInHand()) ;
					List<Boolean> playersThatMightBeatHighestTrump = this.publicKnowledge.possiblePlayersWithCardsThatMightTakeTrickIfThisLed(highestTrumpCardInHand,commonFuncs.getSuitInt(this.trump), trumpDifference);
					//Set yourself to false as you can't beat your own card.
					playersThatMightBeatHighestTrump.set(player.getPlayerId() -1, Boolean.FALSE);
					Boolean aPlayerMightBeatThatTrump = Boolean.FALSE;
					for(boolean theyCould: playersThatMightBeatHighestTrump){
						  if(theyCould){ aPlayerMightBeatThatTrump = Boolean.TRUE;break;}
					}
					if (!aPlayerMightBeatThatTrump){
						return highestTrumpCardInHand;
					}
					else {
						Card highestNonPointTrumpCard =determineHighestLoserInTrump(this.players.get(player.getPlayerId() - 1).getCardsInHand());
						if (highestNonPointTrumpCard!= null){
							return highestNonPointTrumpCard;
						}
						else {
							return determineLowestPointEarnerInTrump(this.players.get(player.getPlayerId() - 1).getCardsInHand());
						}
					}
				}
				
				//return eligibleCards.get(0);
			}
			else{
				
				//You need to get back control asap.
				
				List<Card> cardsOfSuitLed = getCardsOfSuit(eligibleCards,suitLed);
				Card maxRankCard = getMaxRankCard(cardsOfSuitLed);
				Card maxRankOfCardsPlayed = getMaxRankCard(getCardsOfSuit(this.cardsInTrick, suitLed));
				
				if (cardsOfSuitLed.size() > 0){
					if(maxRankCard.getRankOfCard() > maxRankOfCardsPlayed.getRankOfCard()){
						
						
						int maxRankNeeded = 0;
						for (int playerId=1; playerId<numberOfPlayers+1;playerId++){
							if (playerId!=player.getPlayerId()){
								Card tempCard = this.publicKnowledge.getKnowledgeAboutPlayers().get(playerId-1).getHighestPossibleCardOfSuit(this.suitLed);
								if (tempCard == null){
									continue; //no valid play
								}
								int tempMax = tempCard.getRankOfCard();
								
								if (maxRankNeeded == 0 ){
									maxRankNeeded = tempMax;
									continue;
								}
								else {
									if (tempMax > maxRankNeeded){
										maxRankNeeded = tempMax;
										continue;
									}
									else {
										continue;
									}
								}
							}
							else {
								continue;//don't compare against self;
							}
						}
						if (maxRankNeeded > maxRankCard.getRankOfCard()){
							//Hope for the best
							return maxRankCard;
						}
						else {
							for (int rank : allRanks){
								if (countNumberEqual((ArrayList<Card>)cardsOfSuitLed, new Card(rank, this.suitLed)) > 0){
									if (maxRankCard.getRankOfCard() > maxRankNeeded){
										return new Card(rank, this.suitLed);
									}
								}
							}
							
							//If here everything was equal to max. very likely win.
							return maxRankCard;
						}
						
					}
					else {
						
						return determineLowestRemainingCardSuit(eligibleCards,suitLed);
					}
				} 
				else {
					
					
					//Hopefully you can trump this sucker.
					//In fact so likely allocate a List<Card> Object then get its size.
					List<Card> cardsRemainingOfTrump = getCardsOfSuit(eligibleCards,commonFuncs.getSuitInt(this.trump));
					int numberOfTrump = cardsRemainingOfTrump.size();
					
					
					if (numberOfTrump > 0){
						//Yay we can trump and hopefully win.
						//Be uber aggressive. Can substitute this for a trump pull before.
						return getMaxRankCard(cardsRemainingOfTrump);
					}
					else {
						//This is a forced loss. Not good. How the heck did bid winner run out of trump.
						
						return determineLowestRemainingCard(eligibleCards);
						
					}
				}
				
				
				
				
			}
			
			
			
			//OLD AI STUPID
			//return eligibleCards.get(0);
			
		
			
		}
		else if (player.getRole().equals(dummyRoleString)){
			
			
			if (this.leaderOfThisTrick == player.getPlayerId()){
				//If you are the leader you need to find way to give control to partner.
				List<Boolean> partnerMightHaveTheseSuits = new ArrayList<Boolean>();
				List<Integer> highestPossibleOfSuit = new ArrayList<Integer>();
				for (int suit: allSuits){
					if (this.publicKnowledge.getKnowledgeAboutPlayers().get(this.bidWinner -1).getPossiblyHasCardsOfSuit(suit)){
						partnerMightHaveTheseSuits.add(Boolean.TRUE);
						highestPossibleOfSuit.add(this.publicKnowledge.getKnowledgeAboutPlayers().get(this.bidWinner -1).getHighestPossibleCardOfSuit(suit).getRankOfCard());
					}
					else {
						partnerMightHaveTheseSuits.add(Boolean.FALSE);
						highestPossibleOfSuit.add(0);
					}
				}
				
				int awesomeSuit =0;
				for (int suit: allSuits){
					
					if (partnerMightHaveTheseSuits.get(suit -1)){
						Card lowestCardOfSuit = determineLowestRemainingCardSuit(getCardsOfSuit(eligibleCards,suit), suit);
						if (lowestCardOfSuit == null){
							continue; // can't lead this.
						}
						awesomeSuit = awesomeSuitDetection(eligibleCards, player, lowestCardOfSuit);
						if (awesomeSuit !=0){
							break;
						}
					}
						
						
				}
				if (awesomeSuit !=0){
					
					//Since partner will win no matter what we play of that suit, play the highest to try to get another pointer.
					//Added feedback from beta tester. 
					Card card = getMaxRankCardNotAce(getCardsOfSuit(eligibleCards,awesomeSuit));
					if (card!=null){
						return card;
					}
					else {
						//This will pull an ace if required.
						card =  getMaxRankCard(getCardsOfSuit(eligibleCards,awesomeSuit));
						if (card!=null){
							return card;
						}
						else{
							//we have no cards in that suit.
							return getMaxRankCard(eligibleCards);
						}
					}
				}
				else {
					//Nothing is a guaranteed win if you play your lowest card.
					//re evaluate for lowest pointer.
					for (int suit: allSuits){
						
						if (partnerMightHaveTheseSuits.get(suit -1)){
							Card lowestPointerOfSuit = determineLowestPointEarnerInSuit(getCardsOfSuit(eligibleCards,suit), suit);
							if (lowestPointerOfSuit == null){
								continue; // can't lead this.
							}
							awesomeSuit = awesomeSuitDetection(eligibleCards, player, lowestPointerOfSuit);
							if (awesomeSuit !=0){
								break;
							}
						}
							
							
					}
					if (awesomeSuit !=0){
						
						//Since partner will win no matter what we play of that suit, play the highest to try to get another pointer.
						//Added feedback from beta tester. 
						Card card = getMaxRankCardNotAce(getCardsOfSuit(eligibleCards,awesomeSuit));
						if (card!=null){
							return card;
						}
						else {
							//This will pull an ace if required.
							card =  getMaxRankCard(getCardsOfSuit(eligibleCards,awesomeSuit));
							if (card!=null){
								return card;
							}
							else{
								//we have no cards in that suit.
								return getMaxRankCard(eligibleCards);
							}
						}
					}
					else {
						//Nothing is a guaranteed win.
						//sloth your lowest card to avoid giving away pointers.
						return determineLowestRemainingCard(eligibleCards);
					}
				}
				
			}

			else if (this.leaderOfThisTrick == this.bidWinner){
				//If partner lead see if anyone could beat that card that is not the bid winner.
				
				
				int currentWinner = winnerOfTrickSoFar();
				//If the bidWinner is not the current winner so far this is a sloth.
				//Put your lowest eligible card on it.
				if (currentWinner!=this.bidWinner){
					return determineLowestRemainingCard(eligibleCards);
				}
				else {
					//more analysis required. Need to determine if can play pointer.
					//This means only the player clockwise will be left.
					int nextPlayer = player.getPlayerId() +1;
					if (nextPlayer == (numberOfPlayers +1)){
						nextPlayer = 1;
					}
					Boolean playerMightHaveSuit = this.publicKnowledge.getKnowledgeAboutPlayers().get(nextPlayer -1).getPossiblyHasCardsOfSuit(this.suitLed);
					Boolean playerMightHaveTrump = this.publicKnowledge.getKnowledgeAboutPlayers().get(nextPlayer -1).getPossiblyHasCardsOfSuit(commonFuncs.getSuitInt(this.trump));
					//No way for player to win.
					if ((!playerMightHaveSuit)&&(!playerMightHaveTrump)){
						
						//Had to replace with Ace last resort.
						Card card = determineHighestRemainingCardNotAce(eligibleCards);
						if (card!=null){
							return card;
						}
						else {
							return determineHighestRemainingCard(eligibleCards);
						}
					}
					else {
						//Need to analyze and see if they have possible winning plays.
						if (this.suitLed == commonFuncs.getSuitInt(this.trump)){
							if (playerMightHaveSuit){
								Card highestPossibleRankCard = this.publicKnowledge.getKnowledgeAboutPlayers().get(nextPlayer -1).getHighestPossibleCardOfSuit(this.suitLed);
								Card cardFirstLed = this.cardsInTrick.get(this.bidWinner -1); 
								if (highestPossibleRankCard.getRankOfCard() <= cardFirstLed.getRankOfCard()){
									//Last player can't win.
									//Had to replace with Ace last resort.
									Card card = determineHighestRemainingCardNotAce(eligibleCards);
									if (card!=null){
										return card;
									}
									else {
										return determineHighestRemainingCard(eligibleCards);
									}
								}
								else {
									//Very likely this is a sloth. play lowest remaining card.
									return determineLowestRemainingCard(eligibleCards);
								}
							} else{
								//We shouldn't have gotten here or something severely broke down but I'm putting this here to avoid the dummy ending.
								return determineLowestRemainingCard(eligibleCards);
							}
						}
						else {
							if (playerMightHaveSuit){
								Card highestPossibleRankCard = this.publicKnowledge.getKnowledgeAboutPlayers().get(nextPlayer -1).getHighestPossibleCardOfSuit(this.suitLed);
								Card cardFirstLed = this.cardsInTrick.get(this.bidWinner -1); 
								if (highestPossibleRankCard.getRankOfCard() <= cardFirstLed.getRankOfCard()){
									//Last player can't win.
									//Had to replace with Ace last resort.
									Card card = determineHighestRemainingCardNotAce(eligibleCards);
									if (card!=null){
										return card;
									}
									else {
										return determineHighestRemainingCard(eligibleCards);
									}
								}
								else {
									//Very likely this is a sloth. play lowest remaining card.
									return determineLowestRemainingCard(eligibleCards);
								}
							} else{
								//Suit led was not trump. Player has no of original suit. Player must trump.
								//This will probably only be hit by a human bid winner or a horrible bidder.
								return determineLowestRemainingCard(eligibleCards);
								
								
							}
						}
					}
				}
				
				
				//This is AI Stupid
				//return eligibleCards.get(0);
				
			}
			else {
				//If partner did not lead then east west stole the last trick.
				Card cardPartnerPlayed = this.cardsInTrick.get(this.bidWinner -1);
				//If partner has played you are last to act.
				if (cardPartnerPlayed != null){
					//This means all have played but you.
					int currentWinner = winnerOfTrickSoFar();
					if (currentWinner == this.bidWinner){
						//Had to replace with Ace last resort.
						Card card = determineHighestRemainingCardNotAce(eligibleCards);
						if (card!=null){
							return card;
						}
						else {
							return determineHighestRemainingCard(eligibleCards);
						}
					}
					else{
						//One of two situations. 1) no way to win -- avoid pointers if possible.
						//2) Forced win since last to play. Keep higher winners for later.
						return determineLowestRemainingCard(eligibleCards);
					}
				}
				else {
					//Need to analyze if partner can win;
					Boolean playerMightHaveSuit = this.publicKnowledge.getKnowledgeAboutPlayers().get(this.bidWinner -1).getPossiblyHasCardsOfSuit(this.suitLed);
					Boolean playerMightHaveTrump = this.publicKnowledge.getKnowledgeAboutPlayers().get(this.bidWinner -1).getPossiblyHasCardsOfSuit(commonFuncs.getSuitInt(this.trump));
					if (playerMightHaveSuit){
						Card highestPossibleCardOfSuit = this.publicKnowledge.getKnowledgeAboutPlayers().get(this.bidWinner -1).getHighestPossibleCardOfSuit(this.suitLed);
						//Note can use opp 3 as we've established they were the one to win the trick.
						if (highestPossibleCardOfSuit.getRankOfCard() > this.cardsInTrick.get(opp3 -1).getRankOfCard()){
							//Had to replace with Ace last resort.
							Card card = determineHighestRemainingCardNotAce(eligibleCards);
							if (card!=null){
								return card;
							}
							else {
								return determineHighestRemainingCard(eligibleCards);
							}
						}
						else {
							//If they have the suit but it won't win against already led (e.g. meld) then throw a card.
							return determineLowestRemainingCard(eligibleCards);
						}
					} 
					else if (playerMightHaveTrump){
						//may the trump be with you.
						return determineHighestRemainingCard(eligibleCards);
					}
					else {
						//partner cannot win. If you can win it is forced win.
						return determineLowestRemainingCard(eligibleCards);
					}
				}
				
				//AI Stupid
				//return eligibleCards.get(0);
			}
			
			
			
		}
		else if (player.getRole().equals(otherTeamRoleString)){
			
			//See if partner led.
			if (this.leaderOfThisTrick == partnerInt){
				int currentWinner = winnerOfTrickSoFar();
				if (currentWinner == partnerInt){
					//See if opp1 can win. If not play high. If possibility play low.
					Boolean playerMightHaveSuit = this.publicKnowledge.getKnowledgeAboutPlayers().get(opp1 -1).getPossiblyHasCardsOfSuit(this.suitLed);
					Boolean playerMightHaveTrump = this.publicKnowledge.getKnowledgeAboutPlayers().get(opp1 -1).getPossiblyHasCardsOfSuit(commonFuncs.getSuitInt(this.trump));
					if (playerMightHaveSuit){
						Card highestPossibleCardOfSuit = this.publicKnowledge.getKnowledgeAboutPlayers().get(opp1 -1).getHighestPossibleCardOfSuit(this.suitLed);
						//Note can use opp 3 as we've established they were the one to win the trick.
						if (highestPossibleCardOfSuit.getRankOfCard() > this.cardsInTrick.get(this.leaderOfThisTrick -1).getRankOfCard()){
							//They may win.
							return determineLowestRemainingCard(eligibleCards);
						}
						else {
							//If they have the suit but it won't win against already led (e.g. meld) then pounce.
							return determineLowestRemainingCard(eligibleCards);
						}
					} 
					else if (playerMightHaveTrump){
						
						return determineLowestRemainingCard(eligibleCards);
					}
					else {
						//opp1 cannot win. If you can win it is forced win.
						//Had to replace with Ace last resort.
						Card card = determineHighestRemainingCardNotAce(eligibleCards);
						if (card!=null){
							return card;
						}
						else {
							return determineHighestRemainingCard(eligibleCards);
						}
					}
				}
				else {
					//if parter cannot win then either you are forced win or forced loss. Play lowest.
					return determineLowestRemainingCard(eligibleCards);
				}
			}
			else if (this.leaderOfThisTrick == opp1){
				
				//You are last player so it is easy for your play.
				int currentWinner = winnerOfTrickSoFar();
				if (currentWinner == partnerInt){
					//Had to replace with Ace last resort.
					Card card = determineHighestRemainingCardNotAce(eligibleCards);
					if (card!=null){
						return card;
					}
					else {
						return determineHighestRemainingCard(eligibleCards);
					}
				}
				else {
					return determineLowestRemainingCard(eligibleCards);
				}
				
			}
			else if (this.leaderOfThisTrick == player.getPlayerId()){
				
				//List<Boolean> bidWinnerMayHaveThisSuit = new ArrayList<Boolean>();
				for (int suit: allSuits){
					if(this.publicKnowledge.getKnowledgeAboutPlayers().get(this.bidWinner -1).getPossiblyHasCardsOfSuit(suit)){
						if (suit != commonFuncs.getSuitInt(this.trump)){
							if (getCardsOfSuit(eligibleCards,suit).size() > 0){
								
								//Had to replace with Ace last resort.
								Card card = determineHighestRemainingCardNotAce(getCardsOfSuit(eligibleCards, suit));
								if (card!=null){
									return card;
								}
								else {
									return determineHighestRemainingCard(getCardsOfSuit(eligibleCards, suit));
								}
								//return determineHighestRemainingCard(getCardsOfSuit(eligibleCards, suit));
							}
						}
					}
				}
				//If here then no great play. Save cards to protect against future point gains.
				return determineLowestRemainingCard(eligibleCards);
				
				
			}
			else {
				//Opp3 is trick leader
				//Analyze if chance of partner winning. 
				
				Boolean playerMightHaveSuit = this.publicKnowledge.getKnowledgeAboutPlayers().get(partnerInt -1).getPossiblyHasCardsOfSuit(this.suitLed);
				Boolean playerMightHaveTrump = this.publicKnowledge.getKnowledgeAboutPlayers().get(partnerInt -1).getPossiblyHasCardsOfSuit(commonFuncs.getSuitInt(this.trump));
				if (playerMightHaveSuit){
					Card highestPossibleCardOfSuit = this.publicKnowledge.getKnowledgeAboutPlayers().get(partnerInt -1).getHighestPossibleCardOfSuit(this.suitLed);
					//Note can use opp 3 as we've established they were the one to win the trick.
					if (highestPossibleCardOfSuit.getRankOfCard() > this.cardsInTrick.get(opp3 -1).getRankOfCard()){
						//May the cards be with you.
						//Had to replace with Ace last resort.
						Card card = determineHighestRemainingCardNotAce(eligibleCards);
						if (card!=null){
							return card;
						}
						else {
							return determineHighestRemainingCard(eligibleCards);
						}
					}
					else {
						//If they have the suit but it won't win against already led (e.g. meld) then throw a card.
						return determineLowestRemainingCard(eligibleCards);
					}
				} 
				else if (playerMightHaveTrump){
					//may the trump be with you.
					//Had to replace with Ace last resort.
					Card card = determineHighestRemainingCardNotAce(eligibleCards);
					if (card!=null){
						return card;
					}
					else {
						return determineHighestRemainingCard(eligibleCards);
					}
				}
				else {
					//partner cannot win. If you can win it is forced win.
					return determineLowestRemainingCard(eligibleCards);
				}
				
			}
			
			/*int potentialWinner = winnerOfTrickSoFar();
			if ((potentialWinner % numberOfPlayers == player.getPlayerId() % numberOfPlayers)){
				Card card = determineLowestPointEarner(eligibleCards);
				if (card!=null){
					return card;
				}
				//If no cards eligible are pointers to throw on the trick then just throw a card this trick doesn't matter.
				else {
					return determineLowestRemainingCardPeriod(eligibleCards);
				}
			}
			//Eligible cards will determine if the trick can be won or not.
			else {
				//It looks like the winner will more than likely NOT be the team. Then throw the lowest possible eligible card.
				
				return determineLowestRemainingCardPeriod(eligibleCards);
			}*/
			
			
			//AI Stupid
			//return eligibleCards.get(0);
		}
		else {
			//Problem occurred don't return a card.
			Log.d("DECK", "ERROR A");
			return null;
		}
		
		
		
		
	}
	
	
	private int awesomeSuitDetection(List<Card> eligibleCards, Player player, Card cardToCheck){
		//Now that bid winner is extracted analyze the lowest card of each suit you have and see if partner is only winner.
		//Card lowestCardOfSuit ;
		int awesomeSuit = 0;
		for (int suit: allSuits){
			/*lowestCardOfSuit = determineLowestRemainingCardSuit(eligibleCards,suitLed);
			if (lowestCardOfSuit == null){
				//This means you have no valid play for this suit.
				//Doesn't matter if bid winner would be only winner.
				isBidWinnerOnlyWinnerAndICanLeadIt.add(Boolean.FALSE);
				continue;
			}
			else {*/
				List<Boolean> possibleWinners = this.publicKnowledge.possiblePlayersWithCardsThatMightTakeTrickIfThisLed(cardToCheck, commonFuncs.getSuitInt(this.trump), this.publicKnowledge.getKnowledgeAboutDeck().cardsRemainingOfSuit(suit).size() - getCardsOfSuit(this.players.get(player.getPlayerId() -1).getCardsInHand(),commonFuncs.getSuitInt(this.trump)).size());
				//Ignore yourself.
				possibleWinners.set(player.getPlayerId() -1, Boolean.FALSE);
				
				for (int playerPossibility=1; playerPossibility<numberOfPlayers+1; playerPossibility++ ){
					if (playerPossibility == player.getPlayerId()){
						continue; //This is the dummy.
					}
					else if(playerPossibility == this.bidWinner) {
						continue;//We are only checking for opponents victory chances.
					}
					else {
						if (possibleWinners.get(playerPossibility -1)){
							awesomeSuit = 0;
							break;
						}
						else {
							awesomeSuit = suit;
						}
						
					}
				}
				if (awesomeSuit !=0){
					return awesomeSuit;
				}
			//}
		}
		return awesomeSuit;
	}
	
	/*private Card ifAppropriatePointer(List<Card> eligibleCards) {
		int potentialWinner = winnerOfTrickSoFar();
		if (potentialWinner == this.bidWinner){
			Card bestCard = determineLowestPointEarner(eligibleCards);
			if (bestCard != null){
				return bestCard;
			}
			
			// if can't play pointer throw away. No plays then matter in the scheme of things since if you win your goal is to lose again.
			else {
				return determineLowestRemainingCardPeriod(eligibleCards);
			}
		}
		else {
			//if partner won't win trick throw away.
			return determineLowestRemainingCardPeriod(eligibleCards);
		}
	}*/


	private int winnerOfTrickSoFar(){
		Card winningCardSoFar = determineWinnerOfTwoCards(determineWinnerOfTwoCards(determineWinnerOfTwoCards(this.cardsInTrick.get(this.leaderOfThisTrick-1), this.cardsInTrick.get(this.leaderOfThisTrick% numberOfPlayers), suitLed), this.cardsInTrick.get((this.leaderOfThisTrick+1) % numberOfPlayers), suitLed), this.cardsInTrick.get((this.leaderOfThisTrick+2) % numberOfPlayers), suitLed);
		for (Integer playerNumber=this.leaderOfThisTrick; playerNumber < numberOfPlayers+1; playerNumber++){
			
			if (this.cardsInTrick.get(playerNumber-1) == winningCardSoFar){

				return playerNumber;
			}
			else if(playerNumber == numberOfPlayers){
				playerNumber = 0;//This will restart the loop.
			}
		}
		
		return (Integer) null;
	}
	
	
	
	private Card loopAndGiveMeSureNonTrumpWin(int playerId, List<Card> cardsInHand) {
		
		List<Card> distinctNonTrumpCardsInHand = getDistinctNonTrumpCardsInHand(cardsInHand);
		int trumpDifference = this.publicKnowledge.getKnowledgeAboutDeck().cardsRemainingOfSuit(commonFuncs.getSuitInt(this.trump)).size() - getCardsOfSuit(cardsInHand, commonFuncs.getSuitInt(this.trump)).size();
	
		if (distinctNonTrumpCardsInHand.size() == 0){
			return null;
		}
		

			for (Card thisCard :distinctNonTrumpCardsInHand){
				if (thisCard != null){
					List<Boolean> playersThatMightBeatThatCard = this.publicKnowledge.possiblePlayersWithCardsThatMightTakeTrickIfThisLed(thisCard,commonFuncs.getSuitInt(this.trump), trumpDifference);
					//Set yourself to false;
					playersThatMightBeatThatCard.set(playerId -1, Boolean.FALSE);
					Boolean aPlayerMightTakeTrick = Boolean.FALSE;
					for(boolean theyCould: playersThatMightBeatThatCard){
						  if(theyCould){ aPlayerMightTakeTrick = Boolean.TRUE;}
					}
					if (!aPlayerMightTakeTrick){
						return thisCard;
					}
				}
			}
			
		
		return null;
	}


	private List<Card> getDistinctNonTrumpCardsInHand(List<Card> cardsInHand) {
		List<Card> distinctNonTrumpCardsInHand = new ArrayList<Card>();
		for (Card card : cardsInHand){
			if (countNumberEqual((ArrayList<Card>)distinctNonTrumpCardsInHand, card) == 0){
				if (card.getCardSuit() != commonFuncs.getSuitInt(this.trump)){
					distinctNonTrumpCardsInHand.add(card);
				}
			}
		}
		return distinctNonTrumpCardsInHand;
	}


	private Card determineLowestPointEarnerInTrump(List<Card> cardsInHand) {
	
		int countOfWinner =0;
		for (int rank=king; rank< ace+1; rank++){
			countOfWinner = countNumberEqual((ArrayList<Card>)cardsInHand, new Card(rank, commonFuncs.getSuitInt(this.trump)));
			if (countOfWinner > 0){
				return new Card(rank, commonFuncs.getSuitInt(this.trump));
			}
		}
		return null;
	}
	
	private Card determineLowestPointEarnerInSuit(List<Card> cardsInHand, int suit) {
		
		int countOfWinner =0;
		for (int rank=king; rank< ace+1; rank++){
			countOfWinner = countNumberEqual((ArrayList<Card>)cardsInHand, new Card(rank, suit));
			if (countOfWinner > 0){
				return new Card(rank, suit);
			}
		}
		return null;
	}
	
	
	
	private Card determineHighestLoserInTrump(List<Card> cardsInHand) {

		int countOfLoser =0;
		for (int rank=queen; rank>nine-1; rank--){
			countOfLoser = countNumberEqual((ArrayList<Card>)cardsInHand, new Card(rank, commonFuncs.getSuitInt(this.trump)));
			if (countOfLoser > 0){
				return new Card(rank, commonFuncs.getSuitInt(this.trump));
			}
		}
		return null;
	}

	
	

	private Card determineHighestRemainingNonTrump(List<Card> cardsInHand) {
		
		for (int rank=ace; rank > nine-1; rank--){
			
			for (int suit: allSuits){
				if (suit != commonFuncs.getSuitInt(this.trump)){
					if (countNumberEqual((ArrayList<Card>)cardsInHand,new Card(rank,suit)) > 0){
						return new Card(rank,suit);
					}
				}
			}
		}
		
		//If still null has no nontrump cards.
		return null;
	}
	
	
	
	
	private Card determineHighestRemainingTrump(List<Card> cardsInHand) {
		
		for (int rank=ace; rank > nine-1; rank--){
			
					if (countNumberEqual((ArrayList<Card>)cardsInHand,new Card(rank,commonFuncs.getSuitInt(this.trump))) > 0){
						return new Card(rank,commonFuncs.getSuitInt(this.trump));
					}
			
		}
		
		//If still null problems occurred as player has no cards.
		return null;
	}

	/*private Card determineLowestRemainingTrump(List<Card> cardsInHand) {
		
		for (int rank=nine; rank < ace+1; rank++){
			
					if (countNumberEqual((ArrayList<Card>)cardsInHand,new Card(rank,commonFuncs.getSuitInt(this.trump))) > 0){
						return new Card(rank,commonFuncs.getSuitInt(this.trump));
					}
			
		}
		
	
		return null;
	}*/


	/*private Card determineLowestRemainingCardNotTrump(List<Card> cardsInHand) {
		
		for (int rank=nine; rank < ace+1; rank++){
			for (int suit : allSuits){
				if (suit != commonFuncs.getSuitInt(this.trump)){
					if (countNumberEqual((ArrayList<Card>)cardsInHand,new Card(rank,suit)) > 0){
						return new Card(rank,suit);
					}
				}
			}
		}
		
		//we have gone through every card.
		return null;
	}*/
	
	
	
	private Card determineHighestRemainingCardNotAce(List<Card> cardsInHand) {
		
		for (int rank=ten; rank > nine-1; rank--){
			for (int suit : allSuits){
				
					if (countNumberEqual((ArrayList<Card>)cardsInHand,new Card(rank,suit)) > 0){
						return new Card(rank,suit);
					}
				
			}
		}
		
		//we have gone through every card.
		return null;
	}
	
	private Card determineHighestRemainingCard(List<Card> cardsInHand) {
		
		for (int rank=ace; rank > nine-1; rank--){
			for (int suit : allSuits){
				
					if (countNumberEqual((ArrayList<Card>)cardsInHand,new Card(rank,suit)) > 0){
						return new Card(rank,suit);
					}
				
			}
		}
		
		//we have gone through every card.
		return null;
	}
	
	private Card determineLowestRemainingCard(List<Card> cardsInHand) {
		
		for (int rank=nine; rank < ace+1; rank++){
			for (int suit : allSuits){
				
					if (countNumberEqual((ArrayList<Card>)cardsInHand,new Card(rank,suit)) > 0){
						return new Card(rank,suit);
					}
				
			}
		}
		
		//we have gone through every card.
		return null;
	}
	
	private Card determineLowestRemainingCardSuit(List<Card> cardsInHand, int suitToFind) {
		
		for (int rank=nine; rank < ace+1; rank++){
			for (int suit : allSuits){
				if (suit == suitToFind){
					if (countNumberEqual((ArrayList<Card>)cardsInHand,new Card(rank,suit)) > 0){
						return new Card(rank,suit);
					}
				}
			}
		}
		
		//we have gone through every card.
		return null;
	}




	


	private String determinePlayerRole(int playerInt){
		if (this.bidWinner == playerInt){
			return winnerRoleString;
		}
		else if ((this.bidWinner % 2) == (playerInt % 2)){
			return dummyRoleString;
		}
		else {
			return otherTeamRoleString;
		}
	}
	

	private void removeACardPictureFromScreen(
			Integer currentPlayerThatNeedsToAct) {
		
		
		if (currentPlayerThatNeedsToAct == 1){
			Log.d("DECK", "should never reach here. If I do there is a problem as 1 is human player.");
		}
		else if (currentPlayerThatNeedsToAct == 2){
			player2HandLayout.removeViewAt(0);
		}
		else if (currentPlayerThatNeedsToAct == 3){
			player3HandLayout.removeViewAt(0);
		}
		else if (currentPlayerThatNeedsToAct == 4){
			player4HandLayout.removeViewAt(0);
		}
	}
	
	private Card getMaxRankCardNotAce(List<Card> cardsOfThatSuitPlayed){
		Card maxCard = null;
		for (Card card : cardsOfThatSuitPlayed){
			if (card.getRankOfCard() == ace){
				continue;
			}
			if (card.getRankOfCard() == ten){
				return card;
			}
			if (maxCard == null){
				maxCard = card;
			}
			else if (card.getRankOfCard() > maxCard.getRankOfCard()){
				maxCard = card;
			}
		}
		return maxCard;
	}
	
	
	
	private Card getMaxRankCard(List<Card> cardsOfThatSuitPlayed){
		Card maxCard = null;
		for (Card card : cardsOfThatSuitPlayed){
			if (card.getRankOfCard() == ace){
				return card;
			}
			if (maxCard == null){
				maxCard = card;
			}
			else if (card.getRankOfCard() > maxCard.getRankOfCard()){
				maxCard = card;
			}
		}
		return maxCard;
	}

	private int getMaxRankOfCards(List<Card> cardsOfThatSuitPlayed) {
	
		int maxRank = 0;
		for (Card card : cardsOfThatSuitPlayed){
			if (card.getRankOfCard() == ace){
				return ace;
			}
			else if (card.getRankOfCard() > maxRank){
				maxRank = card.getRankOfCard();
			}
		}
		return maxRank;
	}


	private void addMelds(int playerId, int determineMeld) {
		
		if (playerId % 2 == 0){
			this.eastWestMeld = this.eastWestMeld + determineMeld;
		}
		else {
			this.northSouthMeld = this.northSouthMeld + determineMeld;
		}
	}

	private void displayScoreOnScreen(){
		
		final Dialog dialog = new Dialog(this, R.style.AppTheme);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.score_display);
		Button dismissalButton = (Button) dialog.findViewById(R.id.score_dismissal);
		dismissalButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				victoryOrNewHand();
				dialog.dismiss();
			}});
		TextView eastWestScoreView = (TextView) dialog.findViewById(R.id.east_west_grid_score);
		TextView northSouthScoreView = (TextView) dialog.findViewById(R.id.north_south_grid_score);
		eastWestScoreView.setText("" + this.eastWestScore);
		northSouthScoreView.setText("" + this.northSouthScore);
		dialog.show();
	}

	private void displayCardsInMeldOnScreen() {
	
		
		final Dialog dialog = new Dialog(this, R.style.AppTheme);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.meld_display);
		dialog.setTitle(readFromStringXML(R.string.meld));
		Button dismissalButton = (Button) dialog.findViewById(R.id.meld_dismissal);
		
		GridLayout cardHolder;
		ImageView picOfCard;
		//TextView meldNumericalOutput;
		
		for (Player player : this.players){
			cardHolder = (GridLayout) dialog.findViewById(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + player.getPlayerId() + "_meld_card_holder", "", getPackageName()));
			
			for (Card card: this.playerMelds.get(player.getPlayerId() -1)){
				picOfCard = new ImageView(this);
			    picOfCard.setImageResource(getResources().getIdentifier(card.getImageFileName(), "", getPackageName()));
			    cardHolder.addView(picOfCard);
			}
			
			//meldNumericalOutput = ((TextView) dialog.findViewById(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + player.getPlayerId() + "_meld_text", "", getPackageName())));
			//String str = "Meld: " + determineMeld(this.meldInPlayerHand, commonFuncs.getSuitInt(this.trump));
			
			//((TextView) dialog.findViewById(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + player.getPlayerId() + "_meld_text", "", getPackageName()))).setText("Meld: " + determineMeld(this.playerMelds.get(player.getPlayerId() -1), commonFuncs.getSuitInt(this.trump)));
			((TextView) dialog.findViewById(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + player.getPlayerId() + readFromStringXML(R.string.underscore_meld_underscore_text), "", getPackageName()))).setText("Meld: " + determineMeld(this.playerMelds.get(player.getPlayerId() -1), commonFuncs.getSuitInt(this.trump)));
		}
		
		
		dismissalButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				meldDismissalClick();
				dialog.dismiss();
			}});

		//View mainBkg = dialog.findViewById(R.id.meld_display_base);
		//mainBkg.setBackgroundColor(Color.WHITE);
		
		
		
		//Now to update the text fields.
		
		TextView winningBid = (TextView) dialog.findViewById(R.id.winning_bid_meld);
		TextView bidPointersNeeded = (TextView) dialog.findViewById(R.id.bid_pointers_needed_meld);
		TextView pointersToSet = (TextView) dialog.findViewById(R.id.pointers_needed_to_set_meld);
		Boolean bidWinnerIsEastWest = (this.bidWinner % 2 == 0);
		//Cannot use constants as they are not defined yet.
		
		
		winningBid.setText(this.currentBid + "" + this.players.get(this.bidWinner -1).getPlayerDesignation() + " " + this.trump);
		if (bidWinnerIsEastWest){
			bidPointersNeeded.setText("" + (this.currentBid - this.eastWestMeld));
			pointersToSet.setText("" + (this.constants.maxPointers - (this.currentBid - this.eastWestMeld) + this.constants.pointerBaseUnit ));
		}
		else {
			bidPointersNeeded.setText("" + (this.currentBid - northSouthMeld));
			pointersToSet.setText("" + (this.constants.maxPointers - (this.currentBid - this.northSouthMeld) + this.constants.pointerBaseUnit));
		}
		
		dialog.show();
	 
	
		
	}


	private void meldDismissalClick(){
		passOrPlay();
	}

	private List<Card> determineCardsUsedInMeld(List<Card> cardsInHand) {
		//Check trump first for meld.
				List<Card> cardsUsedInMeld = new ArrayList<Card>();
				int trumpInt = commonFuncs.getSuitInt(this.trump);
				List<Card> cardsOfThatSuit = getCardsOfSuit(cardsInHand, trumpInt);
				//int meld = 0;
				int acesCount = 0;
				int tensCount = 0;
				int kingsCount = 0;
				int queensCount = 0;
				int jacksCount = 0;
				int ninesCount = 0;
				boolean runExists = false;
				boolean doubleRunExists = false;
				for (Card card: cardsOfThatSuit){
					if (card.getRankOfCard()==ace){
						acesCount++;
					}
					else if (card.getRankOfCard()==ten){
						tensCount++;
					}
					else if (card.getRankOfCard()==king){
						kingsCount++;
					}
					else if (card.getRankOfCard()==queen){
						queensCount++;
					}
					else if (card.getRankOfCard()==jack){
						jacksCount++;
					}
					else if (card.getRankOfCard()==nine){
						ninesCount++;
					}
				}
				
				
				if ( acesCount > 0 && tensCount > 0 && kingsCount > 0 && queensCount > 0 && jacksCount > 0){
					//Run exists
					//meld = meld+runOfTrump;
					cardsUsedInMeld.add(new Card(ace, trumpInt));
					cardsUsedInMeld.add(new Card(ten, trumpInt));
					cardsUsedInMeld.add(new Card(king, trumpInt));
					cardsUsedInMeld.add(new Card(queen, trumpInt));
					cardsUsedInMeld.add(new Card(jack, trumpInt));
					runExists = true;
					if ( acesCount > 1 && tensCount > 1 && kingsCount > 1 && queensCount > 1 && jacksCount > 1){
						//meld = meld+doubleRunOfTrump-runOfTrump;//Double run exists;//Reason to add 135 is we already added 15 for first run.
						doubleRunExists = true;
						cardsUsedInMeld.add(new Card(ace, trumpInt));
						cardsUsedInMeld.add(new Card(ten, trumpInt));
						cardsUsedInMeld.add(new Card(king, trumpInt));
						cardsUsedInMeld.add(new Card(queen, trumpInt));
						cardsUsedInMeld.add(new Card(jack, trumpInt));
					}
				}
				
				
				if (!doubleRunExists){
					if(!runExists){
						if (kingsCount > 0 && queensCount > 0){
							if (kingsCount == 2 && queensCount == 2){
								//meld = meld + 8;
								cardsUsedInMeld.add(new Card(king, trumpInt));
								cardsUsedInMeld.add(new Card(queen, trumpInt));
								cardsUsedInMeld.add(new Card(king, trumpInt));
								cardsUsedInMeld.add(new Card(queen, trumpInt));
							}
							else {
								//meld = meld + 4;
								cardsUsedInMeld.add(new Card(king, trumpInt));
								cardsUsedInMeld.add(new Card(queen, trumpInt));
							}
						}
					}
					else {
						if (kingsCount == 2 && queensCount == 2){
							//meld = meld + 4;
							cardsUsedInMeld.add(new Card(king, trumpInt));
							cardsUsedInMeld.add(new Card(queen, trumpInt));
						}
					}
				}
				
				//meld = meld + ninesCount;
				for (int nineCard = 0; nineCard < ninesCount; nineCard++){
					cardsUsedInMeld.add(new Card(nine, trumpInt));
				}
				
				//Now for X around;
				List<Integer> possibleXAround = new ArrayList<Integer>();
				possibleXAround.add(ace);
				possibleXAround.add(king);
				possibleXAround.add(queen);
				possibleXAround.add(jack);
				Boolean xAroundStillPossible = true;
				Boolean xDoubleAroundStillPossible = true;
				
				for (Integer rankToCheckForXAround : possibleXAround){
					for (int suit: allSuits){
						int numberOfXCard = countNumberEqual((ArrayList<Card>) cardsInHand, new Card(rankToCheckForXAround, suit));
						if (xAroundStillPossible){
							if ( numberOfXCard == 1){
								//SingleAround possible only now;
								xDoubleAroundStillPossible = false;
							}
							else {
								if (numberOfXCard == 0){
									xAroundStillPossible = false;
								}
							}
							
						}
						if (xDoubleAroundStillPossible){
							if (numberOfXCard == 2){
								//doubleXaround still possible
								//Do not exclude single around as a player may have 2 ace of diamonds but one ace of spades.
							}
							else{
								xDoubleAroundStillPossible=false;
							}
						}
					}
					//If true here doubleAround exists;
					int numberOfXInMeldAlready = 0;
					if (xDoubleAroundStillPossible){
						//if (rankToCheckForXAround == jack){
							//meld = meld + jacksDoubleAround;
							for (int suit: allSuits){
								
									
									numberOfXInMeldAlready = countNumberEqual((ArrayList<Card>) cardsUsedInMeld,new Card(rankToCheckForXAround,suit));
									for (int XToAdd=2-numberOfXInMeldAlready;XToAdd>0;XToAdd--){
										cardsUsedInMeld.add(new Card(rankToCheckForXAround,suit));
									}
									
								
							}
							xAroundStillPossible = false;
							
					
					}
					//If true here xAround exists;
					if (xAroundStillPossible){
						
						
						for (int suit: allSuits){
							numberOfXInMeldAlready = countNumberEqual((ArrayList<Card>) cardsUsedInMeld,new Card(rankToCheckForXAround,suit));
							for (int XToAdd=1-numberOfXInMeldAlready;XToAdd>0;XToAdd--){
								cardsUsedInMeld.add(new Card(rankToCheckForXAround,suit));
							}
						}
					}
					
					//Set for next rank;
					xAroundStillPossible=true;
					xDoubleAroundStillPossible=true;
				}
				
				//Now check for Pinochle and double pinochle
				int queenOfSpadesCount = countNumberEqual((ArrayList<Card>) cardsInHand, new Card(queen, spadesInt));
				int jackOfDiamondsCount = countNumberEqual((ArrayList<Card>) cardsInHand, new Card(jack, diamondsInt));
				int numberOfXInMeldAlready = 0;
				if (queenOfSpadesCount > 0 && jackOfDiamondsCount > 0){
					if (queenOfSpadesCount > 1 && jackOfDiamondsCount > 1){
						numberOfXInMeldAlready = countNumberEqual((ArrayList<Card>) cardsUsedInMeld,new Card(queen,spadesInt));
						for (int XToAdd=2-numberOfXInMeldAlready;XToAdd > 0;XToAdd--){
							cardsUsedInMeld.add(new Card(queen,spadesInt));
						}
						numberOfXInMeldAlready = countNumberEqual((ArrayList<Card>) cardsUsedInMeld,new Card(jack,diamondsInt));
						for (int XToAdd=2-numberOfXInMeldAlready;XToAdd > 0;XToAdd--){
							cardsUsedInMeld.add(new Card(jack,diamondsInt));
						}
					}
					else {
						//meld = meld + singlePinochle;
						numberOfXInMeldAlready = countNumberEqual((ArrayList<Card>) cardsUsedInMeld,new Card(queen,spadesInt));
						for (int XToAdd=1-numberOfXInMeldAlready;XToAdd > 0;XToAdd--){
							cardsUsedInMeld.add(new Card(queen,spadesInt));
						}
						numberOfXInMeldAlready = countNumberEqual((ArrayList<Card>) cardsUsedInMeld,new Card(jack,diamondsInt));
						for (int XToAdd=1-numberOfXInMeldAlready;XToAdd > 0;XToAdd--){
							cardsUsedInMeld.add(new Card(jack,diamondsInt));
						}
					}
				}
				
				
				//Check opposite sex marriages for meld;
				// :( I wish same sex marriages counted for meld;
				//Maybe marriage equality will come for Pinochle one day.
				//Go HRC! (Seriously love the HRC no sarcasm there. One day may they make marriage legal in Arkansas. )
				
				int KingsOffSuit = 0;
				int QueensOffSuit = 0;
				for (int suit: allSuits){
					//Skip if suit is trump. Was counted before do not want to double count.
					if (suit != trumpInt){
						KingsOffSuit = countNumberEqual((ArrayList<Card>) cardsInHand, new Card(king, suit));
						QueensOffSuit = countNumberEqual((ArrayList<Card>) cardsInHand, new Card(queen, suit));
						//Log.d("DECK", "SUIT:" + commonFuncs.getSuitString(suit) + " Kings: " + KingsOffSuit + " Queens: " + QueensOffSuit);
						if (KingsOffSuit > 0 && QueensOffSuit > 0){
							if (KingsOffSuit > 1 && QueensOffSuit > 1){
								//meld = meld + 4;
								numberOfXInMeldAlready = countNumberEqual((ArrayList<Card>) cardsUsedInMeld,new Card(king,suit));
								for (int XToAdd=2-numberOfXInMeldAlready;XToAdd>0;XToAdd--){
									cardsUsedInMeld.add(new Card(king,suit));
								}
								numberOfXInMeldAlready = countNumberEqual((ArrayList<Card>) cardsUsedInMeld,new Card(queen,suit));
								for (int XToAdd=2-numberOfXInMeldAlready;XToAdd>0;XToAdd--){
									cardsUsedInMeld.add(new Card(queen,suit));
								}
							}
							else {
								//meld = meld + 2;
								numberOfXInMeldAlready = countNumberEqual((ArrayList<Card>) cardsUsedInMeld,new Card(king,suit));
								for (int XToAdd=1-numberOfXInMeldAlready;XToAdd>0;XToAdd--){
									cardsUsedInMeld.add(new Card(king,suit));
								}
								numberOfXInMeldAlready = countNumberEqual((ArrayList<Card>) cardsUsedInMeld,new Card(queen,suit));
								for (int XToAdd=1-numberOfXInMeldAlready;XToAdd>0;XToAdd--){
									cardsUsedInMeld.add(new Card(queen,suit));
								}
							}
						}
					}
					
				}
				
				
				
				
				//Done checking all kinds of meld.
				return cardsUsedInMeld;
	}


	private void displayPromptForHumanToChooseCardsToPass() {
		
		
		if (this.humanIsBidderOrDummy != null){
			refreshCardOnClickListeners();
			if (this.humanIsBidderOrDummy){
				resetCardPassedImages();
				Button donePassingButton = (Button) findViewById(R.id.done_passing);
				donePassingButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						attemptToPassCards(v);
					}
					
				});
			}
			else {
				resetCardPassedImagesOpponentStyle();
				Button donePassingButton = (Button) findViewById(R.id.done_passing);
				donePassingButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						attemptToPassCardsOpponentStyle(v);
					}
					
				});
			}
		}
		hideOrShowFrame(R.id.passing_card_prompt, View.VISIBLE);
		for (int playerId = 1; playerId < numberOfPlayers+1; playerId++){
			//hideOrShowFrame(getResources().getIdentifier("@id/player" + playerId + "CardPlayed" , "", getPackageName()),View.INVISIBLE);
			hideOrShowFrame(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + playerId + readFromStringXML(R.string.cardplayed) , "", getPackageName()),View.INVISIBLE);
			if (playerId != humanPlayer){
				this.popupViews.get(playerId-1).setVisibility(View.INVISIBLE);
				//hideOrShowFrame(getResources().getIdentifier("@id/player" + playerId + "_grid" , "", getPackageName()), View.INVISIBLE);
				hideOrShowFrame(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + playerId + readFromStringXML(R.string.underscore_grid) , "", getPackageName()), View.INVISIBLE);
			}
		}
		
		
	}
	
	private void refreshCardOnClickListeners() {
		Collections.sort(this.players.get(humanPlayer -1).getCardsInHand(),commonFuncs.cardSorter);
		GridLayout handLayout =(GridLayout) findViewById(R.id.hand_grid);
		handLayout.removeAllViews();
		ImageView newCardButton = new ImageView(this);
		int imageId=0;
		
	    for (Card cardInHand : this.players.get(humanPlayer -1).getCardsInHand()){
	    	
	    	newCardButton = new ImageView(this);
			imageId = getResources().getIdentifier(cardInHand.getImageFileName(), "", getPackageName());
		    newCardButton.setImageResource(imageId);
		    newCardButton.setOnClickListener(getOnClickListener(cardInHand));
		    handLayout.addView(newCardButton);
		
	    	
	    }
		
	}


	private void displayPromptForPlayerToPlayCard(){
		
		hideOrShowFrame(R.id.playing_card_prompt, View.VISIBLE);
		
	}
	
	private void hidePromptForPlayerToPlayCard(){
		hideOrShowFrame(R.id.playing_card_prompt, View.INVISIBLE);
	}

	private List<Card> determineOppCardsToPass(int playerPassing){
		//TODO If setting turned back on, add intelligence here.
		List<Card> cardsToPass = new ArrayList<Card>();
		if (this.constants.cardsPassBetweenOpponentsToBid > 0){
			
			//This for loop will be replaced by the intelligence of the pass.
			for (Card card : this.players.get(playerPassing -1).getCardsInHand()){
				cardsToPass.add(card);
				if (cardsToPass.size() == this.constants.cardsPassBetweenOpponentsToBid){
					break;
				}
			}
			
			//Set the cards to be passed to be null.
			List<Integer> locationsOfCardsToBePassed = new ArrayList<Integer>();
			for (Card card: cardsToPass){
				int loc = getCardLocationEqual((ArrayList<Card>)this.players.get(playerPassing -1).getCardsInHand(), card).get(0);
				locationsOfCardsToBePassed.add(loc);
				this.players.get(playerPassing -1).getCardsInHand().set(loc, null);
			}
			
			//Remove the cards set to null as they have been passed to the opposing player.
			this.players.get(playerPassing -1).getCardsInHand().removeAll(Collections.singleton(null)); 
			return cardsToPass;
		}
		else {
			return cardsToPass;
		}
	}

	private List<Card> determineBidWinnerPassCardsToDummy(int bidWinner) {
		
		List<Card> cardsToPass = new ArrayList<Card>();
		List<Card> bidWinnerHand = this.players.get(bidWinner -1).getCardsInHand();
		List<Integer> locationsOfCardsToPass = new ArrayList<Integer>();
		//Note Ace is missing. Would rather pass back trump to partner here because you will use those aces for winners if it comes down to it.
		Integer[] offSuitPassingPriorities = {king,ten, nine, jack, queen};
		List<Integer> offSuitPrioritizationOfPassing = Arrays.asList(offSuitPassingPriorities);
		Boolean onePinochleExists = false;
		Boolean twoPinochlesExist = false;
		int queenOfSpadesCount = countNumberEqual((ArrayList<Card>) bidWinnerHand, new Card(queen, spadesInt));
		int jackOfDiamondsCount = countNumberEqual((ArrayList<Card>) bidWinnerHand, new Card(jack, diamondsInt));
		int queenOfSpadesPassed = 0;
		int jackOfDiamondsPassed = 0;
		
		if (queenOfSpadesCount>0 && jackOfDiamondsCount > 0){
			if (queenOfSpadesCount >1 && jackOfDiamondsCount > 1){
				twoPinochlesExist = true;
			}
			else {
				onePinochleExists = true;
			}
		}
		
		//Pass cards for pinochle or double pinochle (unless it is trump).
		if (this.trump.equals(clubs) || this.trump.equals(hearts)){
			List<Integer> queenOfSpadesLocations = getCardLocationEqual((ArrayList<Card>) bidWinnerHand, new Card(queen, spadesInt));
			if (queenOfSpadesLocations.size() > 0){
				locationsOfCardsToPass.addAll(queenOfSpadesLocations);
			}
			List<Integer> jackOfDiamondsLocations = getCardLocationEqual((ArrayList<Card>) bidWinnerHand, new Card(jack, diamondsInt));
			if (jackOfDiamondsLocations.size() > 0){
				locationsOfCardsToPass.addAll(jackOfDiamondsLocations);
			}
		}
		Log.d("DECK", "After QS JD:" + locationsOfCardsToPass.toString());
		//Now pass tens and kings of off suit. They may or may not be "winners" but are more than likely losers.
		//The strategy is to give them to the dummy to "slop" rather than hope they take a trick.
		List<Integer> rankXOffSuit = new ArrayList<Integer>();
		for (int rank: offSuitPrioritizationOfPassing){
			for (int suit: allSuits){
				
				if (suit != commonFuncs.getSuitInt(this.trump)){
					
					Log.d("DECK", "TRUMP IS: " + this.trump);
					//If it is NOT the queen of spades with trump = diamonds or jack of diamonds with trump = spades then
					//If not queen of spades and not jack of diamonds okay to pass.
					if ((!(this.trump.equals(diamonds) && rank==queen && suit == spadesInt)) && (!(this.trump.equals(diamonds) && rank==jack && suit == diamondsInt))){
					
						
						rankXOffSuit = getCardLocationEqual((ArrayList<Card>)bidWinnerHand, new Card(rank,suit));
						for (Integer location : rankXOffSuit)
							if (!(locationsOfCardsToPass.contains(location))){
								locationsOfCardsToPass.add(location);
								
							}
						rankXOffSuit.clear();
						
					} 
					//If trump is clubs or hearts already passed them.
					else if (!(this.trump.equals(clubs) || this.trump.equals(hearts))){
						   if (!onePinochleExists && !twoPinochlesExist){

								rankXOffSuit = getCardLocationEqual((ArrayList<Card>)bidWinnerHand, new Card(rank,suit));
								for (Integer location : rankXOffSuit)
									if (!(locationsOfCardsToPass.contains(location))){
										locationsOfCardsToPass.add(location);
										
									}
								rankXOffSuit.clear();
						   }
						   else if (onePinochleExists){
							   
							   //If this is true then there is one "left over" queen of spades or jack of diamonds.
							   if ((queenOfSpadesCount - jackOfDiamondsCount) != 0){
								   //This means the queen is the left over. Check to see if we have added it to the passed list.
								   if ((queenOfSpadesCount > jackOfDiamondsCount) && (queenOfSpadesPassed == 0) && rank == queen && suit == spadesInt){
									   rankXOffSuit = getCardLocationEqual((ArrayList<Card>)bidWinnerHand, new Card(rank,suit));
									   for (Integer location : rankXOffSuit)
											if (!(locationsOfCardsToPass.contains(location))){
												locationsOfCardsToPass.add(location);
												
											}
										rankXOffSuit.clear();
									   queenOfSpadesPassed = 1;
								   }
								   //This means the jack is the left over. Check to see if we have added it to the passed list.
								   else if ((jackOfDiamondsCount > queenOfSpadesCount) && (jackOfDiamondsPassed == 0) && rank == jack && suit == diamondsInt){
									   rankXOffSuit = getCardLocationEqual((ArrayList<Card>)bidWinnerHand, new Card(rank,suit));
									   for (Integer location : rankXOffSuit)
											if (!(locationsOfCardsToPass.contains(location))){
												locationsOfCardsToPass.add(location);
												
											}
										rankXOffSuit.clear();
									   jackOfDiamondsPassed = 1;
								   }
							   }
							   
						   }
						   else if (twoPinochlesExist){
							   //Skip the Queen and Jack as they are EVERYTHING that is being passed already.
						   }
					}
					
					
				}
				
			}
		}
		Log.d("DECK", "After QS JD and non trump:" + locationsOfCardsToPass.toString());
		
		//By this point we've prioritized everything but trump.
		List<Integer> trumpXLocations = new ArrayList<Integer>();
		for (int rank = nine; rank < ace+1; rank++){
			
			//Jack of diamonds and Queen of Spades should never be passed if trump. 
			//Neither really should the ace either just building priority listing.
			if (!((this.trump.equals(spades) && rank == queen) || (this.trump.equals(diamonds) && rank == jack))){
				trumpXLocations = getCardLocationEqual((ArrayList<Card>) bidWinnerHand, new Card(rank,commonFuncs.getSuitInt(this.trump)));
				locationsOfCardsToPass.addAll(trumpXLocations);
				trumpXLocations.clear();
			}
			
		}
		
		Log.d("DECK", "after all locations of cards to pass:" + locationsOfCardsToPass.toString());
		
		Log.d("DECK", "Bid Winner Hand Pre null:" + bidWinnerHand.toString());
		//Now to set the cards to pass and to set the ones passed to null.
		for (int cardLoc = 0; cardLoc < this.constants.cardsPassBetweenDummyAndBidder; cardLoc++){
			try {
			cardsToPass.add(bidWinnerHand.get(locationsOfCardsToPass.get(cardLoc)));
			}
			catch (Exception e){
				Log.d("DECK", "Bad stuff happened in card pass:");
				Log.d("DECK", "Cards to pass currently set: " + cardsToPass.toString());
				Log.d("DECK", "Locations of cards to pass: " + locationsOfCardsToPass.toString());
				Log.d("DECK", "Bid Winner Hand:" + bidWinnerHand.toString());
				
				//Line added so exception still thrown.
				cardsToPass.add(bidWinnerHand.get(locationsOfCardsToPass.get(cardLoc)));
			}
			bidWinnerHand.set(locationsOfCardsToPass.get(cardLoc), null);
		}
		Log.d("DECK", "Bid Winner Hand:" + bidWinnerHand.toString());
		Log.d("DECK", "cardsToPass:" + cardsToPass.toString());
		for (Card card: cardsToPass){
			Log.d("DECK", "Bid Winner Player " + bidWinner + " to pass card " + card.getCardName());
		}
		
		//Remove the cards set to null as they have been passed to the bidding player.
		bidWinnerHand.removeAll(Collections.singleton(null)); 
		
		
		return cardsToPass;
		
	}


	private void passCards(int recipient, List<Card> cardsReceived) {
		
		this.players.get(recipient -1).getCardsInHand().addAll(cardsReceived);
		
		
	}

	

	private List<Card> determineDummyPassCardsToBidWinner(int dummy) {
		
		List<Card> cardsToPass = new ArrayList<Card>();
		List<Card> dummyHand = this.players.get(dummy -1).getCardsInHand();
		List<Integer> locationsOfCardsToPass = new ArrayList<Integer>();
		
		if (this.trump.equals(spades) || this.trump.equals(diamonds)){
		List<Integer> queenOfSpadesLocations = getCardLocationEqual((ArrayList<Card>) dummyHand, new Card(queen, spadesInt));
			if (queenOfSpadesLocations.size() > 0){
				locationsOfCardsToPass.addAll(queenOfSpadesLocations);
			}
			List<Integer> jackOfDiamondsLocations = getCardLocationEqual((ArrayList<Card>) dummyHand, new Card(jack, diamondsInt));
			if (jackOfDiamondsLocations.size() > 0){
				locationsOfCardsToPass.addAll(jackOfDiamondsLocations);
			}
		}
		List<Integer> trumpXLocations = new ArrayList<Integer>();
		for (int rank = ace; rank > nine-1; rank--){
			//Only get the locs if not already done above.
			if (!(((this.trump == spades) && rank == queen)  || ((this.trump==diamonds) && rank ==jack))){
				trumpXLocations = getCardLocationEqual((ArrayList<Card>) dummyHand, new Card(rank, commonFuncs.getSuitInt(this.trump)));
				if (trumpXLocations.size() > 0){
					locationsOfCardsToPass.addAll(trumpXLocations);
					trumpXLocations.clear();
				}
			}
		}
		
		
		
		List<Integer> rankXOffSuit = new ArrayList<Integer>();
		for (int rank=ace; rank > nine-1; rank--){
			
			for (int suit: allSuits){
				
				if (suit != commonFuncs.getSuitInt(this.trump)){
					
					//have to skip the queen of spades if trump is diamonds and the jack of diamonds if trump is spades.
					if ((!(this.trump.equals(diamonds) && rank==queen && suit == spadesInt)) && (!(this.trump.equals(spades) && rank==jack && suit == diamondsInt))){
					
						rankXOffSuit = getCardLocationEqual((ArrayList<Card>)dummyHand, new Card(rank,suit));
						locationsOfCardsToPass.addAll(rankXOffSuit);
						rankXOffSuit.clear();
						
					}
						
				}
				
				
			}
			
		}
		
		//Now we have sorted all of the cards in the order to pass them.
		//Now to put them in the cardsToPass list.
		
		for (int cardLoc = 0; cardLoc < this.constants.cardsPassBetweenDummyAndBidder; cardLoc++){
			cardsToPass.add(dummyHand.get(locationsOfCardsToPass.get(cardLoc)));
			dummyHand.set(locationsOfCardsToPass.get(cardLoc), null);
		}
		
		for (Card card: cardsToPass){
			Log.d("DECK", "Dummy Player " + dummy + " to pass card " + card.getCardName());
		}
		
		//Remove the cards set to null as they have been passed to the bidding player.
		dummyHand.removeAll(Collections.singleton(null)); 
		return cardsToPass;
	}


	private String generateTrumpSuit(int playerWhoWonBid) {
		
		List<Card> cardHand = this.players.get(playerWhoWonBid -1).getCardsInHand();
		int spadeStrength = getCardStrengthOfSuit(cardHand,spadesInt);
		int heartsStrength = getCardStrengthOfSuit(cardHand,heartsInt);
		int diamondStrength = getCardStrengthOfSuit(cardHand,diamondsInt);
		int clubStrength = getCardStrengthOfSuit(cardHand,clubsInt);
		List<Integer> suitCounts = new ArrayList<Integer>();
		int biggestCount = 0;
		List<Integer> strengths = new ArrayList<Integer>();
		strengths.add(spadeStrength);
		strengths.add(heartsStrength);
		strengths.add(diamondStrength);
		strengths.add(clubStrength);
		List<Integer> possibleTrumpSuits = new ArrayList<Integer>();
		int biggestStrength = Collections.max(strengths);
		
		int thisSuitStrength = 0;
		int thisCount = 0;
		for (int suit = 1; suit < 5; suit++){
			thisSuitStrength = getCardStrengthOfSuit(cardHand,suit);
			if (thisSuitStrength == biggestStrength){
				
				possibleTrumpSuits.add(suit);
			}
		}
		
		if (possibleTrumpSuits.size() == 1){
			return commonFuncs.getSuitString(possibleTrumpSuits.get(0));
		}
		else {
			for (int suit : possibleTrumpSuits){
				suitCounts.add((getCardsOfSuit(cardHand,suit)).size());
			}
			biggestCount = Collections.max(suitCounts);
			
			for (int index = 0; index < possibleTrumpSuits.size(); index++){
				thisCount = suitCounts.get(index);
				if (thisCount == biggestCount){
					//Can get away with this because if two suits have same count and same strength
					//Then they are the same, up to us to pick one.
					return commonFuncs.getSuitString(possibleTrumpSuits.get(index));
				}
			}
		}
		
		//if here problem occurred;
		return null;
		
	}
	
	
	private List<Card> getCardsOfSuit(List<Card> cards, int suit){
		
		List<Card> cardsOfThatSuit = new ArrayList<Card>();
		
		Log.d("DECK", "Cards to examine: " + cards.toString());
		Log.d("DECK", "Suit:" + commonFuncs.getSuitString(suit));
		
		for (Card card: cards){
			//check needed for when midway through the trick. Some players maybe null.
			if (card !=null){
				if (card.getCardSuit() == suit){
					cardsOfThatSuit.add(card);
				}
			}
		}
		
		
		
			
		
		return cardsOfThatSuit;
	}
	
	
	private int getCardStrengthOfSuit(List<Card> cards, int suit){
		
		List<Card> cardsOfThatSuit = new ArrayList<Card>();
		int suitStrength = 0;
		int acesCount = 0;
		int tensCount = 0;
		int kingsCount = 0;
		int queensCount = 0;
		int jacksCount = 0;
		Boolean runExists = false;
		Boolean doubleRunExists = false;
		for (Card card: cards){
			if (card.getCardSuit() == suit){
				cardsOfThatSuit.add(card);
				suitStrength=suitStrength+card.getRankOfCard();
				if (card.getRankOfCard()==ace){
					acesCount++;
				}
				else if (card.getRankOfCard()==ten){
					tensCount++;
				}
				else if (card.getRankOfCard()==king){
					kingsCount++;
				}
				else if (card.getRankOfCard()==queen){
					queensCount++;
				}
				else if (card.getRankOfCard()==jack){
					jacksCount++;
				}
			}
		}
		
		
		if ( acesCount > 0 && tensCount > 0 && kingsCount > 0 && queensCount > 0 && jacksCount > 0){
			//Run exists
			suitStrength = suitStrength+15;
			runExists = true;
			if ( acesCount > 1 && tensCount > 1 && kingsCount > 1 && queensCount > 1 && jacksCount > 1){
				suitStrength = suitStrength+135;//Double run exists;
				doubleRunExists = true;
			}
		}
		
		
		if (!doubleRunExists){
			if(!runExists){
				if (kingsCount > 1 && queensCount > 1){
					suitStrength=suitStrength+2;//Want it weighted slightly less than meld, because you may not get that suit as trump.
				}
			}
			else {
				if (kingsCount == 2 && queensCount == 2){
					suitStrength=suitStrength+2;//Want it weighted slightly less than meld, because you may not get that suit as trump.
				}
			}
		}
		
		
		if (acesCount == 0){
			suitStrength = suitStrength -6;
		}
		
		
			
		//Log.d("DECK", "Suit:" + commonFuncs.getSuitString(suit) + " Strength:" + suitStrength);
		return suitStrength*this.constants.pointerBaseUnit;
	}



	private int getPlayerWhoWonBid() {
	
		Log.d("DECK","Hi!");
		int currentPlayer = 0;
		for (Boolean hasPassed: this.playersPassed){
			currentPlayer++;
			if (!hasPassed){
				Log.d("DECK", "Yay" + currentPlayer);
				return currentPlayer;
			}
		}
		Log.d("DECK","Boo!");
		return 0;
	}





	private int checkPlayersPassed() {
	
		int playersPassed=0;
		for (Boolean hasPassed: this.playersPassed){
			if (hasPassed){
				playersPassed++;
			}
		}
		return playersPassed;
	}



	
	
	private void determineMaxBids() {
	
		
		List<Integer> maximumBids = generateMaximumBidsPerCPU();
		this.maximumCPUBids = maximumBids;
	}

	private List<Integer> generateMaximumBidsPerCPU() {
	
		List<Integer> bidsDetermined = new ArrayList<Integer>();
		for (Player player: this.players){
			if (player.getPlayerId() == humanPlayer){
				bidsDetermined.add(0);
			}
			else {
				bidsDetermined.add(getMaxBidForThisCPU(player.getPlayerId()));
			}
		}
		return bidsDetermined;
	}

	
	private int determineMeld(List<Card> cardHand, int trump){
		
		//Check trump first for meld.
		List<Card> cardsOfThatSuit = getCardsOfSuit(cardHand, trump);
		int meld = 0;
		int acesCount = 0;
		int tensCount = 0;
		int kingsCount = 0;
		int queensCount = 0;
		int jacksCount = 0;
		int ninesCount = 0;
		boolean runExists = false;
		boolean doubleRunExists = false;
		for (Card card: cardsOfThatSuit){
			if (card.getRankOfCard()==ace){
				acesCount++;
			}
			else if (card.getRankOfCard()==ten){
				tensCount++;
			}
			else if (card.getRankOfCard()==king){
				kingsCount++;
			}
			else if (card.getRankOfCard()==queen){
				queensCount++;
			}
			else if (card.getRankOfCard()==jack){
				jacksCount++;
			}
			else if (card.getRankOfCard()==nine){
				ninesCount++;
			}
		}
		
		
		if ( acesCount > 0 && tensCount > 0 && kingsCount > 0 && queensCount > 0 && jacksCount > 0){
			//Run exists
			meld = meld+this.constants.runOfTrump;
			runExists = true;
			if ( acesCount > 1 && tensCount > 1 && kingsCount > 1 && queensCount > 1 && jacksCount > 1){
				meld = meld+this.constants.doubleRunOfTrump-this.constants.runOfTrump;//Double run exists;//Reason to add 135 is we already added 15 for first run.
				doubleRunExists = true;
			}
		}
		
		
		if (!doubleRunExists){
			if(!runExists){
				if (kingsCount > 0 && queensCount > 0){
					if (kingsCount == 2 && queensCount == 2){
						meld = meld + 2*this.constants.trumpMarriage;
					}
					else {
						meld = meld + this.constants.trumpMarriage;
					}
				}
			}
			else {
				if (kingsCount == 2 && queensCount == 2){
					meld = meld +this.constants.trumpMarriage;
				}
			}
		}
		
		meld = meld + ninesCount*this.constants.deeceValue;
		
		//Now for X around;
		List<Integer> possibleXAround = new ArrayList<Integer>();
		possibleXAround.add(ace);
		possibleXAround.add(king);
		possibleXAround.add(queen);
		possibleXAround.add(jack);
		Boolean xAroundStillPossible = true;
		Boolean xDoubleAroundStillPossible = true;
		
		for (Integer rankToCheckForXAround : possibleXAround){
			for (int suit: allSuits){
				int numberOfXCard = countNumberEqual((ArrayList<Card>) cardHand, new Card(rankToCheckForXAround, suit));
				if (xAroundStillPossible){
					if ( numberOfXCard == 1){
						//SingleAround possible only now;
						xDoubleAroundStillPossible = false;
					}
					else {
						if (numberOfXCard == 0){
							xAroundStillPossible = false;
						}
					}
					
				}
				if (xDoubleAroundStillPossible){
					if (numberOfXCard == 2){
						//doubleXaround still possible
						//Do not exclude single around as a player may have 2 ace of diamonds but one ace of spades.
					}
					else{
						xDoubleAroundStillPossible=false;
					}
				}
			}
			//If true here doubleAround exists;
			if (xDoubleAroundStillPossible){
				if (rankToCheckForXAround == jack){
					meld = meld + this.constants.jacksDoubleAround;
				}
				else if (rankToCheckForXAround == queen){
					meld = meld + this.constants.queensDoubleAround;
				}
				else if (rankToCheckForXAround == king){
					meld = meld + this.constants.kingsDoubleAround;
				}
				else if (rankToCheckForXAround == ace){
					meld = meld + this.constants.acesDoubleAround;
				}
			}
			//If true here xAround exists;
			if (xAroundStillPossible){
				if (rankToCheckForXAround == jack){
					meld = meld + this.constants.jacksAround;
				}
				else if (rankToCheckForXAround == queen){
					meld = meld + this.constants.queensAround;
				}
				else if (rankToCheckForXAround == king){
					meld = meld + this.constants.kingsAround;
				}
				else if (rankToCheckForXAround == ace){
					meld = meld + this.constants.acesAround;
				}
			}
			
			//Set for next rank;
			xAroundStillPossible=true;
			xDoubleAroundStillPossible=true;
		}
		
		//Now check for Pinochle and double pinochle
		int queenOfSpadesCount = countNumberEqual((ArrayList<Card>) cardHand, new Card(queen, spadesInt));
		int jackOfDiamondsCount = countNumberEqual((ArrayList<Card>) cardHand, new Card(jack, diamondsInt));
		
		if (queenOfSpadesCount > 0 && jackOfDiamondsCount > 0){
			if (queenOfSpadesCount > 1 && jackOfDiamondsCount > 1){
				meld = meld + this.constants.doublePinochle;
			}
			else {
				meld = meld + this.constants.singlePinochle;
			}
		}
		
		
		//Check opposite sex marriages for meld;
		// :( I wish same sex marriages counted for meld;
		//Maybe marriage equality will come for Pinochle one day.
		//Go HRC! (Seriously love the HRC no sarcasm there. One day may they make marriage legal in Arkansas. )
		
		int KingsOffSuit = 0;
		int QueensOffSuit = 0;
		for (int suit: allSuits){
			//Skip if suit is trump. Was counted before do not want to double count.
			if (suit != trump){
				KingsOffSuit = countNumberEqual((ArrayList<Card>) cardHand, new Card(king, suit));
				QueensOffSuit = countNumberEqual((ArrayList<Card>) cardHand, new Card(queen, suit));
				//Log.d("DECK", "SUIT:" + commonFuncs.getSuitString(suit) + " Kings: " + KingsOffSuit + " Queens: " + QueensOffSuit);
				if (KingsOffSuit > 0 && QueensOffSuit > 0){
					if (KingsOffSuit > 1 && QueensOffSuit > 1){
						meld = meld + 2*this.constants.offSuitMarriage;
					}
					else {
						meld = meld + this.constants.offSuitMarriage;
					}
				}
			}
			
		}
		
		
		
		
		//Done checking all kinds of meld.
		return meld;
		
	}

	private int getMaxBidForThisCPU(int playerId) {
	
		
		
		//Reevaluate bid determination AI
		Log.d("DECK", "PLAYER " + playerId + " Hand:");
		for (Card card : this.players.get(playerId -1).getCardsInHand()){
			Log.d("DECK", card.getCardName());
		}
		Log.d("DECK", "PLAYER " + playerId + " Desired Bid Info:");
		String wantedTrumpSuit  = generateTrumpSuit(playerId);
		Log.d("DECK", "wanted Trump: " + wantedTrumpSuit);
		int strengthOfSuit = getCardStrengthOfSuit(this.players.get(playerId -1).getCardsInHand(),commonFuncs.getSuitInt(wantedTrumpSuit));
		Log.d("DECK", "Strength: " + strengthOfSuit);
		int bestMeldInHand = determineMeld(this.players.get(playerId -1).getCardsInHand(), commonFuncs.getSuitInt(wantedTrumpSuit));
		Log.d("DECK", "best Meld: " + bestMeldInHand);
		int maxBid = 0;
		List<Card> playerCards = this.players.get(playerId -1).getCardsInHand();
		
		//maxBid = 35*this.constants.pointerBaseUnit;
		
		List<Integer> countsOfEachRank = determineCountsOfEachRank(getCardsOfSuit(playerCards,commonFuncs.getSuitInt(wantedTrumpSuit)), commonFuncs.getSuitInt(wantedTrumpSuit));
		List<Card> specificCardsNeededForDoubleRun = cardsNeededForRun( countsOfEachRank,commonFuncs.getSuitInt(wantedTrumpSuit),2);
		List<Card> specificCardsNeededForSingleRun = cardsNeededForRun( countsOfEachRank,commonFuncs.getSuitInt(wantedTrumpSuit),1);
		List<Card> specificCardsNeededForSingleRunAndMarriage = cardsNeededForRunAndMarriage(countsOfEachRank, commonFuncs.getSuitInt(wantedTrumpSuit));
		List<Card> specificCardsNeededForSingleMarriage = cardsNeededForMarriage(countsOfEachRank, commonFuncs.getSuitInt(wantedTrumpSuit), 1);
		List<Card> specificCardsNeededForDoubleMarriage = cardsNeededForMarriage(countsOfEachRank, commonFuncs.getSuitInt(wantedTrumpSuit), 2);
		List<Card> specificCardsNeededForSingleDeece = cardsNeededForDeece(countsOfEachRank, commonFuncs.getSuitInt(wantedTrumpSuit), 1);
		List<Card> specificCardsNeededForDoubleDeece = cardsNeededForDeece(countsOfEachRank, commonFuncs.getSuitInt(wantedTrumpSuit), 2);
		List<Card> specificCardsNeededForSinglePinochle = cardsNeededForPinochle(playerCards, 1);
		List<Card> specificCardsNeededForDoublePinochle = cardsNeededForPinochle(playerCards,2);
		List<Card> specificCardsNeededForJacksAround = cardsNeededForXAround(playerCards,jack, 1);
		List<Card> specificCardsNeededForQueensAround = cardsNeededForXAround(playerCards, queen, 1);
		List<Card> specificCardsNeededForKingsAround = cardsNeededForXAround(playerCards, king, 1);
		List<Card> specificCardsNeededForAceAround = cardsNeededForXAround(playerCards, king, 1);
		List<Card> specificCardsNeededForJacks2Around = cardsNeededForXAround(playerCards,jack, 2);
		List<Card> specificCardsNeededForQueens2Around = cardsNeededForXAround(playerCards, queen, 2);
		List<Card> specificCardsNeededForKings2Around = cardsNeededForXAround(playerCards, king, 2);
		List<Card> specificCardsNeededForAce2Around = cardsNeededForXAround(playerCards, king, 2);
		
		//Best Meld to least meld is
		//Double Run
		//Ace Double Around
		//Kings Double Around
		//Queens Double Around
		//Jacks Double Around
		//Double Pinochle
		//One Run + Marriage
		//One Run
		//Aces Around
		//Kings Around
		//Two Marriages
		//Queens Around
		//Pinochle
		//Jacks Around
		//One Marriage
		//Two Deece
		//One Deece
		
		List<List<Card>> cardsNeededForEachMeld = new ArrayList<List<Card>>();
		cardsNeededForEachMeld.add(specificCardsNeededForDoubleRun);
		cardsNeededForEachMeld.add(specificCardsNeededForAce2Around);
		cardsNeededForEachMeld.add(specificCardsNeededForKings2Around);
		cardsNeededForEachMeld.add(specificCardsNeededForQueens2Around);
		cardsNeededForEachMeld.add(specificCardsNeededForJacks2Around);
		cardsNeededForEachMeld.add(specificCardsNeededForDoublePinochle);
		cardsNeededForEachMeld.add(specificCardsNeededForSingleRunAndMarriage);
		cardsNeededForEachMeld.add(specificCardsNeededForSingleRun);
		cardsNeededForEachMeld.add(specificCardsNeededForAceAround);
		cardsNeededForEachMeld.add(specificCardsNeededForKingsAround);
		cardsNeededForEachMeld.add(specificCardsNeededForDoubleMarriage);
		cardsNeededForEachMeld.add(specificCardsNeededForQueensAround);
		cardsNeededForEachMeld.add(specificCardsNeededForSinglePinochle);
		cardsNeededForEachMeld.add(specificCardsNeededForJacksAround);
		cardsNeededForEachMeld.add(specificCardsNeededForSingleMarriage);
		cardsNeededForEachMeld.add(specificCardsNeededForDoubleDeece);
		cardsNeededForEachMeld.add(specificCardsNeededForSingleDeece);
		
		Double oddsOfHavingOneDeece = determineDeeceOdds(specificCardsNeededForSingleDeece, 1);
		Double oddsOfHavingTwoDeece = determineDeeceOdds(specificCardsNeededForDoubleDeece, 2);
		Double oddsOfHavingOneRun = determineRunOdds(specificCardsNeededForSingleRun, 1);
		Double oddsOfHavingTwoRuns = determineRunOdds(specificCardsNeededForDoubleRun,2);
		Double oddsOfHavingOneRunAndMarriage = determineOneRunAndMarriageOdds(specificCardsNeededForSingleRunAndMarriage);
		Double oddsOfHavingOneTrumpMarriage = determineMarriageOdds(specificCardsNeededForSingleMarriage, 1);
		Double oddsOfHavingTwoTrumpMarriages = determineMarriageOdds(specificCardsNeededForDoubleMarriage, 2);
		Double oddsOfHavingSinglePinochle = determinePinochleOdds(specificCardsNeededForSinglePinochle, 1);
		Double oddsOfHavingDoublePinochle = determinePinochleOdds(specificCardsNeededForDoublePinochle,2);
		Double oddsOfHavingAcesAround = determineOddsOfXAround(specificCardsNeededForAceAround,1);
		Double oddsOfHavingKingsAround = determineOddsOfXAround(specificCardsNeededForKingsAround,1);
		Double oddsOfHavingQueensAround = determineOddsOfXAround(specificCardsNeededForQueensAround,1);
		Double oddsOfHavingJacksAround = determineOddsOfXAround(specificCardsNeededForJacksAround,1);
		Double oddsOfHavingAces2Around = determineOddsOfXAround(specificCardsNeededForAce2Around,2);
		Double oddsOfHavingKings2Around = determineOddsOfXAround(specificCardsNeededForKings2Around,2);
		Double oddsOfHavingQueens2Around = determineOddsOfXAround(specificCardsNeededForQueens2Around,2);
		Double oddsOfHavingJacks2Around = determineOddsOfXAround(specificCardsNeededForJacks2Around,2);
		
		
		
		//There is OVERLAP between the odds presented here. That is intentional. 
		
		List<Double> oddsOfMeldType = new ArrayList<Double>();
		oddsOfMeldType.add(oddsOfHavingTwoRuns);
		oddsOfMeldType.add(oddsOfHavingAces2Around);
		oddsOfMeldType.add(oddsOfHavingKings2Around);
		oddsOfMeldType.add(oddsOfHavingQueens2Around);
		oddsOfMeldType.add(oddsOfHavingJacks2Around);
		oddsOfMeldType.add(oddsOfHavingDoublePinochle);
		oddsOfMeldType.add(oddsOfHavingOneRunAndMarriage);
		oddsOfMeldType.add(oddsOfHavingOneRun); 
		oddsOfMeldType.add(oddsOfHavingAcesAround);
		oddsOfMeldType.add(oddsOfHavingKingsAround);
		oddsOfMeldType.add(oddsOfHavingTwoTrumpMarriages);
		oddsOfMeldType.add(oddsOfHavingQueensAround);
		oddsOfMeldType.add(oddsOfHavingSinglePinochle);
		oddsOfMeldType.add(oddsOfHavingJacksAround);
		oddsOfMeldType.add(oddsOfHavingOneTrumpMarriage);
		oddsOfMeldType.add(oddsOfHavingTwoDeece);
		oddsOfMeldType.add(oddsOfHavingOneDeece);
		
		
		Log.d("DECK", "Percentage of having certain trump melds after passing.");
		Log.d("DECK", "One deece odds after pass: " + (oddsOfHavingOneDeece * 100));
		Log.d("DECK", "Two deece odds after pass: " + (oddsOfHavingTwoDeece * 100));
		Log.d("DECK", "One marriage without regards to run odds after pass: " + (oddsOfHavingOneTrumpMarriage * 100));
		Log.d("DECK", "One marriage but no run: " + (1-oddsOfHavingOneRun) * (oddsOfHavingOneTrumpMarriage) * 100 ); 
		Log.d("DECK", "Two marriages odds without regards run after pass: " + (oddsOfHavingTwoTrumpMarriages * 100));
		Log.d("DECK", "Two marriages odds without run: " + (1-oddsOfHavingOneRun) * (oddsOfHavingTwoTrumpMarriages) * 100 );
		Log.d("DECK", "One run odds after pass: " + (oddsOfHavingOneRun * 100));
		Log.d("DECK", "One run and marriage odds after pass: " + (oddsOfHavingOneRunAndMarriage * 100));
		Log.d("DECK", "Two run odds after pass: " + (oddsOfHavingTwoRuns * 100));
		Log.d("DECK", "Single Pinochle odds after pass: " + (oddsOfHavingSinglePinochle * 100));
		Log.d("DECK", "Double Pinochle odds after pass: " + (oddsOfHavingDoublePinochle * 100));
		Log.d("DECK", "Aces Around odds after pass: " + (oddsOfHavingAcesAround * 100));
		Log.d("DECK", "Kings Around odds after pass: " + (oddsOfHavingKingsAround * 100));
		Log.d("DECK", "Queens Around odds after pass: " + (oddsOfHavingQueensAround * 100));
		Log.d("DECK", "Jacks Around odds after pass: " + (oddsOfHavingJacksAround * 100));
		Log.d("DECK", "Aces 2Around odds after pass: " + (oddsOfHavingAces2Around * 100));
		Log.d("DECK", "Kings 2Around odds after pass: " + (oddsOfHavingKings2Around * 100));
		Log.d("DECK", "Queens 2Around odds after pass: " + (oddsOfHavingQueens2Around * 100));
		Log.d("DECK", "Jacks 2Around odds after pass: " + (oddsOfHavingJacks2Around * 100));
		
		//Best Meld to least meld is
		//Double Run
		//Ace Double Around
		//Kings Double Around
		//Queens Double Around
		//Jacks Double Around
		//Double Pinochle
		//One Run + Marriage
		//One Run
		//Aces Around
		//Kings Around
		//Two Marriages
		//Queens Around
		//Pinochle
		//Jacks Around
		//One Marriage
		//Two Deece
		//One Deece
		
		
		
		Double likelyMeld = Double.valueOf(0);
		int index = 0;
		
		
		//Reverse the odds and cardsNeeded order because ranking system is 1 >> 17 deece to tworuns.
		Collections.reverse(oddsOfMeldType);
		Collections.reverse(cardsNeededForEachMeld);
		
		//Calculate unconfirmed meld.
		//Note we add the percentages for queen of spades/jack of diamonds melds as they should be the first thing a bidwinner passes.
		//Therefore it will still act like the symbiosis.
		//The X arounds are commented out because rarely expected to happen and elevated totals were throwing off calcs.
		for (Double odds: oddsOfMeldType){
			index++;
			if (odds > 0){
				if (index == twoRuns){
					Log.d("DECK", "2Runs Likely Meld Addition " + (odds * this.constants.doubleRunOfTrump));
					likelyMeld = likelyMeld + (odds * this.constants.doubleRunOfTrump);
				}
				/*else if (index == twoAcesAround){
					
					Log.d("DECK", "2AceAround Likely Meld Addition " + (odds * this.constants.acesDoubleAround));
					likelyMeld = likelyMeld + (odds * this.constants.acesDoubleAround / 4);
				}
				else if (index == twoKingsAround){
					
					Log.d("DECK", "2KingsAround Likely Meld Addition " + (odds * this.constants.kingsDoubleAround));
					likelyMeld = likelyMeld + (odds * this.constants.kingsDoubleAround / 8);
				}
				else if (index == twoQueensAround){
					
					Log.d("DECK", "2QueensAround Likely Meld Addition " + (odds * this.constants.queensDoubleAround));
					likelyMeld = likelyMeld + (odds * this.constants.queensDoubleAround / 16);
				}
				else if (index == twoJacksAround){
					
					Log.d("DECK", "2JacksAround Likely Meld Addition " + (odds * this.constants.jacksDoubleAround));
					likelyMeld = likelyMeld + (odds * this.constants.jacksDoubleAround / 32);
				}*/
				else if (index == twoPinochle){
					Log.d("DECK", "2Pinochle Likely Meld Addition " + (odds * this.constants.doublePinochle));
					likelyMeld = likelyMeld + (odds * this.constants.doublePinochle);
				}
				else if (index == oneRunWithMarriage){
					Log.d("DECK", "OneRunWM Likely Meld Addition " + (odds * (this.constants.runOfTrump + this.constants.trumpMarriage)));
					likelyMeld = likelyMeld + (odds* (this.constants.runOfTrump + this.constants.trumpMarriage));
				}
				else if (index == oneRun){
					Log.d("DECK", "OneRun Likely Meld Addition " + (odds * this.constants.runOfTrump));
					likelyMeld = likelyMeld + (odds* this.constants.runOfTrump);
				}
				/*else if (index == oneAcesAround){
					
					Log.d("DECK", "AceAround Likely Meld Addition " + (odds * this.constants.acesAround));
					likelyMeld = likelyMeld + (odds * this.constants.acesAround /4 );
				}
				else if (index == oneKingsAround){
					
					Log.d("DECK", "KingsAround Likely Meld Addition " + (odds * this.constants.kingsAround));
					likelyMeld = likelyMeld + (odds * this.constants.kingsAround /8);
				}*/
				else if (index == twoMarriages){
					Log.d("DECK", "2Marriages Likely Meld Addition " + (odds * this.constants.trumpMarriage * 2));
					likelyMeld = likelyMeld + (odds * this.constants.trumpMarriage * 2);
				}
				/*else if (index == oneQueensAround){
					
					Log.d("DECK", "QueensAround Likely Meld Addition " + (odds * this.constants.queensAround));
					likelyMeld = likelyMeld + (odds * this.constants.queensAround/16);
				}*/
				else if (index == onePinochle){
					Log.d("DECK", "OnePinochle Likely Meld Addition " + (odds * this.constants.singlePinochle));
					likelyMeld = likelyMeld + (odds * this.constants.singlePinochle);
				}
				/*else if (index == oneJacksAround){
					
					Log.d("DECK", "JacksAround Likely Meld Addition " + (odds * this.constants.jacksAround));
					likelyMeld = likelyMeld + (odds * this.constants.jacksAround/32);
				}*/
				else if (index == oneMarriage){
					Log.d("DECK", "OneMarriage Likely Meld Addition " + (odds * this.constants.trumpMarriage));
					likelyMeld = likelyMeld + (odds * this.constants.trumpMarriage);
				}
				else if (index == twoDeeces){
					Log.d("DECK", "TwoDeecee Likely Meld Addition " + (odds * this.constants.deeceValue *2));
					likelyMeld = likelyMeld + (odds * this.constants.deeceValue * 2);
				}
				else if (index == oneDeece){
					Log.d("DECK", "OneDeecee Likely Meld Addition " + (odds * this.constants.deeceValue));
					likelyMeld = likelyMeld + (odds * this.constants.deeceValue);
				}
			}
		}
		
		Log.d("DECK", "Likely Meld: " + likelyMeld);
		//Calculate confirmed meld. Note we do not odds this one as it is unlikely that your partner will pass you losers to increase this.
		//There is no also guarantee that you will pass these to your partner.
		int confirmedMeld = 0;
			
		//Check opposite sex marriages for meld;
		// :( I wish same sex marriages counted for meld;
		//Maybe marriage equality will come for Pinochle one day.
		//Go HRC! (Seriously love the HRC no sarcasm there. One day may they make marriage legal in Arkansas. )
		
		int KingsOffSuit = 0;
		int QueensOffSuit = 0;
		for (int suit: allSuits){
			//Skip if suit is trump. Was counted before do not want to double count.
			if (suit != commonFuncs.getSuitInt(wantedTrumpSuit)){
				KingsOffSuit = countNumberEqual((ArrayList<Card>) playerCards, new Card(king, suit));
				QueensOffSuit = countNumberEqual((ArrayList<Card>) playerCards, new Card(queen, suit));
				//Log.d("DECK", "SUIT:" + commonFuncs.getSuitString(suit) + " Kings: " + KingsOffSuit + " Queens: " + QueensOffSuit);
				if (KingsOffSuit > 0 && QueensOffSuit > 0){
					if (KingsOffSuit > 1 && QueensOffSuit > 1){
						confirmedMeld = confirmedMeld + 2*this.constants.offSuitMarriage;
					}
					else {
						confirmedMeld = confirmedMeld + this.constants.offSuitMarriage;
					}
				}
			}
			
		}
		
		//nerfing likelyMeld as it is a bit too aggressive. 
		likelyMeld = likelyMeld * .75;
		
		int bidMeld = ((Double) (Math.ceil(likelyMeld))).intValue() + confirmedMeld;
		Log.d("DECK", "Bid Meld: " + bidMeld);
		
	
		int countLikelyWinningCards = countLikelyWinners(playerCards, wantedTrumpSuit);
		Double likelyPointersCaptured = Double.valueOf(countLikelyWinningCards) / Double.valueOf(playerCards.size()) * Double.valueOf(this.constants.maxPointers);
		
		int recommendedMaxBid = bidMeld + ((Double) (Math.ceil(likelyPointersCaptured))).intValue();
		Log.d("DECK", "Recommended Max: " + recommendedMaxBid);
		
		
		recommendedMaxBid = recommendedMaxBid - (this.players.get(playerId -1).getBidStyle()*this.constants.pointerBaseUnit); //-4 for conservative, -2 for moderate and -0 for aggressive set this up later
		maxBid = recommendedMaxBid;
		
		//Need to round up or down to the next 10.
		
		
		// maxBid = recommendedMaxBid;
		
		
		
		
		/*
		if (bestMeldInHand > (29*this.constants.pointerBaseUnit)){
			maxBid = bestMeldInHand;
			
			//If user has run or close to run this is not an xAround junk hand or double pinochle junk hand;
			//Add 15 to that bid as they can probably take 15 pointers.
			if (strengthOfSuit > (15*this.constants.pointerBaseUnit)){
				maxBid = maxBid + (15*this.constants.pointerBaseUnit);
			}
			
		}
		else if (bestMeldInHand > (14*this.constants.pointerBaseUnit)) {
			//User has a run or run + something;
			//Or they have AcesAround and pinochle + at least 1 trump;
			maxBid =  bestMeldInHand;
			
			//If user has run or close to run this is not an xAround junk hand or double pinochle hand;
			//Add strengthOfSuit=15;
			if (strengthOfSuit > (15*this.constants.pointerBaseUnit)){
				maxBid = maxBid + strengthOfSuit;
				
				//Strength of Suit counts a run as 15 so need to remove one instance of run from maxBid if run exists;
				int acesCount = countNumberEqual((ArrayList<Card>)this.players.get(playerId -1).getCardsInHand(), new Card(ace,commonFuncs.getSuitInt(wantedTrumpSuit)));
				int tensCount = countNumberEqual((ArrayList<Card>)this.players.get(playerId -1).getCardsInHand(), new Card(ten,commonFuncs.getSuitInt(wantedTrumpSuit)));
				int kingsCount = countNumberEqual((ArrayList<Card>)this.players.get(playerId -1).getCardsInHand(), new Card(king,commonFuncs.getSuitInt(wantedTrumpSuit)));
				int queensCount = countNumberEqual((ArrayList<Card>)this.players.get(playerId -1).getCardsInHand(), new Card(queen,commonFuncs.getSuitInt(wantedTrumpSuit)));
				int jacksCount = countNumberEqual((ArrayList<Card>)this.players.get(playerId -1).getCardsInHand(), new Card(jack,commonFuncs.getSuitInt(wantedTrumpSuit)));
				
				if (acesCount > 0 && tensCount > 0 && kingsCount > 0 && queensCount > 0 && jacksCount > 0){
					maxBid = maxBid - (15*this.constants.pointerBaseUnit);
				}
				
			}
		}
		else {
			//This CPU is gambling because they'd have no real "meld" in any particular suit. They are hoping for a dummy pass to get what is needed.
			//The best they have is aces Around and that is not guaranteed to take tricks.
			if (strengthOfSuit > (14*this.constants.pointerBaseUnit)){
				//Definitely hoping for a run.
				//Dividing by 2 here to account for having 2 aces and 2 tens and 2 kings and 2 queens and no jacks being highest possible suit strength without run.
				maxBid = (bestMeldInHand + (int) (15+Math.floor(strengthOfSuit/2)) +5)*this.constants.pointerBaseUnit ; //The plus five is because I am too conservative.
			}
			else{
			//If the strength of your strongest suit is not at least 15 and CPU has no decent meld it has no business winning this hand. Max bid 1;
			
				maxBid = 1*this.constants.pointerBaseUnit;
			}
			
		}
		
		*/
		
		
		Log.d("DECK", "Max Bid: " + maxBid);
		
		return maxBid ;//Sometimes games are played with 10 or 1 per trick.
			
		
	}

	


	private int countLikelyWinners(List<Card> playerCards, String trump) {
		
		List<Card> likelyWinners = new ArrayList<Card>();
		
		for (Card card: playerCards){
			if (card.getSuitString().equals(trump)){
				likelyWinners.add(card);
				continue;
			}
			else if(card.getRankOfCard() == ace){
				likelyWinners.add(card);
			}
			
		}
		//Assuming good passing both ways.
		return likelyWinners.size() + 4;
		
	}


	private Double determineOddsOfXAround( List<Card> specificCardsNeededForXAround, int numberAround) {
		
			
		//Now this becomes the same bat channel as the marriage loop. Just potentially more cards needed. :)
		return determineMarriageOdds(specificCardsNeededForXAround, numberAround);
		
	}


	private List<Card> cardsNeededForXAround(List<Card> playerCards, int rank, int numberAround) {
		
		List<Card> cardsNeeded = new ArrayList<Card>();
		int XofSpadesCount = countNumberEqual((ArrayList<Card>) playerCards, new Card(rank, spadesInt));
		int XofHeartsCount = countNumberEqual((ArrayList<Card>) playerCards, new Card(rank, heartsInt));
		int XofDiamondsCount = countNumberEqual((ArrayList<Card>) playerCards, new Card(rank, diamondsInt));
		int XofClubsCount = countNumberEqual((ArrayList<Card>) playerCards, new Card(rank, clubsInt));
		
		if ((XofSpadesCount > (numberAround-1)) && (XofHeartsCount > (numberAround-1)) && (XofDiamondsCount > (numberAround-1)) && (XofClubsCount > (numberAround-1))){
			return cardsNeeded;
		}
		else {
			for (int i=XofSpadesCount;i <numberAround; i++ ){
				cardsNeeded.add(new Card(rank,spadesInt));
			}
			for (int i=XofHeartsCount;i <numberAround; i++ ){
				cardsNeeded.add(new Card(rank,heartsInt));
			}
			for (int i=XofDiamondsCount;i <numberAround; i++ ){
				cardsNeeded.add(new Card(rank,diamondsInt));
			}
			for (int i=XofClubsCount;i <numberAround; i++ ){
				cardsNeeded.add(new Card(rank,clubsInt));
			}
		}
		return cardsNeeded;
	}


	private Double determinePinochleOdds(List<Card> specificCardsNeededForPinochle, int numberOfPinochles) {
		
		//Since this is probability only these odds are EXACTLY the same calculation as the marriage ones, just different cards.
		return determineMarriageOdds(specificCardsNeededForPinochle, numberOfPinochles);
	}


	private List<Card> cardsNeededForPinochle(List<Card> playerCards, int numberOfPinochle) {
	
		List<Card> cardsNeeded = new ArrayList<Card>();
		int queenOfSpadesCount = countNumberEqual((ArrayList<Card>) playerCards, new Card(queen, spadesInt));
		int jackOfDiamondsCount = countNumberEqual((ArrayList<Card>) playerCards, new Card(jack, diamondsInt));
		
		if ((queenOfSpadesCount > (numberOfPinochle-1)) && (jackOfDiamondsCount > (numberOfPinochle-1))){
			return cardsNeeded;
		}
		else {
			for (int i=queenOfSpadesCount;i <numberOfPinochle; i++ ){
				cardsNeeded.add(new Card(queen,spadesInt));
			}
			for (int i=jackOfDiamondsCount;i <numberOfPinochle; i++ ){
				cardsNeeded.add(new Card(jack,diamondsInt));
			}
		}
		return cardsNeeded;
	}


	private List<Card> cardsNeededForDeece(List<Integer> countsOfEachRank,	int suitInt, int numberOfDeece) {
		
		int ninesCount = countsOfEachRank.get(nine -1);
		
		
		List<Card> distinctCardsNeededForSetup = new ArrayList<Card>();
		
		if ( ninesCount >= (numberOfDeece) ){
			//NumberOfD exist.	
			return distinctCardsNeededForSetup;
		}

		else{
			for (int i=ninesCount; i< (numberOfDeece); i++){
				distinctCardsNeededForSetup.add(new Card(nine, suitInt));
			}
		}
			
		
		
		return distinctCardsNeededForSetup;
	}


	private List<Card> cardsNeededForRunAndMarriage(List<Integer> countsOfEachRank, int suitInt) {
		int acesCount = countsOfEachRank.get(ace -1);
		int tensCount = countsOfEachRank.get(ten -1);
		int kingsCount = countsOfEachRank.get(king -1);
		int queensCount = countsOfEachRank.get(queen -1);
		int jacksCount = countsOfEachRank.get(jack -1);
	
		
		
		
		List<Card> distinctCardsNeededForThisTypeOfRun = new ArrayList<Card>();
		
		for (int rank=jack; rank< ace+1; rank++){
			if ((rank==jack) && (jacksCount<1)){
				
					distinctCardsNeededForThisTypeOfRun.add(new Card(jack, suitInt));
				
			}
			if ((rank==queen) && (queensCount<2)){
				for (int i=queensCount; i< 2; i++){
					distinctCardsNeededForThisTypeOfRun.add(new Card(queen, suitInt));
				}
			}
			if ((rank==king) && (kingsCount<2)){
				for (int i=kingsCount; i< 2; i++){
					distinctCardsNeededForThisTypeOfRun.add(new Card(king, suitInt));
				}
			}
			if ((rank==ten) && (tensCount<1)){
				
					distinctCardsNeededForThisTypeOfRun.add(new Card(ten, suitInt));
				
			}
			if ((rank==ace) && (acesCount<1)){
				
					distinctCardsNeededForThisTypeOfRun.add(new Card(ace, suitInt));
				
			}
		}
		
		
		
		return distinctCardsNeededForThisTypeOfRun;
	}


	private List<Card> cardsNeededForMarriage(List<Integer> countsOfEachRank,
			int suitInt, int numberOfMarriages) {
	
		int kingsCount = countsOfEachRank.get(king -1);
		int queensCount = countsOfEachRank.get(queen -1);
		
		List<Card> distinctCardsNeededForSetup = new ArrayList<Card>();
		
		if ( kingsCount > (numberOfMarriages-1) && queensCount > (numberOfMarriages-1) ){
			//NumberOfMarriages exist.	
			return distinctCardsNeededForSetup;
		}
		
		
		
		for (int rank=jack; rank< ace+1; rank++){
			
			if ((rank==queen) && (queensCount<(numberOfMarriages))){
				for (int i=queensCount; i< (numberOfMarriages); i++){
					distinctCardsNeededForSetup.add(new Card(queen, suitInt));
				}
			}
			if ((rank==king) && (kingsCount<(numberOfMarriages))){
				for (int i=kingsCount; i< (numberOfMarriages); i++){
					distinctCardsNeededForSetup.add(new Card(king, suitInt));
				}
			}
			
		}
		
		return distinctCardsNeededForSetup;
	}


	private List<Card> cardsNeededForRun(List<Integer> cardsOfEachRank, int suitInt, int numberOfRuns) {
		
		int acesCount = cardsOfEachRank.get(ace -1);
		int tensCount = cardsOfEachRank.get(ten -1);
		int kingsCount = cardsOfEachRank.get(king -1);
		int queensCount = cardsOfEachRank.get(queen -1);
		int jacksCount = cardsOfEachRank.get(jack -1);
	
		
		List<Card> cardsNeeded = new ArrayList<Card>();
		
		if ( acesCount > (numberOfRuns -1) && tensCount > (numberOfRuns -1) && kingsCount > (numberOfRuns -1) && queensCount > (numberOfRuns -1) && jacksCount > (numberOfRuns -1)){
			//The right Run exists	
			return cardsNeeded;
		}
		
		List<Card> distinctCardsNeededForThisTypeOfRun = new ArrayList<Card>();
		
		for (int rank=jack; rank< ace+1; rank++){
			if ((rank==jack) && (jacksCount<numberOfRuns)){
				for (int i=jacksCount; i< numberOfRuns; i++){
					distinctCardsNeededForThisTypeOfRun.add(new Card(jack, suitInt));
				}
			}
			if ((rank==queen) && (queensCount<numberOfRuns)){
				for (int i=queensCount; i< numberOfRuns; i++){
					distinctCardsNeededForThisTypeOfRun.add(new Card(queen, suitInt));
				}
			}
			if ((rank==king) && (kingsCount<numberOfRuns)){
				for (int i=kingsCount; i< numberOfRuns; i++){
					distinctCardsNeededForThisTypeOfRun.add(new Card(king, suitInt));
				}
			}
			if ((rank==ten) && (tensCount<numberOfRuns)){
				for (int i=tensCount; i< numberOfRuns; i++){
					distinctCardsNeededForThisTypeOfRun.add(new Card(ten, suitInt));
				}
			}
			if ((rank==ace) && (acesCount<numberOfRuns)){
				for (int i=acesCount; i< numberOfRuns; i++){
					distinctCardsNeededForThisTypeOfRun.add(new Card(ace, suitInt));
				}
			}
		}
		
		
		
		return distinctCardsNeededForThisTypeOfRun;
	}


	private double determineMarriageOdds(List<Card> distinctCardsNeededForSetup, int numberOfMarriages) {
	
		
		int cardsToBeDrawn = this.players.get(0).getCardsInHand().size();
		int rem = cardsToBeDrawn * (numberOfPlayers-1);
		int cardsNeeded = distinctCardsNeededForSetup.size();
		
		//We can leverage the one run functionality now since the math formulas and outs are the same.
		if (numberOfMarriages == 1){
			return loopingOneRunOdds(cardsNeeded,rem, cardsToBeDrawn);
		}
		else {
			return loopingTwoRunOdds(cardsNeeded, rem, cardsToBeDrawn);
		}
	}


	private double determineDeeceOdds(List<Card> cardsNeeded , int numberOfDeeceToFind) {

		int ninesNeeded = cardsNeeded.size();
		
		if (ninesNeeded==0){
			return 1;
		}
		else {
			int partnerHandSize=this.players.get(0).getCardsInHand().size();
			int deckSize = partnerHandSize * (numberOfPlayers -1);
			if (ninesNeeded == 1){
				//two outs if one deece to find; one out if two.
				int outs =0;
				if (numberOfDeeceToFind ==1){outs =2;}else {outs=1;};
				return oddsOfCardDealtToPartner(deckSize,partnerHandSize ,outs);
			}
			else {
				return oddsOfCardDealtToPartner(deckSize,partnerHandSize , 1) * oddsOfCardDealtToPartner(deckSize-1,partnerHandSize-1 , 1);
			}
		}
		
	}


	private List<Integer> determineCountsOfEachRank(List<Card> cardsOfThatSuit, int suitInt) {
		
		
		List<Integer> countOfEachRank = new ArrayList<Integer>();
		
		
		//for each rank initialize the count to be zero.
		for (@SuppressWarnings("unused") int rank: allRanks){
			countOfEachRank.add(0);
		}
		
		//This loop says
		//For each card.
		//We will set the index of the rank which is rank -1 to be whatever it is now +1;
		for (Card card: cardsOfThatSuit){
			countOfEachRank.set(card.getRankOfCard() -1, countOfEachRank.get(card.getRankOfCard() -1)+1);
		}
		
		return countOfEachRank;
			
	}


	private double determineOneRunAndMarriageOdds(List<Card> distinctCardsNeededForSetup) {
	
		
		
		double setupOdds = 1;
		
		
		
		int cardsToBeDrawn = this.players.get(0).getCardsInHand().size();
		int rem = cardsToBeDrawn * (numberOfPlayers-1);
		int cardsNeeded = distinctCardsNeededForSetup.size();
		
		
		
		//This one we cannot use a simple loop like the one run and two run but we can still leverage the factorial.
		if (cardsNeeded > this.constants.cardsPassBetweenDummyAndBidder){
			return 0;
		}
		else {
			int outs = 2;
			for (Card card: distinctCardsNeededForSetup){
				if ((card.getRankOfCard() == queen) || (card.getRankOfCard() == king)){
					outs = 1;
				}
				else {
					outs = 2;
				}
				setupOdds = setupOdds * oddsOfCardDealtToPartner(rem, cardsToBeDrawn, outs);
				rem = rem -1;
				cardsToBeDrawn = cardsToBeDrawn -1;
			}
		}
		
		return setupOdds;
	}

	private double determineRunOdds(List<Card> distinctCardsNeededForSingleRun, int numberOfRuns) {
		
		
		
			
		double runOdds = 0;
				
		
		
		int cardsToBeDrawn = this.players.get(0).getCardsInHand().size();
		int rem = cardsToBeDrawn * (numberOfPlayers-1);
		int cardsNeeded = distinctCardsNeededForSingleRun.size();
		
		
		
		//The laws of conditional probability state that the odds of all four events occurring are the odds of each event occurring multiplied together.
		//Therefore we need to build a specific formula. Call it odds of card dealt to partner.
		//Then we will re-calculate with a shrinking "deck" assuming the first was met.
		//Needing too many cards is taken care of in the function and it will return 0;
		if(numberOfRuns == 1){
			runOdds = loopingOneRunOdds(cardsNeeded, rem, cardsToBeDrawn);
		
	
			return runOdds;
		}
		else {
			runOdds = loopingTwoRunOdds(cardsNeeded, rem, cardsToBeDrawn);
			
			
			return runOdds;
		}
		
		
	}
	
	private double loopingTwoRunOdds(int cardsNeeded, int remCardsDeck, int cardsToBeDrawn){
		
		if (cardsNeeded > this.constants.cardsPassBetweenDummyAndBidder){
			return 0;
		}
				
		if (cardsNeeded < 1){
			return 1;
		}
		else if (cardsNeeded == 1){
			return oddsOfCardDealtToPartner(remCardsDeck,cardsToBeDrawn,1);
		}
		else {
			//Calculate odds for this card. Then afterward one less card to repeat loop, one less card in "deck", and one less unknown in partners hand.
			//Outs here is one as both cards are needed.
			return oddsOfCardDealtToPartner(remCardsDeck,cardsToBeDrawn,1) * loopingTwoRunOdds(cardsNeeded-1,remCardsDeck-1,cardsToBeDrawn -1);
		}
	}
	
	private double loopingOneRunOdds(int cardsNeeded, int remCardsDeck, int cardsToBeDrawn){
		//We must take into account if the player can pass the amount of cards required if you win the bid.
		if (cardsNeeded > this.constants.cardsPassBetweenDummyAndBidder){
			return 0;
		}
		
		if (cardsNeeded < 1){
			return 1;
		}
		else if (cardsNeeded == 1){
			return oddsOfCardDealtToPartner(remCardsDeck,cardsToBeDrawn,2);
		}
		else {
			//Calculate odds for this card. Then afterward one less card to repeat loop, one less card in "deck", and one less unknown in partners hand.
			//Outs here is two as either card is acceptable.
			return oddsOfCardDealtToPartner(remCardsDeck,cardsToBeDrawn,2) * loopingOneRunOdds(cardsNeeded-1,remCardsDeck-1,cardsToBeDrawn -1);
		}
	}
	
	
	private double oddsOfCardDealtToPartner(int remCardsDeck, int cardsToBeDrawn, int outsForCard){
		//The odds of having the card dealt is 100-minus odds of not being dealt. 
		//not being dealt is easier to calc.
		
		return 1 - oddsOfNotDealtToPartner(remCardsDeck, cardsToBeDrawn, outsForCard);
		
	}
	
	private double oddsOfNotDealtToPartner(int remCardsDeck, int cardsToBeDrawn, int outsForCard) {
		
		
	//We need to calculate the odds that NONE of the remaining (rem) cards drawn have the 2 outs then subtract from 1.
	//First card is (rem-2/rem)
	//second is (rem-3)/(rem-1)
	//nth card is (rem-2-n)/(rem -n)
	//So top is (rem-2)*....(rem-2-cardsToBeDrawn)
	//Bottom is (rem)*....(rem-cardsToBeDrawn)
			
	//Top is equivalent to rem-2!/(rem-2-(cardsToBeDrawn + 1))!
	//Bottom is equivalent to rem!/(rem-(cardsToBeDrawn+1))!
		
		double topHalfOfFraction = factorial(remCardsDeck - outsForCard).doubleValue() / (factorial(remCardsDeck - outsForCard - (cardsToBeDrawn +1))).doubleValue();
		double bottomHalfOfFraction = factorial(remCardsDeck).doubleValue() / (factorial(remCardsDeck - (cardsToBeDrawn +1))).doubleValue();

		return topHalfOfFraction/bottomHalfOfFraction;
	}


	private BigInteger factorial(Integer n){
		if (n < 0){
			return null;
		}
		else if (n == 0) {
			return BigInteger.valueOf(1);
		}
		else {
			return BigInteger.valueOf(n).multiply(factorial(n-1));
		}
	}


	private ImageView rotateCardBacking(Bitmap image, float degrees){
		
		ImageView rotatedCard = new ImageView(this);
		Matrix rotationMatrix = new Matrix();
		
		rotationMatrix.postRotate(degrees);
		
		Bitmap rotated = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(),
		        rotationMatrix, true);

		rotatedCard.setImageBitmap(rotated);
		
		
		return rotatedCard;
		
	}
	
	private OnClickListener getOnClickListener(Card card) {
		OnClickListener imageClickListener;
		final Card cardClicked = card;
		
		if (this.humanPassedCards){
			
			 
			
			
			
	        imageClickListener = new OnClickListener(){
	
	            @Override
	            public void onClick(View v) {
	            	
	            	
	            	Button showHideTrickButton = (Button) findViewById(R.id.btn_show_last_trick);
	            	if ((showHideTrickButton.getText().equals(readFromStringXML(R.string.hide_last_trick)))){
	            		createAndShowErrorMessage(readFromStringXML(R.string.current_trick_not_displayed), readFromStringXML(R.string.please_show_current_trick));
	            		return;
	            	}
	            	
	                Log.d("MTITUS", "Clicked a card: " + cardClicked.getCardName() );
	                
	                Log.d("DECK", "Eligible Cards: " + eligibleCardsToChoose(humanPlayer).toString());
	                	              
	                if(eligibleCardsToChoose(humanPlayer).contains(cardClicked)){
	                	
	                	
	        	        if ((SystemClock.elapsedRealtime() - lastClickTime) < UsefulConstants.doubleClickMillisecondThresholdBlock){
	        	        	lastClickTime = SystemClock.elapsedRealtime();
	        	        	return;
	        	        }
	        	        else {
	        	        	
	        	        lastClickTime = SystemClock.elapsedRealtime();
		                Log.d("MTITUS", "Setting Card");
		                ImageView displayCard = (ImageView) findViewById(R.id.player1CardPlayedImage);
		                int imageId = 0;
		                imageId = getResources().getIdentifier(cardClicked.getImageFileName(), "", getPackageName());
		                displayCard.setImageResource(imageId);
		                humanHasPlayedACard(cardClicked);
		                returnToTrickFlow();
	        	        }
	                }
	                else {
	                	//createAndShowErrorMessage("Invalid Card Chosen", "Please choose a valid card.");
	                	createAndShowErrorMessage(readFromStringXML(R.string.invalid_card_chosen), readFromStringXML(R.string.please_choose_valid_card));
	                }
	                
	            }
	        };
	        
	        return imageClickListener;
		}else  {
				
			if (this.humanIsBidderOrDummy == null){
				imageClickListener = new OnClickListener(){

					@Override
					public void onClick(View v) {
						//DO NOTHING!
						
					}
				
				};
				return imageClickListener;
			}
			if (this.humanIsBidderOrDummy){
		
				 List<ImageView> preFinalPassingImages = new ArrayList<ImageView>();
				 final int defaultTag = R.drawable.img_card_back_96;
				 for (int cardImageView=1; cardImageView < this.constants.cardsPassBetweenDummyAndBidder + 1; cardImageView++){
					 preFinalPassingImages.add((ImageView) findViewById(getResources().getIdentifier("@id/human_pass_card_" + cardImageView, "", getPackageName())));
					 preFinalPassingImages.get(cardImageView -1).setTag(defaultTag);
				 }
				 final List<ImageView> passCardImageViews = preFinalPassingImages;
				 //final String defaultTag = "res/drawable-hdpi/img_card_back_96.png";
				 
				 
				 final List<Card> humanPlayerHand = this.players.get(humanPlayer-1).getCardsInHand();
				 final List<Card> cardsAlreadyPassed = this.cardsHumanPassed;
				imageClickListener = new OnClickListener(){
					
		            @Override
		            public void onClick(View v) {
		            	
		            	Log.d("DECK", "Wants to pass card: " + cardClicked.getCardName() );
		            	
		            	int cardsInPlayerHandOfClickedCard = countNumberEqual((ArrayList<Card>) humanPlayerHand, cardClicked);
						int cardsAlreadyPassedOfClickedCard = countNumberEqual((ArrayList<Card>) cardsAlreadyPassed, cardClicked);
						Log.d("DECK",""+cardsAlreadyPassed.size());
						if (!(cardsAlreadyPassed.size() == (new UsefulConstants()).cardsPassBetweenDummyAndBidder) ||  cardsAlreadyPassed.contains(null)){
							if (cardsInPlayerHandOfClickedCard > cardsAlreadyPassedOfClickedCard){
								Log.d("DECK", "Setting Card");
								
								int locationId = 0;
				                for (ImageView passCardImageView: passCardImageViews){
				                	//if (passCardImageView.get)
				                	if (passCardImageView.getTag().equals(defaultTag)){
				                	//if (passCardImageView.getTag().toString().equals(defaultTag)){
				                		addCardPassed(cardClicked, locationId);
				                		passCardImageView.setTag(getResources().getIdentifier(cardClicked.getImageFileName(), "", getPackageName()));
				                		passCardImageView.setImageResource(getResources().getIdentifier(cardClicked.getImageFileName(), "", getPackageName()));
				                		break;
				                	}
				                	locationId++;
				                }
								
							}
							else {
								Log.d("DECK", "Do alert dialog that user cannot choose to pass more of a card than is in your hand.");
								//createAndShowErrorMessage("Invalid Card Chosen", "You cannot choose more " + cardClicked.getCardName() + " than you have in your hand." );
								createAndShowErrorMessage(readFromStringXML(R.string.invalid_card_chosen), readAndReplaceStringXML(R.string.cannot_choose_more_x_card, cardClicked.getCardName()));
								
								  
								  
							}
						} else {
							Log.d("DECK", "Too many cards attempted to be passed");
							//createAndShowErrorMessage("Attempting to pass too many cards", "You cannot choose more than " + cardsPassBetweenDummyAndBidder + " to pass to your partner. Please replace a card or submit the cards.");
							createAndShowErrorMessage(readFromStringXML(R.string.cannot_pass_that_many_cards), readAndReplaceStringXML(R.string.cannot_pass_that_many_cards_detailed, ""+ (new UsefulConstants()).cardsPassBetweenDummyAndBidder));
						}
		            }
		        };
		        return imageClickListener;
			}
			else {
				 List<ImageView> preFinalPassingImages = new ArrayList<ImageView>();
				 final int defaultTag = R.drawable.img_card_back_96;
				 for (int cardImageView=1; cardImageView < this.constants.cardsPassBetweenOpponentsToBid + 1; cardImageView++){
					 preFinalPassingImages.add((ImageView) findViewById(getResources().getIdentifier("@id/human_pass_card_" + cardImageView, "", getPackageName())));
					 preFinalPassingImages.get(cardImageView -1).setTag(defaultTag);
				 }
				 final List<ImageView> passCardImageViews = preFinalPassingImages;
				 //final String defaultTag = "res/drawable-hdpi/img_card_back_96.png";
				 
				 
				 final List<Card> humanPlayerHand = this.players.get(humanPlayer-1).getCardsInHand();
				 final List<Card> cardsAlreadyPassed = this.cardsHumanPassed;
				imageClickListener = new OnClickListener(){
					
		            @Override
		            public void onClick(View v) {
		            	
		            	Log.d("DECK", "Wants to pass card: " + cardClicked.getCardName() );
		            	
		            	int cardsInPlayerHandOfClickedCard = countNumberEqual((ArrayList<Card>) humanPlayerHand, cardClicked);
						int cardsAlreadyPassedOfClickedCard = countNumberEqual((ArrayList<Card>) cardsAlreadyPassed, cardClicked);
						Log.d("DECK",""+cardsAlreadyPassed.size());
						if (!(cardsAlreadyPassed.size() == (new UsefulConstants()).cardsPassBetweenOpponentsToBid) ||  cardsAlreadyPassed.contains(null)){
							if (cardsInPlayerHandOfClickedCard > cardsAlreadyPassedOfClickedCard){
								Log.d("DECK", "Setting Card");
								
								int locationId = 0;
				                for (ImageView passCardImageView: passCardImageViews){
				                	//if (passCardImageView.get)
				                	if (passCardImageView.getTag().equals(defaultTag)){
				                	//if (passCardImageView.getTag().toString().equals(defaultTag)){
				                		addCardPassed(cardClicked, locationId);
				                		passCardImageView.setTag(getResources().getIdentifier(cardClicked.getImageFileName(), "", getPackageName()));
				                		passCardImageView.setImageResource(getResources().getIdentifier(cardClicked.getImageFileName(), "", getPackageName()));
				                		break;
				                	}
				                	locationId++;
				                }
								
							}
							else {
								Log.d("DECK", "Do alert dialog that user cannot choose to pass more of a card than is in your hand.");
								//createAndShowErrorMessage("Invalid Card Chosen", "You cannot choose more " + cardClicked.getCardName() + " than you have in your hand." );
								createAndShowErrorMessage(readFromStringXML(R.string.invalid_card_chosen), readAndReplaceStringXML(R.string.cannot_choose_more_x_card, cardClicked.getCardName()));
								
								  
								  
							}
						} else {
							Log.d("DECK", "Too many cards attempted to be passed");
							//createAndShowErrorMessage("Attempting to pass too many cards", "You cannot choose more than " + cardsPassBetweenDummyAndBidder + " to pass to your partner. Please replace a card or submit the cards.");
							createAndShowErrorMessage(readFromStringXML(R.string.cannot_pass_that_many_cards), readAndReplaceStringXML(R.string.cannot_pass_that_many_cards_detailed, ""+ (new UsefulConstants()).cardsPassBetweenOpponentsToBid));
						}
		            }
		        };
		        return imageClickListener;
			}
			
		}
		
	   
		
	}
	
	protected void humanHasPlayedACard(Card card) {
		
		this.humanChoseCardToPlay = true;
		this.playersPlayedCard.set(humanPlayer-1, Boolean.TRUE);
		this.cardsInTrick.set(humanPlayer -1, card);
		Log.d("MTITUS","Remove the " + card.toString());
		Log.d("MTITUS","Cards In Hand Pre remove " + this.players.get(humanPlayer -1).getCardsInHand().toString());
		this.players.get(humanPlayer -1).getCardsInHand().remove(card);
		recreateCardsToBeClickedOn(); //This is needed because the cards may still the old card listing.
		hidePromptForPlayerToPlayCard();
		this.cardsPlayedThisHand.add(card);
		Log.d("MTITUS","Cards In Hand Post Remove " + this.players.get(humanPlayer -1).getCardsInHand().toString());
	}


	protected void createAndShowErrorMessage(String title, String message) {
		
		//Borrowed title color change from the question on
		//http://stackoverflow.com/questions/14439538/how-can-i-change-the-color-of-alertdialog-title-and-the-color-of-the-line-under
		
		AlertDialog.Builder alertDialogBuilder = getNewDialogBuilder();
		alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF0000'>" + title + "</font>"));
		alertDialogBuilder
		.setMessage(message)
		.setCancelable(false)
		.setIcon(R.drawable.img_card_back_96)
		.setPositiveButton(readFromStringXML(R.string.ok),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				//This is just an alert no need to do anything other than close it.
				dialog.cancel();
			}
		  })
		  
		  ;
		
		
		//http://stackoverflow.com/questions/14439538/how-can-i-change-the-color-of-alertdialog-title-and-the-color-of-the-line-under
		//Borrowed solution from mmrmartin 
		Dialog alert = alertDialogBuilder.show();
		int dividerId = alert.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = alert.findViewById(dividerId);
		divider.setBackgroundColor(Color.RED);
		
		
	}
	
	protected void bidErrorMessage(String title, String message){
		
		//Borrowed title color change from the question on
		//http://stackoverflow.com/questions/14439538/how-can-i-change-the-color-of-alertdialog-title-and-the-color-of-the-line-under
		
		AlertDialog.Builder alertDialogBuilder = getNewDialogBuilder();
		alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF0000'>" + title + "</font>"));
		alertDialogBuilder
		.setMessage(message)
		.setCancelable(false)
		.setIcon(R.drawable.img_card_back_96)
		.setPositiveButton(readFromStringXML(R.string.ok),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				//This is just an alert no need to do anything other than close it.
				dialog.cancel();
				doBidding(humanPlayer);
			}
		  })
		  
		  ;
		
	
		
		//http://stackoverflow.com/questions/14439538/how-can-i-change-the-color-of-alertdialog-title-and-the-color-of-the-line-under
		//Borrowed solution from mmrmartin 
		Dialog alert = alertDialogBuilder.show();
		int dividerId = alert.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = alert.findViewById(dividerId);
		divider.setBackgroundColor(Color.RED);
	}
	
	protected void startNewGamePrompt(){
		AlertDialog.Builder alertDialogBuilder = getNewDialogBuilder();
		//alertDialogBuilder.setTitle("New Game?");
		alertDialogBuilder.setTitle(readFromStringXML(R.string.new_game_question_header));
		alertDialogBuilder
		//.setMessage("Do you wish to play another game of Pinochle?")
		.setMessage(readFromStringXML(R.string.new_game_question_text))
		.setIcon(R.drawable.img_card_back_96)
		//.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
		.setPositiveButton(readFromStringXML(R.string.new_game_same_settings),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				newGame();
			}
		  })
		  .setNegativeButton(readFromStringXML(R.string.new_game_different_settings), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					//This is negative button.
					openSettings();
					//dialog.cancel();
				}
			  })
		  
		  ;
		
		//http://stackoverflow.com/questions/14439538/how-can-i-change-the-color-of-alertdialog-title-and-the-color-of-the-line-under
				//Borrowed solution from mmrmartin 
			Dialog alert = alertDialogBuilder.show();
			int dividerId = alert.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
			View divider = alert.findViewById(dividerId);
			divider.setBackgroundColor(Color.RED);
	}


	protected Builder getNewDialogBuilder() {
	
		return new AlertDialog.Builder(this);
	}


	protected void addCardPassed(Card cardClicked, int locationId) {
		
		Log.d("DECK", "Hi setting");
		if (this.cardsHumanPassed.size() < locationId+1){
			this.cardsHumanPassed.add(cardClicked);
		}
		else {
			this.cardsHumanPassed.set(locationId, cardClicked);
		}
		Log.d("DECK", "Done setting");
	}
	
	
	public void btn_show_last_trick_click(View v){
		Log.d("DECK", "Show Last TrickButton clicked.");
		
		Button showHideTrickButton = (Button) findViewById(R.id.btn_show_last_trick);
    	if ((showHideTrickButton.getText().equals(readFromStringXML(R.string.show_last_trick)))){
    		int playerNumber=1;
    		for(Card card: this.cardsInLastTrick){
    			ImageView cardPlayedByThisPlayer = (ImageView) findViewById(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + "" + playerNumber + readFromStringXML(R.string.card_played_image), "", getPackageName()));
    			cardPlayedByThisPlayer.setImageResource(getResources().getIdentifier(card.getImageFileName(), "", getPackageName()));
    			playerNumber++;
    		}
    		hideOrShowFrame(R.id.playing_card_message, View.INVISIBLE);
    		showHideTrickButton.setText(R.string.hide_last_trick);
    	}
    	else {
    		
    		
    		for(int playerNumber=1; playerNumber<numberOfPlayers+1; playerNumber++){
    			ImageView cardViewPlayedByThisPlayer = (ImageView) findViewById(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + "" + playerNumber + readFromStringXML(R.string.card_played_image), "", getPackageName()));
    			Card cardPlayed = this.cardsInTrick.get(playerNumber -1);
    			if (cardPlayed!=null){
    				cardViewPlayedByThisPlayer.setImageResource(getResources().getIdentifier(cardPlayed.getImageFileName(), "", getPackageName()));
    			}
    			else {
    				cardViewPlayedByThisPlayer.setImageBitmap(this.myImg);
    			}
    		}
    		hideOrShowFrame(R.id.playing_card_message, View.VISIBLE);
    		showHideTrickButton.setText(R.string.show_last_trick);
    	}
	}
	
	
	public void attemptToPassCardsOpponentStyle(View v){
		if ((this.cardsHumanPassed.size() != this.constants.cardsPassBetweenOpponentsToBid)  || this.cardsHumanPassed.contains(null)){
			//createAndShowErrorMessage("Not enough cards selected", "Please choose more cards until you have chosen " +  cardsPassBetweenDummyAndBidder + " cards.");
			createAndShowErrorMessage(readFromStringXML(R.string.not_enough_cards_selected), readAndReplaceStringXML(R.string.please_choose_more_cards_until_x, "" + this.constants.cardsPassBetweenOpponentsToBid));
		}
		else {
			Log.d("DECK", "Good to go Opp Style!");


			for (Card passThisCard: this.cardsHumanPassed){
				Log.d("DECK" , "Pass these cards" + passThisCard.getCardName());
				setFirstInstanceNull(this.players.get(humanPlayer-1).getCardsInHand(),passThisCard);
			}
			
			for (Card card: this.players.get(humanPlayer -1).getCardsInHand()){
				if (card != null){
					Log.d("DECK", "After passing old: " + card.getCardName() );
				}
			}
			
			
			Log.d("DECK", "Amount of nulls: " + numberOfNulls(this.players.get(humanPlayer-1).getCardsInHand()) );
			//Remove the cards set to null as they have been passed to the partner.
			this.players.get(humanPlayer -1).getCardsInHand().removeAll(Collections.singleton(null));
			
			for (Card card: this.players.get(humanPlayer -1).getCardsInHand()){
				Log.d("DECK", "After passing: " + card.getCardName() );
			}
			
			passCards(humanPlayer + 2,this.cardsHumanPassed);
			hideThisPromptShowAllElse();
			this.humanPassedCards = true;
			Log.d("DECK", "done with passing");
			//displayTrumpAndContinue();
			doOpponentPassing();
		}
		
	}
	
	
	
	public void attemptToPassCards(View v){
		if ((this.cardsHumanPassed.size() != this.constants.cardsPassBetweenDummyAndBidder)  || this.cardsHumanPassed.contains(null)){
			//createAndShowErrorMessage("Not enough cards selected", "Please choose more cards until you have chosen " +  cardsPassBetweenDummyAndBidder + " cards.");
			createAndShowErrorMessage(readFromStringXML(R.string.not_enough_cards_selected), readAndReplaceStringXML(R.string.please_choose_more_cards_until_x, "" + this.constants.cardsPassBetweenDummyAndBidder));
		}
		else {
			Log.d("DECK", "Good to go!");


			for (Card passThisCard: this.cardsHumanPassed){
				Log.d("DECK" , "Pass these cards" + passThisCard.getCardName());
				setFirstInstanceNull(this.players.get(humanPlayer-1).getCardsInHand(),passThisCard);
			}
			
			for (Card card: this.players.get(humanPlayer -1).getCardsInHand()){
				if (card != null){
					Log.d("DECK", "After passing old: " + card.getCardName() );
				}
			}
			
			
			Log.d("DECK", "Amount of nulls: " + numberOfNulls(this.players.get(humanPlayer-1).getCardsInHand()) );
			//Remove the cards set to null as they have been passed to the partner.
			this.players.get(humanPlayer -1).getCardsInHand().removeAll(Collections.singleton(null));
			
			for (Card card: this.players.get(humanPlayer -1).getCardsInHand()){
				Log.d("DECK", "After passing: " + card.getCardName() );
			}
			
			passCards(humanPlayer + 2,this.cardsHumanPassed);
			hideThisPromptShowAllElse();
			this.humanPassedCards = true;
			Log.d("DECK", "done with passing");
			displayTrumpAndContinue();
		}
		
	}
	
	private void setFirstInstanceNull(List<Card> cardHand, Card cardToCheck) {
		int location = 0;
		
		for (Card card :cardHand) {
	    	if (card != null){
		    	if (card.getRankOfCard() == cardToCheck.getRankOfCard() && card.getCardSuit() == cardToCheck.getCardSuit()){
		    		cardHand.set(location, null);
		    		break;
		        }
	    	}
	    	location++;
	    	
	    }
	   
		
	}


	private int numberOfNulls(List<Card> cardHand){
		int nullsInHand = 0;
		for (Card card : cardHand){
			if (card == null){
				nullsInHand++;
			}
		}
		
		return nullsInHand;
	}
	
	
	private void hideThisPromptShowAllElse() {
	
		
		hideOrShowFrame(R.id.passing_card_prompt, View.INVISIBLE);
		for (int playerId = 1; playerId < numberOfPlayers+1; playerId++){
			//hideOrShowFrame(getResources().getIdentifier("@id/player" + playerId + "CardPlayed" , "", getPackageName()),View.VISIBLE);
			hideOrShowFrame(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + playerId + readFromStringXML(R.string.cardplayed) , "", getPackageName()),View.VISIBLE);
			if (playerId != humanPlayer){
				this.popupViews.get(playerId-1).setVisibility(View.VISIBLE);
				//hideOrShowFrame(getResources().getIdentifier("@id/player" + playerId + "_grid" , "", getPackageName()), View.VISIBLE);
				hideOrShowFrame(getResources().getIdentifier(readFromStringXML(R.string.at_id_slash_player) + playerId + readFromStringXML(R.string.underscore_grid) , "", getPackageName()), View.VISIBLE);
				
			}
		}
		
		
	}


	public void undoSelectedCard(View v) {
		
    	if (v.getId() == R.id.human_pass_card_1){
    		undoSelectedCard(0,(ImageView) v);
    	}
    	else if (v.getId() == R.id.human_pass_card_2){
    		undoSelectedCard(1,(ImageView) v);
    	}
    	else if (v.getId() == R.id.human_pass_card_3){
    		undoSelectedCard(2,(ImageView) v);
    	}
    	else if (v.getId() == R.id.human_pass_card_4){
    		undoSelectedCard(3,(ImageView) v);
    	}
    }

	private void undoSelectedCard(int cardPassedLoc, ImageView cardToFlip) {

		Log.d("DECK" , "Card Passed Loc: " + cardPassedLoc);
		Log.d("DECK", "ID: " + cardToFlip.getId() + " Tag: " + cardToFlip.getTag());
		if (this.cardsHumanPassed.size() >  cardPassedLoc){
			this.cardsHumanPassed.set(cardPassedLoc, null);
			//cardToFlip.setTag( getResources().getIdentifier("drawable/" + "img_card_back_96", "", getPackageName()));
			cardToFlip.setTag(R.drawable.img_card_back_96);
			Log.d("DECK","WHAT AM I: " + cardToFlip.getTag().toString());
			//cardToFlip.setImageResource( getResources().getIdentifier("drawable/" + "img_card_back_96", "", getPackageName()));
			cardToFlip.setImageResource( getResources().getIdentifier(readFromStringXML(R.string.drawable_slash) + readFromStringXML(R.string.img_card_back_96), "", getPackageName()));
		}
		//Else card is not set do nothing.
	}


	private void dealCards() {
		
		this.cardsCurrentlyDealt.clear();
		for (int player=1; player < numberOfPlayers +1; player++){
			this.players.get(player -1).getCardsInHand().clear();
			
		}
		this.handLayout.removeAllViews();
		this.player2HandLayout.removeAllViews();
		this.player3HandLayout.removeAllViews();
		this.player4HandLayout.removeAllViews();
		
		int cardsDealt = 0;
		int playerToDealTo=this.dealer+1;//Symbolizes one position to the clockwise of the dealer.
		if (playerToDealTo == numberOfPlayers +1){
			playerToDealTo =1;
		}
		
		for (Player player: this.players){
			player.getCardsInHand().clear();
		}
		
		while (cardsDealt < this.deck.size()){
			
			if (playerToDealTo > numberOfPlayers){
				playerToDealTo = 1;
			}
			
			int randomNumber = this.random.nextInt(this.deck.size());
			while (this.cardsCurrentlyDealt.contains(randomNumber)){
				randomNumber=this.random.nextInt(this.deck.size());
			}
			
			
			
			
			//Note this is minus one because lists are 0 index'd.
			players.get(playerToDealTo-1).getCardsInHand().add(deck.getCard(randomNumber));
			this.cardsCurrentlyDealt.add(randomNumber);
			playerToDealTo++;
			cardsDealt++;
		
		}
		
		//Check to see if anyone has 5 9's and no meld.
		for (Player player: this.players){
			int numberOfNines = getNineCount(player.getCardsInHand());
			if (numberOfNines >= 5){
				int meld = 0;
				for (int suit: allSuits){
					meld = determineMeld(player.getCardsInHand(), suit);
					if (meld > 0){
						break;
					}
					else{
						continue;
					}
					
				}
				
				if (meld == 0){
					//We have to redeal.
					Toast.makeText(this, readFromStringXML(R.string.five_nine_redeal_rule), Toast.LENGTH_SHORT).show();
					pauseToRedealCards(4000);
					
				}
			}
			else {
				continue;
			}
		}
		
	}

	
	
	
	private int getNineCount(List<Card> cardsInHand) {

		int countNineSpades = countNumberEqual((ArrayList<Card>)cardsInHand, new Card(nine, spadesInt));
		int countNineHearts = countNumberEqual((ArrayList<Card>)cardsInHand, new Card(nine, heartsInt));
		int countNineDiamonds = countNumberEqual((ArrayList<Card>)cardsInHand, new Card(nine, diamondsInt));
		int countNineClubs = countNumberEqual((ArrayList<Card>)cardsInHand, new Card(nine, clubsInt));
		
		return (countNineSpades + countNineHearts + countNineDiamonds + countNineClubs);
	}


	public int countNumberEqual(ArrayList<Card> cardList, Card cardToCheck) {
	    int count = 0;
	    for (Card card :cardList) {
	    	if (card!=null){
		    	if (card.getRankOfCard() == cardToCheck.getRankOfCard() && card.getCardSuit() == cardToCheck.getCardSuit()){
		        //if (card.equals(cardToCheck)) {
		          count++;
		        }
	    	}
	    }
	    return count;
	}
	
	public List<Integer> getCardLocationEqual (ArrayList<Card> cardList, Card cardToCheck){
		List<Integer> locations = new ArrayList<Integer>();
		int location = 0;
		
		for (Card card :cardList) {
	    	
	    	if (card.getRankOfCard() == cardToCheck.getRankOfCard() && card.getCardSuit() == cardToCheck.getCardSuit()){
	    		locations.add(location);
	          
	        }
	    	location++;
	    	
	    }
	    return locations;
		
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
		getMenuInflater().inflate(R.menu.pinochle_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
            openSettings();
            return true;
        case R.id.action_new_game:
            createNewGame();
            return true;
        case R.id.action_rules:
           showRules();
            return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	public void openSettings(){
    	
   	 // Load settings page
   	Intent intent = new Intent(this, Settings.class);
   	startActivity(intent);
   	
   }
	public void showRules(){

		Intent intent = new Intent(this, RulesActivity.class);
	   	startActivity(intent);
	}
   


public void createNewGame(){
   	
  	 // Load new game
  	Intent intent = new Intent(this, PinochleGame.class);
  	startActivity(intent);
  	
   }
   
	@Override
	public void setOnSubmitListener(String arg) {
		
		
		
		if (arg.equals(readFromStringXML(R.string.pass_btn_response))){
			Log.d("DECK","User passed.");
			this.playersPassed.set(0, true);
			this.humanBiddingDone = true;
			doBidding(humanPlayer+1);
		}
		else if (arg.equals(readFromStringXML(R.string.plus_one_btn_response))){
			Log.d("DECK", "User incremented.");
			this.currentBid = this.currentBid + this.constants.pointerBaseUnit;
			this.humanBiddingDone = false;
			doBidding(humanPlayer+1);
		}
		else if (arg.equals(spades)){
			
			this.trump=spades;
			displayTrumpAndContinue();
		}
		else if (arg.equals(hearts)){
			
			this.trump=hearts;
			displayTrumpAndContinue();
		}
		else if (arg.equals(diamonds)){
			
			this.trump=diamonds;
			displayTrumpAndContinue();
		}
		else if (arg.equals(clubs)){
			
			this.trump=clubs;
			displayTrumpAndContinue();
		}
		else if (arg.equals(readFromStringXML(R.string.invalid_bid))){
			invalidBidPause(readFromStringXML(R.string.invalid_reason_1));
		
			
			
		}
		else if (arg.contains(readFromStringXML(R.string.custom_bid_btn_response))){
			Log.d("DECK", arg.substring(arg.lastIndexOf(readFromStringXML(R.string.custom_bid_btn_response)) +readFromStringXML(R.string.custom_bid_btn_response).length()));
			Log.d("DECK", ""+Integer.parseInt(arg.substring(arg.lastIndexOf(readFromStringXML(R.string.custom_bid_btn_response)) +readFromStringXML(R.string.custom_bid_btn_response).length())));
			int bidFromPlayer = Integer.parseInt(arg.substring(arg.lastIndexOf(readFromStringXML(R.string.custom_bid_btn_response)) +readFromStringXML(R.string.custom_bid_btn_response).length()));
			
			//Current Bid requirement must be met or exceeded.
			if (bidFromPlayer> this.constants.winningScore + 1){
				invalidBidPause(readAndReplaceStringXML(R.string.invalid_reason_3, "" + (this.constants.winningScore+1)));
			}
			else if (bidFromPlayer>=this.currentBid){
				this.currentBid = bidFromPlayer +1*this.constants.pointerBaseUnit;//The plus one is because current bid is a forcing mechanism.
				doBidding(humanPlayer+1);
			}
			else {
				invalidBidPause(readFromStringXML(R.string.invalid_reason_2));
			}
		}
	}

	
	public void invalidBidPause(String reason){
		//bidErrorMessage(readFromStringXML(R.string.invalid_bid), "You entered an invalid bid. " + reason + " Please enter a valid bid.");
		bidErrorMessage(readFromStringXML(R.string.invalid_bid), readAndReplaceStringXML(R.string.invalid_bid_main_message, reason));
	}
	public String readFromStringXML(int id){
 		return  getResources().getString(id);
 	}
	public String readAndReplaceStringXML(int idOfMainString, String variableReplace){
		return getResources().getString(idOfMainString, variableReplace);
	}
	
	
	
}