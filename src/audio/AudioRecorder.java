package audio;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import programManagers.Manager_General;



import java.awt.BorderLayout;
import java.awt.Button;

import java.awt.Frame;

import java.awt.FlowLayout;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

public class AudioRecorder extends JFrame implements I_AudioRecord{
	private boolean stopCapture = false;       //����¼����־
    private AudioFormat audioFormat;           //¼����ʽ

    //��ȡ���ݣ���TargetDataLineд��ByteArrayOutputStream¼��
   private ByteArrayOutputStream byteArrayOutputStream;
   private int totaldatasize = 0;
   private TargetDataLine targetDataLine;

   //����Ԫ��
   private JButton captureBtn = new JButton("¼��");
   private JButton stopBtn = new JButton("ֹͣ");
   private JButton playBtn = new JButton("����");
   private JButton saveBtn = new JButton("����");
   
    //�������ݣ���AudioInputStreamд��SourceDataLine����
   private AudioInputStream audioInputStream;
   private SourceDataLine sourceDataLine;

   boolean canBeginRecord;
   boolean canPlayrecorded;
   boolean canGetVoiceBinary;
   byte[] audioData;
   

   public void ShowOwnWindow()
   {
	   this.setVisible(true);
   }
   private void initComponent(){
   	setTitle("¼����");
   	setSize(300, 80);
	this.setLocationRelativeTo(null);
   	this.setLayout(new BorderLayout());
   	

   	captureBtn = new JButton("¼��");
   	stopBtn = new JButton("ֹͣ");
   	playBtn = new JButton("����");
   	saveBtn = new JButton("����");
       captureBtn.setEnabled(true);
       stopBtn.setEnabled(false);
       playBtn.setEnabled(false);
       saveBtn.setEnabled(false);

       //���ô�������
   	JPanel panel = new JPanel();
   	panel.setLayout(new FlowLayout());
       panel.add(captureBtn);
       panel.add(stopBtn);
       panel.add(playBtn);
       panel.add(saveBtn);
       this.add(panel, "Center");

       //ע��¼���¼�
       captureBtn.addActionListener(new ActionListener() {

           public void actionPerformed(ActionEvent e) {

               captureBtn.setEnabled(false);
               stopBtn.setEnabled(true);
               playBtn.setEnabled(false);
               saveBtn.setEnabled(false);

               //��ʼ¼��
               capture();
           }
       });


       //ע��ֹͣ�¼�
       stopBtn.addActionListener(new ActionListener() {
       	

           public void actionPerformed(ActionEvent e) {

               captureBtn.setEnabled(true);
               stopBtn.setEnabled(false);
               playBtn.setEnabled(true);
               saveBtn.setEnabled(true);

               //ֹͣ¼��
               stop();
           }
       });


       //ע�Ქ���¼�
       playBtn.addActionListener(new ActionListener() {

           public void actionPerformed(ActionEvent e) {

               //����¼��
               play();
           }
       });


       //ע�ᱣ���¼�
       saveBtn.addActionListener(new ActionListener() {

           public void actionPerformed(ActionEvent e) {

               //����¼��
        	   SaveAsWaveFile();
           }
       });
       
   }

   //¼���¼������浽ByteArrayOutputStream��
   public AudioRecorder()
   {
	   initComponent();
	   canBeginRecord=true;
	   canPlayrecorded=false;
	   canGetVoiceBinary=false;
   }
   private void capture() 
   {
       try
       {
           //��¼��
           audioFormat = getAudioFormat();

           DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

           targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

           targetDataLine.open(audioFormat);

           targetDataLine.start();

           //���������߳̽���¼��

           Thread captureThread = new Thread(new CaptureThread());

           captureThread.start();

       } 
       catch (Exception e) 
       {

           e.printStackTrace();

       }
	   
   }

   //��2������ByteArrayOutputStream�е�����
   private void play() {

       try {

           //ȡ��¼������

           if(audioData==null)
        	   return;

           //ת����������

           InputStream byteArrayInputStream = new ByteArrayInputStream(

                   audioData);

           AudioFormat audioFormat = getAudioFormat();

           audioInputStream = new AudioInputStream(byteArrayInputStream,

                   audioFormat, audioData.length / audioFormat.getFrameSize());

           DataLine.Info dataLineInfo = new DataLine.Info(

                   SourceDataLine.class, audioFormat);

           sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

           sourceDataLine.open(audioFormat);

           sourceDataLine.start();

           //���������߳̽��в���

           Thread playThread = new Thread(new PlayThread());

           playThread.start();

       } catch (Exception e) {

           e.printStackTrace();

           System.exit(0);

       }

   }

   //��3��ֹͣ¼��

   public void stop() {
       stopCapture = true;
   }
   private AudioFormat getAudioFormat() {

       float sampleRate = 16000.0F;
       int sampleSizeInBits = 16;
       int channels = 1;
       boolean signed = true;
       boolean bigEndian = false;

       return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,

               bigEndian);

   }
   
   class CaptureThread extends Thread {

	    //��ʱ����

	    byte tempBuffer[] = new byte[10000];

	    public void run() {
	    	
	    	audioData = null;

	        byteArrayOutputStream = new ByteArrayOutputStream();

	        totaldatasize = 0;

	        stopCapture = false;

	        try {//ѭ��ִ�У�ֱ������ֹͣ¼����ť

	            while (!stopCapture) {

	                //��ȡ10000������

	                int cnt = targetDataLine.read(tempBuffer, 0,

	                        tempBuffer.length);

	                if (cnt > 0) {

	                    //���������

	                    byteArrayOutputStream.write(tempBuffer, 0, cnt);

	                    totaldatasize += cnt;

	                }

	            }

	            byteArrayOutputStream.close();
	            audioData = byteArrayOutputStream.toByteArray();

	        } catch (Exception e) {

	            e.printStackTrace();

	            System.exit(0);

	        }

	    }

	}
   
   class PlayThread extends Thread {

	    byte tempBuffer[] = new byte[10000];

	    public void run() {

	        try {

	            int cnt;

	            //��ȡ���ݵ���������

	            while ((cnt = audioInputStream.read(tempBuffer, 0,

	                    tempBuffer.length)) != -1) {

	                if (cnt > 0) {

	                    //д�뻺������

	                    sourceDataLine.write(tempBuffer, 0, cnt);

	                }

	            }

	            //Block�ȴ���ʱ���ݱ����Ϊ��

	            sourceDataLine.drain();

	            sourceDataLine.close();

	        } catch (Exception e) {

	            e.printStackTrace();

	            System.exit(0);

	        }

	    }

		

	}
   //����ʵ�ֽӿ�
	public void BeginRecord()
	{
		canBeginRecord=false;
		canGetVoiceBinary=false;
		canPlayrecorded=false;
		capture();

	}
	public byte[] GetVoiceBinary()
	{
		return audioData;
	}
	public void StopRecord()
	{
		stop();
		canBeginRecord=true;
		canGetVoiceBinary=true;
		canPlayrecorded=true;
	}
	public void PlayRecorded()
	{
		play();
	}
	public boolean CanBeginRecord()
	{
		return canBeginRecord;
	}	
	public boolean CanGetVoiceBinary()
	{
		return canGetVoiceBinary;
	}
	public boolean CanPlayRecorded()
	{
		return canPlayrecorded;
	}
	public void SetVoiceBinary(byte[] adata)
	{
		audioData=adata;
		
		canPlayrecorded=true;
		canGetVoiceBinary=true;
	}
	public String SaveAsWaveFile()
	{
		AudioFormat audioFormat = getAudioFormat();
        InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
        audioInputStream = new AudioInputStream(byteArrayInputStream,audioFormat, audioData.length / audioFormat.getFrameSize());

        //д���ļ�
        JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("ѡ������");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("WAV FILES(*.wav)","wav");
		chooser.setFileFilter(filter);
		
		
        int result = chooser.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {    
        	 File file = chooser.getSelectedFile();
        	 if (file.exists()) {
        		 int copy = JOptionPane.showConfirmDialog(null,"�Ƿ�Ҫ���ǵ�ǰ�ļ���", "����", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        		 if (copy == JOptionPane.NO_OPTION){
        			return null;
        		 }
        	 }
        	 
        	try {
				AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, file);
	         	return file.getPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showConfirmDialog(null, "�����ļ�����ʧ�ܣ�", "��ʾ", JOptionPane.YES_OPTION);
		        e.printStackTrace();
		        return null;
			}
         	
            
	}
		return null;

  }
}
