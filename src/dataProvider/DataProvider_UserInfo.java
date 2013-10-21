package dataProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dataProvider.dbo.SqlBean;
/**
 * 主要作用：
 * 提供“用户表”的数据，业务逻辑需要的有关“用户表”的数据全由该类提供,并提供对用户表的增删改操作
 * 
 * 该类可调用的方法均为静态方法，所以无需要实例化该类
 * 
 * @作者 wave
*/
public class DataProvider_UserInfo 
{
	/**
	 * @作用：增加一个用户
	 * @param user 所要添加的用户
	 * @param pw 所要添加用户的密码
	 * @注意 用户名在数据表中为主键，不允许重复
     * @return 成功添加返回true，否则返回false
    */
	public static boolean AddUser(String user, String pw)
	{
		String sql="insert into users (username,password) values(?,?)";
		int i=SqlBean.executeUpdate(sql, user, pw);
			if(i==0)
				return false;
			else
				return true;
	}
	
	public static boolean UpdatePw(String user, String pw)
	{
		String sql="update users  set password=? where username=?";
		int i=SqlBean.executeUpdate(sql, pw, user);
			if(i==0)
				return false;
			else
				return true;
	}
	/**
	 * 删除一个用户
	 * @param user 所要删除的用户名
     * @return 成功删除返回true，否则返回false
    */
	public static boolean DeleteUser(String user)
	{
		String sql="delete from users where username=?";
		int i=SqlBean.executeUpdate(sql, user);
		if(i==0)
			return false;
		else
			return true;
	}

    /**
     * @作用：返回所有的用户名       
     */
	public static List<String> GetAllUsers()
	{
		String sql="select username from users";
		List<Map<Object, Object>> rs=SqlBean.executeQuery(sql);
		List<String> ls=new ArrayList<String>();
		for(Map<Object,Object> oneMap:rs)
		{
						
			String userName=(String)oneMap.get("username");
			ls.add(userName);
			

		}
		return ls;
	}
	/**
	 * @作用：验证是否为合法的用户
	 * @param user 用户名
	 * @param pw 该用户名的密码
	 * @return 密码和用户名是否符合
	 */
	public static boolean IsVerifiedUser(String user, String pw)
	{
		String sql="select * from users";
		List<Map<Object, Object>> rs=SqlBean.executeQuery(sql);
		if(rs.size()<1)
			return false;
		String pw2=(String)rs.get(0).get("password");
		if(pw.equals(pw2))
			return true;
		else
		    return false;

	
	}
}
