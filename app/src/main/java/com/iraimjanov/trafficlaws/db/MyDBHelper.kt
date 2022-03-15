package com.iraimjanov.trafficlaws.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.iraimjanov.trafficlaws.models.RoadSign

class MyDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION),
    DBService {

    companion object {
        const val DB_NAME = "base"
        const val DB_VERSION = 1

        const val TABLE_ROAD_SIGN = "roadSign"
        const val ID = "id"
        const val NAME = "name"
        const val ABOUT = "about"
        const val TYPE = "type"
        const val IMAGE = "image"
        const val LIKE = "likes"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            "create table $TABLE_ROAD_SIGN ($ID integer not null primary key autoincrement unique , $NAME text not null unique , $ABOUT text not null , $TYPE text not null , $IMAGE text not null , $LIKE text not null)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    override fun addRoadSign(roadSign: RoadSign): Boolean {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, roadSign.name)
        contentValues.put(ABOUT, roadSign.about)
        contentValues.put(TYPE, roadSign.type)
        contentValues.put(IMAGE, roadSign.image)
        contentValues.put(LIKE, roadSign.like)
        val result = database.insert(TABLE_ROAD_SIGN, null, contentValues)
        return result.toInt() != -1
    }

    override fun showRoadSign(): ArrayList<RoadSign> {
        val arrayList = ArrayList<RoadSign>()
        val database = this.readableDatabase
        val query = "select * from $TABLE_ROAD_SIGN"
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val roadSign = RoadSign(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
                )
                arrayList.add(roadSign)
            } while (cursor.moveToNext())
        }
        return arrayList
    }

    override fun updateRoadSign(roadSign: RoadSign): Boolean {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, roadSign.name)
        contentValues.put(ABOUT, roadSign.about)
        contentValues.put(TYPE, roadSign.type)
        contentValues.put(IMAGE, roadSign.image)
        contentValues.put(LIKE, roadSign.like)
        val result = database.update(TABLE_ROAD_SIGN,
            contentValues,
            "$ID = ?",
            arrayOf(roadSign.id.toString()))
        return result != -1
    }

    override fun deleteRoadSign(roadSign: RoadSign): Boolean {
        val database = this.writableDatabase
        val result = database.delete(TABLE_ROAD_SIGN, "$ID = ?", arrayOf(roadSign.id.toString()))
        return result != -1
    }
}