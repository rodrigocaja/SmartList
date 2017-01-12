package com.smartdays.smartlist;


import android.content.ContentValues;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectionarListaFragment extends Fragment {
    private SQLiteDatabase db = null;
    private SimpleCursorAdapter adt = null;

    public SelectionarListaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_selectionar_lista, container, false);

        if (rootView != null) {
            final String tipoCompra = getArguments().getString("tipoCompra");
            Context c = getContext().getApplicationContext();

            String DB_PATH = c.getDatabasePath("smartlistapp.db").getPath();
            db = openOrCreateDatabase(DB_PATH, null);

            Cursor cursor = null;
            String[] campos = null;

            if (tipoCompra == "A") {
                TextView txvSelectList = (TextView) rootView.findViewById(R.id.txvSelectList);
                txvSelectList.setText(R.string.txvSelectIntList);
                cursor = db.rawQuery("select lista._id as _id, lista.nome as listanome, " +
                        "(select count(distinct lista_item.produto) from lista_item where lista._id = lista_item.lista) as qtdeitens " +
                        "from lista where lista.lista_intel = '1';", null);
                campos = new String[]{"_id", "listanome", "qtdeitens"};
            } else {

                cursor = db.rawQuery("select lista._id as _id, lista.nome as listanome, (select count(distinct lista_item.produto) " +
                        "from lista_item where lista._id = lista_item.lista) as qtdeitens from lista;", null);
                campos = new String[]{"_id", "listanome", "qtdeitens"};
            }
            int[] ids = {R.id.txvListaID, R.id.txtNomeLista, R.id.txtQtde};

            adt = new SimpleCursorAdapter(getContext(), R.layout.model_ver_lista, cursor, campos, ids, 0);
            ListView ltwSelectList = (ListView) rootView.findViewById(R.id.ltwSelectList);

            ltwSelectList.setAdapter(adt);

            //Seleção de registro no listview
            ltwSelectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor retornoCursor = (Cursor) adt.getItem(i);
                    //Intent it = null;
                    //it = new Intent(getBaseContext(), CompraSequencialActivity.class);
                    Bundle args = new Bundle();

                    ContentValues ctv = new ContentValues();
                    ctv.put("supermercado", "1");
                    ctv.put("lista", retornoCursor.getString(retornoCursor.getColumnIndex("_id")));
                    ctv.put("modo_compra", tipoCompra);

                    if (db.insert("compra","_id; data_compra; valor_total; usuario",ctv) > 0) {

                        Cursor seqCursor = db.rawQuery("SELECT seq FROM sqlite_sequence WHERE name=?", new String[]{"compra"});

                        seqCursor.moveToFirst();

                        args.putString("idCompra", seqCursor.getString(seqCursor.getColumnIndex("seq")));
                        args.putString("idLista", retornoCursor.getString(retornoCursor.getColumnIndex("_id")));
                        args.putString("idSuperMercado", "1");
                        args.putString("modo_compra", tipoCompra);

                        if (tipoCompra == "B"){
                            ContentValues ctvInt = new ContentValues();
                            ctvInt.put("lista_intel", "1");
                            db.update("lista",ctvInt, "_id = ?", new String[]{retornoCursor.getString(retornoCursor.getColumnIndex("_id"))});

                            ContentValues ctvIntClear = new ContentValues();
                            ctvIntClear.put("ordem", "0");
                            String[] buscaInt = new String[]{retornoCursor.getString(retornoCursor.getColumnIndex("_id"))};
                            db.update("lista_item",  ctvIntClear, "lista = ?", buscaInt);

                            //Log do sistema
                            LogSis logInt = new LogSis();
                            String tabelaInt = "lista";
                            String acaoInt = "Limpou as informações de ordem da lista no. " + retornoCursor.getString(retornoCursor.getColumnIndex("_id"));
                            logInt.gravaLog(db, tabelaInt, acaoInt);
                        }

                        //Log do sistema
                        LogSis log = new LogSis();
                        String tabela = "compra";
                        String acao = "Inseriu o registro no. " + seqCursor.getString(seqCursor.getColumnIndex("seq"));
                        log.gravaLog(db, tabela, acao);

                        //MainActivity activity = (MainActivity) getActivity();
                        //activity.
                    } else {
                        Toast.makeText(getContext(), R.string.notifyErrorShopp, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return rootView;
    }

}
