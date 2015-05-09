import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends ChatServer implements Runnable
{
	private BufferedReader reader;
	private Socket sock;
	private ArrayList<PrintWriter> clientOutputStreams;

	public ClientHandler(Socket clientSocket, ArrayList<PrintWriter> clientOutputStreams)
	{
		try
		{	
			this.clientOutputStreams = clientOutputStreams;
			this.sock = clientSocket;
			InputStreamReader isReader = new InputStreamReader(this.sock.getInputStream());
			this.reader = new BufferedReader(isReader);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		String msg;
		
		try
		{
			while ((msg = this.reader.readLine()) != null)
			{
				System.out.println("Read: " + msg);
				this.tellEveryone(msg, this.clientOutputStreams);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}	
}
