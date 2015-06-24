import java.net.*;
import java.util.ArrayList;
import java.io.*;

import org.omg.CORBA.INTERNAL;

public class server_UDP {
	public enum TypeConn{SAME_NAT,PUBLIC_IP,BEHIND_NAT,REPLY};
	public static DatagramSocket server;
	static byte[] receiveData = new byte[1024];
	public static void main(String args[]) throws Exception {
		ArrayList<peerInfor> peers = new ArrayList<peerInfor>();
		boolean isExist=false;
		int indexPeer=0;
		server = new DatagramSocket(12345);
		while (true) {
			System.out.println("server waiting connect to port 12345");
			DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
			server.receive(receivePacket);
			
			//infor from message received
			String receiveMsg = new String(receivePacket.getData(),0,receivePacket.getLength());
			System.out.println("peer sent "+receiveMsg);
			String split[]=receiveMsg.split("-",4);
			if(split.length!=4) continue;
			peerInfor peer=new peerInfor(split[0],receivePacket.getAddress(),receivePacket.getPort(),InetAddress.getByName(split[1]),Integer.parseInt(split[2]));
		
			for(int i=0;i<peers.size();i++){
				if(String.valueOf(peer.address).equals(String.valueOf(peers.get(i).address)) && peer.port==peers.get(i).port){
					indexPeer=i;
					isExist=true;
					break;
				}
			}
			if( !isExist) {
				peers.add(peer);
				indexPeer=peers.size()-1;
			}
			System.out.println("peer connect: "+indexPeer+"  "+receivePacket.getAddress()+ "_" +receivePacket.getAddress().getHostAddress()+ "_" +receivePacket.getPort());
			//Thread.sleep(1000);
			if(split[3]=="") break;
			for(peerInfor p :peers){
				String send1,send2;
				if (split[3].equals(p.username)){
					byte[] sendData1 = new byte[1024];
					
					/* getAllByName compare
					if(String.valueOf(peer.address).equals(String.valueOf(p.address))){
						//SAME NAT
						send1= String.valueOf(p.portPri+ "_" + p.addressPri.toString());
						send2= String.valueOf(peer.portPri+ "_" + peer.addressPri.toString());
					}
					else */ 
					{
						send1= String.valueOf(p.port+ "_" + p.address.toString());
						send2= String.valueOf(peer.port+ "_" + peer.address.toString());
					}
					sendData1 = send1.getBytes(); 
					DatagramPacket sendPacket1 = new DatagramPacket(sendData1, sendData1.length,peer.address, peer.port);
					server.send(sendPacket1);
					
					byte[] sendData2 = new byte[1024];
					
					sendData2 = send2.getBytes(); 
					DatagramPacket sendPacket2 = new DatagramPacket(sendData2, sendData2.length,p.address, p.port);
					server.send(sendPacket2);
					break;
				}
			}
		}
	}
}
