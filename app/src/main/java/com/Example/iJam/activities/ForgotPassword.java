package com.Example.iJam.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Example.iJam.R;
import com.Example.iJam.network.HttpGetTask;
import com.Example.iJam.network.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class ForgotPassword extends Activity {

    private String emaill, password = null;
    private Session session;
    private EditText mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mail=(EditText)findViewById(R.id.emailTxt);
        Button sendemail = (Button)findViewById(R.id.sendpasswordButton);

        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emaill = mail.getText().toString().trim();

                new HttpGetTask(ServerManager.getServerURL()+"/tracks/password.php?mail="+emaill, getApplicationContext()){
                    @Override
                    protected void onPostExecute(String s) {
                        try {
                            JSONObject response = new JSONObject(s);
                            if (response.getString("status").equals("success")) {
                                password = response.getString("password");

                                Properties props = new Properties();
                                props.put("mail.smtp.host", "smtp.gmail.com");
                                props.put("mail.smtp.socketFactory.port", "465");
                                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                                props.put("mail.smtp.auth", "true");
                                props.put("mail.smtp.port", "465");
                                session = Session.getDefaultInstance(props, new Authenticator() {
                                    @Override
                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        return new PasswordAuthentication("jamhubapp@gmail.com", "jamhub123");
                                    }
                                });
                                RetreiveFeedTask task = new RetreiveFeedTask();
                                task.execute();

                                finish();
                            }
                            else
                                Toast.makeText(ctx, "email not found in our database!", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.execute();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
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

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("jamhub"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emaill));
                message.setSubject("Retrieving password");
                message.setContent("Your password is "+password, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Password sent", Toast.LENGTH_SHORT).show();
        }
    }
}
