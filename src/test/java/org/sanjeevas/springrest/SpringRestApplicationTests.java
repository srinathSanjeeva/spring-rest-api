package org.sanjeevas.springrest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class SpringRestApplicationTests {

	@Test
	void contextLoads() {
		// This test ensures that the Spring context loads successfully
		// If the context fails to load, the test will fail
	}

}
