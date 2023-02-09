package com.example.modugarden.main.upload.post

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import com.bumptech.glide.Glide
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder.searchLocationAPI
import com.example.modugarden.data.*
import com.example.modugarden.main.upload.UploadCurationEx
import com.example.modugarden.main.upload.UploadPostEx
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_POST
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UploadPostViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalPagerApi::class)
@Composable
fun UploadPostTagLocationResultScreen(
    navController: NavHostController,
    data: UploadPost,
    address: String,
    uploadPostViewModel: UploadPostViewModel,
    page1: Int
) {

    val complete = remember { mutableStateOf(false) }
    val res = remember { mutableStateOf(MapsGeocoding(
        listOf(""),
        listOf(Place(listOf(AddressComponent("", "", listOf(""))), "", "", Geometry(LatLngLiteral("", ""), Viewport(
            LatLngLiteral("", ""), LatLngLiteral("", "")
        )), "", "#f3f5f4", "", "", "")),
        "",
        "",
        listOf(""),
        ""
    ))}

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-33.8670522,151.1957362), 1f)
    }

    LaunchedEffect(key1 = null) {
        launch {
            searchLocationAPI.getUploadPostTagLocationSearchAPI(
                "-33.8670522,151.1957362", 1500, address
            ).enqueue(object: Callback<MapsGeocoding> {
                override fun onResponse(
                    call: Call<MapsGeocoding>,
                    response: Response<MapsGeocoding>
                ) {
                    if(response.isSuccessful) {
                        if(response.body() != null) {
                            res.value = response.body()!!
                            Log.e("apires", res.value.results.toString())
                            complete.value = true
                        }
                    }
                    else {
                        Log.e("apires", response.raw().toString())
                    }
                }

                override fun onFailure(call: Call<MapsGeocoding>, t: Throwable) {
                    Log.e("apires", "hi")
                }
            })
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)) {
        if(!(complete.value)) {
            Text("검색 결과 로딩 중", fontSize = 16.sp, color = moduBlack, modifier = Modifier.align(Alignment.Center))
        }
        else
        {
            Column {
                TopBar(title = "위치 검색", main = true, bottomLine = false)
                Row(
                    Modifier
                        .padding(start = 18.dp)
                        .padding(bottom = 18.dp)
                ) {
                    Text(text = "목록", fontSize = 20.sp, color = if(pagerState.currentPage == 0) moduBlack else moduGray_normal, fontWeight = FontWeight.Bold, modifier = Modifier.bounceClick {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    })
                    Spacer(Modifier.size(20.dp))
                    Text(text = "지도", fontSize = 20.sp, color = if(pagerState.currentPage == 1) moduBlack else moduGray_normal, fontWeight = FontWeight.Bold, modifier = Modifier.bounceClick {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    })
                }
                HorizontalPager(count = 2, state = pagerState, modifier = Modifier.weight(1f), userScrollEnabled = false) { page ->
                    Box(Modifier.fillMaxSize())
                    {
                        when(page) {
                            0 -> {
                                LazyColumn() {
                                    itemsIndexed(
                                        items = res.value.results,
                                        key = { index: Int, item: Place ->
                                            item.adr_address
                                        }
                                    ) { index: Int, item: Place ->
                                        UploadPostTagLocationCard(item = item, index, uploadPostViewModel, page1, navController)
                                    }
                                }
                            }
                            else -> {
                                GoogleMap(
                                    modifier = Modifier,
                                    uiSettings = MapUiSettings(
                                        compassEnabled = true,
                                        mapToolbarEnabled = true,
                                        myLocationButtonEnabled = true
                                    ),
                                    cameraPositionState = cameraPosition
                                ) {
                                    var totLat = 0.0
                                    var totLng = 0.0
                                    for(i in 0 until res.value.results.size) {
                                        Marker(
                                            state = rememberMarkerState(position = LatLng(res.value.results[i].geometry.location.lat.toDouble(), res.value.results[i].geometry.location.lng.toDouble())),
                                            title = res.value.results[i].name,
                                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                                        )
                                        totLat += res.value.results[i].geometry.location.lat.toDouble()
                                        totLng += res.value.results[i].geometry.location.lng.toDouble()
                                        cameraPosition.position = CameraPosition.fromLatLngZoom(
                                            LatLng(totLat/(res.value.results.size).toDouble(), totLng/(res.value.results.size).toDouble()), 10f)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UploadPostTagLocationCard(
    item: Place,
    index: Int,
    uploadPostViewModel: UploadPostViewModel,
    page: Int,
    navController: NavHostController
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        backgroundColor = Color.White,
        elevation = 0.dp,
        modifier = Modifier.bounceClick {
            uploadPostViewModel.addLocation(
                "${item.name}``${item.geometry.location.lat}``${item.geometry.location.lng}``${item.place_id}", page)
            navController.navigate(NAV_ROUTE_UPLOAD_POST.IMAGEEDIT.routeName) {
                popUpTo(NAV_ROUTE_UPLOAD_POST.IMAGEEDIT.routeName) {
                    inclusive = true
                }
            }
        }
    ) {
        Row(Modifier.padding(18.dp)) {
            Card(
                shape = CircleShape,
                elevation = 0.dp,
                modifier = Modifier.size(50.dp),
                backgroundColor = Color(item.icon_background_color.toColorInt())
            ) {
                GlideImage(
                    imageModel = item.icon,
                    modifier = Modifier
                        .padding(15.dp)
                        .size(30.dp),
                    colorFilter = ColorFilter.tint(Color.White),
                )
            }
            Spacer(Modifier.size(18.dp))
            Column(
                Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)) {
                Text(item.name, fontSize = 20.sp, color = moduBlack, overflow = TextOverflow.Ellipsis, maxLines = 1)
                Spacer(Modifier.size(5.dp))
                Text(item.formatted_address, fontSize = 14.sp, color = moduGray_strong, overflow = TextOverflow.Ellipsis, maxLines = 1)
            }
            Spacer(Modifier.size(18.dp))
            Box(
                Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_chevron_right_bold),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}