import java.io.*;
import java.net.*;
import java.util.*;

/*
 * Server to process ping requests over UDP.
 */
public class UNCC_Pinger
{
   private static final double LOSS_RATE = 0.3;
   private static final int AVERAGE_DELAY = 100;  // milliseconds

   public static void main(String[] args) throws Exception
   {
      // Get command line argument.
      if (args.length != 2) {
         System.out.println("Required arguments: target port");
         return;
      }
	  String target = args[0];
      int port = Integer.parseInt(args[1]);
		
	  
      // Create a datagram socket for receiving and sending UDP packets
      // through the port specified on the command line.
      DatagramSocket socket = new DatagramSocket(port);
	  InetAddress targetHost= InetAddress.getByName(target);
		long sendTime;
		long receiveTime;
      // Processing loop.
      for (int i=0;i<10;i++) {
		  
         // Send message.
         sendTime=System.currentTimeMillis();
         int targetPort = port;
         String data = "PING"+i+sendTime+"CRLF";
		 byte[] buf = data.getBytes();
         DatagramPacket message = new DatagramPacket(buf, buf.length, targetHost, port);
         socket.send(message);

         System.out.println("   Message sent.");		  
		  
		  
	
		  
		  
         // Create a datagram packet to hold incomming UDP packet.
         DatagramPacket reply = new DatagramPacket(new byte[1024], 1024);
		 
		 // makes it so if packet is lost i can time out.
	     socket.setSoTimeout(1000);
		 
		 try {
         // 
         socket.receive(reply);
        
         receiveTime=System.currentTimeMillis();
		 long TimetoReply=receiveTime-sendTime;
		 String printable="Reply from "+ targetHost +" Bytes= "+buf.length+ "time= " +TimetoReply; 
		 System.out.println( printable);
		 // Print the recieved data.
         printData(reply);
		 } catch (Exception e) {
			 System.out.println("packet lost");
		 
		 }


      }
   }

   /*
    * Print ping data to the standard output stream.
    */
   private static void printData(DatagramPacket request) throws Exception
   {
      // Obtain references to the packet's array of bytes.
      byte[] buf = request.getData();

      // Wrap the bytes in a byte array input stream,
      // so that you can read the data as a stream of bytes.
      ByteArrayInputStream bais = new ByteArrayInputStream(buf);

      // Wrap the byte array output stream in an input stream reader,
      // so you can read the data as a stream of characters.
      InputStreamReader isr = new InputStreamReader(bais);

      // Wrap the input stream reader in a bufferred reader,
      // so you can read the character data a line at a time.
      // (A line is a sequence of chars terminated by any combination of \r and \n.)
      BufferedReader br = new BufferedReader(isr);

      // The message data is contained in a single line, so read this line.
      String line = br.readLine();

      // Print host address and data received from it.
      System.out.println(
         "reply from " +
         request.getAddress().getHostAddress() +
         ": " +
         new String(line) );
   }
}