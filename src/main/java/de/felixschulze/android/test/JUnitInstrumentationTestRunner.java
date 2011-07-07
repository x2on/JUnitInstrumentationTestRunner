/**
 *  Copyright 2011 Felix Schulze
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */

package de.felixschulze.android.test;

import android.os.Bundle;
import android.test.AndroidTestRunner;
import android.test.InstrumentationTestRunner;

/**
 * JUnitInstrumentationTestRunner is a extension for the Android InstrumentationTestRunner
 * which adds the JUnitTestListener for writing test-results into an XML file.
 * 
 * @author <a href="mailto:code@felixschulze.de">Felix Schulze</a>
 * @see android.test.InstrumentationTestRunnerTest
 * 
 */

public class JUnitInstrumentationTestRunner extends InstrumentationTestRunner {

	private static final String JUNIT_REPORT_FILE = "junitreport.xml";

	private JUnitTestListener junitListener;

	@Override
	public void onCreate(Bundle arguments) {
		super.onCreate(arguments);
	}

	@Override
	protected AndroidTestRunner getAndroidTestRunner() {
		AndroidTestRunner runner = super.getAndroidTestRunner();
		junitListener = new JUnitTestListener(getTargetContext(), JUNIT_REPORT_FILE);
		runner.addTestListener(junitListener);
		return runner;
	}

	@Override
	public void finish(int resultCode, Bundle results) {
		if (junitListener != null) {
			junitListener.close();
		}
		super.finish(resultCode, results);
	}
}
