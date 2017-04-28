package com.example.fred.securitycenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModuleActivity extends AppCompatActivity {

    Button btn_activateModule;
    private AccessServiceAPI m_serviceAccess;
    private ProgressDialog m_progressDialog;
    private String nomeDominio;
    private String id_modulo;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_module);

        //Button: ActivateModule
        btn_activateModule = (Button) findViewById(R.id.btn_activateModule);

        //API: Conexão com o webservice
        m_serviceAccess = new AccessServiceAPI();

        //Formata Activity para Pop-up
        DisplayMetrics dn = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dn);
        int width = dn.widthPixels;
        int height = dn.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.5));

        //Recupera Extra Data do Intent
        Intent intent = getIntent();
        nomeDominio = intent.getStringExtra("dominio");
        id_modulo = intent.getStringExtra("id_modulo");
        username = intent.getStringExtra("username");
        //Coloca nome do dominio recuperado na Extra Data do Intent no TextView do Pop-up
        TextView text = (TextView) findViewById(R.id.textView_moduleTitle);
        text.setText(nomeDominio);

        //Click Listener: Button ActivateModule
        btn_activateModule.setOnClickListener(new View.OnClickListener()
        {
            public void onClick (View v)
            {
                new TaskActivateModule().execute(id_modulo.toString(),username.toString(),nomeDominio.toString());
            }
        });
    }

    public class TaskActivateModule extends AsyncTask<String, Void, Integer>
    {
        @Override
        protected  void onPreExecute()
        {
            super.onPreExecute();
            //Abre dialog de progresso durante ativação do modulo
            m_progressDialog = ProgressDialog.show(ModuleActivity.this, "Espere por favor...", "Em progresso...", true);
        }

        @Override
        protected Integer doInBackground(String... params)
        {
            //Cria dados para passar nos 'params'
            Map<String, String> param = new HashMap<>();
            param.put("action", "activateModule");
            param.put("id_module", params[0]);
            param.put("username", params[1]);
            param.put("desc", params[2]);

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
                Toast.makeText(getApplicationContext(),nomeDominio + " ativado!",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),nomeDominio + " não ativado!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
