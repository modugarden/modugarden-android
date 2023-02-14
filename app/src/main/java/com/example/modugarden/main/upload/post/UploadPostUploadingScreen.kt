package com.example.modugarden.main.upload.post

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.CurationUploadResponse
import com.example.modugarden.data.Category
import com.example.modugarden.data.UploadPost
import com.example.modugarden.main.upload.UploadCurationEx
import com.example.modugarden.main.upload.curation.UriUtil
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_POST
import com.example.modugarden.ui.theme.ShowProgressBar
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_light
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalPagerApi::class)
@Composable
fun UploadPostUploadingScreen(navController: NavHostController, data: UploadPost) {

    val pagerStateAuto = rememberPagerState()
    val mContext = LocalContext.current
    val oneExecutor = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val animationData = remember { mutableStateListOf<Boolean>(false, false, false, false, false) }

    LaunchedEffect(key1 = pagerStateAuto) {
        launch {
            while (true) {
                delay(1000)
                with(pagerStateAuto) {
                    val target = if (currentPage < pageCount - 1) currentPage + 1 else 0

                    tween<Float>(
                        durationMillis = 500,
                        easing = EaseOutExpo
                    )
                    animateScrollToPage(page = target)
                }
            }
        }
    }

    LaunchedEffect(key1 = animationData) {
        launch {
            while (true) {
                delay(500)
                animationData[0] = true
                delay(500)
                animationData[1] = true
                delay(500)
                animationData[2] = true
                delay(500)
                animationData[3] = true
                delay(500)
                animationData[4] = true
            }
        }
    }

    if(!(oneExecutor.value)) {
        oneExecutor.value = true
        var imageList = listOf<MultipartBody.Part>()
        val jsonData = JsonObject()
        val jsonDataContent = JsonArray()
        val jsonDataLocation = JsonArray()
        //포스트 업로드 데이터 전달 API 연결.
        for(i in 0 until data.image.size) {
            val file = UriUtil.toFile(mContext, data.image[i])
            val requestFile = MultipartBody.Part.createFormData(
                name = "file",
                filename = file.name,
                body = file.asRequestBody("image/*".toMediaType())
            )
            imageList += requestFile
            jsonDataContent.add(data.description[i])
            jsonDataLocation.add(data.location[i])
        }

        jsonData.apply {
            addProperty("category", data.category.category)
            add("content", jsonDataContent)
            add("location", jsonDataLocation)
            addProperty("title", data.title)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val jsonBody = jsonData.toString().toRequestBody(mediaType)

        RetrofitBuilder.postCreateAPI.postCreateAPI(jsonBody, imageList)
            .enqueue(object: Callback<CurationUploadResponse> {
                override fun onResponse(
                    call: Call<CurationUploadResponse>,
                    response: Response<CurationUploadResponse>
                ) {
                    if(response.isSuccessful) {
                        val res = response.body()
                        if(res != null) {
                            if(res.isSuccess) {
                                navController.navigate(NAV_ROUTE_UPLOAD_POST.UPLOADSUCCESSFULLY.routeName) {
                                    popUpTo(0) {
                                        inclusive = true
                                    }
                                }
                            }
                            Log.e("apires", res.toString())
                        }
                        else {
                            Log.e("apires", response.toString())
                        }
                    }
                    else {
                        Toast.makeText(mContext, "데이터를 받지 못했어요", Toast.LENGTH_SHORT).show()
                        Log.e("apires", response.toString())
                        Log.e("apires", jsonData.toString())
                    }
                }

                override fun onFailure(
                    call: Call<CurationUploadResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(mContext, "서버와 연결하지 못했어요", Toast.LENGTH_SHORT).show()
                }
            })
    }

    Column {
        Spacer(Modifier.height(100.dp))
        Text("포스트를 업로드 하고 있어요", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = moduBlack, textAlign = TextAlign.Center, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.size(30.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            shape = RoundedCornerShape(15.dp),
            backgroundColor = moduBackground,
            elevation = 0.dp
        ) {
            UploadPostUploadingCard(pagerStateAuto, data, animationData)
        }
        Spacer(Modifier.weight(1f))
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun UploadPostUploadingCard(
    pagerStateAuto: PagerState,
    data: UploadPost,
    animationData: MutableList<Boolean>
) {
    Column {
        Card(
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            backgroundColor = Color.White,
            elevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .padding(top = 20.dp)
                .height(50.dp)
        ) {
            Column {
                Spacer(Modifier.weight(1f))
                Row() {
                    Spacer(Modifier.size(10.dp))
                    AnimatedVisibility(
                        visible = animationData[0],
                        enter = scaleIn(animationSpec = tween(500, easing = EaseOutExpo))
                    ) {
                        Card(
                            shape = CircleShape,
                            backgroundColor = moduGray_light,
                            modifier = Modifier.size(15.dp),
                            elevation = 0.dp
                        ) {}
                    }
                    Spacer(Modifier.size(7.dp))
                    AnimatedVisibility(
                        visible = animationData[1],
                        enter = slideInHorizontally(animationSpec = tween(500, easing = EaseOutExpo)) + fadeIn(animationSpec = tween(300))
                    ) {
                        Card(
                            shape = RoundedCornerShape(4.dp),
                            backgroundColor = moduGray_light,
                            modifier = Modifier
                                .width(100.dp)
                                .height(15.dp),
                            elevation = 0.dp
                        ) {}
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        HorizontalPager(
            count = data.image.size, userScrollEnabled = false, reverseLayout = true, itemSpacing = 0.dp, state = pagerStateAuto,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            GlideImage(
                imageModel = data.image[page],
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
                requestOptions = { RequestOptions().override(500) },
                loading = {
                    ShowProgressBar()
                }
            )
        }
        Card(
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .height(67.dp),
            backgroundColor = Color.White,
            elevation = 0.dp
        ) {
            Column {
                Spacer(Modifier.height(15.dp))
                AnimatedVisibility(
                    visible = animationData[2],
                    enter = slideInHorizontally(animationSpec = tween(500, easing = EaseOutExpo)) + fadeIn(animationSpec = tween(300))
                ) {
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        backgroundColor = moduGray_light,
                        elevation = 0.dp,
                        modifier = Modifier
                            .height(15.dp)
                            .width(100.dp)
                            .padding(horizontal = 10.dp)
                    ) { }
                }
                Spacer(Modifier.size(7.dp))
                Row(Modifier.padding(horizontal = 10.dp)) {
                    AnimatedVisibility(
                        visible = animationData[3],
                        enter = slideInHorizontally(animationSpec = tween(500, easing = EaseOutExpo)) + fadeIn(animationSpec = tween(300))
                    ) {
                        Card(
                            shape = RoundedCornerShape(4.dp),
                            backgroundColor = moduGray_light,
                            elevation = 0.dp,
                            modifier = Modifier
                                .height(15.dp)
                                .width(54.dp)
                        ) { }
                    }
                    Spacer(Modifier.weight(1f))
                    AnimatedVisibility(
                        visible = animationData[4],
                        enter = slideInHorizontally(animationSpec = tween(500, easing = EaseOutExpo)) + fadeIn(animationSpec = tween(300))
                    ) {
                        Card(
                            shape = RoundedCornerShape(4.dp),
                            backgroundColor = moduGray_light,
                            elevation = 0.dp,
                            modifier = Modifier
                                .height(15.dp)
                                .width(40.dp)
                        ) { }
                    }
                }
                Spacer(Modifier.size(15.dp))
            }
        }
        Card(
            shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .padding(bottom = 20.dp)
                .height(46.dp),
            backgroundColor = Color.White,
            elevation = 0.dp
        ) {
            Column {
                Divider(color = moduGray_light, thickness = 1.dp)
                Spacer(Modifier.size(15.dp))
                Row(
                    Modifier
                        .padding(horizontal = 10.dp)
                        .padding(bottom = 10.dp)) {
                    Card(
                        modifier = Modifier.size(15.dp),
                        shape = CircleShape,
                        backgroundColor = moduGray_light,
                        elevation = 0.dp
                    ) {}
                    Spacer(Modifier.size(7.dp))
                    Card(
                        modifier = Modifier.size(15.dp),
                        shape = CircleShape,
                        backgroundColor = moduGray_light,
                        elevation = 0.dp
                    ) {}
                    Spacer(Modifier.size(7.dp))
                    Card(
                        modifier = Modifier.size(15.dp),
                        shape = CircleShape,
                        backgroundColor = moduGray_light,
                        elevation = 0.dp
                    ) {}
                    Spacer(Modifier.weight(1f))
                    Card(
                        modifier = Modifier.size(15.dp),
                        shape = CircleShape,
                        backgroundColor = moduGray_light,
                        elevation = 0.dp
                    ) {}
                }
            }
        }
    }
}