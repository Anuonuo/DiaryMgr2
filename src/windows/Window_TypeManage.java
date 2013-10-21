package windows;
import programManagers.I_ManagerGeneral_Service;
import basicDataStructure.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import basicDataStructure.Diary;


public class Window_TypeManage extends JDialog{

	private JLabel lab = new JLabel("类   别 :");
	private JTextField text = new JTextField();
	private JButton butSave = new JButton("添加");
	private JButton butDelete = new JButton("删除");
	private JPanel pantable = new JPanel();
	private JTable table = new JTable();
	private JScrollPane scrollpane;
	private I_ManagerGeneral_Service gm;
	
	private DefaultTableModel myModel;
	
	
	public void SetTypes(List<String> clss)
	{
		while(myModel.getRowCount()>0){
		      myModel.removeRow(myModel.getRowCount()-1);
		}
		int l=clss.size();
		if(l==0)
			return;
		for(String cls:clss)
		{
                    Vector v=new Vector();
                    v.add(cls);
		    myModel.addRow(v);

		}
	}
	private void RemoveOneClassRow()
	{
		
		if(table.getSelectedRow()<0)
			return;
		int i=JOptionPane.showOptionDialog(butDelete, "确实要删除该条分类吗？", "确认", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,null,null,null);
		if(i!=JOptionPane.YES_OPTION)
			return;
		String cls = (String)(table.getValueAt(table.getSelectedRow(), 0));
		gm.DeleteType(cls);
		myModel.removeRow(table.getSelectedRow());

	}
	public Window_TypeManage(I_ManagerGeneral_Service gem){
		this.setTitle("分类管理");
		this.setSize(320, 500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(null);
                this.gm=gem;
		
		
		lab.setBounds(50, 20, 60, 30);
		text.setBounds(110,20,150,30);
		butSave.setBounds(70,60,70,25);
		butDelete.setBounds(170,60, 70, 25);
		butSave.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent arg0){
				//添加事件处理代码
				if(text.getText().equals(""))
				{
					JOptionPane.showMessageDialog(butSave, "请输入一个分类名");
					return;
				}
				gm.AddNewType(text.getText());
				Vector<String> v=new Vector<String>();
				v.add(text.getText());
				myModel.addRow(v);
			}
		});
		
		butDelete.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent arg0){
				//添加事件处理代码
				RemoveOneClassRow();
			}
		});

		
		//添加JTable的过程，JTable添加到进度条scrollpane上，scrollpane再添加到名叫pantable的JPanel上
		pantable.setBounds(60, 110, 200, 310);
		pantable.setLayout(new GridLayout(1,1));

		String[] caption={"分类名"};
		Object[][] RowData={{""},{""}};
		myModel=new DefaultTableModel(RowData,caption);
		table=new JTable(myModel);
		table.setRowHeight(30);
		
		scrollpane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pantable.add(scrollpane);		
		////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		this.add(lab);
		this.add(text);
		this.add(butSave);
		this.add(butDelete);
		this.add(pantable);
		
		
		this.setModalityType(DEFAULT_MODALITY_TYPE);
		 SetTypes(gm.GetUserTypesList());
                 this.setVisible(true);


	}
	
	
	
}
