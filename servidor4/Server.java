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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
		ServerSocket server;
		Socket socket;
		Integer retorno;
		while(true){
			server = new ServerSocket(port);
			socket = server.accept();
			retorno = download(socket);

			if (retorno == 5) {
				break;
			}
			socket.close();
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
			case 10:
				getSimpleListFiles(socket);
				break;
			case 2:
				setNewFile(socket,nomeArquivo);
				break;
			case 3:
				renameFile(socket,nomeArquivo,antigoArquivo);
				break;
			case 11:
				renameSimpleFile(socket, nomeArquivo, antigoArquivo);
				break;
			case 4:
				removeFile(socket,nomeArquivo);
				break;
			case 12:
				removeSimpleFile(socket, nomeArquivo);
				break;
			case 6:
				String msg = "" + getLowestFolder();
	    		upload(socket,msg);
				break;
			case 7:
				String msg2 = "" + getSizeFolder();
				upload(socket,msg2);
				break;
			case 9:
				getNewFile(socket,nomeArquivo);
				break;
			default:
				break;
		}
		return opcao;
	}


	private static void removeFile(Socket socket, String nomeArquivo) throws IOException {
		try{
			File file = new File("./arquivos/" + nomeArquivo);

			file.delete();

			removeRemoteFile(remoteport1,nomeArquivo);
			removeRemoteFile(remoteport2,nomeArquivo);
			removeRemoteFile(remoteport3,nomeArquivo);
			
			upload(socket,"Arquivo removido com sucesso");
		}catch(Exception e){
			upload(socket,"Erro ao excluir arquivo");
		}
	}
	private static void removeSimpleFile(Socket socket, String nomeArquivo) throws IOException {
		try{
			File file = new File("./arquivos/" + nomeArquivo);

			file.delete();
			
			upload(socket,"Arquivo removido com sucesso");
		}catch(Exception e){
			upload(socket,"Erro ao excluir arquivo");
		}
	}
	private static String removeRemoteFile(Integer port, String nomeArquivo) throws IOException {
		ArrayList<String> my = new ArrayList<String>();
		my.add("12");
		my.add(nomeArquivo);

		Socket socketemp = new Socket("127.0.0.1", port);
		OutputStream output = socketemp.getOutputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(my);
		byte[] saida = baos.toByteArray();
		output.write(saida);
		BufferedReader input = new BufferedReader(new InputStreamReader(socketemp.getInputStream()));
		String input2 = input.readLine();
		
		return input2;
	}

	private static void renameFile(Socket socket, String nomeArquivo, String antigoArquivo) throws IOException {
		try{
			File file = new File("./arquivos/" + nomeArquivo);

			file.renameTo(new File("./arquivos/" + antigoArquivo));

			renameRemoteFile(remoteport1,nomeArquivo,antigoArquivo);
			renameRemoteFile(remoteport2,nomeArquivo,antigoArquivo);
			renameRemoteFile(remoteport3,nomeArquivo,antigoArquivo);

			upload(socket,"Arquivo renomeado com sucesso");
		}catch(Exception e){
			upload(socket,"Erro ao renomear arquivo");
		}
	}
	private static void renameSimpleFile(Socket socket, String nomeArquivo, String antigoArquivo) throws IOException {
		try{
			File file = new File("./arquivos/" + nomeArquivo);

			file.renameTo(new File("./arquivos/" + antigoArquivo));

			upload(socket,"Arquivo renomeado com sucesso");
		}catch(Exception e){
			upload(socket,"Erro ao renomear arquivo");
		}
	}
	private static String renameRemoteFile(Integer port, String nomeArquivo, String antigoArquivo) throws IOException {
		ArrayList<String> my = new ArrayList<String>();
		my.add("11");
		my.add(nomeArquivo);
		my.add(antigoArquivo);

		Socket socketemp = new Socket("127.0.0.1", port);
		OutputStream output = socketemp.getOutputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(my);
		byte[] saida = baos.toByteArray();
		output.write(saida);
		BufferedReader input = new BufferedReader(new InputStreamReader(socketemp.getInputStream()));
		String input2 = input.readLine();
		
		return input2;
	}

	private static void setNewFile(Socket socket, String nomeArquivo) throws IOException {
		try{
			Path source = Paths.get(nomeArquivo);
        	Path target = Paths.get("arquivos");

			File file = new File("./arquivos/" + nomeArquivo);

			Integer s1 = findDuplicate(socket,file);
			Integer porta = getLowestFolder();
			if(s1 == 0){
				if(porta == port){
					Files.copy(source,target.resolve(source.getFileName()));
				}else{
					setRemoteNewFile(socket,nomeArquivo,porta);
				}
				upload(socket,"Arquivo criado com sucesso");

			}else{
				upload(socket,"Arquivo duplicado");

			}

		}catch(Exception e){
			upload(socket,"Erro ao criar arquivo");
		}
	}
	private static void getNewFile(Socket socket, String nomeArquivo) throws IOException {
		try{
			Path source = Paths.get(nomeArquivo);
        	Path target = Paths.get("arquivos");

			File file = new File("./arquivos/" + nomeArquivo);
			Files.copy(source,target.resolve(source.getFileName()));
			upload(socket,"Arquivo criado com sucesso");
		}catch(Exception e){
			upload(socket,"Erro ao criar arquivo");
		}
	}
	private static String setRemoteNewFile(Socket socket,String nomeArquivo,Integer port) throws IOException {
		try{
			ArrayList<String> my = new ArrayList<String>();
			my.add("9");
			my.add(nomeArquivo);
	
			Socket socketemp = new Socket("127.0.0.1", port);
			OutputStream output = socketemp.getOutputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(my);
			byte[] saida = baos.toByteArray();
			output.write(saida);
			BufferedReader input = new BufferedReader(new InputStreamReader(socketemp.getInputStream()));
			String input2 = input.readLine();

			return input2;
		}catch(IOException e){
			return "error";
		}
	}
	private static String getRemoteListFiles(Integer port) throws IOException {
		try{
			ArrayList<String> my = new ArrayList<String>();
			my.add("10");
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

			return input2;
		}catch(IOException e){
			return "error";
		}
	}
	private static String getListFiles(Socket socket) throws IOException {
		File file = new File("./arquivos/");
		File[] arquivos = file.listFiles();
		String msg = "\t";
		for (File fileTmp : arquivos) {
			msg += fileTmp.getName();
			msg += "\t";
		}
		msg += getRemoteListFiles(remoteport1);
		msg += getRemoteListFiles(remoteport2);
		msg += getRemoteListFiles(remoteport3);
		upload(socket,msg);
		
		return msg;
	}
	private static String getSimpleListFiles(Socket socket) throws IOException {
		File file = new File("./arquivos/");
		File[] arquivos = file.listFiles();
		String msg = "\t";
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

		if(s1 == 0){
			return port;
		}
		if(s2 == 0){
			return remoteport1;
		}
		if(s3 == 0){
			return remoteport2;
		}
		if(s4 == 0){
			return remoteport3;
		}
		if(s1 <= s2 && s1 <= s3 && s1 <= s4 ){
			return port;
		}else if(s2 <= s1 && s2 <= s3 && s2 <= s4){
			return remoteport1;
		}else if(s3 <= s1 && s3 <= s2 && s3 <= s4){
			return remoteport2;
		}else if(s4 <= s1 && s4 <= s2 && s4 <= s3){
			return remoteport3;
		}
		else{
			return port;
		}
	}
	private static Integer findDuplicate(Socket socket, File duplicateFile){
		String duplicateFileName = duplicateFile.getName();
		String msg = "";
		Integer count = 0;
	
		try{
			msg += getListFiles(socket);
		}catch(Exception e){
			return 0;
		}
		String[] parts = msg.split("\t");
		while (parts.length > count) {
			if (duplicateFile.getName().equals(parts[count])) {
				return 1;
			}
			count++;
		}
		return 0;
	}
	private static Integer findRemoteDuplicate(File duplicateFile, Integer remoteport) throws IOException {
		String duplicateFileName = duplicateFile.getName();
		String msg = "";
		Integer count = 0, server = 0;
		switch(remoteport) {
			case 6001:
				server = 1;
				break;
			case 6002:
				server = 2;
				break;
			case 6003:
				server = 3;
				break;
			case 6004:
				server = 4;
				break;
		}
		
		try{
			msg += getRemoteListFiles(remoteport);
		}catch(Exception e){
			return 0;
		}
		String[] parts = msg.split("\t");
		while (parts.length > count) {
			if (duplicateFile.getName().equals(parts[count])) {
				return server;
			}
			count++;
		}
		return 0;
	}

	private static Integer getLowestFolderBck() throws IOException{
		long s1,s2,s3,s4;

		s1 = getSizeFolder();
		s2 = getRemoteSizeFolder(remoteport1);
		s3 = getRemoteSizeFolder(remoteport2);
		s4 = getRemoteSizeFolder(remoteport3);

		if(s1 == 0){
			return port;
		}
		if(s2 == 0){
			return remoteport1;
		}
		if(s3 == 0){
			return remoteport2;
		}
		if(s4 == 0){
			return remoteport3;
		}
		if(s1 <= s2 && s1 <= s3 && s1 <= s4 ){
			return port;
		}else if(s2 <= s1 && s2 <= s3 && s2 <= s4){
			return remoteport1;
		}else if(s3 <= s1 && s3 <= s2 && s3 <= s4){
			return remoteport2;
		}else if(s4 <= s1 && s4 <= s2 && s4 <= s3){
			return remoteport3;
		}
		else{
			return remoteport1;
		}
	}

	private static void setNewFileBck(Socket socket, String nomeArquivo) throws IOException {
		try{
			Path source = Paths.get(nomeArquivo);
        	Path target = Paths.get("arquivos");
        	Files.copy(source,target.resolveSibling(source.getFileName() + ".bck"));

			upload(socket,"Arquivo backup criado com sucesso");
		}catch(Exception e){
			upload(socket,"Erro ao criar arquivo backup");
		}
		
	}
}