package model;

import java.util.ArrayList;
import java.util.List;

public class TrainCarCardList {
    List<TrainCarCard> cards;

    public TrainCarCardList (List<TrainCarCard> cards){
        this.cards = new ArrayList<>();
        for(TrainCarCard card : cards){
            this.cards.add(card);
        }
    }

    public List<TrainCarCard> getCards() {
        return cards;
    }
}