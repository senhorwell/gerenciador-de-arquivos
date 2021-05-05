import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

// Feito por
// Anderson Yoshio Makino
// Wellington Luiz da Silva Junior
public class Client {
	
	private static int port = 8001;
	private static int fileSize = 1024;
	public static void main(String[] args) throws Exception {
		DatagramSocket rcp = new DatagramSocket();
		message (rcp);
		rcp.close();
	}
	
	private static void message(DatagramSocket rcp) throws IOException {
		InetAddress address = InetAddress.getByName("127.0.0.1");
		String sMessage;
		Scanner scann = new Scanner(System.in);
		byte[] message = new byte[1024];
		byte[] receiveMessage = new byte[1024];
		// 1 - Excluir
		// 2 - Adicionar
		// 3 - Editar
		// 4 - Ler
		System.out.print("Insira a opção:");
		sMessage = scann.next();
		message = sMessage.getBytes();
		DatagramPacket receivePacket = new DatagramPacket(receiveMessage,receiveMessage.length);
		DatagramPacket sendPacket = new DatagramPacket(message,message.length,address,port);
		rcp.receive(receivePacket);
		rcp.send(sendPacket);
	}
}
