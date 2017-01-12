package com.smartdays.smartlist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class InserirItemCartFragment extends Fragment {
    SQLiteDatabase db = null;

    public InserirItemCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_inserir_item_cart, container, false);

        if (rootView != null) {
            Context c = getContext().getApplicationContext();

            String DB_PATH = c.getDatabasePath("smartlistapp.db").getPath();

            db = openOrCreateDatabase(DB_PATH, null);


            TextView txtIdProdCart = (TextView) rootView.findViewById(R.id.txtIdProdCart);
            TextView txvNomeProdCart = (TextView) rootView.findViewById(R.id.txvNomeProdCart);
            final EditText txtQtde = (EditText) rootView.findViewById(R.id.txtQtde);
            final EditText txtEditVlrUnit = (EditText) rootView.findViewById(R.id.txtEditVlrUnit);
            final TextView txtVlrTotItem = (TextView) rootView.findViewById(R.id.txtVlrTotItem);
            final String tipocompra = getArguments().getString("modo_compra");

            txtIdProdCart.setText(getArguments().getString("idProduto"));
            txvNomeProdCart.setText(getArguments().getString("nomeProduto"));

            txtQtde.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (txtQtde.length() > 0 && txtEditVlrUnit.length() > 0) {
                        double qtde = Double.parseDouble(txtQtde.getText().toString());
                        double vlUnit = Double.parseDouble(txtEditVlrUnit.getText().toString());

                        double totalVlIt = vlUnit * qtde;
                        String totalString = String.format("%.2f", totalVlIt);

                        txtVlrTotItem.setText(totalString);
                    }
                    return false;
                }
            });

            txtEditVlrUnit.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (txtQtde.length() > 0 && txtEditVlrUnit.length() > 0) {
                        double qtde = Double.parseDouble(txtQtde.getText().toString());
                        double vlUnit = Double.parseDouble(txtEditVlrUnit.getText().toString());

                        double totalVlIt = vlUnit * qtde;
                        String totalString = String.format("%.2f", totalVlIt);

                        txtVlrTotItem.setText(totalString);
                    }
                    return false;
                }
            });


            Button btnAddCart = (Button) rootView.findViewById(R.id.btnAddCart);
            btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentValues ctv = new ContentValues();
                    ctv.put("compra", getArguments().getString("compraID"));
                    ctv.put("produto", getArguments().getString("idProduto"));
                    ctv.put("valor_unit", txtEditVlrUnit.getText().toString());
                    ctv.put("qtde",txtQtde.getText().toString());
                    ctv.put("valor_tot",txtVlrTotItem.getText().toString());

                    if (db.insert("compra_item", "_id", ctv) > 0) {


                        if (tipocompra == "B"){
                            String[] busca = new String[]{getArguments().getString("listaID")};
                            Cursor seqCursor = db.rawQuery("select max(ordem) + 1 as ordem_ins from lista_item where lista = ?", busca);
                            seqCursor.moveToFirst();

                            String seqItem = seqCursor.getString(0);

                            ContentValues ctvUpd = new ContentValues();
                            ctvUpd.put("ordem", seqItem);

                            String[] constraints = new String[]{getArguments().getString("listaID"), getArguments().getString("idProduto")};
                            db.update("lista_item", ctvUpd, "lista = ? and produto = ?", constraints);

                            //Log do sistema
                            LogSis logInt = new LogSis();
                            String tabelaInt = "lista_item";
                            String acaoInt = "Alterou a ordem do produto " + getArguments().getString("nomeProduto") + " para " + seqItem + " na lista " + getArguments().getString("listaID");
                            logInt.gravaLog(db, tabelaInt, acaoInt);

                        }

                        //Log do sistema
                        LogSis log = new LogSis();
                        String tabela = "compra_item";
                        String acao = "Inseriu o produto " + getArguments().getString("nomeProduto") + " na compra " + getArguments().getString("compraID") + " || valor unitario: " + txtEditVlrUnit.getText().toString() +
                                " qtde: " + txtQtde.getText().toString();
                        log.gravaLog(db, tabela, acao);


                        String[] busca = new String[]{getArguments().getString("compraID")};
                        Cursor totalCursor = db.rawQuery("select sum(valor_tot) as soma from compra_item where compra = ?", busca);
                        totalCursor.moveToFirst();
                        double total_vl = totalCursor.getDouble(totalCursor.getColumnIndex("soma"));
                        String resultado = String.format("%.2f", total_vl);

                        ContentValues updCtv = new ContentValues();
                        updCtv.put("valor_total", resultado);
                        db.update("compra", updCtv, "_id = ?", new String[]{getArguments().getString("compraID")});


                        Toast.makeText(getContext(), R.string.notifyCartProductAdded, Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getContext(), R.string.notifyErrorAddCartProduct, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return rootView;
    }

}
