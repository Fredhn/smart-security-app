package com.example.fred.securitycenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by FRED on 21/04/2017.
 */

public class ModuleAdapter extends ArrayAdapter<Module>
{
    private List<Module> itemList;
    private Context context;

    public ModuleAdapter(List<Module> itemList, Context ctx)
    {
        super(ctx, android.R.layout.simple_list_item_1, itemList);
        this.itemList = itemList;
        this.context = ctx;
    }

    public int getCount()
    {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public Module getItem(int position)
    {
        if (itemList != null)
            return itemList.get(position);
        return null;
    }

    public long getItemId(int position)
    {
        if (itemList != null)
            return itemList.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View v = View.inflate(context, R.layout.item_module_list, null);


        Module mdl = itemList.get(position);TextView text = (TextView) v.findViewById(R.id.id_modulo);
        text.setText(mdl.getId_modulo());


        TextView text1 = (TextView) v.findViewById(R.id.dominio);
        text1.setText(mdl.getDominio());


        return v;
    }

    public List<Module> getItemList()
    {
        return itemList;
    }

    public void setItemList(List<Module> itemList)
    {
        this.itemList = itemList;
    }

}
