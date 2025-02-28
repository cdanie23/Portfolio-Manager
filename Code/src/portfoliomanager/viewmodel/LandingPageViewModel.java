package portfoliomanager.viewmodel;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import portfoliomanager.Main;
import portfoliomanager.datareader.DataReader;
import portfoliomanager.model.Crypto;
import portfoliomanager.model.CryptoCollection;

/**
 * The view model for the landing page
 * 
 * @author Colby
 * @version Spring 2025
 */
public class LandingPageViewModel {
	private DataReader dataReader;
	private ObservableList<Crypto> cryptoObservableList;
	
	public LandingPageViewModel() {
		dataReader = new DataReader(DataReader.FILEPATH);
		this.dataReader.readCryptoData();
		List<Crypto> cryptoCollection = dataReader.getCryptoCollection(); 
		
		this.cryptoObservableList = FXCollections.observableList(cryptoCollection);
	}
	/**
	 * Gets the observable list of cryptos
	 * @return obserable list of cryptos
	 */
	public ObservableList<Crypto> getCryptoCollection() {
		return this.cryptoObservableList;
	}
	
	
}
