package programManagers;

import java.util.Date;
import java.util.List;

import javax.print.DocFlavor.STRING;
import javax.swing.JFrame;

import windows.*;
import basicDataStructure.*;
//总管类，实现I_ManagerGeneral_Service接口
public class Manager_General implements I_ManagerGeneral_Service{
    
	Manager_Diary Dmanage = new Manager_Diary();
	Manager_User Umanage=new Manager_User();
	Manager_Type Cmanage=new Manager_Type();
	Window_MainFrame Mframe;	
	String currentUser;
        
	public void ShowMainWindow()
	{	
            Mframe=new Window_MainFrame(this);
	}
        public boolean AddNewType(String type)
        {
            return Cmanage.AddUserDiaryType(currentUser, type);
        }
        public boolean DeleteDairy(int dairyID)
        {
            return Dmanage.DeleteDiary(dairyID);
        }
        public String GetCurrentUser()
        {
            return currentUser;
        }
        public List<DiaryDescription> GetRecentDiaryByUser(int num)
        {
            return Dmanage.GetRecentDiaryByUser(currentUser,num);
        }
        public DiaryMultiMedia GetDiaryMultiMedia(int diaryid)
        {
            return Dmanage.GetDiaryMultiMedia(diaryid);
        }
	public void Login()
	{
            new Window_Login(this);
	}
        public List<DiaryDescription> GetInquiredDairiyDescription(String Key,String cls, String dateStart, String dateEnd)
        {
            return Dmanage.GetInquiredDairyDescription(currentUser,Key, cls, dateStart, dateEnd);
        }
        public List<String> GetUserList()
        {
            return Umanage.GetAllUser();
        }
	public List<String> GetUserTypesList()
        {
            return Cmanage.GetAllDiaryTypeByUser(currentUser);
                    
        }
	public boolean UpdateDiary(Diary dairy) {
		// TODO Auto-generated method stub
		return Dmanage.upDateDiary(dairy);
	}
	public boolean AddNewDiary(Diary dairy)
	{
		return Dmanage.AddDiary(dairy);
	}
	public boolean DeleteDiary(int dairyID) {
		return Dmanage.DeleteDiary(dairyID);
	}
	public void ShowTypeManageWindow(){
		new Window_TypeManage(this);
	}
	public List<DiaryDescription> GetMonthDairyDescription(String m)
        {
		return Dmanage.GetMonthDairyDescription(currentUser, m);
	}
	public void ShowModiPasswordWindow()
	{
		new Window_ModifyPw(this);		
	}
	public List<DiaryDescription> GetInquiredDairDescription(String Key,String cls, String dateStart, String dateEnd)
	{
		return Dmanage.GetInquiredDairyDescription(currentUser, Key,cls, dateStart, dateEnd);
	}
	public boolean VerifyUser(String user, String pw)
	{
		return Umanage.VerifiedUser(user, pw);
	}
        public boolean VerifyUser(String pw)
	{
		return Umanage.VerifiedUser(currentUser, pw);
	}
        public void SetCurrentUser(String user)
        {
            currentUser=user;
        }
	public void ChangePw(String pw)
	{
		Umanage.UpdataPw(currentUser,pw);
	}
	
	public void ShowRegiserWindow()
	{
		new Window_Register(this);
	}
	//����ʵ��RegisterAction
	public boolean AddNewUser(String user, String pw)
	{
		return Umanage.AddUser(user, pw);
	}
	
	//����ʵ��ClassWindowAction�ӿ�
	public boolean AddNewDiaryType(String cla)
	{
		return Cmanage.AddUserDiaryType(currentUser, cla);
	
	}
	public boolean DeleteType(String cls)
	{
		return Cmanage.DeleteUserDiaryType(currentUser, cls);
	}
}
