import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ChatServer
{
	private final int port = 5000;
	
	private ArrayList<PrintWriter> clientOutputStreams;
	private ServerSocket serverSock;
	
	public void go()
	{
		this.clientOutputStreams = new ArrayList<PrintWriter>();
		
		try
		{
			this.serverSock = new ServerSocket(this.port);
			
			while (true)
			{
				Socket clientSocket = this.serverSock.accept();
				PrintWriter writer =  new PrintWriter(clientSocket.getOutputStream());
				this.clientOutputStreams.add(writer);
				
				Thread t = new Thread(new ClientHandler(clientSocket, this.clientOutputStreams));
				t.start();
				
				System.out.println("Got a connection from " + clientSocket.getInetAddress().toString() + " !");
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				this.serverSock.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	protected void tellEveryone(String msg, ArrayList<PrintWriter> clientOutputStreams)
	{	
		this.clientOutputStreams = clientOutputStreams;
		
		try
		{
			Iterator<PrintWriter> it = this.clientOutputStreams.iterator();
			
			while (it.hasNext())
			{
				try
				{
					PrintWriter writer = it.next();
					
					writer.println(msg);
					writer.flush();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
