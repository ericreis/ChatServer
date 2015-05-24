import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import Models.Message;

public class ChatServer
{
	private final int port = 5000;
	
//	private ArrayList<PrintWriter> clientOutputStreams;
	private ArrayList<ObjectOutputStream> clientOutputStreamList;
	private ServerSocket serverSock;
	
	public void go()
	{
		this.clientOutputStreamList = new ArrayList<ObjectOutputStream>();
		
		try
		{
			this.serverSock = new ServerSocket(this.port);
			
			while (true)
			{
				Socket clientSocket = this.serverSock.accept();
				ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
//				PrintWriter writer =  new PrintWriter(clientSocket.getOutputStream());
				this.clientOutputStreamList.add(outputStream);
				
				Thread t = new Thread(new ClientHandler(clientSocket, this.clientOutputStreamList));
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
	
	protected void tellEveryone(Message msg, ArrayList<ObjectOutputStream> clientOutputStreamList)
	{	
		this.clientOutputStreamList = clientOutputStreamList;
		
		try
		{
			Iterator<ObjectOutputStream> it = this.clientOutputStreamList.iterator();
			
			while (it.hasNext())
			{
				try
				{
					ObjectOutputStream outputStream = it.next();
					
					outputStream.writeObject(msg);;
					outputStream.flush();
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
