/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.gistlabs.mechanize.cookie.Cookies;
import com.gistlabs.mechanize.exceptions.MechanizeIOException;
import com.gistlabs.mechanize.exceptions.MechanizeURISyntaxException;
import com.gistlabs.mechanize.exceptions.MechanizeUnsupportedEncodingException;
import com.gistlabs.mechanize.form.Form;
import com.gistlabs.mechanize.form.FormElement;
import com.gistlabs.mechanize.form.Upload;
import com.gistlabs.mechanize.history.History;
import com.gistlabs.mechanize.link.Link;
import com.gistlabs.mechanize.parameters.Parameter;
import com.gistlabs.mechanize.parameters.Parameters;
import com.gistlabs.mechanize.util.Util;

/**
 * Mechanize agent.
 * 
 * <p>Interesting resources: http://en.wikipedia.org/wiki/List_of_HTTP_header_fields</p>
 * 
 * <p>NOTE: The mechanize library is not synchronized and should be used in a single thread environment or with custom synchronization.</p>
 * 
 * TODO Add a test case for the doRequest multi part form sending a file.
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class MechanizeAgent implements PageRequestor {
	
	private AbstractHttpClient client;
	private final Cookies cookies;
	private final List<Interceptor> interceptors = new ArrayList<Interceptor>();
	private final History history = new History(this);

	public MechanizeAgent() {
		this(new DefaultHttpClient());
	}
	
	public MechanizeAgent(AbstractHttpClient client) {
		this.client = client;
		this.cookies = new Cookies(client);
	}
	
	public History history() {
		return history;
	}

	@Override
	public Page request(HttpRequestBase request) {
		try {
			HttpResponse response = execute(client, request);
			Page page = toPage(request, response);
			history.add(page);
			return page;
		} catch (ClientProtocolException e) {
			throw new com.gistlabs.mechanize.exceptions.MechanizeClientProtocolException(e);
		} catch (IOException e) {
			throw new MechanizeIOException(e);
		}
	}
	
	public Page get(String uri) {
		return doRequest(uri).get();
	}
	
	public RequestBuilder doRequest(String uri) {
		return new RequestBuilder(this, uri);
	}
	
	public Page post(String uri, Map<String, String> params) throws UnsupportedEncodingException {
		return post(uri, new Parameters(unsafeCast(params)));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> unsafeCast(Map<String, String> params) {
		return (Map<String,Object>)(Map)params;
	}
	
	public Page post(String uri, Parameters params) {
		return doRequest(uri).set(params).post();
	}
		
	public void addInterceptor(Interceptor interceptor) {
		if(!interceptors.contains(interceptor))
			interceptors.add(interceptor);
	}
	
	/** Idles / Waits for the given amount of milliseconds useful to prevent being blocked by mass sending 
	 *  requests or to appear as a artificial user. */
	public void idle(int milliseconds) {
		long startTime = System.currentTimeMillis();
		while((System.currentTimeMillis() - startTime) < milliseconds) {
			try {
				Thread.sleep(Math.max(1, milliseconds - (System.currentTimeMillis() - startTime)));
			}
			catch(InterruptedException e) {
			}
		}
	}

	public void removeInterceptor(Interceptor interceptor) {
		interceptors.remove(interceptor);
	}
	
	public Cookies cookies() {
		return cookies;
	}
	
	/** Returns the content received as a byte buffer. 
	 * 
	 * TODO: Move body into util somewhere
	 * 
	 * @throws IllegalArgumentException If file exists. */
	public byte [] getToBuffer(String uri) {
		InputStream content = null;
		ByteArrayOutputStream out = null;
		
		try {
			HttpGet request = new HttpGet(uri);
			HttpResponse response = execute(client, request);
			content = response.getEntity().getContent();
			out = new ByteArrayOutputStream();
			
			byte [] buffer = new byte[4096];
			while(true) {
				int read = content.read(buffer);	
				if(read > 0) 
					out.write(buffer, 0, read);
				else if(read == -1)
					break;
			}
			return out.toByteArray();
		} catch (ClientProtocolException e) {
			throw new com.gistlabs.mechanize.exceptions.MechanizeClientProtocolException(e);
		} catch (IOException e) {
			throw new MechanizeIOException(e);
		}
		finally {
			try {
				if(out != null)
					out.close();
			}
			catch(IOException e) {
			}
			finally {
				try {
					if(content != null)
						content.close();
				}
				catch(IOException e) {
				}
			}
		}
	}
	

	/** Returns the image received by the URI. */
	public BufferedImage getImage(String uri) {
		try {
			byte [] buffer = getToBuffer(uri);
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(buffer));
			return image;
		} catch (IOException e) {
			throw new MechanizeIOException(e);
		}
	}

	/** Writes the content received to file. 
	 * This method has a very small memory footprint by using a 4KB buffer.  
	 * 
	 * TODO move body into util somewhere
	 * 
	 * @throws IllegalArgumentException If file exists. */
	public void getToFile(String uri, File file) {
		if(file.exists())
			throw new IllegalArgumentException("File '" + file.toString() + "' already exists");
		
		InputStream content = null;
		BufferedOutputStream out = null;
		
		try {
			HttpGet request = new HttpGet(uri);
			HttpResponse response = execute(client, request);
			content = response.getEntity().getContent();
			out = new BufferedOutputStream(new FileOutputStream(file));
			
			byte [] buffer = new byte[4096];
			while(true) {
				int read = content.read(buffer);	
				if(read > 0) 
					out.write(buffer, 0, read);
				else if(read == -1)
					break;
			}
		} catch (ClientProtocolException e) {
			throw new com.gistlabs.mechanize.exceptions.MechanizeClientProtocolException(e);
		} catch (IOException e) {
			throw new MechanizeIOException(e);
		}
		finally {
			try {
				if(out != null)
					out.close();
			}
			catch(IOException e) {
			}
			finally {
				try {
					if(content != null)
						content.close();
				}
				catch(IOException e) {
				}
			}
		}
	}
	
	public AbstractHttpClient getClient() {
		return client;
	}
	
	private Page toPage(HttpRequestBase request, HttpResponse response)
			throws IOException, UnsupportedEncodingException {
		InputStream in = response.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, getEncoding(response)));
		String line;
		StringBuilder contentBuilder = new StringBuilder();
		while((line = reader.readLine()) != null) {
			contentBuilder.append(line);
			contentBuilder.append('\n');
		}
		String content = contentBuilder.toString();
		
		String baseUri = request.getURI().toString();
		Header contentLocation = Util.findHeader(response, "content-location");
		if(contentLocation != null && contentLocation.getValue() != null)
			baseUri = contentLocation.getValue();
		return new Page(this, baseUri, content, request, response);
	}

	protected HttpResponse execute(HttpClient client, HttpRequestBase request) throws IOException, ClientProtocolException {
		for(RequestInterceptor interceptor : filterInterceptors(RequestInterceptor.class))
			interceptor.intercept(this, request);
		HttpResponse response = client.execute(request);
		
		for(ResponseInterceptor interceptor : filterInterceptors(ResponseInterceptor.class))
			interceptor.intercept(this, response, request);
		return response;
	}

	private <T extends Interceptor> List<T> filterInterceptors(Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		for(Interceptor interceptor : interceptors)
			if(clazz.isInstance(interceptor))
				result.add(clazz.cast(interceptor));
		return result;
	}
	
	private String getEncoding(HttpResponse response) {
		Header encoding = response.getEntity().getContentEncoding();
		return encoding != null ? encoding.getValue() : Charset.defaultCharset().name();
	}

	public Page click(Page page, Link link) {
		return get(link.href());
	}

	/** Returns the page object received as response to the form submit action. */
	public Page submit(Form form, Parameters parameters) {
		RequestBuilder request = doRequest(form.getUri()).set(parameters);
		boolean doPost = form.isDoPost();
		boolean multiPart = form.isMultiPart();
		if(doPost && multiPart) {
			request.multiPart();
			addFiles(request, form);
		}
		return doPost ? request.post() : request.get(); 
	}

	private void addFiles(RequestBuilder request, Form form) {
		for(FormElement formElement : form) {
			if(formElement instanceof Upload) {
				Upload upload = (Upload)formElement;
				if (upload.hasValue()) {
					File file = upload.hasFileValue() ? upload.getFileValue() : new File(upload.getValue());
					request.set(upload.getName(), file);					
				}
			}
		}
	}

	public class RequestBuilder {
		private final PageRequestor requestor;
		private String uri;
		private final Parameters parameters = new Parameters();
		private final Map<String, ContentBody> files = new HashMap<String, ContentBody>();
		private boolean isMultiPart = false;

		public RequestBuilder(PageRequestor requestor) {
			this.requestor = requestor;
		}
		
		private RequestBuilder(PageRequestor requestor, String uri) {
			this(requestor);
			setUri(uri);
		}

		private void setUri(String uri) {
			this.uri = uri;
			
			if(uri.contains("?")) 
				for(NameValuePair param : URLEncodedUtils.parse(uri.substring(uri.indexOf('?') + 1), Charset.forName("UTF-8"))) 
					parameters.add(param.getName(), param.getValue());
		}
		
		public RequestBuilder multiPart() {
			this.isMultiPart = true;
			return this;
		}
		
		public RequestBuilder add(String name, String ... values) {
			parameters.add(name, values);
			return this;
		}
		
		public RequestBuilder set(String name, String ... values) {
			parameters.set(name, values);
			return this;
		}
		
		public RequestBuilder set(Parameters parameters) {
			for(String name : parameters.getNames()) 
				set(name, parameters.get(name));
			
			return this;
		}

		public RequestBuilder add(Parameters parameters) {
			for(String name :parameters.getNames()) 
				add(name, parameters.get(name));
			
			return this;
		}
		
		/** Adds a file to the request also making the request to become a multi-part post request or removes any file registered
		 *  under the given name if the file value is null. */
		public RequestBuilder set(String name, File file) {
			return set(name, file != null ? new FileBody(file) : null);
		}
		
		/** Adds an ContentBody object. */
		public RequestBuilder set(String name, ContentBody contentBody) {
			if(contentBody != null)
				files.put(name, contentBody);
			else
				files.remove(name);
			return this;
		}
		
		
		
		public Parameters parameters() {
			return parameters;
		}
		
		public Page get() {
			if(hasFiles())
				throw new UnsupportedOperationException("Files can not be send using a get request");
			return requestor.request(composeGetRequest(uri, parameters));
		}
		
		public Page post() {
			HttpPost request = (!hasFiles()) || isMultiPart ? composePostRequest(getBaseUri(), parameters) : 
				composeMultiPartFormRequest(getBaseUri(), parameters, files);
			return requestor.request(request);
		}
		
		private boolean hasFiles() {
			return !files.isEmpty();
		}

		private String getBaseUri() {
			return uri.contains("?") ? uri.substring(0, uri.indexOf('?')) : uri;
		}
		
		private HttpRequestBase composeGetRequest(String uri, Parameters parameters) {
			try {
				URIBuilder builder = new URIBuilder(uri);
				
				for(Parameter param : parameters) {
					if(param.isSingleValue())
						builder.setParameter(param.getName(), param.getValue());
					else
						for(String value : param.getValues())
							builder.addParameter(param.getName(), value);
				}
				
				URI requestURI = builder.build();
				uri = requestURI.toString();
				return new HttpGet(requestURI);
			}
			catch(URISyntaxException e) {
				throw new MechanizeURISyntaxException(e);
			}
		}
		
		private HttpPost composePostRequest(String uri, Parameters parameters) {
			HttpPost request = new HttpPost(uri);
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for(Parameter param : parameters) {
				if(param.isSingleValue())
					formparams.add(new BasicNameValuePair(param.getName(), param.getValue()));
				else {
					for(String value : param.getValues())
						formparams.add(new BasicNameValuePair(param.getName(), value));
				}
			}
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
				request.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				throw new MechanizeUnsupportedEncodingException(e);
			}
			
			return request;
		}

		private HttpPost composeMultiPartFormRequest(String uri, Parameters parameters, Map<String, ContentBody> files) {
			HttpPost request = new HttpPost(uri);
			MultipartEntity multiPartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			try {
				Charset utf8 = Charset.forName("UTF-8");
				for(Parameter param : parameters) {
					if(param.isSingleValue())
						multiPartEntity.addPart(param.getName(), new StringBody(param.getValue(), utf8));
					else 
						for(String value : param.getValues())
								multiPartEntity.addPart(param.getName(), new StringBody(value, utf8));
				}
			} catch (UnsupportedEncodingException e) {
				throw new MechanizeUnsupportedEncodingException(e); 
			}

			List<String> fileNames = new ArrayList<String>(files.keySet());
			Collections.sort(fileNames);
			for(String name : fileNames) {
				ContentBody contentBody = files.get(name);
				multiPartEntity.addPart(name, contentBody);
			}
			((HttpPost)request).setEntity(multiPartEntity);
			return request;
		}
	}
}
