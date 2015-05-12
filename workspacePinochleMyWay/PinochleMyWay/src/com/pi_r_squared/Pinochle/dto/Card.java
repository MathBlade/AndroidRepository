package com.pi_r_squared.Pinochle.dto;

//import com.pi_r_squared.Pinochle.R;
import static com.pi_r_squared.Pinochle.properties.UsefulConstants.*;

import java.util.Locale;






public class Card {
	
	String cardName;
	int rankOfCard;
	int cardSuit;
	String suitString;
	String imageFileName;
	
    
	
	
	
	
	public Card(int rankOfCard, int cardSuit) {
		super();
		this.rankOfCard = rankOfCard;
		this.cardSuit = cardSuit;
		this.suitString = getSuitString(cardSuit);
		this.cardName = getSuitRank(rankOfCard) + " " + OF + " " + this.suitString;//had to add spaces since strings.xml chops them. Also modified strings.xml to not have spaces since they were ignored.
		//this.imageFileName = "drawable/" + getSuitRank(rankOfCard).toLowerCase(Locale.US) + "_" + this.suitString.toLowerCase();
		this.imageFileName = drawableSlash + getSuitRank(rankOfCard).toLowerCase(Locale.US) + underscore + this.suitString.toLowerCase();
	}


	public Card(Card card) {
		this.rankOfCard = card.rankOfCard;
		this.cardSuit = card.cardSuit;
		this.suitString = card.suitString;
		this.cardName = card.cardName;
		this.imageFileName = card.imageFileName;
		
	}


	public String getSuitString(int cardSuit){
		
		switch (cardSuit) {
        case clubsInt:
           return clubs;
        case diamondsInt:
           return diamonds;
        case heartsInt:
            return hearts;
        case spadesInt:
            return spades;
        default:
            return null;
		}
		
	}
	
	public String getSuitRank(int suitRank){

		switch (suitRank) {
        case nine:
           return nineString;
        case ten:
           return tenString;
        case jack:
            return jackString;
        case queen:
            return queenString;
        case king:
            return kingString;
        case ace:
            return aceString;
        default:
            return null;
		}
	}
	
	
	
	
	//Getters setters

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}



	public int getRankOfCard() {
		return rankOfCard;
	}



	public void setRankOfCard(int rankOfCard) {
		this.rankOfCard = rankOfCard;
	}



	public int getCardSuit() {
		return cardSuit;
	}



	public void setCardSuit(int cardSuit) {
		this.cardSuit = cardSuit;
	}



	public String getSuitString() {
		return suitString;
	}



	public void setSuitString(String suitString) {
		this.suitString = suitString;
	}


	public String getImageFileName() {
		return imageFileName;
	}


	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}


	

	@Override
	public String toString(){
	    return this.cardName;
	}
	
	

}