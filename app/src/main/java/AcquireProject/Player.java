/**
 * MIT License
 *
 * Copyright (c) 2021 Michael Collier, Emily Elzinga, Benjamin Keninger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.

 * Player class that holds methods and attributes relevant to player objects
 *
 * @author Benjamin, Emily, Michael
 *
 * @since 1.0.0
 */

package AcquireProject;
import java.util.ArrayList;
import java.util.List;

public class Player {


    private String playerName;
    private int balance;
    private List<Tile> playerTiles;
    private List<Stock> playerStocks;

    /**
     * Constructor that creates a player object with a name and starting money
     *
     * @param name the name of the player
     *
     * @param startingTiles a list of 6 tiles that will be the player's starting tiles
     */
    Player(String name, List<Tile> startingTiles) {
        this.playerTiles = startingTiles;
        this.playerName = name;
        this.balance = 6000;
        this.playerStocks = new ArrayList<>();
    }

    /**
     * A method to add a tile to the player's list of tiles
     *
     * @param tile that will be added to the player's list of tiles
     */
    public void addTile(List<Tile> tile){
        playerTiles.addAll(tile);
    }

    /**
     * A method to remove a tile from the player's list of tiles
     *
     * @param tile that will be removed from the player's list of tiles
     */
    public void removeTile(Tile tile){
        playerTiles.remove(tile);
    }

    /**
     * A method to get the player's name
     *
     * @return the name of the player
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * A method to get the player's balance
     *
     * @return the balance of the player
     */
    public int getBalance() {
        return balance;
    }

    /**
     * A method to change the balance of the player
     *
     * @param change the amount that the balance will get changed by, can be positive or negative
     */
    public void modifyBalance(int change){
        balance += change;
    }

    /**
     * A method to get a list of the player's tiles
     *
     * @return a list of player's tiles
     */
    public List<Tile> getPlayerTiles() {
        return playerTiles;
    }

    /**
     * A method to get a list of the player's stocks
     *
     * @return a list of player's stocks
     */
    public List<Stock> getPlayerStocks() {
        return playerStocks;
    }

    /**
     * A method to add Stock to the player's list of stocks
     *
     * @param stock the Stock object that will be added to the player's list of stocks
     */
    public void addStock(Stock stock){
        playerStocks.add(stock);
    }

    /**
     * A method to remove stock from a player's list of stocks
     *
     * @param stock the Stock object that will be removed from the player's list of stocks
     */
    public void removeStock(Stock stock){
        playerStocks.remove(stock);
    }

    /**
     * replaces the players hand with a new set of tiles
     *
     * @param newHand the new list of tiles
     */
    public void giveNewHand(List<Tile> newHand){
        playerTiles = newHand;
    }

}
