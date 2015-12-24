import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ThreadClient extends Thread{
	private String clientName = null;
	private DataInputStream is = null;
	private PrintStream os = null;
	private Socket clientSocket = null;
	private final ThreadClient[] threads;
	private int maxClientsCount;
	public ThreadClient(Socket clientSocket, ThreadClient[] threads) {
		this.clientSocket = clientSocket;
	    this.threads = threads;
	    maxClientsCount = threads.length;
	  }
	
	public void run() {
	    int maxClientsCount = this.maxClientsCount;
	    ThreadClient[] threads = this.threads;
	    try {
	        is = new DataInputStream(clientSocket.getInputStream());
	        os = new PrintStream(clientSocket.getOutputStream());
	        String name;
	        while (true) {
	          os.println("Введите Ваше имя");
	          name = is.readLine().trim();
	          break;
	        }
	        os.println("Привет "+name);
	        synchronized (this) {
	            for (int i = 0; i < maxClientsCount; i++) {
	              if (threads[i] != null && threads[i] == this) {
	                clientName = name;
	                break;
	              }
	            }
	        }
	        while (true) {
	            String line = is.readLine(); 
	            if (line.startsWith("exit")) {
	                break;
	              }
	        synchronized (this) {
	            for (int i = 0; i < maxClientsCount; i++) {
	              if (threads[i] != null && threads[i].clientName != null) {
	                threads[i].os.println("_" + name + "_ " + line);
	              }
	            }
	          }
	        }
	        synchronized (this) {
	            for (int i = 0; i < maxClientsCount; i++) {
	              if (threads[i] == this) {
	                threads[i] = null;
	              }
	            }
	          }
	        is.close();
	        os.close();
	        clientSocket.close();
	    } catch (IOException e) {
	    }
	}
}
