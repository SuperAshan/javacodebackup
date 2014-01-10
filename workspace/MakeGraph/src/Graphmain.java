import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.TreeSet;

import Config.Conf;


public class Graphmain {
	/**
	 * @param args
	 *            暂时没有参数
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException
	{
 
		// TODO Auto-generated method stub
		int nodenumber=1000000;
		Graph dbToNodeRelation = new Graph(nodenumber);
		dbToNodeRelation.Initialize();
		dbToNodeRelation.GetIdCollections();
	


}
}
