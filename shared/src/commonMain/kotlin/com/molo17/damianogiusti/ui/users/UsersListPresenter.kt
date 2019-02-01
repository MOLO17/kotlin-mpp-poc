package com.molo17.damianogiusti.ui.users

import com.molo17.damianogiusti.data.User
import com.molo17.damianogiusti.data.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Damiano Giusti on 01/02/19.
 */
class UsersListPresenter(private val usersRepository: UsersRepository) {

    private var view: UsersListView? = null
    private val jobs = mutableListOf<Job>()

    fun attachView(v: UsersListView) {
        view = v

        jobs += GlobalScope.launch(Dispatchers.Main) {
            val users = usersRepository.getAllUsers()
            val displayableUsers = users.map(::mapToUiUser)
            view?.showUsers(displayableUsers)
        }
    }

    fun detachView() {
        view = null
        jobs.forEach { it.cancel() }
        jobs.clear()
    }
}

private fun mapToUiUser(user: User) = UiUser(
    id = user.id,
    displayName = user.run { "${name.capitalize()} ${surname.capitalize()}" },
    email = user.email,
    pictureUrl = user.profilePictureUrl
)

fun getUserListPresenter() = UsersListPresenter(UsersRepository())