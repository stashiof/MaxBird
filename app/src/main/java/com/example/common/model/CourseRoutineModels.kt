package com.example.common.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class EnrolledCourse(
    val id: String,
    val title: String,
    val badge: String,
    val instructor: String,
    val totalClasses: Int,
    val completedClasses: Int,
    val colorPrimaryHex: Long = 0xFF4338CA,
    val colorSecondaryHex: Long = 0xFF6366F1
)

enum class RoutineItemType {
    LIVE_CLASS,
    RECORDED,
    LAB_PRACTICE,
    EXAM_MCQ,
    EXAM_CQ
}

data class RoutineClassItem(
    val id: String,
    val time: String,
    val durationText: String = "",
    val subject: String,
    val chapterOrTopic: String,
    val instructor: String,
    val typeLabel: String = "লেকচার ক্লাস",
    val type: RoutineItemType = RoutineItemType.LIVE_CLASS,
    val subjectColorHex: Long = 0xFF0284C7,
    val isLiveNow: Boolean = false,
    val isCompleted: Boolean = false
)

data class RoutineExamItem(
    val id: String,
    val time: String,
    val title: String,
    val syllabus: String,
    val marks: Int,
    val durationText: String,
    val type: RoutineItemType = RoutineItemType.EXAM_MCQ
)

data class DaySchedule(
    val dayIndex: Int, // 0 = Sat, 1 = Sun, 2 = Mon, 3 = Tue, 4 = Wed, 5 = Thu, 6 = Fri
    val dayNameBn: String,
    val dayShortBn: String,
    val dateText: String,
    val fullDateBn: String = "",
    val classes: List<RoutineClassItem> = emptyList(),
    val exams: List<RoutineExamItem> = emptyList()
) {
    val totalCount: Int get() = classes.size + exams.size
}

data class MonthClassOverview(
    val dateString: String,
    val dayNameBn: String,
    val subject: String,
    val topic: String,
    val time: String,
    val typeName: String,
    val isExam: Boolean = false
)

data class SubjectItem(
    val id: String,
    val titleBn: String,
    val iconSymbol: String,
    val primaryColorHex: Long,
    val secondaryColorHex: Long,
    val progressPercent: Int = 0
)

data class ChapterItem(
    val id: String,
    val titleBn: String,
    val statusBadge: String, // e.g. "পড়ানো শেষ", "পড়ানো হচ্ছে"
    val isCompleted: Boolean,
    val classesCount: Int,
    val examsCount: Int
)

data class LectureItem(
    val id: String,
    val titleBn: String,
    val typeLabel: String, // "লেকচার ক্লাস", "চ্যাপ্টার এক্সাম"
    val dateText: String, // "01 September 2026", "04 September 2026"
    val statusBadge: String = "মিসড", // "মিসড"
    val noticeText: String = "", // "এখনো এক্সাম দেওয়া যাবে"
    val isExam: Boolean = false,
    val instructorName: String = "হিরন্ময় বাউলিয়া",
    val instructorBio: String = "এম.এস.এস, এম.ফিল, ঢাবি '০৮\nবাংলা শিক্ষক",
    val instructorExp: String = "১৬ বছর+ শিক্ষকতার অভিজ্ঞতা",
    val instructorStudents: String = "১০ লক্ষ+ শিক্ষার্থী পড়েছেন",
    val recordingUrl: String = "",
    val videoStreamUrl: String = "",
    val topics: List<String> = emptyList(),
    val pdfUrl: String = ""
)

object MockStudyData {
    val studentCourses = listOf(
        EnrolledCourse(
            id = "c1",
            title = "HSC '27 বিজ্ঞান - ২য় বর্ষ প্রস্তুতি",
            badge = "এইচএসসি বিজ্ঞান",
            instructor = "বুয়েট ও ঢাবি শিক্ষক পরিষদ",
            totalClasses = 140,
            completedClasses = 52,
            colorPrimaryHex = 0xFF0B1440,
            colorSecondaryHex = 0xFF3B82F6
        ),
        EnrolledCourse(
            id = "c2",
            title = "উচ্চতর গণিত ১ম ও ২য় পত্র মাস্টারকোর্স",
            badge = "ম্যাথ স্পেশাল",
            instructor = "ইঞ্জি. মোবাশ্বির হোসেন (বুয়েট)",
            totalClasses = 64,
            completedClasses = 36,
            colorPrimaryHex = 0xFF0D9488,
            colorSecondaryHex = 0xFF14B8A6
        ),
        EnrolledCourse(
            id = "c3",
            title = "পদার্থবিজ্ঞান গাণিতিক সমস্যা ও প্র্যাকটিকাল",
            badge = "ফিজিক্স ল্যাব",
            instructor = "ড. সাজ্জাদুল করিম (ঢাবি)",
            totalClasses = 50,
            completedClasses = 22,
            colorPrimaryHex = 0xFFEA580C,
            colorSecondaryHex = 0xFFF97316
        ),
        EnrolledCourse(
            id = "c4",
            title = "তথ্য ও যোগাযোগ প্রযুক্তি (ICT) বুস্টার",
            badge = "আইসিটি ২০২৭",
            instructor = "রাকিবুর রহমান",
            totalClasses = 32,
            completedClasses = 18,
            colorPrimaryHex = 0xFF7C3AED,
            colorSecondaryHex = 0xFF8B5CF6
        )
    )

    fun getWeeklySchedule(courseId: String): List<DaySchedule> {
        return listOf(
            DaySchedule(
                dayIndex = 0,
                dayNameBn = "শনিবার",
                dayShortBn = "শনি",
                dateText = "৫",
                fullDateBn = "শনিবার, ০৫/০৯/২০২৬",
                classes = emptyList(),
                exams = emptyList()
            ),
            DaySchedule(
                dayIndex = 1,
                dayNameBn = "রবিবার",
                dayShortBn = "রবি",
                dateText = "৬",
                fullDateBn = "রবিবার, ০৬/০৯/২০২৬",
                classes = listOf(
                    RoutineClassItem(
                        id = "sun1",
                        time = "07.00 PM - 08.30 PM",
                        durationText = "১ ঘন্টা ৩০ মিনিট",
                        subject = "পদার্থবিজ্ঞান ১ম পত্র",
                        chapterOrTopic = "মহাকর্ষ ও অভিকর্ষ: মুক্তিবেগ ও গ্র্যাভিটি",
                        instructor = "ড. সাজ্জাদুল করিম",
                        typeLabel = "লেকচার ক্লাস",
                        subjectColorHex = 0xFF3B82F6
                    ),
                    RoutineClassItem(
                        id = "sun2",
                        time = "08.45 PM - 10.00 PM",
                        durationText = "১ ঘন্টা ১৫ মিনিট",
                        subject = "রসায়ন ১ম পত্র",
                        chapterOrTopic = "পর্যায় সারণি ও ইলেকট্রন বিন্যাস",
                        instructor = "অধ্যাপক শওকত আলী",
                        typeLabel = "লেকচার ক্লাস",
                        subjectColorHex = 0xFF00B4D8
                    ),
                    RoutineClassItem(
                        id = "sun3",
                        time = "10.15 PM - 11.00 PM",
                        durationText = "৪৫ মিনিট",
                        subject = "উচ্চতর গণিত",
                        chapterOrTopic = "ম্যাট্রিক্স সমীকরণ সমাধান",
                        instructor = "ইঞ্জি. মোবাশ্বির",
                        typeLabel = "প্র্যাকটিস সেশন",
                        subjectColorHex = 0xFF6366F1
                    )
                ),
                exams = emptyList()
            ),
            DaySchedule(
                dayIndex = 2,
                dayNameBn = "সোমবার",
                dayShortBn = "সোম",
                dateText = "৭",
                fullDateBn = "সোমবার, ০৭/০৯/২০২৬",
                classes = listOf(
                    RoutineClassItem(
                        id = "mon1",
                        time = "07.00 PM - 08.30 PM",
                        durationText = "১ ঘন্টা ৩০ মিনিট",
                        subject = "উচ্চতর গণিত ১ম পত্র",
                        chapterOrTopic = "অন্তরীকরণ: ফাংশনের সীমা ও অবিরত",
                        instructor = "ইঞ্জি. মোবাশ্বির",
                        typeLabel = "লেকচার ক্লাস",
                        subjectColorHex = 0xFF6366F1
                    ),
                    RoutineClassItem(
                        id = "mon2",
                        time = "08.40 PM - 09.55 PM",
                        durationText = "১ ঘন্টা ১৫ মিনিট",
                        subject = "জীববিজ্ঞান ১ম পত্র",
                        chapterOrTopic = "কোষ ও এর গঠন: সাইটোপ্লাজমীয় অঙ্গাণু",
                        instructor = "ডা. তাসনিম",
                        typeLabel = "লেকচার ক্লাস",
                        subjectColorHex = 0xFF10B981
                    ),
                    RoutineClassItem(
                        id = "mon3",
                        time = "10.00 PM - 11.00 PM",
                        durationText = "১ ঘন্টা",
                        subject = "তথ্য ও যোগাযোগ প্রযুক্তি",
                        chapterOrTopic = "সংখ্যা পদ্ধতি রূপান্তর ও পরিপূরক",
                        instructor = "রাকিবুর রহমান",
                        typeLabel = "লেকচার ক্লাস",
                        subjectColorHex = 0xFF8B5CF6
                    )
                ),
                exams = emptyList()
            ),
            DaySchedule(
                dayIndex = 3,
                dayNameBn = "মঙ্গলবার",
                dayShortBn = "মঙ্গল",
                dateText = "৮",
                fullDateBn = "মঙ্গলবার, ০৮/০৯/২০২৬",
                classes = listOf(
                    RoutineClassItem(
                        id = "tue1",
                        time = "07.00 PM - 08.30 PM",
                        durationText = "১ ঘন্টা ৩০ মিনিট",
                        subject = "রসায়ন ২য় পত্র",
                        chapterOrTopic = "পরিবেশ রসায়ন: বয়েল ও চার্লসের সূত্র",
                        instructor = "অধ্যাপক শওকত আলী",
                        typeLabel = "লেকচার ক্লাস",
                        subjectColorHex = 0xFF00B4D8
                    ),
                    RoutineClassItem(
                        id = "tue2",
                        time = "08.45 PM - 10.00 PM",
                        durationText = "১ ঘন্টা ১৫ মিনিট",
                        subject = "পদার্থবিজ্ঞান ১ম পত্র",
                        chapterOrTopic = "নিউটনিয়ান বলবিদ্যা: ঘর্ষণ ও ব্যাংকিং",
                        instructor = "ড. সাজ্জাদুল করিম",
                        typeLabel = "লেকচার ক্লাস",
                        subjectColorHex = 0xFF3B82F6
                    ),
                    RoutineClassItem(
                        id = "tue3",
                        time = "10.15 PM - 11.00 PM",
                        durationText = "৪৫ মিনিট",
                        subject = "ইংরেজি",
                        chapterOrTopic = "Modifiers & Connectors Tricks",
                        instructor = "পারভীন আক্তার",
                        typeLabel = "হ্যাকস ক্লাস",
                        subjectColorHex = 0xFFEC4899
                    )
                ),
                exams = emptyList()
            ),
            // Wednesday - 9 (EXACT SCREENSHOT STATE)
            DaySchedule(
                dayIndex = 4,
                dayNameBn = "বুধবার",
                dayShortBn = "বুধ",
                dateText = "৯",
                fullDateBn = "বুধবার, ০৯/০৯/২০২৬",
                classes = listOf(
                    RoutineClassItem(
                        id = "wed1",
                        time = "07.00 PM - 08.32 PM",
                        durationText = "১ ঘন্টা ৩২ মিনিট",
                        subject = "রসায়ন ২য় পত্র",
                        chapterOrTopic = "পর্ব-১২: জৈব রসায়ন (নামকরণ + সমাণুতা)",
                        instructor = "অধ্যাপক শওকত আলী",
                        typeLabel = "লেকচার ক্লাস",
                        subjectColorHex = 0xFF00B4D8
                    ),
                    RoutineClassItem(
                        id = "wed2",
                        time = "07.00 PM - 08.30 PM",
                        durationText = "১ ঘন্টা ৩০ মিনিট",
                        subject = "পদার্থবিজ্ঞান ১ম পত্র",
                        chapterOrTopic = "MCQ Solving: ভেক্টর ও গতিবিদ্যা",
                        instructor = "ড. সাজ্জাদুল করিম",
                        typeLabel = "MCQ সলভিং",
                        subjectColorHex = 0xFF4338CA
                    ),
                    RoutineClassItem(
                        id = "wed3",
                        time = "08.45 PM - 10.00 PM",
                        durationText = "১ ঘন্টা ১৫ মিনিট",
                        subject = "উচ্চতর গণিত",
                        chapterOrTopic = "অন্তরীকরণ: স্পর্শক ও অভিলম্ব সমাধান",
                        instructor = "ইঞ্জি. মোবাশ্বির",
                        typeLabel = "লেকচার ক্লাস",
                        subjectColorHex = 0xFF0D9488
                    )
                ),
                exams = emptyList()
            ),
            DaySchedule(
                dayIndex = 5,
                dayNameBn = "বৃহস্পতিবার",
                dayShortBn = "বৃহ",
                dateText = "১০",
                fullDateBn = "বৃহস্পতিবার, ১০/০৯/২০২৬",
                classes = listOf(
                    RoutineClassItem(
                        id = "thu1",
                        time = "05.00 PM - 06.30 PM",
                        durationText = "১ ঘন্টা ৩০ মিনিট",
                        subject = "রসায়ন ১ম পত্র",
                        chapterOrTopic = "রাসায়নিক পরিবর্তন ও সাম্যাবস্থা",
                        instructor = "অধ্যাপক শওকত আলী",
                        typeLabel = "লেকচার ক্লাস",
                        subjectColorHex = 0xFF00B4D8
                    ),
                    RoutineClassItem(
                        id = "thu2",
                        time = "06.45 PM - 08.00 PM",
                        durationText = "১ ঘন্টা ১৫ মিনিট",
                        subject = "উচ্চতর গণিত ১ম পত্র",
                        chapterOrTopic = "সরলরেখা: ৩.৫ ও ৩.৬ সমাধান",
                        instructor = "ইঞ্জি. মোবাশ্বির",
                        typeLabel = "লেকচার ক্লাস",
                        subjectColorHex = 0xFF6366F1
                    ),
                    RoutineClassItem(
                        id = "thu3",
                        time = "08.15 PM - 09.30 PM",
                        durationText = "১ ঘন্টা ১৫ মিনিট",
                        subject = "পদার্থবিজ্ঞান ল্যাব",
                        chapterOrTopic = "স্লাইড ক্যালিপার্স প্র্যাকটিকাল",
                        instructor = "ল্যাব শিক্ষক",
                        typeLabel = "ল্যাব সেশন",
                        subjectColorHex = 0xFF3B82F6
                    ),
                    RoutineClassItem(
                        id = "thu4",
                        time = "09.45 PM - 10.45 PM",
                        durationText = "১ ঘন্টা",
                        subject = "ডাউট সলভিং লাইভ",
                        chapterOrTopic = "সাপ্তাহিক প্রশ্নোত্তর লাইভ",
                        instructor = "মেন্টর টিম",
                        typeLabel = "লাইভ ডাউট",
                        subjectColorHex = 0xFF10B981
                    )
                ),
                exams = listOf(
                    RoutineExamItem(
                        id = "thue1",
                        time = "10.00 PM - 11.00 PM",
                        title = "সাপ্তাহিক মেগা টেস্ট (MCQ)",
                        syllabus = "পদার্থবিজ্ঞান ও রসায়ন সমন্বিত",
                        marks = 30,
                        durationText = "৬০ মিনিট"
                    )
                )
            ),
            DaySchedule(
                dayIndex = 6,
                dayNameBn = "শুক্রবার",
                dayShortBn = "শুক্র",
                dateText = "১১",
                fullDateBn = "শুক্রবার, ১১/০৯/২০২৬",
                classes = listOf(
                    RoutineClassItem(
                        id = "fri1",
                        time = "04.00 PM - 05.30 PM",
                        durationText = "১ ঘন্টা ৩০ মিনিট",
                        subject = "স্পেশাল রিভিশন ক্লাস",
                        chapterOrTopic = "জৈব যৌগ নামকরণ রিভিশন ও প্রবলেম সলভিং",
                        instructor = "অধ্যাপক শওকত আলী",
                        typeLabel = "রিভিশন ক্লাস",
                        subjectColorHex = 0xFF00B4D8
                    )
                ),
                exams = emptyList()
            )
        )
    }

    fun getMonthlyClassList(courseTitle: String): List<MonthClassOverview> {
        return listOf(
            MonthClassOverview("০৫ সেপ্টে", "শনিবার", "সেলফ স্টাডি", "সাপ্তাহিক রিভিশন ডে", "পুরো দিন", "ছুটি"),
            MonthClassOverview("০৬ সেপ্টে", "রবিবার", "পদার্থবিজ্ঞান ১ম পত্র", "মহাকর্ষ ও অভিকর্ষ", "07.00 PM", "লাইভ ক্লাস"),
            MonthClassOverview("০৬ সেপ্টে", "রবিবার", "রসায়ন ১ম পত্র", "পর্যায় সারণি ও ইলেকট্রন", "08.45 PM", "লাইভ ক্লাস"),
            MonthClassOverview("০৭ সেপ্টে", "সোমবার", "উচ্চতর গণিত ১ম পত্র", "অন্তরীকরণ লিমিট", "07.00 PM", "লাইভ ক্লাস"),
            MonthClassOverview("০৮ সেপ্টে", "মঙ্গলবার", "রসায়ন ২য় পত্র", "পরিবেশ রসায়ন", "07.00 PM", "লাইভ ক্লাস"),
            MonthClassOverview("০৯ সেপ্টে", "বুধবার", "রসায়ন ২য় পত্র", "পর্ব-১২: জৈব রসায়ন", "07.00 PM", "লেকচার ক্লাস"),
            MonthClassOverview("০৯ সেপ্টে", "বুধবার", "পদার্থবিজ্ঞান ১ম পত্র", "MCQ Solving: ভেক্টর", "07.00 PM", "MCQ সলভিং"),
            MonthClassOverview("১০ সেপ্টে", "বৃহস্পতিবার", "রসায়ন ১ম পত্র", "রাসায়নিক সাম্যাবস্থা", "05.00 PM", "লাইভ ক্লাস"),
            MonthClassOverview("১০ সেপ্টে", "বৃহস্পতিবার", "সাপ্তাহিক মেগা টেস্ট", "পদার্থবিজ্ঞান ও রসায়ন MCQ", "10.00 PM", "পরীক্ষা", isExam = true),
            MonthClassOverview("১১ সেপ্টে", "শুক্রবার", "স্পেশাল রিভিশন", "জৈব যৌগ নামকরণ", "04.00 PM", "রিভিশন ক্লাস"),
            MonthClassOverview("১৩ সেপ্টে", "রবিবার", "উচ্চতর গণিত ২য় পত্র", "জটিল সংখ্যা আরগ্যান্ড চিত্র", "07.00 PM", "লাইভ ক্লাস"),
            MonthClassOverview("১৫ সেপ্টে", "মঙ্গলবার", "জীববিজ্ঞান ১ম পত্র", "কোষ বিভাজন ক্রসিং ওভার", "08.00 PM", "লাইভ ক্লাস"),
            MonthClassOverview("১৭ সেপ্টে", "বৃহস্পতিবার", "অধ্যায়ভিত্তিক মডেল টেস্ট", "জৈব রসায়ন CQ", "08.00 PM", "মডেল টেস্ট", isExam = true),
            MonthClassOverview("২০ সেপ্টে", "রবিবার", "তথ্য ও যোগাযোগ প্রযুক্তি", "লজিক গেট ও বুলিয়ান অ্যালজেব্রা", "06.00 PM", "লাইভ ক্লাস")
        )
    }

    val allSubjects = listOf(
        SubjectItem(
            id = "s_bn1",
            titleBn = "বাংলা ১ম পত্র",
            iconSymbol = "অ আ\nক খ",
            primaryColorHex = 0xFFDC2626,
            secondaryColorHex = 0xFF991B1B,
            progressPercent = 0
        ),
        SubjectItem(
            id = "s_bn2",
            titleBn = "বাংলা ২য় পত্র",
            iconSymbol = "অ আ\nক খ",
            primaryColorHex = 0xFFEA580C,
            secondaryColorHex = 0xFF9A3412,
            progressPercent = 0
        ),
        SubjectItem(
            id = "s_en2",
            titleBn = "ইংরেজি ২য় পত্র",
            iconSymbol = "ABC",
            primaryColorHex = 0xFF06B6D4,
            secondaryColorHex = 0xFF0E7490,
            progressPercent = 0
        ),
        SubjectItem(
            id = "s_math1",
            titleBn = "উচ্চতর গণিত ১ম",
            iconSymbol = "∫ f(x)",
            primaryColorHex = 0xFF8B5CF6,
            secondaryColorHex = 0xFF6D28D9,
            progressPercent = 0
        ),
        SubjectItem(
            id = "s_math2",
            titleBn = "উচ্চতর গণিত ২য়",
            iconSymbol = "y = x²",
            primaryColorHex = 0xFF9333EA,
            secondaryColorHex = 0xFF7E22CE,
            progressPercent = 0
        ),
        SubjectItem(
            id = "s_phy1",
            titleBn = "পদার্থবিজ্ঞান ১ম পত্র",
            iconSymbol = "🪐",
            primaryColorHex = 0xFF3B82F6,
            secondaryColorHex = 0xFF1D4ED8,
            progressPercent = 0
        ),
        SubjectItem(
            id = "s_phy2",
            titleBn = "পদার্থবিজ্ঞান ২য় পত্র",
            iconSymbol = "🪐",
            primaryColorHex = 0xFF4F46E5,
            secondaryColorHex = 0xFF3730A3,
            progressPercent = 0
        ),
        SubjectItem(
            id = "s_chem1",
            titleBn = "রসায়ন ১ম পত্র",
            iconSymbol = "⚗️",
            primaryColorHex = 0xFF0284C7,
            secondaryColorHex = 0xFF0369A1,
            progressPercent = 0
        ),
        SubjectItem(
            id = "s_chem2",
            titleBn = "রসায়ন ২য় পত্র",
            iconSymbol = "🧪",
            primaryColorHex = 0xFF0D9488,
            secondaryColorHex = 0xFF0F766E,
            progressPercent = 0
        )
    )

    fun getSubjectChapters(subjectTitle: String): List<ChapterItem> {
        return listOf(
            ChapterItem(
                id = "ch1",
                titleBn = "বায়ান্নর দিনগুলি + ফেব্রুয়ারি ১৯৬৯",
                statusBadge = "পড়ানো শেষ",
                isCompleted = true,
                classesCount = 1,
                examsCount = 1
            ),
            ChapterItem(
                id = "ch2",
                titleBn = "রেইনকোট + আমি কিংবদন্তির কথা বলছি",
                statusBadge = "পড়ানো শেষ",
                isCompleted = true,
                classesCount = 2,
                examsCount = 1
            ),
            ChapterItem(
                id = "ch3",
                titleBn = "নেকলেস + ছবি",
                statusBadge = "পড়ানো হচ্ছে",
                isCompleted = false,
                classesCount = 1,
                examsCount = 1
            ),
            ChapterItem(
                id = "ch4",
                titleBn = "গন্তব্য কাবুল + প্রত্যাবর্তনের লজ্জা",
                statusBadge = "পড়ানো শেষ",
                isCompleted = true,
                classesCount = 2,
                examsCount = 1
            )
        )
    }

    fun getChapterLectures(chapterTitle: String): List<LectureItem> {
        return listOf(
            LectureItem(
                id = "lec1",
                titleBn = "ফেব্রুয়ারি ১৯৬৯",
                typeLabel = "লেকচার ক্লাস",
                dateText = "01 September 2026",
                statusBadge = "মিসড",
                isExam = false,
                instructorName = "হিরন্ময় বাউলিয়া",
                instructorBio = "এম.এস.এস, এম.ফিল, ঢাবি '০৮\nবাংলা শিক্ষক",
                instructorExp = "১৬ বছর+ শিক্ষকতার অভিজ্ঞতা",
                instructorStudents = "১০ লক্ষ+ শিক্ষার্থী পড়িয়েছেন"
            ),
            LectureItem(
                id = "exam1",
                titleBn = "ফেব্রুয়ারি ১৯৬৯: Bangla 1st",
                typeLabel = "চ্যাপ্টার এক্সাম",
                dateText = "04 September 2026",
                statusBadge = "মিসড",
                noticeText = "এখনো এক্সাম দেওয়া যাবে",
                isExam = true
            )
        )
    }

    var currentUserProfile by mutableStateOf(
        UserProfile(
            name = "Student",
            phone = "",
            birthDate = "",
            gender = "",
            avatarUrl = "",
            studentClass = "এইচএসসি",
            group = "",
            examBatch = "",
            classShift = "",
            sscBoard = "",
            sscRoll = "",
            hscBoard = "",
            hscRoll = "",
            boardRegNumber = "",
            institutionDivision = "",
            institutionDistrict = "",
            institutionName = "",
            educationMedium = "",
            guardianName = "",
            guardianPhone = "",
            isLoggedIn = false
        )
    )
}

data class UserProfile(
    val id: String = "",
    val name: String = "Student",
    val phone: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val avatarUrl: String = "",
    val studentClass: String = "এইচএসসি",
    val group: String = "",
    val examBatch: String = "",
    val classShift: String = "",
    val sscBoard: String = "",
    val sscRoll: String = "",
    val hscBoard: String = "",
    val hscRoll: String = "",
    val boardRegNumber: String = "",
    val institutionDivision: String = "",
    val institutionDistrict: String = "",
    val institutionName: String = "",
    val educationMedium: String = "",
    val guardianName: String = "",
    val guardianPhone: String = "",
    val otherTutoringSources: List<String> = emptyList(),
    val isLoggedIn: Boolean = false
)

