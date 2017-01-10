package com.smartdays.smartlist;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Caja on 10/01/2017.
 */

public class DbHandle {
    public void criaDB(SQLiteDatabase db) {
        String supermercado = "CREATE TABLE IF NOT EXISTS supermercado (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome_supermercado CHAR(100) NULL, " +
                "end_supermercado CHAR(200) NULL, " +
                "cidade_supermercado CHAR(50) NULL," +
                "uf_supermercado CHAR(2) NULL)";

        String usuario = "CREATE TABLE IF NOT EXISTS usuario (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  nome CHAR(200) NULL," +
                "  email CHAR(100) NULL," +
                "  idade INT(3) NULL," +
                "  sexo CHAR(1) NULL," +
                "  senha CHAR(20) NULL)";

        String categoria = "CREATE TABLE IF NOT EXISTS categoria ( _id CHAR(3) PRIMARY KEY, " +
                "desc_categ CHAR(100) NULL)";

        String produto = "CREATE TABLE IF NOT EXISTS produto ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "produto_desc CHAR(200) NULL, " +
                "produto_cat CHAR(3) NULL)";

        String lista = "CREATE TABLE IF NOT EXISTS lista ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario INTEGER UNSIGNED NULL, " +
                "nome CHAR(100) NULL, " +
                "lista_intel int(1))";

        String compra = "CREATE TABLE IF NOT EXISTS compra ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "supermercado INTEGER UNSIGNED NULL, " +
                "lista INTEGER UNSIGNED NULL, " +
                "modo_compra CHAR(1) NULL, " +
                "data_compra DATE default (date('now','localtime'))," +
                "valor_total DOUBLE NULL, " +
                "usuario INTEGER UNSIGNED NULL)";


        String compra_item = "CREATE TABLE IF NOT EXISTS compra_item ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "compra INTEGER UNSIGNED NULL, " +
                "produto INTEGER UNSIGNED NULL, " +
                "valor_unit DOUBLE NULL, " +
                "qtde INTEGER UNSIGNED NULL, " +
                "valor_tot DOUBLE NULL)";

        String lista_item = "CREATE TABLE IF NOT EXISTS lista_item ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "qtde INTEGER UNSIGNED NULL, " +
                "ordem INTEGER UNSIGNED NULL, " +
                "produto INTEGER UNSIGNED NULL, " +
                "lista INTEGER UNSIGNED NULL)";

        String log_sis = "CREATE TABLE IF NOT EXISTS log ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tabela CHAR(20) NULL," +
                "acao CHAR(200) NULL," +
                "data_hora_log datetime default (datetime('now','localtime')))";

        /*String ordem_compra_produto = "CREATE TABLE IF NOT EXISTS ordem_compra ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "produto INTEGER NULL," +
                "lista INTEGER NULL," +
                "supermercado INTEGER NULL," +
                "ordem INTEGER NULL)";*/


        db.execSQL(supermercado);
        db.execSQL(usuario);
        db.execSQL(categoria);
        db.execSQL(produto);
        db.execSQL(lista);
        db.execSQL(compra);
        db.execSQL(compra_item);
        db.execSQL(lista_item);
        db.execSQL(log_sis);
        Log.v("Create", "Worked");
        //db.execSQL(ordem_compra_produto);
    }

}
