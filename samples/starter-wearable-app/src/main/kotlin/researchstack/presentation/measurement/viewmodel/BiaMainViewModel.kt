package researchstack.presentation.measurement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import researchstack.domain.usecase.TrackMeasureTimeUseCase
import researchstack.presentation.main.screen.HomeScreenItem
import researchstack.presentation.main.screen.getItemPrefKey
import researchstack.presentation.main.viewmodel.getTimeString
import javax.inject.Inject

@HiltViewModel
class BiaMainViewModel @Inject constructor(
    application: Application,
    trackMeasureTimeUseCase: TrackMeasureTimeUseCase
) : AndroidViewModel(application) {
    private val _lastMeasurementTime = MutableLiveData<String>()
    val lastMeasurementTime: LiveData<String>
        get() = _lastMeasurementTime

    init {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                viewModelScope.launch {
                    trackMeasureTimeUseCase(HomeScreenItem.BODY_COMPOSITION.getItemPrefKey()).collect {
                        _lastMeasurementTime.postValue(it.getTimeString())
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}
