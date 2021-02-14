package com.avaloninfosys.hiresmart

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row.view.*

/**
 * Created by VV on 11/10/2019.
 */
class GithubUserAdapter(private val GithubUsers : ArrayList<GithubUser>) : RecyclerView.Adapter<GithubUserAdapter.GithubViewHolder>() {

    override fun onBindViewHolder(holder: GithubViewHolder, position: Int) {
        holder?.bind(GithubUsers[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubViewHolder =
            GithubViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_row, parent, false))

    override fun getItemCount() = GithubUsers.size

    class GithubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(githubUser: GithubUser){
            itemView?.tViewLogin?.text = githubUser.login
            itemView?.tViewScore?.text = githubUser.score.toString()
            itemView?.tViewUrl?.text = githubUser.html_url
            Picasso.get().load(githubUser.avatar_url).into(itemView?.imageViewProfile)
        }
    }
}