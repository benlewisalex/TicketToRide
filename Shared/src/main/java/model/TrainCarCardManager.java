package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import Utils.TrainColor;

/**
 * TrainCarCardManager stores and manages the TrainCarCards.
 * It does this by splitting up the TrainCarCards into
 * face up cards, face down cards, and discarded cards.
 * This class also has methods to handle players drawing trainCarCards,
 * discarding/using TrainCarCards, initializing all the TrainCarCards,
 * shuffling, replacing the face up cards, and checking the limit of locomotive
 * cards in the face up list of cards.
 *
 * @invariant displayedTrainCards.size() = 5
 * @invariant 0 < facedownTrainCarCards < 105
 * @invariant 0 < discardPile < 105
 * @invariant displayedTrainCards.size() + facedownTrainCarCards.size() + discardPile() = 110
 */

public class TrainCarCardManager {
    private List<TrainCarCard> displayedTrainCards;
    private List<TrainCarCard> facedownTrainCarCards;
    private List<TrainCarCard> discardPile;
    private boolean hasDrawnCard;

    /**
     * TrainCarCardManager Constructor
     * @pre none
     *
     * @post fully initialized TrainCarCardManager
     */
    public TrainCarCardManager() {
        displayedTrainCards = new ArrayList<TrainCarCard>();
        facedownTrainCarCards = new ArrayList<>();
        discardPile = new ArrayList<>();
        hasDrawnCard = false;
        initialize();
    }

    /**
     * initialize method creates the 110 TrainCarCards used in the game
     * with the correct number of each color of card and the correct
     * number of wilds/locomotives. Then shuffles the cards and places
     * 5 in the face up card list.
     * @pre displayedTrainCards != null
     * @pre facedownTrainCarCards != null
     * @pre discardPile != null
     *
     * @post displayedTrainCards.size() = 5
     * @post facedownTrainCarCards.size() = 105
     * @post discardPile.size() = 0
     */
    private void initialize()
    {
        for(int i = 0; i < 110; i++)
        {
            if (i < 12)
                discardPile.add(new TrainCarCard(TrainColor.PURPLE));
            else if (i < 24)
                discardPile.add(new TrainCarCard(TrainColor.WHITE));
            else if (i < 36)
                discardPile.add(new TrainCarCard(TrainColor.BLUE));
            else if (i < 48)
                discardPile.add(new TrainCarCard(TrainColor.YELLOW));
            else if (i < 60)
                discardPile.add(new TrainCarCard(TrainColor.ORANGE));
            else if (i < 72)
                discardPile.add(new TrainCarCard(TrainColor.BLACK));
            else if (i < 84)
                discardPile.add(new TrainCarCard(TrainColor.RED));
            else if (i < 96)
                discardPile.add(new TrainCarCard(TrainColor.GREEN));
            else
                discardPile.add(new TrainCarCard(TrainColor.WILD));
        }
        shuffle();
        flushFaceupCards();
    }

    /**
     * isEmpty checks to see if there are any cards left in the draw pile
     * @pre facedownTrainCarCards != null
     *
     * @post know whether facedownTrainCarCards is empty or not
     * @return true if facedownTrainCarCards is empty
     * @return false if facedownTrainCarCards is not empty
     */
    public boolean isEmpty() {
        return facedownTrainCarCards.isEmpty();
    }

    public boolean hasDrawnCard() {
        return hasDrawnCard;
    }

    public void setHasDrawnCard(boolean hasDrawnCard) {
        this.hasDrawnCard = hasDrawnCard;
    }


    //    /**
//     *
//     * @param index
//     * @return
//     */
//    public TrainCarCard chooseCard(int index)
//    {
//        if(index < 5)
//        {
//            TrainCarCard chosenCard = new TrainCarCard(displayedTrainCards.get(index).getColor());
//            displayedTrainCards.remove(index);
//            refill();
//            return chosenCard;
//        }
//        else
//        {
//            TrainCarCard topCard = new TrainCarCard(facedownTrainCarCards.get(0).getColor());
//            facedownTrainCarCards.remove(0);
//            return topCard;
//        }
//    }

    /**
     * get the list of diplayed train car cards
     * @pre displayedTrainCards != null
     * @return displayedTrainCards (list of face up train car cards)
     */
    public List<TrainCarCard> getFaceupCards() {
        return displayedTrainCards;
    }

    public List<TrainCarCard> getFacedownCards() {
        return facedownTrainCarCards;
    }

    /**
     * This method checks to see if there are 3 or more locomotive/wild cards in the
     * list of displayedTrainCards.
     * @pre displayedTrainCards != null
     *
     * @post know whether there are 3 or more locomotive/wild cards displayed currently
     * @return true if there are 3 or more locomotive/wild cards displayed currently
     * @return false if there are less than 3 locomotive/wild cards displayed currently
     */
    public boolean checkLocomotiveCount()
    {
        int count = 0;
        for (TrainCarCard t : displayedTrainCards)
        {
            if(t != null) {
                if (t.getColor() == TrainColor.WILD) {
                    count++;
                    if (count >= 3)
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * This method draws a card from the facedown draw pile.
     * @pre facedownTrainCarCards != null
     * @pre facedownTrainCarCards.size() >= 1
     *
     * @post new facedownTrainCarCards.size() = old facedownTrainCarCards.size() - 1
     * @return the top TrainCarCard from facedownTrainCarCards if facedownTrainCarCards.size() >= 1
     * @return null if facedownTrainCarCards.size() < 1
     */
    public TrainCarCard draw() {
        try {
            if(facedownTrainCarCards.size() == 0){
                shuffle();
            }
            TrainCarCard card = facedownTrainCarCards.get(0);
            facedownTrainCarCards.remove(0);
            return card;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * This method returns the TrainCarCard at the given index of the displayed TrainCarCards
     * @pre displayedTrainCards != null
     * @pre 0 < index < 4
     * @param index - location of the TrainCarCard in displayedTrainCards
     * @return the TrainCarCard at the specified index of displayedTrainCards
     */
    public TrainCarCard getCard(int index) {
        return displayedTrainCards.get(index);
    }

    /**
     * this method removes and returns the TrainCarCard at the specified location
     * @pre 0 < index < 4
     *
     * @post 4 0f the 5 cards in displayedTrainCards are the same with one new drawn card
     * @param index - location in displayedTrainCards that the TrainCarCard will be taken from
     * @return the TrainCarCard at the specified index of displayedTrainCards
     */
    public TrainCarCard takeCard(int index) {
        TrainCarCard card = displayedTrainCards.set(index, draw());
        if(card != null) {
            while (checkLocomotiveCount()) {
                flushFaceupCards();
            }
        }
        return card;
    }

    /**
     * this method takes a list of TrainCarCards and adds them to the discard pile
     * @pre discardedCards != null
     * @pre discardPile != null
     *
     * @post new discardPile.size() = old discardPile.size() + discardedCards.size()
     * @param discardedCards list of TrainCarCards to be discarded(added to discardPile)
     */
    public void discard(List<TrainCarCard> discardedCards)
    {
        for(TrainCarCard t : discardedCards)
        {
            discardPile.add(t);
        }
    }

    /**
     * this method discards all the displayed TrainCarCards and then replaces all of them with
     * new cards from the draw pile
     * @pre displayedTrainCards != null
     * @pre discardPile != null
     *
     * @post new discardPile.size() = old discardPile.size() + 5
     * @post new facedownTrainCarCards.size() = old facedownTrainCarCards.size - 5
     */
    public void flushFaceupCards()
    {
        discard(displayedTrainCards);
        displayedTrainCards.clear();
        for (int i = 0; i < 5; i++) {
            displayedTrainCards.add(draw());
        }
        while (checkLocomotiveCount())
        {
            flushFaceupCards();
        }
    }

    /**
     * this method randomly rearranges the TrainCarCards in the discardPile and then adds the
     * rearranged cards to the facedownTrainCarCards.
     * @pre discardPile.size = 105
     * @pre facedownTrainCarCards.size() = 0
     *
     * @post new discardPile.size() = 0
     * @post new facedownTrainCarCards.size() = 105
     */
    private void shuffle()
    {
        for(int i = 0; i < 10; i++)
            Collections.shuffle(discardPile);
        facedownTrainCarCards = discardPile;
        discardPile = new ArrayList<TrainCarCard>();
    }

    public void setFaceUpCards(List<TrainCarCard> cards) {
        displayedTrainCards = cards;
    }
}
