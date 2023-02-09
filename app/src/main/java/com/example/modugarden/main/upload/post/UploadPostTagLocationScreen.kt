package com.example.modugarden.main.upload.post

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder.searchLocationAPI
import com.example.modugarden.api.UploadPostTagLocationSearchAPIRetrofitBuilder
import com.example.modugarden.api.api.UploadPostTagLocationSearchAPI
import com.example.modugarden.data.MapsGeocoding
import com.example.modugarden.data.UploadPost
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_POST
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UploadPostViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class, ExperimentalPagerApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun UploadPostTagLocationScreen(
    navController: NavHostController,
    uploadPostViewModel: UploadPostViewModel,
    data: UploadPost,
    page: Int
) {
    val focusManager = LocalFocusManager.current
    val textData = remember { mutableStateOf("") }
    val textFocusedData = remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Box() {
        Column {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp)
            ) {
                Spacer(Modifier.size(30.dp))
                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .bounceClick {
                                navController.popBackStack()
                            }
                            .align(Alignment.CenterVertically)
                    )
                    Spacer(Modifier.size(18.dp))
                    Text("위치 찾기", fontSize = 22.sp, color = moduBlack, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    AnimatedVisibility(
                        visible = data.location[page].isNotEmpty(),
                        enter = scaleIn(tween(300, easing = EaseOutExpo)) + fadeIn(tween(300)),
                        exit = scaleOut(tween(300, easing = EaseOutExpo)) + fadeOut(tween(300))
                    ) {
                        SmallButton(text = "위치 삭제", backgroundColor = moduBackground, textColor = moduGray_strong) {
                            uploadPostViewModel.removeOnlyLocation(page)
                        }
                    }
                }
                Spacer(Modifier.size(18.dp))
                EditText(title = "", data = textData, isTextFieldFocused = textFocusedData, textStyle = TextStyle(fontSize = 14.sp, color = moduBlack), singleLine = true, keyboardActions = KeyboardActions(
                    onDone = {
                        if(textData.value != "") {
                            navController.navigate(NAV_ROUTE_UPLOAD_POST.TAGLOCATIONRESULT.routeName+"/${textData.value}/${page}")
                        }
                    }
                ), placeholder = "위치를 찾아보세요", placeholderSize = 14)
                Spacer(Modifier.size(18.dp))
            }
            GoogleMap(
                modifier = Modifier
                    .weight(1f)
                    .addFocusCleaner(focusManager),
                onMapClick = {

                },
                uiSettings = MapUiSettings(
                    compassEnabled = true,
                    mapToolbarEnabled = true,
                    myLocationButtonEnabled = true
                )
            ) {  }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(30.dp)
        ) {
            SnackBar(
                snackbarHostState = snackbarHostState,
                icon = R.drawable.ic_xmark,
                iconTint = moduErrorPoint
            )
        }
    }
}