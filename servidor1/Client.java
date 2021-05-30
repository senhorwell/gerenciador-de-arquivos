import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		
		String msg = "";
		ArrayList<String> my;
		
		while(true){
			Socket socket = new Socket("127.0.0.1", 6001);
			my = null;
			my =  new ArrayList<String>();
			System.out.println("-------------------------------------------");
			System.out.println("Gerenciador de Arquivos Distribuidos");
			System.out.println("-------------------------------------------");
			System.out.println("Escolha sua operacao:");
			System.out.println("1 - Listar os arquivos da pasta atual");
			System.out.println("2 - Criar novo arquivo");
			System.out.println("3 - Renomear arquivo");
			System.out.println("4 - Remover arquivo");
			System.out.println("5 - Sair do Programa");
			System.out.print("Resposta: ");
			msg = sc.nextLine();
			my.add(msg);
			switch(msg){
				case "1":
					my.add("listagem");
					break;
				case "2":
					System.out.print("Insira o nome do arquivo: ");
					my.add(sc.nextLine());
					break;
				case "3":
					System.out.print("Insira o nome do arquivo antigo: ");
					my.add(sc.nextLine());
					System.out.print("Insira o novo nome: ");
					my.add(sc.nextLine());
					break;
				case "4":
					System.out.print("Insira o nome do arquivo: ");
					my.add(sc.nextLine());
					break;
				case "5":
					socket.close();
					System.exit(0);
				case "6":
					my.add("listagem");
					break;
				default:
					return;
			}
			upload(socket,my);
			download(socket);
			socket.close();
		}
	}
	
	private static void upload(Socket socket, ArrayList<String> my) throws IOException {
		OutputStream output = socket.getOutputStream();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);

		out.writeObject(my);

		byte[] saida = baos.toByteArray();
		
		output.write(saida);
	}
	
	
	private static void download(Socket socket) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String input2 = input.readLine();

		System.out.println("Resposta: " + input2);
		return;
	}

}