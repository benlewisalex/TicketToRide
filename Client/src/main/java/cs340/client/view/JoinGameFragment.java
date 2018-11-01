package cs340.client.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Utils.PlayerColor;
import cs340.client.R;
import model.Game;
import model.User;

public class JoinGameFragment extends Fragment {

    public static final String gameArgumentKey = "cs340.client.view.JoinGameFragment.gameArgumentKey";

    private static final String TAG = "JoinGameFragment";

    private RecyclerView playerList;
    private RecyclerView colorList;
    private List<TextView> colorTextViews = new ArrayList<>();
    private Button joinButton;
    private Button cancelButton;

    private PlayerColor selectedColor;

    private Game game;
    private GameJoiner joiner;
    private FragmentContainer fragmentContainer;

    public static JoinGameFragment newInstance(Game game) {
        JoinGameFragment fragment = new JoinGameFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(gameArgumentKey, game);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        joiner = (GameJoiner) activity;
        fragmentContainer = (FragmentContainer) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        game = (Game) getArguments().getSerializable(gameArgumentKey);
        Log.d(TAG, "onCreateView()");
        View v = inflater.inflate(R.layout.join_game_fragment, container, false);

        playerList = (RecyclerView) v.findViewById(R.id.player_list);
        PlayerAdapter playerAdapter = new PlayerAdapter(game.getCurrentPlayers());
        playerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        playerList.setAdapter(playerAdapter);

        colorList = (RecyclerView) v.findViewById(R.id.color_list);
        List<PlayerColor> colors = new LinkedList<PlayerColor>();
        for (PlayerColor color : PlayerColor.values()) {
            if (game.isPlayerColorOpen(color)) {
                colors.add(color);
                Log.d(TAG, String.format("Adding %s to list of colors", color.getName()));
            }
        }
        ColorAdapter colorAdapter = new ColorAdapter(colors);
        colorList.setLayoutManager(new LinearLayoutManager(getActivity()));
        colorList.setAdapter(colorAdapter);

        joinButton = (Button) v.findViewById(R.id.join_button);
        joinButton.setEnabled(false);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "join game");
                joiner.joinGame(game.getGameID(), selectedColor);
                fragmentContainer.fragmentDone();
            }
        });

        cancelButton = (Button) v.findViewById(R.id.join_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentContainer.fragmentDone();
            }
        });

        return v;
    }

    private class PlayerHolder extends RecyclerView.ViewHolder {

        private TextView playerDisplay;

        public PlayerHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.player_list_entry, parent, false));
            playerDisplay = itemView.findViewById(R.id.player_display);
        }

        public void prepPlayerDisplay(User player) {
            playerDisplay.setText(player.getUsername());
            switch (player.getPlayerColor().getName()) {
                case "Red":
                    playerDisplay.setBackgroundColor(Color.rgb(255, 153, 153));
                    break;
                case "Green":
                    playerDisplay.setBackgroundColor(Color.rgb(153,255,153));
                    break;
                case "Yellow":
                    playerDisplay.setBackgroundColor(Color.rgb(255,255,153));
                    break;
                case "Black":
                    playerDisplay.setBackgroundColor(Color.rgb(192,192,192));
                    break;
                case "Blue":
                    playerDisplay.setBackgroundColor(Color.rgb(153,204,255));
                    break;
                default:
                    playerDisplay.setBackgroundColor(Color.BLACK);
                    break;
            }

        }

        public void bind(User player) {
            Log.d(TAG, "bind()");
            prepPlayerDisplay(player);
            Log.d(TAG, "bound");
        }
    }

    private class ColorHolder extends RecyclerView.ViewHolder {
        private TextView colorName;

        public ColorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.color_list_entry, parent, false));

            colorName = itemView.findViewById(R.id.color_name);
        }

        public void prepColorDisplay(PlayerColor color) {
            colorName.setText(color.getName());
            switch (color.getName()) {
                case "Red":
                    colorName.setBackgroundColor(Color.rgb(255, 153, 153));
                    break;
                case "Green":
                    colorName.setBackgroundColor(Color.rgb(153,255,153));
                    break;
                case "Yellow":
                    colorName.setBackgroundColor(Color.rgb(255,255,153));
                    break;
                case "Black":
                    colorName.setBackgroundColor(Color.rgb(192,192,192));
                    break;
                case "Blue":
                    colorName.setBackgroundColor(Color.rgb(153,204,255));
                    break;
                default:
                    colorName.setBackgroundColor(Color.BLACK);
                    break;
            }

        }

        public void textWeightSetter(){
            for(TextView color : colorTextViews){
                color.setTypeface(Typeface.DEFAULT);
            }
            colorName.setTypeface(Typeface.DEFAULT_BOLD);
        }

        public void bind(final PlayerColor color) {
            if (color != null) {
                Log.d(TAG, String.format("Binding color %s", color.getName()));
                prepColorDisplay(color);
                colorTextViews.add(colorName);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, String.format("Clicked %s", color.getName()));
                        selectedColor = color;
                        joinButton.setEnabled(true);
                        textWeightSetter();
                    }
                });
            }
        }
    }

    private class PlayerAdapter extends RecyclerView.Adapter<PlayerHolder> {

        private List<User> players;

        public PlayerAdapter(List<User> players) {
            for (User player : players) {
                Log.d(TAG, "Hello");
                Log.d(TAG, player.getUsername());
            }
            Log.d(TAG, Integer.toString(players.size()));
            this.players = players;
        }

        @Override
        public PlayerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PlayerHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PlayerHolder playerHolder, int i) {
            Log.d(TAG, String.format("Index %d: %s", i, players.get(i).getUsername()));
            playerHolder.bind(players.get(i));
            Log.d(TAG, "onBindViewHolder() done");
        }

        @Override
        public int getItemCount() {
            return players.size();
        }
    }

    private class ColorAdapter extends RecyclerView.Adapter<ColorHolder> {
        private List<PlayerColor> colors;

        public ColorAdapter(List<PlayerColor> colors) {
            this.colors = colors;
        }

        @Override
        public ColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ColorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ColorHolder colorHolder, int i) {
            colorHolder.bind(colors.get(i));
        }

        @Override
        public int getItemCount() {
            return colors.size();
        }
    }
}
