package com.example.modugarden.data

import android.content.Context
import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import java.util.UUID
@Entity(tableName = "comment")
@TypeConverters(Converters::class)
@Parcelize
data class Comment(
    @PrimaryKey
    var id: Int,
    @ColumnInfo(name = "boardId")
    val boardId:Int,
    val userID: String? = null, // 댓글 작성자
    var description: String? = null, //댓글 내용
    var time: Int? = null, //  댓글 작성 및 수정 일시
    var isReplying: @RawValue MutableState<Boolean> = mutableStateOf(false),//답글 작성중인지 // true면 작성중
    @ColumnInfo(name = "parentID")
    val parentID:Int, // 답글을 달 댓글의 id
    val mode: Boolean = false// 댓글인지 답글인지 // true면 답글, false면 댓글
    // /*var userProfile,*//
) : Parcelable{
  //댓글 id,
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
