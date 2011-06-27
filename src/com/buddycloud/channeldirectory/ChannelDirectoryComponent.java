package com.buddycloud.channeldirectory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.xmpp.component.AbstractComponent;
import org.xmpp.packet.IQ;

import com.buddycloud.channeldirectory.handler.QueryHandler;
import com.buddycloud.channeldirectory.handler.content.ContentQueryHandler;
import com.buddycloud.channeldirectory.handler.metadata.MetadataQueryHandler;
import com.buddycloud.channeldirectory.handler.nearby.NearbyQueryHandler;
import com.buddycloud.channeldirectory.handler.recommendation.ChannelRecommender;
import com.buddycloud.channeldirectory.handler.recommendation.RecommendationQueryHandler;
import com.buddycloud.channeldirectory.handler.similarity.SimilarityQueryHandler;
import com.buddycloud.channeldirectory.rsm.RSM;
import com.buddycloud.channeldirectory.utils.XMPPUtils;

/**
 * Channel Directory XMPP Component
 * Follows the XEP-0114 (http://xmpp.org/extensions/xep-0114.html)
 * 
 */
public class ChannelDirectoryComponent extends AbstractComponent {

	private static final String DESCRIPTION = "A pub sub search engine, " +
			"metadata crawler and recommendation service";
	private static final String NAME = "Channel Directory";
	private static final Logger LOGGER = Logger.getLogger(ChannelDirectoryComponent.class);
	
	private final Map<String, QueryHandler> queryHandlers = new HashMap<String, QueryHandler>();
	private final Properties properties;

	public ChannelDirectoryComponent(Properties properties) {
		this.properties = properties;
	}
	
	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public void postComponentStart() {
		createHandlers();
	}

	@Override
	protected IQ handleIQGet(IQ iq) throws Exception {
		Element queryElement = iq.getElement().element("query");
		if (queryElement == null) {
			return XMPPUtils.error(iq, "IQ does not contain query element.", 
					LOGGER);
		}
		
		Namespace namespace = queryElement.getNamespace();
		
		QueryHandler queryHandler = queryHandlers.get(namespace.getURI());
		if (queryHandler == null) {
			return XMPPUtils.error(iq, "QueryHandler not found for namespace: " + namespace, 
					LOGGER);
		}
		
		return queryHandler.handle(iq);
	}
	
	@Override 
	protected String[] discoInfoFeatureNamespaces() {
		ArrayList<String> namespaces = new ArrayList<String>(queryHandlers.keySet());
		namespaces.add(RSM.NAMESPACE);
		return (namespaces.toArray(new String[]{}));
	}

	@Override 
	protected String discoInfoIdentityCategory() {
		return ("Search");
	}

	@Override 
	protected String discoInfoIdentityCategoryType() {
		return ("Directory");
	}

	private void createHandlers() {
		addHandler(new NearbyQueryHandler(properties));
		addHandler(new MetadataQueryHandler(properties));
		addHandler(new ContentQueryHandler(properties));
		
		ChannelRecommender recommender = createRecommender();
		addHandler(new RecommendationQueryHandler(properties, recommender));
		addHandler(new SimilarityQueryHandler(properties, recommender));
	}
	
	private ChannelRecommender createRecommender() {
		try {
			return new ChannelRecommender();
		} catch (TasteException e) {
			throw new RuntimeException(e);
		}
	}

	private void addHandler(QueryHandler queryHandler) {
		queryHandlers.put(queryHandler.getNamespace(), queryHandler);
	}
}
