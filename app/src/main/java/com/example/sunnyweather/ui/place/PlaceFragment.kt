package com.example.sunnyweather.ui.place

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.ui.weather.WeatherActivity

class PlaceFragment : Fragment() {
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter: PlaceAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchPlaceEdit: EditText
    private lateinit var bgImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_place, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        searchPlaceEdit = view.findViewById(R.id.searchPlaceEdit)
        bgImageView = view.findViewById(R.id.bgImageView)
        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 关闭软键盘
                    val imm: InputMethodManager = SunnyWeatherApplication.context
                        .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.applicationWindowToken, 0)
                }
                MotionEvent.ACTION_UP -> {
                    v.performClick()
                }
            }
            true
        }
        return view
    }

    // TODO
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = this
        requireActivity().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event.targetState == Lifecycle.State.CREATED) {
                    //在这里任你飞翔
                    if (viewModel.isPlaceSaved()) {
                        val place = viewModel.getSavedPlace()
                        val intent = Intent(context, WeatherActivity::class.java).apply {
                            putExtra("location_lng", place.location.lng)
                            putExtra("location_lat", place.location.lat)
                            putExtra("place_name", place.name)
                        }
                        fragment.startActivity(intent)
                    }
                    val layoutManager = LinearLayoutManager(activity)
                    recyclerView.layoutManager = layoutManager
                    // TODO fragment recyclerview 分发问题
                    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(
                            recyclerView: RecyclerView,
                            newState: Int
                        ) {
                            super.onScrollStateChanged(recyclerView, newState)
                            val imm: InputMethodManager = SunnyWeatherApplication.context
                                .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(recyclerView.applicationWindowToken, 0)
                        }
                    })
                    adapter = PlaceAdapter(fragment, viewModel.placeList)
                    recyclerView.adapter = adapter
                    searchPlaceEdit.addTextChangedListener { editable ->
                        val content = editable.toString()
                        if (content.isNotEmpty()) {
                            viewModel.searchPlaces(content)
                        } else {
                            recyclerView.visibility = View.GONE
                            bgImageView.visibility = View.VISIBLE
                            viewModel.placeList.clear()
                            // TODO
                            adapter.notifyDataSetChanged()
                        }
                    }
                    viewModel.searchLiveData.observe(viewLifecycleOwner, Observer { result ->
                        val places = result.getOrNull()
                        if (places != null) {
                            recyclerView.visibility = View.VISIBLE
                            bgImageView.visibility = View.GONE
                            viewModel.placeList.clear()
                            viewModel.placeList.addAll(places)
                        } else {
                            viewModel.placeList.clear()
                            Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                            result.exceptionOrNull()?.printStackTrace()
                        }
                        adapter.notifyDataSetChanged()
                    })
                    //这里是删除观察者
                    lifecycle.removeObserver(this);
                }
            }
        })
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        val layoutManager = LinearLayoutManager(activity)
//        recyclerView.layoutManager = layoutManager
//        // TODO fragment recyclerview 分发问题
//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(
//                recyclerView: RecyclerView,
//                newState: Int
//            ) {
//                super.onScrollStateChanged(recyclerView, newState)
//                val imm: InputMethodManager = SunnyWeatherApplication.context
//                    .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(recyclerView.applicationWindowToken, 0)
//            }
//        })
//        adapter = PlaceAdapter(this, viewModel.placeList)
//        recyclerView.adapter = adapter
//        searchPlaceEdit.addTextChangedListener { editable ->
//            val content = editable.toString()
//            if (content.isNotEmpty()) {
//                viewModel.searchPlaces(content)
//            } else {
//                recyclerView.visibility = View.GONE
//                bgImageView.visibility = View.VISIBLE
//                viewModel.placeList.clear()
//                // TODO
//                adapter.notifyDataSetChanged()
//            }
//        }
//        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
//            val places = result.getOrNull()
//            if (places != null) {
//                recyclerView.visibility = View.VISIBLE
//                bgImageView.visibility = View.GONE
//                viewModel.placeList.clear()
//                viewModel.placeList.addAll(places)
//            } else {
//                viewModel.placeList.clear()
//                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
//                result.exceptionOrNull()?.printStackTrace()
//            }
//            adapter.notifyDataSetChanged()
//        })
//    }
}