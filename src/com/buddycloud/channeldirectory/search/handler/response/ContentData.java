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
package com.buddycloud.channeldirectory.search.handler.response;

import com.buddycloud.channeldirectory.search.rsm.RSM;

/**
 * Content query response in RSMable format.
 * Must have an identifier for indexing and paging purposes. 
 * 
 * @see RSM
 */
public class ContentData {

	private String id;
	private String type;
	
	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
