package com.example.modugarden.api.dto

data class FcmSendDTO(
    val multicast_id: String,
    val success: Int,
    val failure: Int,
    val canonical_ids: Int,
    val results: List<FcmSendResults>
)
data class FcmSendResults(
    val message_id: String
)