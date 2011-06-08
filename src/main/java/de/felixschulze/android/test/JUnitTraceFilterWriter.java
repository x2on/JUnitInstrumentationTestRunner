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

import java.io.PrintWriter;
import java.io.Writer;

/**
 * JUnitTraceFilterWriter is a extension of Java PrintWriter 
 * which filters the unneeded traces
 * 
 * @author <a href="mailto:code@felixschulz.dee">Felix Schulze</a>
 * @see http://svn.apache.org/viewvc/ant/core/trunk/src/main/org/apache/tools/ant/taskdefs/optional/junit/JUnitTestRunner.java?view=markup
 * 
 */
public class JUnitTraceFilterWriter extends PrintWriter {
	
    private static final String[] DEFAULT_TRACE_FILTERS = new String[] {
            "junit.framework.TestCase", 
            "junit.framework.TestResult",
            "junit.framework.TestSuite",
            "junit.framework.Assert.", // don't filter AssertionFailure
            "java.lang.reflect.Method.invoke(",
            "sun.reflect.",
            // JUnit 4 support:
            "org.junit.", 
            "junit.framework.JUnit4TestAdapter", 
            " more",
            // Android support
            "android.test.", 
            "android.app.Instrumentation",
            "java.lang.reflect.Method.invokeNative",
    };

	public JUnitTraceFilterWriter(Writer out) {
		super(out);
	}

	@Override
	public void println(String str) {
		for (String filterLine : DEFAULT_TRACE_FILTERS) {
			if (str.contains(filterLine)) {
				return;
			}
		}
		super.println(str);
	}
}