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
 * The game board class has the characteristics of a physical acquire game
 * board, with a matrix representing the physical board.
 *
 * @author Emily Elzinga
 * @version v1.0.0
 * @since 11/4/2021
 */

package AcquireProject;


import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;


class GameBoard{


   private List<Tile> playedTiles;
   private ArrayList<ArrayList<Tile>> board;

   @Getter private List<HotelChain> unfoundedChains;
   @Getter private List<HotelChain> foundedChains;

  private Founder currentFounder = null;
  private List<Merger> mergersToHandle;


   /**
    * Initializes the game board. The board matrix will always have a height of 9 and a
    * length of 12, so it there are no parameters for it in the constructor.
    * @param unfoundedChains list of chains that haven't been founded yet
    * @param foundedChains list of chains that are founded
    * @author Emily Elzinga
    */
   public GameBoard(List<HotelChain> unfoundedChains, List<HotelChain> foundedChains) {
      this.board = new ArrayList<>();
      for (int i = 0; i < 12; i++){
         ArrayList<Tile> y = new ArrayList<>();

         for (int j = 0; j < 9; j++){
            y.add(j, null);
         }
         board.add(i, y);
      }

      this.playedTiles = new ArrayList<>();
      this.unfoundedChains = unfoundedChains;
      this.foundedChains = foundedChains;

      this.mergersToHandle = new ArrayList<>();
   }

    /**
     * Constructor that incorperates a list of unfounded chains and creates a list of all hotel chains
     *
     * @param unfoundedChains that keeps track of hotel chains that haven't been founded yet
     */
   public GameBoard(List<HotelChain> unfoundedChains){
      this(unfoundedChains, new ArrayList<HotelChain>());
   }

   public void addToUnfoundedChains(HotelChain chain){
       unfoundedChains.add(chain);
   }

    /**
     * a method that adds a tile to the list of player's tiles
     *
     * @param playedTile that is added to the player's list of tiles
     */
   public void addToPlayedTiles(Tile playedTile) {
      playedTiles.add(playedTile);
   }

    /**
     * retrieves the matrix of the board in a 2-by-2 matrix of Tile objects
     *
     * @return the board matrix
     */
   public ArrayList<ArrayList<Tile>> getGameBoardMatrix(){
      return board;
   }

    /**
     * a method that retrieves the list of all played tiles on the board
     *
     * @return list of played tiles
     */
   public List<Tile> getPlayedTiles() {
      return playedTiles;
   }

   /**
    * This method uses a breadth-first-search algorithm to count the number of tiles
    * part of a one that has just been laid. Because the new tile is part of the hotel chain,
    * the method counts it as well.
    *
    * @param queue List of nodes for possible neighbors for the tile just laid
    * @param visited Boolean matrix that has a bool value of whether that node has been visited
    * @return the newly laid tile's neighbors.
    * @author Emily Elzinga
    */
   private List<Tile> breadthFirstSearch(
           Queue<List<Integer>> queue,
           boolean[][] visited
   ) {
      ArrayList<Tile> neighborTiles = new ArrayList<>();
      int H = board.size();
      int L = board.get(0).size();

      while (!queue.isEmpty()) {
         List<Integer> x = queue.remove();
         int px = x.get(0);
         int py = x.get(1);


         if(px<0 || py<0 || px>=H || py>=L ||
                 visited[px][py] || board.get(px).get(py) == null)
            continue;

         visited[px][py]=true;

         neighborTiles.add(board.get(px).get(py));

         queue.add(Arrays.asList(px + 1,py)); //go right
         queue.add(Arrays.asList(px - 1,py)); //go left
         queue.add(Arrays.asList(px,py - 1)); //go up
         queue.add(Arrays.asList(px,py + 1)); //go down
      }

      return neighborTiles;
   }


   /**
    * Setup necessary for the breadthFirstSearch method
    * @param tile string representation of the newly laid tile
    * @return list of all of the newly laid tile's neighbors, including itself
    * @author Emily Elzinga
    */
   private List<Tile> Scout(Tile tile){
       board.get(tile.getCoordinates().get(0)).set(tile.getCoordinates().get(1), tile);
      Queue<List<Integer>> neighbors = new LinkedList<>();
      neighbors.add(tile.getCoordinates());
      boolean[][] visited = new boolean[board.size()][board.get(0).size()];

      return breadthFirstSearch(neighbors,visited);
   }

   /**
    * Loops through the given tile clump list and cross-references the tile
    * chain name with elements of the founded chain list. When found, add the that chain to
    * the frequencyList
    * @param neighbors list of tiles in the clump
    * @return ordered list of hotel chains in the clump ordered in frequency
    * @author Emily Elzinga
    */
   public List<HotelChain> modeInNeighborList(List<Tile> neighbors) {
      String chainName;

      Map<HotelChain, Integer> frequencyMap = new HashMap<>();
      for (int i = 0; i < neighbors.size(); i++) {
         chainName = neighbors.get(i).getChainName();
         for (HotelChain chain : foundedChains) {
            if (chainName.equals(chain.getName())){

               if (frequencyMap.get(chain) != null) {
                  int count = frequencyMap.get(chain);
                  count++;
                  frequencyMap.put(chain, count);
               } else {
                  frequencyMap.put(chain, 1);
               }
            }
         }

      }

      return frequencyMap.entrySet().stream()
              .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
              .map(Map.Entry::getKey)
              .collect(Collectors.toList());
   }


    /**
     * Determines whether a safe hotel chain is trying to merge another safe hotel chain
     * @param frequencyChains list of hotel chains in order of frequency that they appear in the clump
     * @return whether the merge can happen or not
     */
   public boolean mergeIsLegal(List<HotelChain> frequencyChains){
       boolean legalMerge = true;
       for (int i = 1; i < frequencyChains.size(); i++) {
           if (frequencyChains.get(i).isSafe()) legalMerge = false;
       }
       return legalMerge;
   }

    /**
     * places a tile on the board and checks if a founding or merger are needed
     *
     * @param tile the tile that will be added to the board
     */
   public void placeTile(Tile tile){
      playedTiles.add(tile);
      List<Tile> chain = Scout(tile);
      List<HotelChain> modeChain = modeInNeighborList(chain);

      if(modeChain.size() == 0 && chain.size() > 1 && !unfoundedChains.isEmpty()){
         currentFounder = new Founder(chain);
      }else if(modeChain.size() == 1){
          for(Tile t : chain){
              t.setChainName(modeChain.get(0).getName());
              if(!modeChain.get(0).getTiles().contains(t)){
                  modeChain.get(0).getTiles().add(t);
              }
          }
      }else if (modeChain.size() > 1 /*&& mergeIsLegal(modeChain)*/) {
         HotelChain mode = modeChain.remove(0);
         for (HotelChain acquiredChain : modeChain){
            Merger merger = new Merger(mode, acquiredChain, this);
            mergersToHandle.add(merger);
         }
      }

   }


    /**
     * checks if there is a merger that has not yet been handled
     *
     * @return true if there is a merger waiting to be handled
     */
   public Boolean mergeNeeded(){
       if(mergersToHandle.size() > 0){
           return true;
       }
       return false;
   }

    /**
     * a method that finds and returns the merger object currently being used
     *
     * @return Merger object that is currently occurring
     */
   public Merger getCurrentMerger(){
       return mergersToHandle.remove(0);
   }

    /**
     * merges two chains and updates all the needed fields
     *
     * @param merger the Merger object holding all the information for the merge
     *
     * @author Michael Collier
     */
   public void mergeChains(Merger merger){
        for(Tile t : Scout(playedTiles.get(playedTiles.size()-1))){
            t.setChainName(merger.getAcquiringChain().getName());
            if(!merger.getAcquiringChain().getTiles().contains(t)){
                merger.getAcquiringChain().addTile(t);
            }
        }

        merger.getAcquiredChain().getTiles().clear();
        unfoundedChains.add(merger.getAcquiredChain());
        foundedChains.remove(merger.getAcquiredChain());
   }

    /**
     * checks if there is a founding that needs to be handled
     *
     * @return the founder if there is a founding required or null if not
     */
   public Founder foundNeeded(){
       return currentFounder;
   }

    /**
     * founds a new hotel chain by adding tiles to the chain, giving the player who founded it stock,
     * and moving the chain into the founded list
     *
     * @param chain the name of the chain that needs to be founded
     * @param founder the player who founded the chain
     *
     * @author Michael Collier
     */
   public void FoundChain(String chain, Player founder){

       HotelChain chainToFound = null;

       for(HotelChain c : unfoundedChains){
           if(Objects.equals(c.getName(), chain)){
               chainToFound = c;
           }
       }

       for(Tile t : currentFounder.getChainTiles()){
           t.setChainName(chain);
           chainToFound.addTile(t);
       }

       foundedChains.add(chainToFound);
       unfoundedChains.remove(chainToFound);

       currentFounder = null;

       if(chainToFound.getNumberOfUnsoldStock() > 0){
           chainToFound.giveStock(founder);
       }
   }

    /**
     * checks if a tile can be placed
     *
     * @param tile the tile to be checked
     *
     * @return true if the placing the tile would not cause a founding or merging that would be illegal
     *
     * @author Michael Collier
     */
   public boolean moveIsLegal(Tile tile){
       boolean legal = true;
       Queue<List<Integer>> neighbors = new LinkedList<>();
       neighbors.add(tile.getCoordinates());
       int x = tile.getCoordinates().get(0);
       int y = tile.getCoordinates().get(1);
       neighbors.add(Arrays.asList(x+1, y));
       neighbors.add(Arrays.asList(x-1, y));
       neighbors.add(Arrays.asList(x, y+1));
       neighbors.add(Arrays.asList(x, y-1));
       boolean[][] visited = new boolean[board.size()][board.get(0).size()];

       List<Tile> chain = breadthFirstSearch(neighbors,visited);
       List<HotelChain> modeChain = modeInNeighborList(chain);


       if(modeChain.size() == 0 && chain.size() > 0 && unfoundedChains.isEmpty()){
           legal = false;
       }else if (modeChain.size() > 1 && !mergeIsLegal(modeChain)) {
           legal = false;
       }

       return legal;
   }

}