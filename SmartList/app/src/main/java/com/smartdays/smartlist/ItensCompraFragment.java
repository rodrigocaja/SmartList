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
import android.widget.TextView;

import java.util.ArrayList;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItensCompraFragment extends Fragment {
    private SQLiteDatabase db = null;
    private SimpleCursorAdapter adt = null;
    private ArrayList<String> mArrayList = new ArrayList<String>();

    public ItensCompraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_itens_compra, container, false);

        if (rootView != null) {
            Context c = getContext().getApplicationContext();

            String DB_PATH = c.getDatabasePath("smartlistapp.db").getPath();

            db = openOrCreateDatabase(DB_PATH, null);

            String[] busca = new String[]{getArguments().getString("compraID")};
            final Cursor cursor = db.rawQuery("select compra_item.produto as _id, produto.produto_desc as ProdDesc, '' as categ from compra_item inner join produto on compra_item.produto = produto._id" +
                    " where compra_item.compra = ? group by compra_item.produto order by compra_item._id",busca);
            String[] campos = {"_id","ProdDesc", "categ"};
            int[] ids = {R.id.txvIDProd, R.id.txtProd, R.id.txtCat};


            Cursor totalCursor = db.rawQuery("select sum(valor_tot) as soma from compra_item where compra = ?", busca);
            TextView txtTotal = (TextView) rootView.findViewById(R.id.txtTotal);

            totalCursor.moveToFirst();
            double total_vl = totalCursor.getDouble(totalCursor.getColumnIndex("soma"));
            String resultado = String.format("%.2f", total_vl);
            txtTotal.setText(resultado);

            adt = new SimpleCursorAdapter(getContext(), R.layout.model_list_addproduct, cursor, campos, ids, 0);
            ListView ltwItensCompra = (ListView) rootView.findViewById(R.id.ltwItensCompra);
            ltwItensCompra.setAdapter(adt);

            registerForContextMenu(ltwItensCompra);

            mArrayList.clear();
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                // The Cursor is now set to the right position
                mArrayList.add(cursor.getString(cursor.getColumnIndex("_id")));
            }

            //Seleção de registro no listview
            ltwItensCompra.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor retornoCursor = (Cursor) adt.getItem(i);

                    Bundle args = new Bundle();

                    //Intent it = new Intent(getBaseContext(), ApagarItemCompraActivity.class);
                    args.putString("compraID", getArguments().getString("compraID"));
                    args.putString("idProduto", cursor.getString(cursor.getColumnIndex("_id")));
                    args.putString("nomeProduto", cursor.getString(cursor.getColumnIndex("ProdDesc")));
                    args.putString("modo_compra", getArguments().getString("modo_compra"));
                    args.putString("listaID", getArguments().getString("listaID"));

                }
            });
        }

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        if (v.getId() == R.id.ltwItensCompra) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(getResources().getString(R.string.optionsDesc));
            menu.add(0, 1, 1, getResources().getString(R.string.contextRemoveCart));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //int menuItemIndex = item.getItemId();
        String chave = mArrayList.get(info.position);

        deleteItem(chave);
        return true;
    }

    public void deleteItem(String id) {
        String[] deleteParm = new String[]{id, getArguments().getString("compraID")};
        db.delete("compra_item", "produto = ? AND compra = ?", deleteParm);



        String[] busca = new String[]{getArguments().getString("compraID")};
        final Cursor cursor = db.rawQuery("select compra_item.produto as _id, produto.produto_desc as ProdDesc, '' as categ from compra_item inner join produto on compra_item.produto = produto._id" +
                " where compra_item.compra = ? group by compra_item.produto order by compra_item._id",busca);
        adt.changeCursor(cursor);

        //Log do sistema
        LogSis log = new LogSis();
        String tabela = "compra_item";
        String acao = "Removeu o produto " + id + " da compra " + getArguments().getString("compraID");
        log.gravaLog(db, tabela, acao);


        mArrayList.clear();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // The Cursor is now set to the right position
            mArrayList.add(cursor.getString(cursor.getColumnIndex("_id")));
        }

        Log.v("DEBUG", id);
    }

}
