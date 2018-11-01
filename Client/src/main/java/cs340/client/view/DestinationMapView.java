package cs340.client.view;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import Utils.PlayerColor;
import Utils.TrainColor;
import cs340.client.R;
import cs340.client.model.ClientModel;
import cs340.client.model.UIFacade;
import cs340.client.presenter.IGamePresenter;
import model.ActiveGame;
import model.Location;
import model.Player;
import model.Route;

public class DestinationMapView extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private TextView numBlueCards;
    private TextView numRedCards;
    private TextView numGreenCards;
    private TextView numYellowCards;
    private TextView numBlackCards;
    private TextView numPurpleCards;
    private TextView numOrangeCards;
    private TextView numWhiteCards;
    private TextView numWildCards;

    private List<Polyline> unclaimedRoutes = new ArrayList<>();
    private List<Polyline> claimedRoutes = new ArrayList<>();
    private List<Route> drawnRoutes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_map_fragment);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        numBlueCards = (TextView) findViewById(R.id.blue_cardCount);
        numRedCards = (TextView) findViewById(R.id.red_cardCount);
        numGreenCards = (TextView) findViewById(R.id.green_cardCount);
        numYellowCards = (TextView) findViewById(R.id.yellow_cardCount);
        numBlackCards = (TextView) findViewById(R.id.black_cardCount);
        numOrangeCards = (TextView) findViewById(R.id.orange_cardCount);
        numPurpleCards = (TextView) findViewById(R.id.purple_cardCount);
        numWhiteCards = (TextView) findViewById(R.id.white_cardCount);
        numWildCards = (TextView) findViewById(R.id.wild_cardCount);

        setCardNumbers(UIFacade.getInstance().getClientPlayer());

    }

    public void setCardNumbers(Player player) {
        numBlueCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.BLUE)));
        numRedCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.RED)));
        numGreenCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.GREEN)));
        numYellowCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.YELLOW)));
        numBlackCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.BLACK)));
        numOrangeCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.ORANGE)));
        numPurpleCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.PURPLE)));
        numWhiteCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.WHITE)));
        numWildCards.setText(String.valueOf(player.getNumColoredTrainCardCards(TrainColor.WILD)));
    }

    @Override
    public void onResume() {
        super.onResume();
        setCardNumbers(UIFacade.getInstance().getClientPlayer());
        if (mMap != null) {
            for (Polyline polyline : unclaimedRoutes) {
                polyline.remove();
            }
            for (Polyline polyline : claimedRoutes) {
                polyline.remove();
            }
            unclaimedRoutes.clear();
            claimedRoutes.clear();
            drawnRoutes.clear();
            displayUnclaimedRoutes();
            displayClaimedRoutes();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        LatLng unitedStatesCenter = new LatLng(41.150609, -100.136645);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(unitedStatesCenter));
        mMap.setMaxZoomPreference(4.75f);
        mMap.setMinZoomPreference(4.75f);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        displayCities();
        displayUnclaimedRoutes();
        displayClaimedRoutes();
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    public void displayMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void displayCities() {
        ActiveGame game = ClientModel.getInstance().getActiveGame();
        List<Location> cites = game.getCities();
        for (Location city : cites) {
            LatLng cityLocation = new LatLng(city.getLatitude(), city.getLongitude());
            MarkerOptions marker = new MarkerOptions()
                    .position(cityLocation)
                    .title(city.getCity() + ", " + city.getCountry())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
            mMap.addMarker(marker);
        }
    }

    public void displayUnclaimedRoutes() {
        ActiveGame game = ClientModel.getInstance().getActiveGame();
        List<Route> routes = game.getUnclaimedRoutes();
        for (Route route : routes) {


            boolean dualRoute = false;
            for (Route claimedRoute : drawnRoutes) {
                if (claimedRoute.getStartCity().getCity().equals(route.getStartCity().getCity()) &&
                        claimedRoute.getEndCity().getCity().equals(route.getEndCity().getCity())){
                    dualRoute = true;
                }
            }
//            for (Route unclaimedRoute : ClientModel.getInstance().getActiveGame().getUnclaimedRoutes()) {
//                if (unclaimedRoute.getStartCity().getCity().equals(route.getStartCity().getCity()) &&
//                        unclaimedRoute.getEndCity().getCity().equals(route.getEndCity().getCity())){
//                    dualRoute = true;
//                }
//            }

            LatLng startLatLng = new LatLng(route.getStartCity().getLatitude(), route.getStartCity().getLongitude());
            LatLng endLatLng = new LatLng(route.getEndCity().getLatitude(), route.getEndCity().getLongitude());

            if(dualRoute){
                startLatLng = new LatLng(route.getStartCity().getLatitude() + .2, route.getStartCity().getLongitude() + .2);
                endLatLng = new LatLng(route.getEndCity().getLatitude() + .2, route.getEndCity().getLongitude() + .2);
            }




            Polyline polyline = mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .color(getPolylineColor(route.getColor()))
                    .width(6)
                    .add(startLatLng, endLatLng)
            );
            unclaimedRoutes.add(polyline);
            drawnRoutes.add(route);
        }
    }

    public void displayClaimedRoutes() {
        ActiveGame game = ClientModel.getInstance().getActiveGame();
        List<Route> routes = game.getRoutes();
        for (Route route : routes) {
            if (!route.isClaimed()) { continue; }
            boolean dualRoute = false;
            for (Route claimedRoute : drawnRoutes) {
                if (claimedRoute.getStartCity().getCity().equals(route.getStartCity().getCity()) &&
                        claimedRoute.getEndCity().getCity().equals(route.getEndCity().getCity())){
                    dualRoute = true;
                }
            }
//            for (Route unclaimedRoute : ClientModel.getInstance().getActiveGame().getUnclaimedRoutes()) {
//                if (unclaimedRoute.getStartCity().getCity().equals(route.getStartCity().getCity()) &&
//                        unclaimedRoute.getEndCity().getCity().equals(route.getEndCity().getCity())){
//                    dualRoute = true;
//                }
//            }

            LatLng startLatLng = new LatLng(route.getStartCity().getLatitude(), route.getStartCity().getLongitude());
            LatLng endLatLng = new LatLng(route.getEndCity().getLatitude(), route.getEndCity().getLongitude());

            if(dualRoute){
                startLatLng = new LatLng(route.getStartCity().getLatitude() + .2, route.getStartCity().getLongitude() + .2);
                endLatLng = new LatLng(route.getEndCity().getLatitude() + .2, route.getEndCity().getLongitude() + .2);
            }

            Polyline polyline = mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .color(getClaimedPolylineColor(route.getOwner().getColor()))
                    .width(15)
                    .add(startLatLng, endLatLng)
            );
            claimedRoutes.add(polyline);
            drawnRoutes.add(route);
        }
    }

    public int getPolylineColor(TrainColor color) {
        switch (color.getValue()) {
            case 0:
                return Color.rgb(96, 38, 155);//purple
            case 1:
                return Color.rgb(255, 255, 242);
            case 2:
                return Color.BLUE;
            case 3:
                return Color.rgb(220, 220, 34);//yellow
            case 4:
                return Color.rgb(255, 165, 0);//orange
            case 5:
                return Color.BLACK;
            case 6:
                return Color.rgb(165, 28, 28);//red
            case 7:
                return Color.rgb(0,128,0);//green
            case 9:
                return Color.GRAY;
            default:
                return 0;

        }
    }

    public int getClaimedPolylineColor(PlayerColor color) {
        switch (color.getValue()) {
            case 0:
                return Color.BLUE;
            case 1:
                return Color.RED;
            case 2:
                return Color.BLACK;
            case 3:
                return Color.rgb(0,128,0);//green
            case 4:
                return Color.YELLOW;
            default:
                return 0;

        }
    }
}
