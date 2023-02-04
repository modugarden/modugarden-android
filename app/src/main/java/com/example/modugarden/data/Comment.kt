package com.example.modugarden.data

import android.content.Context
import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Entity(tableName = "comment")
@TypeConverters(Converters::class)
@Parcelize
data class Comment(
    @PrimaryKey
    val commentId: Int?=null,
    @ColumnInfo(name = "boardId")
    var nickname: String? = null, // 댓글 작성자
    val comment: String? = null, //댓글 내용
    val localDateTime: String, //  댓글 작성 및 수정 일시
    @ColumnInfo(name = "parentID")
    val parentID:Int? = null // 답글을 달 댓글의 id
    // /*var userProfile,*//
) : Parcelable{
}

@Dao
interface CommentDao{
    @Query("SELECT * FROM comment")
    fun getAll() : MutableList<Comment>
   @Query("SELECT * FROM comment WHERE boardId = :boardId ")
   fun getComments(boardId:Int) : MutableList<Comment>
    @Insert
    fun insert(comment: Comment)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg comment: Comment)
    @Delete
    fun delete(comment: Comment)
    @Query("DELETE FROM comment")
    fun deleteAll()
    @Update
    fun update(comment: Comment)

}
@Database(entities = [Comment::class], version = 1)
@TypeConverters(Converters::class)
abstract class CommentDataBase : RoomDatabase(){
    abstract fun commentDao() : CommentDao
    companion object{
        private var commentDataBase :CommentDataBase? = null
        @Synchronized
        fun getInstance(context:Context) :CommentDataBase?{
            if (commentDataBase==null){
                // synchronized : 중복 방지
                synchronized(CommentDataBase::class.java){
                    commentDataBase = Room.databaseBuilder(
                        context.applicationContext,
                        CommentDataBase::class.java,
                        "comment-database")
                        .allowMainThreadQueries().build()
                }
            }
            return commentDataBase
        }
    }
}

class Converters{
    @TypeConverter
    fun mutableFrom(state:MutableState<Boolean>) : Boolean{
        return state.value
    }
    @TypeConverter
    fun toMutable(boolean: Boolean): MutableState<Boolean>{
        return mutableStateOf(boolean)
    }

}
