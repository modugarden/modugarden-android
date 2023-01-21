package com.example.modugarden.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.modugarden.BuildConfig
import com.example.modugarden.MainActivity
import com.example.modugarden.signup.SignupActivity
import com.example.modugarden.ui.theme.*
import com.example.modugarden.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class Login: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainLoginScreen()
        }
    }
}

@Composable
fun MainLoginScreen() {
    val textFieldId = remember { mutableStateOf("") }
    val isTextFieldFocusedId = remember { mutableStateOf(false) }
    val textFieldPw = remember { mutableStateOf("") }
    val isTextFieldFocusedPw = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val mContext = LocalContext.current

    var googleSignInClient: GoogleSignInClient

    fun googleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("869252961332-55mlg41lk58vd8qpthfikc0echuu0mb5.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(mContext, gso)
    }

    val startForResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val intent = result.data
        if (result.data != null) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
        }
    }

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
                        mContext.startActivity(
                            Intent(mContext, MainActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
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
            Text("소셜 로그인", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(Modifier.size(18.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.naver_icon),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp).clip(CircleShape).bounceClick {

                    }
                )
                Spacer(Modifier.size(15.dp))
                Card(
                    shape = CircleShape,
                    backgroundColor = moduBackground,
                    elevation = 0.dp,
                    modifier = Modifier.size(50.dp).bounceClick {
                        //구글 로그인

                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_icon),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.clip(CircleShape).padding(10.dp).align(Alignment.CenterVertically)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            //계정 새로 만들기 버튼
            Card(
                modifier = Modifier
                    .bounceClick {
                        mContext.startActivity(Intent(mContext, SignupActivity::class.java))
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

@Preview
@Composable
fun LoginPreview(){
    MainLoginScreen()
}
