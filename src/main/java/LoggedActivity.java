package com.example.fred.securitycenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoggedActivity extends AppCompatActivity {

    private AccessServiceAPI m_serviceAccess;
    private ProgressDialog m_progressDialog;
    private ListView listaModulos;
    private List<Module> customList;
    private ModuleAdapter adapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        m_serviceAccess = new AccessServiceAPI();
        adapter = new ModuleAdapter(new ArrayList(), this);
        listaModulos = (ListView) findViewById(R.id.listView_modules);
        listaModulos.setAdapter(adapter);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        Log.d("GetExtra (Username): ",username);
        new TaskModules().execute(username.toString());

        //CLICK EVENT: ListView
        listaModulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int codigoModulo = position;
                Module moduloClicado = (Module) listaModulos.getItemAtPosition(codigoModulo);
                Toast.makeText(getApplicationContext(), moduloClicado.getDominio(), Toast.LENGTH_SHORT).show();
                try
                {
                    Class moduloAtivo = Class.forName("com.example.fred.securitycenter.ModuleActivity");
                    Intent intSelecionado = new Intent(getApplicationContext(), moduloAtivo);
                    intSelecionado.putExtra("id_modulo",moduloClicado.getId_modulo().toString());
                    intSelecionado.putExtra("dominio",moduloClicado.getDominio().toString());
                    intSelecionado.putExtra("username", username.toString());
                    startActivity(intSelecionado);

                } catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        });
        //CLICK EVENT: FIM

    }//fim OnCreate

    //AsyncTask pra montar o listview
    public class TaskModules extends AsyncTask<String, Void, List<Module>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Abre dialog de progresso durante o login
            m_progressDialog = ProgressDialog.show(LoggedActivity.this, "Espere por favor...", "Em progresso...", true);
        }

        @Override
        protected List<Module> doInBackground(String... params) {
            
            try {

                customList = new ArrayList<>();
                Map<String, String> param = new HashMap<>();
                param.put("action", "loadModules");
                param.put("username", params[0]);

                //Recebe a resposta do web service (JsonArray) para montar a listview de módulos:
                JSONArray arr = m_serviceAccess.getJSONArrayWithParam_POST(Common.SERVICE_API_URL, param);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Module>>(){}.getType();
                customList = gson.fromJson(arr.toString(), type);

            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
            //return null;
            return customList;
        }

        @Override
        protected void onPostExecute(List<Module> result)
        {
            super.onPostExecute(result);
            m_progressDialog.dismiss();

            adapter.setItemList(result);
            adapter.notifyDataSetChanged();
        }
    }

}
