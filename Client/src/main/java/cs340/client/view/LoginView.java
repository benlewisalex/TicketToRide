package cs340.client.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cs340.client.R;
import cs340.client.presenter.ILoginPresenter;
import cs340.client.presenter.LoginPresenter;

public class LoginView extends AppCompatActivity implements ILoginView
{
    private static final String TAG = "LoginView";

    private String loginUsername;
    private String loginPassword;
    private String registerUsername;
    private String registerPassword;
    private String confirmPassword;
    private String ipAddress;
    private String port;

    private boolean loginUsernameValid;
    private boolean loginPasswordValid;
    private boolean registerUsernameValid;
    private boolean registerPasswordValid;
    private boolean ipValid;
    private boolean portValid;

    private ILoginPresenter presenter;

    private Button loginButton;
    private Button registerButton;
    private EditText loginUsernameField;
    private EditText loginPasswordField;
    private EditText registerUsernameField;
    private EditText registerPasswordField;
    private EditText confirmPasswordField;
    private EditText ipField;
    private EditText portField;

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attach(this);
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.login_view);

        presenter = LoginPresenter.getInstance();

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setEnabled(false);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.login();
                loginButton.setEnabled(false);
            }
        });

        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setEnabled(false);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.register();
                registerButton.setEnabled(false);
            }
        });

        loginUsernameField = (EditText) findViewById(R.id.login_username);
        loginUsernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginUsername = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginUsernameValid = loginUsername.length() > 0;
                updateLoginButton();
            }
        });

        loginPasswordField =(EditText) findViewById(R.id.login_password);
        loginPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginPassword = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginPasswordValid = loginPassword.length() > 0;
                updateLoginButton();
            }
        });

        registerUsernameField = (EditText) findViewById(R.id.register_username);
        registerUsernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerUsername = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerUsernameValid = registerUsername.length() > 0;
                updateRegisterButton();
            }
        });

        registerPasswordField = (EditText) findViewById(R.id.register_password);
        registerPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerPassword = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerPasswordValid = registerPassword.length() > 0;
                updateRegisterButton();
            }
        });

        confirmPasswordField = (EditText) findViewById(R.id.confirm_password);
        confirmPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmPassword = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateRegisterButton();
            }
        });

        ipField = findViewById(R.id.ip_address);
        ipField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ipAddress = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ipValid = ipAddress.length() > 0;
                updateLoginButton();
                updateRegisterButton();
            }
        });

        portField = findViewById(R.id.port);
        portField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                port = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                portValid = port.length() > 0;
                updateLoginButton();
                updateRegisterButton();
            }
        });
    }

    @Override
    public String getLoginUsername() {
        return loginUsername;
    }

    @Override
    public String getLoginPassword() {
        return loginPassword;
    }

    @Override
    public String getRegisterUsername() {
        return registerUsername;
    }

    @Override
    public String getRegisterPassword() {
        return registerPassword;
    }

    @Override
    public String getIPAddress() {
        return ipAddress;
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public void login() {
        Intent intent = new Intent(this, LobbyView.class);
        startActivity(intent);
    }

    @Override
    public void displayMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void updateLoginButton() {
        loginButton.setEnabled(loginUsernameValid && loginPasswordValid && ipValid && portValid);
    }

    public void updateRegisterButton() {
        registerButton.setEnabled(registerUsernameValid && registerPasswordValid && registerPassword.equals(confirmPassword) && ipValid && portValid);
    }
}
