package cs340.client.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Utils.PlayerColor;
import cs340.client.R;
import cs340.client.presenter.ILobbyPresenter;
import cs340.client.presenter.LobbyPresenter;
import model.ActiveGame;
import model.Game;

public class LobbyView extends AppCompatActivity implements FragmentContainer, GameCreator, GameJoiner, ILobbyView {
    private static final String TAG = "LobbyView";

    private ILobbyPresenter presenter;

    private Button createGameButton;
    private RecyclerView gameListView;
    private Fragment currentFragment;
    private Button disabledButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayMessage("Login successful");
        setContentView(R.layout.lobby_view);

        createGameButton = (Button) findViewById(R.id.create_game_button);
        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButton(createGameButton);
                loadFragment(new CreateGameFragment());
            }
        });

        gameListView = (RecyclerView) findViewById(R.id.game_list);
        gameListView.setLayoutManager(new LinearLayoutManager(this));

        presenter = LobbyPresenter.getInstance();
        presenter.attach(this);
    }

    @Override
    public void fragmentDone() {
        loadFragment(null);
        disableButton(null);
    }

    @Override
    public void createGame(String gameName, Integer playerCount) {
        Log.d(TAG, "createGame()");
        presenter.createGame(gameName, playerCount);
    }

    @Override
    public void joinGame(String gameID, PlayerColor color) {
        Log.d(TAG, String.format("joining game %s with color %s", gameID, color.getName()));
        presenter.joinGame(gameID, color);
    }

    private void disableButton(Button b) {
        if (disabledButton != null)
            disabledButton.setEnabled(true);

        if (b != null)
            b.setEnabled(false);

        disabledButton = b;
    }

    private void loadFragment(Fragment frag) {
        FragmentManager fm = getSupportFragmentManager();
        if (currentFragment != null)
            fm.beginTransaction().remove(currentFragment).commit();

        if (frag != null)
            fm.beginTransaction().add(R.id.lobby_left_half, frag).commit();

        currentFragment = frag;
    }

    @Override
    public void setJoinedGame(Game game) {
        displayMessage("Successfully joined game " + game.getGameName());
        loadFragment(new JoinedFragment());
    }

    @Override
    public void setGameList(List<Game> games) {
        Adapter adapter = new Adapter(games);
        gameListView.setAdapter(adapter);
    }

    @Override
    public void startGame(ActiveGame game) {
        Intent intent = new Intent(this, GameView.class);
        startActivity(intent);
    }

    private class GameHolder extends RecyclerView.ViewHolder {
        private TextView gameNameView;
        private TextView playerCountView;
        private Button joinGameButton;

        public GameHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.game_list_entry, parent, false));

            gameNameView = (TextView) itemView.findViewById(R.id.game_name);
            playerCountView = (TextView) itemView.findViewById(R.id.game_player_count);
            joinGameButton = (Button) itemView.findViewById(R.id.join_game);
        }

        public void bind(final Game game) {
            if (gameNameView != null)
                gameNameView.setText(game.getGameName());
            if (playerCountView != null)
                playerCountView.setText(String.format("%d/%d", game.getCurrentNumPlayers(), game.getNumPossiblePlayers()));

            joinGameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadFragment(JoinGameFragment.newInstance(game));
                    disableButton(joinGameButton);
                }
            });
        }
    }

    private class Adapter extends RecyclerView.Adapter<GameHolder> {

        private List<Game> games;

        public Adapter(List<Game> games) {
            this.games = games;
        }

        @Override
        public GameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(LobbyView.this);

            return new GameHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(GameHolder gameHolder, int i) {
            gameHolder.bind(games.get(i));
        }

        @Override
        public int getItemCount() {
            return games.size();
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    @Override
    public void displayMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
