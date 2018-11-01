package cs340.client.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import cs340.client.R;
import cs340.client.presenter.GamePresenter;
import cs340.client.presenter.IGamePresenter;
import model.DestinationTicketCard;

public class DestinationView extends AbstractGameView{

    private IGamePresenter presenter;

    CheckBox[] checkBoxes;
    TextView[] startCities;
    TextView[] endCities;
    TextView[] pointValues;
    Button submitButton;
    Button seeMapButton;

    private Fragment currentFragment;

    FragmentManager fm;

    List<DestinationTicketCard> options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_view);

        checkBoxes = new CheckBox[3];
        checkBoxes[0] = findViewById(R.id.checkbox0);
        checkBoxes[1] = findViewById(R.id.checkbox1);
        checkBoxes[2] = findViewById(R.id.checkbox2);

        startCities = new TextView[3];
        startCities[0] = findViewById(R.id.start_city0);
        startCities[1] = findViewById(R.id.start_city1);
        startCities[2] = findViewById(R.id.start_city2);

        endCities = new TextView[3];
        endCities[0] = findViewById(R.id.end_city0);
        endCities[1] = findViewById(R.id.end_city1);
        endCities[2] = findViewById(R.id.end_city2);

        pointValues = new TextView[3];
        pointValues[0] = findViewById(R.id.point_value0);
        pointValues[1] = findViewById(R.id.point_value1);
        pointValues[2] = findViewById(R.id.point_value2);

        submitButton = findViewById(R.id.submit);
        submitButton.setEnabled(false);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<DestinationTicketCard> chosen = new LinkedList<>();
                for (int i = 0; i < 3; ++i) {
                    if (checkBoxes[i].isChecked()) {
                        chosen.add(options.get(i));
                    }
                }
                if (presenter.selectDestinations(chosen)) {
                    finish();
                }
            }
        });

        seeMapButton = findViewById(R.id.see_map);
        seeMapButton.setEnabled(true);
        seeMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMapActivity();
            }
        });


        presenter = GamePresenter.getInstance();
        presenter.attach(this);
    }

    private void loadMapActivity(){
        Intent intent = new Intent(this, DestinationMapView.class);
        this.startActivity(intent);
    }

    public void setDestinationOptions(List<DestinationTicketCard> o) {
        options = (o == null || o.size() == 0) ? null : o;

        if (options == null) {
            submitButton.setEnabled(false);
        } else {
            submitButton.setEnabled(true);
            for (int i = 0; i < 3; ++i) {
                String start;
                String end;
                String points;
                try {
                    DestinationTicketCard card = options.get(i);
                    start = card.getStartCity().getCity();
                    end = card.getEndCity().getCity();
                    points = String.format("Point Value: %d", card.getPoints());
                } catch (IndexOutOfBoundsException e) {
                    start = null;
                    end = null;
                    points = null;
                }
                startCities[i].setText(start);
                endCities[i].setText(end);
                pointValues[i].setText(points);
            }
        }
    }

    @Override
    public void update() {
        /*options = presenter.getDestinationOptions();
        submitButton.setEnabled(true);
        for (int i = 0; i < 3; ++i) {
            DestinationTicketCard card = options.get(i);
            startCities[i].setText(card.getStartCity().getCity());
            endCities[i].setText(card.getEndCity().getCity());
            pointValues[i].setText(String.format("Point Value: %d", card.getPoints()));
        }*/
    }

    @Override
    public void onBackPressed() {
        if (options == null) {
            super.onBackPressed();
        }
    }

    @Override
    public void displayMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
