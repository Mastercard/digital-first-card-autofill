// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.mastercard.android.test.savecardsref.browser.webview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.mastercard.android.test.savecardsref.browser.customtab.CustomTabActivityHelper

/**
 * A Fallback that opens a Webview when Custom Tabs is not available
 */
class WebviewFallback : CustomTabActivityHelper.CustomTabFallback {

    override fun openUri(activity: Activity?, uri: Uri?) {
        val intent = Intent(activity, WebviewActivity::class.java)
        intent.putExtra(WebviewActivity.EXTRA_URL, uri.toString())
        activity?.startActivity(intent)
    }
}