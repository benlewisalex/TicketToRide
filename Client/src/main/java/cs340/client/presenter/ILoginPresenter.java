package cs340.client.presenter;

import cs340.client.view.ILoginView;

public interface ILoginPresenter
{
    public void attach(ILoginView view);
    public void login();
    public void register();
}
