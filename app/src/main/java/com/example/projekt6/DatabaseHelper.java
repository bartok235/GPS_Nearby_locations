package com.example.projekt6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db.sqlite";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String locationSQL = "" +
                "create table location ( " +
                "   id integer primary key autoincrement," +
                "   name varchar(128)," +
                "   description text," +
                "   latitude double," +
                "   longitude double" +
                ")";
        db.execSQL(locationSQL);

        insertLocation(db, new DBLocation(0,
                "Palac Kultury w Warszawie",
                "Palac Kultury i Nauki PKN - drugi pod względem wysokości całkowitej budynek w Polsce. Znajduje się w śródmieściu Warszawy, przy placu Defilad 1. Właścicielem gmachu jest miasto stołeczne Warszawa, zarządza nim miejska spółka Zarząd Pałacu Kultury i Nauki Sp. z o.o.",
                52.2480, 21.0153));
        insertLocation(db, new DBLocation(0,
                "Zamek Królewski w Warszawie",
                "Zamek Królewski w Warszawie – barokowo-klasycystyczny zamek królewski znajdujący się w Warszawie przy placu Zamkowym 4. Pełni funkcje muzealne i reprezentacyjne. Pierwotnie rezydencja książąt mazowieckich, a od XVI wieku siedziba władz I Rzeczypospolitej: króla i Sejmu (Izby Poselskiej i Senatu).",
                52.2480, 21.0153));
        insertLocation(db, new DBLocation(0,
                "Zlote tarasy w Warszawie",
                "Złote Tarasy – kompleks handlowo-biurowo-rozrywkowy znajdujący się w Śródmieściu Warszawy przy ulicy Złotej. Część handlowa obiektu została otwarta 7 lutego 2007. ",
                52.2301,  21.0024));
        insertLocation(db, new DBLocation(0,
                "Muzeum Narodowe w Warszawie",
                "Muzeum Narodowe w Warszawie (MNW) – muzeum sztuki w Warszawie, założone w 1862 jako Muzeum Sztuk Pięknych w Warszawie, narodowa instytucja kultury; jedno z największych muzeów w Polsce i największe w Warszawie.",
                52.2316, 21.0248));
        insertLocation(db, new DBLocation(0,
                "Lotnisko Chopina w Warszawie",
                "Lotnisko Chopina w Warszawie (ang. Warsaw Chopin Airport, kod IATA: WAW, kod ICAO: EPWA) – międzynarodowy port lotniczy znajdujący się w Warszawie. Został otwarty w 1934. Obsługuje loty rozkładowe, czarterowe i cargo.",
                52.1672, 20.9679));
        insertLocation(db, new DBLocation(0,
                "Muzeum Pałacu Króla Jana III w Wilanowie",
                "Muzeum Pałacu Króla Jana III w Wilanowie (do 2013 Muzeum Pałac w Wilanowie) – muzeum w Warszawie utworzone w 1995 w wyniku wydzielenia z Muzeum Narodowego w Warszawie na mocy zarządzenia Ministra Kultury i Sztuki Oddziału w Wilanowie i nadania mu osobowości prawnej. Siedzibą muzeum jest barokowy pałac w Wilanowie, rezydencja króla Polski Jana III Sobieskiego.",
                52.1652, 21.0905));
        insertLocation(db, new DBLocation(0,
                "Sniardwy",
                "Jezioro Sniardwy – Śniardwy – największe jezioro w Polsce, w województwie warmińsko-mazurskim, w powiatach: mrągowskim i piskim, położone w Krainie Wielkich Jezior Mazurskich, w dorzeczu Pisy. Jest to jezioro polodowcowe. Lustro wody jest na wysokości 116 m n.p.m.",
                53.7500,  21.7166));
        insertLocation(db, new DBLocation(0,
                "Radom",
                "Radom – miasto na prawach powiatu w centralno-wschodniej Polsce, położone nad rzeką Mleczną. Pomimo administracyjnej przynależności do województwa mazowieckiego pod względem historycznym, kulturowym i etnograficznym Radom wraz ze swoim regionem stanowią integralną część Małopolski.",
                51.3801, 21.1602));
        insertLocation(db, new DBLocation(0,
                "Ustka",
                "Ustka – miasto w północnej Polsce, w województwie pomorskim, w powiecie słupskim, siedziba gminy wiejskiej Ustka, na historycznym Pomorzu Zachodnim. Ustka jest położona na Wybrzeżu Słowińskim, u ujścia rzeki Słupi do Morza Bałtyckiego. Jest miastem portowym, uzdrowiskiem z dwoma letnimi kąpieliskami morskimi",
                54.5817, 16.8711));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists location;");
        onCreate(db);
    }

    // location

    public boolean insertLocation(DBLocation location) {
        SQLiteDatabase db = getWritableDatabase();
        return insertLocation(location);
    }

    public boolean insertLocation(SQLiteDatabase db, DBLocation location) {
        ContentValues cv = new ContentValues();
        cv.put("name", location.getName());
        cv.put("description", location.getDescription());
        cv.put("latitude", location.getLatitude());
        cv.put("longitude", location.getLongitude());
        long result = db.insert("location", null, cv);
        return result != -1;
    }

    public List<DBLocation> getAllLocations() {
        Cursor cur = null;
        SQLiteDatabase db = getReadableDatabase();

        try {
            cur = db.rawQuery("select id, name, description, latitude, longitude from location", null);

            List<DBLocation> locations = new ArrayList<>();

            if (cur.moveToFirst()) {
                while (!cur.isAfterLast()) {
                    locations.add(new DBLocation(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getDouble(3), cur.getDouble(4)));
                    cur.moveToNext();
                }
            }
            return locations;
        } finally {
            Objects.requireNonNull(cur).close();
        }
    }

    public DBLocation getLocation(String name) {
        Cursor cur = null;
        SQLiteDatabase db = getReadableDatabase();

        try {
            cur = db.rawQuery("select id, name, description, latitude, longitude from location where name like '" + name + "'", null);

            if (cur.getCount() > 0) {
                cur.moveToFirst();
                return new DBLocation(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getDouble(3), cur.getDouble(4));
            }
            return null;
        } finally {
            Objects.requireNonNull(cur).close();
        }
    }

    public Integer deleteLocation(String name) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete("location", "name = ?", new String[]{name});
    }
}
