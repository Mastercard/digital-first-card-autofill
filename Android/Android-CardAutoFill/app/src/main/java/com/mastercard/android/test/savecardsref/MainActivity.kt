package com.mastercard.android.test.savecardsref

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.mastercard.android.test.savecardsref.adapter.Card
import com.mastercard.android.test.savecardsref.adapter.CardListAdapter
import com.mastercard.android.test.savecardsref.browser.customtab.CustomTabActivityHelper


class MainActivity : AppCompatActivity(),
    CustomTabActivityHelper.ConnectionCallback {

    private lateinit var customTabActivityHelper: CustomTabActivityHelper
    private lateinit var listView: RecyclerView
    private lateinit var mockCards: List<Card>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customTabActivityHelper = CustomTabActivityHelper()
        customTabActivityHelper.setConnectionCallback(this)

        mockCards = listOf(Card("**** **** **** 6378", Uri.parse(getString(R.string.default_url))))

        setupUI()
    }

    private fun setupUI() {
        listView = findViewById(R.id.list_view)
        listView.adapter = CardListAdapter(mockCards) { card -> launchURL(card.url) }
        listView.isEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        customTabActivityHelper.setConnectionCallback(null)
    }

    override fun onCustomTabsConnected() {
        listView.isEnabled = true
    }

    override fun onCustomTabsDisconnected() {
        listView.isEnabled = false
    }

    override fun onStart() {
        super.onStart()
        customTabActivityHelper.bindCustomTabsService(this)
    }

    override fun onStop() {
        super.onStop()
        customTabActivityHelper.unbindCustomTabsService(this)
        listView.isEnabled = false
    }

    private fun launchURL(uri: Uri) {
        val customTabsIntent = CustomTabsIntent.Builder(customTabActivityHelper.session).build()
        CustomTabActivityHelper.openCustomTab(
            this, customTabsIntent,
            uri, null
        )
    }
}