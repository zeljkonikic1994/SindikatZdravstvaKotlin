package com.aawebdesign.sindikatzdravstva.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.aawebdesign.sindikatzdravstva.dto.Post

class PostDBHandler(
    context: Context, name: String?,
    factory: SQLiteDatabase.CursorFactory?, version: Int
) :
    SQLiteOpenHelper(
        context, DATABASE_NAME,
        factory, DATABASE_VERSION
    ) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "sindikat_post.db"
        val TABLE_POSTS = "posts"

        val COLUMN_ID = "post_id"
        val COLUMN_POST_TITLE = "post_title"
        val COLUMN_POST_CONTENT = "post_content"
        val COLUMN_POST_IMAGE = "post_image"
        val COLUMN_POST_LAST_MODIFIED = "last_modified"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createPostsQuery = ("CREATE TABLE " +
                TABLE_POSTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_POST_TITLE + " TEXT, " +
                COLUMN_POST_CONTENT + " TEXT, " +
                COLUMN_POST_IMAGE + " TEXT, " +
                COLUMN_POST_LAST_MODIFIED + " TEXT" +
                ")")
        db.execSQL(createPostsQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase, oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_POSTS")
        onCreate(db)
    }

    private fun insertPost(post: Post) : Long {
        val db = this.writableDatabase
        var status = db.insert(TABLE_POSTS, null, values(post))
        db.close()
        return status
    }

    private fun updatePost(post: Post) : Int{
        val db = this.writableDatabase
        var status = db.update(TABLE_POSTS, values(post), "post_id=?", arrayOf(post.id))
        db.close()
        return status
    }

    fun deletePost(postId: String?) : Int {
        val db = this.writableDatabase
        var status = db.delete(TABLE_POSTS, "$COLUMN_ID = ?", arrayOf(postId))
        db.close()
        return status
    }

    fun addPosts(postList: List<Post>) {
        var allPosts = getAll()
        for (post in postList) {
            val existingPost = getPost(allPosts, post.id)
            if (existingPost == null) {
                insertPost(post)
                Log.d("DB", "Saving post: " + post.id)
            } else if (!existingPost.lastModified.equals(post.lastModified)) {
                updatePost(post)
                Log.d("DB", "Updating post: " + post.id)
            } else {
                Log.d("DB", "Ignoring post: " + post.id)
            }
        }

    }

    private fun getPost(postList: List<Post>, id: String?): Post? {
        for (post in postList) {
            if (post.id.equals(id))
                return post
        }
        return null
    }

    fun getAll(): ArrayList<Post> {
        val query =
            "SELECT * FROM $TABLE_POSTS"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null) as SQLiteCursor
        val postList = arrayListOf<Post>()
        with(cursor) {
            while (moveToNext()) {
                postList.add(createPost(cursor))
            }
        }
        for (post in postList) {
            Log.d("DB", "Post read from db: " + post.id)
        }
        return postList
    }



    private fun values(post: Post): ContentValues {
        val values = ContentValues()
        values.put(COLUMN_ID, Integer.valueOf(post.id))
        values.put(COLUMN_POST_TITLE, post.title)
        values.put(COLUMN_POST_CONTENT, post.content)
        values.put(COLUMN_POST_IMAGE, post.imgPath)
        values.put(COLUMN_POST_LAST_MODIFIED, post.lastModified)
        return values
    }

    private fun createPost(cursor: SQLiteCursor): Post {
        val id = cursor.getString(0)
        val title = cursor.getString(1)
        val content = cursor.getString(2)
        val image = cursor.getString(3)
        val lastModified = cursor.getString(4)
        return Post(id, title, content, image, lastModified)
    }

    fun findPost(postId: String?): Post? {
        val query =
            "SELECT * FROM $TABLE_POSTS WHERE $COLUMN_ID = \"$postId\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null) as SQLiteCursor
        var post: Post? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            post = createPost(cursor)
            cursor.close()
        }

        db.close()
        return post
    }
}