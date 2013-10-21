package dataProvider;
import dataProvider.dbo.*;
import basicDataStructure.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @主要作用：
 * 提供“分类表”的数据，业务逻辑需要的有关“日记表”的数据全由该类提供,并提供对日记表的增删改操作
 * 
 * 该类可调用的方法均为静态方法，所以无需要实例化该类
 * 
 * @作者 wave
*/

/**
 *
 * @主要作用 ：
 提供“分类表”的数据，业务逻辑需要的有关“日记表”的数据全由该类提供,并提供对日记表的增删改操作
 
 该类可调用的方法均为静态方法，所以无需要实例化该类
 * @作者 wave
 */
public class DataProvider_TypeInfo
{
	/**
	 * 私有函数，将从SqlBean类获得的数据格式化成List<DClass>类型
	 * 
	 */
	private static List<String> ListMapToListString(List<Map<Object,Object>> rs)
	{
		List<String> result=new ArrayList<String>();
		for(Map<Object,Object> oneMap:rs)
		{
			String cla=(String)oneMap.get("type");
			result.add(cla);
		}
		return result;
	}
	 /**
	  * @作用：根据用户名取回与其相关的所有分类
	  *      分类这个类里组合了其ID、分类名及与其关联用户三个信息
	   * @param user 传入用户名
     * @return 返回与指定用户相关的分类
    */
	public static List<String> GetAllDiaryTypeByUser(String user)
	{
		String sql="select * from diaryType where username=?";
		List<Map<Object, Object>> rs=SqlBean.executeQuery(sql, user);
		return ListMapToListString(rs);
	
	}
	public static boolean AddDClass(String user, String type)
	{
		String sql="insert into diaryType (username,type) values(?,?)";
		int i=SqlBean.executeUpdate(sql, user,type);
			if(i==0)
				return false;
			else
				return true;
	}
	public static boolean DeleteUserDiarType(String user, String type)
	{
		String sql="delete from diaryType where username=? and type=?";
		int i=SqlBean.executeUpdate(sql, user, type);
		if(i==0)
			return false;
		else
			return true;
	}
	
}
