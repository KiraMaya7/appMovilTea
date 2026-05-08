package com.example.apptea.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.apptea.ui.screens.PinScreen
import com.example.apptea.ui.screens.WelcomeScreen
import com.example.apptea.ui.screens.tutor.CategoryDetailScreen
import com.example.apptea.ui.screens.tutor.TutorPanelScreen

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Pin : Screen("pin")
    object TutorPanel : Screen("tutor")
    data class CategoryDetail(val categoryId: String, val categoryName: String) : Screen("category_detail/$categoryId/$categoryName")
}

@Composable
fun rememberAppState(
    onBack: () -> Unit = {}
) = remember {
    AppState(onBack)
}

class AppState(val onBack: () -> Unit) {
    var currentScreen by mutableStateOf<Screen>(Screen.Welcome)
    var selectedCategoryId by mutableStateOf("")
    var selectedCategoryName by mutableStateOf("")
    var userRole by mutableStateOf("tutor") // "tutor" o "infante"

    fun navigateTo(screen: Screen, role: String? = null) {
        if (role != null) userRole = role
        when (screen) {
            is Screen.CategoryDetail -> {
                selectedCategoryId = screen.categoryId
                selectedCategoryName = screen.categoryName
            }
            else -> {}
        }
        currentScreen = screen
    }

    fun goBack() {
        when (currentScreen) {
            Screen.Welcome -> onBack()
            Screen.Pin -> currentScreen = Screen.Welcome
            Screen.TutorPanel -> currentScreen = Screen.Pin
            is Screen.CategoryDetail -> currentScreen = if (userRole == "tutor") Screen.TutorPanel else Screen.Welcome
            else -> {}
        }
    }
}

@Composable
fun AppNavigation(appState: AppState) {
    when (appState.currentScreen) {
        Screen.Welcome -> WelcomeScreen(
            onTutorClick = { appState.navigateTo(Screen.Pin, role = "tutor") },
            onNinoClick = { 
                // Para el niño, podríamos ir directo a las categorías o a un panel simplificado
                // Suponiendo que va al TutorPanel pero con rol "infante" para ver categorías
                appState.navigateTo(Screen.TutorPanel, role = "infante") 
            }
        )
        Screen.Pin -> PinScreen(
            onPinSuccess = { appState.navigateTo(Screen.TutorPanel) },
            onBack = { appState.goBack() }
        )
        Screen.TutorPanel -> TutorPanelScreen(
            onBack = { appState.goBack() },
            onNavigateToCategory = { categoryId, categoryName ->
                appState.navigateTo(Screen.CategoryDetail(categoryId, categoryName))
            }
        )
        is Screen.CategoryDetail -> {
            val screen = appState.currentScreen as Screen.CategoryDetail
            CategoryDetailScreen(
                categoryId = screen.categoryId,
                categoryName = screen.categoryName,
                userRole = appState.userRole,
                onBack = { appState.goBack() }
            )
        }
    }
}
