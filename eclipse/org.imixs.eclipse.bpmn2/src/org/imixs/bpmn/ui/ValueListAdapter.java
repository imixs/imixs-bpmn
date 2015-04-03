package org.imixs.bpmn.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This adapter manages a Value List in a string array. The adapter provides methods to add and remove values
 * 
 * This results into a String with <value> elements to be used by UI Widget
 * TableEditor.
 * 
 * 
 * 
 * <code>
 *   <value>1</value>
 *   <value>2</value>
 *   <value>3</value>
 * </code>
 * 
 * 
 * @author rsoika
 *
 */
public class ValueListAdapter {


	List<String> valueList;
	public ValueListAdapter() {
		valueList=new ArrayList<String>();
	}
	/**
	 * converts the valueList string into a array map. 
	 * 
	 * @param valueList
	 */
	public ValueListAdapter(String values) {
		// get List with all current values from valueList
		valueList = getList(values);
	}

	
	/**
	 * adds a new value
	 * 
	 * @param aOption
	 * @return
	 */
	public void addValue(String value) {
		valueList.add(value);
	}
	
	public void removeValue(String value) {
		valueList.remove(value);
	}
	
	/**
	 * moves a given value up inside the array list
	 * @param value
	 */
	public void moveUp(int i) {
		if (i>0)
			Collections.swap(valueList, i, i-1);
	}

	public void moveDown(int i) {
		if (i<valueList.size()-1) 
			Collections.swap(valueList, i, i+1);
	}

	
	public void replaceValue (String sold,String snew) {
		int index=valueList.indexOf(sold); 
		if (index>-1) {
			valueList.set(index, snew);
		} else {
			// old value dos not exist!
			valueList.add(snew);
		}
	}
	
	public List<String> getValueList() {
		return valueList;
	}

	/**
	 * This method returns the valueList string. The value list is ordered by
	 * the given OptionList
	 * 
	 * <code>
	 * 
	 * <value>xxx</value>....
	 * 
	 * </code>
	 * 
	 * @return
	 */
	public String getValue() {

		String result = "";
		for (String aValue : valueList) {
				result += "<value>" + aValue + "</value>";
		}
		return result;
	}

	/**
	 * converts the xml snippet into a String array lst
	 * 
	 * @param value
	 */
	public static List<String> getList(String valueList) {
		ArrayList<String> result = new ArrayList<String>();

		Pattern regex = Pattern.compile("<value>(.*?)</value>", Pattern.DOTALL);
		Matcher matcher = regex.matcher(valueList);
		while (matcher.find()) {
			String DataElements = matcher.group(1);
			result.add(DataElements);
		}
		return result;
	}

	/**
	 * converts a array list of values into a xml snippet
	 * 
	 * @param valueList
	 * @return
	 */
	public static String putList(List<String> valueList) {
		StringBuffer result = new StringBuffer();

		for (String avalue : valueList) {
			if (avalue != null && !avalue.equals("")) {
				result.append("<value>" + avalue + "</value>");
			}
		}
		return result.toString();
	}

}
