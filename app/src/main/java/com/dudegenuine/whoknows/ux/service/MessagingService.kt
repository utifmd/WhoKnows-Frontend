package com.dudegenuine.whoknows.ux.service

import android.util.Log
import androidx.work.ExistingWorkPolicy
import com.dudegenuine.model.Notification
import com.dudegenuine.repository.contract.dependency.local.IPrefsFactory
import com.dudegenuine.repository.contract.dependency.local.IWorkerManager
import com.dudegenuine.usecase.messaging.RetrieveMessaging
import com.dudegenuine.usecase.notification.PostNotification
import com.dudegenuine.usecase.participation.DeleteBoarding
import com.dudegenuine.whoknows.ux.worker.TokenWorker
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
@AndroidEntryPoint
class MessagingService: FirebaseMessagingService() {
    @Inject lateinit var caseRetriever: RetrieveMessaging
    @Inject lateinit var caseParticipation: DeleteBoarding
    @Inject lateinit var caseNotification: PostNotification
    @Inject lateinit var workManager: IWorkerManager
    @Inject lateinit var prefs: IPrefsFactory
    private val TAG: String = javaClass.simpleName
    private val messagingJob = SupervisorJob()
    private val messagingScope = CoroutineScope(Dispatchers.Main + messagingJob)
    private val worker get() = workManager.instance()
    private val userId get() = prefs.userId
    private val isLoggedIn get() = userId.isBlank()

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        suspend fun synchronize() = caseRetriever(
            message.data, ::onRemoveParticipation, ::onForwardNotification)
        message.notification?.let(caseRetriever::invoke)
        Log.d(TAG, "onMessageReceived: triggered")
        if (isLoggedIn) return
        messagingScope.launch{ synchronize() }
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
        val serverRequest = TokenWorker.oneTimeBuilder(token).build()
        val chainer = worker.beginUniqueWork(
            IWorkerManager.WORK_NAME_ROOM_TOKEN,
            ExistingWorkPolicy.KEEP,
            serverRequest
        )
        token.let(::onTokenIdChange)
        if (isLoggedIn) chainer.enqueue()
        Log.d(TAG, "onNewToken: ended")
    }
    private fun onRemoveParticipation() = caseParticipation()
        .onEach{ Log.d(TAG, "onRemoveParticipation: $it") }
        .launchIn(messagingScope)

    private fun onForwardNotification(notification: Notification) =
        caseNotification.plus(notification)
            .onEach{ Log.d(TAG, "onForwardNotification: $it") }
            .launchIn(messagingScope)

    private fun onTokenIdChange(token: String) {
        prefs.tokenId = token
    }
    override fun onDestroy() {
        super.onDestroy()
        messagingJob.cancel()
    }
    //companion object { const val MESSAGE_INTENT = "MessagingService message intent" }
}
/*@EntryPoint
    @InstallIn(SingletonComponent::class)
    interface IRepositories {
        val reposRoom: IRoomRepository
        val reposNotifier: INotificationRepository
    }*/
//val repositories = EntryPoints.get(applicationContext, IRepositories::class.java)