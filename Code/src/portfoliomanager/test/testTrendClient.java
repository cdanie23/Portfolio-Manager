package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import portfoliomanager.client.TrendClient;

@TestInstance(Lifecycle.PER_CLASS)
class testTrendClient {
	private PubMockServer pubServer;
    private Thread serverThread;
    private TrendClient trendClient;

    @BeforeEach
    public void setup() {
        this.pubServer = new PubMockServer();
        this.trendClient = new TrendClient();
        this.trendClient.setServerPort("5550");
        this.serverThread = new Thread(() -> this.pubServer.start("tcp://127.0.0.1:5550"));
        this.serverThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void teardown() {
        
        this.pubServer.stop();
        try {
        	this.serverThread.join();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    @Test
    public void testTrendClientReceivesData() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        this.trendClient.start(data -> {
            assertNotNull(data);
            assertTrue(data.containsKey("Bitcoin"));
            assertTrue(data.containsKey("Ethereum"));
            assertEquals(new BigDecimal("5000"), data.get("Bitcoin"));
            assertEquals(new BigDecimal(55.230), data.get("Ethereum"));
            latch.countDown();
        });
        assertTrue(latch.await(1, TimeUnit.SECONDS));
        this.trendClient.stop();
    }
}
