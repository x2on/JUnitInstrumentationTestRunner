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

import android.content.Context;
import android.util.Log;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;

import com.generationjava.io.xml.SimpleXmlWriter;
import com.generationjava.io.xml.XmlWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * JUnitTestlistener is an extension for the JUnit TestListener used on Android.
 * It writes the test-results in one XML file.
 * 
 * @author <a href="mailto:code@felixschulze.de">Felix Schulze</a>
 * @see http://testng.googlecode.com/svn/branches/newconfiguration/src/main/org/testng/junit/JUnitTestRunner.java
 * 
 */
public class JUnitTestListener implements TestListener {

	private static final String TAG_LOG = "JUnitTestListener";

	private static final String TAG_TESTSUITES = "testsuites";
	private static final String TAG_TESTSUITE = "testsuite";
	private static final String TAG_TESTCASE = "testcase";
	private static final String TAG_ERROR = "error";
	private static final String TAG_FAILURE = "failure";

	private static final String ATTRIBUTE_NAME = "name";
	private static final String ATTRIBUTE_CLASSNAME = "classname";
	private static final String ATTRIBUTE_TYPE = "type";
	private static final String ATTRIBUTE_MESSAGE = "message";
	private static final String ATTRIBUTE_TIME = "time";

	private Context targetContext;
	private String fileName;
	private String currentTestSuite;

	private XmlWriter xmlwriter;
	private Writer writer;

	private long testStartTime;

	public JUnitTestListener(Context targetContext, String reportFile) {
		this.targetContext = targetContext;
		this.fileName = reportFile;
	}

	public void addError(Test test, Throwable error) {
		recordFailure(TAG_ERROR, error);
	}

	public void addFailure(Test test, AssertionFailedError error) {
		recordFailure(TAG_FAILURE, error);
	}

	public void endTest(Test test) {
		try {
			if (test instanceof TestCase) {
				addTestTime();
				xmlwriter.endEntity();
			}
		} catch (IOException e) {
			Log.e(TAG_LOG, formatError(e));
		}
	}

	public void startTest(Test test) {
		try {
			if (test instanceof TestCase) {
				TestCase testCase = (TestCase) test;

				open();

				String testSuite = testCase.getClass().getName();
				if (currentTestSuite == null || !currentTestSuite.equals(testSuite)) {
					if (currentTestSuite != null) {
						xmlwriter.endEntity();
					}
					xmlwriter.writeEntity(TAG_TESTSUITE);
					xmlwriter.writeAttribute(ATTRIBUTE_NAME, testSuite);
					currentTestSuite = testSuite;
				}

				xmlwriter.writeEntity(TAG_TESTCASE);
				xmlwriter.writeAttribute(ATTRIBUTE_CLASSNAME, currentTestSuite);
				xmlwriter.writeAttribute(ATTRIBUTE_NAME, testCase.getName());

				testStartTime = System.currentTimeMillis();
			}
		} catch (IOException e) {
			Log.e(TAG_LOG, formatError(e));
		}
	}

	private void recordFailure(String tag, Throwable error) {
		try {
			addTestTime();
			xmlwriter.writeEntity(tag);
			xmlwriter.writeAttribute(ATTRIBUTE_MESSAGE, formatError(error));
			xmlwriter.writeAttribute(ATTRIBUTE_TYPE, error.getClass().getName());

			StringWriter errorStringWriter = new StringWriter();
			error.printStackTrace(new JUnitTraceFilterWriter(errorStringWriter));
			xmlwriter.writeText(errorStringWriter.toString());
			errorStringWriter.close();

			xmlwriter.endEntity();
		} catch (IOException e) {
			Log.e(TAG_LOG, formatError(e));
		}
	}

	private void addTestTime() throws IOException {
		long usedTime = System.currentTimeMillis() - testStartTime;
		xmlwriter.writeAttribute(ATTRIBUTE_TIME, String.format("%.3f", usedTime / 1000.));
	}

	private String formatError(Throwable error) {
		String message = error.getMessage();
		if (message == null) {
			message = "<null>";
		}
		return error.getClass().getName() + ":" + " " + message;
	}

	private void open() throws IOException {
		if (xmlwriter == null) {

			String filePath = targetContext.getFilesDir() + "/" + fileName;
			writer = new BufferedWriter(new FileWriter(filePath));

			xmlwriter = new SimpleXmlWriter(writer);
			xmlwriter.writeXmlVersion();
			xmlwriter.writeComment("JUNIT Test");
			xmlwriter.writeEntity(TAG_TESTSUITES);
		}
	}

	public void close() {
		if (xmlwriter != null) {
			try {
				if (currentTestSuite != null) {
					xmlwriter.endEntity();
				}
				xmlwriter.endEntity();
				xmlwriter.close();
			} catch (IOException e) {
				Log.e(TAG_LOG, formatError(e));
			}
		}

		if (writer != null) {
			try {
				writer.close();
				writer = null;
			} catch (IOException e) {
				Log.e(TAG_LOG, formatError(e));
			}
		}
	}

}
