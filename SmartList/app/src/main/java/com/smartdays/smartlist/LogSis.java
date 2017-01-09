package com.smartdays.smartlist;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Caja on 09/01/2017.
 */

public class LogSis {
    public int gravaLog (SQLiteDatabase db, String tabela, String acao) {


        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = new Date();

        ContentValues ctv = new ContentValues();
        ctv.put("acao", acao);
        ctv.put("tabela", tabela);

        if (db.insert("log","_id; data_hora_log", ctv) > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
