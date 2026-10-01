package com.mato.studio.network.response

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("result") val result: String,
    @SerializedName("documentation") val documentation: String? = null,
    @SerializedName("terms_of_use") val termsOfUse: String? = null,
    @SerializedName("time_last_update_unix") val timeLastUpdateUnix: Long? = null,
    @SerializedName("time_last_update_utc") val timeLastUpdateUtc: String? = null,
    @SerializedName("time_next_update_unix") val timeNextUpdateUnix: Long? = null,
    @SerializedName("time_next_update_utc") val timeNextUpdateUtc: String? = null,
    @SerializedName("base_code") val baseCode: String? = null,
    @SerializedName("target_code") val targetCode: String? = null,
    @SerializedName("conversion_rate") val conversionRate: Double? = null,
    @SerializedName("conversion_result") val conversionResult: Double? = null,
    @SerializedName("error-type") val errorType: String? = null
)

enum class Currency(
    val code: String,
    val symbol: String,
    val currencyName: String
) {
    USD("USD", "$", "US Dollar"),
    AED("AED", "د.إ", "UAE Dirham"),
    AFN("AFN", "؋", "Afghan Afghani"),
    ALL("ALL", "L", "Albanian Lek"),
    AMD("AMD", "֏", "Armenian Dram"),
    EUR("EUR", "€", "Euro"),
    GBP("GBP", "£", "British Pound"),
    INR("INR", "₹", "Indian Rupee"),
    JPY("JPY", "¥", "Japanese Yen"),
    CAD("CAD", "C$", "Canadian Dollar"),
    AUD("AUD", "A$", "Australian Dollar"),
    SGD("SGD", "S$", "Singapore Dollar"),
    CHF("CHF", "CHF", "Swiss Franc"),
    CNY("CNY", "¥", "Chinese Yuan"),
    SAR("SAR", "﷼", "Saudi Riyal"),
    KWD("KWD", "KD", "Kuwaiti Dinar"),
    NZD("NZD", "NZ$", "New Zealand Dollar");

    companion object {
        fun fromCode(code: String): Currency {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: USD
        }
    }
}

enum class TransactionType {
    DEPOSIT,
    WITHDRAWAL
}

data class TransactionModel(
    val id: Long = 0,
    val transactionCode: String,
    val type: TransactionType,
    val originalAmount: Double,
    val currencyCode: String,
    val currencySymbol: String,
    val amountInr: Double,
    val timestamp: Long
)