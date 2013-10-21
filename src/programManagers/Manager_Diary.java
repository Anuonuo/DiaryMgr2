package programManagers;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import basicDataStructure.*;
import dataProvider.*;
//import dataProvider.Diary;
import windows.*;


public class Manager_Diary {
	
	public boolean AddDiary(Diary newDairy)
	{ 
		return DataProvider_DiaryInfo.AddDairy(newDairy);
	}
	public boolean upDateDiary(Diary newDairy)
	{   
		return DataProvider_DiaryInfo.UpdateDiary(newDairy);
	}
	public List<DiaryDescription> GetRecentDiaryByUser(String user, int num)
        {
            return DataProvider_DiaryInfo.GetRecentDairyDescriptionByUser(user,num);
        }
	public  List<DiaryDescription> GetInquiredDairyDescription(String user, String key, String cls,
			String dateStart, String dateEnd)
	{   
                return DataProvider_DiaryInfo.GetInquiredDairyDescription(user, key, cls, dateStart, dateEnd);		
	}
	public boolean DeleteDiary(int dairyID)
        {
            return DataProvider_DiaryInfo.DeleteDiary(dairyID);
	}
        
        public List<DiaryDescription> GetMonthDairyDescription(String user, String m) 
	{
		Date ds=new Date();
		Date de=new Date();
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM");
		
		try {
			ds=f.parse(m);
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		Calendar c=Calendar.getInstance();
		c.setTime(ds);
		c.add(Calendar.MONTH, +1);
		de=c.getTime();
		String dstart=m;
		String dend=f.format(de);
		List<DiaryDescription> MonthDiary = DataProvider_DiaryInfo.GetInquiredDairyDescription(user, "","", dstart, dend);
		
		return MonthDiary;
	}
        public DiaryMultiMedia GetDiaryMultiMedia(int diaryid)
        {
            return DataProvider_DiaryInfo.GetDairyDiaryMultiMedia(diaryid);
        }

}
