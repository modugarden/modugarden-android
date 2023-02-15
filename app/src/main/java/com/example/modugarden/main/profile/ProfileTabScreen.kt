package com.example.modugarden.main.profile

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.R
import com.example.modugarden.api.AuthCallBack
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.*
import com.example.modugarden.api.dto.PostDTO.*
import com.example.modugarden.main.content.CurationContentActivity
import com.example.modugarden.main.content.PostContentActivity
import com.example.modugarden.main.content.timeToDate
import com.example.modugarden.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

@Composable
fun ProfileTab (
    postList: List<GetUserPostResponseContent>,
    context: Context
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawLine(
                    color = moduGray_light,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            },
        contentPadding = PaddingValues(18.dp)
    ) {
        items(postList) { postCard ->
            // 이미지가 들어간 버튼을 넣어야 함
            Box(modifier = Modifier.bounceClick {
                context.startActivity(
                    Intent(context, PostContentActivity::class.java)
                        .putExtra("board_id", postCard.id)
                        .putExtra("run",true)
                )
            }) {
                GlideImage(
                    imageModel =
                    if(postCard.id == 0) {
                        R.drawable.plus
                    }
                    else {
                        postCard.image
                    },
                    contentDescription = null,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(15.dp))
                            .background(moduBackground),
                    contentScale =
                    if(postCard.id == 0) {
                        ContentScale.None
                    }
                    else {
                        ContentScale.Crop
                    },
                    loading = {
                        ShowProgressBar()
                    },
                    // shows an error text if fail to load an image.
                    failure = {
                        Text(text = "image request failed.")
                    },
                    requestOptions = {
                        RequestOptions()
                            .override(256,256)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CuratorProfileTab(
    postList: List<GetUserPostResponseContent>,
    curationList: List<GetUserCurationsResponseContent>,
    context: Context
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()


    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = moduBlack,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState,tabPositions),
                color = moduBlack
            )
        }
    ) {
        pages.forEachIndexed { index, title ->
            Tab(
                text = {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        color =
                        if(pagerState.currentPage == index) moduBlack
                        else moduGray_strong
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }

    HorizontalPager(
        count = pages.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
    ) { page ->
        when(page) {
            0 -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(18.dp)
                ) {
                    items(postList) { postCard ->
                        // 이미지가 들어간 버튼을 넣어야 함
                        Box(modifier = Modifier.bounceClick {
                            context.startActivity(
                                Intent(context, PostContentActivity::class.java)
                                    .putExtra("board_id", postCard.id)
                                    .putExtra("run",true)
                            )
                        }) {
                            GlideImage(
                                imageModel =
                                if(postCard.id == 0) {
                                    R.drawable.plus
                                }
                                else {
                                    postCard.image
                                },
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(moduBackground),
                                contentScale =
                                if(postCard.id == 0) {
                                    ContentScale.None
                                }
                                else {
                                    ContentScale.Crop
                                },
                                loading = {
                                    ShowProgressBar()
                                },
                                // shows an error text if fail to load an image.
                                failure = {
                                    Text(text = "image request failed.")
                                },
                                requestOptions = {
                                    RequestOptions()
                                        .override(256,256)
                                }
                            )
                        }
                    }
                }
            }
            1 -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    contentPadding = PaddingValues(18.dp)
                ) {
                    items(curationList) { curationCard ->
                        Row(
                            modifier = Modifier
                                .height(90.dp)
                                .bounceClick {
                                    context.startActivity(
                                        Intent(context, CurationContentActivity::class.java)
                                            .putExtra("curation_id", curationCard.id)
                                            .putExtra("run",true)
                                    )
                                }
                        ) {
                            GlideImage(
                                imageModel = if(curationCard.id == 0) {
                                    R.drawable.plus
                                }
                                else {
                                    curationCard.image
                                },
                                contentDescription = null,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(moduBackground),
                                contentScale =
                                if(curationCard.id == 0) {
                                    ContentScale.None
                                }
                                else {
                                    ContentScale.Crop
                                },
                                loading = {
                                    ShowProgressBar()
                                },
                                // shows an error text if fail to load an image.
                                failure = {
                                    Text(text = "image request failed.")
                                },
                                requestOptions = {
                                    RequestOptions()
                                        .override(256,256)
                                }
                            )
                            Spacer(modifier = Modifier.width(18.dp))
                            if(curationCard.id == 0) {
                                Text(
                                    text = curationCard.title,
                                    style = TextStyle(
                                        color = moduGray_strong,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                            else {
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
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class)
@Composable
fun StoredTab(
//    postList: List<GetStoredPostResponseContent>,
//    curationList: List<GetStoredCurationsResponseContent>,
    context: Context
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val postList = remember { mutableStateOf<List<GetStoredPostResponseContent>?>(
        listOf())
    }

    val curationList = remember { mutableStateOf<List<GetStoredCurationsResponseContent>?>(
        listOf())
    }
    RetrofitBuilder.postAPI.getMyPostStorage()
        .enqueue(object :
            AuthCallBack<GetStoredPostResponse>(context, "저장된 항목 부르기 성공!") {
            override fun onResponse(
                call: Call<GetStoredPostResponse>,
                response: Response<GetStoredPostResponse>
            ) {
                super.onResponse(call, response)
                if(response.body()?.content != null)
                    postList.value = response.body()?.content
            }
        })

    RetrofitBuilder.curationAPI.getMyCurationStorage()
        .enqueue(object :
            AuthCallBack<GetStoredCurationsResponse>(context, "저장된 항목 부르기 성공!") {
            override fun onResponse(
                call: Call<GetStoredCurationsResponse>,
                response: Response<GetStoredCurationsResponse>
            ) {
                super.onResponse(call, response)
                if (response.body()?.content != null)
                    curationList.value = response.body()?.content
            }
        })

    val postLauncher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.StartIntentSenderForResult()) {
        RetrofitBuilder.postAPI.getMyPostStorage()
        .enqueue(object :
            AuthCallBack<GetStoredPostResponse>(context, "저장된 항목 부르기 성공!") {
            override fun onResponse(
                call: Call<GetStoredPostResponse>,
                response: Response<GetStoredPostResponse>
            ) {
                super.onResponse(call, response)
                if(response.body()?.content != null)
                    postList.value = response.body()?.content
            }
        })
    }

    val curationLauncher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.StartIntentSenderForResult()) {

        RetrofitBuilder.curationAPI.getMyCurationStorage()
            .enqueue(object :
                AuthCallBack<GetStoredCurationsResponse>(context, "저장된 항목 부르기 성공!") {
                override fun onResponse(
                    call: Call<GetStoredCurationsResponse>,
                    response: Response<GetStoredCurationsResponse>
                ) {
                    super.onResponse(call, response)
                    if (response.body()?.content != null)
                        curationList.value = response.body()?.content
                }
            })

    }

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = moduBlack,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState,tabPositions),
                color = moduBlack
            )
        }
    ) {
        pages.forEachIndexed { index, title ->
            Tab(
                text = {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        color =
                        if(pagerState.currentPage == index) moduBlack
                        else moduGray_strong
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }

    HorizontalPager(
        count = pages.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
    ) { page ->
        when(page) {
            0 -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(18.dp)
                ) {
                    items(
                        items = postList.value!!,
                        key = { it.board_id }
                    ) { postCard ->
                        // 이미지가 들어간 버튼을 넣어야 함
                        Box(modifier = Modifier.bounceClick {
                            val intent = Intent(context, PostContentActivity::class.java)
                            val bundle = Bundle()

                            bundle.putInt("board_id", postCard.board_id)
                            bundle.putBoolean("run", true)

                            intent.putExtras(bundle)

                            val pendIntent: PendingIntent
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                pendIntent = PendingIntent
                                    .getActivity(
                                        context, 0,
                                        intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent . FLAG_MUTABLE
                                    )

                            } else {
                                pendIntent = PendingIntent
                                    .getActivity(
                                        context, 0,
                                        intent, PendingIntent.FLAG_UPDATE_CURRENT
                                    )
                            }

                            postLauncher.launch(
                                IntentSenderRequest
                                    .Builder(pendIntent)
                                    .build()
                            )
                        }) {
                            GlideImage(
                                imageModel =
                                if(postCard.board_id == 0) {
                                    R.drawable.plus
                                }
                                else {
                                    postCard.preview_img
                                },
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(moduBackground),
                                contentScale =
                                if(postCard.board_id == 0) {
                                    ContentScale.None
                                }
                                else {
                                    ContentScale.Crop
                                },
                                loading = {
                                    ShowProgressBar()
                                },
                                requestOptions = {
                                    RequestOptions()
                                        .override(256,256)
                                }
                            )
                        }
                    }
                }
            }
            1 -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    contentPadding = PaddingValues(18.dp)
                ) {
                    items(curationList.value!!) { curationCard ->
                        Row(
                            modifier = Modifier
                                .height(90.dp)
                                .bounceClick {
                                    val intent = Intent(context, CurationContentActivity::class.java)
                                    val bundle = Bundle()

                                    bundle.putInt("curation_id", curationCard.id)
                                    bundle.putBoolean("run", true)

                                    intent.putExtras(bundle)

                                    val pendIntent: PendingIntent
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        pendIntent = PendingIntent
                                            .getActivity(
                                                context, 0,
                                                intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent . FLAG_MUTABLE
                                            )

                                    } else {
                                        pendIntent = PendingIntent
                                            .getActivity(
                                                context, 0,
                                                intent, PendingIntent.FLAG_UPDATE_CURRENT
                                            )
                                    }

                                    curationLauncher.launch(
                                        IntentSenderRequest
                                            .Builder(pendIntent)
                                            .build()
                                    )
                                }
                        ) {
                            GlideImage(
                                imageModel = if(curationCard.id == 0) {
                                    curationCard.user_id
                                }
                                else {
                                    curationCard.preview_image
                                },
                                contentDescription = null,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(moduBackground),
                                contentScale =
                                if(curationCard.id == 0) {
                                    ContentScale.None
                                }
                                else {
                                    ContentScale.Crop
                                },
                                loading = {
                                    ShowProgressBar()
                                },
                                requestOptions = {
                                    RequestOptions()
                                        .override(256,256)
                                }
                            )
                            Spacer(modifier = Modifier.width(18.dp))
                            if(curationCard.id == 0) {
                                Text(
                                    text = curationCard.title,
                                    style = TextStyle(
                                        color = moduGray_strong,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                            else {
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
                                        text = "${curationCard.category_category}, ${timeToDate(curationCard.localDateTime)}",
                                        fontSize = 12.sp,
                                        color = moduGray_strong
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}