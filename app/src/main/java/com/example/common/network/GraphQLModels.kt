package com.example.common.network

/**
 * Common GraphQL Request wrapper for api.shikho.com/graphql
 */
data class GraphQLRequest(
    val operationName: String,
    val query: String,
    val variables: Map<String, Any?>
)

/**
 * Domain Models for GraphQL Course Hierarchy
 */
data class GqlChapterItem(
    val chapterId: String,
    val chapterName: String,
    val chapterNo: String = "",
    val status: String = "Finished", // e.g. "Running", "Finished", "Upcoming"
    val classCounter: Int = 0,
    val examCounter: Int = 0,
    val progressPercentage: Double = 0.0
) {
    val isRunning: Boolean get() = status.equals("Running", ignoreCase = true) || status == "চলমান"
    val statusBadgeBn: String
        get() = when {
            isRunning -> "চলমান"
            status.equals("Finished", ignoreCase = true) -> "পড়ানো শেষ"
            status.equals("Upcoming", ignoreCase = true) -> "আসন্ন"
            else -> status
        }
}

data class GqlTopic(
    val id: String,
    val name: String
)

data class GqlLiveClass(
    val id: String,
    val chapterName: String? = null,
    val recordingUrl: String? = null,
    val isOngoing: Boolean = false,
    val type: String? = null,
    val topics: List<GqlTopic> = emptyList()
)

data class GqlQuiz(
    val id: String,
    val title: String
)

data class GqlAnimatedVideo(
    val id: String,
    val topicName: String
)

enum class LessonContentType {
    LIVE_CLASS,
    RECORDED_CLASS,
    EXAM_LIVE,
    EXAM_QUIZ,
    ANIMATED_VIDEO,
    HOMEWORK,
    UNKNOWN
}

data class GqlLessonItem(
    val id: String,
    val title: String,
    val contentType: String, // e.g. "LiveClass", "LiveExam", "HomeWork"
    val userActivityState: String = "Upcoming", // "Missed", "Completed", "Upcoming"
    val startTime: String? = null,
    val endTime: String? = null,
    val hwType: String? = null,
    val liveClass: GqlLiveClass? = null,
    val hwQuiz: GqlQuiz? = null,
    val hwAnimatedVideo: GqlAnimatedVideo? = null
) {
    val parsedContentType: LessonContentType
        get() = when {
            contentType.equals("LiveClass", ignoreCase = true) -> {
                if (liveClass?.isOngoing == true) LessonContentType.LIVE_CLASS
                else LessonContentType.RECORDED_CLASS
            }
            contentType.equals("LiveExam", ignoreCase = true) -> LessonContentType.EXAM_LIVE
            hwQuiz != null || contentType.contains("Quiz", ignoreCase = true) -> LessonContentType.EXAM_QUIZ
            hwAnimatedVideo != null || contentType.contains("Animated", ignoreCase = true) -> LessonContentType.ANIMATED_VIDEO
            else -> LessonContentType.LIVE_CLASS
        }

    val isExam: Boolean
        get() = parsedContentType == LessonContentType.EXAM_LIVE || parsedContentType == LessonContentType.EXAM_QUIZ

    val activityStateBadgeBn: String
        get() = when (userActivityState.lowercase()) {
            "completed" -> "সম্পন্ন"
            "missed" -> "মিসড"
            "upcoming" -> "আসন্ন"
            "ongoing" -> "চলমান"
            else -> userActivityState
        }
}

data class GqlResourceAttachment(
    val id: String,
    val title: String,
    val description: String? = null,
    val url: String // AWS S3 pre-signed direct URL
) {
    val isPdf: Boolean
        get() = url.contains(".pdf", ignoreCase = true) || title.contains("PDF", ignoreCase = true) || title.contains("বই", ignoreCase = true) || title.contains("শিট", ignoreCase = true)
}

/**
 * Domain Models for Syllabus Switch Flow & Program Phases
 */
data class GqlAcademicProgram(
    val id: String,
    val title: String,
    val className: String,
    val group: String,
    val examBatch: String,
    val isActive: Boolean = true,
    val expiryDate: String = "০১ সেপ্টেম্বর, ২০২৬",
    val isOnInstallment: Boolean = true
)

data class GqlProgramPhase(
    val id: String,
    val academicProgramId: String,
    val title: String,
    val status: String,
    val isCurrent: Boolean = false,
    val courseProgressPercentage: Double = 0.0,
    val syllabusAttachmentUrl: String? = null
)

/**
 * Domain Models for REST Address API & Profile Updation
 */
data class AddressDivision(
    val id: String,
    val name: String,
    val nameBn: String = name
)

data class AddressDistrict(
    val id: String,
    val divisionId: String,
    val name: String,
    val nameBn: String = name
)

data class SchoolItem(
    val id: String,
    val name: String,
    val districtId: String? = null
)

data class ProfileUpdatePayload(
    val dob: String? = null,
    val gender: String? = null,
    val shift: String? = null,
    val sscBoardName: String? = null,
    val hscBoardName: String? = null,
    val boardRollNumber: String? = null,
    val hscBoardRollNumber: String? = null,
    val boardRegNumber: String? = null,
    val otherTutoringSource: List<String> = emptyList(),
    val guardianName: String? = null,
    val guardianMobile: String? = null,
    val schoolId: String? = null
)

/**
 * Domain Models for Enrollment & Payment Plans
 */
data class PricePlan(
    val title: String,
    val amount: Double,
    val serial: Int = 1
)

data class EnrollPaymentPlanItem(
    val id: String,
    val isActive: Boolean = true,
    val serial: Int = 1,
    val paymentStatus: String = "Paid", // "Paid", "Pending", "Due"
    val paymentDate: String? = null,
    val dueDate: String? = null,
    val pricePlan: PricePlan? = null
) {
    val isPaid: Boolean
        get() = paymentStatus.equals("Paid", ignoreCase = true) || paymentStatus == "পরিশোধিত"

    val titleBn: String
        get() = pricePlan?.title ?: when (serial) {
            1 -> "১ম কিস্তি"
            2 -> "২য় কিস্তি"
            3 -> "৩য় কিস্তি"
            4 -> "৪র্থ কিস্তি"
            else -> "$serial-তম কিস্তি"
        }

    val amountFormattedBn: String
        get() {
            val amt = pricePlan?.amount?.toInt() ?: 0
            return "৳%,d".format(amt)
        }
}

/**
 * Sealed UI State wrappers for clean StateFlow presentation
 */
sealed interface CourseContentUiState<out T> {
    data object Loading : CourseContentUiState<Nothing>
    data class Success<T>(val data: T) : CourseContentUiState<T>
    data class Error(val message: String, val cachedData: Any? = null) : CourseContentUiState<Nothing>
}
