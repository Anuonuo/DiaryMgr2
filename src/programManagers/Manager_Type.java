package programManagers;
import dataProvider.*;
import basicDataStructure.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class Manager_Type {
	
	public boolean AddUserDiaryType(String strUser,String strType)
	{   
            return DataProvider_TypeInfo.AddDClass(strUser, strType);
	}
	public  List<String> GetAllDiaryTypeByUser(String user)
	{
            return DataProvider_TypeInfo.GetAllDiaryTypeByUser(user);
	}
	public boolean DeleteUserDiaryType(String user,String type)
	{
		return DataProvider_TypeInfo.DeleteUserDiarType(user, type);
	}
}
