package pl.tinlink.josu.utils;

public class StateLock {
	private boolean isDone = false;
	private boolean waitingForSubscriber = false;
	Thread subscriber;
	
	public void subscribe()
	{
		if(waitingForSubscriber)
		{
			subscriber.interrupt();
			waitingForSubscriber = false;
			subscriber = null;
		}
		
		if(subscriber == null)
		{
			subscriber = Thread.currentThread();
		}else throw new IllegalStateException();
		
		while(!isDone)
		{	
			try {
				Thread.sleep(Long.MAX_VALUE);
			} catch (InterruptedException e) {}
		}
	}
	
	public void done()
	{
		isDone = true;
		if(subscriber == null)throw new IllegalStateException();
		
		subscriber.interrupt();
	}
	
	public void waitForSubscriber()
	{
		if(!waitingForSubscriber || subscriber != null)return;
		if(waitingForSubscriber || subscriber != null)throw new IllegalStateException();
		waitingForSubscriber = true;
		subscriber = Thread.currentThread();
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {}
	}
	
	public boolean isDone()
	{
		return isDone;
	}
}
