package com.smartdays.smartlist;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaItemFragment extends Fragment {
    private SQLiteDatabase db = null;
    private SimpleCursorAdapter adt = null;

    public ListaItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lista_item, container, false);

        if (rootView != null) {
            //db = openOrCreateDatabase("smartlist.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);



            //Button btnAddIt = (Button) rootView.findViewById(R.id.btnAddIt);
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

            /*
            final ImageView imgDeleteList = (ImageView) findViewById(R.id.imgDeleteList);
            imgDeleteList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent itDelete = new Intent(getBaseContext(), ApagarListaActivity.class);
                    itDelete.putExtra("listaId", txvIdLista.getText().toString());
                    itDelete.putExtra("nomeLista", txvListaNome.getText().toString());
                    startActivity(itDelete);
                }
            });

            btnAddIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent itCall = new Intent(getBaseContext(), AdicionarProdutosActivity.class);
                    itCall.putExtra("listaId", txvIdLista.getText().toString());
                    itCall.putExtra("nomeLista", txvListaNome.getText().toString());

                    startActivity(itCall);
                }
            });

            ltwItensLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor retornoCursor = (Cursor) adt.getItem(i);
                    Intent it = new Intent(getBaseContext(), ApagarItemListaActivity.class);
                    it.putExtra("listaId", txvIdLista.getText().toString());
                    it.putExtra("produto_id", retornoCursor.getString(retornoCursor.getColumnIndex("_id")));
                    it.putExtra("produto_nome", retornoCursor.getString(retornoCursor.getColumnIndex("ProdDesc")));

                    startActivity(it);
                }
            });*/
        }

        return rootView;
    }

}
