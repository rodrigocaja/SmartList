package com.smartdays.smartlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by Caja on 10/01/2017.
 */

public class CargaSQLite {
    public void carga(Context context) {
        SQLiteDatabase db = null;

        Context c = context.getApplicationContext();

        String DB_PATH = c.getDatabasePath("smartlistapp.db").getPath();

        Log.v("FullContext", DB_PATH);
        db = openOrCreateDatabase(DB_PATH, null);
        //limpando dados
        db.delete("categoria", null, null);
        db.delete("produto", null, null);

        // Criando categorias
        ContentValues ctv = new ContentValues();
        ctv.put("_id", "BOL");
        ctv.put("desc_categ", "Bolacha");

        db.insert("categoria", null, ctv);
        ctv.clear();

        ctv.put("_id", "BEB");
        ctv.put("desc_categ", "Bebidas");

        db.insert("categoria", null, ctv);
        ctv.clear();

        ctv.put("_id", "MAN");
        ctv.put("desc_categ", "Mantimentos");

        db.insert("categoria", null, ctv);
        ctv.clear();

        ctv.put("_id", "HIG");
        ctv.put("desc_categ", "Higiene Pessoal");

        db.insert("categoria", null, ctv);
        ctv.clear();

        ctv.put("_id", "ALC");
        ctv.put("desc_categ", "Bebidas Alcólicas");

        db.insert("categoria", null, ctv);
        ctv.clear();

        ctv.put("_id", "OUT");
        ctv.put("desc_categ", "Outros");

        db.insert("categoria", null, ctv);
        ctv.clear();

        ctv.put("_id", "FRU");
        ctv.put("desc_categ", "Frutas");

        db.insert("categoria", null, ctv);
        ctv.clear();

        ctv.put("_id", "CON");
        ctv.put("desc_categ", "Congelados");

        db.insert("categoria", null, ctv);
        ctv.clear();


        //Inserindo produtos
        ctv.put("produto_desc", "Bolacha Trakinas");
        ctv.put("produto_cat", "BOL");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Coca Cola");
        ctv.put("produto_cat", "BEB");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Macarrão");
        ctv.put("produto_cat", "MAN");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Sabão em pó Omo");
        ctv.put("produto_cat", "HIG");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Papel Higiênico");
        ctv.put("produto_cat", "HIG");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Arroz Tio João");
        ctv.put("produto_cat", "MAN");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Feijão da Roça");
        ctv.put("produto_cat", "MAN");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Abacaxi pç");
        ctv.put("produto_cat", "FRU");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Vinho Shile John");
        ctv.put("produto_cat", "ALC");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Água de coco");
        ctv.put("produto_cat", "BEB");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Sorvete Kibom");
        ctv.put("produto_cat", "MAN");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Shampoo Elseve");
        ctv.put("produto_cat", "HIG");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Gatorade Maracujá");
        ctv.put("produto_cat", "BEB");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Molho de tomate Quero");
        ctv.put("produto_cat", "MAN");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Leite Integral Jussara");
        ctv.put("produto_cat", "MAN");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Torrada Integral Vick Bold");
        ctv.put("produto_cat", "BOL");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Escova de dentes Johnson");
        ctv.put("produto_cat", "HIG");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Margarina Delícia");
        ctv.put("produto_cat", "MAN");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Mini chicken Sadia");
        ctv.put("produto_cat", "CON");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Skol Beats");
        ctv.put("produto_cat", "ALC");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Pipoca Yoki sabor Manteiga");
        ctv.put("produto_cat", "MAN");

        db.insert("produto", "_id", ctv);
        ctv.clear();

        ctv.put("produto_desc", "Morango cartela");
        ctv.put("produto_cat", "FRU");

        db.insert("produto", "_id", ctv);
        ctv.clear();
    }
}
