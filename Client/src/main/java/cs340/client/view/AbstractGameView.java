package cs340.client.view;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public abstract class AbstractGameView extends AppCompatActivity {
    public abstract void update();
    public void displayMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
