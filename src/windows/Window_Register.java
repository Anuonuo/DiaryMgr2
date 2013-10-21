package windows;

import programManagers.I_ManagerGeneral_Service;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class Window_Register extends JDialog{
	

	private JLabel lab1;
	private JLabel lab2;
	private JLabel lab3;
	private JTextField text;
	private JPasswordField password1;
	private JPasswordField password2;
	private JButton but1;
	private JButton but2;
	private I_ManagerGeneral_Service gm;
	

	public Window_Register(I_ManagerGeneral_Service gem){
		this.setTitle("登陆");
		this.setSize(400, 280);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.gm=gem;
		
		text = new JTextField(30);
		password1 = new JPasswordField(30);
		password2 = new JPasswordField(30);
		lab1 = new JLabel("用  户  名：");
		lab2 = new JLabel("密       码：");
		lab3 = new JLabel("确认密码：");
		but1 = new JButton("注  册");
		but2 = new JButton("重  置");
		
		this.setLayout(null);
		lab1.setBounds(60,30,70,30);
		lab2.setBounds(60,70,70,30);
		lab3.setBounds(60,110,70,30);
		text.setBounds(150,30,180,30);
		password1.setBounds(150,70,180,30);
		password2.setBounds(150,110,180,30);
		but1.setBounds(90,180,70,30);
		but2.setBounds(230,180,70,30);
		
		
		but1.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent arg0){
				//添加事件处理代码
				String p1=String.valueOf(password1.getPassword());
				String p2=String.valueOf(password2.getPassword());
				String user=text.getText();
				if(user.equals(""))
				{
					JOptionPane.showMessageDialog(but1, "请输入用户名");
					return;
				}
				if(p1.equals(p2))
				{
					gm.AddNewUser(user, p1);
					JOptionPane.showMessageDialog(but1, "注册成功");
				}
				else
					JOptionPane.showMessageDialog(but1, "密码不一致");
				
			}
		});
		
		but2.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent arg0){
				//添加事件处理代码
				password1.setText("");
				password2.setText("");
				
			}
		});
		
		
		this.add(lab1);
		this.add(lab3);
		this.add(lab2);
		this.add(text);
		this.add(password1);
		this.add(password2);
		this.add(but1);
		this.add(but2);
		this.setModalityType(DEFAULT_MODALITY_TYPE);
                this.setVisible(true);	
	}
}
