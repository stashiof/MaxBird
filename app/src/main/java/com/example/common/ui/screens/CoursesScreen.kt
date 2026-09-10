package com.example.common.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Screen matching Screenshot 1 (Tab: কোর্স - Courses Screen)
 */
@Composable
fun CoursesScreen(
    onCourseClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFD))
            .statusBarsPadding()
    ) {
        // Top App Bar: Back arrow + Center title "কোর্স"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .testTag("courses_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "ফিরে যান",
                    tint = Color(0xFF0F172A)
                )
            }

            Text(
                text = "কোর্স",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Heading: আমার কোর্স
            item {
                Text(
                    text = "আমার কোর্স",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // Card 1: HSC '27 বিজ্ঞান - ২য় বর্ষ প্রস্তুতি (matching Screenshot 1)
            item {
                EnrolledCourseCard(
                    title = "HSC '27 বিজ্ঞান - ২য় বর্ষ প্রস্তুতি",
                    programTag = "ACADEMIC\nPROGRAM",
                    classTag = "HSC 27\nScience",
                    gradientColors = listOf(
                        Color(0xFF881337), // Crimson
                        Color(0xFF4C0519),
                        Color(0xFF0F172A)
                    ),
                    onContinueClick = { onCourseClick("HSC '27 বিজ্ঞান - ২য় বর্ষ প্রস্তুতি") }
                )
            }

            // Card 2: HSC '27 অ্যাডভান্সড আইসিটি (matching Screenshot 1)
            item {
                EnrolledCourseCard(
                    title = "HSC '27 অ্যাডভান্সড আইসিটি",
                    programTag = "HSC 27",
                    classTag = "ADVANCED\nICT",
                    gradientColors = listOf(
                        Color(0xFF0E7490), // Cyan / Teal
                        Color(0xFF155E75),
                        Color(0xFF0F172A)
                    ),
                    onContinueClick = { onCourseClick("HSC '27 অ্যাডভান্সড আইসিটি") }
                )
            }

            // Card 3: HSC '27 গণিত ও পদার্থবিজ্ঞান
            item {
                EnrolledCourseCard(
                    title = "HSC '27 উচ্চতর গণিত ও পদার্থবিজ্ঞান",
                    programTag = "SPECIAL BATCH",
                    classTag = "MATH & PHYSICS\nMASTERY",
                    gradientColors = listOf(
                        Color(0xFF4338CA), // Indigo
                        Color(0xFF312E81),
                        Color(0xFF0F172A)
                    ),
                    onContinueClick = { onCourseClick("HSC '27 উচ্চতর গণিত ও পদার্থবিজ্ঞান") }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

/**
 * Enrolled Course Card matching Screenshot 1
 */
@Composable
private fun EnrolledCourseCard(
    title: String,
    programTag: String,
    classTag: String,
    gradientColors: List<Color>,
    onContinueClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .height(235.dp)
            .clip(RoundedCornerShape(22.dp))
            .testTag("course_card_$title")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradientColors))
                .padding(18.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Row: "ভর্তি হয়েছো" badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFDCFCE7),
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = "ভর্তি হয়েছো",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                // Center Graphic / Badging
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E3A8A).copy(alpha = 0.85f),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF60A5FA).copy(alpha = 0.4f))
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = programTag,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFBBF24),
                                lineHeight = 12.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = classTag,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                lineHeight = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                // Bottom Outlined Button: "শেখা চালিয়ে যাও"
                OutlinedButton(
                    onClick = onContinueClick,
                    shape = RoundedCornerShape(14.dp),
                    border = ButtonDefaults.outlinedButtonBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color.White)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("continue_learning_button")
                ) {
                    Text(
                        text = "শেখা চালিয়ে যাও",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
