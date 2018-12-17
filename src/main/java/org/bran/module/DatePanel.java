package org.bran.module;

import java.awt.FlowLayout;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class DatePanel extends JPanel {
	private JDatePickerImpl datePicker;
	DatePanel(){
		this.setLayout(new FlowLayout());
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		// Don't know about the formatter, but there it is...
		 datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		this.add(datePicker);
	}
	public void setLeft(){
		this.setAlignmentX(LEFT_ALIGNMENT);
	}
	public void setRight(){
		this.setAlignmentX(RIGHT_ALIGNMENT);
	}
	/**
	 * 
	 *<p>Description:此方法用于返回在面板上选中的适用于数据库操作的Date对象 </p>
	 * @return java.sql.Date类型的时间
	 */
	public Date getDate(){
		java.util.Date selectedTime=(java.util.Date)datePicker.getModel().getValue();
		if(selectedTime==null){
			return null;
		}
		long time=selectedTime.getTime();
		return new Date(time);
	}
	public static void main(String[] args) {
		JFrame frame=new JFrame("test");
		frame.setSize(200, 100);
		frame.getContentPane().add(new DatePanel());
		frame.setVisible(true);
	}
}
class DateLabelFormatter extends AbstractFormatter {

    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }

        return "";
    }

}
