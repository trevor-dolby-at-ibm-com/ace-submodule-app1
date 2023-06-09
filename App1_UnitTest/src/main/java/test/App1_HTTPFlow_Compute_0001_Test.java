package test;

import java.io.InputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.ibm.integration.test.v1.NodeSpy;
import com.ibm.integration.test.v1.SpyObjectReference;
import com.ibm.integration.test.v1.TestMessageAssembly;
import com.ibm.integration.test.v1.TestSetup;
import com.ibm.integration.test.v1.exception.TestException;

import static com.ibm.integration.test.v1.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class App1_HTTPFlow_Compute_0001_Test {

	/*
	 * App1_HTTPFlow_Compute_0001_Test
	 * Test generated by IBM App Connect Enterprise Toolkit 12.0.8.0 on Mar 31, 2023 1:25:50 PM
	 */

	@AfterEach
	public void cleanupTest() throws TestException {
		// Ensure any mocks created by a test are cleared after the test runs 
		TestSetup.restoreAllMocks();
	}

	@Test
	public void App1_HTTPFlow_Compute_TestCase_001() throws TestException {

		// Define the SpyObjectReference
		SpyObjectReference nodeReference = new SpyObjectReference().application("App1").messageFlow("HTTPFlow")
				.node("Compute");

		// Initialise a NodeSpy
		NodeSpy nodeSpy = new NodeSpy(nodeReference);

		// Declare a new TestMessageAssembly object for the message being sent into the node
		// This is left blank as the Compute node does not do anything with the input.
		TestMessageAssembly inputMessageAssembly = new TestMessageAssembly();

		// Call the message flow node with the Message Assembly
		nodeSpy.evaluate(inputMessageAssembly, true, "in");

		// Assert the number of times that the node is called
		assertThat(nodeSpy, nodeCallCountIs(1));

		// Assert the terminal propagate count for the message
		assertThat(nodeSpy, terminalPropagateCountIs("out", 1));

		// Compare Output Message 1 at output terminal out

		try {
			TestMessageAssembly actualMessageAssembly = null;
			TestMessageAssembly expectedMessageAssembly = null;

			// Get the TestMessageAssembly object for the actual propagated message
			actualMessageAssembly = nodeSpy.propagatedMessageAssembly("out", 1);

			// Assert output message body data
			// Get the TestMessageAssembly object for the expected propagated message
			expectedMessageAssembly = new TestMessageAssembly();

			// Create a Message Assembly from the expected output mxml resource
			String messageAssemblyPath = "/00000408-6425FEAF-00000001-1.mxml";
			InputStream messageStream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(messageAssemblyPath);
			if (messageStream == null) {
				throw new TestException("Unable to locate message assembly file: " + messageAssemblyPath);
			}
			expectedMessageAssembly.buildFromRecordedMessageAssembly(messageStream);

			// Assert that the actual message tree matches the expected message tree
			assertThat(actualMessageAssembly, equalsMessage(expectedMessageAssembly).ignorePath("/Properties", true));

		} catch (Exception ex) {
			throw new TestException("Failed to compare with expected message", ex);
		}

	}

}
