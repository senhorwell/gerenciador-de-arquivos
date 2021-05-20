import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.imageio.ImageIO;


class ThreadWorker extends Thread {
	private String worker;
	private int index;

	ThreadWorker(String server, int idx) {
		this.worker = server;
		this.index = idx;
		
		System.out.println("Conectando a " + worker);
	}

	public BufferedImage run(Integer port, Integer half) {
        try {			
        	    Registry registry = LocateRegistry.getRegistry(worker, port);					
    			return image;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + " Error in thread");
        }
		return null;
    }

}

public class RMIClient {
	
    public static void main(String[] args) throws IOException {
        long Inicio = System.currentTimeMillis();
        Integer port = 8001;
        
        ThreadWorker t1 = new ThreadWorker("127.0.0.1", 1);

        half1 = t1.run(port, 1);

    }
    
}