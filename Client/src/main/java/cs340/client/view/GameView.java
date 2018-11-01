package cs340.client.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import Utils.TrainColor;
import cs340.client.R;
import cs340.client.presenter.GamePresenter;
import cs340.client.presenter.IGamePresenter;
import model.Player;

public class GameView extends AbstractGameView {
    private static final String TAG = "GameView";

    MapFragment fragment;
    IGamePresenter presenter;

    @Override
    public void update() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attach(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.game_view);

        FragmentManager fm = this.getSupportFragmentManager();
        fragment = new MapFragment();
        fm.beginTransaction().add(R.id.fragment_container, fragment).commit();

        presenter = GamePresenter.getInstance();
    }

    public void setButtonsEnabled(boolean enabled) {
        fragment.setButtonsEnabled(enabled);
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    public void updateClaimedRoutes() {
        fragment.displayClaimedRoutes();
    }


}