package CentralServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
//import java.util.HashMap;

import Peer.PeerClientIF;

public class PeerServer extends UnicastRemoteObject implements PeerServerIF {
	private static final long serialVersionUID = 1L;
	private ArrayList<PeerClientIF> peerClients = new ArrayList<PeerClientIF>();
	//private HashMap<String, PeerClientIF> peerClients = new HashMap<String, PeerClientIF>();
	protected PeerServer() throws RemoteException {
		super();
	}
		
	public synchronized String registerPeerClient(PeerClientIF peerClient) throws RemoteException {
		//register peer and log files
		peerClients.add(peerClient);	
		//peerClients.put(peerClient.getName(), peerClient);
		
		//list files
		String peerfiles = "";
		String[] files = peerClient.getFiles();	
		for (int i=0; i<files.length; i++){
			peerfiles += "\n\t- "+files[i];
		}
		
		System.out.println("\n\nNew Peer has registered to Server. See list of registered Peers Below:");
		displayRegisteredPeers();
		
		return "Peer '"+peerClient.getName()+"' has registered with central server and logged the following files"+peerfiles;
	}
	
	public boolean updatePeerClient(PeerClientIF peerClient) throws RemoteException {
		for (int l=0; l<peerClients.size(); l++ ) {
			if(peerClient.getName().equals(peerClients.get(l).getName())){
				peerClients.remove(l);
				peerClients.add(peerClient);
			}
		}
		
		//peerClients.remove(peerClient.getName());
		//peerClients.put(peerClient.getName(), peerClient);
		
		System.out.println("\n\nServer has been updated. See list of registered Peers Below:");
		displayRegisteredPeers();
		return true;
	}
	
	private void displayRegisteredPeers() throws RemoteException {
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
		
	}

	public synchronized PeerClientIF[] searchFile(String file, String requestingPeer) throws RemoteException {
		System.out.println("\nPeer '"+requestingPeer+"' has requested a file...");
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
			System.out.println("A list of Peers containing the file '"+file+"' has been sent to "+requestingPeer);
			return peer;
		} else
			System.out.println("The file: '"+file+"' was not found in any Peer");
			return null;
	}
}
