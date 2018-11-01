package cs340.client.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cs340.client.R;
import cs340.client.communication.Poller;
import cs340.client.presenter.GamePresenter;
import cs340.client.presenter.IGamePresenter;
import model.DestinationTicketCard;
import model.Player;
import model.Route;

public class EndGameView extends AbstractGameView implements IEndGameView {

    private TextView winnerName;
    private RecyclerView playerRecycler;
    private IGamePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_game);

        winnerName = (TextView) findViewById(R.id.end_game_winner);
        playerRecycler = (RecyclerView) findViewById(R.id.end_game_recycler);
        playerRecycler.setLayoutManager(new LinearLayoutManager(this));
        StatsAdapter adapter = new StatsAdapter(new ArrayList<Player>());
        playerRecycler.setAdapter(adapter);

        presenter = GamePresenter.getInstance();
        presenter.attach(this);
    }

    @Override
    public void setStats(List<Player> players) {
        winnerName.setText("Winner: " + getWinningPlayer(players) + "!");
        StatsAdapter statsAdapter = new EndGameView.StatsAdapter(players);
        playerRecycler.setAdapter(statsAdapter);
        Poller.getInstance().disable();
    }

    @Override
    public void update() {

    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    public String getWinningPlayer(List<Player> players) {
        String result = " Everybody lost";
        int maxScore = 0;
        for(Player player: players) {
            int currentScore = getTotalScore(player);
            if(currentScore >= maxScore) {
                maxScore = currentScore;
                result = player.getPlayerName();
            }
        }
        return result;
    }

    public int getTotalScore(Player player) {
        int result = 0;
        result += getClaimedRoutesScore(player);
        result += getClaimedDestScore(player);
        result += getUnclaimedDestScore(player);
        return result;
    }

    public int getClaimedRoutesScore(Player player) {
        return player.getScore();
    }

    public int getClaimedDestScore(Player player) {
        int result = 0;
        for(DestinationTicketCard ticket: player.getDestinationTickets()) {
            if(ticket.getCompleted()) {
                result += ticket.getPoints();
            }
        }

        return result;
    }

    public int getUnclaimedDestScore(Player player) {
        int result = 0;
        for(DestinationTicketCard ticket: player.getDestinationTickets()) {
            if(!ticket.getCompleted()) {
                result -= ticket.getPoints();
            }
        }

        return result;
    }

    public void displayMessage(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    private class StatsHolder extends RecyclerView.ViewHolder {
        private TextView playerName;
        private TextView claimedRoutePtns;
        private TextView claimedDestPtns;
        private TextView unclaimedDestPtns;
        private TextView totalPtns;

        public StatsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.end_game_stats_row, parent, false));

            playerName = (TextView) itemView.findViewById(R.id.player_name_field);
            claimedRoutePtns = (TextView) itemView.findViewById(R.id.claimed_routes_field);
            claimedDestPtns = (TextView) itemView.findViewById(R.id.claimed_dest_field);
            unclaimedDestPtns = (TextView) itemView.findViewById(R.id.unclaimed_dest_field);
            totalPtns = (TextView) itemView.findViewById(R.id.total_ptns_field);
        }

        public void bind(Player player) {
            playerName.setText(player.getPlayerName());
            claimedRoutePtns.setText(String.valueOf(getClaimedRoutesScore(player)));
            claimedDestPtns.setText(String.valueOf(getClaimedDestScore(player)));
            unclaimedDestPtns.setText(String.valueOf(getUnclaimedDestScore(player)));
            totalPtns.setText(String.valueOf(getTotalScore(player)));
        }
    }

    private class StatsAdapter extends RecyclerView.Adapter<EndGameView.StatsHolder> {

        private List<Player> playerList;

        public StatsAdapter(List<Player> players) {
            playerList = players;
        }

        @Override
        public EndGameView.StatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(EndGameView.this);

            return new EndGameView.StatsHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(EndGameView.StatsHolder statsHolder, int i) {
            statsHolder.bind(playerList.get(i));
        }

        @Override
        public int getItemCount() {
            return playerList.size();
        }
    }

}
