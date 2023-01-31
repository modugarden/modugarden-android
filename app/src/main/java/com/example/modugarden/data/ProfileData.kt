package com.example.modugarden.data

import android.net.Uri
import android.os.Parcelable
import com.example.modugarden.R
import com.example.modugarden.api.UserAuthority
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class User(
    val image: Uri,
    val name: String,
    val category: List<String>,
    val follower: Int,
    val following: Int,
    val state: Boolean,
    val post: @RawValue List<PostCard>?,
    val curation: @RawValue List<CurationCard>?
) : Parcelable

@Parcelize
data class NewUser(
    val categories: List<String> = listOf("식물 가꾸기"),
    val follow: Boolean = true,
    val followerCount: Int = 100,
    val id: Int = 15,
    val nickname: String = "mara",
    val postCount: Int = 10,
    val profileImage: String? = null,
    val userAuthority: String = UserAuthority.ROLE_CURATOR.name
) : Parcelable

@Parcelize
data class FollowData(
    val categories: List<String>,
    val follow: Boolean,
    val nickname: String,
    val profileImage: String,
    val userId: Int
) : Parcelable

@Parcelize
data class PostCard(
    val image: Int = R.drawable.ic_plus_big,
    val title: String = "Upload",
    val category: String = "Upload",
    val time: String = "No time",
    val user: String = "Default UserName"
) : Parcelable

@Parcelize
data class CurationCard(
    val thumbnail_image: Int = R.drawable.ic_plus_big,
    val title: String = "새 큐레이션 게시",
    val category: String = "Upload",
    val time: String = "No time",
    val user: String = "No user"
) : Parcelable
