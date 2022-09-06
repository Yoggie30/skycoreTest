package com.example.yogeshtestapplication.model

data class ResponseDO(
	var status_flag: Boolean = false,
	val total: Int? = null,
	val region: RegionDO? = null,
	val businesses: List<BusinessesItem?>? = null
)

data class RegionDO(
	val center: CenterDO? = null
)

data class CoordinatesDO(
	val latitude: Double? = null,
	val longitude: Double? = null
)

data class BusinessesItem(
	val distance: Double? = null,
	val image_url: String? = null,
	val rating: Double? = null,
	val coordinates: CoordinatesDO? = null,
	val reviewCount: Int? = null,
	val transactions: List<Any?>? = null,
	val url: String? = null,
	val displayPhone: String? = null,
	val phone: String? = null,
	val name: String? = null,
	val alias: String? = null,
	val location: LocationDO? = null,
	val id: String? = null,
	val categories: List<CategoriesItem?>? = null,
	val is_closed: Boolean = false,
	val price: String? = null
)

data class CenterDO(
	val latitude: Double? = null,
	val longitude: Double? = null
)

data class LocationDO(
	val country: String? = null,
	val address3: Any? = null,
	val address2: String? = null,
	val city: String? = null,
	val address1: String? = null,
	val display_address: List<String?>? = null,
	val state: String? = null,
	val zipCode: String? = null
)

data class CategoriesItem(
	val alias: String? = null,
	val title: String? = null
)

