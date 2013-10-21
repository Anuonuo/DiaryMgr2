package audio;

public interface I_AudioRecord {
	public void BeginRecord();
	public byte[] GetVoiceBinary();
	public void StopRecord();
	public void PlayRecorded();
	public boolean CanBeginRecord();
	public boolean CanGetVoiceBinary();
	public boolean CanPlayRecorded();
	public void SetVoiceBinary(byte[] adata);
	public String SaveAsWaveFile();
	

}
