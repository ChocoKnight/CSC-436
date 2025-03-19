package com.zybooks.studyhelper.ui.camera

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LightingColorFilter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.zybooks.studyhelper.TransactionHelperApplication
import com.zybooks.studyhelper.data.image.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CameraViewModel(private val imageRepo: ImageRepository) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TransactionHelperApplication)
                CameraViewModel(application.imageRepository)
            }
        }
    }

    private val _uiState = MutableStateFlow(CameraScreenUiState())
    val uiState: StateFlow<CameraScreenUiState> = _uiState

    fun takePhoto(): Uri {
        _uiState.update {
            it.copy(
                photoVisible = false,
                photoSaved = true,
                photoUri = imageRepo.createPhotoFile()
            )
        }

        return _uiState.value.photoUri
    }

    fun photoTaken() {
        _uiState.update {
            it.copy(
                photoVisible = true,
                photoSaved = false
            )
        }
    }

    fun changeBrightness(brightness: Float) {
        _uiState.update {
            it.copy(
                brightness = brightness,
                colorFilter = imageRepo.createColorFilter(brightness),
                photoSaved = false
            )
        }
    }

    suspend fun savePhoto() {
        imageRepo.saveAlteredPhoto(uiState.value.colorFilter)
        _uiState.update {
            it.copy(photoSaved = true)
        }
    }
}

data class CameraScreenUiState(
    val photoUri: Uri = Uri.EMPTY,
    val brightness: Float = 100f,
    val colorFilter: LightingColorFilter = LightingColorFilter(Color.White, Color.Black),
    val photoVisible: Boolean = false,
    val photoSaved: Boolean = true
)