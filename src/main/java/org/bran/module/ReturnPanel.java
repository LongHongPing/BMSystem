/**
 * FileName: ReturnPanel
 * Author:   hplong
 * Date:     2018-12-08 15:07
 * Description: 还书面板
 */
package org.bran.module;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReturnPanel extends JPanel {
    //标签 显示,姓名，是否在黑名单，已借阅册数，最大借阅数
    private JLabel borrowNum;
    private JLabel maxNum;
    private JLabel name;
    private JLabel blackList;
    private JLabel typeName;

    //文本框 搜索输入
    private JTextField input;
    //按钮 查找 借阅 归还 更新
    private JButton queryB;
    private JButton borrowB;
    private JButton returnB;
    private JButton updateB;
    //表格 用于借阅
    private JTable table;
    private DefaultTableModel model;
    private String[] headers={"勾选","书号","ISBN","书名","出版社","状态"};
    private Object[][] cellData={{false,"","","","",""}};
    private Object[] rowData={false,"","","","",""};
    //表格 用于归还
    private JTable table2;
    private DefaultTableModel model2;
    private String[] headers2={"勾选","书号","书名","借阅日期","期限/月"};
    private Object[][] cellData2={{false,"","","",""}};
    private Object[] rowData2={false,"","","",""};
    //下拉框
    private JComboBox<String> combobox;
    private String[] querySort={"查询书名","查询书号","查询ISBN","查询出版社","查询作者"};



}

