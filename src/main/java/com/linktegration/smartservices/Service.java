package com.linktegration.smartservices;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.linktegration.smartservices.util.RequestOptions;
import com.linktegration.smartservices.util.ServiceInstanceStatus;
import com.linktegration.smartservices.util.ServiceNotStartedException;

abstract public class Service<T> {

	private Future<T> future;
	private Set<SimpleRequest<T>> requests = new HashSet<SimpleRequest<T>>();

	private Thread workerThread;

	private SimpleRequest<T> initiatingRequest;

	void setInitiatingRequest(SimpleRequest<T> req) {
		this.initiatingRequest = req;
	}

	SimpleRequest<T> getInitiatingRequest() {
		return this.initiatingRequest;
	}

	// ---- definition data
	protected boolean isGlobal() {
		return false;
	}

	protected int getCacheTTL() {
		return 0;
	}

	// final
	// do we expose this to the user?
	final boolean invalidateCache() {
		return false;
	}

	// this will only be called if this operation gets cached
	protected boolean handleEvent(Event e) {
		return false;
	}

	// this is where your actual code goes
	abstract protected T execute() throws Exception;

	/**
	 * You must implement this if your operation has to be cached in some way.
	 * The string you return here should be distinct for each combination of
	 * parameters the operation takes.
	 * 
	 * @return
	 */
	protected String getIdentityHash() {
		// NULL is the default because many operations don't have parameters
		return null;
	}

	final protected String getIdentity() {
		String hash = getIdentityHash();
		return this.getClass().getName() + "#" + ((hash == null) ? "" : hash);
	}

	// an operation instance may be proxying another operation instance
	// final
	final boolean isProxy() {
		return false;
	}

	// will only work if isProxy()==true
	// final
	final Service<T> getProxyTarget() {
		return null;
	}

	// runtime data ( provided by the manager )
	final int getStartTime() {
		return 0;
	}

	final int getEndTime() {
		return 0;
	}

	final ServiceInstanceStatus getStatus() {
		return null;
	}

	// remember to check this if you're running a long operation
	final protected boolean hasPendingRequests() {
		return false;
	}

	final Set<SimpleRequest<T>> getRequests() {
		return requests;
	}

	final public InteractiveRequest<T> interactiveRequest() {
		InteractiveRequest<T> ir = new InteractiveRequest<T>();
		SimpleRequest<T> req = createRequest(null, ir);
		// we need this references before any service executes
		req.attachInteractiveRequest(ir);
		ir.setup(req);
		return ir;
	}

	private SimpleRequest<T> createRequest(RequestOptions opts,
			InteractiveRequest<T> ir) {
		if (opts == null)
			opts = RequestOptions.getDefault();
		// get consumer ( the service that invoked us )
		// could be NULL
		Service<?> consumer = thread2service.get(Thread.currentThread());
		if (this.future != null) {
			// this means we are not a proxy
			// and that we are already executing
			return new SimpleRequest<T>(consumer, this, null);
		}
		if (isGlobal()) {
			// see if there is an equivalent service instance running
			// and route this request to that service instance
			Service<?> equivalentService = identity2service.get(getIdentity());
			if (equivalentService != null) {
				// we just cast the service to our generic type
				// we know it is of the same type if the identities match
				@SuppressWarnings("unchecked")
				Service<T> serv = (Service<T>) equivalentService;
				return new SimpleRequest<T>(consumer, serv, null);
			}
		}
		// we are not a proxy and we are not cached. we must execute
		SimpleRequest<T> req = new SimpleRequest<T>(consumer, this, null);
		this.setInitiatingRequest(req); // MUST be set before running ( to allow
										// interaction )
		if (ir != null)
			req.attachInteractiveRequest(ir);
		this.future = this.runInOwnThread();
		return req;
	}

	// creates and sends a request to the service
	// can only be called once. will throw an error otherwise
	final public SimpleRequest<T> request() {
		return request(null);
	}

	final public SimpleRequest<T> request(RequestOptions opts) {
		return createRequest(opts, null);
	}

	private Future<T> runInOwnThread() {
		// TODO: move executor to a global manager
		Executor executor = Executors.newFixedThreadPool(1);
		final Service<T> service = this;
		FutureTask<T> futureTask = new FutureTask<T>(new Callable<T>() {
			public T call() throws Exception {
				service.setupReferenceToWorkerThread(Thread.currentThread());
				T result = service.execute();
				service.clearReferenceToWorkerThread();
				removeFromCache(); // TODO: if we have a TTL, we should not
									// remove ourselves right away
				return result;
			}
		});
		executor.execute(futureTask);
		addToCacheIfNecessary();
		return futureTask;
	}

	final private static Map<Thread, Service<?>> thread2service = Collections
			.synchronizedMap(new HashMap<Thread, Service<?>>());
	final private static Map<String, Service<?>> identity2service = Collections
			.synchronizedMap(new HashMap<String, Service<?>>());

	private void setupReferenceToWorkerThread(Thread t) {
		// store relation ( both ways )
		thread2service.put(t, this);
		workerThread = t;
	}

	private void clearReferenceToWorkerThread() {
		// remove relation ( both ways )
		thread2service.remove(workerThread);
		workerThread = null;
	}

	private void addToCacheIfNecessary() {
		if (isGlobal()) {
			identity2service.put(getIdentity(), this);
		}
	}

	private void removeFromCache() {
		identity2service.remove(getIdentity());
	}

	final public Future<T> getFuture() throws ServiceNotStartedException {
		if (this.future == null)
			throw new ServiceNotStartedException();
		return this.future;
	}

	// TODO: cache
	final protected Object interact(Object message) {
		// which request do we use to bubble up?
		// we should use the initiating request
		// ( the one that resulted in the service starting execution )
		SimpleRequest<?> req;
		Service<?> srv = this;
		while (true) { // loop until we don't find a consumer

			req = srv.getInitiatingRequest();
			srv = req.getConsumer();
			if (srv == null)
				break;
		}
		// we now have the topmost request
		// does it have an interactive request attached?
		InteractiveRequest<?> ireq = req.getAttachedInteractiveRequest();
		if (ireq == null) {
			throw new Error(
					"No interactive request attached but a Service tried to interact()");
		}

		return ireq.ask(message);

	}
}
