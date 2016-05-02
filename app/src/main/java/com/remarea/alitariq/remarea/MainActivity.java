package com.remarea.alitariq.remarea;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;

import org.bson.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener {
    EditText username, password;
    Button login;
    TextView register;

    ProgressDialog progress;

    public static DB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress = ProgressDialog.show(this, "Please Wait", "Connecting with Server!", true);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        View v = findViewById(R.id.login);
        View root = v.getRootView();
        root.setBackgroundColor(getResources().getColor(android.R.color.white));

        username= (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);

        new AsynchronousDatabaseConnector().execute();
    }//OnCreate method ends here

    @Override
    public void onClick(View v)
    {
        if (v==findViewById(R.id.login))
        {
            progress = ProgressDialog.show(this, "Please Wait", "Logging In!", true);

            String user = username.getText().toString();
            String pass = password.getText().toString();

            new AsynchronousLogin().execute(user, pass);
        }
        else if (v==findViewById(R.id.register))
        {
            Intent in = new Intent(this, RegisterActivity.class);
            startActivity(in);
        }
    }//OnClick method ends here

    public void turnOff(){
        this.finishAffinity();
    }

    private class AsynchronousDatabaseConnector extends AsyncTask<String, Void, Void> {
        private final String addr = "mongodb://mainuser:seecs123@ds017070.mlab.com:17070/remarea";

        @Override
        protected Void doInBackground(String... params) {
            MongoClientURI uri = new MongoClientURI(addr);
            MongoClient mongoclient =  new MongoClient(uri);
            database = mongoclient.getDB(uri.getDatabase());
            try {
                database.command("ping");
            } catch (MongoTimeoutException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();

                        Toast.makeText(MainActivity.this, "Connection Failed!", Toast.LENGTH_LONG).show();

                        turnOff();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progress.dismiss();
            Toast.makeText(MainActivity.this, "Connection Successful!", Toast.LENGTH_LONG).show();
        }
    }

    private class AsynchronousLogin extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... params) {
            DBCursor cursor = database.getCollection("users").find(new BasicDBObject("username", params[0]));

            progress.dismiss();

            if(cursor.hasNext()){
                DBObject data = cursor.next();

                if(data.get("password").equals(params[1])){
                    Intent myIntent = new Intent(MainActivity.this, userHome.class);
                    myIntent.putExtra("username", params[0]);
                    MainActivity.this.startActivity(myIntent);
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Incorrect Password!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Incorrect Username!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }
    }
}
