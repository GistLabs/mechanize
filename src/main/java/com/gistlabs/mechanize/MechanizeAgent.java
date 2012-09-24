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
import java.util.List;

import javax.imageio.ImageIO;

import com.gistlabs.mechanize.cookie.Cookies;
import com.gistlabs.mechanize.exceptions.MechanizeIOException;
import com.gistlabs.mechanize.exceptions.MechanizeURISyntaxException;
import com.gistlabs.mechanize.exceptions.MechanizeUnsupportedEncodingException;
import com.gistlabs.mechanize.form.Form;
import com.gistlabs.mechanize.form.FormElement;
import com.gistlabs.mechanize.form.FormParams;
import com.gistlabs.mechanize.form.Upload;
import com.gistlabs.mechanize.history.History;

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
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Mechanize agent.
 * 
 * <p>Interesting resources: http://en.wikipedia.org/wiki/List_of_HTTP_header_fields</p>
 * 
 * <p>NOTE: The mechanize library is not synchronized and should be used in a single thread environment or with custom synchronization.</p>
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class MechanizeAgent {
	
	private AbstractHttpClient client;
	private final Cookies cookies = new Cookies(this);
	private final List<Interceptor> interceptors = new ArrayList<Interceptor>();
	private final History history = new History(this);

	public MechanizeAgent() {
		client = new DefaultHttpClient();
	}
	
	public MechanizeAgent(AbstractHttpClient client) {
		this.client = client;
	}
	
	public History history() {
		return history;
	}
	
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
		return request(new HttpGet(uri));
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
		Document document = Jsoup.parse(content, baseUri);
		return new Page(this, baseUri, document, content, request, response);
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

	public Page click(Page page, Element link) {
		if(link.hasAttr("href")) {
			String uri = link.absUrl("href");
			return get(uri);
		}
		return null;
	}

	/** Returns the page object received as response to the form submit action. */
	public Page submit(Form form, FormParams formParams) {
		try {
			return request(createSubmitRequest(form, formParams));
		} catch (UnsupportedEncodingException e) {
			throw new MechanizeUnsupportedEncodingException(e);
		}
	}

	private HttpRequestBase createSubmitRequest(Form form, FormParams formParams)
			throws UnsupportedEncodingException {
		String uri = null;
		if(form.getElement().hasAttr("action"))
			uri = form.getElement().absUrl("action");
		else 
			uri = form.getPage().getUri().toString();
		
		boolean isDoPost = form.getElement().hasAttr("method") && form.getElement().attr("method").equalsIgnoreCase("post");
		
		HttpRequestBase request = null;
		
		if(isDoPost) {
			request = new HttpPost(uri);

			boolean isDoMultiPart = form.getElement().hasAttr("enctype") && form.getElement().attr("enctype").equalsIgnoreCase("multipart/form-data");
			if(isDoMultiPart) {

				MultipartEntity multiPartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				
				Charset utf8 = Charset.forName("UTF-8");
				for(FormParams.FormHttpParameter param : formParams) {
					if(param.isSingleValue())
						multiPartEntity.addPart(param.getName(), new StringBody(param.getValue(), utf8));
					else 
						for(String value : param.getValues())
							multiPartEntity.addPart(param.getName(), new StringBody(value, utf8));
				}

				for(FormElement formElement : form) {
					if(formElement instanceof Upload) {
						Upload upload = (Upload)formElement;
						File file = upload.hasFileValue() ? upload.getFileValue() : new File(upload.getValue());
						multiPartEntity.addPart(upload.getName(), new FileBody(file));
					}
				}
			
				((HttpPost)request).setEntity(multiPartEntity);

			} else {
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				for(FormParams.FormHttpParameter param : formParams) {
					if(param.isSingleValue())
						formparams.add(new BasicNameValuePair(param.getName(), param.getValue()));
					else {
						for(String value : param.getValues())
							formparams.add(new BasicNameValuePair(param.getName(), value));
					}
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
				((HttpPost)request).setEntity(entity);
			}
		}
		else {
			try {
				URIBuilder builder = new URIBuilder(uri);
				
				for(FormParams.FormHttpParameter param : formParams) {
					if(param.isSingleValue())
						builder.setParameter(param.getName(), param.getValue());
					else
						for(String value : param.getValues())
							builder.addParameter(param.getName(), value);
				}
				
				URI requestURI = builder.build();
				uri = requestURI.toString();
				request = new HttpGet(requestURI);
			}
			catch(URISyntaxException e) {
				throw new MechanizeURISyntaxException(e);
			}
		}
		return request;
	}
}
