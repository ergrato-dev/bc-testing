package com.bootcamp.bdd;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/** Los features de src/test/resources/features. El glue y los reportes, en junit-platform.properties. */
@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
class RunCucumberTest {
}
