package com.example.modugarden.main.profile

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.net.toUri
import com.example.modugarden.R
import com.example.modugarden.api.*
import com.example.modugarden.data.CurationCard
import com.example.modugarden.data.PostCard
import com.example.modugarden.data.User
import com.example.modugarden.main.profile.follow.ProfileFollowActivity
import com.example.modugarden.main.settings.SettingsActivity
import com.example.modugarden.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
서버에서 프로필에 표시될 유저의 정보를 받아옴
*/
const val userId= 0
const val myId = 1

val pages = listOf("포스트", "큐레이션")

val postResponse = listOf(
    PostCard(
        R.drawable.test_image1, "타이틀1", "카테고리1",
        "2023년 1월 6일", "유저1"
    ),
    PostCard(
        R.drawable.test_image2, "타이틀2", "카테고리2",
        "2023년 1월 6일", "유저2"
    ),
    PostCard(
        R.drawable.test_image3, "타이틀3", "카테고리3",
        "2023년 1월 6일", "유저3"
    ),
    PostCard(
        R.drawable.test_image4, "타이틀4", "카테고리4",
        "2023년 1월 6일", "유저4"
    ),
    PostCard(
        R.drawable.test_image5, "타이틀5", "카테고리5",
        "2023년 1월 6일", "유저5"
    ),
    PostCard(
        R.drawable.test_image1, "타이틀6", "카테고리6",
        "2023년 1월 6일", "유저6"
    ),
    PostCard(
        R.drawable.test_image2, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    ),
    PostCard(
        R.drawable.test_image3, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    ),
    PostCard(
        R.drawable.test_image4, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    ),
    PostCard(
        R.drawable.test_image5, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    ),
    PostCard(
        R.drawable.test_image1, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    ),
    PostCard(
        R.drawable.test_image2, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    ),
    PostCard(
        R.drawable.test_image3, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    )
)

val curationResponse = listOf(
    CurationCard(
        R.drawable.test_image1, "타이틀1", "카테고리1",
        "2023년 1월 6일", "유저1"
    ),
    CurationCard(
        R.drawable.test_image2, "타이틀2", "카테고리2",
        "2023년 1월 6일", "유저2"
    ),
    CurationCard(
        R.drawable.test_image3, "타이틀3", "카테고리3",
        "2023년 1월 6일", "유저3"
    ),
    CurationCard(
        R.drawable.test_image4, "타이틀4", "카테고리4",
        "2023년 1월 6일", "유저4"
    ),
    CurationCard(
        R.drawable.test_image5, "타이틀5", "카테고리5",
        "2023년 1월 6일", "유저5"
    ),
    CurationCard(
        R.drawable.test_image1, "타이틀6", "카테고리6",
        "2023년 1월 6일", "유저6"
    ),
    CurationCard(
        R.drawable.test_image2, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    )
)

val categoryResponse = listOf("식물 키우기", "식물 부수기", "식물 심기")

val user = User(
    "https://blog.kakaocdn.net/dn/dTQvL4/btrusOKyP2u/TZBNHQSAHpJU5k8vmYVSvK/img.png".toUri(), "Mara", categoryResponse, 100, 50,
    true, postResponse, curationResponse
)

@OptIn(ExperimentalMaterialApi::class)
@Composable //프로필, 인수로 유저의 정보를 받아옴
fun MyProfileScreen(data:User,id:Int) {
    val focusManager = LocalFocusManager.current
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    ModalBottomSheet(
        title = "",
        bottomSheetState = bottomSheetState,
        sheetScreen = {
            ModalBottomSheetItem(text = "신고", icon = R.drawable.ic_profile_block, trailing = true, modifier = Modifier.bounceClick {
                scope.launch {
                    bottomSheetState.hide()
                }
            })
            ModalBottomSheetItem(text = "차단", icon = R.drawable.ic_profile_block, trailing = true, modifier = Modifier.bounceClick {
                scope.launch {
                    bottomSheetState.hide()
                }
            })
        },
        uiScreen = {
            Scaffold (
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
                scaffoldState = scaffoldState,
                snackbarHost = {
                    ScaffoldSnackBar (
                        snackbarHostState = it
                    )
                }
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(it)
                        .background(color = Color.White)
                ) {
                    val context = LocalContext.current

                    // 프로필, 설정(또는 메뉴), 팔로우 목록을 묶고 있는 박스 레이아웃
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .padding(24.dp),
                        propagateMinConstraints = false
                    ) {
                        // 설정(내 프로필) 또는 메뉴

                        if (id == myId) {
                            Row(
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_profile_saved),
                                    contentDescription = null,
                                    modifier = Modifier.bounceClick {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                ProfileSaveActivity::class.java
                                            )
                                        )
                                    },
                                    tint = moduGray_normal
                                )
                                Spacer(modifier = Modifier.width(18.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.cog_8_tooth),
                                    contentDescription = null,
                                    modifier = Modifier.bounceClick {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                SettingsActivity::class.java
                                            )
                                        )
                                    },
                                    tint = moduGray_normal
                                )
                            }
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ellipsis_vertical),
                                contentDescription = null,
                                modifier = Modifier
                                    .bounceClick {
                                        focusManager.clearFocus()
                                        scope.launch {
                                            bottomSheetState.show()
                                        }
                                    }
                                    .align(Alignment.TopEnd),
                                tint = moduGray_normal
                            )
                        }

                        // 프로필
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .height(50.dp)
                                .bounceClick {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            ProfileFollowActivity::class.java
                                        )
                                    )
                                }
                        ) {
                           GlideImage(
                                imageModel = data.image,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                            )
                            Column(
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .height(30.dp)
                                ) {
                                    Text(
                                        text = data.name,
                                        style = TextStyle(
                                            color = moduBlack,
                                            textAlign = TextAlign.Center,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                    )
                                    if(data.state) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_check_solid),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .align(Alignment.CenterVertically)
                                                .padding(start = 5.dp)
                                                .size(18.dp)
                                        )
                                    }
                                }
                                Text(
                                    modifier = Modifier
                                        .height(20.dp)
                                        .wrapContentWidth(),
                                    text = "팔로워 ${data.follower} · 게시물 ${data.post!!.size}",
                                    style = TextStyle(
                                        color = moduGray_normal,
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chevron_right),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(vertical = 15.dp)
                                    .size(20.dp),
                                tint = moduGray_normal
                            )
                        }
                    }

                    // 카테고리를 띄울 레이지로우와 카테고리 추가(내 프로필) 또는 팔로우 버튼을 넣을 로우레이아웃
                    Row(
                        Modifier
                            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                            .fillMaxWidth()
                            .height(36.dp)
                    ) {
                        // 카테고리 리스트가 들어갈 레이지로우
                        LazyRow(
                            modifier = Modifier.fillMaxHeight(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(data.category) { category ->
                                Card(
                                    Modifier
                                        .wrapContentWidth()
                                        .fillMaxHeight(),
                                    backgroundColor = moduBackground,
                                    shape = RoundedCornerShape(15.dp),
                                    elevation = 0.dp
                                ) {
                                    Text( text = category,
                                        style = TextStyle(
                                            color = moduBlack,
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Center
                                        ),
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .align(Alignment.CenterVertically))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        val followState = remember { mutableStateOf(true) }
                        // 카테고리 추가(내 프로필) 또는 팔로우 버튼 역할을 할 카드
                        if (id == myId) {
                            Card(
                                modifier = Modifier
                                    .width(36.dp)
                                    .height(36.dp)
                                    .bounceClick {

                                    },
                                shape = RoundedCornerShape(10.dp),
                                backgroundColor = moduBackground,
                                elevation = 0.dp
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.plus),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .background(color = Color.Transparent)
                                        .bounceClick {
                                            RetrofitBuilder.userAPI
                                                .getUserByNickname(
                                                    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkbGRtc3RqcTk5QGdtYWlsLmNvbSIsInJvbGVzIjpbIlJPTEVfR0VORVJBTCJdLCJpYXQiOjE2NzQ4ODQ5ODYsImV4cCI6MTY3NDg4Njc4Nn0.hMxdk8INOCOp_NJiXkwj3QjytFXRW1le_wHvf1Q_4V4",
                                                    "Mara"
                                                )
                                                .enqueue(object : Callback<FindByNicknameRes> {
                                                    override fun onResponse(call: Call<FindByNicknameRes>, response: Response<FindByNicknameRes>) {
                                                        Log.d(ContentValues.TAG, "onResponse: \n${response.message()}")
                                                    }

                                                    override fun onFailure(call: Call<FindByNicknameRes>, t: Throwable) {
                                                        Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                                                    }
                                                })
                                        }
                                )
                            }
                        } else {
                            Card(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .width(74.dp)
                                    .height(36.dp)
                                    .bounceClick {
                                        // 팔로우 api
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar("${data.name} 님을 팔로우 했어요.")
                                        }
                                        followState.value = !followState.value
                                    },
                                shape = RoundedCornerShape(10.dp),
                                backgroundColor = if(followState.value) { moduBackground } else { moduPoint },
                                elevation = 0.dp
                            ) {
                                Text(
                                    text = if(followState.value) { "팔로잉" } else { "팔로우" },
                                    style = TextStyle(
                                        color = if(followState.value) { Color.Black } else { Color.White },
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    ),
                                    modifier = Modifier
                                        .padding(vertical = 6.dp, horizontal = 10.dp)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                    // 탭 구현
                    // 탭으로 넘길 때 만약 내 프로필이라면 포스트, 큐레이션 추가 버튼까지 넘겨야됨
                    // 그럼 포스트리스트랑 큐레이션리스트의 맨 앞에 추가해서 넣으면 됨
                    if (data.state)
                    {
                        if(id == myId)
                            CuratorProfileTab(
                                listOf(PostCard()).plus(data.post!!),
                                listOf(CurationCard()).plus(data.curation!!)
                            )
                        else
                            CuratorProfileTab(data.post!!, data.curation!!)
                    }
                    else if(id == myId)
                        ProfileTab(listOf(PostCard()).plus(data.post!!))
                    else
                        ProfileTab(data.post!!)
                }
            }
        }
    )
}