package com.example.gameday

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FixtureRecyclerViewAdapter():RecyclerView.Adapter<FixtureViewHolder>() {

    private val fixtureList = ArrayList<Response>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixtureViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item,parent,false)
        return FixtureViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: FixtureViewHolder, position: Int) {
        holder.bind(fixtureList[position])
    }

    override fun getItemCount(): Int {
        return fixtureList.size
    }

    fun setList(fixtures:List<Response>){
        fixtureList.clear()
        fixtureList.addAll(fixtures)
    }

}

class FixtureViewHolder(private val view:View):RecyclerView.ViewHolder(view){
    fun bind(response: Response){
        val homeTextView = view.findViewById<TextView>(R.id.tvHomeName)
        val awayTextView = view.findViewById<TextView>(R.id.tvAwayName)
        val resultTextView = view.findViewById<TextView>(R.id.tvResult)
        val dateOfGame = view.findViewById<TextView>(R.id.tvGameDate)
        homeTextView.text = response.teams.home.name
        awayTextView.text = response.teams.away.name
        val formattedDate : String = response.fixture.date.take(10)
        dateOfGame.text = formattedDate
        if (response.fixture.status.short=="NS") {
            resultTextView.text = "VS"
        }
        else{
            resultTextView.text = "${response.goals.home} - ${response.goals.away}"
        }
    }
}