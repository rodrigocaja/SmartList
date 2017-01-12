package com.smartdays.smartlist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdicionarProdutosFragment extends Fragment {
    private SQLiteDatabase db = null;
    private SimpleCursorAdapter adt = null;

    public AdicionarProdutosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_adicionar_produtos, container, false);

        if (rootView != null) {
            Context c = getContext().getApplicationContext();

            String DB_PATH = c.getDatabasePath("smartlistapp.db").getPath();

            db = openOrCreateDatabase(DB_PATH, null);

            Cursor cursor = db.rawQuery("Select produto._id as _id, produto.produto_desc as produtodesc, categoria.desc_categ as categoriadesc " +
                    "from produto, categoria " +
                    "where produto.produto_cat = categoria._id " +
                    "order by produto._id limit 0", null);
            String[] campos = {"_id", "produtodesc", "categoriadesc"};
            int[] ids = {R.id.txvIDProd, R.id.txtProd, R.id.txtCat};

            adt = new SimpleCursorAdapter(getContext(), R.layout.model_list_addproduct, cursor, campos, ids, 0);
            ListView ltwProdutos = (ListView) rootView.findViewById(R.id.ltwProdutos);
            ltwProdutos.setAdapter(adt);


            final EditText txtProdName = (EditText) rootView.findViewById(R.id.txtProdName);
            final EditText txtCatName = (EditText) rootView.findViewById(R.id.txtCatName);


            txtProdName.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    int specialLimit;

                    String prodName;
                    String catName;

                    prodName = txtProdName.getText().toString();
                    catName = txtCatName.getText().toString();
                    if(prodName.isEmpty() && catName.isEmpty()){
                        specialLimit = 0;
                    } else {
                        specialLimit = 999999;
                    }

                    String[] busca = new String[]{"%" + txtProdName.getText().toString() + "%", "%" + txtCatName.getText().toString() + "%"};

                    Cursor cursor = db.rawQuery(String.format("Select produto._id as _id, produto.produto_desc as produtodesc, categoria.desc_categ as categoriadesc " +
                            "from produto, categoria " +
                            "where produto.produto_desc LIKE ? " +
                            "AND categoria.desc_categ LIKE ? AND produto.produto_cat = categoria._id " +
                            "order by produto.produto_desc limit %d", specialLimit), busca);

                    adt.changeCursor(cursor);
                    return false;
                }
            });

            txtCatName.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    int specialLimit;

                    String prodName;
                    String catName;

                    prodName = txtProdName.getText().toString();
                    catName = txtCatName.getText().toString();
                    if(prodName.isEmpty() && catName.isEmpty()){
                        specialLimit = 0;
                    } else {
                        specialLimit = 999999;
                    }

                    String[] busca = new String[]{"%"+txtProdName.getText().toString()+"%", "%"+txtCatName.getText().toString()+"%"};

                    Cursor cursor = db.rawQuery(String.format("Select produto._id as _id, produto.produto_desc as produtodesc, categoria.desc_categ as categoriadesc " +
                            "from produto, categoria " +
                            "where produto.produto_desc LIKE ? " +
                            "AND categoria.desc_categ LIKE ? AND produto.produto_cat = categoria._id " +
                            "order by produto._id limit %d", specialLimit), busca);

                    adt.changeCursor(cursor);
                    return false;
                }
            });

            //Seleção de registro
            ltwProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Context c = getContext().getApplicationContext();

                    String DB_PATH = c.getDatabasePath("smartlistapp.db").getPath();

                    Log.v("FullContext", DB_PATH);
                    db = openOrCreateDatabase(DB_PATH, null);
                    Cursor retornoCursor = (Cursor) adt.getItem(i);

                    //Intent itGet = getIntent();
                    //String idItemLista_Lista = itGet.getStringExtra("listaId");

                    ContentValues ctv = new ContentValues();
                    ctv.put("produto", retornoCursor.getString(retornoCursor.getColumnIndex("_id")));
                    ctv.put("lista", getArguments().getString("listaId"));
                    ctv.put("ordem", "0");

                    if (db.insert("lista_item", "_id; qtde", ctv) > 0) {
                        String msgInsert = retornoCursor.getString(retornoCursor.getColumnIndex("produtodesc"));
                        msgInsert += getResources().getString(R.string.notifyAddedProd);
                        Toast.makeText(getContext(), msgInsert, Toast.LENGTH_SHORT).show();

                        //Log do sistema
                        LogSis log = new LogSis();
                        String tabela = "lista_item";
                        String acao = "Inseriu o produto " + retornoCursor.getString(retornoCursor.getColumnIndex("produtodesc")) + " na lista " + getArguments().getString("listaId");
                        log.gravaLog(db, tabela, acao);

                        db.rawQuery("delete from lista_item where _id not in (select max(_id) from lista_item group by produto, lista)", new String[]{}).moveToFirst();
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.notifyErrorAddProd), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            cursor.close();
        }

        return rootView;
    }

}
