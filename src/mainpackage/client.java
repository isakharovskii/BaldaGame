package mainpackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class client {
	 static Socket socket;
	 static int port = 10101;
	 private static InetAddress toConnect ;
	 private static String ServerIP = "127.0.0.1";
	 
	 public static void host(int n)
	 {
		 try {
			socket = new Socket(ServerIP, port);
			
			OutputStream os = socket.getOutputStream();
			
			DataOutputStream out = new DataOutputStream(os);
			
			out.writeInt(0);
			out.writeInt(n);
			out.writeUTF(InetAddress.getLocalHost().getHostAddress().toString());
			out.writeInt(2);
			
			socket.close();
			//System.out.println("MyAddress: " + InetAddress.getLocalHost().getHostAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}	 
	 }
	 
	 public static void join(int n)
	 {
		 try{
			 System.out.println("IP: " + ServerIP);
			 socket = new Socket(ServerIP, port);
			 
			 OutputStream os = socket.getOutputStream();
			 InputStream is = socket.getInputStream();
			 
			 DataInputStream in = new DataInputStream(is);
			 DataOutputStream out = new DataOutputStream(os);
			 
			 out.writeInt(1);
			 
			 out.writeInt(n);
			 String str = in.readUTF();
			 if (str.equals("Error")) 
			 {
				 System.out.println("No Host");
				 out.writeInt(2);
				 socket.close();
				 System.exit(0);
			 }
			 String [] res = str.split(">");
			 toConnect = InetAddress.getByName(res[0]);
			 
			 System.out.println("Client got " + str + "  " + toConnect.toString());
			 out.writeInt(2);
			 socket.close();
			 
		 } catch (Exception e)
		 {
			 e.printStackTrace();
		 }
	 }
	 public static void setIp(String str)
	 {
		 ServerIP = str;
	 }
	 public static InetAddress getAddress()
	 {
		 return toConnect;
	 }
}
