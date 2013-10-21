package windows;
import programManagers.I_ManagerGeneral_Service;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;



public class Window_Login extends JFrame{
	

	private JLabel lab1;
	private JLabel lab2;
	private JComboBox<String> cUser;
	private JPasswordField password;
	private JButton but1;
	private JButton but2;
	
	private I_ManagerGeneral_Service gm;
        
	public void SetUserList(List<String> users)
	{
		for(String uName: users)
		{
		    cUser.addItem(uName);
		}
	}
        
	public void CloseWindow()
	{
		this.dispose();
	}
	public Window_Login(I_ManagerGeneral_Service gem){
		this.setTitle("登陆");
		this.setSize(400, 250);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		gm=gem;                
		cUser = new JComboBox<String>();
		password = new JPasswordField(30);
		lab1 = new JLabel("用户名：");
		lab2 = new JLabel("密  码：");
		but1 = new JButton("登  陆");
		but2 = new JButton("注  册");
		this.setLayout(null);
		lab1.setBounds(60,30,70,30);
		lab2.setBounds(60,80,70,30);
		cUser.setBounds(150,30,180,30);
		password.setBounds(150,80,180,30);
		but1.setBounds(90,150,70,30);
		but2.setBounds(230,150,70,30);
		SetUserList(gm.GetUserList());
		but1.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent arg0){
				//添加事件处理代码
				String user=(String)cUser.getSelectedItem();
				if(gm.VerifyUser(user, String.valueOf(password.getPassword())))
				{
					gm.SetCurrentUser(user);
                                        gm.ShowMainWindow();;
					CloseWindow();
				}

			}
		});
		
		but2.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent arg0){
				//添加事件处理代码
				gm.ShowRegiserWindow();
                                CloseWindow();
			}
		});
		
		this.add(lab1);
		this.add(cUser);
		this.add(lab2);
		this.add(password);
		this.add(but1);
		this.add(but2);
		this.setVisible(true);
		
		
		
			/*		
		this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {            	
            		System.exit(0);
            }
        });
        */
	}
}
