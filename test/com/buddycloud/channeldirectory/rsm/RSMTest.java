package com.buddycloud.channeldirectory.rsm;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.buddycloud.channeldirectory.handler.QueryResponseObject;

public class RSMTest {

	private static final int TEST_DATA_SIZE = 20;
	
	@Test
	public void testNoRSMInfo() {
		RSM rsm = new RSM();
		List<QueryResponseObject> response = RSMUtils.filterRSMResponse(
				createTestData(), rsm);
		
		Assert.assertEquals(TEST_DATA_SIZE, response.size());
		Assert.assertEquals(0, rsm.getIndex().intValue());
		Assert.assertEquals(TEST_DATA_SIZE, rsm.getCount().intValue());
		Assert.assertEquals("jid0", rsm.getFirst());
		Assert.assertEquals("jid19", rsm.getLast());
	}

	@Test
	public void testLimitToResultSet() {
		RSM rsm = new RSM();
		rsm.setMax(10);
		List<QueryResponseObject> response = RSMUtils.filterRSMResponse(
				createTestData(), rsm);
		
		Assert.assertEquals(10, response.size());
		Assert.assertEquals(0, rsm.getIndex().intValue());
		Assert.assertEquals(TEST_DATA_SIZE, rsm.getCount().intValue());
		Assert.assertEquals("jid0", rsm.getFirst());
		Assert.assertEquals("jid9", rsm.getLast());
	}
	
	@Test
	public void testPaging() {
		RSM rsm = new RSM();
		rsm.setMax(10);
		rsm.setAfter("jid9");
		List<QueryResponseObject> response = RSMUtils.filterRSMResponse(
				createTestData(), rsm);
		
		Assert.assertEquals(10, response.size());
		Assert.assertEquals(10, rsm.getIndex().intValue());
		Assert.assertEquals(TEST_DATA_SIZE, rsm.getCount().intValue());
		Assert.assertEquals("jid10", rsm.getFirst());
		Assert.assertEquals("jid19", rsm.getLast());
	}
	
	@Test
	public void testBlankPage() {
		RSM rsm = new RSM();
		rsm.setMax(10);
		rsm.setAfter("jid19");
		List<QueryResponseObject> response = RSMUtils.filterRSMResponse(
				createTestData(), rsm);
		
		Assert.assertEquals(0, response.size());
		Assert.assertEquals(TEST_DATA_SIZE, rsm.getCount().intValue());
		Assert.assertNull(rsm.getFirst());
		Assert.assertNull(rsm.getLast());
	}
	
	@Test
	public void testPagingBackwards() {
		RSM rsm = new RSM();
		rsm.setMax(10);
		rsm.setBefore("jid15");
		List<QueryResponseObject> response = RSMUtils.filterRSMResponse(
				createTestData(), rsm);
		
		Assert.assertEquals(10, response.size());
		Assert.assertEquals(5, rsm.getIndex().intValue());
		Assert.assertEquals(TEST_DATA_SIZE, rsm.getCount().intValue());
		Assert.assertEquals("jid5", rsm.getFirst());
		Assert.assertEquals("jid14", rsm.getLast());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPageNotFound() {
		RSM rsm = new RSM();
		rsm.setMax(10);
		rsm.setAfter("jid20");
		RSMUtils.filterRSMResponse(createTestData(), rsm);
	}
	
	@Test
	public void testLastPage() {
		RSM rsm = new RSM();
		rsm.setMax(10);
		rsm.setBefore("");
		List<QueryResponseObject> response = RSMUtils.filterRSMResponse(
				createTestData(), rsm);
		
		Assert.assertEquals(10, response.size());
		Assert.assertEquals(10, rsm.getIndex().intValue());
		Assert.assertEquals(TEST_DATA_SIZE, rsm.getCount().intValue());
		Assert.assertEquals("jid10", rsm.getFirst());
		Assert.assertEquals("jid19", rsm.getLast());
	}
	
	@Test
	public void testPageOutOfOrder() {
		RSM rsm = new RSM();
		rsm.setMax(10);
		rsm.setIndex(5);
		List<QueryResponseObject> response = RSMUtils.filterRSMResponse(
				createTestData(), rsm);
		
		Assert.assertEquals(10, response.size());
		Assert.assertEquals(5, rsm.getIndex().intValue());
		Assert.assertEquals(TEST_DATA_SIZE, rsm.getCount().intValue());
		Assert.assertEquals("jid5", rsm.getFirst());
		Assert.assertEquals("jid14", rsm.getLast());
	}
	
	@Test
	public void testGettingItemCount() {
		RSM rsm = new RSM();
		rsm.setMax(0);
		List<QueryResponseObject> response = RSMUtils.filterRSMResponse(
				createTestData(), rsm);
		
		Assert.assertEquals(0, response.size());
		Assert.assertEquals(TEST_DATA_SIZE, rsm.getCount().intValue());
		Assert.assertEquals(null, rsm.getFirst());
		Assert.assertEquals(null, rsm.getLast());
	}
	
	private static List<QueryResponseObject> createTestData() {
		List<QueryResponseObject> objects = new LinkedList<QueryResponseObject>();
		for (int i = 0; i < TEST_DATA_SIZE; i++) {
			QueryResponseObject object = new QueryResponseObject();
			object.setJid("jid" + i);
			objects.add(object);
		}
		
		return objects;
	}
	
}
