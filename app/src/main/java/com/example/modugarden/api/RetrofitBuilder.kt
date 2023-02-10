package com.example.modugarden.api

import com.example.modugarden.ApplicationClass.Companion.retrofit
import com.example.modugarden.ApplicationClass.Companion.retrofitGoogleMap
import com.example.modugarden.ApplicationClass.Companion.retrofitWithNoInterceptor
import com.example.modugarden.api.api.*

object RetrofitBuilder {
    val userAPI: UserAPI = retrofit.create(UserAPI::class.java)
    val followAPI: FollowAPI = retrofit.create(FollowAPI::class.java)
    val curationAPI: CurationAPI = retrofit.create(CurationAPI::class.java)
    val fcmSaveAPI: FcmSaveAPI = retrofit.create(FcmSaveAPI::class.java)
    val fcmCheckAPI: FcmCheckAPI = retrofit.create(FcmCheckAPI::class.java)
    val postCreateAPI: PostCreateAPI = retrofit.create(PostCreateAPI::class.java)
    val blockAPI: BlockAPI = retrofit.create(BlockAPI::class.java)
    val postAPI: PostAPI = retrofit.create(PostAPI::class.java)
    val reportAPI: ReportAPI = retrofit.create(ReportAPI::class.java)
    val commentAPI:CommentAPI = retrofit.create(CommentAPI::class.java)

    val loginAPI: LoginAPI = retrofitWithNoInterceptor.create(LoginAPI::class.java)
    val signupAPI: SignupAPI = retrofitWithNoInterceptor.create(SignupAPI::class.java)

    val searchLocationAPI: UploadPostTagLocationSearchAPI = retrofitGoogleMap.create(UploadPostTagLocationSearchAPI::class.java)
    val postLocationPhotoAPI: PostLocationPhotoAPI = retrofitGoogleMap.create(PostLocationPhotoAPI::class.java)

}