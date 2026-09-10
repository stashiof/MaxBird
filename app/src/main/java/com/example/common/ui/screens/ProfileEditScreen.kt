package com.example.common.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.model.MockStudyData
import com.example.common.model.UserProfile

/**
 * Premium 3-Step Profile Edit Screen matching the user's uploaded screenshots
 * (Screenshot 1 & 2: ব্যক্তিগত তথ্য / Personal Info,
 *  Screenshot 3 & 4: স্কুল/কলেজের তথ্য / Institutional Info,
 *  Screenshot 5: অভিভাবকের তথ্য / Guardian Info)
 *
 * Upgraded with:
 * - Clean modern progress indicator bar
 * - Interactive inputs with floating card containers
 * - Modern avatar selector with camera badge
 * - Custom illustrated student gender selector
 * - Dropdown pickers for Board, Shift, Division, District & Medium
 * - Real-time save back to MockStudyData.currentUserProfile
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Current step: 1 = Personal Info, 2 = School/College Info, 3 = Guardian Info
    var currentStep by remember { mutableIntStateOf(1) }

    // State initialized from MockStudyData
    var profile by remember { mutableStateOf(MockStudyData.currentUserProfile) }

    // Feedback message
    var showSavedMessage by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    if (currentStep > 1) {
                                        currentStep -= 1
                                    } else {
                                        onBackClick()
                                    }
                                },
                                modifier = Modifier.testTag("edit_profile_back")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "ফিরে যান",
                                    tint = Color(0xFF1E293B)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when (currentStep) {
                                    1 -> "ব্যক্তিগত তথ্য যোগ করো"
                                    2 -> "স্কুল/কলেজের তথ্য যোগ করো"
                                    else -> "অভিভাবকের তথ্য যোগ করো"
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        // Step Indicator Text e.g. "ধাপ ১/৩"
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFEFF6FF)
                        ) {
                            Text(
                                text = when (currentStep) {
                                    1 -> "ধাপ ১/৩"
                                    2 -> "ধাপ ২/৩"
                                    else -> "ধাপ ৩/৩"
                                },
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF2563EB),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3-Segment Progress Indicator Bar (matches screenshots 1, 3, 5)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (i in 1..3) {
                            val isActive = i <= currentStep
                            val barColor = if (isActive) Color(0xFF3B82F6) else Color(0xFFE2E8F0)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(barColor)
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Persistent Bottom Actions: [ফিরে যাও] and [এগিয়ে যাও / সম্পূর্ণ করো]
            Surface(
                color = Color.White,
                shadowElevation = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Back Button
                    OutlinedButton(
                        onClick = {
                            if (currentStep > 1) {
                                currentStep -= 1
                            } else {
                                onBackClick()
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.5.dp, Color(0xFF2563EB)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF2563EB)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("step_back_button")
                    ) {
                        Text(
                            text = "ফিরে যাও",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Next / Complete Button
                    Button(
                        onClick = {
                            if (currentStep < 3) {
                                currentStep += 1
                            } else {
                                // Save and finish
                                MockStudyData.currentUserProfile = profile
                                onSaveSuccess()
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E3A8A)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("step_next_button")
                    ) {
                        Text(
                            text = if (currentStep < 3) "এগিয়ে যাও" else "সংরক্ষণ করো",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFFF8FAFC),
        modifier = modifier
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
            },
            label = "step_content_transition",
            modifier = Modifier.padding(innerPadding)
        ) { step ->
            when (step) {
                1 -> Step1PersonalInfo(
                    profile = profile,
                    onProfileChange = { profile = it }
                )
                2 -> Step2InstitutionalInfo(
                    profile = profile,
                    onProfileChange = { profile = it }
                )
                3 -> Step3GuardianInfo(
                    profile = profile,
                    onProfileChange = { profile = it }
                )
            }
        }
    }
}

/**
 * Step 1: ব্যক্তিগত তথ্য (Personal Information)
 * Matches Screenshots 1 & 2
 */
@Composable
private fun Step1PersonalInfo(
    profile: UserProfile,
    onProfileChange: (UserProfile) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            // Profile Picture with Camera Action
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color(0xFF2563EB), CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF1E293B), Color(0xFF3B82F6))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "প্রোফাইল পিকচার",
                            tint = Color.White,
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    // Camera Icon Badge
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB))
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "ছবি আপলোড",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "তোমার প্রোফাইল পিকচার দাও",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A)
                )
            }
        }

        // Your Name Field
        item {
            Column {
                Row {
                    Text(
                        text = "তোমার নাম",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(text = " *", color = Color.Red, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = profile.name,
                    onValueChange = { onProfileChange(profile.copy(name = it)) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2563EB),
                        unfocusedBorderColor = Color(0xFFCBD5E1),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_profile_name")
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Warning note: ৬০ দিনের মধ্যে নাম চেঞ্জ করতে পারবে না।
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "৬০ দিনের মধ্যে নাম চেঞ্জ করতে পারবে না।",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }

        // Mobile Number Field
        item {
            Column {
                Text(
                    text = "মোবাইল নম্বর",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = profile.phone,
                    onValueChange = { onProfileChange(profile.copy(phone = it)) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2563EB),
                        unfocusedBorderColor = Color(0xFFCBD5E1),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_profile_phone")
                )
            }
        }

        // Birth Date Field
        item {
            Column {
                Row {
                    Text(
                        text = "জন্ম তারিখ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(text = " *", color = Color.Red, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = profile.birthDate,
                    onValueChange = { onProfileChange(profile.copy(birthDate = it)) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "তারিখ বাছাই করুন",
                            tint = Color(0xFF64748B)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2563EB),
                        unfocusedBorderColor = Color(0xFFCBD5E1),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_profile_dob")
                )
            }
        }

        // Gender Selector Cards (ছাত্র / ছাত্রী) - Matches Screenshots 1 & 2
        item {
            Column {
                Row {
                    Text(
                        text = "তুমি একজন",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(text = " *", color = Color.Red, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // ছাত্র Card
                    GenderSelectionCard(
                        title = "ছাত্র",
                        isSelected = profile.gender == "ছাত্র",
                        isMale = true,
                        onClick = { onProfileChange(profile.copy(gender = "ছাত্র")) },
                        modifier = Modifier.weight(1f)
                    )

                    // ছাত্রী Card
                    GenderSelectionCard(
                        title = "ছাত্রী",
                        isSelected = profile.gender == "ছাত্রী",
                        isMale = false,
                        onClick = { onProfileChange(profile.copy(gender = "ছাত্রী")) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Step 2: স্কুল/কলেজের তথ্য (Institutional Information)
 * Matches Screenshots 3 & 4
 */
@Composable
private fun Step2InstitutionalInfo(
    profile: UserProfile,
    onProfileChange: (UserProfile) -> Unit
) {
    var expandedBoard by remember { mutableStateOf(false) }
    var expandedShift by remember { mutableStateOf(false) }
    var expandedDivision by remember { mutableStateOf(false) }
    var expandedDistrict by remember { mutableStateOf(false) }
    var expandedMedium by remember { mutableStateOf(false) }

    val boardList = listOf("Jessore", "Dhaka", "Chattogram", "Rajshahi", "Dinajpur", "Cumilla", "Barishal", "Sylhet", "Mymensingh", "Madrasah", "Technical")
    val shiftList = listOf("প্রযোজ্য নয়", "প্রভাতী (সকাল)", "দিবা (দুপুর)", "সান্ধ্য")
    val divisionList = listOf("Chattogram", "Dhaka", "Rajshahi", "Khulna", "Barishal", "Sylhet", "Rangpur", "Mymensingh")
    val districtList = listOf("Khagrachari", "Chattogram", "Cox's Bazar", "Rangamati", "Bandarban", "Feni", "Cumilla", "Noakhali")
    val mediumList = listOf("কোনটাই নয়", "বাংলা ভার্সন", "ইংরেজি ভার্সন", "ইংলিশ মিডিয়াম", "মাদ্রাসা")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            // Class and Group Row (side by side, disabled or read-only feel like screenshot)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ক্লাস",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = profile.studentClass,
                        onValueChange = { onProfileChange(profile.copy(studentClass = it)) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF1F5F9),
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "গ্রুপ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = profile.group,
                        onValueChange = { onProfileChange(profile.copy(group = it)) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF1F5F9),
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Exam Batch and Class Shift
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "পরীক্ষার ব্যাচ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = profile.examBatch,
                        onValueChange = { onProfileChange(profile.copy(examBatch = it)) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF1F5F9),
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row {
                        Text(
                            text = "ক্লাসের শিফট",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(text = " *", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Box {
                        OutlinedTextField(
                            value = profile.classShift,
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            trailingIcon = {
                                IconButton(onClick = { expandedShift = true }) {
                                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedShift = true }
                        )

                        DropdownMenu(
                            expanded = expandedShift,
                            onDismissRequest = { expandedShift = false }
                        ) {
                            shiftList.forEach { shift ->
                                DropdownMenuItem(
                                    text = { Text(shift) },
                                    onClick = {
                                        onProfileChange(profile.copy(classShift = shift))
                                        expandedShift = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // SSC Board Dropdown
        item {
            Column {
                Row {
                    Text(
                        text = "এসএসসি বোর্ড",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(text = " *", color = Color.Red, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Box {
                    OutlinedTextField(
                        value = profile.sscBoard,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        trailingIcon = {
                            IconButton(onClick = { expandedBoard = true }) {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedBoard = true }
                    )

                    DropdownMenu(
                        expanded = expandedBoard,
                        onDismissRequest = { expandedBoard = false }
                    ) {
                        boardList.forEach { board ->
                            DropdownMenuItem(
                                text = { Text(board) },
                                onClick = {
                                    onProfileChange(profile.copy(sscBoard = board))
                                    expandedBoard = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // SSC Roll Number
        item {
            Column {
                Row {
                    Text(
                        text = "এসএসসি বোর্ড রোল নাম্বার",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(text = " *", color = Color.Red, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = profile.sscRoll,
                    onValueChange = { onProfileChange(profile.copy(sscRoll = it)) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_profile_ssc_roll")
                )
            }
        }

        // Division Selector
        item {
            Column {
                Row {
                    Text(
                        text = "প্রতিষ্ঠানের বিভাগ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(text = " *", color = Color.Red, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Box {
                    OutlinedTextField(
                        value = profile.institutionDivision,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        trailingIcon = {
                            IconButton(onClick = { expandedDivision = true }) {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedDivision = true }
                    )

                    DropdownMenu(
                        expanded = expandedDivision,
                        onDismissRequest = { expandedDivision = false }
                    ) {
                        divisionList.forEach { div ->
                            DropdownMenuItem(
                                text = { Text(div) },
                                onClick = {
                                    onProfileChange(profile.copy(institutionDivision = div))
                                    expandedDivision = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // District Selector
        item {
            Column {
                Row {
                    Text(
                        text = "প্রতিষ্ঠানের জেলা",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(text = " *", color = Color.Red, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Box {
                    OutlinedTextField(
                        value = profile.institutionDistrict,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        trailingIcon = {
                            IconButton(onClick = { expandedDistrict = true }) {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedDistrict = true }
                    )

                    DropdownMenu(
                        expanded = expandedDistrict,
                        onDismissRequest = { expandedDistrict = false }
                    ) {
                        districtList.forEach { dist ->
                            DropdownMenuItem(
                                text = { Text(dist) },
                                onClick = {
                                    onProfileChange(profile.copy(institutionDistrict = dist))
                                    expandedDistrict = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Institution Name with Search Icon
        item {
            Column {
                Text(
                    text = "প্রতিষ্ঠানের নাম",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = profile.institutionName,
                    onValueChange = { onProfileChange(profile.copy(institutionName = it)) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "খুঁজুন",
                            tint = Color(0xFF94A3B8)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_profile_institution_name")
                )
            }
        }

        // Education Medium Dropdown
        item {
            Column {
                Row {
                    Text(
                        text = "অন্যান্য শিক্ষা মাধ্যম",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(text = " *", color = Color.Red, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Box {
                    OutlinedTextField(
                        value = profile.educationMedium,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        trailingIcon = {
                            IconButton(onClick = { expandedMedium = true }) {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedMedium = true }
                    )

                    DropdownMenu(
                        expanded = expandedMedium,
                        onDismissRequest = { expandedMedium = false }
                    ) {
                        mediumList.forEach { med ->
                            DropdownMenuItem(
                                text = { Text(med) },
                                onClick = {
                                    onProfileChange(profile.copy(educationMedium = med))
                                    expandedMedium = false
                                }
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Step 3: অভিভাবকের তথ্য (Guardian Information)
 * Matches Screenshot 5
 */
@Composable
private fun Step3GuardianInfo(
    profile: UserProfile,
    onProfileChange: (UserProfile) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(14.dp))

            // Guardian Header Badge
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFEFF6FF),
                border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "অভিভাবক ভেরিফিকেশন তথ্য",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A)
                        )
                        Text(
                            text = "জরুরি নোটিফিকেশন ও অগ্রগতি জানতে নম্বর প্রদান করুন",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }

        // Guardian Name
        item {
            Column {
                Row {
                    Text(
                        text = "অভিভাবকের নাম",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(text = " *", color = Color.Red, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = profile.guardianName,
                    onValueChange = { onProfileChange(profile.copy(guardianName = it)) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_guardian_name")
                )
            }
        }

        // Guardian Mobile Number
        item {
            Column {
                Row {
                    Text(
                        text = "অভিভাবকের মোবাইল নাম্বার",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(text = " *", color = Color.Red, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = profile.guardianPhone,
                    onValueChange = { onProfileChange(profile.copy(guardianPhone = it)) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_guardian_phone")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "তোমার এই অভিভাবকের মোবাইল নাম্বার ভেরিফিকেশন করতে হবে",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 16.sp
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Custom Illustrated Gender Selection Card (matching Screenshots 1 & 2)
 */
@Composable
private fun GenderSelectionCard(
    title: String,
    isSelected: Boolean,
    isMale: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) Color(0xFF1E3A8A) else Color(0xFFE2E8F0)
    val cardBg = if (isSelected) Color(0xFF1E3A8A).copy(alpha = 0.05f) else Color.White

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = cardBg,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag("gender_card_$title")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Illustrated Avatar Box
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(
                        if (isMale) Color(0xFFDBEAFE) else Color(0xFFFCE7F3)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isMale) Icons.Default.Male else Icons.Default.Female,
                    contentDescription = title,
                    tint = if (isMale) Color(0xFF2563EB) else Color(0xFFEC4899),
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color(0xFF1E3A8A) else Color(0xFF475569)
                )

                // Radio / Check indicator
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) Color(0xFF10B981) else Color(0xFFE2E8F0)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}
