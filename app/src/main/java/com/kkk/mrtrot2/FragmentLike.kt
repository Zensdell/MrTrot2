package com.kkk.mrtrot2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.AdSettings
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds

class FragmentLike  : Fragment() {

    private var mInterstitialAd : InterstitialAd?=null

    lateinit var rvAdapter: RVLikeAdapter
    private val TAG = "FragmentLike"

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        rvAdapter.datas = likeDatas
        likeDatas.forEach{
            Log.d(TAG, "onResume: "+it.singerId)
        }
        rvAdapter.notifyDataSetChanged()
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 여기가 메인액티비티에서 온크리에이트랑 비슷한 역할을하는함수야
        // 프래그먼트라 이름이 온크리에이트뷰임

        MobileAds.initialize(context) {}
        AdSettings.addTestDevice("796e3543-c38d-4b34-a088-f1b238d2cea3")

        mInterstitialAd = InterstitialAd(context)
        mInterstitialAd!!.adUnitId = "ca-app-pub-8246055051187544/2447787797"
        mInterstitialAd!!.loadAd(AdRequest.Builder().build())
        Log.d("라이크전면광고","받아오나?")
        val view: View = inflater.inflate(R.layout.fragment_like, container, false) as ViewGroup
        // view 를 inflate 로 받으면 view 안에 이제 context 가 들어가는것임 ! (원래는 Activity 안에 있던것을  Fragment 로 옮기면서 이렇게 변경됨)


        val recyclerView = view.findViewById<RecyclerView>(R.id.like_rv)
        rvAdapter = RVLikeAdapter(view.context)
        rvAdapter.datas = likeDatas

        recyclerView.adapter = rvAdapter

        rvAdapter.itemClick=object : RVLikeAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {

                val intent = Intent(view.context, ViewActivity::class.java)
                intent.putExtra("img",likeDatas[position].img)
                intent.putExtra("stageUrl", likeDatas[position].stageUrl)
                intent.putExtra("singerId", likeDatas[position].singerId)
                intent.putExtra("infoUrl",likeDatas[position].infoUrl)
                intent.putExtra("commentCount",likeDatas[position].commentCount)
                intent.putExtra("name",datas[position].name)

                startActivity(intent)

                if (mInterstitialAd != null) {
                    if(mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
                } //광고를 보여줍니다.
                else {
                    Toast.makeText(context,"광고 로드 실패", Toast.LENGTH_SHORT).show()//광고를 불러오지 못했을때
                }
            }
        }
        recyclerView.layoutManager = GridLayoutManager(view.context,3)

        return view
    }

}