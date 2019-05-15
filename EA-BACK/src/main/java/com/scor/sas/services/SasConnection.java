package com.scor.sas.services;


import com.sas.iom.SAS.IWorkspace;
import com.scor.sas.entities.SasConnectionWrapper;
import com.scor.sas.exception.SasException;
import com.sas.iom.WorkspaceFactory;
import com.sas.iom.WorkspaceFactoryException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Properties;

@Component
public class SasConnection implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5451508414594428680L;
    private static final Logger LOGGER = Logger.getLogger(SasConnection.class);

    @Value("${sas-server-name}")
    private String serverName;
    @Value("${sas-port}")
    private String serverPort;
    @Value("${sas-user}")
    private  String serverUser;
    @Value("${sas-pass}")
    private String serverPass;
    @Value("${sas-domain}")
    private String sas_domain;

    public SasConnection(){}

    public SasConnectionWrapper getConnectionWrapperByWorkspaceFactory() throws SasException {
        try {
            final long td = System.currentTimeMillis();
            final Properties iomServerProperties = new Properties();
            iomServerProperties.put("host", serverName);
            iomServerProperties.put("port", serverPort);
            iomServerProperties.put("userName", sas_domain+"\\"+serverUser);
            iomServerProperties.put("password", serverPass);

//			Properties[] serverList = { iomServerProperties };

            // Connect to the Workspace
            LOGGER.info("Connecting to SAS Server");
            final WorkspaceFactory wFactory = new WorkspaceFactory();
            final IWorkspace iWorkspace = wFactory.createWorkspaceByServer(iomServerProperties);
            if (iWorkspace == null) {
                LOGGER.error("Error Connecting to SAS");
                return null;
            }
            LOGGER.info("SAS Connection Established in "+(System.currentTimeMillis()-td));

            final SasConnectionWrapper cw = new SasConnectionWrapper(serverName, iWorkspace);
            cw.setWorkspaceFactory(wFactory);
            return cw;
        } catch (WorkspaceFactoryException e) {
            throw new SasException(e);
        }
    }

}
