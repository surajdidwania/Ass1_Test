package Peer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

//import org.apache.commons.io.FileUtils;

import CentralServer.PeerServerIF;


//to implement instances of this class as threads use 'Runnable' which will add the 'run' method
public class PeerClient extends UnicastRemoteObject implements PeerClientIF, Runnable { 
	private static final long serialVersionUID = 1L;
	private PeerServerIF peerServer;
	private String name = null;
	private String peerRootDirectoryPath = null;
	private String[] files;

	protected PeerClient(String name, PeerServerIF peerServer) throws RemoteException {
		this.name = name;
		this.peerServer = peerServer;
		
		//create list of files in peer root directory
		try{
			//get peer root directory pathname
			this.peerRootDirectoryPath = System.getProperty("user.dir");
		    System.out.print("Peer Directory is: "+peerRootDirectoryPath.replace("\\", "/")+"\n");
		    
		    //get all files in peer root directory
			File f = new File(peerRootDirectoryPath);
			this.files = f.list();	//returns directory and files (with extension) in directory
			
		}catch (Exception e){
		    System.out.println("Peer path Exception caught ="+e.getMessage());
		}		

		//register peer data structure, including files array
		System.out.println(peerServer.registerPeerClient(this));
	}
	
	public String getName() {
		return name;
	}
	public String[] getFiles() {
		return files;
	}
	public String getPeerDir() {
		return peerRootDirectoryPath;
	}
	public PeerServerIF getPeerServer() {
		return peerServer;
	}
	
	public synchronized boolean acceptFile(String filename, byte[] data, int len) throws RemoteException{
        try{
        	File f=new File(filename);
        	f.createNewFile();
        	FileOutputStream out=new FileOutputStream(f,true);
        	out.write(data,0,len);
        	out.flush();
        	out.close();
        	System.out.println("Done writing data...");
        }catch(Exception e){
        	e.printStackTrace();
        }
		return true;
	}
	
	public synchronized boolean sendFile(PeerClientIF c, String file) throws RemoteException{
		/*
		 * Sending The File...
		 * file: receives the file path of file location 
		 * that was sent to requesting peer from central server
		 * c: requesting Peer client
		 */
		 try{
			 File f1 = new File(file);			 
			 FileInputStream in = new FileInputStream(f1);			 				 
			 byte[] mydata = new byte[1024*1024];						
			 int mylen = in.read(mydata);
			 while(mylen>0){
				 if(c.acceptFile(f1.getName(), mydata, mylen)){
					 System.out.println("File has been sent to Requesting Peer");
				 } else {
					 System.out.println("Fault: File was NOT sent");
				 }
				 mylen = in.read(mydata);				 
			 }
		 }catch(Exception e){
			 e.printStackTrace();
			 
		 }
		
		return true;
	}

	public void run() {
		//read messages from command line
		Scanner cmdline = new Scanner(System.in);
		String command, task, filename;
		while (true) {	//continue reading commands
			command = cmdline.nextLine();
			CharSequence symbol = " ";
			//wait till command is received
			if (command.contains(symbol)) {
				task = command.substring(0, command.indexOf(' '));
				filename = command.substring(command.indexOf(' ')+1);
				System.out.println("You want to download the file: "+filename);
				
				if (task.equals("download")) {
					PeerClientIF[] peer;	//peer client that contains file
					try {
						//returns a peer with file
						peer = peerServer.searchFile(filename);
						if (peer != null) {
							//list peers with file
							System.out.println("The following Peers has the file you want:");
							for (int i=0; i<peer.length; i++) {
								if (peer[i] != null)
									System.out.println((i+1)+". "+peer[i].getName());
							}
							//prompt user to choose Peer to download from
							System.out.println("Enter number matching the Peer you will like to download from");
							
							int choice = cmdline.nextInt();
							/*
							 * establish connection with the peer
							 * then call sendFile with the client of this peer as parameter
							 */
							if(peer[choice-1].sendFile(this, filename)){
								System.out.println("File has been downloaded");
							} else {
								System.out.println("Fault: File was NOT downloaded");
							}
							//sendFile(peer[choice-1], filename);
						} else
							System.out.println("File not found");
	
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}	
				} else {
					System.out.println("Usage: <task> <item>");
				}
			}
		}
	}
}
