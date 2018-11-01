package cs340.client.presenter;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import cs340.client.model.ClientModel;
import cs340.client.model.IUIFacade;
import cs340.client.model.UIFacade;
import cs340.client.view.ILoginView;

public class LoginPresenter implements ILoginPresenter, Observer {
    private static final String TAG = "LoginPresenter";
    private static LoginPresenter instance;

    private ILoginView view;
    private IUIFacade facade;

    private LoginPresenter()
    {
        facade = UIFacade.getInstance();
    }

    public static LoginPresenter getInstance() {
        if (instance == null)
            instance = new LoginPresenter();
        return instance;
    }

    private void setServer() {
        facade.setIPandPort(view.getIPAddress(), view.getPort());
    }

    @Override
    public void attach(ILoginView view) {
        this.view = view;

        Observable o = ClientModel.getInstance();
        o.addObserver(this);
    }

    public void update(Observable o, Object arg)
    {
        Log.d(TAG, "Received update notification");
        if (ClientModel.getInstance().isLoggedIn()) {
            o.deleteObserver(this);
            view.login();
        } else {
            view.displayMessage(ClientModel.getInstance().getToastMessage());
        }
    }

    @Override
    public void login() {
        setServer();
        String username = view.getLoginUsername();
        String password = view.getLoginPassword();

        if (username == null) {
            view.displayMessage("Please enter your username");
        } else if (password == null) {
            view.displayMessage("Please enter your password");
        } else {
            Log.d(TAG, String.format("Logging in with username %s", username));
            facade.requestLogin(username, password);
        }
    }

    @Override
    public void register() {
        setServer();
        String username = view.getRegisterUsername();
        String password = view.getRegisterPassword();

        if (username == null) {
            view.displayMessage("Please provide a username");
        } else if (password == null) {
            view.displayMessage("Please provide a password");
        } else {
            Log.d(TAG, String.format("Registering new user %s", username));
            facade.requestRegister(username, password);
        }
    }
}
