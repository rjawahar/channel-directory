/*
 * Copyright 2011 buddycloud
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.buddycloud.channeldirectory.cli.query;

import java.util.Properties;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.buddycloud.channeldirectory.commons.solr.SolrServerFactory;

/**
 * @author Abmar
 *
 */
public class PostCountQuery implements Query {
	
	public static final String NAME = "post-count";
	
	@Override
	public String exec(String args, Properties configuration) throws Exception {
		SolrServer solrServer = SolrServerFactory.createPostCore(configuration);
		SolrQuery solrQuery = new SolrQuery("content:[* TO *]");
		
		QueryResponse queryResponse = solrServer.query(solrQuery);
		Long numFound = queryResponse.getResults().getNumFound();
		
		return numFound.toString();
	}

}
