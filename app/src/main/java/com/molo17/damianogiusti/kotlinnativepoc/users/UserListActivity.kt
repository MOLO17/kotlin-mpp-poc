package com.molo17.damianogiusti.kotlinnativepoc.users

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.molo17.damianogiusti.kotlinnativepoc.R
import com.molo17.damianogiusti.ui.users.UiUser
import com.molo17.damianogiusti.ui.users.UsersListView
import com.molo17.damianogiusti.ui.users.getUserListPresenter

class UserListActivity : AppCompatActivity(), UsersListView {

    private val presenter by lazy { getUserListPresenter() }
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recycler_view) }
    private val progressView by lazy { findViewById<ProgressBar>(R.id.progress_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UserListAdapter()

        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showUsers(displayableUsers: List<UiUser>) {
        recyclerView.visibility = View.VISIBLE
        val adapter = recyclerView.adapter as UserListAdapter
        adapter.dataset = displayableUsers
    }

    override fun hideUsers() {
        recyclerView.visibility = View.GONE
    }

    override fun showLoading() {
        progressView.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressView.visibility = View.GONE
    }
}
