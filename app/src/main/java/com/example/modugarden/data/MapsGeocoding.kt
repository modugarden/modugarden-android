package com.example.modugarden.data

data class MapsGeocoding(
    val results: List<Results>,
    val status: String
)
data class Results(
    val address_components: List<AddressComponents>,
    val formatted_address: String,
    val geometry: Geometry,
    val partial_match: String,
    val place_id: String,
    val plus_code: PlusCode,
    val types: List<String>
)
data class PlusCode(
    val compound_code: String,
    val global_code: String,
)
data class AddressComponents(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)
data class Geometry(
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)
data class Location(
    val lat: String,
    val lng: String,
)
data class Viewport(
    val northeast: Location,
    val southwest: Location
)