package org.imixs.bpmn.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class OptionListAdapter extends ValueListAdapter {

	Map<String, Boolean> map;
	List<String> optionList;

	/**
	 * converts the optionList into a hash map. For each entry contained in the
	 * valueList the map entry will be set to true.
	 * 
	 * 
	 * @param valueList
	 */
	public OptionListAdapter(List<String> aoptionList, String valueList) {
		super();
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


	
}
