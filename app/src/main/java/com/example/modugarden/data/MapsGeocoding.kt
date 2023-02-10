package com.example.modugarden.data

data class MapsDetailRes(
    val html_attributions: List<String>?,
    val result: PlaceDetails?,
    val status: String,
)

data class PlaceDetails(
    val photos:List<PhotoReference>
)

data class PhotoReference(
    val height:Int,
    val width:Int,
    val html_attributions:List<String>,
    val photo_reference :String
)
data class MapsGeocoding(
    val html_attributions: List<String>,
    val results: List<Place>,
    val status: String,
    val error_message: String,
    val info_messages: List<String>,
    val next_page_token: String
)
data class Place(
    val address_components: List<AddressComponent>,
    val adr_address: String,
    val formatted_address: String,
    val geometry: Geometry,
    val icon: String,
    val icon_background_color: String,
    val icon_mask_base_uri: String,
    val name: String,
    val place_id: String,
)
data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)
data class Geometry(
    val location: LatLngLiteral,
    val viewport: Viewport
)
data class LatLngLiteral(
    val lat: String,
    val lng: String,
)
data class Viewport(
    val northeast: LatLngLiteral,
    val southwest: LatLngLiteral
)