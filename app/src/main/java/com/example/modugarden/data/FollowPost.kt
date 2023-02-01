package com.example.modugarden.data

import android.net.Uri
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class FollowPost(
    @PrimaryKey
    var boardId: Int,
    val user : User, // 글 작성자
    val title: String,
    val category: List<String>,
    val time : String = "",
    val image: List<Uri>,
    val location : List<String>?,
    val description: List<String>,
    var liKeNum : Int, // 좋아요 개수
    val comments: List<Comment>?
):Parcelable{

}

val dana = User(
    image = "https://ifh.cc/g/jDDHBg.png".toUri(),
    name = "dana",
    category = listOf("식물 키우기","플랜테리어"),
    follower = 1,
    following = 1,
    state = false,
    post = listOf(),
    curation = listOf()
)
@RequiresApi(Build.VERSION_CODES.O)
val followPosts = listOf(
    FollowPost(
        boardId =0,
        user = dana,
        title = "안녕하세요!",
        category = listOf("식물 가꾸기"),
        time= "",
        image = listOf(
            "https://ifh.cc/g/HHLBxb.jpg".toUri(),
            "https://cdn.shopify.com/s/files/1/0275/2102/4023/files/IMG_3001_3.jpg?v=1661443717&width=1100.jpg".toUri(),
            "https://images.squarespace-cdn.com/content/v1/5b0ebdd5697a98669b443b28/1536616948826-V9DJJJIRDP73NAQQ87NA/PBH%2BPreview-6.jpg?format=1500w.jpg".toUri()
            ),
        location = listOf("장소1","장소2","장소3"),
        description = listOf("첫 번째","두 번째","세 번째"),
        liKeNum = 0,
        comments = null
),
    FollowPost(
        boardId = 1,
        user = dana,
        title = "두번째 게시물입니다.",
        category = listOf("식물 가꾸기"),
        time= "",
        image = listOf(
            "https://ifh.cc/g/HHLBxb.jpg".toUri(),
            "https://cdn.shopify.com/s/files/1/0275/2102/4023/files/IMG_3001_3.jpg?v=1661443717&width=1100.jpg".toUri(),
            "https://images.squarespace-cdn.com/content/v1/5b0ebdd5697a98669b443b28/1536616948826-V9DJJJIRDP73NAQQ87NA/PBH%2BPreview-6.jpg?format=1500w.jpg".toUri()
        ),
        location = listOf("장소1","장소2","장소3"),
        description = listOf("첫 번째","두 번째","세 번째"),
        liKeNum = 0,
        comments = null
    )
)
@RequiresApi(Build.VERSION_CODES.O)
val followCurations = listOf(
    FollowCuration(
        user = dana,
        title = "안녕하세요!",
        time = "",
        category = listOf("플랜테리어"),
        previewImage = "https://ifh.cc/g/cLgQS1.jpg".toUri(),
        link = "https://www.figma.com/file/qJWUWYtT61VA1cV7lnACwv/GUI?node-id=0%3A1&t=bFbrORNQ4xWyzAPK-0",
        liKeNum = 0)
)