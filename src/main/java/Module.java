package com.example.fred.securitycenter;

import java.io.Serializable;

/**
 * Created by FRED on 11/04/2017.
 */

public class Module implements Serializable {

    int id_modulo;
    String dominio;

    public Module (int id_modulo, String dominio)
    {
        super();
        this.id_modulo = id_modulo;
        this.dominio = dominio;
    }

    public String getId_modulo()
    {
        String id_mdl = String.valueOf(id_modulo);
        return id_mdl;
    }

    public void setId_modulo(int id_modulo)
    {
        this.id_modulo = id_modulo;
    }

    public String getDominio()
    {
        return dominio;
    }

    public void setDominio(String dominio)
    {
        this.dominio = dominio;
    }
}
