package Peer;

import java.rmi.Remote;
import java.rmi.RemoteException;

import CentralServer.PeerServerIF;

public interface PeerClientIF extends Remote {
	String[] getFiles() throws RemoteException;
	String getName() throws RemoteException;
	String getPeerDir() throws RemoteException;
	PeerServerIF getPeerServer() throws RemoteException;
	boolean sendFile(PeerClientIF peerClient, String filename) throws RemoteException;
	boolean acceptFile(String name, byte[] mydata, int mylen) throws RemoteException;
}
