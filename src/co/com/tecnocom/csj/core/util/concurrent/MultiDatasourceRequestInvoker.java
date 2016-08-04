package co.com.tecnocom.csj.core.util.concurrent;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiDatasourceRequestInvoker {

	private AtomicBoolean threadsBusy;
	private List<SingleDatasourceThreadRequest> threads;
	private int threadsRunningCount;
	
	
	
	
	private List<String[]> finalResults;
	
	
	public MultiDatasourceRequestInvoker(MultiDatasourceRequestBuilder builder) {
		this.threadsBusy = new AtomicBoolean(true);//true;
		threads = builder.listSingleDatasourceRequests();
		for (SingleDatasourceThreadRequest thread : threads) {
			thread.setObserver(this);
		}
		threadsRunningCount = builder.listSingleDatasourceRequests().size();
		finalResults = new LinkedList<String[]>();
	}
	
	/**
	 * este metodo detiene el hilo principal mientras los otros realizan las consultas necesarias a los datasources correspondientes
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void startQuery() throws InterruptedException{
		System.out.println("iniciando hilos para consultas " + threads.size());
		for(Thread thread: threads){
			thread.start();
		}
		if(threadsRunningCount > 0){
			do{
				wait();
			} while(threadsBusy.get());
			System.out.println("finalizados hilos para consultas");
		} else{
			System.out.println("no se realizó ninguna consulta");
		}
	}
	
	public synchronized void notifyCompletion(List<String[]> partialResults){
		if(partialResults != null){
			System.out.println("partialResults.size() = " + partialResults.size());
			finalResults.addAll(partialResults);
		}
		if(threadsRunningCount <= 1){
			threadsBusy.set(false);// = false;
			notify();
		}
		else{
			threadsRunningCount--;
		}
	}
	
	public List<String[]> getResults(){
		return finalResults;
	}
}
