package org.imixs.pbmn;

import java.util.ArrayList;
import java.util.List;

import org.imixs.bpmn.ui.ValueListAdapter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This JUnit Test test the behavior of the ValueListAdapter
 * 
 * @author rsoika
 *
 */
public class ListAdapterTest {

	String value = "";
	List<String> options;

	@Before
	public void setup() {
		value = "<value>1</value>  <value>2</value><value>3</value>";

		options = new ArrayList<String>();
		options.add("1");
		options.add("Test | 2 ");
		options.add("3");
		options.add("4");
		options.add("5");
	}

	@Test
	public void test1() {

		List<String> ding = ValueListAdapter.getList(value);
		Assert.assertEquals(3, ding.size());

		String reverse = ValueListAdapter.putList(ding);
		Assert.assertEquals("<value>1</value><value>2</value><value>3</value>",
				reverse);

	}

	@Test
	public void test2() {

		ValueListAdapter adapter = new ValueListAdapter(options, value);

		Assert.assertTrue(adapter.isSelected("1"));
		Assert.assertTrue(adapter.isSelected("2"));
		Assert.assertFalse(adapter.isSelected("4"));

	}

	@Test
	public void test3() {

		ValueListAdapter adapter = new ValueListAdapter(options, value);

		adapter.setSelection("1", false);
		adapter.setSelection("5", true);

		Assert.assertEquals("<value>2</value><value>3</value><value>5</value>",
				adapter.getValue());

	}

}
