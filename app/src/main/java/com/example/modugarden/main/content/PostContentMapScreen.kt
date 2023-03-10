package com.example.modugarden.main.content

import android.media.Image
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.R
import com.example.modugarden.data.MapInfo
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.ui.theme.ShowProgressBar
import com.example.modugarden.ui.theme.addFocusCleaner
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_strong
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.skydoves.landscapist.glide.GlideImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun PostContentMapScreen(navController: NavHostController,data:MapInfo) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .addFocusCleaner(focusManager)
    )
    {

        Box {
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(data.lat!!, data.lng!!), 20f)
            }
            GoogleMap(
                Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ){
                Marker(
                    state = MarkerState(position = LatLng(data.lat!!, data.lng!!)),
                    title = data.location
                )
            }
        }
        // ??????
        Column(
            modifier = Modifier
                .background(Color.White)
                .align(Alignment.TopCenter),
        ) {
            Row(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                // ????????????
                Icon(
                    modifier = Modifier
                        .bounceClick { navController.popBackStack() },
                    painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                    contentDescription = "?????? ??????", tint = moduBlack
                )
                Spacer(modifier = Modifier.size(18.dp))
                Text(text = "?????? ??????", style = moduBold, fontSize = 16.sp)
                Spacer(modifier = Modifier.size(5.dp))

            }
            // ?????????
            Divider(color = moduGray_light, modifier = Modifier
                .fillMaxWidth()
                .height(1.dp))
        }

        //??????
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(18.dp)
            .align(Alignment.BottomCenter))
        { // ?????? ??????
            if (data.photoURL==null){
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(moduGray_light)
                        .size(40.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_map_pin),
                        contentDescription = "",
                        modifier = Modifier
                    )
                }
            }
           else {
                GlideImage(
                    imageModel = data.photoURL,
                    contentDescription = "",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp)
                        .border(1.dp, moduGray_light, CircleShape),
                    contentScale = ContentScale.Crop,
                    requestOptions = {
                        RequestOptions()
                            .override(256, 256)
                    },
                    loading = {
                        ShowProgressBar()
                    }
                )
            }
            Spacer(modifier = Modifier.width(18.dp))
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                // ??????
                Text(text = data.location!!, style = moduBold, fontSize = 12.sp,)
                // ?????? ??????
                Text(text = data.address!!, fontSize = 14.sp, color = Color.Gray)
            }

        }

    }

    }

