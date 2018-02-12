/*
 * Copyright (C) 2015. Jared Rummler <jared.rummler@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ss174h.amsa.MonitorBehaviour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ss174h.amsa.MainActivity;


public class MonitorActivity extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
      KnownPorts.initPortMap();
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction().add(android.R.id.content, new ProcessListFragment()).commit();
    }
  }

  @Override
  public void onBackPressed() {
      super.onBackPressed();
      getFragmentManager().popBackStack();
      MonitorActivity.this.finish();
  }
}
