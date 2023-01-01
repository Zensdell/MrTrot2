package com.kkk.mrtrot2

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import android.content.pm.PackageManager

import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Build
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.ads.mediationtestsuite.MediationTestSuite
//import com.kakao.adfit.ads.AdListener
//import com.kakao.adfit.ads.ba.BannerAdView
import kotlinx.android.synthetic.main.activity_main.*


val datas = ArrayList<ProfileData>()
var likeDatas = ArrayList<ProfileData>()

class MainActivity : FragmentActivity() {

    val datas2 = mutableListOf<ProfileData>()


    lateinit var tabs: TabLayout
    lateinit var fragmentAll: FragmentAll
    lateinit var fragmentLike: FragmentLike
    lateinit var fragmentMySet: FragmentMySet

    var isShowingAlertDialog: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //메인액티비티 뜨자마자 데이터 가져오기
        dataFromFirestore()
        //  viewMySetPage()
//        kakaoAdfit()

    }

//    private fun kakaoAdfit() {
//        val adView = findViewById<BannerAdView>(R.id.adView)
//        adView.setClientId("DAN-CRFQ2XII8V9vW05U") // 할당 받은 광고단위 ID 설정
//
//        adView.setAdListener(object : AdListener {
//            // 광고 수신 리스너 설정
//            override fun onAdLoaded() {
//                Log.d("애드핏로드성공?","로드됨")
//            }
//
//            override fun onAdFailed(errorCode: Int) {
//                Log.d("애드핏로드실패?" ,"${errorCode}")
//            }
//
//            override fun onAdClicked() {
//                Log.d("애드핏클릭됨?","클릭됨")
//            }
//        })
//
//        lifecycle.addObserver(object : LifecycleObserver {
//
//            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//            fun onResume() {
//                adView.resume()
//                Log.d("resume","나오나?")
//            }
//
//            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
//            fun onPause() {
//                adView.pause()
//                Log.d("pause","나오나?")
//            }
//
//            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//            fun onDestroy() {
//                adView.destroy()
//                Log.d("destroy","나오나?")
//            }
//        })
//        adView.loadAd()
//
//    }

//    override fun onResume() {
//        super.onResume()
//        checkRemoteConfig()
//        // lifecycle 사용이 불가능한 경우
//        adView?.resume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//
//        // lifecycle 사용이 불가능한 경우
//        adView?.pause()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//
//        // lifecycle 사용이 불가능한 경우
//        adView?.destroy()
//    }



    private fun checkRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUpdateVersion = remoteConfig.getLong("updateVersionCode")
                    Log.d(TAG, "checkRemoteConfig: $firebaseUpdateVersion")

                    try {
                        val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
                        val userVersion = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            pInfo.longVersionCode
                        } else {
                            pInfo.versionCode.toLong()
                        }
                        if(firebaseUpdateVersion > userVersion) {
                            if(!isShowingAlertDialog) {
                                isShowingAlertDialog = true
                                AlertDialog.Builder(this)
                                    .setCancelable(false)
                                    .setTitle("업데이트 공지")
                                    .setMessage("정상적인 응원댓글을 위해 최신버젼으로 업데이트 해주세요.")
                                    .setPositiveButton("확인") { dialog, which ->

                                        val myUri =
                                            Uri.parse("market://details?id=com.kkk.mrtrot2") // 플레이스토어 주소 대입.
                                        val myIntent = Intent(Intent.ACTION_VIEW, myUri)
                                        startActivity(myIntent)
                                        isShowingAlertDialog = false

                                    }
                                    .show()
                            }
                        } else {
                            isShowingAlertDialog = false
                        }

                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
    }

    private fun dataFromFirestore() {
        val pref = getSharedPreferences("pref",0)
        val db = Firebase.firestore
        db.collection("Singers")
            .get()
            .addOnSuccessListener { result ->

                datas.clear()
                likeDatas.clear()

                Log.d(TAG, "dataFromFirestore: Data READ??????")

                for (document in result) {
                    datas.add(
                        ProfileData(
                            img = document.data["imageUrl"] as String,
                            name = document.data["name"] as String,
                            stageUrl = document.data["stageUrl"] as String,
                            singerId = document.data["id"] as String,
                            infoUrl = document.data["infoUrl"] as String,
                            commentCount = document.data["commentCount"] as Long,
                        )
                    )

                    val singerId: String = document.data["id"] as String
                    val isLiked = pref.getBoolean(singerId, false)
                    if(isLiked) {
                        likeDatas.add(
                            ProfileData(
                                img = document.data["imageUrl"] as String,
                                name = document.data["name"] as String,
                                stageUrl = document.data["stageUrl"] as String,
                                singerId = document.data["id"] as String,
                                infoUrl = document.data["infoUrl"] as String,
                                commentCount = document.data["commentCount"] as Long,
                            )
                        )
                    }

                }

                for (document in result) {
                    datas2.add(
                        ProfileData(
                            img = document.data["imageUrl"] as String,
                            name = document.data["name"] as String,
                            stageUrl = document.data["stageUrl"] as String,
                            singerId = document.data["id"] as String,
                            infoUrl = document.data["infoUrl"] as String,
                            commentCount = document.data["commentCount"] as Long,
                        )
                    )

                    Log.d(TAG, "${document.id} => ${document.data}")
                }

                datas2.sortByDescending { it.commentCount }
                datas.add(0,datas2[8])
                datas.add(0,datas2[7])
                datas.add(0,datas2[6])
                datas.add(0,datas2[5])
                datas.add(0,datas2[4])
                datas.add(0,datas2[3])
                datas.add(0,datas2[2])
                datas.add(0,datas2[1])
                datas.add(0,datas2[0])

                fragmentAll = FragmentAll()
                fragmentLike = FragmentLike()
                fragmentMySet = FragmentMySet()

                supportFragmentManager.beginTransaction().add(R.id.container, fragmentAll).commit()
                tabs = findViewById(R.id.tabs)
//                tabs.addTab(tabs.newTab().setText("전체 가수"))
//                tabs.addTab(tabs.newTab().setText("내가 찜한 가수"))
                tabs.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                    override fun onTabSelected(tab: TabLayout.Tab) {

                        val position = tab.position
                        var selected: Fragment? = null

                        // 탭이 선택되면 나타내줄 프래그먼트를 정하는곳

                        if (position == 0) selected = fragmentAll
                        else if (position == 1) selected = fragmentLike
                        else if (position == 2)  selected = fragmentMySet

                        supportFragmentManager.beginTransaction().replace(R.id.container, selected!!).commit()

                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) {}
                    override fun onTabReselected(tab: TabLayout.Tab) {}

                })


            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "인터넷 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
    }
//    private fun viewMySetPage(){
//        val mySetBtn = findViewById<TextView>(R.id.humanImage)
//        mySetBtn.setOnClickListener {
//            val intent = Intent(this,MySetActivity::class.java)
//            startActivity(intent)
//        }
//    }

}


//    lateinit var mAdView : AdView
//
//    private val items = mutableListOf<ProfileData>()
//
//    val datas = mutableListOf<ProfileData>()
//    val datas2 = mutableListOf<ProfileData>()
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
////        initRecycler()
////        loadBannerAd()
//        dataFromFirestore()
//        viewMySetPage()
//
////        val allSinger = findViewById<Button>(R.id.allSingers)
////        allSinger.setOnClickListener {
////            val allIntent = Intent(this,AllSingersActivity::class.java)
////            startActivity(intent)
////        }
//
//    }
//
//    private fun dataFromFirestore() {
//        val db = Firebase.firestore
//        db.collection("Singers")
//            .get()
//            .addOnSuccessListener { result ->
//
//                for (document in result) {
//                    datas.add(
//                        ProfileData(
//                            img = document.data["imageUrl"] as String,
//                            name = document.data["name"] as String,
//                            stageUrl = document.data["stageUrl"] as String,
//                            singerId = document.data["id"] as String,
//                            infoUrl = document.data["infoUrl"] as String,
//                            commentCount = document.data["commentCount"] as Long,
//                        )
//                    )
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
//
//                for (document in result) {
//                    datas2.add(
//                        ProfileData(
//                            img = document.data["imageUrl"] as String,
//                            name = document.data["name"] as String,
//                            stageUrl = document.data["stageUrl"] as String,
//                            singerId = document.data["id"] as String,
//                            infoUrl = document.data["infoUrl"] as String,
//                            commentCount = document.data["commentCount"] as Long,
//                        )
//                    )
//
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
//
//                datas2.sortByDescending { it.commentCount }
//                datas.add(0,datas2[2])
//                datas.add(0,datas2[1])
//                datas.add(0,datas2[0])
//
//                val recyclerView = findViewById<RecyclerView>(R.id.re_rv)
//                val rvAdapter = RVAdapter(this)
//                rvAdapter.datas = datas
//
//                recyclerView.adapter = rvAdapter
//
//                rvAdapter.itemClick=object : RVAdapter.ItemClick{
//                    override fun onClick(view: View, position: Int) {
//
//                        val intent = Intent(baseContext, ViewActivity::class.java)
//                        intent.putExtra("img",datas[position].img)
//                        intent.putExtra("stageUrl", datas[position].stageUrl)
//                        intent.putExtra("singerId", datas[position].singerId)
//                        intent.putExtra("infoUrl",datas[position].infoUrl)
//                        intent.putExtra("commentCount",datas[position].commentCount)
//
//                        startActivity(intent)
//                    }
//                }
//                recyclerView.layoutManager = GridLayoutManager(this,3)
//
//
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(this, "인터넷 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun viewMySetPage(){
//        val mySetBtn = findViewById<ImageView>(R.id.humanImage)
//        mySetBtn.setOnClickListener {
//            val intent = Intent(this,MySetActivity::class.java)
//            startActivity(intent)
//        }
//    }
//    private fun loadBannerAd() {
//        MobileAds.initialize(this) {}
//
////        mAdView = findViewById(R.id.adView)
//        val adRequest = AdRequest.Builder().build()
//        mAdView.loadAd(adRequest)
//
//        mAdView.adListener = object: AdListener() {
//            override fun onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                Toast.makeText(this@MainActivity, "광고 나옴", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onAdFailedToLoad(adError : LoadAdError) {
//                // Code to be executed when an ad request fails.
//            }
//
//            override fun onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            override fun onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            override fun onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        }
//
//    }
//}