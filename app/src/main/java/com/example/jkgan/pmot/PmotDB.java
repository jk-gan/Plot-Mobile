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

    public PmotDB(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        System.out.println("testing1");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+tblUser+" (" +
                "  id integer primary key," +
                "  name varchar(255)," +
                "  email varchar(255)," +
                "  token varchar(255)" +
                "  );");
        System.out.println("testing2");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+tblShop+" (" +
                "  id integer primary key," +
                "  name varchar(255)," +
                "  address text," +
                "  created_at datetime," +
                "  phone varchar(255)," +
                "  description text" +
                "  );");
        System.out.println("testing3");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+tblPromotion+" (" +
                "  id integer primary key," +
                "  name text," +
                "  description text," +
                "  term_and_condition text," +
                "  starts_at datetimeL," +
                "  expires_at datetime," +
                "  shop_id integer," +
                "  created_at datetime" +
                "  );");
        System.out.println("testing4");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXITS "+tblUser+";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXITS "+tblShop+";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXITS "+tblPromotion+";");
    }

    public void fnRunSQL(String sql, Context context){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS expenses;");
    }
}
