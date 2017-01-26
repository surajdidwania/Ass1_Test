package CentralServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class PeerServerDriver {
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		System.setProperty("java.rmi.server.hostname","localhost");		//192.168.107.1
		Naming.rebind("RMIPeerServer", new PeerServer());
		System.out.println("Server is Ready");
	}
}
