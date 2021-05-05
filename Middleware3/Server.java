import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

// Feito por
// Anderson Yoshio Makino
// Wellington Luiz da Silva Junior
public class Server {
	
	private static int port = 8003;
	private static int fileSize = 1024;
	private static FileInputStream fis;

	public static void main(String[] args) throws Exception {
		DatagramSocket rcp = new DatagramSocket(port);
		message (rcp);
		rcp.close();
	}
	
	private static void message(DatagramSocket rcp) throws IOException {
		InetAddress address = InetAddress.getByName("127.0.0.1");
		String sMessage;
		byte[] message = new byte[1024];
		byte[] receiveMessage = new byte[1024];
		sMessage = leitor();
		message = sMessage.getBytes();
		DatagramPacket receivePacket = new DatagramPacket(receiveMessage,receiveMessage.length);
		DatagramPacket sendPacket = new DatagramPacket(message,message.length,address,port);
		rcp.receive(receivePacket);
		rcp.send(sendPacket);
	}

	public static String leitor() throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader("..\\archive.txt"));
		String linha = "";
		while (true) {
			if (linha == null)
				break;
			linha = linha + buffRead.readLine();
		}
		buffRead.close();
		return linha;
	}

	public static void escritor(String linha) throws IOException {
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter("..\\archive.txt"));
		buffWrite.append(linha + "\n");
		buffWrite.close();
	}

}
