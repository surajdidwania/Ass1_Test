package Peer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import CentralServer.PeerServerIF;

public class PeerClientDriver {
	private static final String INDEX_SERVER = "localhost";

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		
		if (args.length == 1) {
			//link server and client
			String peerServerURL = "rmi://"+INDEX_SERVER+"/RMIPeerServer";
			PeerServerIF peerServer = (PeerServerIF) Naming.lookup(peerServerURL);
			
			//invoke thread
			new Thread(new PeerClient(args[0], peerServer)).start();			
		} else {
			System.err.println("Usage: PeerClientDriver <Peer_name>");
		}
	}

}
