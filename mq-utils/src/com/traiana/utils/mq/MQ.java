//Originally written by ???? 
//Updated 01/07/13 by Jim Johnson jajohnso@gmail.com

package com.traiana.utils.mq;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;   
import java.io.OutputStreamWriter;

// Referenced classes of package com.traiana.utils.mq:
//            NullOutputStream

public class MQ
{

    public MQ(String qmgr, int port, String mqServerName, String channelName, int ccsid, boolean log)
        throws MQException
    {
        MQEnvironment.hostname = mqServerName;
        MQEnvironment.port = port;
        MQEnvironment.channel = channelName;
        MQEnvironment.properties.put("MQC.TRANSPORT_PROPERTY", "MQC.TRANSPORT_MQSERIES_CLIENT");
        if(log)
        {
            MQException.log = new OutputStreamWriter(System.out);
        }
        else
        {
            MQException.log = new OutputStreamWriter(new NullOutputStream());
        }
        if(ccsid > 0)
        {
                MQEnvironment.properties.put("CCSID", new Integer(ccsid));
        }
            try
            {
                _qMgr = new MQQueueManager(qmgr);
            }
            catch(MQException ex)
            {
                System.out.println("\nAn MQ Error Occurred :(" + "\nCompletion Code is : " + ex.completionCode + "\nThe Reason Code is :\t" + ex.reasonCode);
            }
    }

    public void InitQueue(String queueName, boolean depth)
        throws MQException
    {
        int openOptions = CMQC.MQOO_INQUIRE + CMQC.MQOO_FAIL_IF_QUIESCING + CMQC.MQOO_INPUT_SHARED;
        if(_mQQueue != null)
        {
            _mQQueue.close();
            _mQQueue = null;
        }
        _mQQueue = _qMgr.accessQueue(queueName, openOptions);
    }

    public int getDepth()
        throws MQException
    {
        return _mQQueue.getCurrentDepth();
    }

    public void close()
        throws MQException
    {
        if(_mQQueue != null)
        {
            _mQQueue.close();
            _qMgr.close();
            _qMgr.disconnect();
        }
    }

    MQQueueManager _qMgr;
    MQQueue _mQQueue;
}