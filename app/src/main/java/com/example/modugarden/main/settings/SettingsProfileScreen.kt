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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.modugarden.R
import com.example.modugarden.api.AuthCallBack
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.UpdateUserSettingInfoRes
import com.example.modugarden.api.dto.UserSettingInfoRes
import com.example.modugarden.main.upload.curation.UriUtil
import com.example.modugarden.ui.theme.*
import com.google.gson.JsonObject
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response

val defaultImage = "https://blog.kakaocdn.net/dn/dTQvL4/btrusOKyP2u/TZBNHQSAHpJU5k8vmYVSvK/img.png".toUri()

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun SettingsProfileScreen(
    onButtonClicked: () -> Unit = {}
) {
    val context = LocalContext.current

    val imageState = remember { mutableStateOf(defaultImage) }
    val takePhotoFromAlbumLauncher = // 갤러리에서 사진 가져오기
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
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val nicknameState = remember { mutableStateOf("") }
    val birthState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val nicknameFocusState = remember { mutableStateOf(false) }
    val birthFocusState = remember { mutableStateOf(false) }
    val emailFocusState = remember { mutableStateOf(false) }
    val categories = remember { mutableStateOf(listOf("")) }

    RetrofitBuilder.userAPI.readUserSettingInfo()
        .enqueue(object : AuthCallBack<UserSettingInfoRes>(context, "유저 정보 불러오기 성공!"){
            override fun onResponse(
                call: Call<UserSettingInfoRes>,
                response: Response<UserSettingInfoRes>
            ) {
                super.onResponse(call, response)
                categories.value = response.body()?.result?.categories!!

                val myBirth = response.body()?.result?.birth!!
                val yyyy = myBirth.substring(0,4)
                val mm = myBirth.substring(4,6)
                val dd = myBirth.substring(6,8)
                birthState.value = "${yyyy}년 ${mm}월 ${dd}일"
                emailState.value = response.body()?.result?.email!!
                nicknameState.value = response.body()?.result?.nickname!!

                if(response.body()?.result?.profileImage != null)
                    imageState.value = response.body()?.result?.profileImage!!.toUri()
            }
        })

    ModalBottomSheet(
        title = "프로필 이미지 설정",
        bottomSheetState = bottomSheetState,
        sheetScreen = {
            ModalBottomSheetItem(text = "현재 사진 삭제", icon = R.drawable.plus,trailing = true, modifier = Modifier.bounceClick {
                scope.launch {
                    takePhotoFromAlbumLauncher.launch(takePhotoFromAlbumIntent)
                    bottomSheetState.hide()
                }
            })
            ModalBottomSheetItem(text = "라이브러리에서 선택", icon=R.drawable.plus, trailing = true, modifier = Modifier.bounceClick {
                scope.launch {
                    imageState.value = defaultImage
                    bottomSheetState.hide()
                }
            })
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
                        focusManager.clearFocus()
                        scope.launch {
                            bottomSheetState.show()
                        }
                    }
                ) {
                    GlideImage(
                        imageModel = imageState.value,
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
                        items(categories.value) { category ->
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
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        backgroundColor = moduBackground,
                        onClick = {},
                        elevation = 0.dp
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = null,
                            modifier = Modifier
                                .clickable(
                                    enabled = true
                                ) {
                                    // 카테고리 추가
                                }
                                .wrapContentSize()
                                .background(color = Color.Transparent)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                BottomButton(
                    title = "수정 완료",
                    onClick = {
                        // 정보 수정

                        val file = UriUtil.toFile(context, imageState.value)
                        val requestFile = MultipartBody.Part.createFormData(
                            name = "file",
                            filename = file.name,
                            body = file.asRequestBody("image/*".toMediaType())
                        )

                        val jsonData = JsonObject().addProperty("nickname", nicknameState.value)
                        val mediaType = "application/json; charset=utf-8".toMediaType()
                        val jsonBody = jsonData.toString().toRequestBody(mediaType)

                        RetrofitBuilder.userAPI.updateUserProfile(jsonBody, requestFile)
                            .enqueue(AuthCallBack<UpdateUserSettingInfoRes>(context, "유저 정보 변경"))
                        onButtonClicked()
                    },
                    dpScale = 0.dp
                )
            }
        }
    )
}