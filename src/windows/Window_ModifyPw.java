package windows;

import programManagers.I_ManagerGeneral_Service;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class Window_ModifyPw extends JDialog{
	

	
	private JLabel lab1;
	private JLabel lab2;
	private JLabel lab3;
	private JTextField text;
	private JPasswordField password1;
	private JPasswordField password2;
	private JButton but1;
	private JButton but2;
	I_ManagerGeneral_Service gm;
	
	public Window_ModifyPw(I_ManagerGeneral_Service gm){
		this.setTitle("密码修改");
		this.setSize(400, 280);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
                this.setModalityType(DEFAULT_MODALITY_TYPE);
		
		this.gm=gm;
                
		text = new JTextField(30);
		password1 = new JPasswordField(30);
		password2 = new JPasswordField(30);
		lab1 = new JLabel("原始密码：");
		lab2 = new JLabel("新  密  码：");
		lab3 = new JLabel("确认密码：");
		but1 = new JButton("修  改");
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
				ModiPassword();
				
			}
		});
		
		but2.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent arg0){
				//添加事件处理代码
				text.setText("");
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
        public void ModiPassword()
        {
            if(!gm.VerifyUser(text.getText()))
				{
					
					JOptionPane.showMessageDialog(but1, "原始密码错误！");
					return;
				}
				String pw1=String.valueOf(password1.getPassword());
				String pw2=String.valueOf(password2.getPassword());
				if(!pw1.equals(pw2))
				{
					JOptionPane.showMessageDialog(but1, "二次密码输入不一致！");
					return;
				}
				gm.ChangePw(pw1);
				JOptionPane.showMessageDialog(but1, "修改成功！！");
        }
}
