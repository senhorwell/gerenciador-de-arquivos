import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer1 {

	Integer port = 8001;
	
	private void startServer(){
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }      
        System.out.println("Server ligado!!!");
    }
	
	public static void main(String[] args) {
        RMIServer1 main = new RMIServer1();
        main.startServer();
    }
	
}