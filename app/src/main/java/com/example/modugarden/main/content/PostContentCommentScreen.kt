package com.example.modugarden.main.content

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyItemScope
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.ApplicationClass
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.BlockUserResponse
import com.example.modugarden.api.dto.CommentDTO
import com.example.modugarden.api.dto.DeleteCommentResponse
import com.example.modugarden.api.dto.GetCommentContent
import com.example.modugarden.api.dto.GetCommentResponse
import com.example.modugarden.data.Report
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.route.NAV_ROUTE_POSTCONTENT
import com.example.modugarden.ui.theme.addFocusCleaner
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.ui.theme.moduGray_strong
import com.example.modugarden.ui.theme.moduPoint
import com.example.modugarden.viewmodel.CommentViewModel
import com.example.modugarden.viewmodel.UserViewModel
import com.google.gson.JsonObject
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@OptIn( ExperimentalMaterialApi::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun PostContentCommentScreen(
    navController: NavHostController,
    commentViewModel: CommentViewModel = viewModel(),
    userViewModel: UserViewModel,
    boardId: Int,
    run: Boolean,
) {

    val data
            = remember{ mutableStateOf(GetCommentContent(nickname = "", comment = "", localDateTime = "", parentId = null, profileImage = "", commentId = 0, userId = 0)) } // 클릭한 댓글 데이터*/
    val isReplying = remember{mutableStateOf(false)}
    val textFieldComment = remember { mutableStateOf("") } // 댓글 입력 데이터
    val isTextFieldFocused = remember { mutableStateOf(false) }
    val isButtonClicked = remember{mutableStateOf(false)}
    
    val focusManager = LocalFocusManager.current
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden)//바텀 시트
    val scope = rememberCoroutineScope()
    val activity = (LocalContext.current as? Activity)//액티비티 종료할 때 필요한 변수
    val context = LocalContext.current.applicationContext
    LaunchedEffect(bottomSheetState.targetValue) {
        isButtonClicked.value = bottomSheetState.targetValue != ModalBottomSheetValue.Hidden
    }
    var commentres by remember { mutableStateOf(GetCommentResponse()) }

    RetrofitBuilder.commentAPI.getComments(boardId)
        .enqueue(object : Callback<GetCommentResponse> {
            override fun onResponse(
                call: Call<GetCommentResponse>,
                response: Response<GetCommentResponse>
            ) {
                val res = response.body()
                if (res != null) {
                    commentres = res
                    Log.d("댓글디비-result", commentres.toString())
                }
            }

            override fun onFailure(call: Call<GetCommentResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    val commentDB= commentres.content
    val commentList = remember { mutableStateListOf<GetCommentContent>() }
    commentList.clear()
    commentList.addAll(commentDB)
    val isBlockedList = commentDB.filter { it.isblocked } // 날 차단한 유저의 댓글
    commentList.removeAll(isBlockedList)

    val block_parent = commentDB.filter { it.parentId==null && it.block} // 내가 차단한 유저 & 부모 댓글
    val block_child = commentDB.filter { it.parentId!=null && it.block } // 내가 차단한 유저 & 자식 댓글


    Log.i("댓글 리스트",commentList.toString())

    val userId =
        ApplicationClass.sharedPreferences.getInt(ApplicationClass.clientId, 0)

    ModalBottomSheetLayout(
        sheetElevation = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetState = bottomSheetState,
        sheetContent = {
            if(data.value.userId==userId){
                Card(
                    modifier = Modifier
                        .padding(10.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(5.dp)
                            .background(moduGray_normal, RoundedCornerShape(30.dp))
                            .alpha(0.2f)
                    )

                    Spacer(modifier = Modifier.size(30.dp))

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 18.dp)
                    ) {
                        Text(text = "댓글을 삭제할까요?", style = moduBold, fontSize = 20.sp)

                        Row(
                            modifier = Modifier
                                .padding(vertical = 30.dp)
                        ) {
                            GlideImage(
                                imageModel =
                                if(data.value.profileImage == null)
                                    R.drawable.ic_default_profile
                                else data.value.profileImage,
                                contentDescription = "",
                                modifier = Modifier
                                    .border(1.dp, moduGray_light, RoundedCornerShape(50.dp))
                                    .size(25.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(18.dp))
                            Text(
                                data.value.comment,
                                fontSize = 16.sp,
                                color = moduBlack,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.weight(1f))


                        }
                        //버튼
                        Row {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .bounceClick {
                                        scope.launch {
                                            bottomSheetState.hide()
                                        }
                                    },
                                shape = RoundedCornerShape(10.dp),
                                backgroundColor = moduGray_light,
                                elevation = 0.dp
                            ) {
                                Text(
                                    text = "취소",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = moduGray_strong,
                                    modifier = Modifier
                                        .padding(14.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.size(18.dp))
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .bounceClick {
                                        Log.i("댓글", data.value.toString())
                                        RetrofitBuilder.commentAPI
                                            .deleteComment(boardId, data.value.commentId)
                                            .enqueue(object : Callback<DeleteCommentResponse> {
                                                override fun onResponse(
                                                    call: Call<DeleteCommentResponse>,
                                                    response: Response<DeleteCommentResponse>
                                                ) {
                                                    Log.i(
                                                        "삭제",
                                                        response
                                                            .body()
                                                            .toString()
                                                    )
                                                    if (response.isSuccessful) {
                                                        Log.i("댓글 삭제", "성공")
                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<DeleteCommentResponse>,
                                                    t: Throwable
                                                ) {
                                                    Log.i("댓글 삭제", "연결 실패")
                                                }
                                            })
                                        commentViewModel.deleteComment(
                                            data.value.commentId,
                                            commentList
                                        )
                                        scope.launch {
                                            bottomSheetState.hide()
                                        }

                                    },
                                shape = RoundedCornerShape(10.dp),
                                backgroundColor = Color(0xFFFF7272),
                                elevation = 0.dp
                            ) {
                                Text(
                                    text = "삭제",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(14.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                    }


            }
            else {
                Card(
                    modifier = Modifier
                        .padding(10.dp),
                    shape = RoundedCornerShape(15.dp)
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 회색 선
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(5.dp)
                                .background(moduGray_normal, RoundedCornerShape(30.dp))
                                .alpha(0.2f)
                        )
                        Spacer(modifier = Modifier.size(30.dp))

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 18.dp)
                        ) {
                            Text(text = "댓글 신고", style = moduBold, fontSize = 20.sp)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GlideImage(
                                    imageModel =
                                    if(data.value.profileImage == null)
                                        R.drawable.ic_default_profile
                                    else data.value.profileImage,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .border(1.dp, moduGray_light, RoundedCornerShape(50.dp))
                                        .size(25.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.size(18.dp))
                                Text(
                                    text = data.value.comment!!,
                                    style = moduBold, fontSize = 14.sp
                                )
                            }
                        }

                        // 구분선
                        Divider(
                            color = moduGray_light, modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )

                        // 신고 카테고리 리스트
                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 18.dp)
                        ) {
                            Log.i("댓글 신고 아이디",data.value.commentId.toString())
                            itemsIndexed(
                                listOf(
                                    Report.ABUSE,
                                    Report.TERROR,
                                    Report.SEXUAL,
                                    Report.FISHING,
                                    Report.INAPPROPRIATE
                                )
                            ) { index, item ->
                                ReportCategoryItem(
                                    report = item,
                                    id = mutableStateOf(data.value.commentId),
                                    modalType = mutableStateOf(modalReportComment) ,
                                    scope,
                                    bottomSheetState)
                            }

                        }
                        Spacer(modifier = Modifier.size(18.dp))
                        // 구분선
                        Divider(
                            color = moduGray_light, modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                        //댓글 작성자 차단
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                                .bounceClick {
                                    scope.launch {
                                        bottomSheetState.hide()
                                    }
                                    CoroutineScope(Dispatchers.IO).launch {
                                        RetrofitBuilder.blockAPI.blockUser(data.value.userId).execute()
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = data.value.nickname!!, style = moduBold, fontSize = 16.sp)
                            Text(text = "님 차단하기", color = moduBlack, fontSize = 16.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chevron_right),
                                contentDescription = null, tint = moduGray_strong
                            )
                        }

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
                                        .bounceClick {
                                            if(run) navController.popBackStack()
                                            else activity?.finish()
                                        },
                                    painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                                    contentDescription = "뒤로가기 아이콘", tint = moduBlack
                                )

                                Spacer(modifier = Modifier.size(18.dp))
                                Text(text = "댓글", style = moduBold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.size(5.dp))
                                Text(text = commentList.size.toString(), color = moduGray_strong, fontSize = 16.sp)
                                Spacer(modifier = Modifier.weight(1f))

                            }
                            // 구분선
                            Divider(
                                color = moduGray_light,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                            )
                            LazyColumn(
                                Modifier
                                    .wrapContentSize()
                                    .background(Color.White)
                            ){

                                val sortedComments  = mutableStateListOf<GetCommentContent>() //  정렬 리스트 초기화
                                val parents = commentList.filter { it.parentId==null } // 댓글 리스트
                                val childs = commentList.filter { it.parentId != null } // 답글 리스트
                                for(i in 0 until parents.size){
                                    sortedComments.add(parents[i])
                                    for (j in 0 until childs.size) {
                                        if (childs[j].parentId == parents[i].commentId) {
                                            sortedComments.add(childs[j])
                                        }
                                    }
                                }
                                items(sortedComments, key = { it.commentId }){ comment->
                                    if (comment in block_parent || comment in block_child) {
                                        BlockCommentItem(comment = comment)
                                    }
                                    else {
                                        CommentItem(
                                            comment = comment,
                                            scope = scope,
                                            bottomSheetState = bottomSheetState,
                                            data = data,
                                            isReplying = isReplying,
                                            isButtonClicked = isButtonClicked,
                                            userViewModel = userViewModel,
                                            navController = navController
                                        )
                                    }

                                }



                            }



                        }

                        // 댓글 입력창
                        Column {
                            //답글 입력중일 때

                                AnimatedVisibility(
                                    visible = isReplying.value,
                                    enter = slideInVertically (initialOffsetY = {it}),
                                    exit = slideOutVertically (targetOffsetY = {it})
                                    ) {
                                    Box(
                                        modifier = Modifier
                                            .background(moduBackground)
                                            .fillMaxWidth()
                                            .padding(18.dp, 12.dp)

                                    ) {
                                        Text(
                                            modifier = Modifier.align(Alignment.CenterStart),
                                            text = "@${data.value.nickname} 님께 답글 남기는 중", color = moduGray_strong, fontSize = 12.sp
                                        )
                                        // 답글 창 닫기
                                        Icon(
                                            modifier = Modifier
                                                .align(Alignment.CenterEnd)
                                                .bounceClick {
                                                    isReplying.value = false

                                                },
                                            painter = painterResource(id = R.drawable.ic_xmark),
                                            contentDescription = "",
                                            tint = moduGray_strong
                                        )
                                    }}



                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
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
                                                isTextFieldFocused.value = it.isFocused
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
                                                    var comment: GetCommentContent
                                                    val jsonData = JsonObject()
                                                    jsonData.apply {
                                                        addProperty(
                                                            "content",
                                                            textFieldComment.value
                                                        )
                                                        if (isReplying.value) {
                                                            addProperty(
                                                                "parentId",
                                                                data.value.commentId
                                                            )
                                                        }

                                                    }
                                                    RetrofitBuilder.commentAPI
                                                        .sendComment(boardId, jsonData)
                                                        .enqueue(object : Callback<CommentDTO> {
                                                            @SuppressLint("SuspiciousIndentation")
                                                            override fun onResponse(
                                                                call: Call<CommentDTO>,
                                                                response: Response<CommentDTO>
                                                            ) {
                                                                if (response.isSuccessful) {
                                                                    val res = response.body()
                                                                    if (res != null) {
                                                                        comment = res.result
                                                                        Log.i(
                                                                            "댓글 작성",
                                                                            comment.toString()
                                                                        )
                                                                        // 답글 입력중이라면
                                                                        if (isReplying.value) {
                                                                            commentViewModel.addComment(
                                                                                comment,
                                                                                commentList
                                                                            )
                                                                            isReplying.value = false
                                                                        } else //댓글일 때
                                                                        {
                                                                            commentViewModel.addComment(
                                                                                comment,
                                                                                commentList
                                                                            )
                                                                        }

                                                                    }
                                                                } else Log.i(
                                                                    "댓글",
                                                                    "${response.body()}"
                                                                )
                                                            }

                                                            override fun onFailure(
                                                                call: Call<CommentDTO>,
                                                                t: Throwable
                                                            ) {
                                                                Toast
                                                                    .makeText(
                                                                        context,
                                                                        "데이터를 받지 못했어요",
                                                                        Toast.LENGTH_SHORT
                                                                    )
                                                                    .show()
                                                                Log.d(
                                                                    "comment-result",
                                                                    t.message.toString()
                                                                )
                                                            }
                                                        })
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

}

@Composable
fun BlockCommentItem(
    comment: GetCommentContent
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Box()
        {
            Row(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (comment.parentId!=null) Spacer(modifier = Modifier.size(18.dp))
                // 댓글 작성자 프로필 사진
                GlideImage(
                    imageModel = R.drawable.ic_default_profile,
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.size(10.dp))

                // 댓글 내용
                    Text(
                        text = "차단된 유저의 댓글입니다.",
                        color = moduGray_strong,
                        fontSize = 14.sp
                    )

            }
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.CommentItem(
    comment: GetCommentContent,
    scope:CoroutineScope,
    bottomSheetState:ModalBottomSheetState,
    isReplying: MutableState<Boolean>,
    isButtonClicked: MutableState<Boolean>,
    data: MutableState<GetCommentContent>,
    userViewModel: UserViewModel,
    navController: NavHostController
){
        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .animateItemPlacement()
        ) {
            Box()
            {
                Row(
                    modifier = Modifier
                        .background(
                            if ((isReplying.value && comment.commentId == data.value.commentId)
                                || (isButtonClicked.value && (comment.commentId == data.value.commentId))
                            ) moduBackground
                            else Color.White
                        )
                        .padding(18.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (comment.parentId!=null) Spacer(modifier = Modifier.size(18.dp))
                    // 댓글 작성자 프로필 사진
                    GlideImage(
                        imageModel = comment.profileImage ?: R.drawable.ic_default_profile,
                        contentDescription = "",
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .clickable {
                                userViewModel.setUserId(comment.userId)
                                navController.navigate(NAV_ROUTE_POSTCONTENT.WRITER.routeName)
                                //  포스트 작성자 프로필로
                            },
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.size(10.dp))

                    // 댓글 내용
                    Column() {
                        Row() {
                            Text(
                                text = "${comment.nickname} ∙ ",
                                color = moduGray_strong,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            val value = remember{ mutableStateOf("") }
                            Text(
                                text =timeFomatter(comment.localDateTime,value) ,
                                color = moduGray_strong,
                                fontSize = 14.sp
                            )
                        }
                        Text(
                            text = comment.comment!!,
                            color = moduBlack,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    // 댓글 버튼들
                    if(comment.parentId==null)
                    {
                        Icon(
                            modifier = Modifier.bounceClick {
                                isReplying.value = true
                                data.value = comment
                            },
                            painter = painterResource(id = R.drawable.ic_chat_line_s),
                            contentDescription = "답글", tint = moduGray_strong
                        )
                    }
                    Spacer(modifier = Modifier.size(18.dp))
                    Icon(
                        modifier = Modifier
                            .bounceClick {
                                data.value = comment
                                scope.launch {
                                    bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                }
                                Log.i("댓글 신고",data.value.commentId.toString())
                            },
                        painter = painterResource(id = R.drawable.ic_dot3_vertical_s),
                        contentDescription = "신고/ 삭제", tint = moduGray_strong
                    )

                }
            }


        }

}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PostContentCommentPreview(){
    val navController = rememberNavController()
    val commentViewModel:CommentViewModel= viewModel()
    /*PostContentCommentScreen(navController = navController,commentViewModel)*/
}