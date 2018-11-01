package cs340.client.view;

import android.content.Intent;

public interface ILoginView
{
    public String getLoginUsername();
    public String getLoginPassword();
    public String getRegisterUsername();
    public String getRegisterPassword();
    public String getIPAddress();
    public String getPort();

    public void login();

    public void displayMessage(String msg);
}
