package com.example.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.network.CourseContentUiState
import com.example.common.network.GqlChapterItem
import com.example.common.network.GqlLessonItem
import com.example.common.network.GqlResourceAttachment
import com.example.common.network.GraphQLCourseService
import com.example.common.repository.CourseRepository
import com.example.common.repository.CourseRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourseViewModel(
    private val repository: CourseRepository = CourseRepositoryImpl.shared
) : ViewModel() {

    private val _chaptersState = MutableStateFlow<CourseContentUiState<List<GqlChapterItem>>>(CourseContentUiState.Loading)
    val chaptersState: StateFlow<CourseContentUiState<List<GqlChapterItem>>> = _chaptersState.asStateFlow()

    private val _lessonsState = MutableStateFlow<CourseContentUiState<List<GqlLessonItem>>>(CourseContentUiState.Loading)
    val lessonsState: StateFlow<CourseContentUiState<List<GqlLessonItem>>> = _lessonsState.asStateFlow()

    private val _attachmentsState = MutableStateFlow<CourseContentUiState<List<GqlResourceAttachment>>>(CourseContentUiState.Loading)
    val attachmentsState: StateFlow<CourseContentUiState<List<GqlResourceAttachment>>> = _attachmentsState.asStateFlow()

    init {
        loadChapters()
    }

    fun loadChapters(
        programId: String = GraphQLCourseService.DEFAULT_PROGRAM_ID,
        phaseId: String = GraphQLCourseService.DEFAULT_PHASE_ID,
        subjectId: String = GraphQLCourseService.DEFAULT_SUBJECT_ID
    ) {
        viewModelScope.launch {
            repository.getChapters(programId, phaseId, subjectId).collect {
                _chaptersState.value = it
            }
        }
    }

    fun loadChapterDetails(
        chapterId: String,
        programId: String = GraphQLCourseService.DEFAULT_PROGRAM_ID,
        phaseId: String = GraphQLCourseService.DEFAULT_PHASE_ID,
        subjectId: String = GraphQLCourseService.DEFAULT_SUBJECT_ID
    ) {
        viewModelScope.launch {
            repository.getChapterLessons(chapterId, programId, phaseId).collect {
                _lessonsState.value = it
            }
        }
        viewModelScope.launch {
            repository.getChapterAttachments(chapterId, subjectId, programId, phaseId).collect {
                _attachmentsState.value = it
            }
        }
    }
}
