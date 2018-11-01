package cs340.client.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import cs340.client.R;

public class CreateGameFragment extends Fragment {
    private static final String TAG = "CreateGameFragment";

    private EditText gameIDField;
    private Spinner playerCountMenu;
    private Button createButton;
    private Button cancelButton;

    private String gameID;
    private int playerCount;

    private boolean gameIDValid;
    private boolean playerCountValid;

    private GameCreator creator;
    private FragmentContainer fragmentContainer;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        creator = (GameCreator) activity;
        fragmentContainer = (FragmentContainer) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View v = inflater.inflate(R.layout.create_game_fragment, container, false);

        gameIDField = (EditText) v.findViewById(R.id.game_name_field);
        gameIDField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                gameID = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                gameIDValid = !gameID.equals("");
                updateCreateButton();
            }
        });

        ArrayAdapter<CharSequence> player_count_adapter;
        player_count_adapter = ArrayAdapter.createFromResource(getContext(), R.array.player_counts, android.R.layout.simple_spinner_item);
        player_count_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        playerCountMenu = (Spinner) v.findViewById(R.id.player_count_spinner);
        playerCountMenu.setAdapter(player_count_adapter);
        playerCountMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                try {
                    playerCount = Integer.parseInt(selectedItem);
                    playerCountValid = (playerCount >= 2 && playerCount <= 5);
                } catch (NumberFormatException e) {
                    playerCountValid = false;
                }
                updateCreateButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                playerCountValid = false;
                updateCreateButton();
            }
        });

        createButton = (Button) v.findViewById(R.id.game_create_button);
        createButton.setEnabled(false);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creator.createGame(gameID, playerCount);
                fragmentContainer.fragmentDone();
            }
        });

        cancelButton = (Button) v.findViewById(R.id.cancel_create_game);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentContainer.fragmentDone();
            }
        });

        return v;
    }

    private void updateCreateButton() {
        createButton.setEnabled(gameIDValid && playerCountValid);
    }
}
