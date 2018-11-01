package cs340.client.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.TrainColor;
import cs340.client.R;
import cs340.client.model.ClientModel;
import cs340.client.model.UIFacade;
import cs340.client.presenter.GamePresenter;
import cs340.client.presenter.IGamePresenter;
import model.TrainCarCard;

public class DrawCardView extends AbstractGameView {
    private static final String TAG = "DrawCardView";

    private Boolean deckEmpty = null;
    private boolean allowBack = true;
    private TrainColor[] cardColors = new TrainColor[5];

    private TextView numBlueCards;
    private TextView numRedCards;
    private TextView numGreenCards;
    private TextView numYellowCards;
    private TextView numBlackCards;
    private TextView numPurpleCards;
    private TextView numOrangeCards;
    private TextView numWhiteCards;
    private TextView numWildCards;


    private IGamePresenter presenter;

    private static Map<TrainColor, Integer> cardImages;
    static {
        cardImages = new HashMap<TrainColor, Integer>();
        cardImages.put(TrainColor.BLACK, R.drawable.black_card);
        cardImages.put(TrainColor.BLUE, R.drawable.blue_card);
        cardImages.put(TrainColor.GREEN, R.drawable.green_card);
        cardImages.put(TrainColor.ORANGE, R.drawable.orange_card);
        cardImages.put(TrainColor.PURPLE, R.drawable.purple_card);
        cardImages.put(TrainColor.RED, R.drawable.red_card);
        cardImages.put(TrainColor.WHITE, R.drawable.white_card);
        cardImages.put(TrainColor.YELLOW, R.drawable.yellow_card);
        cardImages.put(TrainColor.WILD, R.drawable.locomotive);
    }

    private ImageView[] cards = new ImageView[5];
    private ImageView deck;

    /**
     * Re-draws the card at the given index
     * @pre presenter != null
     * @pre 0 <= i < 5
     *
     * @post The view at index i will show the correct card or a blank
     * @post Clicking on the new card will draw that card
     * @post Clicking on the blank space will do nothing
     *
     * @param i the index of the card to be updated
     */
    private void updateCard(final int i, TrainColor color) {
        Integer image = cardImages.get(color);
        if (image != null) {
            cards[i].setImageResource(image);
            cards[i].setOnClickListener(new View.OnClickListener() {
                private boolean enabled = true;
                @Override
                public void onClick(View view) {
                    if (enabled) {
                        presenter.takeCard(i);
                        enabled = false;
                        allowBack = !allowBack;
                    }
                }
            });
        } else {
            Log.d(TAG, String.format("Color: %s", color));
            cards[i].setOnClickListener(null);
            cards[i].setImageResource(R.drawable.no_card);
        }
        UIFacade facade = UIFacade.getInstance();
        numBlueCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.BLUE)));
        numRedCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.RED)));
        numGreenCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.GREEN)));
        numYellowCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.YELLOW)));
        numBlackCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.BLACK)));
        numOrangeCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.ORANGE)));
        numPurpleCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.PURPLE)));
        numWhiteCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.WHITE)));
        numWildCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.WILD)));
    }

//    private void updateDeckImage()
//    {
//        drawDeck();
//    }

    /**
     * @pre none
     * @post The view will show 5 slots containing either a face up card or a blank slot
     * @post The view will show 1 slot containing either a face down deck or a blank slot
     *
     * @param savedInstanceState Bundle object passed in by the framework
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.draw_card_view);

        cards[0] = findViewById(R.id.card0);
        cards[1] = findViewById(R.id.card1);
        cards[2] = findViewById(R.id.card2);
        cards[3] = findViewById(R.id.card3);
        cards[4] = findViewById(R.id.card4);
        deck = findViewById(R.id.deck);

        numBlueCards = (TextView) findViewById(R.id.blue_cardCount);
        numRedCards = (TextView) findViewById(R.id.red_cardCount);
        numGreenCards = (TextView) findViewById(R.id.green_cardCount);
        numYellowCards = (TextView) findViewById(R.id.yellow_cardCount);
        numBlackCards = (TextView) findViewById(R.id.black_cardCount);
        numOrangeCards = (TextView) findViewById(R.id.orange_cardCount);
        numPurpleCards = (TextView) findViewById(R.id.purple_cardCount);
        numWhiteCards = (TextView) findViewById(R.id.white_cardCount);
        numWildCards = (TextView) findViewById(R.id.wild_cardCount);

        UIFacade facade = UIFacade.getInstance();
        numBlueCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.BLUE)));
        numRedCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.RED)));
        numGreenCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.GREEN)));
        numYellowCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.YELLOW)));
        numBlackCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.BLACK)));
        numOrangeCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.ORANGE)));
        numPurpleCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.PURPLE)));
        numWhiteCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.WHITE)));
        numWildCards.setText(String.valueOf(facade.getClientPlayer().getNumColoredTrainCardCards(TrainColor.WILD)));

        presenter = GamePresenter.getInstance();
        presenter.attach(this);
    }

    private void drawDeck() {
        if (deckEmpty) {
            deck.setImageResource(R.drawable.no_card);
            deck.setOnClickListener(null);
        } else {
            deck.setImageResource(R.drawable.train_card_back);
            deck.setOnClickListener(new View.OnClickListener() {
                private boolean enabled = true;

                @Override
                public void onClick(View view) {
                    if (enabled) {
                        presenter.drawCard();
//                        enabled = false;
                        allowBack = false;
                    }
                }
            });
        }
    }

    public void setDeckEmpty(boolean empty) {
        if (deckEmpty == null || deckEmpty != empty) {
            deckEmpty = empty;
            drawDeck();
        }
    }

    public void setFaceUpCards(List<TrainColor> cards) {
        for (int i = 0; i < cardColors.length; ++i) {
            TrainColor color = cards.get(i);
            if(color == null) {
                cardColors[i] = color;
                updateCard(i, color);
            }
            else if (cardColors[i] == null || !color.getName().equals(cardColors[i].getName()) || !allowBack) {
                cardColors[i] = color;
                updateCard(i, color);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (allowBack) {
            super.onBackPressed();
        }
    }

    /**
     * @pre presenter != null
     * @pre cards != null
     * @pre cards.length == 5
     * @post the set of displayed cards will reflect the contents of the deck
     */
    @Override
    public void update() {}
}
