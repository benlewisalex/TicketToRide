package cs340.client.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Utils.TrainColor;
import cs340.client.R;
import cs340.client.presenter.GamePresenter;
import cs340.client.presenter.IGamePresenter;
import model.DestinationTicketCard;
import model.Player;

public class StatsView extends AbstractGameView {

    private IGamePresenter presenter;
    private TextView numBlueCards;
    private TextView numRedCards;
    private TextView numGreenCards;
    private TextView numYellowCards;
    private TextView numBlackCards;
    private TextView numPurpleCards;
    private TextView numOrangeCards;
    private TextView numWhiteCards;
    private TextView numWildCards;
    private RecyclerView statsRecycler;
    private RecyclerView destTicketRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_view);

        numBlueCards = (TextView) findViewById(R.id.blue_cardCount);
        numRedCards = (TextView) findViewById(R.id.red_cardCount);
        numGreenCards = (TextView) findViewById(R.id.green_cardCount);
        numYellowCards = (TextView) findViewById(R.id.yellow_cardCount);
        numBlackCards = (TextView) findViewById(R.id.black_cardCount);
        numOrangeCards = (TextView) findViewById(R.id.orange_cardCount);
        numPurpleCards = (TextView) findViewById(R.id.purple_cardCount);
        numWhiteCards = (TextView) findViewById(R.id.white_cardCount);
        numWildCards = (TextView) findViewById(R.id.wild_cardCount);
        statsRecycler = (RecyclerView) findViewById(R.id.player_stats_recycler);
        statsRecycler.setLayoutManager(new LinearLayoutManager(this));
        destTicketRecycler = (RecyclerView) findViewById(R.id.dest_ticket_recycler);
        destTicketRecycler.setLayoutManager(new LinearLayoutManager(this));

        presenter = GamePresenter.getInstance();
        presenter.attach(this);
    }

    public void setActivePlayer(Player player) {
        numBlueCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.BLUE)));
        numRedCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.RED)));
        numGreenCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.GREEN)));
        numYellowCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.YELLOW)));
        numBlackCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.BLACK)));
        numOrangeCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.ORANGE)));
        numPurpleCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.PURPLE)));
        numWhiteCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.WHITE)));
        numWildCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.WILD)));
        DestAdapter destAdapter = new DestAdapter(player.getDestinationTickets());
        destTicketRecycler.setAdapter(destAdapter);
    }

    public void setPlayers(List<Player> players) {
        StatsAdapter statsAdapter = new StatsAdapter(players);
        statsRecycler.setAdapter(statsAdapter);
    }

    @Override
    public void update() {
        /*List<Player> players = presenter.getPlayers();
        String userName = presenter.getUserName();

        for(Player player: players) {
            if(player.getPlayerName().equals(userName)) {
                numBlueCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.BLUE)));
                numRedCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.RED)));
                numGreenCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.GREEN)));
                numYellowCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.YELLOW)));
                numBlackCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.BLACK)));
                numOrangeCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.ORANGE)));
                numPurpleCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.PURPLE)));
                numWhiteCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.WHITE)));
                numWildCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.WILD)));
                DestAdapter destAdapter = new DestAdapter(player.getDestinationTickets());
                destTicketRecycler.setAdapter(destAdapter);
            }
        }
        StatsAdapter statsAdapter = new StatsAdapter(players);
        statsRecycler.setAdapter(statsAdapter);*/
    }

    @Override
    public void displayMessage(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    private class StatsHolder extends RecyclerView.ViewHolder {
        private TextView row0;
        private TextView row1;
        private TextView row2;
        private TextView row3;

        public StatsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.stats_view_row, parent, false));

            row0 = (TextView) itemView.findViewById(R.id.row_zero);
            row1 = (TextView) itemView.findViewById(R.id.row_one);
            row2 = (TextView) itemView.findViewById(R.id.row_two);
            row3 = (TextView) itemView.findViewById(R.id.row_three);
        }

        public void bind(Player player) {
            row0.setText(player.getPlayerName());
            row1.setText(String.valueOf(player.getNumTrainCards()));
            row2.setText(String.valueOf(player.getRemainingTrainCars()));
            row3.setText(String.valueOf(player.getScore()));
            switch (player.getColor().getName()) {
                case "Red":
                    row0.setBackgroundColor(Color.rgb(255, 153, 153));
                    row1.setBackgroundColor(Color.rgb(255, 153, 153));
                    row2.setBackgroundColor(Color.rgb(255, 153, 153));
                    row3.setBackgroundColor(Color.rgb(255, 153, 153));
                    break;
                case "Green":
                    row0.setBackgroundColor(Color.rgb(153,255,153));
                    row1.setBackgroundColor(Color.rgb(153,255,153));
                    row2.setBackgroundColor(Color.rgb(153,255,153));
                    row3.setBackgroundColor(Color.rgb(153,255,153));
                    break;
                case "Yellow":
                    row0.setBackgroundColor(Color.rgb(255,255,153));
                    row1.setBackgroundColor(Color.rgb(255,255,153));
                    row2.setBackgroundColor(Color.rgb(255,255,153));
                    row3.setBackgroundColor(Color.rgb(255,255,153));
                    break;
                case "Black":
                    row0.setBackgroundColor(Color.rgb(192,192,192));
                    row1.setBackgroundColor(Color.rgb(192,192,192));
                    row2.setBackgroundColor(Color.rgb(192,192,192));
                    row3.setBackgroundColor(Color.rgb(192,192,192));
                    break;
                case "Blue":
                    row0.setBackgroundColor(Color.rgb(153,204,255));
                    row1.setBackgroundColor(Color.rgb(153,204,255));
                    row2.setBackgroundColor(Color.rgb(153,204,255));
                    row3.setBackgroundColor(Color.rgb(153,204,255));
                    break;
                default:
                    row0.setBackgroundColor(Color.BLACK);
                    break;
            }
        }
    }

    private class StatsAdapter extends RecyclerView.Adapter<StatsView.StatsHolder> {

        private List<Player> playerList;

        public StatsAdapter(List<Player> players) {
            playerList = players;
        }

        @Override
        public StatsView.StatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StatsView.this);

            return new StatsView.StatsHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(StatsView.StatsHolder statsHolder, int i) {
            statsHolder.bind(playerList.get(i));
        }

        @Override
        public int getItemCount() {
            return playerList.size();
        }
    }

    private class DestHolder extends RecyclerView.ViewHolder {
        private TextView row0;
        private TextView row1;
        private TextView row2;
        private TextView row3;

        public DestHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.stats_view_row, parent, false));

            row0 = (TextView) itemView.findViewById(R.id.row_zero);
            row1 = (TextView) itemView.findViewById(R.id.row_one);
            row2 = (TextView) itemView.findViewById(R.id.row_two);
            row3 = (TextView) itemView.findViewById(R.id.row_three);
        }

        public void bind(DestinationTicketCard card) {
            System.out.println(card.getStartCity());

            if(card.getStartCity() != null) {
                row0.setText(card.getStartCity().getCity());
            }
            if(card.getEndCity() != null) {
                row1.setText(card.getEndCity().getCity());
            }
            row2.setText(String.valueOf(card.getPoints()));
            row3.setText(String.valueOf(card.getCompleted()));
        }
    }

    private class DestAdapter extends RecyclerView.Adapter<StatsView.DestHolder> {

        private List<DestinationTicketCard> destCards;

        public DestAdapter(List<DestinationTicketCard> cards) {
            destCards = cards;
        }

        @Override
        public StatsView.DestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StatsView.this);

            return new StatsView.DestHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(StatsView.DestHolder destHolder, int i) {
            destHolder.bind(destCards.get(i));
        }

        @Override
        public int getItemCount() {
            return destCards.size();
        }
    }
}
