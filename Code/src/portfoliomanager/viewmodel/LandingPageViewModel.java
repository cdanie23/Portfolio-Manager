package portfoliomanager.viewmodel;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import portfoliomanager.datareader.DataReader;
import portfoliomanager.model.Crypto;

/**
 * The view model for the landing page
 * 
 * @author Colby
 * @version Spring 2025
 */
public class LandingPageViewModel {
	private DataReader dataReader;
	private ObservableList<Crypto> cryptoObservableList;
	/**
	 * Instantiates an instance of the view-model
	 * @post this.dataReader != null, this.cryptoObservableList != null
	 */
	public LandingPageViewModel() {
		this.dataReader = new DataReader(DataReader.FILEPATH);
		this.dataReader.readCryptoData();
		List<Crypto> cryptoCollection = this.dataReader.getCryptoCollection(); 
		
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
