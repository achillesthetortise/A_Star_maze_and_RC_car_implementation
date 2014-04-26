import java.io.IOException;
import java.util.Stack;

public class Subprocess {

	public Subprocess(Stack<Integer> directions) {
		try {
			int dir = 3;
			while(directions.size() > 0) {
				int prevDir = dir;
				dir = directions.pop();
				int AD = 0;
				int delay = 0;
				if(dir == prevDir) {
					AD = 1;
				} else if((prevDir==1&&dir==2)||(prevDir==2&&dir==3)||(prevDir==3&&dir==4)||(prevDir==4&&dir==1)){
					AD = 2;
				} else if((prevDir==1&&dir==4)||(prevDir==2&&dir==1)||(prevDir==3&&dir==2)||(prevDir==4&&dir==3)){
					AD = 3;
				} else if((prevDir==1&&dir==3)||(prevDir==2&&dir==4)||(prevDir==3&&dir==1)||(prevDir==4&&dir==2)){
					AD = 4;
				}
				Process p = new ProcessBuilder("bash","-c","echo " + AD + " > /dev/ttyACM0").start();
				//try{
				//	Thread.sleep(2000);
				//}catch(Exception e){}
			}
		} catch (IOException ioe) {
			System.out.println("An IO error occurred.");
			ioe.printStackTrace();
			System.exit(0);
		}
	}
}
