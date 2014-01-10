
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
//import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import Config.Conf;
import Core.IWeiboAPIService;
import Core.RelationType;
import Core.WeiboServiceManager;
import Core.WeiboType;
import Datas.UserData;
import SelfCollection.SortedList;

public class Graph {

	private HashMap<Integer, TreeSet<Integer>> edgeMap;
	private int nodenumber = Integer.MAX_VALUE;
	private String usernameString = null;
	private String collectionName;

	private List<String> userIDList;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

	public Graph(int nodenumber) {
		this.nodenumber = nodenumber;
	}

	/**
	 * 连接Mongodb指定的Collection，读取数据的起始位置
	 */
	public void Initialize() {
		this.userIDList = new ArrayList<String>();
		this.edgeMap = new HashMap<Integer, TreeSet<Integer>>();
	}

	/**
	 * @throws IOException
	 */
	/**
	 * @throws IOException
	 */
	/**
	 * @throws IOException
	 */

	public void DataProcessConnected() throws IOException {
		HashMap<Integer, TreeSet<Integer>> map = new HashMap<Integer, TreeSet<Integer>>();
		String usernameString = "大漠飞刀客";
		IWeiboAPIService service = WeiboServiceManager.Instance().Get(
				WeiboType.Sina);
		UserData seedData = service.GetUserData(usernameString);
		List<Long> userIDindexIntegers = new ArrayList<Long>();
		int userindex = 0;

		String UserIdFilePath = Conf.GetGraphDir() + "/"
				+ Integer.toString(nodenumber) + "/conneted/NodoID.txt";
		// String
		// UserIdFilePath=Conf.GetDataDir()+"\\NetWorkMeasurement\\UserIDCollectionConnected"+usernumberString+".txt";
		File UserIDFile = new File(UserIdFilePath);
		if (!UserIDFile.exists()) {
			UserIDFile.createNewFile();
		}
		OutputStreamWriter UserIDwrite = new OutputStreamWriter(
				new FileOutputStream(UserIDFile), "GB2312");
		BufferedWriter UserIDwriter = new BufferedWriter(UserIDwrite);

		String AdjacementHaidianFilePath = Conf.GetGraphDir() + "/"
				+ Integer.toString(nodenumber) + "/conneted/NodoMatrix.txt";
		File AdjacementHaidianFile = new File(AdjacementHaidianFilePath);
		if (!AdjacementHaidianFile.exists()) {
			AdjacementHaidianFile.createNewFile();
		}
		OutputStreamWriter AdjacementHaidianwrite = new OutputStreamWriter(
				new FileOutputStream(AdjacementHaidianFile), "GB2312");
		BufferedWriter AdjacementHaidianwriter = new BufferedWriter(
				AdjacementHaidianwrite);
		int curnumber = 0;
		int usermount = 0;

		Long ID = seedData.getID();

		userIDindexIntegers.add(ID);
		UserIDwriter.write(userindex + " " + Long.toString(ID));
		UserIDwriter.append("\n");
		UserIDwriter.flush();
		usermount++;

		while (usermount < this.nodenumber) {
			Long Id = userIDindexIntegers.get(userindex);
			List<Long> followDatas = service.GetRelationIDs(
					RelationType.Follows, Long.toString(Id), 1000);
			// List<Long> followDatas=seedData.getFollowIDs();
			TreeSet<Integer> IDCollectionIntegers = null;
			if (map.containsKey(userindex)) {
				IDCollectionIntegers = map.get(userindex);
			} else {
				IDCollectionIntegers = new TreeSet<Integer>();
				map.put(userindex, IDCollectionIntegers);
			}

			int count = Math.min(followDatas.size(), this.nodenumber
					- usermount);
			for (int i = 0; i < count; i++) {
				Long id = null;

				id = followDatas.get(i);

				if (!userIDindexIntegers.contains(id)) {
					userIDindexIntegers.add(id);
					UserIDwriter.write(usermount + " " + Long.toString(id));
					UserIDwriter.append("\n");
					UserIDwriter.flush();
					IDCollectionIntegers.add(usermount);
					TreeSet<Integer> set = new TreeSet();
					set.add(userindex);
					map.put(usermount, set);
					usermount++;
					System.out.println("usermount:"
							+ Integer.toString(usermount));
				} else {
					continue;
				}

				//
			}

			// if(ID)
			edgeMap.put(userindex, IDCollectionIntegers);
			String edgefinalindexString = "";
			// int edgenumber=IDCollectionIntegers.size();
			Iterator iterator = IDCollectionIntegers.iterator();
			while (iterator.hasNext()) {
				edgefinalindexString += Integer.toString((Integer) iterator
						.next());
				edgefinalindexString += " ";
			}
			AdjacementHaidianwriter.write(userindex + ":"
					+ edgefinalindexString);
			AdjacementHaidianwriter.append("\n");
			AdjacementHaidianwriter.flush();
			System.out.println("userindex:" + Integer.toString(userindex));
			userindex++;
			if (userindex >= usermount) {
				System.out.println("关于大漠飞刀客的最大连通节点个数："
						+ Integer.toString(usermount));
				break;
			}
		}
		UserIDwriter.close();
		for (int i = userindex; i < usermount; i++) {
			long id = userIDindexIntegers.get(i);
			TreeSet<Integer> IDCollectionIntegers = null;
			if (map.containsKey(userindex)) {
				IDCollectionIntegers = map.get(userindex);
			} else {
				IDCollectionIntegers = new TreeSet<Integer>();
				map.put(userindex, IDCollectionIntegers);
			}
			List<Long> followDatas = service.GetRelationIDs(
					RelationType.Follows, Long.toString(id), 1000);
			int count = followDatas.size();
			for (int j = 0; j < count; j++) {

				Long Id = followDatas.get(j);
				int index = userIDindexIntegers.indexOf(Id);
				if (index < 0) {
				} else {
					IDCollectionIntegers.add(index);
				}
			}
			edgeMap.put(i, IDCollectionIntegers);
			String edgefinalindexString = "";
			// int edgenumber=IDCollectionIntegers.size();
			Iterator iterator = IDCollectionIntegers.iterator();
			while (iterator.hasNext()) {
				edgefinalindexString += Integer.toString((Integer) iterator
						.next());
				edgefinalindexString += " ";
			}
			AdjacementHaidianwriter.write(userindex + ":"
					+ edgefinalindexString);
			AdjacementHaidianwriter.append("\n");
			AdjacementHaidianwriter.flush();
			System.out.println("userindex:" + Integer.toString(userindex));
			userindex++;

		}
		AdjacementHaidianwriter.close();
		System.out.println("用户名列表生成完毕！开始生成邻接表文件");

	}
	public void print(String s)
	{
	//	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println(df.format(new Date(System.currentTimeMillis()))+"\t"+s);// new Date()为获取当前系统时间
	}
	
	public void GetIdCollections() throws IOException, InterruptedException
	{
		String usernameString = "大漠飞刀客";
		IWeiboAPIService service = WeiboServiceManager.Instance().Get(
				WeiboType.Sina);
		UserData seedData = service.GetUserData(usernameString);
		SortedList userIDindexIntegers = new SortedList();
		int userindex = 0;

		String UserIdFilePath = Conf.GetGraphDir();
		File filet = new File(UserIdFilePath + "/"
				+ Integer.toString(nodenumber));
		if (!filet.isDirectory()) {
			filet.mkdir();
		}
		UserIdFilePath = filet.getAbsolutePath() + "/followsID.txt";
		File UserIDFile = new File(UserIdFilePath);
		if (!UserIDFile.exists()) {
			UserIDFile.createNewFile();
		}
		OutputStreamWriter UserIDwrite = new OutputStreamWriter(
				new FileOutputStream(UserIDFile), "GB2312");
		BufferedWriter UserIDwriter = new BufferedWriter(UserIDwrite);
		
		int usermount=0;
		Long ID = seedData.getID();

		userIDindexIntegers.add(ID);
		UserIDwriter.write(userindex + " " + Long.toString(ID));
		UserIDwriter.append("\n");
		UserIDwriter.flush();
		usermount++;
		List<Long> result=new ArrayList<Long>();

		while (usermount < this.nodenumber) {
			Long Id = userIDindexIntegers.get(userindex);
			// SinaService sinaservice1=(SinaService)service;
			List<Long> followDatas = service.GetRelationIDs(
					RelationType.Follows, Long.toString(Id), 5000);
		//	for(int i=0;i<10;i++)
			{
				int iplimit=service.GetCurrentCountInfo();
				if(iplimit<10)
				{
					Date date=new Date(System.currentTimeMillis());
					int miniute=60-date.getMinutes();
					Thread.sleep(miniute*60*1000);
				}
					
				print("剩余IP次数："+iplimit);
			}
			int number=followDatas.size();
			for(int i=0;i<number;i++)
			{
				ID=followDatas.get(i);
				if(userIDindexIntegers.add(followDatas.get(i)))
				{
					UserIDwriter.write(usermount + " " + Long.toString(ID));
					UserIDwriter.append("\n");
					UserIDwriter.flush();
					usermount++;
				}
			}
//			usermount=userIDindexIntegers.size();
			userindex++;
			print(Integer.toString(userindex)+","+Integer.toString(usermount));
			
		}
		UserIDwriter.close();
		
	}
	
	
	public void DataProcess() throws IOException {
		HashMap<Integer, TreeSet<Integer>> map = new HashMap<Integer, TreeSet<Integer>>();
		String usernameString = "大漠飞刀客";
		IWeiboAPIService service = WeiboServiceManager.Instance().Get(
				WeiboType.Sina);
		UserData seedData = service.GetUserData(usernameString);
		List<Long> userIDindexIntegers = new ArrayList<Long>();
		int userindex = 0;

		String UserIdFilePath = Conf.GetGraphDir();
		File filet = new File(UserIdFilePath + "/"
				+ Integer.toString(nodenumber));
		if (!filet.isDirectory()) {
			filet.mkdir();
		}
		UserIdFilePath = filet.getAbsolutePath() + "/followsID.txt";
		File UserIDFile = new File(UserIdFilePath);
		if (!UserIDFile.exists()) {
			UserIDFile.createNewFile();
		}
		OutputStreamWriter UserIDwrite = new OutputStreamWriter(
				new FileOutputStream(UserIDFile), "GB2312");
		BufferedWriter UserIDwriter = new BufferedWriter(UserIDwrite);

		String AdjacementHaidianFilePath = Conf.GetGraphDir();
		File filet1 = new File(AdjacementHaidianFilePath + "/"
				+ Integer.toString(nodenumber));
		if (!filet1.isDirectory()) {
			filet1.mkdir();
		}
		AdjacementHaidianFilePath = filet1.getAbsolutePath()
				+ "/followsAdjacement.txt";
		File AdjacementHaidianFile = new File(AdjacementHaidianFilePath);
		if (!AdjacementHaidianFile.exists()) {
			AdjacementHaidianFile.createNewFile();
		}
		OutputStreamWriter AdjacementHaidianwrite = new OutputStreamWriter(
				new FileOutputStream(AdjacementHaidianFile), "GB2312");
		BufferedWriter AdjacementHaidianwriter = new BufferedWriter(
				AdjacementHaidianwrite);
		int curnumber = 0;
		int usermount = 0;

		Long ID = seedData.getID();

		userIDindexIntegers.add(ID);
		UserIDwriter.write(userindex + " " + Long.toString(ID));
		UserIDwriter.append("\n");
		UserIDwriter.flush();
		usermount++;

		while (usermount < this.nodenumber) {
			Long Id = userIDindexIntegers.get(userindex);
			// SinaService sinaservice1=(SinaService)service;
			List<Long> followDatas = service.GetRelationIDs(
					RelationType.Follows, Long.toString(Id), 1000);
			// List<Long> followDatas=seedData.getFollowIDs();
			for (int i = 0; i < 10; i++) {
				System.out.print(followDatas.get(i) + ",");
			}
			System.out.println("\n");

			for (int i = 50000; i < 5010; i++) {
				System.out.print(followDatas.get(i) + ",");
			}
			System.out.println("\n");

			for (int i = 10000; i < 10010; i++) {
				System.out.print(followDatas.get(i) + ",");
			}
			System.out.println("\n");

			TreeSet<Integer> IDCollectionIntegers = null;
			if (map.containsKey(userindex)) {
				IDCollectionIntegers = map.get(userindex);
			} else {
				IDCollectionIntegers = new TreeSet<Integer>();
				map.put(userindex, IDCollectionIntegers);
			}

			int count = Math.min(followDatas.size(), this.nodenumber
					- usermount);
			for (int i = 0; i < count; i++) {
				Long id = null;

				id = followDatas.get(i);

				if (!userIDindexIntegers.contains(id)) {
					userIDindexIntegers.add(id);
					UserIDwriter.write(usermount + " " + Long.toString(id));
					UserIDwriter.append("\n");
					UserIDwriter.flush();
					IDCollectionIntegers.add(usermount);
					TreeSet<Integer> set = new TreeSet();
					set.add(userindex);
					map.put(usermount, set);
					usermount++;
					System.out.println("usermount:"
							+ Integer.toString(usermount));
				} else {
					continue;
				}

				//
			}

			// if(ID)
			edgeMap.put(userindex, IDCollectionIntegers);
			String edgefinalindexString = "";
			// int edgenumber=IDCollectionIntegers.size();
			Iterator iterator = IDCollectionIntegers.iterator();
			while (iterator.hasNext()) {
				edgefinalindexString += Integer.toString((Integer) iterator
						.next());
				edgefinalindexString += " ";
			}
			AdjacementHaidianwriter.write(userindex + ":"
					+ edgefinalindexString);
			AdjacementHaidianwriter.append("\n");
			AdjacementHaidianwriter.flush();
			System.out.println("userindex:" + Integer.toString(userindex));
			userindex++;
			if (userindex >= usermount) {
				System.out.println("关于大漠飞刀客的最大连通节点个数："
						+ Integer.toString(usermount));
				break;
			}
		}
		UserIDwriter.close();
		for (int i = userindex; i < usermount; i++) {
			long id = userIDindexIntegers.get(i);
			TreeSet<Integer> IDCollectionIntegers = null;
			if (map.containsKey(userindex)) {
				IDCollectionIntegers = map.get(userindex);
			} else {
				IDCollectionIntegers = new TreeSet<Integer>();
				map.put(userindex, IDCollectionIntegers);
			}
			List<Long> followDatas = service.GetRelationIDs(
					RelationType.Follows, Long.toString(id), 1000);
			int count = followDatas.size();
			for (int j = 0; j < count; j++) {

				Long Id = followDatas.get(j);
				int index = userIDindexIntegers.indexOf(Id);
				if (index < 0) {
				} else {
					IDCollectionIntegers.add(index);
				}
			}
			edgeMap.put(i, IDCollectionIntegers);
			String edgefinalindexString = "";
			// int edgenumber=IDCollectionIntegers.size();
			Iterator iterator = IDCollectionIntegers.iterator();
			while (iterator.hasNext()) {
				edgefinalindexString += Integer.toString((Integer) iterator
						.next());
				edgefinalindexString += " ";
			}
			AdjacementHaidianwriter.write(userindex + ":"
					+ edgefinalindexString);
			AdjacementHaidianwriter.append("\n");
			AdjacementHaidianwriter.flush();
			System.out.println("userindex:" + Integer.toString(userindex));
			userindex++;

		}
		AdjacementHaidianwriter.close();
		System.out.println("用户名列表生成完毕！开始生成邻接表文件");

	}
	
	
	

	public void DataProcessConnectedInternet() throws IOException {
		HashMap<Integer, TreeSet<Integer>> map = new HashMap<Integer, TreeSet<Integer>>();
		String usernameString = "姚晨";
		IWeiboAPIService service = WeiboServiceManager.Instance().Get(
				WeiboType.Sina);
		UserData seedData = service.GetUserData(usernameString);
		List<Long> userIDindexIntegers = new ArrayList<Long>();
		int userindex = 0;

		String UserIdFilePath = Conf.GetGraphDir();
		File filet = new File(UserIdFilePath + "/"
				+ Integer.toString(nodenumber));
		if (!filet.isDirectory()) {
			filet.mkdir();
		}
		UserIdFilePath = filet.getAbsolutePath() + "/followsID.txt";
		File UserIDFile = new File(UserIdFilePath);
		if (!UserIDFile.exists()) {
			UserIDFile.createNewFile();
		}
		OutputStreamWriter UserIDwrite = new OutputStreamWriter(
				new FileOutputStream(UserIDFile), "GB2312");
		BufferedWriter UserIDwriter = new BufferedWriter(UserIDwrite);

		String AdjacementHaidianFilePath = Conf.GetGraphDir();
		File filet1 = new File(AdjacementHaidianFilePath + "/"
				+ Integer.toString(nodenumber));
		if (!filet1.isDirectory()) {
			filet1.mkdir();
		}
		AdjacementHaidianFilePath = filet1.getAbsolutePath()
				+ "/followsAdjacement.txt";
		File AdjacementHaidianFile = new File(AdjacementHaidianFilePath);
		if (!AdjacementHaidianFile.exists()) {
			AdjacementHaidianFile.createNewFile();
		}
		OutputStreamWriter AdjacementHaidianwrite = new OutputStreamWriter(
				new FileOutputStream(AdjacementHaidianFile), "GB2312");
		BufferedWriter AdjacementHaidianwriter = new BufferedWriter(
				AdjacementHaidianwrite);
		int curnumber = 0;
		int usermount = 0;

		Long ID = seedData.getID();

		userIDindexIntegers.add(ID);
		UserIDwriter.write(userindex + " " + Long.toString(ID));
		UserIDwriter.append("\n");
		UserIDwriter.flush();
		usermount++;

		while (usermount < this.nodenumber) {
			Long Id = userIDindexIntegers.get(userindex);
			// SinaService sinaservice1=(SinaService)service;
			List<Long> followDatas = service.GetRelationIDs(
					RelationType.Follows, Long.toString(Id), 1000);
			// List<Long> followDatas=seedData.getFollowIDs();
			for (int i = 0; i < 10; i++) {
				System.out.print(followDatas.get(i) + ",");
			}
			System.out.println("\n");

			for (int i = 50000; i < 5010; i++) {
				System.out.print(followDatas.get(i) + ",");
			}
			System.out.println("\n");

			for (int i = 10000; i < 10010; i++) {
				System.out.print(followDatas.get(i) + ",");
			}
			System.out.println("\n");

			TreeSet<Integer> IDCollectionIntegers = null;
			if (map.containsKey(userindex)) {
				IDCollectionIntegers = map.get(userindex);
			} else {
				IDCollectionIntegers = new TreeSet<Integer>();
				map.put(userindex, IDCollectionIntegers);
			}

			int count = Math.min(followDatas.size(), this.nodenumber
					- usermount);
			for (int i = 0; i < count; i++) {
				Long id = null;

				id = followDatas.get(i);

				if (!userIDindexIntegers.contains(id)) {
					userIDindexIntegers.add(id);
					UserIDwriter.write(usermount + " " + Long.toString(id));
					UserIDwriter.append("\n");
					UserIDwriter.flush();
					IDCollectionIntegers.add(usermount);
					TreeSet<Integer> set = new TreeSet();
					set.add(userindex);
					map.put(usermount, set);
					usermount++;
					System.out.println("usermount:"
							+ Integer.toString(usermount));
				} else {
					continue;
				}

				//
			}

			// if(ID)
			edgeMap.put(userindex, IDCollectionIntegers);
			String edgefinalindexString = "";
			// int edgenumber=IDCollectionIntegers.size();
			Iterator iterator = IDCollectionIntegers.iterator();
			while (iterator.hasNext()) {
				edgefinalindexString += Integer.toString((Integer) iterator
						.next());
				edgefinalindexString += " ";
			}
			AdjacementHaidianwriter.write(userindex + ":"
					+ edgefinalindexString);
			AdjacementHaidianwriter.append("\n");
			AdjacementHaidianwriter.flush();
			System.out.println("userindex:" + Integer.toString(userindex));
			userindex++;
			if (userindex >= usermount) {
				System.out.println("关于大漠飞刀客的最大连通节点个数："
						+ Integer.toString(usermount));
				break;
			}
		}
		UserIDwriter.close();
		for (int i = userindex; i < usermount; i++) {
			long id = userIDindexIntegers.get(i);
			TreeSet<Integer> IDCollectionIntegers = null;
			if (map.containsKey(userindex)) {
				IDCollectionIntegers = map.get(userindex);
			} else {
				IDCollectionIntegers = new TreeSet<Integer>();
				map.put(userindex, IDCollectionIntegers);
			}
			List<Long> followDatas = service.GetRelationIDs(
					RelationType.Follows, Long.toString(id), 1000);
			int count = followDatas.size();
			for (int j = 0; j < count; j++) {

				Long Id = followDatas.get(j);
				int index = userIDindexIntegers.indexOf(Id);
				if (index < 0) {
				} else {
					IDCollectionIntegers.add(index);
				}
			}
			edgeMap.put(i, IDCollectionIntegers);
			String edgefinalindexString = "";
			// int edgenumber=IDCollectionIntegers.size();
			Iterator iterator = IDCollectionIntegers.iterator();
			while (iterator.hasNext()) {
				edgefinalindexString += Integer.toString((Integer) iterator
						.next());
				edgefinalindexString += " ";
			}
			AdjacementHaidianwriter.write(userindex + ":"
					+ edgefinalindexString);
			AdjacementHaidianwriter.append("\n");
			AdjacementHaidianwriter.flush();
			System.out.println("userindex:" + Integer.toString(userindex));
			userindex++;

		}
		AdjacementHaidianwriter.close();
		System.out.println("用户名列表生成完毕！开始生成邻接表文件");

	}

}