package portfoliomanager.model;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Client extends Thread {
	
	@Override
	public void run() {
        Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Connecting to hello world server");

        Socket socket = context.socket(ZMQ.REQ);
        socket.connect("tcp://127.0.0.1:5555");

        for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
            String request = "Hello";
            System.out.println("Client - Sending Hello " + requestNbr);
            socket.send(request.getBytes(ZMQ.CHARSET), 0);

            byte[] reply = socket.recv(0);
            String response = new String(reply, ZMQ.CHARSET);
			System.out.println("Client - Received " + response + " " + requestNbr);
        }
        

        String request = "exit";
        System.out.println("Client - Sending exit");
        socket.send(request.getBytes(ZMQ.CHARSET), 0);

        socket.close();
        context.term();
	}

}
