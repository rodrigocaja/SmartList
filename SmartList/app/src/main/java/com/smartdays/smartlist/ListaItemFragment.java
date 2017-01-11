package com.smartdays.smartlist;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaItemFragment extends Fragment {
    private SQLiteDatabase db = null;
    private SimpleCursorAdapter adt = null;
    private ArrayList<String> mArrayList = new ArrayList<String>();

    public ListaItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lista_item, container, false);

        if (rootView != null) {
            Context c = getContext().getApplicationContext();

            String DB_PATH = c.getDatabasePath("smartlistapp.db").getPath();

            Log.v("FullContext", DB_PATH);
            db = openOrCreateDatabase(DB_PATH, null);


            ImageButton btnAddIt = (ImageButton) rootView.findViewById(R.id.btnAddIt);
            final TextView txvListaNome = (TextView) rootView.findViewById(R.id.txvListaNome);
            final TextView txvIdLista = (TextView) rootView.findViewById(R.id.txvIdLista);

            String argNomeLista = getArguments().getString("nomeLista");
            String argIdLista = getArguments().getString("idLista");

            txvListaNome.setText(argNomeLista);
            txvIdLista.setText(argIdLista);

            String[] busca = new String[]{txvIdLista.getText().toString()};
            Cursor cursor = db.rawQuery("select lista_item.produto as _id, produto.produto_desc as ProdDesc, '' as categ from lista_item inner join produto on lista_item.produto = produto._id" +
                    " where lista_item.lista = ? order by lista_item._id",busca);
            String[] campos = {"_id","ProdDesc", "categ"};
            int[] ids = {R.id.txvIDProd, R.id.txtProd, R.id.txtCat};

            adt = new SimpleCursorAdapter(getContext(), R.layout.model_list_addproduct, cursor, campos, ids, 0);
            ListView ltwItensLista = (ListView) rootView.findViewById(R.id.ltwItensLista);
            ltwItensLista.setAdapter(adt);

            mArrayList.clear();
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                // The Cursor is now set to the right position
                mArrayList.add(cursor.getString(cursor.getColumnIndex("_id")));
            }

            btnAddIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle args = new Bundle();

                    args.putString("listaId", txvIdLista.getText().toString());
                    args.putString("nomeLista", txvListaNome.getText().toString());

                    MainActivity activity = (MainActivity) getActivity();
                    activity.callAdicionarProdutosFragment(args);

                }
            });

            //Registrando para o menu de contexto
            registerForContextMenu(ltwItensLista);
        }

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        if (v.getId() == R.id.ltwItensLista) {
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

    public void deleteItem(String produto) {

        String[] deleteParm = new String[]{produto, getArguments().getString("idLista")};
        db.delete("lista_item", "produto = ? and lista = ?", deleteParm);

        String[] busca = new String[]{getArguments().getString("idLista")};
        Cursor cursor = db.rawQuery("select lista_item.produto as _id, produto.produto_desc as ProdDesc, '' as categ from lista_item inner join produto on lista_item.produto = produto._id" +
                " where lista_item.lista = ? order by lista_item._id",busca);
        adt.changeCursor(cursor);

        mArrayList.clear();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // The Cursor is now set to the right position
            mArrayList.add(cursor.getString(cursor.getColumnIndex("_id")));
        }
        Log.v("DEBUG", produto);
    }
}
