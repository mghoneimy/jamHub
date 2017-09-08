package com.Example.iJam.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Example.iJam.R;
import com.Example.iJam.models.User;
import com.Example.iJam.network.HttpPostTask;
import com.Example.iJam.network.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_user_name;
    EditText et_password;
    EditText et_ip;
    Button signup;
    Button signin;

    public static final String PREFS_NAME = "MyPrefsFile";

    SharedPreferences settings = null;
    Boolean signedIn = false;

    String user_name = null;
    String password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_signin);

        et_user_name = (EditText) findViewById(R.id.signin_et_username);
        et_password = (EditText) findViewById(R.id.signin_et_pass);
        et_ip = (EditText) findViewById(R.id.signin_et_ip);
        signin = (Button) findViewById(R.id.signin_bt_signin);
        signup = (Button) findViewById(R.id.signin_bt_signup);

        settings = getSharedPreferences(PREFS_NAME, 0);
        signedIn = settings.getBoolean("signIn", false);

//        if (signedIn) {
//            Toast.makeText(getApplicationContext(), "Intializing...Please Wait!", Toast.LENGTH_SHORT).show();
//            user_name = settings.getString("user_name", user_name);
//            password = settings.getString("password", password);
//            try {
//                signInUser(user_name, password);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

        TextView mTextView = (TextView) findViewById(R.id.signin_txt_forget);
        mTextView.setPaintFlags(mTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mTextView.setOnClickListener(this);

        signin.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signInUser(String user_name, String password) throws JSONException {

        JSONObject login_info = new JSONObject();
        login_info.put("user_name", user_name);
        login_info.put("password", password);

        new HttpPostTask(ServerManager.getServerURL()+"/users/login.php", getApplicationContext()) {
            @Override
            protected void onPostExecute(String s) {
                try {
                    JSONObject response = new JSONObject(s);
                    String status = response.getString("status");
                    if (status.equals("fail")) {
                        Toast.makeText(ctx, "Log in failed! " + response.getString("error"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ctx, "Success", Toast.LENGTH_SHORT).show();
                        JSONObject user_info = response.getJSONObject("user");
                        User u = User.parseJson(user_info);
                        u.setImgUrl(ServerManager.getServerURL() + u.getImgUrl());

                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("user_name", u.getUser_name());
                        editor.putString("password", u.getPassword());
                        editor.putBoolean("signIn", true);
                        editor.commit();

                        Intent i = new Intent(ctx, MainActivity.class);
                        i.putExtra("user", u);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(i);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(login_info);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_txt_forget:
                Intent i = new Intent(SignInActivity.this,ForgotPassword.class);
                startActivity(i);
                break;
            case R.id.signin_bt_signup:
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.signin_bt_signin:
                ServerManager.setServerURL(et_ip.getText().toString().trim());
                user_name = et_user_name.getText().toString().trim();
                password = et_password.getText().toString().trim();

                if (user_name.equals("") || password.equals(""))
                    Toast.makeText(getApplicationContext(), "one or more of the fields is empty!", Toast.LENGTH_SHORT).show();
                else
                    try {
                        signInUser(user_name, password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                break;
        }
    }
}
