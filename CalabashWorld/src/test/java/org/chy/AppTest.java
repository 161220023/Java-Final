package org.chy;

import java.util.Random;

import being.Being;
import formation.Position;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	
    	class BeingThread extends Thread{
        	
        	Being being;
        	Random r;
        	
        	BeingThread(Being being){
        		this.being=being;
        		r=new Random();
        	}
        	
        	public void run() {
        		int next=r.nextInt(10);
        		being.moveto(0, next);
        	}
        }
    	
        Position[][] pos=new Position[1][10];
        pos[0]=new Position[10];
        for(int i=0;i<10;i++) {
        	pos[0][i]=new Position(0,i);
        }
        
        Being[] beings=new Being[10];
        for(int i=0;i<10;i++) {
        	beings[i]=new Being(pos);
        	BeingThread threads=new BeingThread(beings[i]);
        	threads.start();
        }
        
        boolean[] occupy=new boolean[10];
        
        for(int i=0;i<10;i++) {
        	occupy[i]=false;
        }
        
        for(int i=0;i<10;i++) {
        	int col=beings[i].getcol();
        	if(col!=-1) {
        		assertEquals(occupy[col],false);
        		occupy[col]=true;
        	}
        }
    }
}
