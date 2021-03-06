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
 *
 *
 * 11/5/2021
 *
 * performs all the functions and steps when merging two hotel chains.
 *
 * @since 1.0.0
 * @author Michael Collier, Emily Elzinga
 */


package AcquireProject;

import lombok.Getter;

import java.util.*;

public class Merger {
    @Getter private HotelChain acquiringChain;
    @Getter private HotelChain acquiredChain;
    private GameBoard gameBoard;

    private List<Player> playersToMakeDecision;

    Merger(HotelChain acquiringChain, HotelChain acquiredChain, GameBoard gameBoard){
        this.acquiringChain = acquiringChain;
        this.acquiredChain = acquiredChain;
        this.playersToMakeDecision = findPlayers();
        this.gameBoard = gameBoard;
    }

    /**
     * gives the majority and minority shareholders their bonus at the beginning of the merge
     */
    public void giveShareholderBonus(){

        List<Player> majority = getMajorityShareHolder();
        int majorityBonus = acquiredChain.getMajorityShareholderBonus();
        for(Player p : majority){
            p.modifyBalance(majorityBonus / majority.size());
        }
        List<Player> minority = getMinorityShareHolder(majority);
        int minorityBonus = acquiredChain.getMinorityShareholderBonus();
        if(minority.size() == 0){
            for(Player p : majority){
                p.modifyBalance(minorityBonus / 100 / majority.size() * 100);
            }
        }else{
            for(Player p : minority){
                p.modifyBalance(minorityBonus / 100 / minority.size() * 100);
            }
        }
    }

    private List<Player> getMajorityShareHolder(){
        return getMinorityShareHolder(new ArrayList<Player>());
    }

    private List<Player> getMinorityShareHolder(List<Player> majority){
        List<Player> minority = new ArrayList<>();
        int maxStock = 1;

        Map<Player, Integer> profile = StockProfiler.instance().createChainProfile(acquiredChain);

        for(Player p : profile.keySet()){
            if(majority.contains(p)){
                continue;
            }

            int stockAmount = profile.get(p);
            if(stockAmount > maxStock){
                minority.clear();
                minority.add(p);
                maxStock = stockAmount;
            }else if(stockAmount == maxStock){
                minority.add(p);
            }
        }

        return minority;
    }

    private List<Player> findPlayers(){
        Set<Player> keys = StockProfiler.instance().createChainProfile(acquiredChain).keySet();
        List<Player> players = new ArrayList<>();

        for(Player p : keys){
            players.add(p);
        }

        return players;
    }

    /**
     * checks if there are players that needed handled
     *
     * @return true if there are still players to make choices about their stocks
     */
    public Boolean morePlayersToHandle(){
        return playersToMakeDecision.size() > 0;
    }

    /**
     * steps the next player in the list of those to be handled
     */
    public void goToNextPlayer(){
        playersToMakeDecision.remove(0);
    }

    /**
     * @return the name of the player currently being handled
     */
    public String getPlayerName(){
        return playersToMakeDecision.get(0).getPlayerName();
    }

    /**
     * @return the omount of stock the current player owns in the hotel chain being acquired
     */
    public int getPlayerStockCount(){
        return StockProfiler.instance().createPlayerProfile(playersToMakeDecision.get(0)).get(acquiredChain.getName());
    }

    /**
     * @return the price of stock in the acquired chain
     */
    public int getStockPrice(){
        return acquiredChain.getStockPrice();
    }

    /**
     * sells one stock back to the chain being acquired
     */
    public void sellStock(){
        acquiredChain.buyStock(playersToMakeDecision.get(0));
    }

    /**
     * trades two stock in the acquired chain for one in the acquiring chain
     */
    public void tradeStock(){
        Player player = playersToMakeDecision.get(0);
        acquiredChain.takeStock(player);
        acquiredChain.takeStock(player);
        acquiringChain.giveStock(player);
    }

    /**
     * merges the two chains tiles together
     */
    public void mergeChains(){
        gameBoard.mergeChains(this);
    }
}
