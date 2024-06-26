package com.app.home.feature.home.domain

import com.app.home.feature.home.data.HomeRepository
import com.app.home.feature.locationpermission.domain.GetLocationActiveUseCase
import com.google.android.gms.maps.model.LatLng

class GetHomeCurrentLocationUseCase(
    private val getLocationActiveUseCase: GetLocationActiveUseCase,
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): LatLng? {
        homeRepository.run {
            if (getLocationActiveUseCase()) {
                return handleCurrentLocation()
            }
        }

        return LatLng(0.0, 0.0)
    }
}