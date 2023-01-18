package com.example.modugarden.main.content

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.R
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.ui.theme.addFocusCleaner
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.ui.theme.moduGray_strong
import com.example.modugarden.ui.theme.moduPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun PostContentCommentScreen(navController: NavHostController) {
    val textFieldComment = remember { mutableStateOf("") } // 댓글 입력 데이터
    val isTextFieldCommentFocused = remember { mutableStateOf(false) }
    val isButtonReplyClicked = remember { mutableStateOf(false) }
    val isButtonCloseReplyClicked = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    //바텀 시트
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden)
    val showModalSheet = rememberSaveable{ mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetElevation = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetState = bottomSheetState,
        sheetContent = {
            Card(modifier = Modifier
                .padding(10.dp),
                shape = RoundedCornerShape(15.dp))
            {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    // 회색 선
                    Box(modifier = Modifier
                        .width(40.dp)
                        .height(5.dp)
                        .background(moduGray_normal, RoundedCornerShape(30.dp))
                        .alpha(0.2f)
                    )
                    Spacer(modifier = Modifier.size(30.dp))

                    Column(modifier = Modifier
                        .padding(horizontal = 18.dp)) {
                        Text(text = "댓글 신고", style = moduBold, fontSize = 20.sp)

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Image(painter = painterResource(id = R.drawable.ic_user),
                                contentDescription = "",
                                modifier = Modifier
                                    .border(1.dp, moduGray_light, RoundedCornerShape(50.dp))
                                    .size(25.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop)
                            Spacer(modifier = Modifier.size(18.dp))
                            Text(text = "댓글 신고", style = moduBold, fontSize = 14.sp)
                        }
                    }

                    // 구분선
                    Divider(color = moduGray_light, modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp))

                    // 신고 카테고리 리스트
                    LazyColumn(modifier = Modifier
                        .padding(horizontal = 18.dp) ){
                        item {
                            categoryItem("욕설/비하")
                            categoryItem( "낚시/놀람/도배")
                            categoryItem(category = "음란물/불건전한 만남 및 대화")
                            categoryItem(category = "유출/사칭/사기")
                            categoryItem(category = "게시판 성격에 부적절함")
                        }
                    }
                    Spacer(modifier = Modifier.size(18.dp))
                    // 구분선
                    Divider(color = moduGray_light, modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp))
                    //댓글 작성자 차단
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                        .bounceClick { },
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "userID", style = moduBold, fontSize = 16.sp)
                        Text(text = "님 차단하기", color = moduBlack, fontSize = 16.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(painter = painterResource(id = R.drawable.ic_chevron_right),
                            contentDescription = null, tint = moduGray_strong)
                    }

                }


            }

        }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .addFocusCleaner(focusManager)
                )
                {
                    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                        Row(
                            modifier = Modifier
                                .padding(18.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            // 뒤로가기 아이콘 (변경 필요)
                            Icon(
                                modifier = Modifier
                                    .bounceClick { navController.popBackStack() },
                                painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                                contentDescription = "뒤로가기 아이콘", tint = moduBlack
                            )
                            Spacer(modifier = Modifier.size(18.dp))
                            Text(text = "댓글", style = moduBold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.size(5.dp))
                            // 댓글 갯수
                            Text(text = "2", color = moduGray_strong, fontSize = 16.sp)
                            Spacer(modifier = Modifier.weight(1f))

                        }
                        // 구분선
                        Divider(
                            color = moduGray_light,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                        // 댓글 영역
                        Column() {
                            //댓글
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isButtonReplyClicked.value == true and
                                            isButtonCloseReplyClicked.value == false
                                        ) moduBackground
                                        else Color.Transparent
                                    )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(18.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // 댓글 작성자 프로필 사진
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_user),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.size(10.dp))
                                    // 댓글 내용
                                    Column() {
                                        Row() {
                                            Text(
                                                text = "user4 ∙ ",
                                                color = moduGray_strong,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(text = "time", color = moduGray_strong, fontSize = 11.sp)
                                        }
                                        Text(text = "comment", color = moduBlack, fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    // 댓글 버튼들
                                    Icon(modifier = Modifier.bounceClick {
                                        keyboardController?.show()
                                        if (isButtonReplyClicked.value)
                                            isButtonReplyClicked.value = false
                                        else isButtonReplyClicked.value = true
                                    }, painter = painterResource(id = R.drawable.ic_chat_line),
                                        contentDescription = "답글", tint = moduGray_strong)
                                    Spacer(modifier = Modifier.size(18.dp))
                                    Icon(
                                        modifier = Modifier.bounceClick {
                                            //버튼 클릭하면 바텀 모달 상태 변수 바뀜
                                            showModalSheet.value= !showModalSheet.value
                                            scope.launch {
                                                bottomSheetState.show()
                                            }
                                        },
                                        painter = painterResource(id = R.drawable.ic_dot3_vertical),
                                        contentDescription = "더보기", tint = moduGray_strong
                                    )
                                }
                            }

                            // 답글
                            Row(
                                modifier = Modifier
                                    .padding(18.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.size(18.dp))
                                // 댓글 작성자 프로필 사진
                                Image(
                                    painter = painterResource(id = R.drawable.ic_user),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.size(10.dp))
                                // 댓글 내용
                                Column() {
                                    Row() {
                                        Text(
                                            text = "user5 ∙ ",
                                            color = moduGray_strong,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(text = "time", color = moduGray_strong, fontSize = 11.sp)
                                    }
                                    Text(text = "reply", color = moduBlack, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    modifier = Modifier.bounceClick {
                                        //버튼 클릭하면 바텀 모달 상태 변수 바뀜
                                        showModalSheet.value= !showModalSheet.value
                                        scope.launch {
                                            bottomSheetState.show()
                                        }
                                    }, painter = painterResource(id = R.drawable.ic_dot3_vertical),
                                    contentDescription = "더보기", tint = moduGray_strong
                                )
                            }
                        }

                    }
                    // 댓글 입력창
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                    ) {

                        if (isButtonReplyClicked.value == true and
                            isButtonCloseReplyClicked.value == false
                        ) {
                            // 답글 작성 시 표시됨
                            Box(
                                modifier = Modifier
                                    .background(moduBackground)
                                    .fillMaxWidth()
                                    .padding(18.dp, 12.dp)
                            ) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    text = "@userID 님께 답글 남기는 중", color = moduGray_strong, fontSize = 12.sp
                                )
                                // 답글 창 닫기
                                Icon(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .bounceClick {
                                            if (isButtonCloseReplyClicked.value) isButtonCloseReplyClicked.value =
                                                false
                                            else isButtonCloseReplyClicked.value = true
                                        },
                                    painter = painterResource(id = R.drawable.ic_xmark),
                                    contentDescription = "",
                                    tint = moduGray_strong
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            // 구분선
                            Divider(
                                color = moduGray_light,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .padding(18.dp, 0.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 댓글 작성자 프로필 사진
                                Image(
                                    painter = painterResource(id = R.drawable.ic_user),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.size(10.dp))
                                val focusRequester = remember { FocusRequester() }
                                //댓글 입력 텍스트 필드
                                TextField(
                                    modifier = Modifier
                                        .focusRequester(focusRequester)
                                        .onFocusChanged {
                                            isTextFieldCommentFocused.value = it.isFocused
                                        },
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        cursorColor = Color.Transparent
                                    ),
                                    value = textFieldComment.value,
                                    placeholder = {
                                        Text(
                                            text = "댓글 입력",
                                            fontSize = 12.sp,
                                            color = moduGray_strong
                                        )
                                    },
                                    onValueChange = { textValue ->
                                        textFieldComment.value = textValue
                                    },
                                    textStyle = TextStyle(fontSize = 12.sp, color = moduBlack),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                // 댓글 작성 아이콘, 댓글 입력중이면 진해짐(변경 필요)
                                Icon(
                                    modifier = Modifier.alpha(
                                        if (textFieldComment.value.isNotEmpty()) 1f
                                        else 0.4f
                                    ),
                                    painter = painterResource(id = R.drawable.ic_plus_solid),
                                    contentDescription = "댓글 작성",
                                    tint = moduPoint
                                )
                            }

                        }
                    }

                }
        }
    @Composable
    fun commentItem(){
        Box(
            modifier = Modifier
                .background(
                    if (isButtonReplyClicked.value == true and
                        isButtonCloseReplyClicked.value == false
                    ) moduBackground
                    else Color.Transparent
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 댓글 작성자 프로필 사진
                Image(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.size(10.dp))
                // 댓글 내용
                Column() {
                    Row() {
                        Text(
                            text = "user1 ∙ ",
                            color = moduGray_strong,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "time", color = moduGray_strong, fontSize = 11.sp)
                    }
                    Text(text = "comment", color = moduBlack, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                // 댓글 버튼들
                Icon(modifier = Modifier.bounceClick {
                    keyboardController?.show()
                    if (isButtonReplyClicked.value)
                        isButtonReplyClicked.value = false
                    else isButtonReplyClicked.value = true
                }, painter = painterResource(id = R.drawable.ic_chat_line),
                    contentDescription = "답글", tint = moduGray_strong)
                Spacer(modifier = Modifier.size(18.dp))
                Icon(
                    modifier = Modifier.bounceClick {
                        //버튼 클릭하면 바텀 모달 상태 변수 바뀜
                        showModalSheet.value= !showModalSheet.value
                        scope.launch {
                            bottomSheetState.show()
                        }
                    },
                    painter = painterResource(id = R.drawable.ic_dot3_vertical),
                    contentDescription = "더보기", tint = moduGray_strong
                )
            }
        }
    }
}




@Composable
fun categoryItem(category:String){

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 18.dp)
        .bounceClick { },
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = category, color = moduBlack, fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null, tint = moduGray_strong)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PostContentCommentPreview(){
    val navController = rememberNavController()
    PostContentCommentScreen(navController = navController )
}