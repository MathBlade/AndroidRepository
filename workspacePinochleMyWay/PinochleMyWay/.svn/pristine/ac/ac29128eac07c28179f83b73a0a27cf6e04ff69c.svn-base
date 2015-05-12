package com.pi_r_squared.Pinochle.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.pi_r_squared.Pinochle.properties.UsefulConstants.*;

public class PublicDeckKnowledge {

	List<Card> cardsAlreadyPlayed;
	

	
	public PublicDeckKnowledge(){
		this.cardsAlreadyPlayed = new ArrayList<Card>();
	}
	
	public List<Card> getCardsAlreadyPlayed() {
		return cardsAlreadyPlayed;
	}

	public void setCardsAlreadyPlayed(List<Card> cardsAlreadyPlayed) {
		this.cardsAlreadyPlayed = cardsAlreadyPlayed;
	}
	public List<Card> getDeckRemaining(){
		List<Card> deckRemaining = new ArrayList<Card>();
		for (int suit: allSuits){
			deckRemaining.addAll(cardsRemainingOfSuit(suit));
		}
		return deckRemaining;
	}
	
	public List<Card> amountOfSpecificCardInPlay(Card card){
		List<Card> specificAmountOfCardRemaining = new ArrayList<Card>();
		for(Card cardInSuit: cardsRemainingOfSuit(card.getCardSuit())){
			if (card.getRankOfCard() == cardInSuit.getRankOfCard()){
				specificAmountOfCardRemaining.add(card);
			}
		}
		return specificAmountOfCardRemaining;
	}
	
	public Card highestCardOfSuitRemaining(int suitId){
		
		//warning can return null;
		Card maxCard = null;
		List<Card> cardsRemainingOfSuit = cardsRemainingOfSuit(suitId);
		for (Card card: cardsRemainingOfSuit){
			if (card.getRankOfCard() == ace){
				return card;
			}
			else {
				if (maxCard != null){
					if (card.getRankOfCard() > maxCard.getRankOfCard()){
						maxCard = card;
					}
				}
				else {
					maxCard = card;
				}
			}
		}
		
		return maxCard;
	}
	
	public Card lowestCardOfSuitRemaining(int suitId){
		//warning can return null;
		Card minCard = null;
		List<Card> cardsRemainingOfSuit = cardsRemainingOfSuit(suitId);
		for (Card card: cardsRemainingOfSuit){
			if (card.getRankOfCard() == nine){
				return card;
			}
			else {
				if (minCard != null){
					if (card.getRankOfCard() < minCard.getRankOfCard()){
						minCard = card;
					}
				}
				else {
					minCard = card;
				}
			}
		}
		
		return minCard;
	}
	
	public List<Card> cardsRemainingOfSuit(int suitId){
		Deck deck = new Deck();
		List<Card> allCardsOfSuit = getCardsOfSuit(deck.getCardsInDeck(), suitId);
		List<Card> cardsPlayedOfSuit = getCardsOfSuit(this.cardsAlreadyPlayed, suitId);
		
		List<Card> leftovers = allCardsOfSuit;
		int indexInDeck=0;
		
		for (Card cardAlreadyPlayed: cardsPlayedOfSuit){
			indexInDeck=0;
			for (Card aCardOfSuit : leftovers){
				if (aCardOfSuit !=null){
					if (cardAlreadyPlayed.getCardName().equals(aCardOfSuit.getCardName())){
						leftovers.set(indexInDeck, null);
						break;
					}
				}
				indexInDeck++;
			}
		}
		
		leftovers.removeAll(Collections.singleton(null));
		return leftovers;
		
		
		
	}

	private List<Card> getCardsOfSuit(List<Card> cards, int suitId) {
		List<Card> cardsOfSuit = new ArrayList<Card>();
		for (Card card: cards){
			if (card.getCardSuit() == suitId){
				cardsOfSuit.add(card);
			}
		}
		
		return cardsOfSuit;
	}
	
}
