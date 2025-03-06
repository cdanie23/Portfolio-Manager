package portfoliomanager.test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import portfoliomanager.datareader.DataReader;
import portfoliomanager.model.Crypto;


class TestDataReader {
	private File tempFile;
	
	@BeforeEach
	void setUp(@TempDir Path tempDir) throws IOException {
		this.tempFile = tempDir.resolve("BTC-USD_data.txt").toFile();
	}
	
	@Test
	void testReadCryptoData(@TempDir Path tempDir) throws IOException {
        try (FileWriter writer = new FileWriter(this.tempFile)) {
            writer.write("Date,Price\n"); 
            writer.write("2024-01-01,50000\n");
            writer.write("2024-01-02,51000\n");
        }
        DataReader dataReader = new DataReader(tempFile.getAbsolutePath());
        dataReader.readCryptoData();
        List<Crypto> collection = dataReader.getCryptoCollection();
        assertAll(()-> assertFalse(collection.isEmpty()),
        		()-> assertEquals(50000.0, collection.get(0).getCurrentPrice()));
        
	}
	
	@Test
	void testFileNotFound() {
		assertThrows(IllegalArgumentException.class, ()-> {
			new DataReader("testFile.txt");
		});
	}
	
	@Test
	void testNullFile() {
		assertThrows(IllegalArgumentException.class, ()-> new DataReader(null));
	}
	
	@Test
	void testBlankFileName() {
		assertThrows(IllegalArgumentException.class, ()-> new DataReader("  "));
	}
	
	@Test
	void testBlankFile() throws IOException {
		try (FileWriter writer = new FileWriter(this.tempFile)) {
            writer.write("Date,Price\n");
        }
		DataReader dataReader = new DataReader(tempFile.getAbsolutePath());
        assertThrows(NoSuchElementException.class, ()-> {
        	dataReader.readCryptoData();
        });
	}
	
	@Test
    void testReadInvalidPrice() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Date,Price\n");
            writer.write("2024-01-01,50000\n");
            writer.write("2024-01-02,INVALID_PRICE\n");
        }

        DataReader reader = new DataReader(tempFile.getAbsolutePath());

        assertThrows(NumberFormatException.class, () -> reader.readCryptoData());
    }
	
	@Test
	void testFileContainsLessColumn() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Date,Price\n");
            writer.write("2024-01-01,50000\n");
            writer.write("2024-01-02\n");
        }
        DataReader reader = new DataReader(tempFile.getAbsolutePath());

        assertThrows(IllegalArgumentException.class, () -> reader.readCryptoData());
	}
	
	@Test
	void testFileContainsMoreColumn() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Date,Price\n");
            writer.write("2024-01-01,50000\n");
            writer.write("2024-01-02,50000,asd\n");
        }
        DataReader reader = new DataReader(tempFile.getAbsolutePath());

        assertThrows(IllegalArgumentException.class, () -> reader.readCryptoData());
	}
	
 }
