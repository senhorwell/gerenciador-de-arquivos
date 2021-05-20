import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer3 {

	Integer port = 8003;
	
	private void startServer(){
        try {
            Registry registry = LocateRegistry.createRegistry(port);
    
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }      
        System.out.println("Server ligado!!!");
    }
	
	public static void main(String[] args) {
        RMIServer3 main = new RMIServer3();
        main.startServer();
    }
	
}