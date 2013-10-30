package WSD;
import java.io.File;
import java.io.FileReader;
import weka.core.Instances;

public class dataReader {
	
	public static void main(String[] args) throws Exception
	{
		if (args.length!=1)
		{
			System.err.println("Need an argument.");
			System.exit(-1);
		}
		Instances train = new Instances(new FileReader(new File(args[0])));			
		
	}
}
