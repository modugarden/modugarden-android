package com.example.modugarden.main.upload.post

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.api.UploadPostTagLocationSearchAPI
import com.example.modugarden.api.retrofitBuilder
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
    data: UploadPost
) {
    val title = remember { mutableStateOf("대한민국") }
    val formattedAddress = remember { mutableStateOf("") }
    val loc = remember { mutableStateOf(LatLng(37.4631589802915, 126.8930169802915)) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(loc.value, 17f)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val textFieldSearchData = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    ModalBottomSheet(
        title = "위치 태그 추가",
        bottomSheetState = bottomSheetState,
        sheetScreen = {
            Column(
            ) {
                Text(title.value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack)
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
                       Column {
                           Box(
                               modifier = Modifier.background(Color.White)
                           ) {
                               Row(
                                   modifier = Modifier.fillMaxWidth().padding(18.dp)
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
                                       shape = RoundedCornerShape(10.dp)
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
                                                   retrofitBuilder.api
                                                       .getUploadPostTagLocationSearchAPI(
                                                           textFieldSearchData.value
                                                       )
                                                       .enqueue(object : Callback<MapsGeocoding> {
                                                           override fun onResponse(
                                                               call: Call<MapsGeocoding>,
                                                               response: Response<MapsGeocoding>
                                                           ) {
                                                               val result = response.body()?.results
                                                               Log.d("response", result.toString())

                                                               if (response.isSuccessful && result != null && result.isNotEmpty()) {
                                                                   Log.d(
                                                                       "response",
                                                                       result.toString()
                                                                   )
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
                                                               Log.e(
                                                                   "resoponse",
                                                                   t.message.toString()
                                                               )
                                                           }

                                                       })
                                               }
                                           }
                                   )
                               }
                           }
                           GoogleMap(
                               modifier = Modifier
                                   .fillMaxSize()
                                   .addFocusCleaner(focusManager),
                               cameraPositionState = cameraPositionState,
                               onMapClick = {
                                   loc.value = it
                                   cameraPositionState.position = CameraPosition.fromLatLngZoom(loc.value, 17f)
                                   retrofitBuilder.api
                                       .getUploadPostTagLocationSearchAPI("${it.latitude}, ${it.longitude}")
                                       .enqueue(object : Callback<MapsGeocoding> {
                                           override fun onResponse(
                                               call: Call<MapsGeocoding>,
                                               response: Response<MapsGeocoding>
                                           ) {
                                               val result = response.body()?.results
                                               Log.d("response", result.toString())

                                               if (response.isSuccessful && result != null && result.isNotEmpty()) {
                                                   Log.d("response", result.toString())
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
                       }
                       Box(Modifier.align(Alignment.BottomCenter)) {
                           BottomButton(title = "위치 추가", onClick = {
                               scope.launch {
                                   keyboardController?.hide()
                                   bottomSheetState.show()
                               }
                           })
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