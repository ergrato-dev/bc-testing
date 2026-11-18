package com.bootcamp;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/** El glue y los plugins están en src/test/resources/junit-platform.properties. */
@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
class RunCucumberTest {
}
