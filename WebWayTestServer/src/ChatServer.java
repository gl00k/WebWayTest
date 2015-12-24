import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
	  private static ServerSocket serverSocket = null;
	  private static Socket clientSocket = null;
	  private static final int maxClientsCount = 10;
	  private static final ThreadClient[] threads = new ThreadClient[maxClientsCount];
	  
	  public static void main(String args[]) {
		  int portNumber = 4444;
		  try {
		      serverSocket = new ServerSocket(portNumber);
		    } catch (IOException e) {
		      System.out.println(e);
		    }
		  while (true) {
		      try {
		        clientSocket = serverSocket.accept();
		        int i = 0;
		        for (i = 0; i < maxClientsCount; i++) {
		          if (threads[i] == null) {
		            (threads[i] = new ThreadClient(clientSocket, threads)).start();
		            break;
		          }
		        }
		      } catch (IOException e) {
		        System.out.println(e);
		      }
		    }
	  }
}
