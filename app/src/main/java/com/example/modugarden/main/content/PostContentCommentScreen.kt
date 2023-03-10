package com.example.modugarden.main.content

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.ApplicationClass.Companion.clientId
import com.example.modugarden.ApplicationClass.Companion.clientNickname
import com.example.modugarden.ApplicationClass.Companion.profileImage
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.CommentDTO
import com.example.modugarden.api.dto.DeleteCommentResponse
import com.example.modugarden.api.dto.GetCommentContent
import com.example.modugarden.api.dto.GetCommentResponse
import com.example.modugarden.api.dto.ReportCommentResponse
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.route.NAV_ROUTE_POSTCONTENT
import com.example.modugarden.ui.theme.DeleteModal
import com.example.modugarden.ui.theme.OneButtonSmallDialog
import com.example.modugarden.ui.theme.ReportModal
import com.example.modugarden.ui.theme.ShowProgressBar
import com.example.modugarden.ui.theme.SmallDialog
import com.example.modugarden.ui.theme.addFocusCleaner
import com.example.modugarden.ui.theme.animateShake
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduErrorPoint
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_strong
import com.example.modugarden.ui.theme.moduPoint
import com.example.modugarden.ui.theme.sendNotification
import com.example.modugarden.viewmodel.UserViewModel
import com.google.gson.JsonObject
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@OptIn( ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class
)
@Composable
fun PostContentCommentScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    boardId: Int,
    fcmToken: ArrayList<String>?,
    run: Boolean,
) {

    val data
            = remember{ mutableStateOf(GetCommentContent(nickname = "", comment = "", localDateTime = "", parentId = null, profileImage = "", commentId = 0, userId = 0)) } // ????????? ?????? ?????????*/
    val isReplying = remember{mutableStateOf(false)}
    val textFieldComment = remember { mutableStateOf("") } // ?????? ?????? ?????????
    val isTextFieldFocused = remember { mutableStateOf(false) }
    val isButtonClicked = remember{mutableStateOf(false)}
    val isRestricted = remember{mutableStateOf(false)}
    isRestricted.value = textFieldComment.value.length>40 || textFieldComment.value.isEmpty()
    val focusManager = LocalFocusManager.current
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden)//?????? ??????
    val scope = rememberCoroutineScope()
    val activity = (LocalContext.current as? Activity)//???????????? ????????? ??? ????????? ??????
    val context = LocalContext.current.applicationContext
    LaunchedEffect(bottomSheetState.targetValue) {
        isButtonClicked.value = bottomSheetState.targetValue != ModalBottomSheetValue.Hidden
    }
    val view = LocalView.current
    val offsetX = remember {
        androidx.compose.animation.core.Animatable(
            0f
        )
    }
    if(textFieldComment.value.length>40) animateShake(offsetX,scope,view)

    val keyboardController = LocalSoftwareKeyboardController.current
    var commentres by remember { mutableStateOf(GetCommentResponse()) }
    val reportCategory = remember{ mutableStateOf("") }
    val reportMessage = remember{ mutableStateOf("") }
    val reportDialogState = remember { mutableStateOf(false) }
    val reportMessageState = remember { mutableStateOf(false) }
    val blockMessageState = remember { mutableStateOf(false) }

    if (reportDialogState.value){
        SmallDialog(
            text = "?????? ????????????????",
            text2 = "????????? ????????? ??? ????????????.",
            textColor = moduBlack,
            backgroundColor = Color.White,
            positiveButtonText = "??????",
            negativeButtonText = "??????",
            positiveButtonTextColor = Color.White,
            negativeButtonTextColor = moduBlack,
            positiveButtonColor = moduErrorPoint,
            negativeButtonColor = moduBackground,
            dialogState = reportDialogState,
            reportCategory=reportCategory.value,
            reportMessage = reportMessage
        ) {
            RetrofitBuilder.reportAPI
                .reportComment(data.value.commentId, reportCategory.value)
                .enqueue(object : Callback<ReportCommentResponse> {
                    override fun onResponse(
                        call: Call<ReportCommentResponse>,
                        response: Response<ReportCommentResponse>
                    ) {
                        if (response.body()?.isSuccess==true) {
                            reportMessage.value = "????????? ????????? ????????? ???????????????!"
                        } else {
                            reportMessage.value = response.body()!!.message
                        }
                    }

                    override fun onFailure(
                        call: Call<ReportCommentResponse>,
                        t: Throwable
                    ) {
                        Log.i("?????? ??????", "?????? ?????? ??????")
                    }
                })
            reportMessageState.value=true
        }
    }
    if(reportMessageState.value)
        OneButtonSmallDialog(
            text = reportMessage.value,
            textColor = moduBlack,
            backgroundColor = Color.White,
            buttonText = "??????",
            buttonTextColor = Color.White,
            buttonColor = moduPoint,
            dialogState = reportMessageState
        ){
            reportMessageState.value=false
            reportMessage.value=""
        }
    if(blockMessageState.value)
        OneButtonSmallDialog(
            text = "????????? ?????????????????????.",
            textColor = moduBlack,
            backgroundColor = Color.White,
            buttonText = "??????",
            buttonTextColor = Color.White,
            buttonColor = moduPoint,
            dialogState = blockMessageState
        )

    RetrofitBuilder.commentAPI.getComments(boardId)
        .enqueue(object : Callback<GetCommentResponse> {
            override fun onResponse(
                call: Call<GetCommentResponse>,
                response: Response<GetCommentResponse>
            ) {
                val res = response.body()
                if (res != null) {
                    commentres = res
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
    val isBlockedList = commentDB.filter { it.isblocked } // ??? ????????? ????????? ??????
    commentList.removeAll(isBlockedList)

    val block_parent = commentDB.filter { it.parentId==null && it.block} // ?????? ????????? ?????? & ?????? ??????
    val block_child = commentDB.filter { it.parentId!=null && it.block } // ?????? ????????? ?????? & ?????? ??????


    Log.i("?????? ?????????",commentList.toString())

    val userId =
        sharedPreferences.getInt(clientId, 0)
    ModalBottomSheetLayout(
        sheetElevation = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetState = bottomSheetState,
        sheetContent = {
            if(data.value.userId==userId){
                DeleteModal(
                    type = "??????",
                    profileImage = data.value.profileImage,
                    title = data.value.comment,
                    scope = scope,
                    bottomSheetState = bottomSheetState,
                    deleteAction = {
                        RetrofitBuilder.commentAPI
                            .deleteComment(boardId, data.value.commentId)
                            .enqueue(object : Callback<DeleteCommentResponse> {
                                override fun onResponse(
                                    call: Call<DeleteCommentResponse>,
                                    response: Response<DeleteCommentResponse>
                                ) {
                                    if (response.body()?.isSuccess == true) {
                                        commentList.removeAll( commentList.filter { it.commentId==data.value.commentId || it.parentId==data.value.commentId})
                                        scope.launch {
                                            bottomSheetState.hide()
                                        }
                                    } else {
                                        TODO()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<DeleteCommentResponse>,
                                    t: Throwable
                                ) {
                                    TODO()
                                }
                            })
                    }
                )

            }
            else {
                ReportModal(
                    type = "??????",
                    profileImage = data.value.profileImage,
                    userId = data.value.userId,
                    userName = data.value.nickname,
                    title = data.value.comment,
                    reportCategory = reportCategory,
                    reportDialogState = reportDialogState,
                    blockMessageState = blockMessageState,
                    scope = scope,
                    bottomSheetState = bottomSheetState
                )
            }

        }) {
        BackHandler(enabled = bottomSheetState.isVisible) {
            scope.launch {
                bottomSheetState.hide()
            }
        }
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
                            // ?????????
                            Row(
                                modifier = Modifier
                                    .padding(18.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                // ???????????? ????????? (?????? ??????)
                                Icon(
                                    modifier = Modifier
                                        .bounceClick {
                                            if(run) navController.popBackStack()
                                            else activity?.finish()
                                        },
                                    painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                                    contentDescription = "???????????? ?????????", tint = moduBlack
                                )

                                Spacer(modifier = Modifier.size(18.dp))
                                Text(text = "??????", style = moduBold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.size(5.dp))
                                Text(text = commentList.size.toString(), color = moduGray_strong, fontSize = 16.sp)
                                Spacer(modifier = Modifier.weight(1f))

                            }
                            // ?????????
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

                                val sortedComments  = mutableStateListOf<GetCommentContent>() //  ?????? ????????? ?????????
                                val parents = commentList.filter { it.parentId==null } // ?????? ?????????
                                val childs = commentList.filter { it.parentId != null } // ?????? ?????????
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

                        // ?????? ?????????
                        Column {
                            //?????? ???????????? ???
                            Box(){
                                androidx.compose.animation.AnimatedVisibility(
                                    visible = isReplying.value,
                                    enter = slideInVertically(initialOffsetY = { it }),
                                    exit = slideOutVertically(targetOffsetY = { it })
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(moduBackground)
                                            .fillMaxWidth()
                                            .padding(18.dp, 12.dp)

                                    ) {
                                        Text(
                                            modifier = Modifier.align(Alignment.CenterStart),
                                            text = "@${data.value.nickname} ?????? ?????? ????????? ???",
                                            color = moduGray_strong,
                                            fontSize = 12.sp
                                        )
                                        // ?????? ??? ??????
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
                                    }
                                }
                                androidx.compose.animation.AnimatedVisibility(
                                    visible = textFieldComment.value.length>40, //40??? ????????? ??????
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0XFFFFF2F2))
                                            .fillMaxWidth()
                                            .padding(18.dp, 12.dp)
                                            .offset(offsetX.value.dp, 0.dp)

                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .align(Alignment.CenterStart),
                                            text = "?????? ?????? ?????????????????????!",
                                            color = Color(0xFFF24747),
                                            fontSize = 12.sp
                                        )

                                        // ?????? ??? ??????
                                        Spacer(
                                            modifier = Modifier
                                                .align(Alignment.CenterEnd)
                                                .size(24.dp)
                                        )
                                    }
                                }
                            }
                                

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                            ) {
                                // ?????????
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
                                    // ?????? ????????? ????????? ??????
                                    GlideImage(
                                        imageModel =
                                        sharedPreferences.getString(
                                            profileImage,
                                            null
                                        ) ?: R.drawable.ic_default_profile,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop,
                                        requestOptions = {
                                            RequestOptions()
                                                .override(30,30)
                                        },
                                        loading = {
                                            ShowProgressBar()
                                        }
                                    )
                                    Spacer(modifier = Modifier.size(10.dp))
                                    val focusRequester = remember { FocusRequester() }
                                    //?????? ?????? ????????? ??????
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
                                                text = "?????? ??????",
                                                fontSize = 16.sp,
                                                color = moduGray_strong
                                            )
                                        },
                                        onValueChange = { textValue ->
                                            textFieldComment.value = textValue
                                        },
                                        textStyle = TextStyle(fontSize = 16.sp, color = moduBlack),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                        maxLines = 2
                                    )
                                    // ?????? ?????? ?????????, ?????? ???????????????  (?????? ??????)
                                    Icon(
                                        modifier = Modifier
                                            .alpha(
                                                if (!isRestricted.value) 1f
                                                else 0.4f
                                            )
                                            .bounceClick {
                                                if (!isRestricted.value) {
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
                                                                    val username =
                                                                        sharedPreferences.getString(
                                                                            clientNickname,
                                                                            ""
                                                                        )
                                                                    if (res != null) {
                                                                        comment = res.result
                                                                        commentList.add(comment)
                                                                        // ?????? ??????????????????
                                                                        if (isReplying.value) {
                                                                            isReplying.value = false
                                                                        }
                                                                        fcmToken?.forEach { token ->
                                                                            sendNotification(
                                                                                notificationType = (if (comment.parentId == null) 1 else 2),
                                                                                boardId,
                                                                                username,
                                                                                sharedPreferences.getString(
                                                                                    profileImage,
                                                                                    null
                                                                                ),
                                                                                titleMessage = (if (comment.parentId == null) "?????? ????????? ????????????." else "?????? ????????? ????????????."),
                                                                                fcmToken = token,
                                                                                message = comment.comment
                                                                            )
                                                                        }

                                                                    }
                                                                } else {
                                                                   TODO()
                                                                }
                                                            }

                                                            override fun onFailure(
                                                                call: Call<CommentDTO>,
                                                                t: Throwable
                                                            ) {
                                                                TODO()
                                                            }
                                                        })
                                                    keyboardController?.hide()
                                                    textFieldComment.value = ""
                                                }
                                            },
                                        painter = painterResource(id = R.drawable.ic_arrowcircle),
                                        contentDescription = "?????? ??????",
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
                // ?????? ????????? ????????? ??????
                GlideImage(
                    imageModel = R.drawable.ic_block_profile,
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    requestOptions = {
                        RequestOptions()
                            .override(30,30)
                    },
                    loading = {
                        ShowProgressBar()
                    }
                )
                Spacer(modifier = Modifier.size(10.dp))

                // ?????? ??????
                    Text(
                        text = "????????? ????????? ???????????????.",
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
                    // ?????? ????????? ????????? ??????
                    GlideImage(
                        imageModel = comment.profileImage ?: R.drawable.ic_default_profile,
                        contentDescription = "",
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .clickable {
                                userViewModel.setUserId(comment.userId)
                                navController.navigate(NAV_ROUTE_POSTCONTENT.WRITER.routeName)
                                //  ????????? ????????? ????????????
                            },
                        contentScale = ContentScale.Crop,
                        requestOptions = {
                            RequestOptions()
                                .override(30,30)
                        },
                        loading = {
                            ShowProgressBar()
                        }
                    )
                    Spacer(modifier = Modifier.size(10.dp))

                    // ?????? ??????
                    Column(modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp)) {
                        Row() {
                            Text(
                                text = "${comment.nickname} ??? ",
                                color = moduGray_strong,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            val value = remember{ mutableStateOf("") }
                            Text(
                                text =timeFomatter(comment.localDateTime,value) ,
                                color = moduGray_strong,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = comment.comment!!,
                            color = moduBlack,
                            fontSize = 14.sp
                        )
                    }
                    // ?????? ?????????
                    if(comment.parentId==null)
                    {
                        Icon(
                            modifier = Modifier
                                .bounceClick {
                                isReplying.value = true
                                data.value = comment
                            }
                            ,
                            painter = painterResource(id = R.drawable.ic_chat_line_s),
                            contentDescription = "??????", tint = moduGray_strong
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
                                Log.i("?????? ??????",data.value.commentId.toString())
                            }
                        ,
                        painter = painterResource(id = R.drawable.ic_dot3_vertical_s),
                        contentDescription = "??????/ ??????", tint = moduGray_strong
                    )

                }
            }


        }

}
