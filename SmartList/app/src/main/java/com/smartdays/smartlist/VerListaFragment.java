package com.smartdays.smartlist;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class VerListaFragment extends Fragment {
    private SQLiteDatabase db = null;
    private SimpleCursorAdapter adt = null;

    public VerListaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_ver_lista, container, false);

        if(rootView != null){
            Context c = getContext().getApplicationContext();
            String DB_PATH = c.getDatabasePath("smartlistapp.db").getPath();
            db = openOrCreateDatabase(DB_PATH, null);

            //Carregando listas salvas
            Cursor cursor = db.rawQuery("select lista._id as _id, lista.nome as listanome, (select count(distinct lista_item.produto) " +
                    "from lista_item where lista._id = lista_item.lista) as qtdeitens from lista;", null);
            String[] campos = {"_id", "listanome", "qtdeitens"};
            int[] ids = {R.id.txvListaID, R.id.txtNomeLista, R.id.txtQtde};

            adt = new SimpleCursorAdapter(getContext(), R.layout.model_ver_lista, cursor, campos, ids, 0);
            ListView ltwListasSalvas = (ListView) rootView.findViewById(R.id.ltwListasSalvas);

            ltwListasSalvas.setAdapter(adt);

            /*
            //Seleção de registro no listview
            ltwListasSalvas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor retornoCursor = (Cursor) adt.getItem(i);
                    Intent it = new Intent(getBaseContext(), ListaItemActivity.class);
                    it.putExtra("idLista", retornoCursor.getString(retornoCursor.getColumnIndex("_id")));
                    it.putExtra("nomeLista", retornoCursor.getString(retornoCursor.getColumnIndex("listanome")));

                    startActivity(it);
                }
            });
            */

        }

        return rootView;
    }

}
