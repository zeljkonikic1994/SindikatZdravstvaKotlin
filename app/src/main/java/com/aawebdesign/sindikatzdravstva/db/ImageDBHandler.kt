package com.aawebdesign.sindikatzdravstva.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.aawebdesign.sindikatzdravstva.dto.PostImage

class ImageDBHandler(
    context: Context, name: String?,
    factory: SQLiteDatabase.CursorFactory?, version: Int
) :
    SQLiteOpenHelper(
        context, DATABASE_NAME,
        factory, DATABASE_VERSION
    ) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "sindikat_image.db"
        val TABLE_IMAGES = "images_table"

        val COLUMN_IMAGE_LINK = "image_url"
        val COLUMN_FILE_LOCATION = "file_location"
        val COLUMN_POST_ID = "post_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createImagesQuery = ("CREATE TABLE " +
                TABLE_IMAGES + "("
                + COLUMN_IMAGE_LINK + " TEXT PRIMARY KEY, " +
                COLUMN_FILE_LOCATION + " TEXT, " +
                COLUMN_POST_ID + " INTEGER" +
                ")")
        db.execSQL(createImagesQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase, oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_IMAGES")
        onCreate(db)
    }

    fun getAll():List<PostImage>{
        val query =
            "SELECT * FROM $TABLE_IMAGES"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null) as SQLiteCursor
        val postImageList = arrayListOf<PostImage>()
        with(cursor) {
            while (moveToNext()) {
                postImageList.add(createPostImage(cursor))
            }
        }
        return postImageList
    }

    fun insertImage(postImage: PostImage): Long {
        val db = this.writableDatabase
        var status = db.insert(TABLE_IMAGES, null, values(postImage))
        db.close()
        return status
    }

    fun findImage(imageUrl: String?): PostImage? {
        val query =
            "SELECT * FROM $TABLE_IMAGES WHERE $COLUMN_IMAGE_LINK = \"$imageUrl\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null) as SQLiteCursor
        var postImage: PostImage? = null
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            postImage = createPostImage(cursor)
            cursor.close()
        }

        db.close()
        return postImage
    }

    fun findImagesForPost(postId: String?): List<PostImage>{
        val query = "SELECT * FROM $TABLE_IMAGES WHERE $COLUMN_POST_ID = \"$postId\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null) as SQLiteCursor
        var imageList = arrayListOf<PostImage>()

        with(cursor) {
            while (moveToNext()) {
                imageList.add(createPostImage(cursor))
            }
        }
        return imageList
    }

    fun deleteImage(imageUrl: String?){
        if(imageUrl == null)
            return
        val db = this.writableDatabase
        db.delete(TABLE_IMAGES, "$COLUMN_IMAGE_LINK = ?", arrayOf(imageUrl))
        db.close()
    }

    private fun createPostImage(cursor: SQLiteCursor): PostImage {
        var imageUrl = cursor.getString(0)
        var imageLocation = cursor.getString(1)
        var postId = cursor.getString(2)
        return PostImage(imageUrl, imageLocation, postId)
    }

    private fun values(postImage: PostImage): ContentValues {
        val values = ContentValues()
        values.put(COLUMN_IMAGE_LINK, postImage.url)
        values.put(COLUMN_FILE_LOCATION, postImage.internalLocation)
        values.put(COLUMN_POST_ID, postImage.postId)
        return values
    }
}