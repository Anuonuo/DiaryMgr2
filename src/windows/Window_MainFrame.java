/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package windows;
import programManagers.I_ManagerGeneral_Service;
import basicDataStructure.*;
import com.eltima.components.ui.DatePicker;
import javax.swing.JButton;
import javax.swing.JToolBar;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import audio.*;
/**
 *
 * @author Administrator
 */
public class Window_MainFrame extends javax.swing.JFrame implements I_Message_From_PanelOneDiary_To_MFrame{

    /**
     * Creates new form Window_MainFrame
     */
    //总管接口实例，初始化时赋值
    private I_ManagerGeneral_Service gm;
    //时间控件
    private DatePicker datepick1;
    private DatePicker datepick2;
    private DatePicker datepick3;
    private DatePicker datepick4;
    //当前用户
    private String currentUser;
    //当前正在编辑的日记id，如果是新建，则值为-1
    int editingDiaryID;
    Panel_OneDiaryElement selectedDairyElement;
    public Window_MainFrame() {
        initComponents();
    }
    public Window_MainFrame(I_ManagerGeneral_Service gm) {
        this.gm=gm;
        currentUser=gm.GetCurrentUser();
        initComponents();
        MyInit();
    }
    private void MyInit()
    {
        this.setTitle("生活管理日记 -------------当前登录用户："+currentUser);
        SetPanelToolBar(jToolBar2);
        datepick1=SetDatePicker(jp_DatePick_Container1);
        System.out.print(datepick1.getText());
        datepick2=SetDatePicker(jp_DatePick_Container2);
        datepick3=SetDatePicker(jp_DatePick_Container3);
        datepick4=SetDatePicker(jp_DatePick_Container4);
        SetTypeComboBox(gm.GetUserTypesList());
        ShowDiaryDescriptionItemListOnPanel(jpanel_DiaryList,gm.GetRecentDiaryByUser(5));
        SetJTree();
        SetSingleDiaryDisplayAreaUnEditable();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    private void SetJTree(){
		
		final DefaultMutableTreeNode root = new javax.swing.tree.DefaultMutableTreeNode("我的日记"); 
		jtree_YearMonth = new JTree(root);
                
                DefaultMutableTreeNode recent=new DefaultMutableTreeNode("最近修改");
		root.add(recent);
                
		Date da=new Date();
		@SuppressWarnings("deprecation")
		int y=da.getYear()+1900;
		for(int i=0;i<5;i++)
		{
			String name=String.valueOf(y-i);
			name+="年";
			DefaultMutableTreeNode fchild=new DefaultMutableTreeNode(name);
			for(int j=12;j>0;j--)
			{
				String name2=String.valueOf(j);
				if(name2.length()<2)
					name2="0"+name2;
				name2+="月";
				DefaultMutableTreeNode schild=new DefaultMutableTreeNode(name2);
				fchild.add(schild);
			}
			root.add(fchild);			
		}
		jtree_YearMonth.expandRow(0);
		jtree_YearMonth.addTreeSelectionListener(new TreeSelectionListener(){

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO 自动生成的方法存根
				DefaultMutableTreeNode node=(DefaultMutableTreeNode)jtree_YearMonth.getLastSelectedPathComponent();
                                String nodeText=(String)node.getUserObject();
                                    if(nodeText.equals("最近修改"))
                                    {
                                        ShowDiaryDescriptionItemListOnPanel(jpanel_DiaryList,gm.GetRecentDiaryByUser(5));  
					return;
                                    }
				if(node.isRoot()||node.getDepth()==1)
                                {
                                                                      
                                    return;
                                }
				String month=(String)node.getUserObject();
				DefaultMutableTreeNode fathernode=(DefaultMutableTreeNode) node.getParent();				
				String year=(String)fathernode.getUserObject();
				year=year.substring(0, 4);			
				month=month.substring(0, 2);
				String m=year+"-"+month;
				ShowSelectedDiaryList(m);
			}
			
		});
		jScrollPane1.setViewportView(jtree_YearMonth);

	}
    
    private void ShowDiaryDescriptionItemListOnPanel(JPanel panel,List<DiaryDescription> dl)
    {
        panel.removeAll();
        panel.setLayout(new FlowLayout());
        Dimension d=new Dimension(510,550);
        if(dl.size()>9)
        {
            d=new Dimension(510, dl.size()*62);
        }
        
        panel.setPreferredSize(d);
        for(DiaryDescription dd:dl)
        {
             Panel_OneDiaryElement de=new Panel_OneDiaryElement();
             de.setRespondingInterface(this);
             de.setDiaryID(dd.getId());
             de.setDiaryTime(dd.getDateTime());
             de.setDiaryTitle(dd.getTitle());
             de.setDiaryType(dd.getDiaryType());
             de.setDiaryStringContent(dd.getStringContent());
             panel.add(de);
        }
        this.repaint();
    }
    private void ShowSelectedDiaryList(String m)
    {
        List<DiaryDescription> dl=gm.GetMonthDairyDescription(m);
        ShowDiaryDescriptionItemListOnPanel(jpanel_DiaryList, dl);
        
    }
    private DatePicker SetDatePicker(JPanel container)
    {
        final String DefaultFormat1 = "yyyy-MM-dd";
	Date date1=new Date();
	Font font1=new Font("Times New Roman", Font.BOLD, 14);
	Dimension dimension1=new Dimension(170,28);
        DatePicker dp= new DatePicker(date1,DefaultFormat1,font1,dimension1);
	dp.setTimePanleVisible(true);
        container.add(dp);
        return dp;
    }
    private void SetTypeComboBox(List<String> types)
    {
        jcb_DiaryType1.removeAllItems();
        jcb_DiaryType2.removeAllItems();
	jcb_DiaryType1.addItem("");
	for(String type:types)
	{
		jcb_DiaryType1.addItem(type);
		jcb_DiaryType2.addItem(type);
	}
    }
    private void SetSingleDiaryDisplayAreaUnEditable()
    {
        jtf_DiaryTitle.setEditable(false);
        jb_SaveUpdate.setEnabled(false);
        jb_InsertPicture.setEnabled(false);
        jb_RecordVoice.setEnabled(false);
        jtp_DiaryContent.setEditable(false);
    }
    private void SetSingleDiaryDisplayAreaEditable()
    {
        jtf_DiaryTitle.setEditable(true);
        jb_SaveUpdate.setEnabled(true);
        jb_InsertPicture.setEnabled(true);
        jb_RecordVoice.setEnabled(true);
        jtp_DiaryContent.setEditable(true);
    }
    private void SetSingleDiaryDisplayAreaClear()
    {
        jtf_DiaryTime.setText("");
        jtf_DiaryTitle.setText("");
        jtp_DiaryContent.setText("");
    }
    private Diary getDiaryFromSingleDiaryDisplayArea()
    {
       StyledDocument doc=(StyledDocument)jtp_DiaryContent.getStyledDocument();
       byte[] richContent=UiCommonFuctions.ConvertObjectToByteArray(doc);
       Diary newd=new Diary();
       newd.setId(editingDiaryID);
       newd.setUser(currentUser);
       newd.setDateTime(jtf_DiaryTime.getText());
       newd.setTitle(jtf_DiaryTitle.getText());
       newd.setDiaryType((String)jcb_DiaryType2.getSelectedItem());
       newd.setStringContent(jtp_DiaryContent.getText());
       newd.setRichContent(richContent);
       return newd;
    }
     public void setWhichElementIsSelected(Panel_OneDiaryElement poe)
    {
        editingDiaryID=poe.getDiaryID();
        selectedDairyElement=poe;
        jtf_DiaryTime.setText(poe.getDiaryTime());
        jtf_DiaryTitle.setText(poe.getDiaryTitle());
        jcb_DiaryType2.setSelectedItem(poe.getDiaryType());
        SetSingleDiaryDisplayAreaUnEditable();
        DiaryMultiMedia dmm=gm.GetDiaryMultiMedia(editingDiaryID);
        Dimension d=new Dimension(600,700);
        jtp_DiaryContent.setPreferredSize(d);
        if(dmm.getrichContent()!=null)
        {
        Object ob=UiCommonFuctions.ConvertByteArrayToObject(dmm.getrichContent());
        if (ob!=null)
        {
        StyledDocument doc=(StyledDocument)ob;
        jtp_DiaryContent.setStyledDocument(doc);
        validate();
        }
        }
        
    }
    private void SetPanelToolBar(JToolBar toolbar){
    	
    	JButton button1;
        JButton button2;
        JButton button3;
        JButton button4;
        JButton button5;
        JButton button6;
    	toolbar.setFloatable(false);
    	toolbar.setLayout(null);
    	
    	button1 = new JButton();
		button1.setText("新建");
		ImageIcon ico = new ImageIcon("images/file-new.png");
		button1.setBounds(1,1, 100,52);
		Image temp=ico.getImage().getScaledInstance(button1.getWidth()-50,button1.getHeight(),ico.getImage().SCALE_DEFAULT);
        ico=new ImageIcon(temp); 
		button1.setIcon(ico);
		button1.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent arg0){
                            SetSingleDiaryDisplayAreaClear();
                            SetSingleDiaryDisplayAreaEditable();
                            jtf_DiaryTime.setText(UiCommonFuctions.getCurrentDateTime());
                            jb_SaveUpdate.setText("保存");
                            editingDiaryID=-1;

			}
		});
		button2 = new JButton();
		button2.setText("删除");
		ico = new ImageIcon("images/delete.png");
		button2.setBounds(102,1, 100,52);
		temp=ico.getImage().getScaledInstance(button2.getWidth()-50,button2.getHeight(),ico.getImage().SCALE_DEFAULT);
        ico=new ImageIcon(temp); 
		button2.setIcon(ico);
		button2.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent arg0){
				if(selectedDairyElement==null)
                                    return;
                                jpanel_DiaryList.remove(selectedDairyElement);
				 repaint();
                                 gm.DeleteDairy(selectedDairyElement.getDiaryID());
                                 SetSingleDiaryDisplayAreaClear();
                                 SetSingleDiaryDisplayAreaUnEditable();
			}
		});
		button3 = new JButton();
		button3.setText("编辑");
		ico = new ImageIcon("images/write.png");
		button3.setBounds(203,1, 100,52);
		temp=ico.getImage().getScaledInstance(button3.getWidth()-50,button3.getHeight(),ico.getImage().SCALE_DEFAULT);
                ico=new ImageIcon(temp); 
		button3.setIcon(ico);
		button3.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent arg0){
			    SetSingleDiaryDisplayAreaEditable();
                            jb_SaveUpdate.setText("更新");
			}
		});
		
		button4 = new JButton();
		button4.setText("分类管理");
		ico = new ImageIcon("images/email_new.png");
		button4.setBounds(304,1, 120,52);
		temp=ico.getImage().getScaledInstance(button4.getWidth()-70,button4.getHeight(),ico.getImage().SCALE_DEFAULT);
                ico=new ImageIcon(temp); 
		button4.setIcon(ico);
		button4.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent arg0){
                            gm.ShowTypeManageWindow();
                            SetTypeComboBox(gm.GetUserTypesList());
			}
		});
                
		button5 = new JButton();
		button5.setText("更改密码");
		ico = new ImageIcon("images/key.png");
		button5.setBounds(425,1, 120,52);
		temp=ico.getImage().getScaledInstance(button5.getWidth()-70,button5.getHeight(),ico.getImage().SCALE_DEFAULT);
                ico=new ImageIcon(temp); 
                button5.setIcon(ico);
                button5.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent arg0){
                            gm.ShowModiPasswordWindow();
			}
		});
        
                button6 = new JButton();
		button6.setText("退出");
		ico = new ImageIcon("images/door.png");
		button6.setBounds(546,1, 100,52);
		temp=ico.getImage().getScaledInstance(button6.getWidth()-50,button6.getHeight(),ico.getImage().SCALE_DEFAULT);
                ico=new ImageIcon(temp); 
		button6.setIcon(ico);
                button6.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent arg0){
                            System.exit(0);
			}
		});
        
		toolbar.add(button1);
		toolbar.add(button2);
		toolbar.add(button3);
		toolbar.add(button4);
		toolbar.add(button5);
		toolbar.add(button6);
	}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtree_YearMonth = new javax.swing.JTree();
        jPanel5 = new javax.swing.JPanel();
        jb_Inquire = new javax.swing.JButton();
        jcb_DiaryType1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jp_DatePick_Container2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jtf_InquireKey = new javax.swing.JTextField();
        jp_DatePick_Container1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jtf_DiaryTime = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jcb_DiaryType2 = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jtf_DiaryTitle = new javax.swing.JTextField();
        jScrollPane12 = new javax.swing.JScrollPane();
        jtp_DiaryContent = new javax.swing.JTextPane();
        jb_InsertPicture = new javax.swing.JButton();
        jCheckBox2 = new javax.swing.JCheckBox();
        jb_PlayVoice = new javax.swing.JButton();
        jb_SaveUpdate = new javax.swing.JButton();
        jb_RecordVoice = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        jScrollPane13 = new javax.swing.JScrollPane();
        jpanel_DiaryList = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jb_InquirePublicDiary = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jp_DatePick_Container4 = new javax.swing.JPanel();
        jtf_UserName = new javax.swing.JTextField();
        jp_DatePick_Container3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jb_InquireDiaryWithMyComment = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jta_WriteComment = new javax.swing.JTextArea();
        jScrollPane11 = new javax.swing.JScrollPane();
        jta_HistoryComment = new javax.swing.JTextArea();
        jb_WriteComment = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jtf_DiaryTime1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jl_UsrName = new javax.swing.JLabel();
        jb_PlayVoice2 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtp_Content2 = new javax.swing.JTextPane();
        jl_DiaryTitle = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jp_CommentDiaryList = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("生活日记管理");

        jTabbedPane4.setForeground(new java.awt.Color(0, 102, 102));
        jTabbedPane4.setFont(new java.awt.Font("宋体", 1, 18)); // NOI18N

        jScrollPane1.setViewportView(jtree_YearMonth);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jb_Inquire.setText(" 查  询 ");
        jb_Inquire.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jb_InquireMouseClicked(evt);
            }
        });

        jcb_DiaryType1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("结束时间：");

        javax.swing.GroupLayout jp_DatePick_Container2Layout = new javax.swing.GroupLayout(jp_DatePick_Container2);
        jp_DatePick_Container2.setLayout(jp_DatePick_Container2Layout);
        jp_DatePick_Container2Layout.setHorizontalGroup(
            jp_DatePick_Container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 169, Short.MAX_VALUE)
        );
        jp_DatePick_Container2Layout.setVerticalGroup(
            jp_DatePick_Container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel4.setText("分类：");

        javax.swing.GroupLayout jp_DatePick_Container1Layout = new javax.swing.GroupLayout(jp_DatePick_Container1);
        jp_DatePick_Container1.setLayout(jp_DatePick_Container1Layout);
        jp_DatePick_Container1Layout.setHorizontalGroup(
            jp_DatePick_Container1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 169, Short.MAX_VALUE)
        );
        jp_DatePick_Container1Layout.setVerticalGroup(
            jp_DatePick_Container1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel1.setText("起始时间：");

        jLabel3.setText("关键字：");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jp_DatePick_Container1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jp_DatePick_Container2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jcb_DiaryType1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtf_InquireKey, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jb_Inquire)
                        .addGap(18, 18, 18))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jp_DatePick_Container2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jp_DatePick_Container1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jcb_DiaryType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jtf_InquireKey)
                            .addComponent(jb_Inquire, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setText("时间：");

        jtf_DiaryTime.setEnabled(false);

        jLabel6.setText("分类：");

        jcb_DiaryType2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("标题：");
        jLabel7.setToolTipText("");

        jtf_DiaryTitle.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N

        jScrollPane12.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane12.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane12.setViewportView(jtp_DiaryContent);

        jb_InsertPicture.setText("插入图片");
        jb_InsertPicture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jb_InsertPictureMouseClicked(evt);
            }
        });

        jCheckBox2.setText("允许评论");

        jb_PlayVoice.setText("播放");

        jb_SaveUpdate.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        jb_SaveUpdate.setText("保存");
        jb_SaveUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jb_SaveUpdateMouseClicked(evt);
            }
        });

        jb_RecordVoice.setText("录音");
        jb_RecordVoice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jb_RecordVoiceMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtf_DiaryTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtf_DiaryTime, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jb_SaveUpdate)
                                .addGap(0, 27, Short.MAX_VALUE)))
                        .addGap(17, 17, 17))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcb_DiaryType2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jb_InsertPicture)
                        .addGap(18, 18, 18)
                        .addComponent(jb_RecordVoice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jb_PlayVoice)
                        .addGap(33, 33, 33))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtf_DiaryTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_SaveUpdate)
                    .addComponent(jLabel7)
                    .addComponent(jtf_DiaryTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jcb_DiaryType2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox2)
                    .addComponent(jb_InsertPicture)
                    .addComponent(jb_PlayVoice)
                    .addComponent(jb_RecordVoice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane12))
        );

        jToolBar2.setRollover(true);

        jScrollPane13.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane13.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        javax.swing.GroupLayout jpanel_DiaryListLayout = new javax.swing.GroupLayout(jpanel_DiaryList);
        jpanel_DiaryList.setLayout(jpanel_DiaryListLayout);
        jpanel_DiaryListLayout.setHorizontalGroup(
            jpanel_DiaryListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );
        jpanel_DiaryListLayout.setVerticalGroup(
            jpanel_DiaryListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
        );

        jScrollPane13.setViewportView(jpanel_DiaryList);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );

        jTabbedPane4.addTab("日记管理", jPanel2);

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jb_InquirePublicDiary.setText("找公开日记");

        jLabel9.setText("结束时间：");

        javax.swing.GroupLayout jp_DatePick_Container4Layout = new javax.swing.GroupLayout(jp_DatePick_Container4);
        jp_DatePick_Container4.setLayout(jp_DatePick_Container4Layout);
        jp_DatePick_Container4Layout.setHorizontalGroup(
            jp_DatePick_Container4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jp_DatePick_Container4Layout.setVerticalGroup(
            jp_DatePick_Container4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jp_DatePick_Container3Layout = new javax.swing.GroupLayout(jp_DatePick_Container3);
        jp_DatePick_Container3.setLayout(jp_DatePick_Container3Layout);
        jp_DatePick_Container3Layout.setHorizontalGroup(
            jp_DatePick_Container3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 169, Short.MAX_VALUE)
        );
        jp_DatePick_Container3Layout.setVerticalGroup(
            jp_DatePick_Container3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jLabel11.setText("起始时间：");

        jLabel12.setText("用户");

        jb_InquireDiaryWithMyComment.setText("找我的评论");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jp_DatePick_Container3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jp_DatePick_Container4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtf_UserName, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))))
                .addGap(28, 28, 28)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jb_InquireDiaryWithMyComment, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_InquirePublicDiary, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jp_DatePick_Container3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jp_DatePick_Container4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jb_InquirePublicDiary, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jtf_UserName, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_InquireDiaryWithMyComment, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("评论"));

        jta_WriteComment.setColumns(20);
        jta_WriteComment.setRows(5);
        jScrollPane10.setViewportView(jta_WriteComment);

        jScrollPane11.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane11.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jta_HistoryComment.setColumns(20);
        jta_HistoryComment.setRows(5);
        jScrollPane11.setViewportView(jta_HistoryComment);

        jb_WriteComment.setText("发表评论");

        jLabel20.setText("历史评论：");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jb_WriteComment))
            .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_WriteComment)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel8.setText("标题：");
        jLabel8.setToolTipText("");

        jLabel16.setText("时间：");

        jtf_DiaryTime1.setText("2013-10-19 18:30");
        jtf_DiaryTime1.setEnabled(false);

        jLabel17.setText("用户：");

        jl_UsrName.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        jl_UsrName.setForeground(new java.awt.Color(0, 0, 204));
        jl_UsrName.setText("我是谁");

        jb_PlayVoice2.setText("播放录音");

        jLabel19.setText("内容：");

        jScrollPane3.setViewportView(jtp_Content2);

        jl_DiaryTitle.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        jl_DiaryTitle.setText("这是我的标题");
        jl_DiaryTitle.setToolTipText("");
        jl_DiaryTitle.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(340, 340, 340)
                                .addComponent(jb_PlayVoice2))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(jl_DiaryTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jl_UsrName, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel16)
                                .addGap(18, 18, 18)
                                .addComponent(jtf_DiaryTime1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jl_DiaryTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jl_UsrName)
                    .addComponent(jLabel16)
                    .addComponent(jtf_DiaryTime1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jb_PlayVoice2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jp_CommentDiaryListLayout = new javax.swing.GroupLayout(jp_CommentDiaryList);
        jp_CommentDiaryList.setLayout(jp_CommentDiaryListLayout);
        jp_CommentDiaryListLayout.setHorizontalGroup(
            jp_CommentDiaryListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 386, Short.MAX_VALUE)
        );
        jp_CommentDiaryListLayout.setVerticalGroup(
            jp_CommentDiaryListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 553, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jp_CommentDiaryList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );

        jTabbedPane4.addTab("评论大厅", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 736, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        // TODO add your handling code here:
        evt.getButton();
    }//GEN-LAST:event_jList1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2MouseClicked

    private void jb_InsertPictureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_InsertPictureMouseClicked
        File pfile=UiCommonFuctions.ChoosePictureFile();
        if(pfile==null)
        {
            return;
        }
        ImageIcon ficon=new ImageIcon(pfile.getPath());        
        jtp_DiaryContent.insertIcon(UiCommonFuctions.getScalcedImageIcon(ficon, 300)); 
    }//GEN-LAST:event_jb_InsertPictureMouseClicked

    private void jb_SaveUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_SaveUpdateMouseClicked
       Diary d=getDiaryFromSingleDiaryDisplayArea();
        if(d.getId()==-1)
             gm.AddNewDiary(d);
       else
           gm.UpdateDiary(d);
        SetSingleDiaryDisplayAreaUnEditable();
        ShowDiaryDescriptionItemListOnPanel(jpanel_DiaryList,gm.GetRecentDiaryByUser(1));
        this.repaint();
        
        
           
    }//GEN-LAST:event_jb_SaveUpdateMouseClicked

    private void jb_InquireMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_InquireMouseClicked
        System.out.print(datepick1.getText());
        List<DiaryDescription> ldd=gm.GetInquiredDairiyDescription(jtf_InquireKey.getText(), (String)jcb_DiaryType1.getSelectedItem(),datepick1.getText(), datepick2.getText());
        ShowDiaryDescriptionItemListOnPanel(jpanel_DiaryList, ldd);
        
    }//GEN-LAST:event_jb_InquireMouseClicked

    private void jb_RecordVoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_RecordVoiceMouseClicked
        VoiceRecorder vr=new VoiceRecorder();
        
    }//GEN-LAST:event_jb_RecordVoiceMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Window_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Window_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Window_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Window_MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JButton jb_Inquire;
    private javax.swing.JButton jb_InquireDiaryWithMyComment;
    private javax.swing.JButton jb_InquirePublicDiary;
    private javax.swing.JButton jb_InsertPicture;
    private javax.swing.JButton jb_PlayVoice;
    private javax.swing.JButton jb_PlayVoice2;
    private javax.swing.JButton jb_RecordVoice;
    private javax.swing.JButton jb_SaveUpdate;
    private javax.swing.JButton jb_WriteComment;
    private javax.swing.JComboBox jcb_DiaryType1;
    private javax.swing.JComboBox jcb_DiaryType2;
    private javax.swing.JLabel jl_DiaryTitle;
    private javax.swing.JLabel jl_UsrName;
    private javax.swing.JPanel jp_CommentDiaryList;
    private javax.swing.JPanel jp_DatePick_Container1;
    private javax.swing.JPanel jp_DatePick_Container2;
    private javax.swing.JPanel jp_DatePick_Container3;
    private javax.swing.JPanel jp_DatePick_Container4;
    private javax.swing.JPanel jpanel_DiaryList;
    private javax.swing.JTextArea jta_HistoryComment;
    private javax.swing.JTextArea jta_WriteComment;
    private javax.swing.JTextField jtf_DiaryTime;
    private javax.swing.JTextField jtf_DiaryTime1;
    private javax.swing.JTextField jtf_DiaryTitle;
    private javax.swing.JTextField jtf_InquireKey;
    private javax.swing.JTextField jtf_UserName;
    private javax.swing.JTextPane jtp_Content2;
    private javax.swing.JTextPane jtp_DiaryContent;
    private javax.swing.JTree jtree_YearMonth;
    // End of variables declaration//GEN-END:variables
}
