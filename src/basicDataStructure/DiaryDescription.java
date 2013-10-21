/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basicDataStructure;

/**
 *
 * @author Administrator
 */
public class DiaryDescription {
        private String DateTime;
	private int Id;
	private String Title;
	private String Type;
	private String User;
	private String StringContent;
	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}
	public String getDateTime() {
		return DateTime;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getId() {
		return Id;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getTitle() {
		return Title;
	}
	public void setUser(String user) {
		User = user;
	}
	public String getUser() {
		return User;
	}
	public void setStringContent(String content) {
		StringContent = content;
	}
	public String getStringContent() {
		return StringContent;
	}
	public void setDiaryType(String cls) {
		Type=cls;
	}
	public String getDiaryType() {
		return Type;
	}
}
