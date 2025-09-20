package com.coursecampus.athleteconnect.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coursecampus.athleteconnect.data.model.Difficulty
import com.coursecampus.athleteconnect.data.model.FitnessTest
import com.coursecampus.athleteconnect.data.model.TestResult
import com.coursecampus.athleteconnect.domain.repository.TestResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestsViewModel @Inject constructor(
    private val testResultRepository: TestResultRepository
) : ViewModel() {

    private val _fitnessTests = MutableStateFlow<List<FitnessTest>>(emptyList())
    val fitnessTests: StateFlow<List<FitnessTest>> = _fitnessTests.asStateFlow()

    private val _recentResults = MutableStateFlow<List<TestResult>>(emptyList())
    val recentResults: StateFlow<List<TestResult>> = _recentResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadFitnessTests()
        loadRecentResults()
    }

    private fun loadFitnessTests() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Load from repository or use mock data
                val tests = getMockFitnessTests()
                _fitnessTests.value = tests
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadRecentResults() {
        viewModelScope.launch {
            try {
                // Load from repository
                testResultRepository.getAllTestResults().collect { results ->
                    _recentResults.value = results.take(10) // Show only recent 10
                }
            } catch (e: Exception) {
                // Handle error - fallback to mock data
                val results = getMockTestResults()
                _recentResults.value = results
            }
        }
    }

    fun startTest(test: FitnessTest) {
        // Navigate to camera screen or test execution
        // This will be handled by the UI layer
    }

    fun saveTestResult(result: TestResult) {
        viewModelScope.launch {
            try {
                // Save to repository
                testResultRepository.saveTestResult(result)

                // Update local state
                val currentResults = _recentResults.value.toMutableList()
                currentResults.add(0, result) // Add to beginning
                _recentResults.value = currentResults.take(10) // Keep only recent 10
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun getMockFitnessTests(): List<FitnessTest> {
        return listOf(
            FitnessTest(
                id = "1",
                name = "40 Yard Dash",
                description = "Measure your sprint speed over 40 yards",
                category = "Speed",
                instructions = listOf(
                    "Stand at the starting line",
                    "Sprint as fast as possible for 40 yards",
                    "Record your time"
                ),
                duration = 10,
                difficulty = Difficulty.INTERMEDIATE
            ),
            FitnessTest(
                id = "2",
                name = "Vertical Jump",
                description = "Test your explosive leg power",
                category = "Power",
                instructions = listOf(
                    "Stand with feet shoulder-width apart",
                    "Jump as high as possible",
                    "Reach for the highest point"
                ),
                duration = 5,
                difficulty = Difficulty.BEGINNER
            ),
            FitnessTest(
                id = "3",
                name = "Push-ups",
                description = "Test upper body strength and endurance",
                category = "Strength",
                instructions = listOf(
                    "Start in plank position",
                    "Lower your body until chest nearly touches floor",
                    "Push back up to starting position"
                ),
                duration = 5,
                difficulty = Difficulty.BEGINNER
            ),
            FitnessTest(
                id = "4",
                name = "Plank Hold",
                description = "Test core strength and stability",
                category = "Core",
                instructions = listOf(
                    "Start in plank position",
                    "Hold position with straight body",
                    "Keep core engaged throughout"
                ),
                duration = 3,
                difficulty = Difficulty.INTERMEDIATE
            ),
            FitnessTest(
                id = "5",
                name = "Squat Test",
                description = "Test lower body strength and endurance",
                category = "Strength",
                instructions = listOf(
                    "Stand with feet shoulder-width apart",
                    "Lower into squat position",
                    "Return to standing position"
                ),
                duration = 5,
                difficulty = Difficulty.BEGINNER
            ),
            FitnessTest(
                id = "6",
                name = "Agility Ladder",
                description = "Test foot speed and coordination",
                category = "Agility",
                instructions = listOf(
                    "Set up agility ladder",
                    "Perform various footwork patterns",
                    "Focus on speed and precision"
                ),
                duration = 10,
                difficulty = Difficulty.ADVANCED
            )
        )
    }

    private fun getMockTestResults(): List<TestResult> {
        return listOf(
            TestResult(
                id = "1",
                testName = "40 Yard Dash",
                score = 4.8,
                unit = "seconds",
                date = "2024-01-15",
                percentile = 85,
                category = "Speed",
                isPersonalBest = true
            ),
            TestResult(
                id = "2",
                testName = "Vertical Jump",
                score = 28.5,
                unit = "inches",
                date = "2024-01-14",
                percentile = 72,
                category = "Power"
            ),
            TestResult(
                id = "3",
                testName = "Push-ups",
                score = 45.0,
                unit = "reps",
                date = "2024-01-13",
                percentile = 68,
                category = "Strength"
            )
        )
    }
}
