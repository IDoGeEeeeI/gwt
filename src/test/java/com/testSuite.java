package com;

import com.client.testTest;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class testSuite extends GWTTestSuite {
  public static Test suite() {
    TestSuite suite = new TestSuite("Tests for test");
    suite.addTestSuite(testTest.class);
    return suite;
  }
}
