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
public class VerComprasFragment extends Fragment {
    private SQLiteDatabase db = null;
    private SimpleCursorAdapter adt = null;
    private ArrayList<String> mArrayList = new ArrayList<String>();

    public VerComprasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ver_compras, container, false);

        if (rootView != null){
            Context c = getContext().getApplicationContext();

            String DB_PATH = c.getDatabasePath("smartlistapp.db").getPath();

            db = openOrCreateDatabase(DB_PATH, null);

            Cursor cursor = db.rawQuery("select compra._id as _id, compra.lista as listaC, compra.valor_total as vlTot, strftime( '%d/%m/%Y', data_compra) as data_compra, substr(lista.nome, 1, 20) || '...' as listaName, " +
                    "compra.modo_compra as modo_compra from compra, lista where compra.lista = lista._id", null);
            String[] campos = {"_id","listaC", "vlTot", "data_compra", "listaName", "modo_compra"};
            int[] ids = {R.id.txtIDCompra, R.id.txtIDListaBase, R.id.txtValor, R.id.txtDataCompra, R.id.txtListaBase, R.id.txvModoCompra};

            adt = new SimpleCursorAdapter(getContext(), R.layout.model_ver_compras, cursor, campos, ids, 0);
            ListView ltwCompras = (ListView) rootView.findViewById(R.id.ltwCompras);
            ltwCompras.setAdapter(adt);

            mArrayList.clear();
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                // The Cursor is now set to the right position
                mArrayList.add(cursor.getString(cursor.getColumnIndex("_id")));
            }

            //Registrando para o menu de contexto
            registerForContextMenu(ltwCompras);

            ltwCompras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    /*Cursor updateCursor = (Cursor) adt.getItem(i);
                    Intent it = new Intent(getBaseContext(), CompraSequencialActivity.class);

                    it.putExtra("idCompra", updateCursor.getString(updateCursor.getColumnIndex("_id")));
                    it.putExtra("idLista", updateCursor.getString(updateCursor.getColumnIndex("listaC")));
                    it.putExtra("idSuperMercado", "1");
                    it.putExtra("modo_compra", updateCursor.getString(updateCursor.getColumnIndex("modo_compra")));

                    startActivity(it);*/
                }
            });

        }

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        if (v.getId() == R.id.ltwCompras) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(getResources().getString(R.string.optionsDesc));
            menu.add(0, 1, 1, getResources().getString(R.string.actionDelete));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String chave = mArrayList.get(info.position);
        Log.v("DEBUG", chave);
        deleteItem(chave);
        return true;
    }

    public void deleteItem(String id) {

        String[] deleteParm = new String[]{id};
        db.delete("compra_item", "compra = ?", deleteParm);
        db.delete("compra", "_id = ?", deleteParm);

        //Log do sistema
        LogSis log = new LogSis();
        String tabela = "lista_item";
        String acao = "Apagou todos os itens da compra " + id;
        log.gravaLog(db, tabela, acao);

        tabela = "lista";
        acao = "Apagou a compra " + id;
        log.gravaLog(db, tabela, acao);

        Cursor cursor = db.rawQuery("select compra._id as _id, compra.lista as listaC, compra.valor_total as vlTot, strftime( '%d/%m/%Y', data_compra) as data_compra, substr(lista.nome, 1, 20) || '...' as listaName, " +
                "compra.modo_compra as modo_compra from compra, lista where compra.lista = lista._id", null);
        adt.changeCursor(cursor);

        mArrayList.clear();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // The Cursor is now set to the right position
            mArrayList.add(cursor.getString(cursor.getColumnIndex("_id")));
        }
        Log.v("DEBUG", id);

    }
}
