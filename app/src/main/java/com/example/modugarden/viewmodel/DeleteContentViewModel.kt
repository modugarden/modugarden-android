package com.example.modugarden.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.modugarden.api.dto.GetFollowFeedCurationContent
import com.example.modugarden.api.dto.PostDTO

class DeleteContentViewModel() :ViewModel(){
    @RequiresApi(Build.VERSION_CODES.N)
    fun deletePost(id:Int, postList : SnapshotStateList<PostDTO.GetFollowFeedPostContent>,
    ){
        val delete = postList.find { it.board_id==id }
        Log.i("포스트 삭제",delete.toString())
        postList.remove(delete)
    }

    fun deleteCuration(id: Int, curationList:SnapshotStateList<GetFollowFeedCurationContent>){
        val delete = curationList.find { it.curation_id==id }
        Log.i("큐레이션 삭제",delete.toString())
        curationList.remove(delete)
    }
}