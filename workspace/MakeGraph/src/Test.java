//import java.sql.Date;

import java.util.Date;

import SelfCollection.SortedList;
import Tool.DbToNodeRelation;


public class Test {

	/**
	 * TODO
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		DbToNodeRelation db=new DbToNodeRelation("userstatus",100);
//		db.Initialize();
//		SortedList dataList=new SortedList();
//		for(int i=0;i<20;i+=2)
//		{
//			dataList.add((long)i);
//		}
//		for(int i=1;i<20;i+=2)
//			dataList.add((long)i);
//		for(int i=20;i<30;i++)
//		{
//			if(!dataList.add((long)i)){
//					System.out.println(Integer.toString(i)+"exsit!");
//			}
//		}
//		for(int i=0;i<30;i++)
//			System.out.println(dataList.get(i));
		
		Date date=new Date(System.currentTimeMillis());
		System.out.println(date.getHours()+" "+date.getMinutes()+" "+date.getSeconds());

	}

}
