package com.example.modugarden.main.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.net.toUri
import com.example.modugarden.R
import com.example.modugarden.api.AuthCallBack
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.UpdateUserSettingInfoRes
import com.example.modugarden.api.dto.UserSettingInfoRes
import com.example.modugarden.main.upload.curation.UriUtil
import com.example.modugarden.ui.theme.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.skydoves.landscapist.glide.GlideImage
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
@Preview(showBackground = true)
@Composable
fun SettingsProfileScreen(
    // 냅호스트에서 수정완료 버튼 온클릭 받아옴
    onButtonClicked: () -> Unit = {}
) {
    val context = LocalContext.current

    // 프로필 이미지
    val imageState = remember { mutableStateOf<Uri?>(null) }

    // 사진 가져오는 런쳐
    val takePhotoFromAlbumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let {
                    imageState.value = it
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

    val focusManager = LocalFocusManager.current

    // 바텀모달시트
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    // 각 정보들 저장하는거임 생일이랑 이메일은 리멤버 안해도 되긴 하는데 귀찮아서 안바꿈
    val nicknameState = remember { mutableStateOf("") }
    val birthState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val nicknameFocusState = remember { mutableStateOf(false) }
    val birthFocusState = remember { mutableStateOf(false) }
    val emailFocusState = remember { mutableStateOf(false) }
    val categories = remember { mutableStateOf(listOf("")) }
    val modalType = remember { mutableStateOf(ModalType.ProfileImage) }

    // 유저 설정 정보 불러오는 API
    RetrofitBuilder.userAPI.readUserSettingInfo()
        .enqueue(object : AuthCallBack<UserSettingInfoRes>(context, "유저 정보 불러오기 성공!"){
            override fun onResponse(
                call: Call<UserSettingInfoRes>,
                response: Response<UserSettingInfoRes>
            ) {
                super.onResponse(call, response)

                // 생일 형식 변환
                val myBirth = response.body()?.result?.birth!!
                val yyyy = myBirth.substring(0,4)
                val mm = myBirth.substring(4,6)
                val dd = myBirth.substring(6,8)
                birthState.value = "${yyyy}년 ${mm}월 ${dd}일"

                // 이메일이랑 닉네임 받아오기
                emailState.value = response.body()?.result?.email!!
                nicknameState.value = response.body()?.result?.nickname!!
                categories.value = response.body()?.result?.categories!!

                // 일단 기본이미지는 드로어블에 ic_dafault_profile 있어
                // 그리고 이미지스테이트는 기본이미지면 널로 저장되는데 서버에서 널을 받을 수 있게 해야할듯..?
                if(response.body()?.result?.profileImage != null)
                    imageState.value = response.body()?.result?.profileImage!!.toUri()
            }
        })

    ModalBottomSheet(
        title =
        if(modalType.value == ModalType.Categories)
            "카테고리 설정(최대 2개)"
        else
            "프로필 이미지 설정",
        bottomSheetState = bottomSheetState,
        sheetScreen =
        {
            if(modalType.value == ModalType.Categories) {
                ModalBottomSheetCategoryItem(R.drawable.ic_potted_plant,"식물 가꾸기", categories)
                ModalBottomSheetCategoryItem(R.drawable.ic_house_with_garden,"플랜테리어", categories)
                ModalBottomSheetCategoryItem(R.drawable.ic_tent,"여행/나들이", categories)
            } else {
                ModalBottomSheetItem(text = "라이브러리에서 선택", icon = R.drawable.ic_upload_image_mountain, trailing = true, modifier = Modifier.bounceClick {
                    scope.launch {
                        takePhotoFromAlbumLauncher.launch(takePhotoFromAlbumIntent)
                        bottomSheetState.hide()
                    }
                })
                ModalBottomSheetItem(text = "기본 이미지로 변경", icon= R.drawable.ic_default_profile, trailing = true, modifier = Modifier.bounceClick {
                    scope.launch {
                        imageState.value = null
                        bottomSheetState.hide()
                    }
                })
            }
        },
        uiScreen = {
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
                    // 만약에 이미지값이 널이면 기본프로필을 드로어블에서 불러옴
                    // 아니면 그냥 넣음
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
                        contentScale = ContentScale.Crop
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
                EditText(
                    title = "닉네임",
                    data = nicknameState,
                    isTextFieldFocused = nicknameFocusState
                )
                Spacer(modifier = Modifier.height(18.dp))
                DisabledEditText(
                    title = "생년월일",
                    data = birthState,
                    isTextFieldFocused = birthFocusState
                )
                Spacer(modifier = Modifier.height(18.dp))
                DisabledEditText(
                    title = "이메일",
                    data = emailState,
                    isTextFieldFocused = emailFocusState
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "카테고리",
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
                    // 카테고리 리스트가 들어갈 레이지로우
                    LazyRow(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        items(
                            items = categories.value,
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
                Spacer(modifier = Modifier.weight(1f))
                BottomButton(
                    title = "수정 완료",
                    onClick = {
                        // 정보 수정 API
                        // 여기에 이미지 변환해서 올릴 수 있게 해주면 됩니다

                        val file = imageState.value?.let { UriUtil.toFile(context, it) }

                        //api에 사진 데이터는 이 requestFile 넣어주면 되고
                        val requestFile = file?.let {
                            MultipartBody.Part.createFormData(
                                name = "file",
                                filename = file.name,
                                body = it.asRequestBody("image/*".toMediaType())
                            )
                        }
                        val jsonArray = JsonArray()
                        for(category in categories.value) {
                            jsonArray.add(category)
                        }

                        val jsonData = JsonObject().apply {
                            add("categories", jsonArray)
                            addProperty("nickname", nicknameState.value)
                        }

                        val mediaType = "application/json; charset=utf-8".toMediaType()
                        val jsonBody = jsonData.toString().toRequestBody(mediaType)

                        Log.d("RequestFile", requestFile.toString())

                        RetrofitBuilder.userAPI.updateUserProfile(requestFile, jsonBody)
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
                        onButtonClicked()
                    },
                    dpScale = 0.dp
                )
            }
        }
    )
}

@Composable
fun ModalBottomSheetCategoryItem(
    icon: Int,
    text: String,
    categories: MutableState<List<String>>,
    modifier: Modifier = Modifier
) {
    val checkState = remember { mutableStateOf(categories.value.contains(text)) }
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
                            if (categories.value.contains(text)) {
                                categories.value = categories.value.minus(text)
                                checkState.value = !checkState.value }
                            else if (categories.value.size < 2) {
                                categories.value = categories.value.plus(text)
                                checkState.value = !checkState.value }
                        }
                )
            }
        }
    }
}