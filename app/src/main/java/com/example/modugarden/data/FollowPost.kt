package com.example.modugarden.data

import android.net.Uri
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.net.toUri
import androidx.room.PrimaryKey
import com.example.modugarden.main.content.updateTime
import kotlinx.parcelize.IgnoredOnParcel
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
    val description: List<String>,
    val likesCount : Int, // 좋아요 개수
    val likesList:  MutableList<User>?, // 좋아요 누른 유저들
    val comments: List<Comment>?
):Parcelable{
    @PrimaryKey(autoGenerate = true)
    var boardId: Int =0
}

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
@RequiresApi(Build.VERSION_CODES.O)
val followPosts = listOf(
    FollowPost(
        writer = dana,
        title = "안녕하세요!",
        category = listOf("식물 가꾸기"),
        time= updateTime(LocalDateTime.now()),
        image = listOf(
            "https://ifh.cc/g/HHLBxb.jpg".toUri(),
            "https://cdn.shopify.com/s/files/1/0275/2102/4023/files/IMG_3001_3.jpg?v=1661443717&width=1100.jpg".toUri(),
            "https://images.squarespace-cdn.com/content/v1/5b0ebdd5697a98669b443b28/1536616948826-V9DJJJIRDP73NAQQ87NA/PBH%2BPreview-6.jpg?format=1500w.jpg".toUri()
            ),
        location = null,
        description = listOf("첫 번째","두 번째","세 번째"),
        likesCount = 0,
        likesList = null,
        comments = null
),
    FollowPost(
        writer = dana,
        title = "두번째 게시물입니다.",
        category = listOf("식물 가꾸기"),
        time= updateTime(LocalDateTime.now()),
        image = listOf(
            "https://ifh.cc/g/HHLBxb.jpg".toUri(),
            "https://cdn.shopify.com/s/files/1/0275/2102/4023/files/IMG_3001_3.jpg?v=1661443717&width=1100.jpg".toUri(),
            "https://images.squarespace-cdn.com/content/v1/5b0ebdd5697a98669b443b28/1536616948826-V9DJJJIRDP73NAQQ87NA/PBH%2BPreview-6.jpg?format=1500w.jpg".toUri()
        ),
        location = null,
        description = listOf("첫 번째","두 번째","세 번째"),
        likesCount = 0,
        likesList = null,
        comments = null
    )
)
@RequiresApi(Build.VERSION_CODES.O)
val followCurations = listOf(
    FollowCuration(
        writer = dana,
        title = "안녕하세요!",
        time = updateTime(LocalDateTime.now()),
        category = listOf("플랜테리어"),
        thumbnail_image = "https://ifh.cc/g/cLgQS1.jpg".toUri(),
        url = "https://www.figma.com/file/qJWUWYtT61VA1cV7lnACwv/GUI?node-id=0%3A1&t=bFbrORNQ4xWyzAPK-0"
    )
)