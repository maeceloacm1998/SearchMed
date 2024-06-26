package com.app.home.onboarding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import com.app.home.MainCoroutineRule
import com.app.home.awaitExecuteCoroutines
import com.app.home.feature.locationpermission.domain.GetLocationActiveUseCase
import com.app.home.feature.onboarding.data.models.OnboardingStepsType.FINISH
import com.app.home.feature.onboarding.data.models.OnboardingStepsType.INTRODUCTION
import com.app.home.feature.onboarding.data.models.OnboardingStepsType.WELCOME
import com.app.home.feature.onboarding.domain.UpdateOnboardingShowOnboardingUseCase
import com.app.home.feature.onboarding.ui.OnboardingUiState
import com.app.home.feature.onboarding.ui.OnboardingViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val getHomeLocationActiveUseCase: GetLocationActiveUseCase = mockk()
    private val updateShowOnboardingUseCase: UpdateOnboardingShowOnboardingUseCase = mockk()
    private val getOnboardingShowOnboardingUseCase: GetLocationActiveUseCase = mockk()
    private lateinit var onboardingViewModel: OnboardingViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        onboardingViewModel =
            OnboardingViewModel(getHomeLocationActiveUseCase, updateShowOnboardingUseCase)
        coEvery { getOnboardingShowOnboardingUseCase() } returns true
        coEvery { getHomeLocationActiveUseCase() } returns true
    }

    @Test
    fun `when called in the next step and it is not the final screen, verify if the steps value is different`() =
        runTest {
            val navigation = mockk<NavController>(relaxed = true)
            onboardingViewModel.onNextStep(steps = WELCOME, navigation = navigation)
                .awaitExecuteCoroutines(this)

            val uiState = onboardingViewModel.uiState.value as OnboardingUiState.Data
            assertTrue(uiState.steps == INTRODUCTION)
        }

    @Test
    fun `when called in the next step and it is the final screen, verify if called update show onboarding tag`() =
        runTest {
            val navigation = mockk<NavController>(relaxed = true)
            coEvery { updateShowOnboardingUseCase() } just Runs

            onboardingViewModel.onNextStep(steps = FINISH, navigation = navigation)

            coVerify { updateShowOnboardingUseCase() }
        }

    @Test
    fun `when called in the after step, verify if the steps value is different`() = runTest {
        onboardingViewModel.onRemoveNewStep(steps = INTRODUCTION).awaitExecuteCoroutines(this)

        val uiState = onboardingViewModel.uiState.value as OnboardingUiState.Data
        assertTrue(uiState.steps == WELCOME)
    }
}