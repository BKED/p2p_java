import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client_test {
	public static void main(String args[]) throws Exception {
		DatagramSocket clientSocket;

		//InetAddress ipSer2=InetAddress.getByName(host);

		try {
			//InetAddress ipSer1=InetAddress.getByName("108.179.207.175");
			InetAddress ipSer1=InetAddress.getByName("14.161.65.97");
			//InetAddress ipSer1=InetAddress.getByName("localhost");
			clientSocket = new DatagramSocket();
			
			

			System.out.println("send to server");
			
			Scanner reader=new Scanner(System.in);
			System.out.println("Nhap vao ten user");
			String mess=reader.nextLine();
			
			//getLocalHost dose not work with many interface, TO FIX
			mess+="-"+InetAddress.getLocalHost().getHostAddress() +"-"+clientSocket.getLocalPort()+"-";
			System.out.println("Muon chat voi");
			mess+=reader.nextLine();
			
			byte[] bufSend=new byte[512];
			bufSend=mess.getBytes();
			DatagramPacket dataSend=new DatagramPacket(bufSend, bufSend.length,ipSer1,12345);
			clientSocket.send(dataSend);
			System.out.println("have sent to server");


			int portPeer;
			InetAddress ipPeer;
			byte[] bufRecive=new byte[512];
			DatagramPacket receivePacket = new DatagramPacket(bufRecive,bufRecive.length);
			clientSocket.receive(receivePacket);
			System.out.println("receive from server "+receivePacket.getAddress()+"  "+receivePacket.getPort());


			String receiveMsg = new String(receivePacket.getData(),0,receivePacket.getLength());
			System.out.println("FROM SERVER:" + receiveMsg);

			int index = 0;
			while (receiveMsg.charAt(index) != '_') {
				index++;
			}
			char[] port = new char[index];
			receiveMsg.getChars(0, index, port, 0);

			char[] address = new char[receivePacket.getLength() - index
			                          - 2];
			receiveMsg.getChars(index + 2, receivePacket.getLength(),
					address, 0);
			ipPeer=InetAddress.getByName(String.valueOf(address));
			portPeer=Integer.parseInt(String.valueOf(port));
			for(int j=0;j<20;j++){
				System.out.println("send to peer");
				String messPeer="hello peer";
				byte[] bufSendPeer=new byte[512];
				bufSendPeer=messPeer.getBytes();
				DatagramPacket dataSendPeer=new DatagramPacket(bufSendPeer, bufSendPeer.length,ipPeer,portPeer);
				clientSocket.send(dataSendPeer);
				
				System.out.println("sent to peer "+portPeer);
				byte[] bufRecivePeer=new byte[512];
				DatagramPacket receivePacketPeer = new DatagramPacket(bufRecivePeer,bufRecivePeer.length);
				clientSocket.receive(receivePacketPeer);
				System.out.println("receive from peer "+receivePacketPeer.getAddress()+"  "+receivePacketPeer.getPort()+new String(receivePacketPeer.getData(),0,receivePacketPeer.getLength()));		
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}