import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	
	public static Integer port = 6004;
	public static Integer remoteport1 = 6001;
	public static Integer remoteport2 = 6002;
	public static Integer remoteport3 = 6003;
	
	public static void main(String[] args) throws Exception {
		
		while(true){
			ServerSocket server = new ServerSocket(port);
			Socket socket = server.accept();
			if (download(socket) == 5) {
				break;
			}
			server.close();
		}
		
	}
	
	
	private static void upload (Socket socket,String msg) throws IOException {
		PrintStream ps = new PrintStream(socket.getOutputStream());
		ps.println(msg);
	}
	
	
	private static Integer download(Socket socket) throws IOException, ClassNotFoundException {
		Integer opcao = 0;
		String nomeArquivo = null;
		String antigoArquivo = null;
		String element;
		InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		ArrayList<String> in = (ArrayList<String>) objectInputStream.readObject();
		if (Integer.valueOf(in.get(0)) == 5) {
			System.exit(0);
		}

		Integer i=0;
		while (i < in.size()) {
			element = in.get(i);
			if(i==0){ //opcao
				opcao = Integer.valueOf(element);
			}
			if(i==1){ //caminho/nome do arquivo
				nomeArquivo = element;
			}
			if(i==2){ //antigo nome do arquivo
				antigoArquivo = element;
			}
			i++;
		}
		in.clear();
		switch(opcao){
			case 1:
				getListFiles(socket);
				break;
			case 2:
				setNewFile(socket,nomeArquivo);
				break;
			case 3:
				renameFile(socket,nomeArquivo,antigoArquivo);
				break;
			case 4:
				removeFile(socket,nomeArquivo);
				break;
			case 6:
				String msg = "" + getLowestFolder();
	    		upload(socket,msg);
			case 7:
				String msg2 = "" + getSizeFolder();
				upload(socket,msg2);
			default:
				break;
		}
		return opcao;
	}


	private static void removeFile(Socket socket, String nomeArquivo) throws IOException {
		try{
			File file = new File("./arquivos/" + nomeArquivo);

			file.delete();

			upload(socket,"Arquivo removido com sucesso");
		}catch(Exception e){
			upload(socket,"Erro ao excluir arquivo");
		}
	}


	private static void renameFile(Socket socket, String nomeArquivo, String antigoArquivo) throws IOException {
		try{
			File file = new File("./arquivos/" + nomeArquivo);

			file.renameTo(new File("./arquivos/" + antigoArquivo));

			upload(socket,"Arquivo renomeado com sucesso");
		}catch(Exception e){
			upload(socket,"Erro ao renomear arquivo");
		}
	}


	private static void setNewFile(Socket socket, String nomeArquivo) throws IOException {
		try{
			FileWriter arq = new FileWriter("./arquivos/" + nomeArquivo);
			PrintWriter gravarArq = new PrintWriter(arq);
			
			gravarArq.printf("blablabla");
			arq.close();

			upload(socket,"Arquivo criado com sucesso");
		}catch(Exception e){
			upload(socket,"Erro ao criar arquivo");
		}
	
		/*ArrayList<String> my = new ArrayList<String>();
		my.add("2");
		my.add(nomeArquivo);

		Socket socketemp = new Socket("127.0.0.1", remoteport1);
		OutputStream output = socketemp.getOutputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);

		out.writeObject(my);

		byte[] saida = baos.toByteArray();
		
		output.write(saida);*/
	}

	private static String getListFiles(Socket socket) throws IOException {
		File file = new File("./arquivos/");
		File[] arquivos = file.listFiles();
		String msg = ""; 
		for (File fileTmp : arquivos) {
			msg += fileTmp.getName();
			msg += "\t";
		}
		upload(socket,msg);
		return msg;
	}
	
	private static long getSizeFolder() throws IOException {
		File directory = new File("./arquivos/");
		long length = 0;
	    for (File file : directory.listFiles()) {
	        if (file.isFile())
	            length += file.length();
	        else
	            length += getSizeFolder();
	    }
		
		return length;
	}
	private static long getRemoteSizeFolder(Integer port) throws IOException {
		ArrayList<String> my = new ArrayList<String>();
		my.add("7");
		my.add("listagem");

		Socket socketemp = new Socket("127.0.0.1", port);
		OutputStream output = socketemp.getOutputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(my);
		byte[] saida = baos.toByteArray();
		output.write(saida);
		BufferedReader input = new BufferedReader(new InputStreamReader(socketemp.getInputStream()));
		String input2 = input.readLine();
		
		return Long.parseLong(input2);
	}
	private static Integer getLowestFolder() throws IOException{
		long s1,s2,s3,s4;

		s1 = getSizeFolder();
		s2 = getRemoteSizeFolder(remoteport1);
		s3 = getRemoteSizeFolder(remoteport2);
		s4 = getRemoteSizeFolder(remoteport3);


		if(s1 < s2 && s1 < s3 && s1 < s4 ){
			return port;
		}else if(s2 < s1 && s2 < s3 && s2 < s4){
			return remoteport1;
		}else if(s3 < s1 && s3 < s2 && s3 < s4){
			return remoteport2;
		}else if(s4 < s1 && s4 < s2 && s4 < s3){
			return remoteport3;
		}
		else{
			return port;
		}
	}
}