package com.example.modugarden.main.upload.curation

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder.curationAPI
import com.example.modugarden.api.dto.CurationUploadResponse
import com.example.modugarden.main.upload.curation.UriUtil.toFile
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_CURATION
import com.example.modugarden.ui.theme.BottomButton
import com.example.modugarden.ui.theme.SnackBar
import com.example.modugarden.ui.theme.TopBar
import com.example.modugarden.viewmodel.UploadCurationViewModel
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


@Composable
fun UploadCurationWebScreen(
    navController: NavHostController,
    uploadCurationViewModel: UploadCurationViewModel,
    url: String
) {
    val mContext = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            TopBar(
                title = "???????????? ????????????",
                titleIcon = R.drawable.ic_arrow_left_bold,
                titleIconOnClick = {
                    navController.popBackStack()
                },
                titleIconSize = 20.dp,
                bottomLine = true
            )
            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams (
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(url)
                }
            }, update = {
                it.loadUrl(url)
            })
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomButton(title = "??????", onClick = {
                val file = toFile(mContext, uploadCurationViewModel.getInImage()[0])

                val requestFile = MultipartBody.Part.createFormData(
                    name = "file",
                    filename = file.name,
                    body = file.asRequestBody("image/*".toMediaType())
                )

                val jsonData = JsonObject().apply {
                    addProperty("category", uploadCurationViewModel.getInCategory().category)
                    addProperty("link", uploadCurationViewModel.getInUri())
                    addProperty("title", uploadCurationViewModel.getInTitle())
                }

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val jsonBody = jsonData.toString().toRequestBody(mediaType)


                curationAPI
                    .curationCreate(jsonBody, requestFile)
                    .enqueue(object: retrofit2.Callback<CurationUploadResponse> {
                        override fun onResponse(
                            call: Call<CurationUploadResponse>,
                            response: Response<CurationUploadResponse>
                        ) {
                            if(response.isSuccessful) {
                                val res = response.body()
                                if(res != null) {
                                    Log.d("upload-result", res.toString())
                                }
                            }
                            else {
                                Toast.makeText(mContext, "???????????? ?????? ????????????", Toast.LENGTH_SHORT).show()
                                Log.d("upload-result", response.toString())
                            }

                        }
                        override fun onFailure(
                            call: Call<CurationUploadResponse>,
                            t: Throwable
                        ) {
                            Toast.makeText(mContext, "????????? ???????????? ????????????", Toast.LENGTH_SHORT).show()
                        }
                    })

                navController.navigate(NAV_ROUTE_UPLOAD_CURATION.UPLOADSUCCESSFULLY.routeName)
            })
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(30.dp)
        ) {
            SnackBar(snackbarHostState = snackbarHostState)
        }
    }
}


object FileUtil {
    // ?????? ?????? ??????
    fun createTempFile(context: Context, fileName: String): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDir, fileName)
    }

    // ?????? ?????? ????????? ??????
    fun copyToFile(context: Context, uri: Uri, file: File) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        val buffer = ByteArray(4 * 1024)
        while (true) {
            val byteCount = inputStream!!.read(buffer)
            if (byteCount < 0) break
            outputStream.write(buffer, 0, byteCount)
        }

        outputStream.flush()
    }
}

//URI??? ????????? ?????????????????? ????????? ?????? object
object UriUtil {
    // URI -> File
    fun toFile(context: Context, uri: Uri): File {
        val fileName = getFileName(context, uri)

        val file = FileUtil.createTempFile(context, fileName)
        FileUtil.copyToFile(context, uri, file)

//        val bmp = BitmapFactory.decodeFile(file.absolutePath)
//        //???????????? ??????
//        bitmapToFile(bmp, file)

        return File(file.absolutePath)
    }

    // get file name & extension
    fun getFileName(context: Context, uri: Uri): String {
        val name = uri.toString().split("/").last()
        val ext = context.contentResolver.getType(uri)!!.split("/").last()

        return "$name.$ext"
    }

    fun bitmapToFile(bitmap: Bitmap, file: File): File{
        var out: OutputStream? = null

        Log.d("upload-result", "?????? : " + file.length())
        try{
            file.createNewFile()
            out = FileOutputStream(file)
            
            //?????? ??????(quality??????)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 63, out)
        }finally{
            out?.close()
        }
        return file
    }
}

