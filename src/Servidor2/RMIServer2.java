import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer2 {
	
	Integer port = 8002;
	
	private void startServer(){
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }      
        System.out.println("Server ligado!!!");
    }
	
	public static void main(String[] args) {
        RMIServer2 main = new RMIServer2();
        main.startServer();
    }
}
