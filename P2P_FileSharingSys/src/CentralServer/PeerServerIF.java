package CentralServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Peer.PeerClientIF;

public interface PeerServerIF extends Remote {
	String registerPeerClient(PeerClientIF peerClient) throws RemoteException;
	PeerClientIF[] searchFile(String file, String requestingPeer) throws RemoteException;
	boolean updatePeerClient(PeerClientIF peerClient) throws RemoteException;
}
