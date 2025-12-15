package com.alpaca.knm.di

import android.content.Context
import com.alpaca.knm.data.repository.AdvanceRepositoryImpl
import com.alpaca.knm.data.repository.AuthRepositoryImpl
import com.alpaca.knm.data.repository.DashboardRepositoryImpl
import com.alpaca.knm.data.repository.GanaderoRepositoryImpl
import com.alpaca.knm.data.repository.ProfileRepositoryImpl
import com.alpaca.knm.data.repository.SolicitudRepositoryImpl
import com.alpaca.knm.data.repository.AlpacaRepositoryImpl
import com.alpaca.knm.data.source.local.AuthLocalDataSource
import com.alpaca.knm.data.remote.datasource.AdvanceRemoteDataSource
import com.alpaca.knm.data.remote.datasource.AlpacaRemoteDataSource
import com.alpaca.knm.data.remote.datasource.AuthRemoteDataSource
import com.alpaca.knm.data.remote.datasource.DashboardRemoteDataSource
import com.alpaca.knm.data.remote.datasource.GanaderoRemoteDataSource
import com.alpaca.knm.data.remote.datasource.ProfileRemoteDataSource
import com.alpaca.knm.data.remote.datasource.SolicitudRemoteDataSource
import com.alpaca.knm.domain.repository.AdvanceRepository
import com.alpaca.knm.domain.repository.AuthRepository
import com.alpaca.knm.domain.repository.DashboardRepository
import com.alpaca.knm.domain.repository.GanaderoRepository
import com.alpaca.knm.domain.repository.ProfileRepository
import com.alpaca.knm.domain.repository.SolicitudRepository
import com.alpaca.knm.domain.repository.AlpacaRepository
import com.alpaca.knm.domain.usecase.advance.CreateAdvanceRequestUseCase
import com.alpaca.knm.domain.usecase.auth.GetCurrentUserUseCase
import com.alpaca.knm.domain.usecase.auth.LoginUseCase
import com.alpaca.knm.domain.usecase.auth.LogoutUseCase
import com.alpaca.knm.domain.usecase.auth.ValidateCredentialsUseCase
import com.alpaca.knm.domain.usecase.dashboard.GetDashboardStatsUseCase
import com.alpaca.knm.domain.usecase.ganadero.CreateGanaderoUseCase
import com.alpaca.knm.domain.usecase.ganadero.DeleteGanaderoUseCase
import com.alpaca.knm.domain.usecase.ganadero.GetGanaderosUseCase
import com.alpaca.knm.domain.usecase.ganadero.UpdateGanaderoUseCase
import com.alpaca.knm.domain.usecase.profile.GetUserProfileUseCase
import com.alpaca.knm.domain.usecase.solicitud.GetAllSolicitudesUseCase
import com.alpaca.knm.domain.usecase.solicitud.GetSolicitudesByStatusUseCase
import com.alpaca.knm.domain.usecase.solicitud.ApproveSolicitudUseCase
import com.alpaca.knm.domain.usecase.solicitud.RejectSolicitudUseCase
import com.alpaca.knm.domain.usecase.alpaca.GetAllAlpacasUseCase
import com.alpaca.knm.domain.usecase.alpaca.GetAlpacasByGanaderoUseCase
import com.alpaca.knm.domain.usecase.alpaca.CreateAlpacaUseCase
import com.alpaca.knm.domain.usecase.alpaca.UpdateAlpacaUseCase
import com.alpaca.knm.domain.usecase.alpaca.DeleteAlpacaUseCase
import com.alpaca.knm.data.remote.api.AlpacaApiService
import com.alpaca.knm.data.remote.api.DashboardApiService
import com.alpaca.knm.data.remote.RetrofitClient

class AppContainer(context: Context) {
    
    private val authRemoteDataSource: AuthRemoteDataSource by lazy {
        AuthRemoteDataSource()
    }
    
    private val authLocalDataSource: AuthLocalDataSource by lazy {
        AuthLocalDataSource(context)
    }
    
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authRemoteDataSource, authLocalDataSource)
    }
    
    val loginUseCase: LoginUseCase by lazy {
        LoginUseCase(authRepository)
    }
    
    val logoutUseCase: LogoutUseCase by lazy {
        LogoutUseCase(authRepository)
    }
    
    val validateCredentialsUseCase: ValidateCredentialsUseCase by lazy {
        ValidateCredentialsUseCase()
    }
    
    val getCurrentUserUseCase: GetCurrentUserUseCase by lazy {
        GetCurrentUserUseCase(authRepository)
    }
    
    val dashboardApiService: DashboardApiService by lazy {
        RetrofitClient.createService(DashboardApiService::class.java)
    }
    
    private val dashboardRemoteDataSource: DashboardRemoteDataSource by lazy {
        DashboardRemoteDataSource()
    }
    
    val dashboardRepository: DashboardRepository by lazy {
        DashboardRepositoryImpl(dashboardRemoteDataSource)
    }
    
    val getDashboardStatsUseCase: GetDashboardStatsUseCase by lazy {
        GetDashboardStatsUseCase(dashboardRepository, authRepository)
    }
    
    private val advanceRemoteDataSource: AdvanceRemoteDataSource by lazy {
        AdvanceRemoteDataSource()
    }
    
    val advanceRepository: AdvanceRepository by lazy {
        AdvanceRepositoryImpl(advanceRemoteDataSource)
    }
    
    val createAdvanceRequestUseCase: CreateAdvanceRequestUseCase by lazy {
        CreateAdvanceRequestUseCase(advanceRepository, authRepository)
    }
    
    private val profileRemoteDataSource: ProfileRemoteDataSource by lazy {
        ProfileRemoteDataSource()
    }
    
    val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl(profileRemoteDataSource)
    }
    
    val getUserProfileUseCase: GetUserProfileUseCase by lazy {
        GetUserProfileUseCase(profileRepository, authRepository)
    }
    
    private val ganaderoRemoteDataSource: GanaderoRemoteDataSource by lazy {
        GanaderoRemoteDataSource()
    }
    
    val ganaderoRepository: GanaderoRepository by lazy {
        GanaderoRepositoryImpl(ganaderoRemoteDataSource)
    }
    
    val getGanaderosUseCase: GetGanaderosUseCase by lazy {
        GetGanaderosUseCase(ganaderoRepository, authRepository)
    }
    
    val createGanaderoUseCase: CreateGanaderoUseCase by lazy {
        CreateGanaderoUseCase(ganaderoRepository, authRepository)
    }
    
    val updateGanaderoUseCase: UpdateGanaderoUseCase by lazy {
        UpdateGanaderoUseCase(ganaderoRepository, authRepository)
    }
    
    val deleteGanaderoUseCase: DeleteGanaderoUseCase by lazy {
        DeleteGanaderoUseCase(ganaderoRepository, authRepository)
    }
    
    private val solicitudRemoteDataSource: SolicitudRemoteDataSource by lazy {
        SolicitudRemoteDataSource(
            com.alpaca.knm.data.remote.RetrofitClient.createService(
                com.alpaca.knm.data.remote.api.SolicitudApiService::class.java
            )
        )
    }
    
    val solicitudRepository: SolicitudRepository by lazy {
        SolicitudRepositoryImpl(solicitudRemoteDataSource)
    }
    
    val getAllSolicitudesUseCase: GetAllSolicitudesUseCase by lazy {
        GetAllSolicitudesUseCase(solicitudRepository)
    }
    
    val getSolicitudesByStatusUseCase: GetSolicitudesByStatusUseCase by lazy {
        GetSolicitudesByStatusUseCase(solicitudRepository)
    }
    
    val approveSolicitudUseCase: ApproveSolicitudUseCase by lazy {
        ApproveSolicitudUseCase(solicitudRepository)
    }
    
    val rejectSolicitudUseCase: RejectSolicitudUseCase by lazy {
        RejectSolicitudUseCase(solicitudRepository)
    }
    
    private val alpacaRemoteDataSource: AlpacaRemoteDataSource by lazy {
        AlpacaRemoteDataSource(
            com.alpaca.knm.data.remote.RetrofitClient.createService(
                com.alpaca.knm.data.remote.api.AlpacaApiService::class.java
            )
        )
    }
    
    val alpacaRepository: AlpacaRepository by lazy {
        AlpacaRepositoryImpl(alpacaRemoteDataSource)
    }
    
    val getAllAlpacasUseCase: GetAllAlpacasUseCase by lazy {
        GetAllAlpacasUseCase(alpacaRepository)
    }
    
    val getAlpacasByGanaderoUseCase: GetAlpacasByGanaderoUseCase by lazy {
        GetAlpacasByGanaderoUseCase(alpacaRepository)
    }
    
    val createAlpacaUseCase: CreateAlpacaUseCase by lazy {
        CreateAlpacaUseCase(alpacaRepository)
    }
    
    val updateAlpacaUseCase: UpdateAlpacaUseCase by lazy {
        UpdateAlpacaUseCase(alpacaRepository)
    }
    
    val deleteAlpacaUseCase: DeleteAlpacaUseCase by lazy {
        DeleteAlpacaUseCase(alpacaRepository)
    }
}
