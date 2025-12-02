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
import com.alpaca.knm.data.source.remote.AdvanceRemoteDataSource
import com.alpaca.knm.data.source.remote.AuthRemoteDataSource
import com.alpaca.knm.data.source.remote.DashboardRemoteDataSource
import com.alpaca.knm.data.source.remote.GanaderoRemoteDataSource
import com.alpaca.knm.data.source.remote.ProfileRemoteDataSource
import com.alpaca.knm.data.remote.datasource.SolicitudRemoteDataSource
import com.alpaca.knm.data.remote.datasource.AlpacaRemoteDataSource
import com.alpaca.knm.domain.repository.AdvanceRepository
import com.alpaca.knm.domain.repository.AuthRepository
import com.alpaca.knm.domain.repository.DashboardRepository
import com.alpaca.knm.domain.repository.GanaderoRepository
import com.alpaca.knm.domain.repository.ProfileRepository
import com.alpaca.knm.domain.repository.SolicitudRepository
import com.alpaca.knm.domain.repository.AlpacaRepository
import com.alpaca.knm.util.ErrorHandler
import com.alpaca.knm.domain.usecase.CreateAdvanceRequestUseCase
import com.alpaca.knm.domain.usecase.CreateGanaderoUseCase
import com.alpaca.knm.domain.usecase.DeleteGanaderoUseCase
import com.alpaca.knm.domain.usecase.GetCurrentUserUseCase
import com.alpaca.knm.domain.usecase.GetDashboardStatsUseCase
import com.alpaca.knm.domain.usecase.GetGanaderosUseCase
import com.alpaca.knm.domain.usecase.GetUserProfileUseCase
import com.alpaca.knm.domain.usecase.IsUserLoggedInUseCase
import com.alpaca.knm.domain.usecase.LoginUseCase
import com.alpaca.knm.domain.usecase.LogoutUseCase
import com.alpaca.knm.domain.usecase.UpdateGanaderoUseCase
import com.alpaca.knm.domain.usecase.ValidateCredentialsUseCase
import com.alpaca.knm.domain.usecase.solicitud.GetAllSolicitudesUseCase
import com.alpaca.knm.domain.usecase.solicitud.GetSolicitudesByStatusUseCase
import com.alpaca.knm.domain.usecase.solicitud.ApproveSolicitudUseCase
import com.alpaca.knm.domain.usecase.solicitud.RejectSolicitudUseCase
import com.alpaca.knm.domain.usecase.alpaca.GetAllAlpacasUseCase
import com.alpaca.knm.domain.usecase.alpaca.GetAlpacasByGanaderoUseCase
import com.alpaca.knm.domain.usecase.alpaca.CreateAlpacaUseCase
import com.alpaca.knm.domain.usecase.alpaca.UpdateAlpacaUseCase
import com.alpaca.knm.domain.usecase.alpaca.DeleteAlpacaUseCase

/**
 * Contenedor de dependencias de la aplicación
 * Implementación manual de Dependency Injection
 * TODO: Considerar migrar a Hilt o Koin
 */
class AppContainer(context: Context) {
    
    // Data Sources
    private val authRemoteDataSource: AuthRemoteDataSource by lazy {
        AuthRemoteDataSource()
    }
    
    private val authLocalDataSource: AuthLocalDataSource by lazy {
        AuthLocalDataSource(context)
    }
    
    // Repositories
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authRemoteDataSource, authLocalDataSource)
    }
    
    // Use Cases
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
    
    val isUserLoggedInUseCase: IsUserLoggedInUseCase by lazy {
        IsUserLoggedInUseCase(authRepository)
    }
    
    // Dashboard Data Sources
    private val dashboardRemoteDataSource: DashboardRemoteDataSource by lazy {
        DashboardRemoteDataSource()
    }
    
    // Dashboard Repository
    val dashboardRepository: DashboardRepository by lazy {
        DashboardRepositoryImpl(dashboardRemoteDataSource)
    }
    
    // Dashboard Use Cases
    val getDashboardStatsUseCase: GetDashboardStatsUseCase by lazy {
        GetDashboardStatsUseCase(dashboardRepository, authRepository)
    }
    
    // Advance Data Sources
    private val advanceRemoteDataSource: AdvanceRemoteDataSource by lazy {
        AdvanceRemoteDataSource()
    }
    
    // Advance Repository
    val advanceRepository: AdvanceRepository by lazy {
        AdvanceRepositoryImpl(advanceRemoteDataSource)
    }
    
    // Advance Use Cases
    val createAdvanceRequestUseCase: CreateAdvanceRequestUseCase by lazy {
        CreateAdvanceRequestUseCase(advanceRepository, authRepository)
    }
    
    // Profile Data Sources
    private val profileRemoteDataSource: ProfileRemoteDataSource by lazy {
        ProfileRemoteDataSource()
    }
    
    // Profile Repository
    val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl(profileRemoteDataSource)
    }
    
    // Profile Use Cases
    val getUserProfileUseCase: GetUserProfileUseCase by lazy {
        GetUserProfileUseCase(profileRepository, authRepository)
    }
    
    // Ganadero Data Sources
    private val ganaderoRemoteDataSource: GanaderoRemoteDataSource by lazy {
        GanaderoRemoteDataSource()
    }
    
    // Ganadero Repository
    val ganaderoRepository: GanaderoRepository by lazy {
        GanaderoRepositoryImpl(ganaderoRemoteDataSource)
    }
    
    // Ganadero Use Cases
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
    
    // Solicitud Data Sources
    
    private val solicitudRemoteDataSource: SolicitudRemoteDataSource by lazy {
        SolicitudRemoteDataSource(
            com.alpaca.knm.data.remote.RetrofitClient.createService(
                com.alpaca.knm.data.remote.api.SolicitudApiService::class.java
            )
        )
    }
    
    // Solicitud Repository
    val solicitudRepository: SolicitudRepository by lazy {
        SolicitudRepositoryImpl(solicitudRemoteDataSource)
    }
    
    // Solicitud Use Cases
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
    
    // Alpaca Data Sources
    private val alpacaRemoteDataSource: AlpacaRemoteDataSource by lazy {
        AlpacaRemoteDataSource(
            com.alpaca.knm.data.remote.RetrofitClient.createService(
                com.alpaca.knm.data.remote.api.AlpacaApiService::class.java
            )
        )
    }
    
    // Alpaca Repository
    val alpacaRepository: AlpacaRepository by lazy {
        AlpacaRepositoryImpl(alpacaRemoteDataSource)
    }
    
    // Alpaca Use Cases
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
