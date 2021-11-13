package AcquireProject;

import lombok.Getter;
import lombok.Setter;

public class Stock {

    @Getter private String chain;
    @Getter private int chainStockPrice;
    @Getter @Setter private Player owner;

    /**
     * stores which chain and which player own each stock in the game
     *
     * @param chain the hotel chain that owns the stock
     */
    public Stock(String chainName, int price){
        this.chain = chainName;
        this.chainStockPrice = price;
    }

    /**
     * calculate and return the purchase value of the stock
     *
     * @return the value of the stock based on the chain size
     */
    public int getValue() {
        return chainStockPrice;
    }

}
