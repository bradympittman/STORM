package gov.usgs.swarm.data;

import gov.usgs.earthworm.Menu;
import gov.usgs.earthworm.MenuItem;
import gov.usgs.swarm.Swarm;
import gov.usgs.util.CurrentTime;
import gov.usgs.util.Util;
import gov.usgs.vdx.data.heli.HelicorderData;
import gov.usgs.vdx.data.wave.Wave;
import gov.usgs.winston.server.WWSClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * An implementation of <code>SeismicDataSource</code> that communicates
 * with a WinstonWaveServer.  This is essentially just a copy of 
 * WaveServerSource with different helicorder functions.  It should probably 
 * be made a descendant of WaveServerSource. 
 * 
 * $Log: not supported by cvs2svn $
 * Revision 1.1  2005/08/26 20:40:28  dcervelli
 * Initial avosouth commit.
 *
 * Revision 1.2  2005/05/08 16:10:48  cervelli
 * Changes for renaming of WWS.
 *
 * Revision 1.1  2005/05/02 16:22:10  cervelli
 * Moved data classes to separate package.
 *
 * Revision 1.5  2005/04/23 15:54:25  cervelli
 * Uses space delimiting instead of underscore.  Handles -- locations better.
 *
 * Revision 1.4  2005/04/16 20:57:30  cervelli
 * SCNL changes.
 *
 * Revision 1.3  2005/04/14 15:37:16  cervelli
 * Doesn't put the latest 30 seconds into the helicorder cache.
 *
 * Revision 1.2  2005/03/25 00:50:11  cervelli
 * Support for enabling/disabling compression.
 *
 * Revision 1.1  2005/03/24 22:06:41  cervelli
 * Initial commit.
 *
 * @author Dan Cervelli
 */
public class WWSSource extends SeismicDataSource
{
	private String params;
	private WWSClient winstonClient;
	private int timeout = 2000;
	private boolean compress = false;
	
	public WWSSource(String s)
	{
		params = s;
//		String[] ss = Util.splitString(params, ":");
		String[] ss = params.split(":");
		String h = ss[0];
		int p = Integer.parseInt(ss[1]);
		timeout = Integer.parseInt(ss[2]);
		compress = ss[3].equals("1");
		
		winstonClient = new WWSClient(h, p);
		setTimeout(timeout);
	}
	
	public WWSSource(WWSSource wss)
	{
		this(wss.params);
		//server = wss.server;
		//waveServer = new WaveServer(server);
	}
 
	public SeismicDataSource getCopy()
	{
		return new WWSSource(this);	
	}
	
	public synchronized void setTimeout(int to)
	{
		winstonClient.setTimeout(to);
	}
	
	public synchronized void close()
	{
		if (winstonClient != null)
			winstonClient.close();
	}
	
	public String getFormattedSCNL(MenuItem mi)
	{
		String scnl = mi.getStation() + " " + mi.getChannel() + " " + mi.getNetwork();
		String loc = mi.getLocation();
		if (loc != null && !loc.equals("--"))
			scnl = scnl + " " + loc;
		return scnl;
	}
	
	public List<String> getMenuList(List items)
	{
		List<String> list = new ArrayList<String>(items.size());
		for (Iterator it = items.iterator(); it.hasNext(); )
		{
			MenuItem mi = (MenuItem)it.next();
			list.add(getFormattedSCNL(mi));
		}
		return list;
	}
	
	public synchronized List<String> getWaveStations()
	{
		Menu menu = winstonClient.getMenuSCNL();
		return getMenuList(menu.getSortedItems());
	}
	
	public String[] parseSCNL(String channel)
	{
		String[] result = new String[4];
		StringTokenizer st = new StringTokenizer(channel, " ");
		result[0] = st.nextToken();
		result[1] = st.nextToken();
		result[2] = st.nextToken();
		if (st.hasMoreTokens())
			result[3] = st.nextToken();
		else
			result[3] = "--";
		return result;
	}
	
	public synchronized Wave getWave(String station, double t1, double t2)
	{
		CachedDataSource cache = Swarm.getCache();
		Wave sw = cache.getWave(station, t1, t2);
		if (sw == null)
		{
			String[] scnl = parseSCNL(station);
			sw = winstonClient.getRawData(scnl[0], scnl[1], scnl[2], scnl[3], Util.j2KToEW(t1), Util.j2KToEW(t2));
			if (sw == null)
				return null;
			sw.convertToJ2K();
			sw.register();
			cache.putWave(station, sw);
		}
		else
		{
			//System.out.println("cached");	
		}
		return sw;
	}
	
	public synchronized HelicorderData getHelicorder(String station, double t1, double t2)
	{
		CachedDataSource cache = Swarm.getCache();
		HelicorderData hd = cache.getHelicorder(station, t1, t2, this);
		if (hd == null)
		{
			String[] scnl = parseSCNL(station);
			hd = winstonClient.getHelicorder(scnl[0], scnl[1], scnl[2], scnl[3], t1, t2, compress);
			
			if (hd != null && hd.rows() != 0)
			{
				HelicorderData noLatest = hd.subset(hd.getStartTime(), CurrentTime.getInstance().nowJ2K() - 30);
				if (noLatest != null && noLatest.rows() > 0)
					cache.putHelicorder(station, noLatest);
				//cache.putHelicorder(station, hd);
			}
			else
				hd = null;
		}
		return hd;
	}
	
	public synchronized List<String> getHelicorderStations()
	{
		Menu menu = winstonClient.getMenuSCNL();
		return getMenuList(menu.getSortedItems());
	}
	
	public synchronized void notifyDataNotNeeded(String station, double t1, double t2)
	{
		
	}
	
	public synchronized boolean isActiveSource()
	{
		return true;	
	}
}
