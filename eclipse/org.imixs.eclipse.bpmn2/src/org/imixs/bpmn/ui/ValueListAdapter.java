package org.imixs.bpmn.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This adapter manages a Option List. Each element of the option list can have
 * the boolean value true/false.
 * 
 * This results into a String with <value> elements to be used by UI Widget
 * CheckBox.
 * 
 * 
 * 
 * 1|2|3
 * 
 * results in
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

	Map<String, Boolean> map;
	List<String> optionList;

	/**
	 * converts the optionList into a hash map. For each entry contained in the
	 * valueList the map entry will be set to true.
	 * 
	 * 
	 * @param valueList
	 */
	public ValueListAdapter(List<String> aoptionList, String valueList) {

		// get List with all current values from valueList
		optionList = aoptionList;
		List<String> values = getList(valueList);

		// first create a map for each element of the option list
		map = new HashMap<String, Boolean>();
		for (String aOption : optionList) {

			// if option has label (label|value) cut
			if (aOption.indexOf('|') > -1) {
				aOption = aOption.substring(aOption.indexOf('|') + 1).trim();
			}
			map.put(aOption, values.indexOf(aOption) > -1);
		}

	}

	/**
	 * returns the state of a option element
	 * 
	 * @param aOption
	 * @return
	 */
	public boolean isSelected(String aOption) {
		return map.get(aOption);
	}

	/**
	 * updates the state of a option element
	 * 
	 * @param aOption
	 * @return
	 */
	public void setSelection(String aOption, boolean selected) {
		map.put(aOption, selected);
	}

	public List<String> getOptionList() {
		return optionList;
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

		for (String aOption : optionList) {
			// if option has label (label|value) cut
			if (aOption.indexOf('|') > -1) {
				aOption = aOption.substring(aOption.indexOf('|') + 1).trim();
			}
			if (isSelected(aOption)) {
				result += "<value>" + aOption + "</value>";
			}
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
