package com.zybooks.countdowntimer.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
//   private var timerJob: Job? = null
//
//   // Values selected in time picker
//   var selectedHour by mutableIntStateOf(0)
//      private set
//   var selectedMinute by mutableIntStateOf(0)
//      private set
//   var selectedSecond by mutableIntStateOf(0)
//      private set
//
//   // Total milliseconds when timer starts
//   var totalMillis by mutableLongStateOf(0L)
//      private set
//
//   // Time that remains
//   var remainingMillis by mutableLongStateOf(0L)
//      private set
//
//   // Timer's running status
//   var isRunning by mutableStateOf(false)
//      private set
//
//   fun selectTime(hour: Int, min: Int, sec: Int) {
//      selectedHour = hour
//      selectedMinute = min
//      selectedSecond = sec
//   }

//   fun startTimer() {
//      // Convert hours, minutes, and seconds to milliseconds
//      totalMillis = (selectedHour * 60 * 60 + selectedMinute * 60 + selectedSecond) * 1000L
//
//      // Start coroutine that makes the timer count down
//      if (totalMillis > 0) {
//         isRunning = true
//         remainingMillis = totalMillis
//
//         timerJob = viewModelScope.launch {
//            while (remainingMillis > 0) {
//               delay(1000)
//               remainingMillis -= 1000
//            }
//
//            isRunning = false
//         }
//      }
//   }
//
//   fun cancelTimer() {
//      if (isRunning) {
//         timerJob?.cancel()
//         isRunning = false
//         remainingMillis = 0
//      }
//   }

   private val _uiState = MutableStateFlow(TimerUiState())
   val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

   private var timerJob: Job? = null

   fun selectTime(hour: Int, min: Int, sec: Int) {
      _uiState.update {
         it.copy(
            selectedHour = hour,
            selectedMinute = min,
            selectedSecond = sec
         )
      }
   }

   fun startTimer() {
      // Convert hours, minutes, and seconds to milliseconds
      val totalMillis = (_uiState.value.selectedHour * 60 * 60 +
              _uiState.value.selectedMinute * 60 +
              _uiState.value.selectedSecond) * 1000L

      if (totalMillis > 0) {
         _uiState.update {
            it.copy(
               totalMillis = totalMillis,
               remainingMillis = totalMillis,
               isRunning = true
            )
         }

         // Start coroutine that makes the timer count down
         timerJob = viewModelScope.launch {
            while (_uiState.value.remainingMillis > 0) {
               delay(1000)
               _uiState.update {
                  it.copy(remainingMillis = it.remainingMillis - 1000)
               }
            }

            _uiState.update {
               it.copy(isRunning = false)
            }
         }
      }
   }

   fun cancelTimer() {
      if (_uiState.value.isRunning) {
         timerJob?.cancel()
         _uiState.update {
            it.copy(
               isRunning = false,
               remainingMillis = 0
            )
         }
      }
   }

   override fun onCleared() {
      super.onCleared()
      timerJob?.cancel()
   }
}