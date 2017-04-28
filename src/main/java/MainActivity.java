package com.example.fred.securitycenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{

    Button btn_loginSubmitt;
    EditText editText_userName, editText_userPassword;
    private AccessServiceAPI m_serviceAccess;
    private ProgressDialog m_progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_userName = (EditText) findViewById(R.id.editText_userName);
        editText_userPassword = (EditText) findViewById(R.id.editText_userPassword);
        btn_loginSubmitt = (Button) findViewById(R.id.btn_loginSubmitt);
        m_serviceAccess = new AccessServiceAPI();


        btn_loginSubmitt.setOnClickListener(new View.OnClickListener()
        {
            public void onClick (View v)
            {
                /*
                if (editText_userName.getText().toString().equals("") ||
                        editText_userPassword.getText().toString().equals(""))
                {
                    //editText_userName.setError("Dados inválidos.");
                    Toast.makeText(getApplicationContext(), "Dados inválidos!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (editText_userName.getText().toString().equals("Admin") &&
                    editText_userPassword.getText().toString().equals("password"))
                {
                    Toast.makeText(getApplicationContext(), "Login realizado!", Toast.LENGTH_LONG).show();
                    Intent myintent = new Intent(MainActivity.this, LoggedActivity.class);
                    myintent.putExtra("username", editText_userName.getText().toString());
                    startActivity(myintent);
                }
                */
                //Validate input
                if ("".equals(editText_userName.getText().toString()))
                {
                    editText_userName.setError("Informe o usuário!");
                    return;
                }
                if ("".equals(editText_userPassword.getText().toString()))
                {
                    editText_userPassword.setError("Informe a senha!");
                    return;
                }
                //Call async task to login
                new TaskLogin().execute(editText_userName.getText().toString(), editText_userPassword.getText().toString());

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1)
        {
            editText_userName.setText(data.getStringExtra("username"));
            editText_userPassword.setText(data.getStringExtra("password"));
        }
    }

    public class TaskLogin extends AsyncTask<String, Void, Integer>
    {
        @Override
        protected  void onPreExecute()
        {
            super.onPreExecute();
            //Abre dialog de progresso durante o login
            m_progressDialog = ProgressDialog.show(MainActivity.this, "Espere por favor...", "Em progresso...", true);
        }

        @Override
        protected Integer doInBackground(String... params)
        {
            //Cria dados para passar nos 'params'
            Map<String, String> param = new HashMap<>();
            param.put("action", "login");
            param.put("username", params[0]);
            param.put("password", params[1]);

            JSONObject jObjectResult;
            try
            {
                jObjectResult = m_serviceAccess.convertJSONString2Obj(m_serviceAccess.getJSONStringWithParam_POST(Common.SERVICE_API_URL, param));
                return jObjectResult.getInt("result");
            }
            catch (Exception e)
            {
                return Common.RESULT_ERROR;
            }
            //return null;
        }

        @Override
        protected void onPostExecute (Integer result)
        {
            super.onPostExecute(result);
            m_progressDialog.dismiss();
            if (Common.RESULT_SUCCESS == result)
            {
                Toast.makeText(getApplicationContext(),"Login efetuado",Toast.LENGTH_LONG).show();
                Intent myintent = new Intent(MainActivity.this, LoggedActivity.class);
                myintent.putExtra("username", editText_userName.getText().toString());
                //myintent.putExtra("userData", egetData(daklsdjaskljdajlkd)); HttpRequest para trazer dados direto pra um array
                startActivity(myintent);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Falha no login",Toast.LENGTH_LONG).show();
            }
        }
    }

}

