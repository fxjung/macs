/** 
 * MACS - Multi-Agent Cooperative Search is a framework to develop cooperating agents using 
 * different Metaheuristics Copyright (C) 2016 Angel Alejandro Juan Perez. This file is part of MACS. 
 * 
 * MACS is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * 
 * MACS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with MACS. 
 * If not, see <http://www.gnu.org/licenses/>.
 */
package macs.uoc.cvrp;
/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class ElapsedTime
{
    public ElapsedTime(){}

    public static long systemTime()
    {   long time = System.nanoTime();
        return time;
    }

    public static double calcElapsed(long start, long end) 
    {   double elapsed = (end - start) / 1.0e+9;
        return elapsed;
    }

    public static String calcElapsedHMS(long start, long end) 
    {   String s = "";
        double elapsed = (end - start) / 1.0e+9;
        s = s + calcHMS((int) Math.round(elapsed));
        return s;
    }

    public static String calcHMS(int timeInSeconds) 
    {   String s = "";
        int hours, minutes, seconds;
        hours = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hours * 3600);
        minutes = timeInSeconds / 60;
        timeInSeconds = timeInSeconds - (minutes * 60);
        seconds = timeInSeconds;       
        s = s + hours + "h " + minutes + "m " + seconds + "s";
        return s;
    }

    public static void doPause(int timeInSeconds) 
    {   long t0, t1;
        t0 = System.currentTimeMillis();
        t1 = System.currentTimeMillis() + (timeInSeconds * 1000);
        do 
        {   t0 = System.currentTimeMillis();
        } while (t0 < t1);
    }
}