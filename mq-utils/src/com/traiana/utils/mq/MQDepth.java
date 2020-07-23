//Originally written by ???? 
//Updated 01/07/13 by Jim Johnson jajohnso@gmail.com

package com.traiana.utils.mq;

import com.ibm.mq.MQException;

// Referenced classes of package com.traiana.utils.mq:
//            MQ

public class MQDepth
{

    public MQDepth()
    {
    }

    public static void main(String args[])
    {
        if(args.length < 5)
        {
            System.out.println("usage: java com.traiana.utils.mq.MQDepth [qmgr] [port] [mqServerName] [channelName] [queueName] {-w [warning threshold]} {-c [critical threshold]} {-debug}");
            System.exit(3);
        }
        MQ sender = null;
        try
        {
            int warning = 0x7fffffff;
            int critical = 0x7fffffff;
            boolean debug = false;
            boolean log = false;
            for(int i = 4; i < args.length; i++)
            {
                String arg = args[i];
                if(arg.equals("-w"))
                {
                    warning = Integer.parseInt(args[i + 1]);
                    continue;
                }
                if(arg.equals("-c"))
                {
                    critical = Integer.parseInt(args[i + 1]);
                    continue;
                }
                if(arg.equals("-debug"))
                {
                    debug = true;
                    log = true;
                }
            }
        
            sender = new MQ(args[0], Integer.parseInt(args[1]), args[2], args[3], 0, log);
            sender.InitQueue(args[4], true);
            int depth = sender.getDepth();
            if(depth < warning)
                {
                    System.out.println("OK: Depth=" + depth + "| depth=" + depth + ";" + warning + ";" + critical);
                    sender.close();
                    System.exit(0);
                } else
            if(depth >= critical)
                {
                    System.out.println("CRITICAL: Depth=" + depth + "| depth=" + depth + ";" + warning + ";" + critical);
                    sender.close();
                    System.exit(2);
                } else
                {
                    System.out.println("WARNING: Depth=" + depth + "| depth=" + depth + ";" + warning + ";" + critical);
                    sender.close();
                    System.exit(1);
                }
    
            if(sender != null)
                {
                    try
                {
                    sender.close();
                }
                    catch(Exception e)
                {
                    e.printStackTrace();
                }
                }
        }     
        catch(MQException ex)
            {
            //System.out.println("\nAn MQ Error Occurred :(" + "\nCompletion Code is : " + ex.completionCode + "\nThe Reason Code is :\t" + ex.reasonCode);
            System.out.println("\nAn MQ Error Occurred. :(" + "\nCompletion Code:" + ex.completionCode + "\nReason Code:\t" + ex.reasonCode + "\n");
            // e.printStackTrace();
            }
        finally
            {
            if(sender != null)
                {
                    try
                    {
                        sender.close();
                    }
                    catch(MQException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
    }
}