package programManagers;
import java.util.List;

import dataProvider.*;

public class Manager_User {
	
	//增加用户
	public boolean AddUser(String user, String pw)
	{
		return DataProvider_UserInfo.AddUser(user,pw);
	}
	public List<String> GetAllUser()
	{
		return DataProvider_UserInfo.GetAllUsers();
	}
	//校验用户
	public boolean VerifiedUser(String user,String pw)
	{
		if(!DataProvider_UserInfo.IsVerifiedUser(user,pw))
		{
			return false;
		}
		return true;
	}
	//更新密码
	public boolean UpdataPw(String user,String newpw)
	{
		return DataProvider_UserInfo.UpdatePw(user,newpw);
	}

}
