package com.smartdays.smartlist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class NovaListaFragment extends Fragment {
    EditText txtListName;
    SQLiteDatabase db = null;

    public static final String TAG = "NovaListaFragment";
    public NovaListaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_nova_lista, container, false);

        if (rootView != null) {
            Button btnSaveList = (Button) rootView.findViewById(R.id.btnSaveList);
            btnSaveList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txtListName = (EditText) rootView.findViewById(R.id.txtListName);
                    Context c = getContext().getApplicationContext();

                    String DB_PATH = c.getDatabasePath("smartlistapp.db").getPath();

                    Log.v("FullContext", DB_PATH);
                    db = openOrCreateDatabase(DB_PATH, null);
                    ContentValues ctv = new ContentValues();
                    ctv.put("nome", txtListName.getText().toString());

                    if (db.insert("lista", "_id; usuario; lista_intel", ctv) > 0){
                        Toast.makeText(getContext(), getResources().getString(R.string.notifyListSaved), Toast.LENGTH_SHORT).show();

                        Cursor retornoCursor = db.rawQuery("SELECT seq FROM sqlite_sequence WHERE name=?", new String[] {"lista"});
                        //int last = (retornoCursor.moveToFirst() ? retornoCursor.getInt(0) : 0);

                        retornoCursor.moveToFirst();

                        //Log do sistema
                        LogSis log = new LogSis();
                        String tabela = "lista";
                        String acao = "Inseriu o registro no. " + retornoCursor.getString(retornoCursor.getColumnIndex("seq"));
                        log.gravaLog(db, tabela, acao);
                        Log.v("INsert", acao);

                        /*
                        Intent it = new Intent(getBaseContext(), ListaItemActivity.class);
                        it.putExtra("idLista", retornoCursor.getString(retornoCursor.getColumnIndex("seq")));
                        it.putExtra("nomeLista", txtListName.getText().toString());

                        startActivity(it);
                        */
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.notifyErrorListSaved), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return rootView;
    }

    public void criaLista() {

    }


}
