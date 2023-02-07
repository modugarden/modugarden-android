package com.example.modugarden.main.upload.post

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.modugarden.api.UploadPostTagLocationSearchAPIRetrofitBuilder
import com.example.modugarden.data.MapsGeocoding
import com.example.modugarden.data.UploadPost
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UploadPostViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun UploadPostTagLocationScreen(
    navController: NavHostController,
    uploadPostViewModel: UploadPostViewModel,
    data: UploadPost,
    page: Int
) {
    val title = remember { mutableStateOf(if(data.location[page].isNotEmpty()) data.location[page].split(",")[0] else "대한민국") }
    val formattedAddress = remember { mutableStateOf("") }
    val loc = remember { mutableStateOf(if(data.location[page].isNotEmpty()) LatLng(data.location[page].split(",")[1].toDouble(), data.location[page].split(",")[2].toDouble()) else LatLng(37.4631589802915, 126.8930169802915)) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(loc.value, 17f)
    }

    val keyboard by keyboardAsState()
    val dpScale = animateDpAsState(if(keyboard.toString() == "Closed") 18.dp else 0.dp)
    val dpScale2 = animateDpAsState(if(keyboard.toString() == "Closed") 18.dp else 0.dp)
    val shapeScale = animateDpAsState(if(keyboard.toString() == "Closed") 10.dp else 0.dp)

    val errorListener = remember { mutableStateOf(false) }
    val errorText = remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val textFieldSearchData = remember { mutableStateOf("") }
    val textFieldNameData = remember { mutableStateOf(if(data.location[page].isNotEmpty()) data.location[page].split(",")[0] else "") }
    val nameFocused = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ModalBottomSheet(
        title = "원하는 위치인가요?",
        bottomSheetState = bottomSheetState,
        sheetScreen = {
            Column(
            ) {
                Text(textFieldNameData.value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack)
                Spacer(Modifier.size(5.dp))
                Text(formattedAddress.value, fontSize = 14.sp, color = moduGray_strong)
                Spacer(Modifier.size(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .bounceClick {
                                scope.launch {
                                    bottomSheetState.hide()
                                }
                            },
                        backgroundColor = moduBackground,
                        shape = RoundedCornerShape(15.dp),
                        elevation = 0.dp
                    ) {
                        Text("다른 장소예요", Modifier.padding(vertical = 18.dp), color = moduGray_strong, fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center)
                    }
                    Spacer(Modifier.size(18.dp))
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .bounceClick {
                                uploadPostViewModel.addLocation(
                                    "${textFieldNameData.value.replace("\n", "")}, ${loc.value.latitude}, ${loc.value.longitude}",
                                    page
                                )
                                navController.popBackStack()
                            },
                        backgroundColor = moduPoint,
                        shape = RoundedCornerShape(15.dp),
                        elevation = 0.dp
                    ) {
                        Text("저장", Modifier.padding(vertical = 18.dp), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        },
        uiScreen = {
                   Box() {
                       GoogleMap(
                           modifier = Modifier
                               .fillMaxSize()
                               .addFocusCleaner(focusManager),
                           cameraPositionState = cameraPositionState,
                           onMapClick = {
                               loc.value = it
                               cameraPositionState.position = CameraPosition.fromLatLngZoom(loc.value, 17f)
                               UploadPostTagLocationSearchAPIRetrofitBuilder.api
                                   .getUploadPostTagLocationSearchAPI("${it.latitude}, ${it.longitude}")
                                   .enqueue(object : Callback<MapsGeocoding> {
                                       override fun onResponse(
                                           call: Call<MapsGeocoding>,
                                           response: Response<MapsGeocoding>
                                       ) {
                                           val result = response.body()?.results

                                           if (response.isSuccessful && result != null && result.isNotEmpty()) {
                                               loc.value = LatLng(
                                                   result[0].geometry.location.lat.toDouble(),
                                                   result[0].geometry.location.lng.toDouble()
                                               )
                                               title.value =
                                                   result[0].address_components[0].long_name
                                               formattedAddress.value =
                                                   result[0].formatted_address
                                               cameraPositionState.position =
                                                   CameraPosition.fromLatLngZoom(
                                                       loc.value,
                                                       17f
                                                   )
                                           }
                                           else if (response.isSuccessful && result != null && result.isEmpty()) {
                                               scope.launch {
                                                   snackbarHostState.showSnackbar("위치를 찾을 수 없어요", duration = SnackbarDuration.Short)
                                               }
                                           }

                                       }

                                       override fun onFailure(
                                           call: Call<MapsGeocoding>,
                                           t: Throwable
                                       ) {
                                           Log.e("resoponse", t.message.toString())
                                       }

                                   })
                           },
                           uiSettings = MapUiSettings(
                               compassEnabled = true,
                               mapToolbarEnabled = true,
                               myLocationButtonEnabled = true
                           )
                       ) {
                           Marker(
                               state = MarkerState(position = loc.value),
                               title = title.value,
                               snippet = formattedAddress.value
                           )
                       }
                       Card(
                           modifier = Modifier,
                           elevation = 10.dp,
                           shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
                       ) {
                           Row(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .padding(horizontal = 18.dp, vertical = 10.dp)
                           ) {
                               Image(
                                   painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                                   contentDescription = null,
                                   modifier = Modifier
                                       .size(20.dp)
                                       .align(Alignment.CenterVertically)
                                       .bounceClick {
                                           navController.popBackStack()
                                       }
                               )
                               Spacer(Modifier.size(18.dp))
                               TextField(
                                   modifier = Modifier
                                       .align(Alignment.CenterVertically)
                                       .weight(1f),
                                   value = textFieldSearchData.value,
                                   onValueChange = { textValue ->
                                       textFieldSearchData.value = textValue
                                   },
                                   colors = TextFieldDefaults.textFieldColors(
                                       backgroundColor = moduBackground,
                                       focusedIndicatorColor = Color.Transparent,
                                       unfocusedIndicatorColor = Color.Transparent
                                   ),
                                   shape = RoundedCornerShape(10.dp),
                                   textStyle = TextStyle(fontSize = 16.sp, color = moduBlack),
                                   placeholder = { Text("위치를 검색하세요", fontSize = 16.sp, color = moduGray_normal) }
                               )
                               Spacer(Modifier.size(18.dp))
                               Image(
                                   painter = painterResource(id = R.drawable.ic_search),
                                   contentDescription = null,
                                   colorFilter = ColorFilter.tint(moduGray_strong),
                                   modifier = Modifier
                                       .size(20.dp)
                                       .align(Alignment.CenterVertically)
                                       .bounceClick {
                                           if (textFieldSearchData.value != "") {
                                               keyboardController?.hide()
                                               UploadPostTagLocationSearchAPIRetrofitBuilder.api
                                                   .getUploadPostTagLocationSearchAPI(
                                                       textFieldSearchData.value
                                                   )
                                                   .enqueue(object : Callback<MapsGeocoding> {
                                                       override fun onResponse(
                                                           call: Call<MapsGeocoding>,
                                                           response: Response<MapsGeocoding>
                                                       ) {
                                                           val result = response.body()?.results

                                                           if (response.isSuccessful && result != null && result.isNotEmpty()) {
                                                               loc.value = LatLng(
                                                                   result[0].geometry.location.lat.toDouble(),
                                                                   result[0].geometry.location.lng.toDouble()
                                                               )
                                                               title.value =
                                                                   result[0].address_components[0].long_name
                                                               formattedAddress.value =
                                                                   result[0].formatted_address
                                                               cameraPositionState.position =
                                                                   CameraPosition.fromLatLngZoom(
                                                                       loc.value,
                                                                       17f
                                                                   )
                                                           } else if (response.isSuccessful && result != null && result.isEmpty()) {
                                                               scope.launch {
                                                                   snackbarHostState.showSnackbar(
                                                                       "위치를 찾을 수 없어요",
                                                                       duration = SnackbarDuration.Short
                                                                   )
                                                               }
                                                           }
                                                       }

                                                       override fun onFailure(
                                                           call: Call<MapsGeocoding>,
                                                           t: Throwable
                                                       ) {
                                                           
                                                       }

                                                   })
                                           }
                                       }
                               )
                           }
                       }
                       Box(Modifier.align(Alignment.BottomCenter)) {

                           Card(
                               modifier = Modifier
                                   ,
                               shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
                               backgroundColor = Color.White,
                           ) {
                               Column {
                                   Box(
                                       modifier = Modifier
                                           .padding(18.dp)
                                           .padding(top = 18.dp)
                                   ) {
                                       EditText(
                                           "위치 이름",
                                           textFieldNameData,
                                           nameFocused,
                                           errorListener = errorListener,
                                           description = errorText.value
                                       )
                                   }
                                   Row() {
                                       if(data.location[page].isNotEmpty()) {
                                           Card(
                                               modifier = Modifier
                                                   .bounceClick {
                                                       scope.launch {
                                                           keyboardController?.hide()
                                                           uploadPostViewModel.removeOnlyLocation(page)
                                                           textFieldNameData.value = ""
                                                       }
                                                   }
                                                   .padding(start = dpScale2.value, top = dpScale.value, bottom = dpScale.value)
                                                   .weight(1f)
                                                   .animateContentSize(),
                                               shape = RoundedCornerShape(shapeScale.value),
                                               backgroundColor = moduBackground,
                                               elevation = 0.dp
                                           ) {
                                               Text(
                                                   "삭제",
                                                   fontWeight = FontWeight.Bold,
                                                   fontSize = 16.sp,
                                                   color = moduGray_strong,
                                                   modifier = Modifier
                                                       .padding(18.dp),
                                                   textAlign = TextAlign.Center
                                               )
                                           }
                                       }
                                       Card(
                                           modifier = Modifier
                                               .bounceClick {
                                                   if(textFieldNameData.value.trim().replace("\n", "") != "") {
                                                       scope.launch {
                                                           keyboardController?.hide()
                                                           bottomSheetState.show()
                                                       }
                                                   }
                                                   else {
                                                       errorListener.value = true
                                                       errorText.value = "위치 이름을 입력해야 해요"
                                                   }
                                               }
                                               .padding(end = dpScale.value, top = dpScale.value, bottom = dpScale.value, start = dpScale2.value)
                                               .weight(1f)
                                               .alpha(if(textFieldNameData.value.trim().replace("\n", "") != "") 1f else 0.4f)
                                               .animateContentSize(),
                                           shape = RoundedCornerShape(shapeScale.value),
                                           backgroundColor = moduPoint,
                                           elevation = 0.dp
                                       ) {
                                           Text(
                                               if(data.location[page].isNotEmpty()) "위치 수정" else "위치 추가",
                                               fontWeight = FontWeight.Bold,
                                               fontSize = 16.sp,
                                               color = Color.White,
                                               modifier = Modifier
                                                   .padding(18.dp),
                                               textAlign = TextAlign.Center
                                           )
                                       }
                                   }
                               }
                           }
                       }
                       Box(
                           modifier = Modifier
                               .align(Alignment.BottomCenter)
                               .padding(30.dp)
                       ) {
                           SnackBar(snackbarHostState = snackbarHostState, icon = R.drawable.ic_xmark, iconTint = moduErrorPoint)
                       }
                   }

        },
    )
}