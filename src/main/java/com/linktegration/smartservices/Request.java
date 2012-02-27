package com.linktegration.smartservices;

import com.linktegration.smartservices.util.RequestNotFinishedException;

public interface Request<T> {
	
	/**
	 * 
	 * Note: This may not be the same Service instance that returned the request
	 * it may be acting as a proxy to a global Service
	 * 
	 * @return
	 */
	public Service<T> getTargetService();

	/**
	 * this is the start time of the request, not the underlying operation
	 * 
	 * @return
	 */
	public long getStartTime();

	/**
	 * end time of this request, not the underlying Service
	 * 
	 * @return
	 */
	public long getEndTime() throws RequestNotFinishedException;
	
	/**
	 * time since the request started.
	 * when the request finishes, this will indicate the duration.
	 * 
	 * @return
	 */
	public long getElapsedTime();
}
