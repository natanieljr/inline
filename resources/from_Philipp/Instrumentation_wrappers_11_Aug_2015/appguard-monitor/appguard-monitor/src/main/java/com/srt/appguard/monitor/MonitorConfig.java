package com.srt.appguard.monitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.srt.appguard.monitor.policy.PolicyConfig;

public class MonitorConfig {
	
	public static void init(InputStream in, OutputStream out) throws Exception {
		MonitorConfig config = new MonitorConfig();
		config.read(in);
		config.write(out);
		
		in.close();
		out.close();
	}

	private final Map<String, PolicyConfig> policyConfigs;
	private long latestReadVersion;
	
	public MonitorConfig() {
		policyConfigs = new HashMap<String, PolicyConfig>();
		latestReadVersion = -1;
	}
	
	public void read(final File file) throws Exception {
		latestReadVersion = file.lastModified();
		policyConfigs.clear();
		
//		final InputStream is = new FileInputStream(file);
//		byte[] buf = new byte[1024];
//		int read = 0;
//		System.out.println("=========================================================================");
//		while ((read = is.read(buf)) > 0) {
//			System.out.write(buf, 0, read);
//		}
//		is.close();
//		System.out.println("\n=========================================================================");
		
		// parse the given xml file
		final DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		final Document xml = db.parse(file);
		read(xml);
	}
	
	private void read(final InputStream is) throws Exception {
		// parse the given xml stream
		final DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		final Document xml = db.parse(is);
		read(xml);
	}
	
	private void read(final Document xml) throws Exception {
		// load individual policy configs
		final NodeList policies = xml.getElementsByTagName("permission");
		final int numPolicies = policies.getLength();
		for (int i = 0; i < numPolicies; ++i) {
			// get id
			final Node node = policies.item(i);
			final NamedNodeMap attribs = node.getAttributes();
			if (attribs == null) {
				continue;
			}
			final Node idAttrib = attribs.getNamedItem("id");
			if (idAttrib == null) {
				continue;
			}
			final String id = idAttrib.getNodeValue();

			// load the policy config
			final PolicyConfig config = new PolicyConfig();
			config.read(node);
			policyConfigs.put(id, config);
		}
	}
	
	public void write(final File file) throws Exception {
		final OutputStream os = new FileOutputStream(file);
		write(os);
		os.close();
	}

	public void write(final OutputStream os) throws Exception {
		// create empty xml document
		final DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		final Document xml = db.newDocument();
		
		// add the root node
		final Node root = xml.createElement("monitor");
		final NamedNodeMap rootAttributes = root.getAttributes();
		final Node monitorIdAttribute = xml.createAttribute("id");
		monitorIdAttribute.setNodeValue("monitor");
		rootAttributes.setNamedItem(monitorIdAttribute);
		xml.appendChild(root);


		// add a policy node for every policy
		for (final Entry<String, PolicyConfig> entry : policyConfigs.entrySet()) {
			final Node node = xml.createElement("permission");
			root.appendChild(node);

			// set id
			final NamedNodeMap attributes = node.getAttributes();
			final Node idAttrib = xml.createAttribute("id");
			idAttrib.setNodeValue(entry.getKey());
			attributes.setNamedItem(idAttrib);
			
			// write the policy config
			final PolicyConfig config = entry.getValue();
			config.write(node);
		}
		
		// write xml to file
		final Source in = new DOMSource(xml);
		final StreamResult out = new StreamResult(os);
		final Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.transform(in, out);
	}

	public Map<String, PolicyConfig> getPolicyConfigs() {
		return policyConfigs;
	}
	
	public PolicyConfig getPolicyConfig(final String identifier) {
		return policyConfigs.get(identifier);
	}
	
	public void putPolicyConfig(final String identifier, final PolicyConfig config) {
		policyConfigs.put(identifier, config);
	}

	
	public void setPermissionStatus(String permissionName, boolean enabled) {
		PolicyConfig policyConfig = this.getPolicyConfig(permissionName);
		if (policyConfig == null) policyConfig = new PolicyConfig();

		policyConfig.put("enabled", Boolean.toString(enabled));
		this.putPolicyConfig(permissionName, policyConfig);
	}
	
	public boolean getPermissionStatus(String permissionName, boolean defaultValue) {
		PolicyConfig policyConfig = this.getPolicyConfig(permissionName);
		if (policyConfig == null) {
			return defaultValue;
		}

		String value = policyConfig.get("enabled", String.class, null);
		if(value == null) {
			return defaultValue;
		}
		return Boolean.parseBoolean(value);
	}

	public <T> void setPermissionMetaInfo(String permissionName, String fieldName, T metaInfo) {
		PolicyConfig policyConfig = this.getPolicyConfig(permissionName);
		if (policyConfig == null) policyConfig = new PolicyConfig();

		policyConfig.put(fieldName, metaInfo);
		this.putPolicyConfig(permissionName, policyConfig);
	}

	public <T> T getPermissionMetaInfo(String permissionName, Class<T> type, String fieldName) {
		PolicyConfig policyConfig = this.getPolicyConfig(permissionName);
		if (policyConfig == null) return null;

		return policyConfig.get(fieldName, type, null);
	}

	public long getLatestReadVersion() {
		return latestReadVersion;
	}
}
