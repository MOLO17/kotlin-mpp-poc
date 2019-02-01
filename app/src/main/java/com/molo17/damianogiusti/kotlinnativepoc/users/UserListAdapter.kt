package com.molo17.damianogiusti.kotlinnativepoc.users

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.molo17.damianogiusti.kotlinnativepoc.R
import com.molo17.damianogiusti.ui.users.UiUser
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

/**
 * Created by Damiano Giusti on 01/02/19.
 */
class UserListAdapter : RecyclerView.Adapter<UserListViewHolder>() {

    var dataset by Delegates.observable(emptyList<UiUser>()) { _, _, _ -> notifyDataSetChanged() }

    override fun getItemCount(): Int = dataset.count()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): UserListViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.user_list_cell, parent, false)
            .let(::UserListViewHolder)


    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val item = dataset[position]
        holder.userNameTextView.text = item.displayName
        holder.emailTextView.text = item.email
        Picasso.get().load(item.pictureUrl).into(holder.picImageView)
    }
}

class UserListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val picImageView: ImageView = itemView.findViewById(R.id.profile_pic_image_view)
    val userNameTextView: TextView = itemView.findViewById(R.id.username_text_view)
    val emailTextView: TextView = itemView.findViewById(R.id.email_text_view)
}