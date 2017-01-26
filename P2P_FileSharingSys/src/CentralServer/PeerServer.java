package CentralServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


import Peer.PeerClientIF;

public class PeerServer extends UnicastRemoteObject implements PeerServerIF {
	private static final long serialVersionUID = 1L;
	private ArrayList<PeerClientIF> peerClients = new ArrayList<PeerClientIF>();
	protected PeerServer() throws RemoteException {
		super();
	}
		
	public synchronized String registerPeerClient(PeerClientIF peerClient) throws RemoteException {
		//register peer and log files
		peerClients.add(peerClient);	
			
		String peerfiles = "";
		String[] files = peerClient.getFiles();	
		//list files
		for (int i=0; i<files.length; i++){
			peerfiles += "\n\t- "+files[i];
		}
		
		//delete this section later
		System.out.println("\n\nNew Peer has registered to Server. See list of registered Peer Below:");
		for (int l=0; l<peerClients.size(); l++ ) {
			System.out.println("------------------------------------------------------------------------------");
			System.out.println("Name: "+peerClients.get(l).getName());
			System.out.println("Root Directory: "+peerClients.get(l).getPeerDir());
			System.out.println("Files: ");
			String[] filelist = peerClients.get(l).getFiles();
			for (int a=0; a<filelist.length; a++) {
				System.out.println("\t"+filelist[a]);
			}
		}
		
		return "Peer '"+peerClient.getName()+"' has registered with central server and logged the following files"+peerfiles;
	}

	public synchronized PeerClientIF[] searchFile(String file) throws RemoteException {
		Boolean filefound = false;
		PeerClientIF[] peer = new PeerClientIF[peerClients.size()];
		int count = 0;
		for (int l=0; l<peerClients.size(); l++ ) {
			String[] filelist = peerClients.get(l).getFiles();
			for (int a=0; a<filelist.length; a++) {
				if (file.equals(filelist[a])) {
					filefound = true;
					peer[count] = (PeerClientIF) peerClients.get(l);
					count++;
				}
			}
		}
		if(filefound) {
			System.out.println("\nA list of Peers containing the file has been sent to requesting Peer");
			return peer;
		} else
			return null;
	}
}
