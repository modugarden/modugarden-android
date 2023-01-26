package com.example.modugarden.data

import android.net.Uri
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.net.toUri
import com.example.modugarden.main.content.updateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.LocalDateTime

@Parcelize
data class FollowPost(
    val writer : User, // 글 작성자
    val title: String,
    val category: List<String>,
    val time : String = "",
    val image: List<Uri>,
    val location : @RawValue SnapshotStateList<String>?,
    val description: String,
    /*val commentData :  @RawValue CommentDataBase, // 댓글 데이터 배이스,*/
    val likesCount : Int, // 좋아요 개수
    val likesList:  MutableList<User>? // 좋아요 누른 유저들,
):Parcelable

val dana = User(
    image = "https://ifh.cc/g/jDDHBg.png".toUri(),
    name = "dana",
    category = listOf(""),
    follower = 1,
    following = 1,
    state = false,
    post = null,
    curation = null
)
val followPosts = listOf(
    FollowPost(
        writer = dana,
        title = "안녕하세요!",
        category = listOf("식물 가꾸기"),
        time= updateTime(LocalDateTime.now()),
        image = listOf(
            "https://ifh.cc/g/HHLBxb.jpg".toUri(),
            "https://ifh.cc/g/roQrJq.jpg".toUri(),
            "https://ifh.cc/g/cLgQS1.jpg".toUri()),
        location = null,
        description = "첫 게시물 입니다",
        likesCount = 0,
        likesList = null
)
)
@RequiresApi(Build.VERSION_CODES.O)
val followCurations = listOf(
    FollowCuration(
        writer = dana,
        title = "안녕하세요!",
        time = updateTime(LocalDateTime.now()),
        category = listOf("식물 가꾸기"),
        thumbnail_image = "https://ifh.cc/g/roQrJq.jpg".toUri(),
        url = "https://www.figma.com/file/qJWUWYtT61VA1cV7lnACwv/GUI?node-id=0%3A1&t=bFbrORNQ4xWyzAPK-0"
    )
)