package com.kbstar.movieproject

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.kbstar.movieproject.dto.MovieResult
import com.kbstar.movieproject.dto.MovieSearched
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.kbstar.movieproject.databinding.ActivityMainBinding
import com.kbstar.movieproject.dto.BoxOffice
import com.kbstar.movieproject.recyclerview.MainAdapter
import com.kbstar.movieproject.viewmodel.MainViewModel
import com.kbstar.movieproject.viewmodel.MyPageViewModel
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private var layout_logout: LinearLayout? = null
    private var layout_login: LinearLayout? = null
    private var tv_login: TextView? = null
    var boxOfficeList = mutableListOf<BoxOffice>()
    lateinit var toggle: ActionBarDrawerToggle

    val adapter = MainAdapter(this, boxOfficeList)
    lateinit var binding: ActivityMainBinding

    val viewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
            //search Activity

                val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

                binding.mainRecyclerView.visibility = View.INVISIBLE
                binding.loadingImageView.visibility = View.VISIBLE

                viewModel.getSearchResult(query!!)

                viewModel.movieSearchedLiveData.observe(this@MainActivity){
                    it?.let {

                        if(it.results.size == 0)
                        {
                            boxOfficeList.clear()
                            Toast.makeText(this@MainActivity, "검색어에 해당하는 영화가 없습니다.", Toast.LENGTH_SHORT).show()

                        }else
                        {
                            boxOfficeList.clear()
                            it.results.forEach {
                                val boxOffice = BoxOffice(movie_IMDbId = it.id, movie_name = it.title, movie_thumbnail = it.image, movie_idx = null, movie_rank = null, movie_status = null)
                                boxOfficeList.add(boxOffice)
                            }
                        }


                        binding.loadingImageView.visibility = View.INVISIBLE
                        binding.mainRecyclerView.visibility = View.VISIBLE

                        adapter.notifyDataSetChanged()

                        Log.d("jbjung searchMovie", boxOfficeList.toString())

                    } ?: let{
                        Toast.makeText(this@MainActivity, "영화 검색을 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                return true
            }

        })

        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this).load(R.drawable.loading_unscreen).into(binding.loadingImageView)
        // toggle ~~~~~
        toggle = ActionBarDrawerToggle(this, binding.drawer,
            R.string.drawer_open, R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //유저가 손으로 끌어서 열거나 닫거나.. toggle 로 열거나 닫거나..
        //둘이 상호 연동..
        toggle.syncState()

        binding.mainRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.mainRecyclerView.adapter = adapter

        viewModel.getBoxoffice()

        viewModel.boxOfficeLiveData.observe(this){
            boxOfficeList.addAll(it)
            adapter.notifyDataSetChanged()
            Log.d("jbjung_boxoffice", boxOfficeList.toString())
        }
        binding.navigationView.setNavigationItemSelectedListener {
            if(it.itemId == R.id.nav_home){
                gotoHome()
            }
            if(it.itemId == R.id.nav_search){
                gotoSearchMovie()
            }
            if(it.itemId == R.id.nav_login){
                gotoLogin()
            }
            if(it.itemId == R.id.nav_mypage){
                gotoMyPage()
            }
            false
        }
    }

    fun logout() {
        AlertDialog.Builder(this)
            .setTitle("로그아웃 확인")
            .setMessage("로그아웃 하시겠습니까?")
            .setPositiveButton(
                "확인"
            ) { dialogInterface, i ->
                tv_login!!.text = ""
                layout_login!!.visibility = View.INVISIBLE
                layout_logout!!.visibility = View.VISIBLE
                val intent = Intent(
                    this@MainActivity,
                    LoginActivity::class.java
                )
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    fun gotoHome() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    fun gotoLogin() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }

    fun gotoSearchMovie(){
        val intent = Intent(applicationContext, SearchActivity::class.java)
        startActivity(intent)
    }

    fun gotoMyPage(){
        val intent = Intent(applicationContext, MyPageActivity::class.java)
        startActivity(intent)
    }

    // 메뉴 이벤트 처리하는 함수..
    // toggle이 개발자가 메뉴가 추가한 것은 아니지만..
    // actionbar에 아이콘으로 나오는 것이다. 내부적으로 메뉴 취급이 된다.
    // 유저 클릭시.. 메뉴 이벤트를 탄다.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //toggle이 눌렸다면.. 일반적인 메뉴 이벤트를 타지 않게..
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}