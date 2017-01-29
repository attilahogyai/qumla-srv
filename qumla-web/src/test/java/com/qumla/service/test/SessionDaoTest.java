package com.qumla.service.test;

import org.junit.Test;

public class SessionDaoTest extends AbstractTest {
	@Test
	public void synchronizeTest(){
		String lockid="1111";
		Thread a = new Thread(){
			public void run(){
				synchronized (lockid) {
					System.out.println("im in sync block");
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		a.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized(lockid){
			System.out.println("im in sync block 2");
		}
	}
}
