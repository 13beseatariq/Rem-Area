package com.remarea.alitariq.remarea;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;

import java.util.Date;

public class RegisterActivity extends Activity  implements View.OnClickListener{
    private EditText email, username, password, confirmPassword;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        View v = findViewById(R.id.email);
        View root = v.getRootView();
        root.setBackgroundColor(getResources().getColor(android.R.color.white));

        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.password1);

        findViewById(R.id.register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.register)){
            if(password.getText().toString().equals(confirmPassword.getText().toString())){
                progress = ProgressDialog.show(this, "Please Wait", "Registering User!", true);

                new AsynchronousRegisteration().execute(username.getText().toString(),
                        email.getText().toString(), password.getText().toString());
            }
            else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "Password doesn't match!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private class AsynchronousRegisteration extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            DBCollection col = MainActivity.database.getCollection("users");

            BasicDBObject doc = new BasicDBObject("username", params[0])
                    .append("email", params[1])
                    .append("password", params[2])
                    .append("registeration_time", new Date().getTime());
            try {
                col.insert(doc);

                progress.dismiss();

                Intent myIntent = new Intent(RegisterActivity.this, userHome.class);
                myIntent.putExtra("username", params[0]);
                RegisterActivity.this.startActivity(myIntent);
            }
            catch (MongoTimeoutException e){
                progress.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "Connection Error!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            catch(MongoException e){
                progress.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "Username or Email already Exist!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }
    }
}
