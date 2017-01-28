package Peer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import CentralServer.PeerServerIF;

public class PeerClientDriver {
	private static final String INDEX_SERVER = "localhost";

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		
		if (args.length == 3) {
			//link server and client
			String peerServerURL = "rmi://"+INDEX_SERVER+":"+args[0]+"/peerserver";
			PeerServerIF peerServer = (PeerServerIF) Naming.lookup(peerServerURL);
			String peerClientURL = "rmi://"+INDEX_SERVER+":"+args[1]+"/clientserver";
			Naming.rebind(peerClientURL,new PeerClient());
			//invoke thread
			new Thread(new PeerClient(args[2], peerServer)).start();			
		} else {
			System.err.println("Usage: PeerClientDriver <port_no> <Peer_name>");
		}
	}


}
