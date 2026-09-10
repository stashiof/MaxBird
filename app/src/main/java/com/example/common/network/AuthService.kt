package com.example.common.network

import com.example.common.model.MockStudyData
import com.example.common.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Request and Response models for Shikho Auth V2 Login
 */
data class LoginProfileData(
    val deviceId: String = "esxGyKOoQsSwaxKKsjil9T:APA91bHl2k9xUMrULVBWEILP2u5PrtTg23D6QOTs-Gx6yTNLiRDRVyCPqe0pA6MGk4AUswD003wMTbrCsYJ96IJf_Sl1E96Kj6CFNWkoUrjiGr4D_8K9ir4"
)

data class LoginRequestPayload(
    val phone: String,
    val otp: String,
    val type: String = "student",
    val profile: LoginProfileData = LoginProfileData(),
    val googleAdsId: String = "e6076d1a-35cc-4b58-b30c-85026db62d0d"
)

sealed interface LoginResult {
    data class Success(
        val accessToken: String,
        val refreshToken: String? = null,
        val userProfile: UserProfile? = null,
        val tokenType: String = "Bearer",
        val rawResponse: String? = null
    ) : LoginResult

    data class Error(
        val message: String,
        val statusCode: Int? = null,
        val errorDetails: String? = null
    ) : LoginResult
}

/**
 * Cross-platform network service for Authentication.
 * Meets the exact specification:
 * - URL: https://api.shikho.com/auth/v2/login
 * - Method: POST
 * - Headers:
 *     - "Accept": "application/json"
 *     - "X-User-Timezone": "Asia/Dhaka"
 *     - "Build-Version": "(605) 6.0.5"
 *     - "User-Agent": "Shikho/(605) 6.0.5 (Android 12; V2029; vivo 2027; en; LTE; )"
 *     - "Content-Type": "application/json"
 * - Payload:
 *     {
 *       "phone": "88" + phoneNumberState,
 *       "otp": passwordState,
 *       "type": "student",
 *       "profile": {
 *         "device_id": "..."
 *       },
 *       "google_ads_id": "e6076d1a-35cc-4b58-b30c-85026db62d0d"
 *     }
 * - Response parsing: tokens.access_token & user profile
 */
class AuthService(
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()
) {

    companion object {
        const val LOGIN_URL = "https://api.shikho.com/auth/v2/login"
        const val PROFILE_URL = "https://api.shikho.com/auth/v2/profile"
        const val USER_URL = "https://api.shikho.com/auth/v2/user"
        const val HEADER_ACCEPT = "application/json"
        const val HEADER_TIMEZONE = "Asia/Dhaka"
        const val HEADER_BUILD_VERSION = "(605) 6.0.5"
        const val HEADER_USER_AGENT = "Shikho/(605) 6.0.5 (Android 12; V2029; vivo 2027; en; LTE; )"
        const val MEDIA_TYPE_JSON = "application/json; charset=utf-8"

        /**
         * Extracts and maps user information from API JSON responses (name, avatar, college, class, etc.)
         */
        fun parseUserProfileFromJson(
            jsonString: String,
            fallbackPhone: String = "",
            baseProfile: UserProfile = MockStudyData.currentUserProfile
        ): UserProfile {
            if (jsonString.isBlank()) return baseProfile.copy(isLoggedIn = true)
            return try {
                val jsonObject = JSONObject(jsonString)
                val dataObj = jsonObject.optJSONObject("data") ?: jsonObject
                val userObj = dataObj.optJSONObject("user")
                    ?: dataObj.optJSONObject("profile")
                    ?: jsonObject.optJSONObject("user")
                    ?: dataObj

                val profileSubObj = userObj.optJSONObject("profile") ?: dataObj.optJSONObject("profile")
                val institutionObj = userObj.optJSONObject("institution")
                    ?: profileSubObj?.optJSONObject("institution")
                    ?: dataObj.optJSONObject("institution")

                // 1. Name
                val parsedName = userObj.optString("name").takeIf { it.isNotBlank() }
                    ?: userObj.optString("full_name").takeIf { it.isNotBlank() }
                    ?: userObj.optString("name_bn").takeIf { it.isNotBlank() }
                    ?: userObj.optString("name_en").takeIf { it.isNotBlank() }
                    ?: "${userObj.optString("first_name")} ${userObj.optString("last_name")}".trim().takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("name")?.takeIf { it.isNotBlank() }

                // 2. Phone
                val parsedPhone = userObj.optString("phone").takeIf { it.isNotBlank() }
                    ?: userObj.optString("mobile").takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("phone")?.takeIf { it.isNotBlank() }
                    ?: fallbackPhone.takeIf { it.isNotBlank() }

                // 3. Avatar / Profile Photo
                val parsedAvatar = userObj.optString("avatar").takeIf { it.isNotBlank() }
                    ?: userObj.optString("avatar_url").takeIf { it.isNotBlank() }
                    ?: userObj.optString("photo").takeIf { it.isNotBlank() }
                    ?: userObj.optString("profile_pic").takeIf { it.isNotBlank() }
                    ?: userObj.optString("profile_photo").takeIf { it.isNotBlank() }
                    ?: userObj.optString("image").takeIf { it.isNotBlank() }
                    ?: userObj.optString("image_url").takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("avatar")?.takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("photo")?.takeIf { it.isNotBlank() }

                // 4. College / Institution Name
                val parsedInstitution = userObj.optString("institution_name").takeIf { it.isNotBlank() }
                    ?: userObj.optString("college").takeIf { it.isNotBlank() }
                    ?: userObj.optString("college_name").takeIf { it.isNotBlank() }
                    ?: userObj.optString("school").takeIf { it.isNotBlank() }
                    ?: userObj.optString("school_name").takeIf { it.isNotBlank() }
                    ?: institutionObj?.optString("name")?.takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("institution_name")?.takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("college")?.takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("college_name")?.takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optJSONObject("institution")?.optString("name")?.takeIf { it.isNotBlank() }

                // 5. Class
                val parsedClass = userObj.optString("class").takeIf { it.isNotBlank() }
                    ?: userObj.optString("class_name").takeIf { it.isNotBlank() }
                    ?: userObj.optString("student_class").takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("class")?.takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("class_name")?.takeIf { it.isNotBlank() }

                // 6. Group / Discipline
                val parsedGroup = userObj.optString("group").takeIf { it.isNotBlank() }
                    ?: userObj.optString("group_name").takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("group")?.takeIf { it.isNotBlank() }

                // 7. Exam Batch
                val parsedBatch = userObj.optString("batch").takeIf { it.isNotBlank() }
                    ?: userObj.optString("exam_batch").takeIf { it.isNotBlank() }
                    ?: userObj.optString("passing_year").takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("batch")?.takeIf { it.isNotBlank() }

                // 8. Gender
                val rawGender = userObj.optString("gender").takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("gender")?.takeIf { it.isNotBlank() }
                val parsedGender = when (rawGender?.lowercase()) {
                    "male", "boy", "m" -> "ছাত্র"
                    "female", "girl", "f" -> "ছাত্রী"
                    else -> rawGender
                }

                // 9. DOB
                val parsedDob = userObj.optString("dob").takeIf { it.isNotBlank() }
                    ?: userObj.optString("birth_date").takeIf { it.isNotBlank() }
                    ?: userObj.optString("date_of_birth").takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("dob")?.takeIf { it.isNotBlank() }

                // 10. Guardian Info
                val parsedGuardianName = userObj.optString("guardian_name").takeIf { it.isNotBlank() }
                    ?: userObj.optString("parent_name").takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("guardian_name")?.takeIf { it.isNotBlank() }

                val parsedGuardianPhone = userObj.optString("guardian_phone").takeIf { it.isNotBlank() }
                    ?: userObj.optString("parent_phone").takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("guardian_phone")?.takeIf { it.isNotBlank() }

                // 11. Board & Roll
                val parsedBoard = userObj.optString("board").takeIf { it.isNotBlank() }
                    ?: userObj.optString("ssc_board").takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("board")?.takeIf { it.isNotBlank() }

                val parsedRoll = userObj.optString("roll").takeIf { it.isNotBlank() }
                    ?: userObj.optString("ssc_roll").takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("roll")?.takeIf { it.isNotBlank() }

                // 12. Division & District
                val parsedDivision = userObj.optString("division").takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("division")?.takeIf { it.isNotBlank() }

                val parsedDistrict = userObj.optString("district").takeIf { it.isNotBlank() }
                    ?: profileSubObj?.optString("district")?.takeIf { it.isNotBlank() }

                baseProfile.copy(
                    name = parsedName ?: baseProfile.name,
                    phone = parsedPhone ?: baseProfile.phone,
                    avatarUrl = parsedAvatar ?: baseProfile.avatarUrl,
                    institutionName = parsedInstitution ?: baseProfile.institutionName,
                    studentClass = parsedClass ?: baseProfile.studentClass,
                    group = parsedGroup ?: baseProfile.group,
                    examBatch = parsedBatch ?: baseProfile.examBatch,
                    gender = parsedGender ?: baseProfile.gender,
                    birthDate = parsedDob ?: baseProfile.birthDate,
                    guardianName = parsedGuardianName ?: baseProfile.guardianName,
                    guardianPhone = parsedGuardianPhone ?: baseProfile.guardianPhone,
                    sscBoard = parsedBoard ?: baseProfile.sscBoard,
                    sscRoll = parsedRoll ?: baseProfile.sscRoll,
                    institutionDivision = parsedDivision ?: baseProfile.institutionDivision,
                    institutionDistrict = parsedDistrict ?: baseProfile.institutionDistrict,
                    isLoggedIn = true
                )
            } catch (_: Exception) {
                baseProfile.copy(isLoggedIn = true)
            }
        }
    }

    /**
     * Attempts to fetch full student profile from Shikho API using the Bearer token.
     */
    suspend fun fetchUserProfile(accessToken: String, currentProfile: UserProfile): UserProfile = withContext(Dispatchers.IO) {
        val urlsToTry = listOf(PROFILE_URL, USER_URL)
        for (url in urlsToTry) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .addHeader("Accept", HEADER_ACCEPT)
                    .addHeader("X-User-Timezone", HEADER_TIMEZONE)
                    .addHeader("Build-Version", HEADER_BUILD_VERSION)
                    .addHeader("User-Agent", HEADER_USER_AGENT)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val body = response.body?.string().orEmpty()
                        if (body.isNotBlank()) {
                            return@withContext parseUserProfileFromJson(
                                jsonString = body,
                                fallbackPhone = currentProfile.phone,
                                baseProfile = currentProfile
                            )
                        }
                    }
                }
            } catch (_: Exception) {
                // Ignore and try fallback url or return current
            }
        }
        currentProfile
    }

    /**
     * Executes the login POST request with the specified phone number and otp/password.
     */
    suspend fun login(phoneNumber: String, otpOrPassword: String): LoginResult = withContext(Dispatchers.IO) {
        val cleanPhone = phoneNumber.trim().removePrefix("+88").removePrefix("88")
        val formattedPhone = "88$cleanPhone"

        // Build exact JSON body payload
        val profileJson = JSONObject().apply {
            put(
                "device_id",
                "esxGyKOoQsSwaxKKsjil9T:APA91bHl2k9xUMrULVBWEILP2u5PrtTg23D6QOTs-Gx6yTNLiRDRVyCPqe0pA6MGk4AUswD003wMTbrCsYJ96IJf_Sl1E96Kj6CFNWkoUrjiGr4D_8K9ir4"
            )
        }

        val requestJson = JSONObject().apply {
            put("phone", formattedPhone)
            put("otp", otpOrPassword.trim())
            put("type", "student")
            put("profile", profileJson)
            put("google_ads_id", "e6076d1a-35cc-4b58-b30c-85026db62d0d")
        }

        val requestBody = requestJson.toString().toRequestBody(MEDIA_TYPE_JSON.toMediaType())

        val request = Request.Builder()
            .url(LOGIN_URL)
            .post(requestBody)
            .addHeader("Accept", HEADER_ACCEPT)
            .addHeader("X-User-Timezone", HEADER_TIMEZONE)
            .addHeader("Build-Version", HEADER_BUILD_VERSION)
            .addHeader("User-Agent", HEADER_USER_AGENT)
            .addHeader("Content-Type", "application/json")
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val responseBodyString = response.body?.string().orEmpty()
                val statusCode = response.code

                if (response.isSuccessful && responseBodyString.isNotEmpty()) {
                    try {
                        val jsonObject = JSONObject(responseBodyString)

                        // Parse tokens.access_token according to API specification
                        val dataObj = jsonObject.optJSONObject("data") ?: jsonObject
                        val tokensObj = dataObj.optJSONObject("tokens") ?: jsonObject.optJSONObject("tokens")
                        
                        val accessToken = tokensObj?.optString("access_token")
                            ?: dataObj.optString("access_token")
                            ?: jsonObject.optString("token")
                            ?: jsonObject.optString("access_token")

                        // 1. Initial parse of user profile from the login response body
                        var studentProfile = parseUserProfileFromJson(
                            jsonString = responseBodyString,
                            fallbackPhone = cleanPhone
                        )

                        val validToken = if (accessToken.isNotEmpty()) {
                            accessToken
                        } else {
                            "session_token_${System.currentTimeMillis()}"
                        }

                        // 2. Fetch full profile using the access token if available
                        if (accessToken.isNotEmpty()) {
                            studentProfile = fetchUserProfile(accessToken, studentProfile)
                        }

                        // 3. Immediately sync to MockStudyData.currentUserProfile
                        MockStudyData.currentUserProfile = studentProfile

                        val refreshToken = tokensObj?.optString("refresh_token")
                        LoginResult.Success(
                            accessToken = validToken,
                            refreshToken = refreshToken,
                            userProfile = studentProfile,
                            rawResponse = responseBodyString
                        )
                    } catch (e: Exception) {
                        LoginResult.Error(
                            message = "রেসপন্স প্রসেসিংয়ে ত্রুটি: ${e.localizedMessage ?: "Unknown parse error"}",
                            statusCode = statusCode,
                            errorDetails = responseBodyString
                        )
                    }
                } else {
                    // Extract error message from body if present
                    val errorMessage = try {
                        val errorJson = JSONObject(responseBodyString)
                        errorJson.optString("message", errorJson.optString("error", "লগইন ব্যর্থ হয়েছে (Status: $statusCode)"))
                    } catch (_: Exception) {
                        "লগইন ব্যর্থ হয়েছে (Status: $statusCode)"
                    }

                    LoginResult.Error(
                        message = errorMessage,
                        statusCode = statusCode,
                        errorDetails = responseBodyString
                    )
                }
            }
        } catch (e: IOException) {
            LoginResult.Error(
                message = "নেটওয়ার্ক সংযোগ ব্যর্থ হয়েছে। অনুগ্রহ করে ইন্টারনেট সংযোগ চেক করুন।",
                errorDetails = e.localizedMessage
            )
        } catch (e: Exception) {
            LoginResult.Error(
                message = "অনাকাঙ্ক্ষিত ত্রুটি ঘটেছে: ${e.localizedMessage ?: "Unknown error"}",
                errorDetails = e.localizedMessage
            )
        }
    }
}
