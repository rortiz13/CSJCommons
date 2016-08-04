package co.com.tecnocom.csj.core.util.concurrent;

//import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

//import co.com.tecnocom.csj.core.util.properties.DatasourceProperties;

public class MultiDatasourceRequestBuilder {

	private List<SingleDatasourceThreadRequest> singleDatasourceRequests;
	private HashMap<String, List<String>> groupedProcessIds;
	private String query;
	private Date startDate;
	private Date endDate;

	public List<SingleDatasourceThreadRequest> listSingleDatasourceRequests(){
		return singleDatasourceRequests;
	}
	
	public MultiDatasourceRequestInvoker build(){
		if(groupedProcessIds == null){
			throw new NullPointerException("no hay un grupo de procesos asignados para este MultiDatasourceRequestBuilder");
		}
		if(query == null){
			throw new NullPointerException("no se ha asignado una query para este MultiDatasourceRequestBuilder");
		}
		if(startDate == null){
			throw new NullPointerException("no se ha asignado una fecha inicial para este MultiDatasourceRequestBuilder");
		}
		if(endDate == null){
			throw new NullPointerException("no se ha asignado una fecha final para este MultiDatasourceRequestBuilder");
		}
		
		Set<String> keys = groupedProcessIds.keySet();
		List<SingleDatasourceThreadRequest> threads = new LinkedList<SingleDatasourceThreadRequest>();
		for (String key : keys) {
			SingleDatasourceThreadRequest singleThread = new SingleDatasourceThreadRequest(key, groupedProcessIds.get(key), query, startDate, endDate);
			threads.add(singleThread);
		}
		singleDatasourceRequests = threads;
		MultiDatasourceRequestInvoker invoker = new MultiDatasourceRequestInvoker(this);
		return invoker;
	}
	
	public MultiDatasourceRequestBuilder setGroupedProcessIds(HashMap<String, List<String>> groupedProcessIds){
		this.groupedProcessIds = groupedProcessIds;
		return this;
	}
	
	public MultiDatasourceRequestBuilder setQuery(String query){
		this.query = query;
		return this;
	}
	
	public MultiDatasourceRequestBuilder setStartDate(Date startDate){
		this.startDate = startDate;
		return this;
	}
	
	public MultiDatasourceRequestBuilder setEndDate(Date endDate){
		this.endDate = endDate;
		return this;
	}
	
}
