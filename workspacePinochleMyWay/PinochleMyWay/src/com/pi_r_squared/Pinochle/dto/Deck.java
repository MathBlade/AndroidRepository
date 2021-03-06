package com.pi_r_squared.Pinochle.dto;

import java.util.ArrayList;
import java.util.List;

import static com.pi_r_squared.Pinochle.properties.UsefulConstants.*;


public final class Deck {

	List<Card> cardsInDeck= new ArrayList<Card>();
	

	public Deck(){
		
		//Log.d("START OF DECK CREATION", "START");
		
		for (int uniqueSet=1; uniqueSet<  setsOf24+1; uniqueSet++){
			create24cards();
		}
		
		
		//Log.d("END OF CREATION", "END");
		
		
	}
	
	public void create24cards(){
		for (int cardRank = nine; cardRank < ace + 1; cardRank++){
			
			for (int suit=clubsInt; suit < spadesInt + 1; suit++){
		
				Card newCard = new Card(cardRank,suit);
			
				this.cardsInDeck.add(newCard);
			}
		}
	}
	
	public Card getCard(int card){
		return this.getCardsInDeck().get(card);
	}
	
	public int size(){
		return this.getCardsInDeck().size();
	}

	public List<Card> getCardsInDeck() {
		return cardsInDeck;
	}

	public void setCardsInDeck(List<Card> cardsInDeck) {
		this.cardsInDeck = cardsInDeck;
	}

	

	

}
