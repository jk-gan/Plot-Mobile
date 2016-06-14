package com.example.jkgan.pmot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jk-gan on 14/06/2016.
 */
public class PmotDB extends SQLiteOpenHelper {

    public static final String dbName = "db_Pmot";
    public static final String tblUser = "users";
    public static final String tblShop = "shops";
    public static final String tblPromotion = "promotions";
    public static final String colExpName = "exp_name";
    public static final String colExpPrice = "exp_price";
    public static final String colExpDate = "exp_date";
    public static final String colExpId = "exp_id";

    public PmotDB(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
