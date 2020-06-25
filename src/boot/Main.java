package boot;

import server.*;

public class Main {
	public static void main(String args[]) {
		Server server = new MySerialServer(7070);
		server.start(new MyClientHandler());
	}
}
