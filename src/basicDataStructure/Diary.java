package basicDataStructure;
public class Diary {
	
	private String DateTime;
	private int Id;
	private String Title;
	private String Type;
	private String User;
	private String StringContent;
        private byte [] RichContent;
        private byte [] Voice;
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
	public void setRichContent(byte [] rich)
        {
            RichContent=rich;
        }
        public byte [] getRichContent()
        {
            return RichContent;
        }
        public void setVoice(byte [] voice)
        {
            Voice=voice;
        }
        public byte [] getVoice()
        {
            return Voice;
        }
        public DiaryDescription getDescription()
        {
            DiaryDescription pde=new DiaryDescription();
            pde.setId(getId());
            pde.setDateTime(getDateTime());
            pde.setDiaryType(getDiaryType());
            pde.setStringContent(getStringContent());
            pde.setTitle(getTitle());
            return pde;
        }

}
