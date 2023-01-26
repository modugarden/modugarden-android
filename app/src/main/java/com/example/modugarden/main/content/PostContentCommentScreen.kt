package com.example.modugarden.main.content

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.R
import com.example.modugarden.data.Comment
import com.example.modugarden.data.CommentDataBase
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.ui.theme.addFocusCleaner
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.ui.theme.moduGray_strong
import com.example.modugarden.ui.theme.moduPoint
import com.example.modugarden.viewmodel.CommentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

@SuppressLint("UnrememberedMutableState")
@OptIn( ExperimentalMaterialApi::class)
@Composable
fun PostContentCommentScreen(navController: NavHostController,
                             commentViewModel: CommentViewModel) {
    val commentDB = CommentDataBase.getInstance(LocalContext.current.applicationContext)!! // 댓글 데이터 베이스
    val comments= remember{ mutableStateOf(commentDB.commentDao().getAll()) } // 댓글 리스트
    val data  :MutableState<Comment>
            = remember{ mutableStateOf(Comment(id = UUID.randomUUID(), userID = "", description = "", time = 0,
        isReplying = mutableStateOf(false), parentID = null)) } // 클릭한 댓글 데이터

    val textFieldComment = remember { mutableStateOf("") } // 댓글 입력 데이터
    val isTextFieldCommentFocused = remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden)//바텀 시트
    val showModalSheet = rememberSaveable{ mutableStateOf(false) } // 신고 모달 상태 변수
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
                            Text(text = data.value.description!!,
                                style = moduBold, fontSize = 14.sp)
                        }
                    }

                    // 구분선
                    Divider(color = moduGray_light, modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp))

                    // 신고 카테고리 리스트
                    LazyColumn(modifier = Modifier
                        .padding(horizontal = 18.dp) ){
                        itemsIndexed(
                            listOf("욕설/비하",
                                "낚시/놀람/도배",
                                "음란물/불건전한 만남 및 대화",
                                "유출/사칭/사기",
                                "게시판 성격에 부적절함")) { index, item ->
                                CategoryItem(category = item)
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
                        Text(text = data.value.userID!!, style = moduBold, fontSize = 16.sp)
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

                    Column {
                        Column(modifier = Modifier
                            .weight(1f, true)
                            .background(Color.White)
                            .fillMaxSize())
                        {
                            // 상단바
                            Row(
                                modifier = Modifier
                                    .padding(18.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
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
                                Text(text = "${comments.value.size}", color = moduGray_strong, fontSize = 16.sp)
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
                                LazyColumn(
                                    Modifier
                                        .wrapContentSize()
                                        .background(Color.White)
                                ){
                                    var sortedComments  = mutableStateListOf<Comment>() //  정렬 리스트 초기화
                                    var parents = comments.value.filter { it.mode==false } // 댓글 리스트
                                    var childs = comments.value.filter { it.mode==true } // 답글 리스트
                                    for(i in 0 until parents.size){
                                        sortedComments.add(parents[i])
                                        for (j in 0 until childs.size) {
                                            if (childs[j].parentID == parents[i].id) {
                                                sortedComments.add(childs[j])
                                            }
                                        }
                                    }
                                    items(sortedComments){ comment->

                                        CommentItem(
                                            comment = comment,
                                            showModalSheet = showModalSheet,
                                            scope = scope,
                                            bottomSheetState = bottomSheetState,
                                            data = data
                                        )
                                    }


                                }

                        }

                        // 댓글 입력창
                        Column {
                            //답글 입력중일 때
                            if(data.value.isReplying.value) {
                                Box(
                                    modifier = Modifier
                                        .background(moduBackground)
                                        .fillMaxWidth()
                                        .padding(18.dp, 12.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.align(Alignment.CenterStart),
                                        text = "@${data.value.userID} 님께 답글 남기는 중", color = moduGray_strong, fontSize = 12.sp
                                    )
                                    // 답글 창 닫기
                                    Icon(
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .bounceClick {
                                                data.value.isReplying.value = false
                                            },
                                        painter = painterResource(id = R.drawable.ic_xmark),
                                        contentDescription = "",
                                        tint = moduGray_strong
                                    )
                                }}

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
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
                                            .weight(weight = 1f, true)
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
                                                fontSize = 16.sp,
                                                color = moduGray_strong
                                            )
                                        },
                                        onValueChange = { textValue ->
                                            textFieldComment.value = textValue
                                        },
                                        textStyle = TextStyle(fontSize = 16.sp, color = moduBlack),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                        maxLines = 3
                                    )
                                    // 댓글 작성 아이콘, 댓글 입력중이면  (변경 필요)
                                    Icon(

                                        modifier = Modifier
                                            .bounceClick {
                                                if (textFieldComment.value.isNotEmpty()) {
                                                    // 답글 입력중이라면
                                                    if (data.value.isReplying.value) {
                                                        commentViewModel.addComment(
                                                            Comment(
                                                                id = UUID.randomUUID(),
                                                                userID = "reply",
                                                                description = textFieldComment.value,
                                                                time = 1,
                                                                parentID = data.value.id,
                                                                mode = true
                                                            ), comments, commentDB
                                                        )
                                                        data.value.isReplying.value = false
                                                    } else //댓글일 때
                                                    {
                                                        commentViewModel.addComment(
                                                            Comment(
                                                                id = UUID.randomUUID(),
                                                                userID = "comment",
                                                                description = textFieldComment.value,
                                                                time = 0,
                                                                parentID = null,
                                                                mode = false
                                                            ), comments, commentDB
                                                        )
                                                    }

                                                }
                                                textFieldComment.value = ""

                                            }
                                            .alpha(
                                                if (textFieldComment.value.isNotEmpty()) 1f
                                                else 0.4f
                                            ),
                                        painter = painterResource(id = R.drawable.ic_arrowcircle),
                                        contentDescription = "댓글 작성",
                                        tint = moduPoint
                                    )
                                }

                            }
                        }

                    }


                }

    }
    if (commentDB.commentDao().getAll().size>0) Log.d("db:",commentDB.commentDao().getAll().toString())

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommentItem(comment: Comment,
                showModalSheet:MutableState<Boolean>,
                scope:CoroutineScope,
                bottomSheetState:ModalBottomSheetState,
                data: MutableState<Comment>){
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.White)
        ) {
            Box()
            {
                Row(
                    modifier = Modifier
                        .background(
                            if (data.value.isReplying.value && comment.description == data.value.description) moduBackground
                            else Color.White
                        )
                        .padding(18.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (comment.mode) Spacer(modifier = Modifier.size(18.dp))
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
                                text = "${comment.userID} ∙ ",
                                color = moduGray_strong,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${comment.time}",
                                color = moduGray_strong,
                                fontSize = 14.sp
                            )
                        }
                        Text(
                            text = comment.description!!,
                            color = moduBlack,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    // 댓글 버튼들
                    if(comment.mode==false)
                    {
                        Icon(
                            modifier = Modifier.bounceClick {
                                comment.isReplying.value = true
                                data.value = comment
                            },
                            painter = painterResource(id = R.drawable.ic_chat_line_s),
                            contentDescription = "답글", tint = moduGray_strong
                        )
                    }
                    Spacer(modifier = Modifier.size(18.dp))
                    Icon(
                        modifier = Modifier.bounceClick {
                            data.value = comment
                            //버튼 클릭하면 바텀 모달 상태 변수 바뀜
                            showModalSheet.value = !showModalSheet.value
                            scope.launch {
                                bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        },
                        painter = painterResource(id = R.drawable.ic_dot3_vertical_s),
                        contentDescription = "더보기", tint = moduGray_strong
                    )
                }
            }


        }

}

@Composable
fun CategoryItem(category:String){
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
    val commentViewModel:CommentViewModel= viewModel()
    PostContentCommentScreen(navController = navController,commentViewModel)
}