package dataProvider.dbo;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.ResultSetMetaData;

import java.sql.SQLException;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JOptionPane;

/**

 * 主要作用：用于对象数据库操作的通用类，提供通用的方法

 * @author Gerry

 * 

 */

public class SqlBean {
 

    /**

     * Constructor

     */

    public SqlBean() {

    }

 

    /**

     * 得到数据库连接对象的方法

     * @return Connection 返回数据库连接对象

     */

    private static Connection getConn() {

       /**

        * 通过自定义的属性文件读取类来或者相关数据库连接所用到的字符串

        */

    	/*
       String className = PropertiesReader.getInstance().getProperty("driverName");

       String url =PropertiesReader.getInstance().getProperty("url");

    String userName = PropertiesReader.getInstance().getProperty("user");

       String password = PropertiesReader.getInstance().getProperty("password");
       */

       Connection conn = null;

       try {

           // 加载驱动

           Class.forName("org.sqlite.JDBC");

           // 通过DriverManager类来得到数据库连接对象

           conn = DriverManager.getConnection("jdbc:sqlite:DiaryMgr.db", "","");

       } catch (Exception e) {

           e.printStackTrace();
           JOptionPane.showMessageDialog(null, e.getMessage()+"ddddddd");


       }

       return conn;

    }

 

    /**

     * <pre>

     * 思路:通过传过来的对象进行判断是否是当前对象的实例

     *      如果是当前对象的实例我们就关闭它。

     * 关闭数据库查询操作的相关对象

     * </pre>

     * @param param 操作数据库的相关对象

     */

    private static void closeDB(Object... param) {

       if (param != null) {

           for (Object obj : param) {

              try {

                  if (obj instanceof ResultSet) {

                     ((ResultSet) obj).close();

                      obj = null;

                  }

                  if (obj instanceof PreparedStatement) {

                     ((PreparedStatement) obj).close();

                      obj = null;

                  }

                  if (obj instanceof Connection) {

                     ((Connection) obj).close();

                      obj = null;

                  }

              } catch (SQLException e) {

                  e.printStackTrace();

              }

 

           }

       }

 

    }
    
 

    /**

     * <pre>

     * 创建预编译上下文对象PreparedStatement对象

     * 通过使用Connection对象来得到

     * </pre>

     * @param sql 传入要预编译的SQL语句

     * @return 返回PreparedStatement对象

     */

    private static PreparedStatement getPreparedStatement(String sql) {

       PreparedStatement pst = null;

       try {

           pst = getConn().prepareStatement(sql);

       } catch (SQLException e) {

           e.printStackTrace();

       } finally {

           closeDB(getConn());

       }

       return pst;

    }

 

    /**

     * 设置预编译上下文对象参数的方法

     * @param pst 预编译上下文对象

     * @param param 传入的参数对象《该数传入的可以是单个对象也可以是数组》

     */

    private static void setParam(PreparedStatement pst, Object... param) {

       int length = param.length;

       for (int i = 0; i < length; i++) {

           try {
        	   if(param[i] instanceof byte[])
        		   pst.setBytes(i+1, (byte[])param[i]);
        	   else
                   pst.setObject(i + 1, param[i]);

           } catch (SQLException e) {

              e.printStackTrace();

           }

       }

 

    }
    
    

 

    /**

     * 执行简单SQL中增加，删除，修改的方法

     * @param sql 传入标准SQL语句

     * @param param 传入的参数对象《该数传入的可以是单个对象也可以是数组》

     * @return int 返回当前在数据库中产生的影响行数

     */

    public static int executeUpdate(String sql, Object... param) {

       int rows = 0;

       PreparedStatement pst = getPreparedStatement(sql);

       setParam(pst, param);

       try {

           rows = pst.executeUpdate();

       } catch (SQLException e) {

           e.printStackTrace();

       } finally {

           closeDB(pst);

       }

       return rows;

 

    }

 

    /**

     * 执行SQL语句的所有查询的方法

     * @param sql 传入标准的SQL查询语句

     * @param param 传入的参数对象《该数传入的可以是单个对象也可以是数组》

     * @return List<Map<Object, Object>> 返回装有Map集合对象的泛型集合对象

     */

    public static List<Map<Object, Object>> executeQuery(String sql,

           Object... param) {

       List<Map<Object, Object>> lst = new ArrayList<Map<Object, Object>>();

       Map<Object, Object> map = null;

       ResultSet rs = null;

       ResultSetMetaData rsd = null;

       PreparedStatement pst = getPreparedStatement(sql);

       setParam(pst, param);

       try {

           rs = pst.executeQuery();

           if (rs != null) {

              rsd = rs.getMetaData();

              while (rs.next()) {

                  int columnCount = rsd.getColumnCount();

                  map = new HashMap<Object, Object>();

                  for (int i = 1; i < columnCount+1; i++) {

                     map.put(rsd.getColumnName(i), rs.getObject(i));

                  }

                  lst.add(map);

              }

           }

 

       } catch (SQLException e) {

           e.printStackTrace();

       } finally {

           closeDB(new Object[]{rs,pst});

       }

       return lst;

 

    }


}
