package cs340.client.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import Utils.TrainColor;
import cs340.client.R;
import cs340.client.presenter.GamePresenter;
import cs340.client.presenter.IGamePresenter;
import model.Player;
import model.Route;

public class ClaimRouteView extends AbstractGameView {
    private static final String TAG = "ClaimRouteView";
    private IGamePresenter presenter;

    private RecyclerView selectRoute;
    private TextView startCity;
    private TextView destination;
    private TextView colorField;
    private TextView lengthField;
    private Button claimRoute;

    private Button whiteButton;
    private Button purpleButton;
    private Button redButton;
    private Button orangeButton;
    private Button yellowButton;
    private Button greenButton;
    private Button blueButton;
    private Button blackButton;
    private Button wildButton;

    private Route selectedRoute;
    private TrainColor selectedColor;

    private List<TextView> routeTextViews = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.claim_route_view);

        startCity = findViewById(R.id.start_city);
        destination = findViewById(R.id.destination);
        colorField = findViewById(R.id.route_color);
        lengthField = findViewById(R.id.route_length);

        claimRoute = findViewById(R.id.claim_route);
        claimRoute.setEnabled(false);
        claimRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                claimRoute.setEnabled(false);
//                displayMessage(String.format(
//                        "Claiming Route from %s to %s",
//                        selectedRoute.getStartCity().getCity(),
//                        selectedRoute.getEndCity().getCity()
//                ));
                presenter.claimRoute(selectedRoute, selectedColor);
            }
        });

        whiteButton = findViewById(R.id.white_button);
        whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = TrainColor.WHITE;
                updateButton();
            }
        });

        purpleButton = findViewById(R.id.purple_button);
        purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = TrainColor.PURPLE;
                updateButton();
            }
        });

        redButton = findViewById(R.id.red_button);
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = TrainColor.RED;
                updateButton();
            }
        });

        orangeButton = findViewById(R.id.orange_button);
        orangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = TrainColor.ORANGE;
                updateButton();
            }
        });

        yellowButton = findViewById(R.id.yellow_button);
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = TrainColor.YELLOW;
                updateButton();
            }
        });

        greenButton = findViewById(R.id.green_button);
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = TrainColor.GREEN;
                updateButton();
            }
        });

        blueButton = findViewById(R.id.blue_button);
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = TrainColor.BLUE;
                updateButton();
            }
        });

        blackButton = findViewById(R.id.black_button);
        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = TrainColor.BLACK;
                updateButton();
            }
        });

        wildButton = findViewById(R.id.wild_button);
        wildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColor = TrainColor.WILD;
                updateButton();
            }
        });

        selectRoute = findViewById(R.id.select_route);
        selectRoute.setLayoutManager(new LinearLayoutManager(this));
        setRoute(null);

        presenter = GamePresenter.getInstance();
        presenter.attach(this);
    }

    @Override
    public void update() {

    }

    @Override
    public void displayMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void setAvailableRoutes(List<Route> routes) {
        RouteAdapter adapter = new RouteAdapter(routes);
        selectRoute.setAdapter(adapter);
    }

    public void setStats(Player player) {
        blueButton.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.BLUE)));
        redButton.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.RED)));
        greenButton.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.GREEN)));
        yellowButton.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.YELLOW)));
        blackButton.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.BLACK)));
        orangeButton.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.ORANGE)));
        purpleButton.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.PURPLE)));
        whiteButton.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.WHITE)));
        wildButton.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.WILD)));
    }

    private void setRoute(Route route) {
        selectedRoute = route;
        if (selectedRoute == null) {
            updateButton();
            startCity.setText("");
            destination.setText("");
            colorField.setText("");
            lengthField.setText("");
        } else {
            updateButton();
            startCity.setText(String.format("Start: %s", selectedRoute.getStartCity().getCity()));
            destination.setText(String.format("Destination: %s", selectedRoute.getEndCity().getCity()));
            colorField.setText(String.format("Route Color: %s", selectedRoute.getColor().getName()));
            lengthField.setText(String.format("Length: %d", selectedRoute.getLength()));
        }
    }

    private void updateButton() {
        boolean enabled = (selectedRoute != null && (selectedRoute.getColor() != TrainColor.ANY || selectedColor != null));
        claimRoute.setEnabled(enabled);
        if (enabled && selectedRoute.getColor().getName().equals(TrainColor.ANY.getName())) {
            displayMessage("Chosen color is " + selectedColor.getName());
        }
    }

    private class RouteHolder extends RecyclerView.ViewHolder {
        private TextView cities;

        public RouteHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.route_entry, parent, false));

            cities = itemView.findViewById(R.id.cities);
        }

        public void textWeightSetter(){
            for(TextView route : routeTextViews){
                route.setTypeface(Typeface.DEFAULT);
            }
            cities.setTypeface(Typeface.DEFAULT_BOLD);
        }

        public void bind(final Route route) {
            Log.d(TAG, "Binding " + route.getStartCity().getCity() + " --> " + route.getEndCity().getCity());
            cities.setText(String.format("%s --> %s Lenght: %d Color: %s", route.getStartCity().getCity(), route.getEndCity().getCity(), route.getLength(), route.getColor().getName()));
            routeTextViews.add(cities);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setRoute(route);
                    textWeightSetter();
                }
            });

            switch (route.getColor().getName()) {
                case "Red":
                    cities.setBackgroundColor(Color.rgb(165, 28, 28));
                    cities.setTextColor(Color.WHITE);
                    break;
                case "Green":
                    cities.setBackgroundColor(Color.rgb(0,128,0));
                    cities.setTextColor(Color.WHITE);
                    break;
                case "Yellow":
                    cities.setBackgroundColor(Color.rgb(220, 220, 34));
                    cities.setTextColor(Color.BLACK);
                    break;
                case "Black":
                    cities.setBackgroundColor(Color.BLACK);
                    cities.setTextColor(Color.WHITE);
                    break;
                case "Blue":
                    cities.setBackgroundColor(Color.BLUE);
                    cities.setTextColor(Color.WHITE);
                    break;
                case "Purple":
                    cities.setBackgroundColor(Color.rgb(96, 38, 155));
                    cities.setTextColor(Color.WHITE);
                    break;
                case "Orange":
                    cities.setBackgroundColor(Color.rgb(255, 165, 0));
                    cities.setTextColor(Color.WHITE);
                    break;
                case "White":
                    cities.setBackgroundColor(Color.rgb(255, 255, 242));
                    cities.setTextColor(Color.BLACK);
                    break;
                default:
                    cities.setBackgroundColor(Color.GRAY);
                    cities.setTextColor(Color.BLACK);
                    break;
            }
        }
    }

    private class RouteAdapter extends RecyclerView.Adapter<RouteHolder> {
        private List<Route> routes;

        public RouteAdapter(List<Route> routes) {
            this.routes = routes;
        }

        @Override
        public RouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(ClaimRouteView.this);
            return new RouteHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(RouteHolder holder, int i) {
            holder.bind(routes.get(i));
        }

        public int getItemCount() {
            return routes.size();
        }
    }
}
