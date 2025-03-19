package com.zybooks.studyhelper.ui.camera

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.zybooks.studyhelper.R.drawable.camera
import com.zybooks.studyhelper.R.drawable.save
import com.zybooks.studyhelper.ui.BottomNavigationBar
import com.zybooks.studyhelper.ui.TopBar
import kotlinx.coroutines.launch


@Composable
fun CameraScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CameraViewModel = viewModel(
        factory = CameraViewModel.Factory
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.photoTaken()
        }
    }

    val onTakePhoto = {
        val photoUri = viewModel.takePhoto()
        cameraLauncher.launch(photoUri)
    }

    val onSavePhoto: () -> Unit = {
        coroutineScope.launch {
            viewModel.savePhoto()
        }
    }


    Scaffold(
        topBar = {
            TopBar(
                title = "Camera",
                navController = navController
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) {
        innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PhotoActions(
                onTakePhoto = onTakePhoto,
                onSavePhoto = onSavePhoto,
                isPhotoSaved = uiState.photoSaved
            )

            if (uiState.photoVisible) {
                PhotoScreen(
                    photoUri = uiState.photoUri,
                    colorFilter = uiState.colorFilter,
                    brightness = uiState.brightness,
                    onBrightnessChange = {
                        viewModel.changeBrightness(it)
                    }
                )
            }
        }
    }
}

@Composable
fun PhotoScreen(
    photoUri: Uri,
    colorFilter: ColorFilter,
    brightness: Float,
    onBrightnessChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        AsyncImage(
            model = photoUri,
            contentDescription = null,
            modifier = Modifier.fillMaxHeight(0.9f),
            colorFilter = colorFilter
        )
    }
}

@Composable
fun PhotoActions(
    onTakePhoto: () -> Unit,
    onSavePhoto: () -> Unit,
    isPhotoSaved: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column (
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            IconButton(onClick = onTakePhoto) {
                Icon(
                    painter = painterResource(camera),
                    contentDescription = "Take Photo"
                )
            }
            Text(
                text = "Take Photo"
            )
        }

        Column (
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            IconButton(
                onClick = onSavePhoto,
                enabled = !isPhotoSaved
            ) {
                Icon(
                    painter = painterResource(save),
                    contentDescription = "Save"
                )
            }
            Text(
                text = "Save Photo"
            )
        }

    }
}
