package com.example.modugarden.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modugarden.BuildConfig.GOOGLE_WEB_KEY
import com.example.modugarden.MainActivity
import com.example.modugarden.signup.SignupActivity
import com.example.modugarden.ui.theme.*
import com.example.modugarden.R
import com.example.modugarden.api.LoginAPI
import com.example.modugarden.api.LoginDTO
import com.example.modugarden.api.RetrofitBuilder.loginAPI
import com.example.modugarden.api.RetrofitBuilder.signupEmailAuthenticationAPI
import com.example.modugarden.api.RetrofitBuilder.signupEmailIsDuplicatedAPI
import com.example.modugarden.api.TokenStore
import com.example.modugarden.data.SignupEmailAuthenticationDTO
import com.example.modugarden.data.SignupEmailIsDuplicatedDTO
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.viewmodel.SignupViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
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
import retrofit2.create

@Composable
fun MainLoginScreen(navController: NavController) {
    val textFieldId = remember { mutableStateOf("") }
    val isTextFieldFocusedId = remember { mutableStateOf(false) }
    val textFieldPw = remember { mutableStateOf("") }
    val isTextFieldFocusedPw = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val mContext = LocalContext.current

    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
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
            signupEmailIsDuplicatedAPI.getSignupEmailIsDuplicatedAPI(jsonData).enqueue(object: Callback<SignupEmailIsDuplicatedDTO> {
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
                                    mContext.startActivity(
                                        Intent(mContext, MainActivity::class.java)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    )
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
    val token = GOOGLE_WEB_KEY

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .addFocusCleaner(focusManager)
            .padding(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(top = 50.dp)
        ) {
            Text(text = "로그인", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = moduBlack)
            Spacer(modifier = Modifier.height(40.dp))
            //아이디 입력 textField
            EditText(title = "아이디", data = textFieldId, isTextFieldFocused = isTextFieldFocusedId)
            Spacer(modifier = Modifier.height(20.dp))
            //비밀번호 입력 textField
            EditText(title = "비밀번호", data = textFieldPw, isTextFieldFocused = isTextFieldFocusedPw)
            Spacer(modifier = Modifier.height(30.dp))
            //로그인 버튼
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceClick {
                        val jsonObject = JsonObject().apply {
                            addProperty("email", textFieldId.value)
                            addProperty("password", textFieldPw.value)
                        }
                        loginAPI.login(jsonObject)
                            .enqueue(object: retrofit2.Callback<LoginDTO> {
                                override fun onResponse(
                                    call: Call<LoginDTO>,
                                    response: Response<LoginDTO>
                                ) {
                                    if(response.isSuccessful) {
                                        val res = response.body()
                                        if(res != null) {
                                            Log.e("apires", res.message)
                                            if(res.isSuccess) {
                                                mContext.startActivity(
                                                    Intent(mContext, MainActivity::class.java)
                                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                )
                                                TokenStore.accessToken = res.result.accessToken
                                                TokenStore.refreshToken = res.result.refreshToken
                                            }
                                            else {
                                                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        else {
                                            Toast.makeText(mContext, "정보를 정상적으로 받지 못했어요", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else {
                                        Log.e("apires", "....;")
                                    }
                                }

                                override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                                    Log.e("apires", t.message.toString())
                                }

                            })
                    },
                backgroundColor = moduPoint,
                shape = RoundedCornerShape(10.dp),
                elevation = 0.dp,
            ) {
                Text(
                    "로그인",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(15.dp)
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
                            Intent(mContext, MainActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                    }
                    .align(Alignment.CenterHorizontally),
                backgroundColor = Color.White,
                elevation = 0.dp,
            ) {
                Text("계정 새로 만들기", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack, modifier = Modifier.padding(10.dp), textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
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