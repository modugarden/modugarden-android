package com.example.modugarden.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modugarden.ApplicationClass
import com.example.modugarden.BuildConfig.GOOGLE_WEB_KEY
import com.example.modugarden.MainActivity
import com.example.modugarden.signup.SignupActivity
import com.example.modugarden.ui.theme.*
import com.example.modugarden.R
import com.example.modugarden.api.dto.LoginDTO
import com.example.modugarden.api.RetrofitBuilder.loginAPI
import com.example.modugarden.api.RetrofitBuilder.signupAPI
import com.example.modugarden.api.dto.SignupEmailIsDuplicatedDTO
import com.example.modugarden.ApplicationClass.Companion.accessToken
import com.example.modugarden.ApplicationClass.Companion.autoLoginId
import com.example.modugarden.ApplicationClass.Companion.autoLoginOption
import com.example.modugarden.ApplicationClass.Companion.autoLoginPw
import com.example.modugarden.ApplicationClass.Companion.autoLoginSetting
import com.example.modugarden.ApplicationClass.Companion.clientId
import com.example.modugarden.ApplicationClass.Companion.clientNickname
import com.example.modugarden.ApplicationClass.Companion.fcmToken
import com.example.modugarden.ApplicationClass.Companion.googleLogin
import com.example.modugarden.ApplicationClass.Companion.normalLogin
import com.example.modugarden.ApplicationClass.Companion.profileImage
import com.example.modugarden.ApplicationClass.Companion.refreshToken
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.example.modugarden.api.AuthCallBack
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.RetrofitBuilder.fcmCheckAPI
import com.example.modugarden.api.RetrofitBuilder.fcmSaveAPI
import com.example.modugarden.api.dto.FcmCheckDTO
import com.example.modugarden.api.dto.FcmSaveDTO
import com.example.modugarden.data.RecentSearch
import com.example.modugarden.route.NAV_ROUTE_DISCOVER_SEARCH
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MainLoginScreen(navController: NavController) {
    val textFieldId = remember { mutableStateOf("") }
    val isTextFieldFocusedId = remember { mutableStateOf(false) }
    val textFieldPw = remember { mutableStateOf("") }
    val isTextFieldFocusedPw = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val mContext = LocalContext.current
    val editor = sharedPreferences.edit()
    val context = LocalContext.current
    val token = GOOGLE_WEB_KEY

    var user by remember { mutableStateOf(Firebase.auth.currentUser) }

    val waitAutoLogin = remember {mutableStateOf(true)}

    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user = result.user
            Toast.makeText(mContext, "${user}", Toast.LENGTH_SHORT).show()
            //만약 로그인 한 이메일이 등록되지 않은 이메일이면 회원 가입 창으로 전환.
            //이메일 중복 체크 API 불러오기.
            //if(user?.email.state == 중복) //로그인 한 이메일이 동록된 이메일임.
            val jsonData = JsonObject().apply {
                addProperty("email", user?.email)
            }
            signupAPI.getSignupEmailIsDuplicatedAPI(jsonData).enqueue(object: Callback<SignupEmailIsDuplicatedDTO> {
                override fun onResponse(
                    call: Call<SignupEmailIsDuplicatedDTO>,
                    response: Response<SignupEmailIsDuplicatedDTO>
                ) {
                    if(response.isSuccessful) {
                        val res = response.body()
                        if(res != null) {
                            if(res.isSuccess) {
                                if(!(res.result.duplicate)) {
                                    val intent = Intent(mContext, SignupActivity::class.java)
                                    intent.putExtra("social", true)
                                    intent.putExtra("social_email", user?.email)
                                    intent.putExtra("social_name", user?.tenantId)
                                    mContext.startActivity(intent)
                                }
                                else {
                                    val jsonData = JsonObject().apply {
                                        addProperty("email", user?.email)
                                    }
                                    val fcmToken = sharedPreferences.getString("fcmToken", "")
                                    Log.d("apires", "fcmToken :: $fcmToken")
                                    val jsonDataFcmToken = JsonObject()
                                    jsonDataFcmToken.apply {
                                        addProperty("fcmToken", fcmToken)
                                    }
                                    loginAPI.loginSocialAPI(jsonData).enqueue(object: Callback<LoginDTO> {
                                        override fun onResponse(
                                            call: Call<LoginDTO>,
                                            response: Response<LoginDTO>
                                        ) {
                                            if(response.isSuccessful) {
                                                val res1 = response.body()
                                                if(res1 != null) {
                                                    if(res1.isSuccess) {
                                                        mContext.startActivity(
                                                            Intent(mContext, MainActivity::class.java)
                                                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                        )
                                                        Log.e("apires", res1.result.accessToken)
                                                        editor.putString(accessToken, res1.result.accessToken)
                                                        editor.putString(refreshToken, res1.result.refreshToken)
                                                        editor.putInt(clientId, res1.result.userId)
                                                        editor.putString(profileImage,res1.result.profileImage)
                                                        editor.putString(clientNickname, res1.result.nickname)
                                                        editor.apply()
                                                        fcmCheckAPI.fcmCheckAPI().enqueue(object: Callback<FcmCheckDTO> {
                                                            override fun onResponse(
                                                                call: Call<FcmCheckDTO>,
                                                                response: Response<FcmCheckDTO>
                                                            ) {
                                                                if(response.isSuccessful) {
                                                                    val res = response.body()
                                                                    if(res != null) {
                                                                        if(res.isSuccess) {
                                                                            if(fcmToken !in res.result.fcmTokens) {
                                                                                Log.d("apires", "새로운 토큰을 저장했어요")
                                                                                fcmSaveAPI.fcmSaveAPI(jsonDataFcmToken).enqueue(object: Callback<FcmSaveDTO> {
                                                                                    override fun onResponse(call: Call<FcmSaveDTO>, response: Response<FcmSaveDTO>) {
                                                                                        if(response.isSuccessful) {
                                                                                            val res = response.body()
                                                                                            if(res != null) {
                                                                                                if(res.isSuccess) {
                                                                                                    Log.d("apires", "토큰을 정상적으로 서버에 저장했어요")
                                                                                                }
                                                                                                else {
                                                                                                    Log.e("apires", res.message)
                                                                                                }
                                                                                            }
                                                                                            else {
                                                                                                Log.e("apires", "res == null")
                                                                                            }
                                                                                        }
                                                                                        else {
                                                                                            Log.e("apires", "response == not Successful")
                                                                                        }
                                                                                    }

                                                                                    override fun onFailure(call: Call<FcmSaveDTO>, t: Throwable) {
                                                                                        Log.e("apires", "토큰 전송 실패!")
                                                                                    }
                                                                                })
                                                                            }
                                                                            Log.d("apires", "토큰 저장 API 작업 완료.")
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            override fun onFailure(
                                                                call: Call<FcmCheckDTO>,
                                                                t: Throwable
                                                            ) {

                                                            }

                                                        })
                                                    }
                                                    else {
                                                        Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                                else {
                                                    Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            else {
                                                Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                                            Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                        }

                                    })
                                }
                            }
                            else {
                                Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else {
                            Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SignupEmailIsDuplicatedDTO>, t: Throwable) {
                    Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                }
            })
        },
        onAuthError = {
            user = null
            Toast.makeText(mContext, "로그인 실패", Toast.LENGTH_SHORT).show()
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .addFocusCleaner(focusManager)
            .padding(18.dp)
    ) {
        if(waitAutoLogin.value) {
            Image(
                painter = painterResource(id = R.drawable.ic_splash1),
                contentDescription = "스플래쉬 화면",
                Modifier
                    .size(300.dp)
                    .padding(bottom = 10.dp)
                    .align(Alignment.Center),
            )

        }
        else {
            Column(
                modifier = Modifier.padding(top = 50.dp)
            ) {
                Row() {
                    Text(text = "모두의 정원", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = moduBlack)
                    Text(text = "에", fontSize = 24.sp, color = moduBlack)
                }
                Text(text = "오신 것을 환영해요", fontSize = 24.sp, color = moduBlack)
                Spacer(modifier = Modifier.height(40.dp))
                //아이디 입력 textField
                EditText(title = "이메일", data = textFieldId, isTextFieldFocused = isTextFieldFocusedId)
                Spacer(modifier = Modifier.height(20.dp))
                //비밀번호 입력 textField
                EditText(title = "비밀번호", data = textFieldPw, isTextFieldFocused = isTextFieldFocusedPw)
                Spacer(modifier = Modifier.height(30.dp))
                //로그인 버튼
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick {
                            textFieldId.value = textFieldId.value.trim()
                            textFieldPw.value = textFieldPw.value.trim()
                            val jsonData = JsonObject()
                            jsonData.apply {
                                addProperty("email", textFieldId.value)
                                addProperty("password", textFieldPw.value)
                            }
                            loginAPI.login(jsonData).enqueue(object: Callback<LoginDTO> {
                                override fun onResponse(
                                    call: Call<LoginDTO>,
                                    response: Response<LoginDTO>
                                ) {
                                    if(response.isSuccessful) {
                                        val res = response.body()
                                        if(res != null) {
                                            if(res.isSuccess) {
                                                Log.e("apires", res.result.accessToken)
                                                mContext.startActivity(
                                                    Intent(mContext, MainActivity::class.java)
                                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                )
                                                val fcmToken = sharedPreferences.getString("fcmToken", "")
                                                Log.d("apires", "fcmToken :: $fcmToken")
                                                val jsonDataFcmToken = JsonObject()
                                                jsonDataFcmToken.apply {
                                                    addProperty("fcmToken", fcmToken)
                                                }
                                                Log.d("Login Info", res.result.toString())
                                                editor.putString(accessToken, res.result.accessToken)
                                                editor.putString(refreshToken, res.result.refreshToken)
                                                editor.putInt(clientId, res.result.userId)
                                                editor.putString(clientNickname, res.result.nickname)
                                                editor.putString(autoLoginId, textFieldId.value)
                                                editor.putString(autoLoginPw, textFieldPw.value)
                                                editor.putString(autoLoginOption, normalLogin)
                                                editor.apply()
                                                fcmCheckAPI.fcmCheckAPI().enqueue(object: Callback<FcmCheckDTO> {
                                                    override fun onResponse(
                                                        call: Call<FcmCheckDTO>,
                                                        response: Response<FcmCheckDTO>
                                                    ) {
                                                        if(response.isSuccessful) {
                                                            val res = response.body()
                                                            if(res != null) {
                                                                if(res.isSuccess) {
                                                                    if(fcmToken !in res.result.fcmTokens) {
                                                                        Log.d("apires", "새로운 토큰을 저장했어요")
                                                                        fcmSaveAPI.fcmSaveAPI(jsonDataFcmToken).enqueue(object: Callback<FcmSaveDTO> {
                                                                            override fun onResponse(call: Call<FcmSaveDTO>, response: Response<FcmSaveDTO>) {
                                                                                if(response.isSuccessful) {
                                                                                    val res = response.body()
                                                                                    if(res != null) {
                                                                                        if(res.isSuccess) {
                                                                                            Log.d("apires", "토큰을 정상적으로 서버에 저장했어요")
                                                                                        }
                                                                                        else {
                                                                                            Log.e("apires", res.message)
                                                                                        }
                                                                                    }
                                                                                    else {
                                                                                        Log.e("apires", "res == null")
                                                                                    }
                                                                                }
                                                                                else {
                                                                                    Log.e("apires", "response == not Successful")
                                                                                }
                                                                            }

                                                                            override fun onFailure(call: Call<FcmSaveDTO>, t: Throwable) {
                                                                                Log.e("apires", "토큰 전송 실패!")
                                                                            }
                                                                        })
                                                                    }
                                                                    Log.d("apires", "토큰 저장 API 작업 완료.")
                                                                }
                                                            }
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<FcmCheckDTO>,
                                                        t: Throwable
                                                    ) {

                                                    }

                                                })
                                            }
                                            else {
                                                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        else {
                                            Toast.makeText(mContext, "res == null", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else {
                                        Toast.makeText(mContext, "response != isSuccessful", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                                    Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                }
                            })
                        },
                    backgroundColor = moduPoint,
                    shape = RoundedCornerShape(15.dp),
                    elevation = 0.dp,
                ) {
                    Text(
                        "로그인",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .padding(18.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
                Text("소셜 계정", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack)
                Spacer(Modifier.size(18.dp))
                Card(
                    shape = RoundedCornerShape(15.dp),
                    backgroundColor = Color.White,
                    border = BorderStroke(1.dp, moduGray_light),
                    elevation = 0.dp,
                    modifier = Modifier
                        .bounceClick {
                            //구글 로그인
                            val gso =
                                GoogleSignInOptions
                                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestEmail()
                                    .requestIdToken(token)
                                    .requestProfile()
                                    .build()
                            val googleSignInClient = GoogleSignIn.getClient(mContext, gso)
                            launcher.launch(googleSignInClient.signInIntent)
                            editor.putString(autoLoginOption, googleLogin).apply()
                        }
                ) {
                    Row(
                        Modifier.padding(18.dp)
                    ) {
                        GlideImage(
                            imageModel = if(user != null) user?.photoUrl else R.drawable.google_icon,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(25.dp)
                        )
                        Spacer(Modifier.size(18.dp))
                        Row(
                            Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(
                                if (user != null) "${user?.displayName}" else "구글 계정",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = moduBlack,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "으로 로그인",
                                fontSize = 14.sp,
                                color = moduBlack,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                //계정 새로 만들기 버튼
                Card(
                    modifier = Modifier
                        .bounceClick {
                            mContext.startActivity(
                                Intent(mContext, SignupActivity::class.java)
                            )
                        }
                        .align(Alignment.CenterHorizontally),
                    backgroundColor = moduBackground,
                    shape = RoundedCornerShape(15.dp),
                    elevation = 0.dp,
                ) {
                    Row(Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                        Text("계정이 없으신가요?", fontSize = 16.sp, color = moduGray_strong, modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), textAlign = TextAlign.Center)
                        Text("가입하기", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduPoint, modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), textAlign = TextAlign.Center)
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }

    if(sharedPreferences.getBoolean(autoLoginSetting, false)) {
        if (sharedPreferences.getString(autoLoginOption, "") == normalLogin) {
            val jsonData = JsonObject()
            jsonData.apply {
                addProperty("email", sharedPreferences.getString(autoLoginId, ""))
                addProperty("password", sharedPreferences.getString(autoLoginPw, ""))
            }
            loginAPI.login(jsonData).enqueue(object : Callback<LoginDTO> {
                override fun onResponse(
                    call: Call<LoginDTO>,
                    response: Response<LoginDTO>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res != null) {
                            if (res.isSuccess) {
                                Log.e("apires", res.result.accessToken)
                                mContext.startActivity(
                                    Intent(mContext, MainActivity::class.java)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                )
                                val fcmToken = sharedPreferences.getString("fcmToken", "")
                                Log.d("apires", "fcmToken :: $fcmToken")
                                val jsonDataFcmToken = JsonObject()
                                jsonDataFcmToken.apply {
                                    addProperty("fcmToken", fcmToken)
                                }
                                Log.d("Login Info", res.result.toString())
                                editor.putString(accessToken, res.result.accessToken)
                                editor.putString(refreshToken, res.result.refreshToken)
                                editor.putInt(clientId, res.result.userId)
                                editor.putString(clientNickname, res.result.nickname)
                                editor.apply()
                                fcmCheckAPI.fcmCheckAPI()
                                    .enqueue(AuthCallBack<FcmCheckDTO>(context, "자동 로그인"))
                            } else {
                                Toast.makeText(
                                    mContext,
                                    "자동 로그인 실패 : ${res.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(mContext, "res == null", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(mContext, "response != isSuccessful", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                    Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                }
            })
        } else if (sharedPreferences.getString(autoLoginOption, "") == googleLogin) {
            val jsonData = JsonObject().apply {
                addProperty("email", user?.email)
            }
            val fcmToken = sharedPreferences.getString(fcmToken, "")
            Log.d("apires", "fcmToken :: $fcmToken")
            val jsonDataFcmToken = JsonObject()
            jsonDataFcmToken.apply {
                addProperty("fcmToken", fcmToken)
            }
            loginAPI.loginSocialAPI(jsonData).enqueue(object : Callback<LoginDTO> {
                override fun onResponse(
                    call: Call<LoginDTO>,
                    response: Response<LoginDTO>
                ) {
                    if (response.isSuccessful) {
                        val res1 = response.body()
                        if (res1 != null) {
                            if (res1.isSuccess) {
                                mContext.startActivity(
                                    Intent(mContext, MainActivity::class.java)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                )
                                Log.e("apires", res1.result.accessToken)
                                editor.putString(accessToken, res1.result.accessToken)
                                editor.putString(refreshToken, res1.result.refreshToken)
                                editor.putInt(clientId, res1.result.userId)
                                editor.putString(clientNickname, res1.result.nickname)
                                editor.apply()
                                fcmCheckAPI.fcmCheckAPI().enqueue(object : Callback<FcmCheckDTO> {
                                    override fun onResponse(
                                        call: Call<FcmCheckDTO>,
                                        response: Response<FcmCheckDTO>
                                    ) {
                                        if (response.isSuccessful) {
                                            val res = response.body()
                                            if (res != null) {
                                                if (res.isSuccess) {
                                                    if (fcmToken !in res.result.fcmTokens) {
                                                        Log.d("apires", "새로운 토큰을 저장했어요")
                                                        fcmSaveAPI.fcmSaveAPI(jsonDataFcmToken)
                                                            .enqueue(object : Callback<FcmSaveDTO> {
                                                                override fun onResponse(
                                                                    call: Call<FcmSaveDTO>,
                                                                    response: Response<FcmSaveDTO>
                                                                ) {
                                                                    if (response.isSuccessful) {
                                                                        val res = response.body()
                                                                        if (res != null) {
                                                                            if (res.isSuccess) {
                                                                                Log.d(
                                                                                    "apires",
                                                                                    "토큰을 정상적으로 서버에 저장했어요"
                                                                                )
                                                                            } else {
                                                                                Log.e(
                                                                                    "apires",
                                                                                    res.message
                                                                                )
                                                                            }
                                                                        } else {
                                                                            Log.e(
                                                                                "apires",
                                                                                "res == null"
                                                                            )
                                                                        }
                                                                    } else {
                                                                        Log.e(
                                                                            "apires",
                                                                            "response == not Successful"
                                                                        )
                                                                    }
                                                                }

                                                                override fun onFailure(
                                                                    call: Call<FcmSaveDTO>,
                                                                    t: Throwable
                                                                ) {
                                                                    Log.e("apires", "토큰 전송 실패!")
                                                                }
                                                            })
                                                    }
                                                    Log.d("apires", "토큰 저장 API 작업 완료.")
                                                }
                                            }
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<FcmCheckDTO>,
                                        t: Throwable
                                    ) {

                                    }

                                })
                            } else {
                                Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                    Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
    else waitAutoLogin.value = false
}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
) : ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()

    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }

}