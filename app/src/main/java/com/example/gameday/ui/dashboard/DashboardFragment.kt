package com.example.gameday.ui.dashboard

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameday.*
import com.example.gameday.databinding.FragmentDashboardBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DashboardFragment : Fragment() {
    private lateinit var retService: FixtureService
    private var _binding: FragmentDashboardBinding? = null
    private lateinit var fixtureRecyclerView: RecyclerView
    private lateinit var adapter: FixtureRecyclerViewAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fixtureRecyclerView = binding.rvFixtures
        var date = LocalDate.now()
        var weekDate = date.plusDays(7)

        retService = RetrofitInstance
            .getRetrofitInstance()
            .create(FixtureService::class.java)

        //path parameter
        getRequestWithPathParameters(date,weekDate)

        /*val responseLiveData:LiveData<retrofit2.Response<Fixtures>> = liveData {
            val response = retService.getFixtures()
            emit(response)
        }
        responseLiveData.observe(viewLifecycleOwner, Observer {
            val fixturesList = it.body()?.response?.listIterator()
            val gameList = mutableListOf<Response>()
            if (fixturesList!=null){
                while (fixturesList.hasNext()){
                    val fixturesItem = fixturesList.next()
                    gameList.add(fixturesItem)
                   // Log.i("MYTAG", "${fixturesItem.teams.home.name} vs ${fixturesItem.teams.away.name}" )
                }
            }*/

            //val formattedDate : String = gameList[0].fixture.date.take(10)
            binding.tvWeekDate.text = "${date} - ${weekDate}"
            binding.btnPrevWeek.setOnClickListener {
                weekDate = date
                date = date.minusDays(7)
                binding.tvWeekDate.text = "${date} - ${weekDate}"
                getRequestWithPathParameters(date,weekDate)
            }

            binding.btnNextWeek.setOnClickListener {
                date = weekDate
                weekDate = weekDate.plusDays(7)
                binding.tvWeekDate.text = "${date} - ${weekDate}"
                getRequestWithPathParameters(date,weekDate)
            }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView(it:List<Response>){
        fixtureRecyclerView.layoutManager = LinearLayoutManager(this.context)
        adapter = FixtureRecyclerViewAdapter()
        fixtureRecyclerView.adapter = adapter

        displayFixtureList(it)
    }

    private fun displayFixtureList(it:List<Response>){
        adapter.setList(it)
    }

    private fun getRequestWithPathParameters(date:LocalDate,weekDate:LocalDate){
        val pathResponse:LiveData<retrofit2.Response<Fixtures>> = liveData{
            val response = retService.getWeekFixtures("39","2022",date.toString(),weekDate.toString())
            emit(response)
        }

        pathResponse.observe(viewLifecycleOwner, Observer {
            val fixturesList = it.body()?.response?.listIterator()
            val gameList = mutableListOf<Response>()
            if (fixturesList != null) {
                while (fixturesList.hasNext()) {
                    val fixturesItem = fixturesList.next()
                    gameList.add(fixturesItem)
                    /*Log.i(
                        "MYTAG",
                        "${fixturesItem.teams.home.name} vs ${fixturesItem.teams.away.name}"
                    )*/
                }
            }
            val sortedGameList = gameList.sortedBy { it.fixture.date }
            initRecyclerView(sortedGameList)
        })

    }

}