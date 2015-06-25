import java.net.*;
import java.io.*;

public class peerInfor {
	

	
	String username=null;
	InetAddress address=null;
	int port=0;
	InetAddress addressPri=null;
	int portPri=0;
	
	public peerInfor(){
		username=null;
		address=null;
		port=0;
	}
	public peerInfor(InetAddress add, int p){
		username=null;
		address=add;
		port=p;
	}
	public peerInfor( String name, InetAddress add, int p,InetAddress addPri, int pPri){
		username=name;
		address=add;
		port=p;
		addressPri=addPri;
		portPri=pPri;
	}
}
