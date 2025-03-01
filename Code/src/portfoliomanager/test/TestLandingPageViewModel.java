package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import portfoliomanager.viewmodel.LandingPageViewModel;

class TestLandingPageViewModel {

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetCryptoCollection() {
		LandingPageViewModel viewModel = new LandingPageViewModel();
		assertTrue(!viewModel.getCryptoCollection().isEmpty());
	}

}
