package com.molo17.damianogiusti.ui.users

/**
 * Created by Damiano Giusti on 01/02/19.
 */
interface UsersListView {
    fun showLoading()
    fun hideLoading()
    fun showUsers(displayableUsers: List<UiUser>)
    fun hideUsers()
}