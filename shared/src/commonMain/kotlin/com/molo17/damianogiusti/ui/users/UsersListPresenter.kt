package com.molo17.damianogiusti.ui.users
import com.molo17.damianogiusti.MainDispatcher
import com.molo17.damianogiusti.data.User
import com.molo17.damianogiusti.data.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by Damiano Giusti on 01/02/19.
 */
class UsersListPresenter(
    private val usersRepository: UsersRepository,
    private val mainDispatcher: CoroutineDispatcher
) : CoroutineScope {

    private var view: UsersListView? = null

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = mainDispatcher + job

    fun attachView(v: UsersListView) {
        view = v

        launch {
            view?.hideUsers()
            view?.showLoading()
            val users = usersRepository.getAllUsers()
            val displayableUsers = users.map(::mapToUiUser)
            view?.hideLoading()
            view?.showUsers(displayableUsers)
        }
    }

    fun detachView() {
        view = null
        job.cancel()
    }
}

private fun mapToUiUser(user: User) = UiUser(
    id = user.id,
    displayName = user.run { "${name.capitalize()} ${surname.capitalize()}" },
    email = user.email,
    pictureUrl = user.profilePictureUrl
)

fun getUserListPresenter() = UsersListPresenter(UsersRepository(), MainDispatcher)