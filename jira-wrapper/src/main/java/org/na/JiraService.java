package org.na;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import com.google.common.collect.Lists;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.lib.uno.helper.WeakBase;
import javax.swing.JOptionPane;

/**
 */
public class JiraService extends WeakBase implements com.sun.star.lang.XInitialization,
		com.sun.star.frame.XDispatch, com.sun.star.lang.XServiceInfo,
		com.sun.star.frame.XDispatchProvider {
	
	private final XComponentContext context;
	private com.sun.star.frame.XFrame frame;
	private static final String m_implementationName = JiraService.class.getName();
	private static final String[] m_serviceNames = { "com.sun.star.frame.ProtocolHandler" };
	private AddonLogic logic;
	
	public JiraService(XComponentContext context) {
		this.context = context;
		try {
			logic = new AddonLogic(context);
		} catch (Exception e) {
			logic = null;
			StringWriter stringWriter = new StringWriter();
			PrintWriter writer = new PrintWriter(stringWriter);
			e.printStackTrace(writer);
			StringBuffer buffer = stringWriter.getBuffer();
			String msg = e.getMessage() + " \n  " + buffer.toString();
			JOptionPane.showMessageDialog(null, msg, e.getClass().getName(),
					JOptionPane.ERROR_MESSAGE);
		}
	};
	
	public static XSingleComponentFactory __getComponentFactory(String sImplementationName) {
		XSingleComponentFactory xFactory = null;
		
		if (sImplementationName.equals(m_implementationName)) {
			xFactory = Factory.createComponentFactory(JiraService.class, m_serviceNames);
		}
		return xFactory;
	}
	
	public static boolean __writeRegistryServiceInfo(XRegistryKey xRegistryKey) {
		return Factory.writeRegistryServiceInfo(m_implementationName, m_serviceNames, xRegistryKey);
	}
	
	// com.sun.star.lang.XInitialization:
	public void initialize(Object[] object) throws com.sun.star.uno.Exception {
		if (object.length > 0) {
			frame = (com.sun.star.frame.XFrame) UnoRuntime.queryInterface(
					com.sun.star.frame.XFrame.class, object[0]);
		}
	}
	
	// com.sun.star.frame.XDispatch:
	public void dispatch(com.sun.star.util.URL aURL, com.sun.star.beans.PropertyValue[] aArguments) {
		if (aURL.Protocol.compareTo("org.na.jiraservice:") == 0) {
			if (aURL.Path.compareTo("Command0") == 0) {
				try {
					logic.fetchDataFromJira();
				} catch (Throwable t) {
					
					StringWriter stringWriter = new StringWriter();
					PrintWriter writer = new PrintWriter(stringWriter);
					t.printStackTrace(writer);
					StringBuffer buffer = stringWriter.getBuffer();
					String msg = t.getMessage() + " \n  " + buffer.toString();
					JOptionPane.showMessageDialog(null, msg, t.getClass().getName(),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	public void addStatusListener(com.sun.star.frame.XStatusListener xControl,
			com.sun.star.util.URL aURL) {
		// add your own code here
	}
	
	public void removeStatusListener(com.sun.star.frame.XStatusListener xControl,
			com.sun.star.util.URL aURL) {
		// add your own code here
	}
	
	// com.sun.star.lang.XServiceInfo:
	public String getImplementationName() {
		return m_implementationName;
	}
	
	public boolean supportsService(String sService) {
		int len = m_serviceNames.length;
		
		for (int i = 0; i < len; i++) {
			if (sService.equals(m_serviceNames[i])) {
				return true;
			}
		}
		return false;
	}
	
	public String[] getSupportedServiceNames() {
		return m_serviceNames;
	}
	
	// com.sun.star.frame.XDispatchProvider:
	public com.sun.star.frame.XDispatch queryDispatch(com.sun.star.util.URL aURL,
			String sTargetFrameName, int iSearchFlags) {
		if (aURL.Protocol.compareTo("org.na.jiraservice:") == 0) {
			if (aURL.Path.compareTo("Command0") == 0) {
				return this;
			}
		}
		return null;
	}
	
	// com.sun.star.frame.XDispatchProvider:
	public com.sun.star.frame.XDispatch[] queryDispatches(
			com.sun.star.frame.DispatchDescriptor[] seqDescriptors) {
		int nCount = seqDescriptors.length;
		com.sun.star.frame.XDispatch[] seqDispatcher = new com.sun.star.frame.XDispatch[seqDescriptors.length];
		
		for (int i = 0; i < nCount; ++i) {
			seqDispatcher[i] = queryDispatch(seqDescriptors[i].FeatureURL,
					seqDescriptors[i].FrameName, seqDescriptors[i].SearchFlags);
		}
		return seqDispatcher;
	}
}