/*
 * Author: Ayodeji I#######
 * Email:    ########
 * Course: CS200, Fall 2025
 * Assignment: CS200 Project 2 - Blackjack
 * Citations: N/A
 */


import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;




/**
 * The Blackjack class serves as the main functionality for the Blackjack game, calling upon Player and Card classes to manage game state and player actions.
 * Blackjack is a classic card game where players compete to have a hand closer to 21 than the dealer without exceeding it.
 * To beat this game, you must reach $1000 starting with $100 by winning bets against the banker.
 * @author Ayodeji I#######
 */

public class Blackjack {


/**
 * This main method serves as the main entry point for the Blackjack game.
 * The method prompts the user to enter their name and initializes their starting money using the Player class.
 * The game consists of multiple rounds where the player can place bets and choose to hit or stand.
 * Once the player decides to stand or exceeds 21, the banker's turn begins, following standard Blackjack rules.
 * The game continues until the player either runs out of money or reaches $1000.
 * @param name The name of the player
 * @param banker The Card dealer player
 * @param betAmount The amount of money the player bets each round
 * @param validbet Checks for valid bet input
 * @param card The card drawn by the player or banker
 * @param round Boolean to control each card dealing round
 * @param stood Boolean to check if player has chosen to stand and disable further hits
 * @param choice The player's choice to hit or stand
 */  
  public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);

        System.out.println("Welcome to Blackjack! To begin, What is your name?");
        String name = scnr.nextLine();
        //Creates player and banker objects (with 100$ starting money for player)
        Player one = new Player(name,100);
        Player banker = new Player("Banker", 0);

        System.out.println(one.getName() + " You will start with $100, to win the game you need to win $1000");

        int betAmount = 0;


        while ((one.getMoney() > 0) && (one.getMoney() < 1000)) {
            System.out.println("You currently have: $" + one.getMoney() + "\nHow much would you like to bet?");

            boolean validbet = true;
            while (validbet) {
                if (scnr.hasNextInt()) {
                    betAmount = scnr.nextInt();
                    if ((betAmount > one.getMoney()) || (betAmount <= 0)) {
                        System.out.println("Please enter a valid bet amount. 1 - $" + one.getMoney());
                    } else {
                        validbet = false;
                    }
                } else {
                    System.out.println("Please enter a valid bet amount. 1 - $" + one.getMoney());
                    scnr.next();
                }
            }
            System.out.println("You have bet $" + betAmount);
            System.out.println("--------------------------");


            //Initial card draw
            Card card = banker.drawcard();
            banker.addHand(card);
            System.out.println("The Banker draws a " + card.getValue());
            card = one.drawcard();
            one.addHand(card);
            System.out.println(one.getName()+" drew a " + card.getValue());

            Boolean round = true;
            Boolean stood = false;
            while (round) {
                if (stood == false) {
                    System.out.println("Would you like to (1) Hit or (2) Stand?");
                    boolean validChoice = true; 
                    int choice = 0;
                    while (validChoice) {
                        if (scnr.hasNextInt()) {
                            int tempchoice = scnr.nextInt();
                            if (tempchoice != 1 && tempchoice != 2) {
                                System.out.println("Please enter a valid choice: (1) Hit or (2) Stand");
                            } else {
                                choice = tempchoice;
                                validChoice = false;
                            }
                        } else {
                            System.out.println("Please enter a valid choice: (1) Hit or (2) Stand");
                            scnr.next();
                        }
                    }


                    if (choice != 1 && choice != 2) {
                        System.out.println("Please enter a valid choice: (1) Hit or (2) Stand");
                        continue;
                    } else if (choice == 1) { // hit
                        card = one.drawcard(); // Player draws a card
                        if ((card.getValue() == 11) && (one.getValue() + 11 > 21)) {
                            card = new Card(1, "Ace");
                        } else if ((card.getValue() == 1) && (one.getValue() + 11 <= 21)) {
                            card = new Card(11, "Ace");
                        }
                        one.addHand(card);
                        System.out.println(one.getName()+" drew a " + card.getValue());

                        if (banker.getValue() < 17) { // Banker draws a card if under 17
                            card = banker.drawcard();
                            banker.addHand(card);
                            System.out.println("The Banker draws a " + card.getValue()); 
                        } else {
                            System.out.println("The Banker stands.");
                        }

                    } else if (choice == 2) { // stand
                        stood = true;
                        if (banker.getValue() < 17) { // Banker draws a card if under 17
                            card = banker.drawcard();
                            banker.addHand(card);  
                            System.out.println("The Banker draws a " + card.getValue()); 
                        } else {
                            if (banker.getValue() > 21) {

                            } else {
                            System.out.println("The Banker stands.");
                            }
                        }
                    }

                System.out.println("--------------------------");
                System.out.println("Your hand value is: " + one.getValue());
                System.out.println("The Banker's hand value is: " + banker.getValue());

                if ((one.getValue() >= 21) || (banker.getValue() >= 21)) { // Check for busts or 21
                    round = false;
                }
            } else if (stood == true) {
                if (banker.getValue() < 17) { // Banker draws a card if under 17
                    card = banker.drawcard();
                    banker.addHand(card);  
                    System.out.println("The Banker draws a " + card.getValue()); 
                } else {
                    System.out.println("The Banker stands.");
                    round = false;
                }
                
            }
        }
        // Determine winner
        if ((one.getValue() > 21) && (banker.getValue() <= 21)) {
            System.out.println("You busted! You lose $" + betAmount);
            one.updateMoney(-betAmount);
        } else if ((one.getValue() > 21) && (banker.getValue() > 21)){
            System.out.println("Both players busted! It's a tie, your bet is returned.");
        } else if ((banker.getValue() > 21) && (one.getValue() <= 21)) {
            System.out.println("The Banker busted! You win $" + (betAmount * 2));
            one.updateMoney(betAmount * 2);
        }else if (one.getValue() == 21) {
            System.out.println("Blackjack! You win $" + (betAmount * 2));
            one.updateMoney(betAmount * 2);
        }else if (one.getValue() > banker.getValue()) {
            System.out.println("You win! You win $" + betAmount * 2);
            one.updateMoney(betAmount * 2);
        } else if (one.getValue() < banker.getValue()) {
            System.out.println("You lose! You lose $" + betAmount);
            one.updateMoney(-betAmount);
        } else if (one.getValue() == banker.getValue()) {
            System.out.println("It's a tie! Your bet is returned.");
        } 
        // Clear hands for next round
        one.clearHand();
        banker.clearHand();
        System.out.println("--------------------------");
        }

        if (one.getMoney() >= 1000) {
            System.out.println("Congratulations " + one.getName() + "! You have reached $1000 and won the game!");
        } else {
            System.out.println(one.getName() + ", You have ran out of money and lost. Better luck next time! 99% of gamblers quit before they win big!");
        }
        scnr.close();
    }
    
}


/**
 * The Player class represents a player/dealer in the Blackjack game
 * The class manages the player's name, money, and hand of cards.
 * It provides methods to draw cards, update money, and calculate the total value of the player's hand.
 * @author (Ayodeji I#######)
 */
class Player {

    private String name;
    private int money;
    private ArrayList<Card> hand;

    /**
     * Constructor to initialize a Player object with a name and starting money.
     * @param name The name of the player
     * @param Money The starting amount of money for the player
     */
    Player (String name, int Money) {
        this.name = name;
        this.money = Money;
        this.hand = new ArrayList<Card>();
        
    }
    /**
     * Draws a random card for the player.
     * The card value ranges from 1 to 13. Face cards are valued at 10, and Aces can be valued at 1 or 11 based on the player's hand.
     * @return newCard A Card object representing the drawn card
     */
    public Card drawcard() {
        Random rnd = new Random();
        int cardValue = rnd.nextInt(13) + 1;
        String cardName = "";
        if (cardValue == 1) {
            cardName = "Ace";
        } else if (cardValue == 11) {
            cardName = "Jack";
            cardValue = 10;
        } else if (cardValue == 12) {
            cardName = "Queen";
            cardValue = 10;
        } else if (cardValue == 13) {
            cardName = "King";
            cardValue = 10;
        } else {
            cardName = Integer.toString(cardValue);
        }

        Card newCard = new Card(cardValue, cardName);
        return newCard;  
    }

    /** Returns the current amount of money the player has.
     * @return money The current amount of money the player has
     */
    public int getMoney() {
        return this.money;
    }
    
    /** Returns the name of the player.
     * @return name The name of the player
     */
    public String getName() {
        return this.name;
    }
    /** Updates the player's money by an amount.
     * @param amount The amount to add or subtract from the player's money.
     */
    public void updateMoney(int amount) {
        this.money += amount;
    }
    /** Adds a card to the player's hand.
     * @param card The Card object to be added to the player's hand
     */
    public void addHand(Card card) {
        hand.add(card);
    }
    /** Clears the player's hand of cards.
     */
    public void clearHand() {
        hand.clear();
    }
    /** Calculates the total value of the player's hand.
     * @return points The total value of the player's hand
     */
    public int getValue() {
        int points = 0;
        for (Card c : hand) {
            points += c.getValue();
        }
        return points;
    }
}

/**
 * The Card class represents a playing card for the Blackjack game.
 * Each card has a value and a name.
 * @author (Ayodeji I#######)
 */
class Card {
    private int value;
    private String name;
    /**
     * Constructor to initialize each Card object with a value and name.
     * @param value The value of the card
     * @param name The name of the card
     */
    Card (int value, String name){
        this.value = value;
        this.name = name;
    }
    /** Returns the value of the card
     * @return value The value of the card
     */
    public int getValue() {
        return this.value;
    }
    /** Returns the name of the card
     * @return name The name of the card
     */
    public String getName() {
        return this.name;
    }
}

