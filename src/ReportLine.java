import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.annotation.*;

/**
 * Object representing a single line of the CSV report
 * Assumes correct formatting as given and is not flexible to report changes.
 */
public class ReportLine implements Comparable<ReportLine>
{
	private String clientAddress;
	private String clientGUID;
	private Date requestTime;
	private String serviceGUID;
	private int retriesRequest;
	private int packetsRequested;
	private int packetsServiced;
	private int maxHoleSize;

	
	private static final String CLIENT_ADDRESS = "client-address";
	private static final String CLIENT_GUID = "client-guid";
	private static final String REQUEST_TIME = "request-time";
	private static final String SERVICE_GUID = "service-guid";
	private static final String RETRIES_REQUEST = "retries-request";
	private static final String PACKETS_REQUESTED = "packets-requested";
	private static final String PACKETS_SERVICED = "packets-serviced";
	private static final String MAX_HOLE_SIZE = "max-hole-size";
	
	/**
	 * Empty constructor
	 */
	public ReportLine() {}
	
//	public ReportLine(String clientAddress, String clientGUID, Date requestTime, String serviceGUID, int retriesRequest,
//				  int packetsRequested, int packetsServiced, int maxHoleSize)
//	{
//		super();
//		this.clientAddress = clientAddress;
//		this.clientGUID = clientGUID;
//		this.requestTime = requestTime;
//		this.serviceGUID = serviceGUID;
//		this.retriesRequest = retriesRequest;
//		this.packetsRequested = packetsRequested;
//		this.packetsServiced = packetsServiced;
//		this.maxHoleSize = maxHoleSize;
//	}

	@JsonProperty(CLIENT_ADDRESS)
	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	@JsonProperty(CLIENT_GUID)
	public String getClientGUID() {
		return clientGUID;
	}

	public void setClientGUID(String clientGUID) {
		this.clientGUID = clientGUID;
	}

	@JsonProperty(REQUEST_TIME)
	public Date getRequestTime() {
		return requestTime;
	}
	
	public void setRequestTime(String requestTime) throws ParseException {
		if(NumberUtils.isDigits(requestTime))
		{
			this.requestTime = new Date(Long.parseLong(requestTime));
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
			this.requestTime = sdf.parse(requestTime);;
		}
	}

	@JsonProperty(SERVICE_GUID)
	public String getServiceGUID() {
		return serviceGUID;
	}

	public void setServiceGUID(String serviceGUID) {
		this.serviceGUID = serviceGUID;
	}

	@JsonProperty(RETRIES_REQUEST)
	public int getRetriesRequest() {
		return retriesRequest;
	}

	public void setRetriesRequest(String retriesRequest) {
		this.retriesRequest = Integer.parseInt(retriesRequest);
	}

	@JsonProperty(PACKETS_REQUESTED)
	public int getPacketsRequested() {
		return packetsRequested;
	}
	
	public void setPacketsRequested(String packetsRequested) {
		this.packetsRequested = Integer.parseInt(packetsRequested);
	}
	
	@JsonProperty(PACKETS_SERVICED)
	public int getPacketsServiced() {
		return packetsServiced;
	}

	public void setPacketsServiced(String packetsServiced) {
		this.packetsServiced = Integer.parseInt(packetsServiced);
	}

	@JsonProperty(MAX_HOLE_SIZE)
	public int getMaxHoleSize() {
		return maxHoleSize;
	}

	public void setMaxHoleSize(String maxHoleSize) {
		this.maxHoleSize = Integer.parseInt(maxHoleSize);
	}
	
	@Override
	public int compareTo(ReportLine o) {
		return getRequestTime().compareTo(o.getRequestTime());
	}
}
