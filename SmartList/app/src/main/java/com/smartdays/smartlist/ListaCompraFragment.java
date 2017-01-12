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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaCompraFragment extends Fragment {
    SQLiteDatabase db = null;
    SimpleCursorAdapter adt = null;

    public ListaCompraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lista_compra, container, false);

        if (rootView != null) {
            Context c = getContext().getApplicationContext();

            String DB_PATH = c.getDatabasePath("smartlistapp.db").getPath();

            db = openOrCreateDatabase(DB_PATH, null);

            final String tipocompra = getArguments().getString("modo_compra");

            TextView txvSeqCompra = (TextView) rootView.findViewById(R.id.txvSeqCompra);


            switch(tipocompra) {
                case "A":
                    txvSeqCompra.setText(R.string.txvInfSmartList);
                    break;
                case "B":
                    txvSeqCompra.setText(R.string.txvInfMakeaSmart);
                    break;
                case "C":
                    txvSeqCompra.setText(R.string.txvSeqCompra);
                    break;
            }

            String[] busca = new String[]{getArguments().getString("idLista"), getArguments().getString("idCompra")};
            Cursor cursor = null;
            if (tipocompra == "A"){
                cursor = db.rawQuery("select lista_item.produto as _id, produto.produto_desc as ProdDesc, '' as categ from lista_item " +
                        "inner join produto on lista_item.produto = produto._id where lista_item.lista = ? and lista_item.produto not in (" +
                        "select compra_item.produto from compra_item where compra_item.compra = ?) order by lista_item.ordem", busca);
            }else {
                cursor = db.rawQuery("select lista_item.produto as _id, produto.produto_desc as ProdDesc, '' as categ from lista_item " +
                        "inner join produto on lista_item.produto = produto._id where lista_item.lista = ? and lista_item.produto not in (" +
                        "select compra_item.produto from compra_item where compra_item.compra = ?) order by produto.produto_desc", busca);
            }
            String[] campos = {"_id","ProdDesc", "categ"};
            int[] ids = {R.id.txvIDProd, R.id.txtProd, R.id.txtCat};

            adt = new SimpleCursorAdapter(getContext(), R.layout.model_list_addproduct, cursor, campos, ids, 0);
            ListView ltwItensPCompra = (ListView) rootView.findViewById(R.id.ltwItensPCompra);
            ltwItensPCompra.setAdapter(adt);

            Cursor totalCursor = db.rawQuery("select sum(valor_tot) as soma from compra_item where compra = ?", new String[]{getArguments().getString("idCompra")});
            TextView txtTotalL = (TextView) rootView.findViewById(R.id.txtTotalL);

            totalCursor.moveToFirst();
            double total_vl = totalCursor.getDouble(totalCursor.getColumnIndex("soma"));
            String resultado = String.format("%.2f", total_vl);
            txtTotalL.setText(resultado);

            final ImageView imgCart = (ImageView) rootView.findViewById(R.id.imgCart);
            imgCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString("compraID", getArguments().getString("idCompra"));
                    args.putString("modo_compra", tipocompra);
                    args.putString("listaID", getArguments().getString("idLista"));

                    MainActivity activity = (MainActivity) getActivity();
                    activity.callItensCompraFragment(args);
                }
            });

            //Seleção de registro no listview
            ltwItensPCompra.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor retornoCursor = (Cursor) adt.getItem(i);
                    /*Intent it = new Intent(getBaseContext(), InserirItemCartActivity.class);
                    it.putExtra("compraID", callIT.getStringExtra("idCompra"));
                    it.putExtra("idProduto", retornoCursor.getString(retornoCursor.getColumnIndex("_id")));
                    it.putExtra("nomeProduto", retornoCursor.getString(retornoCursor.getColumnIndex("ProdDesc")));
                    it.putExtra("listaID", callIT.getStringExtra("idLista"));
                    it.putExtra("modo_compra", tipocompra);

                    startActivity(it);*/
                }
            });
        }

        return rootView;
    }

}
