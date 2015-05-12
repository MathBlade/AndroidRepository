package com.pi_r_squared.Pinochle.dto;

import java.util.ArrayList;
import java.util.List;


import static com.pi_r_squared.Pinochle.properties.UsefulConstants.*;

public class PublicPlayerKnowledge {

	List<Card> cardsAlreadyPlayed;
	Boolean possiblyHasSpades;
	Boolean possiblyHasHearts;
	Boolean possiblyHasDiamonds;
	Boolean possiblyHasClubs;
	Card highestPossibleSpade;
	Card highestPossibleHeart;
	Card highestPossibleDiamond;
	Card highestPossibleClub;
	List<Card> cardsInMeld;
	
	public PublicPlayerKnowledge(){
		super();
		this.cardsAlreadyPlayed = new ArrayList<Card>();
		this.cardsInMeld = new ArrayList<Card>();

		for (int suit: allSuits){
			setPossiblyHasSuit(suit, Boolean.TRUE);
			setPossiblyHighestPossibleCardOfSuit(new Card(ace, suit));
		}
		
	}
	
	public List<Card> getCardsAlreadyPlayed() {
		return cardsAlreadyPlayed;
	}

	public void setCardsAlreadyPlayed(List<Card> cardsAlreadyPlayed) {
		this.cardsAlreadyPlayed = cardsAlreadyPlayed;
	}
	
	public Boolean getPossiblyHasCardsOfSuit (int suitInt){
		if (suitInt == spadesInt){
			return getPossiblyHasSpades();
		}
		else if (suitInt == heartsInt){
			return getPossiblyHasHearts();
		}
		else if (suitInt == diamondsInt){
			return getPossiblyHasDiamonds();
		}
		else if (suitInt == clubsInt){
			return getPossiblyHasClubs();
		}
		else {
			return null;
		}
	}
	
	public void setPossiblyHasSuit(int suitInt, Boolean possibilityToHaveSuit){
		if (suitInt == spadesInt){
			setPossiblyHasSpades(possibilityToHaveSuit);
		}
		else if (suitInt == heartsInt){
			setPossiblyHasHearts(possibilityToHaveSuit);
		}
		else if (suitInt == diamondsInt){
			setPossiblyHasDiamonds(possibilityToHaveSuit);
		}
		else if (suitInt == clubsInt){
			setPossiblyHasClubs(possibilityToHaveSuit);
		}
		else {
			throw new NullPointerException();
		}
	}
	
	public Card getHighestPossibleCardOfSuit (int suitInt){
		if (suitInt == spadesInt){
			return getHighestPossibleSpade();
		}
		else if (suitInt == heartsInt){
			return getHighestPossibleHeart();
		}
		else if (suitInt == diamondsInt){
			return getHighestPossibleDiamond();
		}
		else if (suitInt == clubsInt){
			return getHighestPossibleClub();
		}
		else {
			return null;
		}
	}
	
	public void setPossiblyHighestPossibleCardOfSuit(Card cardToSet){
		
		int suitInt = cardToSet.getCardSuit();
		if (suitInt == spadesInt){
			setHighestPossibleSpade(cardToSet);
		}
		else if (suitInt == heartsInt){
			setHighestPossibleHeart(cardToSet);
		}
		else if (suitInt == diamondsInt){
			setHighestPossibleDiamond(cardToSet);
		}
		else if (suitInt == clubsInt){
			setHighestPossibleClub(cardToSet);
		}
		else {
			throw new NullPointerException();
		}
	}

	public Boolean getPossiblyHasSpades() {
		return possiblyHasSpades;
	}

	public void setPossiblyHasSpades(Boolean possiblyHasSpades) {
		this.possiblyHasSpades = possiblyHasSpades;
	}

	public Boolean getPossiblyHasHearts() {
		return possiblyHasHearts;
	}

	public void setPossiblyHasHearts(Boolean possiblyHasHearts) {
		this.possiblyHasHearts = possiblyHasHearts;
	}

	public Boolean getPossiblyHasDiamonds() {
		return possiblyHasDiamonds;
	}

	public void setPossiblyHasDiamonds(Boolean possiblyHasDiamonds) {
		this.possiblyHasDiamonds = possiblyHasDiamonds;
	}

	public Boolean getPossiblyHasClubs() {
		return possiblyHasClubs;
	}

	public void setPossiblyHasClubs(Boolean possiblyHasClubs) {
		this.possiblyHasClubs = possiblyHasClubs;
	}
	
	
	
	public PublicPlayerKnowledge(List<Card> cardsPlayerPlayed, List<Card> cardsInMeld, List<Boolean> possibilityToHaveSuits) {
		super();
		this.cardsAlreadyPlayed = cardsPlayerPlayed;
		int index=1;
		for (Boolean possibilityToHaveSuit: possibilityToHaveSuits){
			setPossiblyHasSuit(index, possibilityToHaveSuit);
			index++;
		}
		this.cardsInMeld = cardsInMeld;
	}


	public PublicPlayerKnowledge(PublicPlayerKnowledge ppk) {
		super();
		this.cardsAlreadyPlayed = ppk.cardsAlreadyPlayed;
		this.possiblyHasSpades = ppk.possiblyHasSpades;
		this.possiblyHasHearts = ppk.possiblyHasHearts;
		this.possiblyHasDiamonds = ppk.possiblyHasDiamonds;
		this.possiblyHasClubs = ppk.possiblyHasClubs;
		this.cardsInMeld = ppk.cardsInMeld;
	}

	public List<Card> getCardsInMeld() {
		return cardsInMeld;
	}

	public void setCardsInMeld(List<Card> cardsInMeld) {
		this.cardsInMeld = cardsInMeld;
	}

	public boolean hasCardInMeld(Card card) {
		
		for (Card cardInMeld: this.cardsInMeld){
			if (card.getCardName().equals(cardInMeld.getCardName())){
				return true;
			}
		}
		return false;
	}

	public Card getHighestPossibleSpade() {
		if (!this.possiblyHasSpades){
			return null;
		}
		return highestPossibleSpade;
	}

	public void setHighestPossibleSpade(Card highestPossibleSpade) {		
		this.highestPossibleSpade = highestPossibleSpade;
	}

	public Card getHighestPossibleHeart() {
		if (!this.possiblyHasHearts){
			return null;
		}
		return highestPossibleHeart;
	}

	public void setHighestPossibleHeart(Card highestPossibleHeart) {
		this.highestPossibleHeart = highestPossibleHeart;
	}

	public Card getHighestPossibleDiamond() {
		if (!this.possiblyHasDiamonds){
			return null;
		}
		return highestPossibleDiamond;
	}

	public void setHighestPossibleDiamond(Card highestPossibleDiamond) {
		this.highestPossibleDiamond = highestPossibleDiamond;
	}

	public Card getHighestPossibleClub() {
		if (!this.possiblyHasClubs){
			return null;
		}
		return highestPossibleClub;
	}

	public void setHighestPossibleClub(Card highestPossibleClub) {
		this.highestPossibleClub = highestPossibleClub;
	}
	
	public int timesPlayedGivenCard(Card card){
		int timesPlayed = 0;
		for (Card cardAlreadyPlayed: this.getCardsAlreadyPlayed()){
			if (card.getCardName().equals(cardAlreadyPlayed.getCardName())){
				timesPlayed++;
			}
		}
		return timesPlayed;
	}
	public int timesInMeld(Card card){
		int timesPlayed = 0;
		for (Card cardInMeld: this.getCardsInMeld()){
			if (card.getCardName().equals(cardInMeld.getCardName())){
				timesPlayed++;
			}
		}
		return timesPlayed;
	}
	
	
	
}
