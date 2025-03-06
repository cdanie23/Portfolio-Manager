package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import portfoliomanager.model.Account;
import portfoliomanager.viewmodel.LandingPageViewModel;

class TestLandingPageViewModel {
	LandingPageViewModel viewModel;
	
	
	
	@BeforeEach 
	void setUp() {
		this.viewModel = new LandingPageViewModel();
	}
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetCryptoCollection() {
		assertTrue(!this.viewModel.getCryptoCollection().isEmpty());
	}
	@Test
	void testGetFundsAvailabe() {
		String expected = "$0.0";
		String actual = this.viewModel.getFundsAvailabe().getValue();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetCryptoHoldings() {
		assertTrue(!this.viewModel.getCryptoHoldings().isEmpty());
	}
	
	@Test
	void testGetUser() {
		Account expectedUser = this.viewModel.getUser();
		
		assertEquals(expectedUser, this.viewModel.getUser());
	}
}
