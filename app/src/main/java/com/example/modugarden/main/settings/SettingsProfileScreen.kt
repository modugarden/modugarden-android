package com.example.modugarden.main.settings

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.core.net.toUri
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.ApplicationClass.Companion.categorySetting
import com.example.modugarden.ApplicationClass.Companion.clientNickname
import com.example.modugarden.ApplicationClass.Companion.profileImage
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.UpdateUserSettingInfoRes
import com.example.modugarden.main.upload.curation.UriUtil
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.SettingViewModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class ModalType() {
    ProfileImage,
    Categories
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsProfileScreen(
    settingViewModel: SettingViewModel,
    onButtonClicked: () -> Unit = {}
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // ??????????????????
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    // ??? ????????? ?????????????????? ???????????? ???????????? ????????? ????????? ?????? ????????? ???????????? ?????????
    val nicknameState = remember { mutableStateOf(sharedPreferences.getString(clientNickname, "")) }
    val birthState = remember { mutableStateOf(settingViewModel.getBirth()) }
    val emailState = remember { mutableStateOf(settingViewModel.getEmail()) }
    val nicknameFocusState = remember { mutableStateOf(false) }
    val nicknameErrorState = remember { mutableStateOf(false) }
    val categoryErrorState = remember { mutableStateOf(false) }
    val modalType = remember { mutableStateOf(ModalType.ProfileImage) }
    val categoriesState = remember { mutableStateOf(sharedPreferences.getStringSet(categorySetting, setOf())!!.toList()) }
    val imageState = remember { mutableStateOf (sharedPreferences.getString(profileImage, null)) }
    var isImageChanged = false

    // ?????? ???????????? ??????
    val takePhotoFromAlbumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let {
                    imageState.value = it.toString()
                }
            } else if (result.resultCode != Activity.RESULT_CANCELED) {
                Log.d("Image Upload", "fail")
            }
        }
    val takePhotoFromAlbumIntent =
        Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf("image/jpeg", "image/png", "image/bmp", "image/webp")
            )
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        }

    ModalBottomSheet(
        title =
        if(modalType.value == ModalType.Categories)
            "???????????? ??????(?????? 2???)"
        else
            "????????? ????????? ??????",
        bottomSheetState = bottomSheetState,
        sheetScreen =
        {
            if(modalType.value == ModalType.Categories) {
                ModalBottomSheetCategoryItem(R.drawable.ic_potted_plant,"?????? ?????????", categoriesState, categoryErrorState)
                ModalBottomSheetCategoryItem(R.drawable.ic_house_with_garden,"???????????????", categoriesState, categoryErrorState)
                ModalBottomSheetCategoryItem(R.drawable.ic_tent,"??????/?????????", categoriesState, categoryErrorState)
            } else {
                ModalBottomSheetItem(text = "????????????????????? ??????", icon = R.drawable.ic_upload_image_mountain, trailing = true, modifier = Modifier.bounceClick {
                    scope.launch {
                        takePhotoFromAlbumLauncher.launch(takePhotoFromAlbumIntent)
                        isImageChanged = true
                        bottomSheetState.hide()
                    }
                })
                ModalBottomSheetItem(text = "?????? ???????????? ??????", icon= R.drawable.ic_default_profile, trailing = true, modifier = Modifier.bounceClick {
                    scope.launch {
                        imageState.value = null
                        isImageChanged = true
                        bottomSheetState.hide()
                    }
                })
            }
            },
        uiScreen = {
            BackHandler(enabled = bottomSheetState.isVisible) {
                scope.launch {
                    bottomSheetState.hide()
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(18.dp)
                    .addFocusCleaner(focusManager)
            ) {
                Box(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(100.dp)
                    .bounceClick {
                        modalType.value = ModalType.ProfileImage
                        focusManager.clearFocus()
                        scope.launch {
                            bottomSheetState.show()
                        }
                    }
                ) {
                    // ????????? ??????????????? ????????? ?????????????????? ?????????????????? ?????????
                    // ????????? ?????? ??????
                    GlideImage(
                        imageModel =
                        if(imageState.value == null)
                            R.drawable.ic_default_profile
                        else
                            imageState.value,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        loading = {
                            ShowProgressBar()
                        },
                        // shows an error text if fail to load an image.
                        requestOptions = {
                            RequestOptions()
                                .override(256,256)
                        }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_plus_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(36.dp))
                NicknameEditText(
                    title = "?????????",
                    data = nicknameState,
                    isTextFieldFocused = nicknameFocusState,
                    errorState = nicknameErrorState
                )
                Spacer(modifier = Modifier.height(18.dp))
                DisabledEditText(
                    title = "????????????",
                    data = birthState
                )
                Spacer(modifier = Modifier.height(18.dp))
                DisabledEditText(
                    title = "?????????",
                    data = emailState
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "????????????",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = moduGray_strong
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    // ???????????? ???????????? ????????? ???????????????
                    LazyRow(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        items(
                            items = categoriesState.value,
                            key = { it }
                        ) { category ->
                            Card(
                                Modifier
                                    .wrapContentSize()
                                    .fillMaxHeight()
                                    .padding(end = 10.dp),
                                backgroundColor = moduBackground,
                                shape = RoundedCornerShape(10.dp),
                                elevation = 0.dp
                            ) {
                                Text(
                                    text = category,
                                    style = TextStyle(
                                        color = moduBlack,
                                        fontSize = 12.sp
                                    ),
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Card(
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .bounceClick {
                                modalType.value = ModalType.Categories
                                focusManager.clearFocus()
                                scope.launch {
                                    bottomSheetState.show()
                                }
                            },
                        backgroundColor = moduBackground,
                        elevation = 0.dp
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = null,
                            modifier = Modifier
                                .wrapContentSize()
                                .background(color = Color.Transparent)
                                .clip(RoundedCornerShape(15.dp))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "1??? ?????? 2?????? ??????????????? ????????? ??? ?????????.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color =
                    if (categoryErrorState.value) moduErrorPoint
                    else moduGray_strong
                )
                Spacer(modifier = Modifier.weight(1f))
                ProfileUpdateBottomButton(
                    title = "?????? ??????",
                    onClick = {
                        // ?????? ?????? API
                        // ????????? ????????? ???????????? ?????? ??? ?????? ????????? ?????????

                        val jsonArray = JsonArray()
                        for(category in categoriesState.value) {
                            jsonArray.add(category)
                        }

                        val jsonData = JsonObject().apply {
                            add("categories", jsonArray)
                            addProperty("nickname", nicknameState.value)
                        }

                        val mediaType = "application/json; charset=utf-8".toMediaType()
                        val jsonBody = jsonData.toString().toRequestBody(mediaType)

                        if(isImageChanged) {
                            val file = imageState.value?.let { UriUtil.toFile(context, it.toUri()) }

                            //api??? ?????? ???????????? ??? requestFile ???????????? ??????
                            val requestFile = file?.let {
                                MultipartBody.Part.createFormData(
                                    name = "file",
                                    filename = file.name,
                                    body = it.asRequestBody("image/*".toMediaType())
                                )
                            }
                            RetrofitBuilder.userAPI.updateUserInfo(requestFile, jsonBody)
                                .enqueue(object : Callback<UpdateUserSettingInfoRes>{
                                    override fun onResponse(
                                        call: Call<UpdateUserSettingInfoRes>,
                                        response: Response<UpdateUserSettingInfoRes>
                                    ) {
                                        Log.d("onResponse",
                                            response.toString() + "\n" +
                                                    response.body().toString() + "\n" +
                                                    response.errorBody().toString())
                                    }

                                    override fun onFailure(
                                        call: Call<UpdateUserSettingInfoRes>,
                                        t: Throwable
                                    ) {
                                        TODO("Not yet implemented")
                                    }

                                })
                        }
                        else {
                            CoroutineScope(Dispatchers.IO).launch {
                                RetrofitBuilder.userAPI.updateUserProfile(jsonBody).execute()
                            }
                        }
                        settingViewModel.setSettingInfo(
                            birthState.value,
                            emailState.value,
                            categoriesState.value
                        )
                        sharedPreferences.edit()
                            .putStringSet(categorySetting, categoriesState.value.toSet())
                            .putString(profileImage, imageState.value)
                            .putString(clientNickname, nicknameState.value)
                            .apply()
                        onButtonClicked()
                    },
                    dpScale = 0.dp,
                    nicknameDisabled = nicknameErrorState,
                    categoryDisabled = categoryErrorState
                )
            }
        }
    )
}

@Composable
fun ModalBottomSheetCategoryItem(
    icon: Int,
    text: String,
    categoriesState: MutableState<List<String>>,
    errorState: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val checkState = remember { mutableStateOf(categoriesState.value.contains(text)) }
    Card(
        modifier = modifier,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
        ) {
            if(icon != 0) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text, fontSize = 16.sp, color = moduBlack, modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id =
                    if(checkState.value)
                        R.drawable.ic_check_solid
                    else
                        R.drawable.ic_check_line),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                        .bounceClick {
                            if (categoriesState.value.contains(text) && categoriesState.value.isNotEmpty()) {
                                categoriesState.value = categoriesState.value.minus(text)
                                checkState.value = !checkState.value
                                if (categoriesState.value.isEmpty())
                                    errorState.value = true
                            } else if (categoriesState.value.size < 2) {
                                categoriesState.value = categoriesState.value.plus(text)
                                checkState.value = !checkState.value
                                errorState.value = false
                            }
                        }
                )
            }
        }
    }
}