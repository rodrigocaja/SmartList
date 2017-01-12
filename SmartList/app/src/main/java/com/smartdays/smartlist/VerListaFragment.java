package com.smartdays.smartlist;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class VerListaFragment extends Fragment {
    private SQLiteDatabase db = null;
    private SimpleCursorAdapter adt = null;
    private ArrayList<String> mArrayList = new ArrayList<String>();

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

            mArrayList.clear();
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                // The Cursor is now set to the right position
                mArrayList.add(cursor.getString(cursor.getColumnIndex("_id")));
            }

            //Registrando para menu de contexto
            registerForContextMenu(ltwListasSalvas);

            //Seleção de registro no listview
            ltwListasSalvas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor retornoCursor = (Cursor) adt.getItem(i);

                    Bundle args = new Bundle();
                    args.putString("idLista", retornoCursor.getString(retornoCursor.getColumnIndex("_id")));
                    args.putString("nomeLista", retornoCursor.getString(retornoCursor.getColumnIndex("listanome")));

                    MainActivity activity = (MainActivity) getActivity();
                    activity.callListaItemFragment(args);
                }
            });


        }

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        if (v.getId() == R.id.ltwListasSalvas) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(getResources().getString(R.string.optionsDesc));
            menu.add(0, 1, 1, getResources().getString(R.string.actionDelete));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //int menuItemIndex = item.getItemId();
        String chave = mArrayList.get(info.position);

        deleteList(chave);
        return true;
    }

    public void deleteList(String id) {
        db.delete("lista", "_id = " + id, null);

        Cursor cursor = db.rawQuery("select lista._id as _id, lista.nome as listanome, (select count(distinct lista_item.produto) " +
                "from lista_item where lista._id = lista_item.lista) as qtdeitens from lista;", null);
        adt.changeCursor(cursor);

        //Log do sistema
        LogSis log = new LogSis();
        String tabela = "lista_item";
        String acao = "Apagou todos os itens da lista " + id;
        log.gravaLog(db, tabela, acao);

        tabela = "lista";
        acao = "Apagou a lista " + id;
        log.gravaLog(db, tabela, acao);


        mArrayList.clear();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // The Cursor is now set to the right position
            mArrayList.add(cursor.getString(cursor.getColumnIndex("_id")));
        }

        Log.v("DEBUG", id);
    }
}
