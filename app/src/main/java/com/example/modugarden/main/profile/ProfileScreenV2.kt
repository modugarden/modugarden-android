package com.example.modugarden.main.profile

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.modugarden.ApplicationClass
import com.example.modugarden.ApplicationClass.Companion.clientId
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.example.modugarden.R
import com.example.modugarden.api.AuthCallBack
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.*
import com.example.modugarden.main.content.CurationContentActivity
import com.example.modugarden.main.content.PostContentActivity
import com.example.modugarden.main.profile.follow.ProfileFollowScreen
import com.example.modugarden.main.settings.SettingsActivity
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UserViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

private val myId = sharedPreferences.getInt(clientId, 0)

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun ProfileScreenV2(
    userId: Int = 0,
    upperNavHostController: NavHostController,
    navController: NavController = NavController(LocalContext.current),
    userViewModel: UserViewModel
) {
    val focusManager = LocalFocusManager.current
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val data = remember { mutableStateOf( UserInfoResResult() ) }
    val followState = remember { mutableStateOf(false) }

    RetrofitBuilder.userAPI.readUserInfo(userId)
        .enqueue(object : AuthCallBack<UserInfoRes>(context, "성공!") {
            override fun onResponse(call: Call<UserInfoRes>, response: Response<UserInfoRes>) {
                super.onResponse(call, response)
                data.value = response.body()?.result!!
                followState.value = response.body()!!.result.follow
            }
        })

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
            ) { it ->
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(it)
                        .background(color = Color.White)
                ) {
                    Spacer(modifier = Modifier.size(18.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .padding(horizontal = 18.dp)
                    ) {
                        if(!userViewModel.getOnBNB()) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                                contentDescription = null,
                                modifier = Modifier
                                    .bounceClick {
                                        upperNavHostController.navigateUp()
                                    }
                                    .size(24.dp),
                                tint = moduBlack
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        if(myId != data.value.id) {
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
                                    .size(24.dp)
                            )
                        }

                    }
                    Row(
                       modifier = Modifier
                           .align(CenterHorizontally)
                           .wrapContentSize()
                    ) {
                        Spacer(modifier = Modifier.width(42.dp))
                        Text(
                            text = data.value.nickname,
                            style = TextStyle(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = moduBlack
                            )
                        )
                        Spacer(modifier = Modifier.width(18.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.cog_8_tooth),
                            contentDescription = null,
                            modifier = Modifier
                                .align(CenterVertically)
                                .size(24.dp)
                                .bounceClick {
                                    if (myId == data.value.id)
                                        context.startActivity(
                                            Intent(
                                                context,
                                                SettingsActivity::class.java
                                            )
                                        )
                                },
                            tint =
                            if(myId == data.value.id)
                                moduGray_normal
                            else
                                Color.Transparent
                        )
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text =  data.value.categories.joinToString(", ","",""),
                        style = TextStyle(
                            color = moduGray_normal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier
                            .align(CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.size(18.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 42.dp)
                            .height(120.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .align(CenterStart)
                                .wrapContentSize(),
                            elevation = 0.dp
                        ) {
                            Column {
                                Text(
                                    text = data.value.postCount.toString(),
                                    style = TextStyle(
                                        color = moduBlack,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    ),
                                    modifier = Modifier.align(CenterHorizontally)
                                )
                                Text(
                                    text = "게시물",
                                    style = TextStyle(
                                        color = moduGray_normal,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .align(Center)
                        ) {
                            GlideImage(
                                imageModel = data.value.profileImage,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .clip(CircleShape)
                            )
                        }
                        Card(
                            modifier = Modifier
                                .bounceClick {
                                    navController.navigate(ProfileFollowScreen.Follow.name)
                                }
                                .align(CenterEnd)
                                .wrapContentSize(),
                            elevation = 0.dp
                        ) {
                            Column {
                                Text(
                                    text = data.value.followerCount.toString(),
                                    style = TextStyle(
                                        color = moduBlack,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    ),
                                    modifier = Modifier.align(CenterHorizontally)
                                )
                                Text(
                                    text = "팔로워",
                                    style = TextStyle(
                                        color = moduGray_normal,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.size(30.dp))

                    if(myId != data.value.id) {
                        FollowCard(
                            id = data.value.id,
                            modifier = Modifier
                                .width(120.dp)
                                .width(36.dp)
                                .align(CenterHorizontally),
                            snackBarAction = {
                                scope.launch {
                                    if (followState.value) scaffoldState.snackbarHostState.showSnackbar("${data.value.nickname} 님을 팔로우 했어요.")
                                    else scaffoldState.snackbarHostState.showSnackbar("${data.value.nickname} 님을 언팔로우 했어요.")
                                }
                            },
                            followState = followState,
                            contentModifier = Modifier
                                .padding(vertical = 6.dp, horizontal = 10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.size(30.dp))

                    val postList = remember { mutableStateOf<List<PostDTO.GetUserPostResponseContent>?>(
                        listOf())
                    }

                    RetrofitBuilder.postAPI.getUserPost(userId)
                        .enqueue(object : AuthCallBack<PostDTO.GetUserPostResponse>(context, "성공!") {
                            override fun onResponse(
                                call: Call<PostDTO.GetUserPostResponse>,
                                response: Response<PostDTO.GetUserPostResponse>
                            ) {
                                super.onResponse(call, response)
                                if(response.body()?.content != null)
                                    postList.value = response.body()?.content
                            }
                        })

                    val curationList = remember { mutableStateOf(
                        listOf( GetUserCurationsResponseContent(
                            "", "",0, "", "")))
                    }

                    if(data.value.userAuthority == UserAuthority.ROLE_CURATOR.name) {
                        RetrofitBuilder.curationAPI.getUserCuration(userId)
                            .enqueue(object : AuthCallBack<GetUserCurationsResponse>(context, "성공!") {
                                override fun onResponse(
                                    call: Call<GetUserCurationsResponse>,
                                    response: Response<GetUserCurationsResponse>
                                ) {
                                    super.onResponse(call, response)
                                    if(response.body()?.content != null)
                                        curationList.value = response.body()?.content!!
                                }
                            })
                    }

                    Row(
                        Modifier
                            .padding(horizontal = 18.dp)
                    ) {
                        Text(text = "포스트",
                            fontSize = 20.sp,
                            color =
                                if(pagerState.currentPage == 0) moduBlack
                                else moduGray_normal,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .bounceClick {
                                scope.launch {
                                    pagerState.animateScrollToPage(0)
                                }
                        })
                        Spacer(Modifier.size(20.dp))
                        Text(text = "큐레이션",
                            fontSize = 20.sp,
                            color =
                                if(pagerState.currentPage == 1) moduBlack
                                else moduGray_normal,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.bounceClick {
                                scope.launch {
                                    pagerState.animateScrollToPage(1)
                                }
                        })
                        Spacer(modifier = Modifier.weight(1f))
                        if(myId == data.value.id) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_profile_saved),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .bounceClick {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                ProfileSaveActivity::class.java
                                            )
                                        )
                                    }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(18.dp))
                    HorizontalPager(
                        count = pages.size,
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                    ) { page ->
                        when (page) {
                            0 -> {
                                if (postList.value?.isEmpty() == false) {
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        verticalArrangement = Arrangement.spacedBy(18.dp),
                                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(18.dp)
                                    ) {
                                        items(postList.value!!) { postCard ->
                                            // 이미지가 들어간 버튼을 넣어야 함
                                            Box(modifier = Modifier.bounceClick {
                                                context.startActivity(
                                                    Intent(context, PostContentActivity::class.java)
                                                        .putExtra("board_id", postCard.id)
                                                        .putExtra("run", true)
                                                )
                                            }) {
                                                GlideImage(
                                                    imageModel =
                                                    if (postCard.id == 0) {
                                                        R.drawable.plus
                                                    } else {
                                                        postCard.image
                                                    },
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .aspectRatio(1f)
                                                        .clip(RoundedCornerShape(15.dp))
                                                        .background(moduBackground),
                                                    contentScale =
                                                    if (postCard.id == 0) {
                                                        ContentScale.None
                                                    } else {
                                                        ContentScale.Crop
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            1 -> {
                                if(data.value.userAuthority == UserAuthority.ROLE_CURATOR.name) {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(15.dp),
                                        contentPadding = PaddingValues(18.dp)
                                    ) {
                                        items(curationList.value) { curationCard ->
                                            Row(
                                                modifier = Modifier
                                                    .height(90.dp)
                                                    .bounceClick {
                                                        context.startActivity(
                                                            Intent(
                                                                context,
                                                                CurationContentActivity::class.java
                                                            )
                                                                .putExtra(
                                                                    "curation_id",
                                                                    curationCard.id
                                                                )
                                                                .putExtra("run", true)
                                                        )
                                                    }
                                            ) {
                                                GlideImage(
                                                    imageModel = if (curationCard.id == 0) {
                                                        R.drawable.plus
                                                    } else {
                                                        curationCard.image
                                                    },
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(90.dp)
                                                        .clip(RoundedCornerShape(15.dp))
                                                        .background(moduBackground),
                                                    contentScale =
                                                    if (curationCard.id == 0) {
                                                        ContentScale.None
                                                    } else {
                                                        ContentScale.Crop
                                                    }
                                                )
                                                Spacer(modifier = Modifier.width(18.dp))
                                                if (curationCard.id == 0) {
                                                    Text(
                                                        text = curationCard.title,
                                                        style = TextStyle(
                                                            color = moduGray_strong,
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight.Bold
                                                        ),
                                                        modifier = Modifier.align(Alignment.CenterVertically)
                                                    )
                                                } else {
                                                    Column(
                                                        modifier = Modifier
                                                            .height(42.dp)
                                                            .align(Alignment.CenterVertically)
                                                    ) {
                                                        Text(
                                                            text = curationCard.title,
                                                            style = TextStyle(
                                                                color = Color.Black,
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 14.sp
                                                            )
                                                        )
                                                        Spacer(modifier = Modifier.weight(1f))
                                                        Text(
                                                            text = "${curationCard.category}, ${curationCard.created_date}",
                                                            fontSize = 12.sp,
                                                            color = moduGray_strong
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                else {

                                }
                            }
                        }
                    }
                }
            }
        }
    )
}