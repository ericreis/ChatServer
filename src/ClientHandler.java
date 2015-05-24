import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Models.Message;

public class ClientHandler extends ChatServer implements Runnable
{
	private Socket sock;
	private ArrayList<ObjectOutputStream> clientOutputStreamList;
	private ObjectInputStream inputStream;

	public ClientHandler(Socket clientSocket, ArrayList<ObjectOutputStream> clientOutputStreamList)
	{
		try
		{	
			this.clientOutputStreamList = clientOutputStreamList;
			this.sock = clientSocket;
			this.inputStream = new ObjectInputStream(this.sock.getInputStream());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		Message msg;
		
		try
		{
			
			while ((msg = (Message) this.inputStream.readObject()) != null)
			{
				System.out.println("Read: " + msg);
				this.tellEveryone(msg, this.clientOutputStreamList);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}	
}
