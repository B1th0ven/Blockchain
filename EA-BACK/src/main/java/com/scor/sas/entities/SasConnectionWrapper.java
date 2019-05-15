package com.scor.sas.entities;

import com.sas.iom.SAS.IWorkspace;
import com.sas.iom.WorkspaceConnector;
import com.sas.iom.WorkspaceFactory;
import com.sas.iom.WorkspaceFactoryException;
import com.sas.rio.MVAConnection;
import com.sas.services.connection.ConnectionFactoryInterface;
import com.sas.metadata.remote.MdFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Properties;

public class SasConnectionWrapper  implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(SasConnectionWrapper.class);

    private String serverName;
    private MdFactory mdFactory;
    private ConnectionFactoryInterface connectionFactoryInterface;
    private WorkspaceFactory workspaceFactory;
    private WorkspaceConnector workspaceConnector;

    private IWorkspace iworkspace;

    public SasConnectionWrapper(String serverName, IWorkspace iworkspace) {
        this.serverName = serverName;
        this.iworkspace = iworkspace;
    }

    public String getServerName() {
        return serverName;
    }
    public IWorkspace getIWorkspace() {
        return iworkspace;
    }


    public MdFactory getFactory() {
        return mdFactory;
    }
    public void setFactory(MdFactory mdFactory) {
        this.mdFactory = mdFactory;
    }

    public ConnectionFactoryInterface getConnectionFactoryInterface() {
        return connectionFactoryInterface;
    }
    public void setConnectionFactoryInterface(ConnectionFactoryInterface connectionFactoryInterface) {
        this.connectionFactoryInterface = connectionFactoryInterface;
    }


    public WorkspaceFactory getWorkspaceFactory() {
        return workspaceFactory;
    }
    public void setWorkspaceFactory(WorkspaceFactory workspaceFactory) {
        this.workspaceFactory = workspaceFactory;
    }
    public WorkspaceConnector getWorkspaceConnector() {
        return workspaceConnector;
    }
    public void setWorkspaceConnector(WorkspaceConnector workspaceConnector) {
        this.workspaceConnector = workspaceConnector;
    }


    /**
     * Use this getter to operate in JDBC form.
     * Example (from http://www.lexjansen.com/wuss/2004/sas_solutions/c_ss_using_sas_iom_with_a_ja.pdf)
     * 	Connection sqlConnection = cw.getSqlConnection();
     * 	Statement stat = sqlConnection.createStatement();
     * 	ResultSet rs_Input = stat.executeQuery("Select * from sasname where oracle_name like 'ECG%'");
     * 	while ( rs_Input.next() ) {
     * 		System.out.println(rs_Input.getString("ORACLE_NAME"));
     * 	}
     * 	rs_Input.close();
     *
     * @return
     */
    public java.sql.Connection getSqlConnnection() {
        try {
            return new MVAConnection(iworkspace.DataService(),new Properties());
        } catch (SQLException e) {
            return null;
        }
    }

    public void close() {
        if ( mdFactory != null ) {
            try {
                mdFactory.getConnection().closeIOMConnection(iworkspace);
            } catch (Exception e) {
               // LogTools.warn(LOG, "Error while factory.getConnection().closeIOMConnection(iworkspace)", e);
            }
        }

        if ( iworkspace != null ) {
            try {
                iworkspace.Close();
                iworkspace = null;
            } catch (Exception e) {
               // LogTools.warn(LOG, "Error while iworkspace.Close()", e);
            }
        }

        if ( workspaceFactory != null ) {
            try {
                workspaceFactory.shutdown();
                workspaceFactory = null;
            } catch (WorkspaceFactoryException e) {
              //  LogTools.warn(LOG, "Error while workspaceFactory.shutdow()", e);
            }
        }

        if ( workspaceConnector != null ) {
            workspaceConnector.close();
            workspaceConnector = null;
        }

        // tell the factory that it can destroy unused connections
        if ( connectionFactoryInterface != null ) {
            try {
                connectionFactoryInterface.getAdminInterface().shutdown();
            } catch (Exception e) {
               // LogTools.warn(LOG, "Error while connectionFactoryInterface.getAdminInterface()", e);
            }
        }
    }
}
