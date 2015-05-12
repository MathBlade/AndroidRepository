package com.pi_r_squared.Pinochle.dto;

import static com.pi_r_squared.Pinochle.properties.UsefulConstants.*;

import java.util.ArrayList;
import java.util.List;

public class Player {
	int playerId;
	List<Card> cardsInHand = new ArrayList<Card>();
	String playerDesignation;
	String role;
	int bidStyle;
	
	public Player(int playerId){
		this.setPlayerId(playerId);
		this.cardsInHand.clear();
		this.setPlayerDesignation(playerDesignation(playerId));
	}
	
	
	private String playerDesignation(int playerId) {
		switch (playerId) {
        case 1:
           return south;
        case 2:
           return west;
        case 3:
            return north;
        case 4:
            return east;
        default:
            return null;
		}
	}


	public List<Card> getCardsInHand() {
		return cardsInHand;
	}
	public void setCardsInHand(List<Card> cardsInHand) {
		this.cardsInHand = cardsInHand;
	}





	public int getPlayerId() {
		return playerId;
	}





	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}


	public String getPlayerDesignation() {
		return playerDesignation;
	}


	public void setPlayerDesignation(String playerDesignation) {
		this.playerDesignation = playerDesignation;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public int getBidStyle() {
		return bidStyle;
	}


	public void setBidStyle(int bidStyle) {
		this.bidStyle = bidStyle;
	}

}
