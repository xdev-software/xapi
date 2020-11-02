package xdev.ui;

/*-
 * #%L
 * XDEV Application Framework
 * %%
 * Copyright (C) 2003 - 2020 XDEV Software
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import xdev.db.Operator;
import xdev.io.ByteHolder;
import xdev.util.ObjectUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;


/**
 * 
 * The standard component of a picture in XDEV.
 * 
 * <p>
 * Provides methods to load and change pictures.
 * </p>
 * 
 * <p>
 * Supported image formats: see
 * {@link ImageFileFilter#getSupportedImageExtensions()}
 * </p>
 * 
 * @see XdevComponent
 * @see FormularComponent
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 * 
 */
@BeanSettings(acceptChildren = true, useXdevCustomizer = true)
public class XdevPicture extends XdevComponent implements ImageFormularComponent<XdevPicture>
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger						log						= LoggerFactory
																						.getLogger(XdevPicture.class);
	
	public final static String							RESIZE_MODE_PROPERTY	= "resizeMode";
	public final static String							AUTO_RESIZE_PROPERTY	= "autoResize";
	public final static String							OUTPUT_FORMAT_PROPERTY	= "outputFormat";
	public final static String							IMAGE_PROPERTY			= "image";
	
	public final static int								RESIZE_STRETCH			= 0;
	public final static int								RESIZE_FIT				= 1;
	
	private int											resizeMode				= RESIZE_STRETCH;
	private boolean										autoResize				= false;
	private String										outputFormat			= "png";
	
	private String										imagePath;
	private Image										image;
	private Image										resizedImage;
	
	private Image										savedValue				= null;
	
	private boolean										valid					= false;
	private Dimension									lastResizeCheck			= new Dimension();
	
	private final FormularComponentSupport<XdevPicture>	support					= new FormularComponentSupport(
																						this);
	
	
	/**
	 * Creates an empty {@link XdevPicture}.
	 * 
	 * 
	 * @see #XdevPicture(String, boolean)
	 */
	public XdevPicture()
	{
		this(null,false);
	}
	
	
	/**
	 * Creates an {@link XdevPicture} and load the image from the
	 * <code>imagePath</code> immediately.
	 * 
	 * @param imagePath
	 *            the path of the image
	 * 
	 * @see #XdevPicture(String, boolean)
	 */
	public XdevPicture(String imagePath)
	{
		this(imagePath,true);
	}
	
	
	/**
	 * Creates an {@link XdevPicture} with the given <code>imagePath</code>.
	 * 
	 * @param imagePath
	 *            the path of the image
	 * 
	 * @param loadImmediately
	 *            <code>true</code> if the picture is loaded immediately,
	 *            otherwise <code>false</code>
	 * 
	 * @see XdevPicture#refresh(boolean, boolean)
	 */
	public XdevPicture(String imagePath, boolean loadImmediately)
	{
		super();
		
		this.imagePath = imagePath;
		
		if(loadImmediately)
		{
			refresh(false,false);
		}
	}
	
	
	/**
	 * Creates an {@link XdevPicture} and load the image. The
	 * {@link XdevPicture} is not resizeable.
	 * 
	 * @param image
	 *            the {@link XdevImage}
	 * 
	 * @see #XdevPicture(Image)
	 */
	public XdevPicture(XdevImage image)
	{
		this(image.getAWTImage());
	}
	
	
	/**
	 * Creates an {@link XdevPicture} and load the image. The
	 * {@link XdevPicture} is not resizeable.
	 * 
	 * @param image
	 *            the image
	 * 
	 * @see #XdevPicture(String, boolean)
	 */
	public XdevPicture(Image image)
	{
		super();
		
		imageLoaded(image,false);
	}
	
	{
		addComponentListener(new ComponentAdapter()
		{
			Timer	timer;
			{
				timer = new Timer(100,new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						checkImageSize();
					}
				});
				timer.setRepeats(false);
			}
			
			
			@Override
			public void componentResized(ComponentEvent e)
			{
				timer.restart();
			}
		});
		
		setOpaque(false);
	}
	
	
	/*
	 * Opaque is always false to avoid artifacts
	 */
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpaque()
	{
		return false;
	}
	
	
	@Override
	protected boolean paintComponent()
	{
		return (style != null && style.isOpaque()) || resizedImage != null;
	}
	
	
	/**
	 * Sets the resize mode and refresh the {@link XdevPicture}.
	 * 
	 * @param resizeMode
	 *            {@link #RESIZE_STRETCH} or {@link #RESIZE_FIT}
	 * 
	 * @throws IllegalArgumentException
	 *             if the <code>resizeMode</code> is not supported
	 * 
	 * @see #refresh()
	 */
	
	public void setResizeMode(int resizeMode) throws IllegalArgumentException
	{
		if(this.resizeMode != resizeMode)
		{
			switch(resizeMode)
			{
				case RESIZE_FIT:
				case RESIZE_STRETCH:
				break;
				
				default:
					throw new IllegalArgumentException("resizeMode");
			}
			
			int oldValue = this.resizeMode;
			this.resizeMode = resizeMode;
			
			firePropertyChange(RESIZE_MODE_PROPERTY,oldValue,resizeMode);
			
			refresh();
		}
	}
	
	
	/**
	 * Returns the resize mode of this {@link XdevPicture}.<br>
	 * Possible values are:
	 * 
	 * <ul>
	 * <li>{@link #RESIZE_FIT}</li>
	 * <li>{@link #RESIZE_STRETCH}</li>
	 * </ul>
	 * 
	 * @return the value of the resize mode as a <code>int</code>
	 */
	public int getResizeMode()
	{
		return resizeMode;
	}
	
	
	public void setAutoResize(boolean autoResize)
	{
		if(this.autoResize != autoResize)
		{
			boolean oldValue = this.autoResize;
			this.autoResize = autoResize;
			
			firePropertyChange(AUTO_RESIZE_PROPERTY,oldValue,autoResize);
			
			refresh();
		}
	}
	
	
	/**
	 * Returns <code>true</code> if this {@link XdevPicture} is auto resizable.
	 * 
	 * @return <code>true</code> if this {@link XdevPicture} is auto resizable,
	 *         otherwise <code>false</code>
	 * 
	 */
	public boolean isAutoResize()
	{
		return autoResize;
	}
	
	
	/**
	 * Sets the output format of this {@link XdevPicture}.
	 * 
	 * <p>
	 * Default supported formats are: JPG, PNG, GIF and BMP.
	 * </p>
	 * 
	 * @param outputFormat
	 *            a {@link String} with the new output format
	 * 
	 * @throws IllegalArgumentException
	 *             if the <code>outputFormat</code> is <code>null</code> or not
	 *             supported
	 * 
	 * @see ImageFileFilter#getSupportedImageExtensions()
	 * @see ImageFileFilter#checkImageFormat(String)
	 */
	public void setOutputFormat(String outputFormat) throws IllegalArgumentException
	{
		if(outputFormat == null)
		{
			throw new IllegalArgumentException("output format cannot be null");
		}
		
		outputFormat = ImageFileFilter.checkImageFormat(outputFormat);
		
		if(!this.outputFormat.equals(outputFormat))
		{
			String oldValue = this.outputFormat;
			this.outputFormat = outputFormat;
			
			firePropertyChange(OUTPUT_FORMAT_PROPERTY,oldValue,outputFormat);
		}
	}
	
	
	/**
	 * Returns the output format of this {@link XdevPicture}. (Standard output
	 * format is <code>PNG</code>)
	 * 
	 * <p>
	 * Default supported formats are: JPG, PNG, GIF and BMP.
	 * </p>
	 * 
	 * @return the output format
	 */
	public String getOutputFormat()
	{
		return outputFormat;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevImage getImage()
	{
		if(image == null)
		{
			return null;
		}
		
		return new XdevImage(image);
	}
	
	
	/**
	 * Returns the resized image, may be the same as {@link #getImage()} if the
	 * component's size matches the size of the image.
	 * 
	 * @return the resized image
	 * @since 3.2
	 */
	public XdevImage getResizedImage()
	{
		if(resizedImage == null)
		{
			return null;
		}
		
		return new XdevImage(resizedImage);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setImage(XdevImage image)
	{
		setImage(image,autoResize);
	}
	
	
	public void setImage(XdevImage image, boolean resize)
	{
		setImage(image == null ? null : image.getAWTImage(),resize);
	}
	
	
	public void setImage(Image image)
	{
		setImage(image,autoResize);
	}
	
	
	public void setImage(final Image image, final boolean resize)
	{
		flush();
		if(image == null)
		{
			return;
		}
		
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				imageLoaded(image,resize);
			}
		};
		getExecutor().submit(runnable);
	}
	
	
	/**
	 * Sets the path of the image and load the picture.
	 * 
	 * <p>
	 * This is an alias for {@link #setImage(String, boolean)}.
	 * </p>
	 * 
	 * @param imagePath
	 *            the image path
	 */
	public void setImage(String imagePath)
	{
		setImage(imagePath,autoResize);
	}
	
	
	/**
	 * Sets the path of the image and loads the picture.
	 * 
	 * @param imagePath
	 *            the path of the image
	 * 
	 * @param resize
	 *            <code>true</code> to adjust the component's size
	 * 
	 * 
	 * @see #refresh(boolean)
	 */
	public void setImage(String imagePath, boolean resize)
	{
		this.imagePath = imagePath;
		refresh(resize);
	}
	
	
	/**
	 * Returns the image path of this {@link XdevPicture}.
	 * 
	 * @return The string form of the image path
	 */
	public String getImagePath()
	{
		return imagePath;
	}
	
	
	/**
	 * Sets the path of the image.
	 * 
	 * @param path
	 *            the image path
	 */
	public void setImagePath(String path)
	{
		setImage(path);
	}
	
	
	/**
	 * 
	 */
	public void loadImage(InputStream in) throws IOException
	{
		loadImage(in,autoResize);
	}
	
	
	public void loadImage(InputStream in, boolean resize) throws IOException
	{
		Image image = ImageIO.read(in);
		if(image != null)
		{
			setImage(image,resize);
		}
	}
	
	
	public void loadImage(File file) throws IOException
	{
		loadImage(file,autoResize);
	}
	
	
	public void loadImage(File file, boolean resize) throws IOException
	{
		Image image = ImageIO.read(file);
		if(image != null)
		{
			setImage(image,resize);
		}
	}
	
	
	public void loadImage(URL url) throws IOException
	{
		loadImage(url,autoResize);
	}
	
	
	public void loadImage(URL url, boolean resize) throws IOException
	{
		Image image = ImageIO.read(url);
		if(image != null)
		{
			setImage(image,resize);
		}
	}
	
	
	public void loadImage(byte[] data)
	{
		loadImage(data,autoResize);
	}
	
	
	public void loadImage(byte[] data, boolean resize)
	{
		Image image = null;
		
		try
		{
			image = ImageIO.read(new ByteArrayInputStream(data));
		}
		catch(IOException e)
		{
			log.error(e);
		}
		
		if(image != null)
		{
			setImage(image,resize);
		}
	}
	
	
	public void loadImage(String path)
	{
		loadImage(path,autoResize);
	}
	
	
	public void loadImage(String path, boolean resize)
	{
		setImage(path,resize);
	}
	
	
	/**
	 * Refresh this {@link XdevPicture}.
	 * 
	 * <p>
	 * This is an alias for {@link #refresh(boolean)} .
	 * </p>
	 */
	public void refresh()
	{
		refresh(autoResize);
	}
	
	
	/**
	 * Refresh this {@link XdevPicture}.
	 * 
	 * <p>
	 * This is an alias for {@link #refresh(boolean, boolean)}.
	 * </p>
	 * 
	 * @param resize
	 */
	public void refresh(boolean resize)
	{
		refresh(resize,true);
	}
	
	private boolean	refreshing	= false;
	
	
	protected void refresh(final boolean resize, boolean inThread)
	{
		if(inThread && Beans.isDesignTime())
		{
			inThread = false;
		}
		
		valid = true;
		final String imagePath = this.imagePath;
		if(imagePath == null || imagePath.length() == 0)
		{
			flush();
			return;
		}
		
		if(refreshing)
		{
			return;
		}
		refreshing = true;
		
		final boolean _inThread = inThread;
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					flush();
					
					if(imagePath != null && imagePath.length() > 0)
					{
						imageLoaded(loadImage(),resize);
					}
				}
				catch(Exception e)
				{
					log.error(e);
				}
				finally
				{
					refreshing = false;
					
					// if image is loaded after picture was already closed
					if(_inThread && !Beans.isDesignTime())
					{
						SwingUtilities.invokeLater(new Runnable()
						{
							public void run()
							{
								if(!valid)
								{
									flush();
								}
							}
						});
					}
				}
			}
		};
		
		if(inThread)
		{
			getExecutor().submit(runnable);
		}
		else
		{
			runnable.run();
		}
	}
	
	
	protected Image loadImage()
	{
		try
		{
			Image image = getToolkit().createImage(imagePath.getBytes(VirtualTable.CHARSET));
			MediaTracker mt = new MediaTracker(this);
			mt.addImage(image,0);
			mt.waitForID(0);
			if(image.getWidth(null) > 0 && image.getHeight(null) > 0)
			{
				return image;
			}
		}
		catch(Exception e)
		{
		}
		
		try
		{
			return GraphicUtils.loadImagePlain(imagePath,this);
		}
		catch(Exception e)
		{
			log.error(e);
			
			return null;
		}
	}
	
	
	/**
	 * Sets a {@link Image} <code>image</code> to be displayed by this
	 * {@link XdevPicture}.
	 * 
	 * @param image
	 *            {@link Image} to be displayed
	 * @param resize
	 *            <code>true</code> to adjust the component's size
	 */
	protected void imageLoaded(final Image image, final boolean resize)
	{
		if(SwingUtilities.isEventDispatchThread())
		{
			imageLoadedImpl(image,resize);
		}
		else
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					imageLoadedImpl(image,resize);
				}
			});
		}
	}
	
	
	private void imageLoadedImpl(Image image, boolean resize)
	{
		if(resize && Beans.isDesignTime())
		{
			resize = false;
		}
		
		Image oldImage = this.image;
		
		this.image = image;
		
		if(image != null)
		{
			if(resize)
			{
				checkAlphaUse();
				
				Dimension d = new Dimension(image.getWidth(null),image.getHeight(null));
				if(getParent() != null && getParent().getLayout() != null)
				{
					setPreferredSize(d);
					revalidate();
				}
				else
				{
					setSize(d);
				}
			}
			else
			{
				checkImageSize();
			}
		}
		else if(resize)
		{
			Dimension d = new Dimension(0,0);
			if(getParent() != null && getParent().getLayout() != null)
			{
				setPreferredSize(d);
				revalidate();
			}
			else
			{
				setSize(d);
			}
		}
		
		forcePaint();
		
		firePropertyChange(IMAGE_PROPERTY,oldImage,this.image);
	}
	
	
	private void checkImageSize()
	{
		boolean resize = false;
		
		int width = getWidth();
		int height = getHeight();
		
		if(image != null)
		{
			if(resizedImage == null)
			{
				resize = true;
			}
			else
			{
				int imageWidth = resizedImage.getWidth(this);
				int imageHeight = resizedImage.getHeight(this);
				
				if(resizeMode == RESIZE_FIT)
				{
					double wf = (double)width / (double)imageWidth;
					double hf = (double)height / (double)imageHeight;
					double f = Math.min(wf,hf);
					width = (int)Math.round((double)imageWidth * f);
					height = (int)Math.round((double)imageHeight * f);
				}
				
				if(imageWidth != width || imageHeight != height)
				{
					resize = true;
				}
			}
		}
		
		if(resize && (lastResizeCheck.width != width || lastResizeCheck.height != height))
		{
			lastResizeCheck.setSize(width,height);
			
			getExecutor().submit(new Runnable()
			{
				@Override
				public void run()
				{
					resizedImage = resizeImage(image);
					repaint();
				}
			});
		}
	}
	
	
	protected Image resizeImage(Image image)
	{
		switch(resizeMode)
		{
			case RESIZE_STRETCH:
			{
				image = GraphicUtils.resize(image,getWidth(),getHeight());
			}
			break;
			
			case RESIZE_FIT:
			{
				image = GraphicUtils.resizeProportional(image,getWidth(),getHeight());
			}
			break;
		}
		
		return image;
	}
	
	
	public void clear()
	{
		imagePath = "";
		refresh(autoResize,false);
		repaint();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void paintImage(Graphics2D g)
	{
		Dimension d = getSize();
		paintBackground(g,0,0,d.width,d.height);
		drawTexture(g,0,0,d.width,d.height);
		
		if(image != null)
		{
			if(resizedImage != null)
			{
				g.drawImage(resizedImage,(d.width - resizedImage.getWidth(null)) / 2,
						(d.height - resizedImage.getHeight(null)) / 2,this);
			}
		}
		else if(!valid)
		{
			refresh(false);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFormularName()
	{
		return support.getFormularName();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void setDataField(String dataField)
	{
		support.setDataField(dataField);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public String getDataField()
	{
		return support.getDataField();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Deprecated
	public final void setFormularValue(VirtualTable vt, int col, Object value)
	{
		support.setFormularValue(vt,col,value);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public void setFormularValue(VirtualTable vt, Map<String, Object> record)
	{
		if(!support.hasDataField())
		{
			return;
		}
		
		Object value = support.getSingleValue(vt,record);
		
		Image image = null;
		
		byte[] bytes = null;
		if(value instanceof byte[])
		{
			bytes = (byte[])value;
		}
		else if(value instanceof ByteHolder)
		{
			bytes = ((ByteHolder)value).toByteArray();
		}
		if(bytes != null)
		{
			try
			{
				image = ImageIO.read(new ByteArrayInputStream(bytes));
				new ImageIcon(image);
			}
			catch(IOException e)
			{
				log.error(e);
			}
		}
		
		setImage(image);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getFormularValue()
	{
		if(image != null)
		{
			try
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ImageIO.write(GraphicUtils.createBufferedImage(image),outputFormat,out);
				return out.toByteArray();
			}
			catch(IOException e)
			{
				// shouldn't happen
				log.error(e);
			}
		}
		
		return null;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveState()
	{
		savedValue = image;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreState()
	{
		setImage(savedValue);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public boolean hasStateChanged()
	{
		return !ObjectUtils.equals(savedValue,image);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void addValueChangeListener(final ValueChangeListener l)
	{
		addPropertyChangeListener(IMAGE_PROPERTY,new PropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				l.valueChanged(evt);
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMultiSelect()
	{
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean verify()
	{
		return support.verify();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void addValidator(Validator validator)
	{
		support.addValidator(validator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void removeValidator(Validator validator)
	{
		support.removeValidator(validator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public Validator[] getValidators()
	{
		return support.getValidators();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void validateState() throws ValidationException
	{
		support.validateState();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void validateState(Validation validation) throws ValidationException
	{
		support.validateState(validation);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void setFilterOperator(Operator filterOperator)
	{
		support.setFilterOperator(filterOperator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public Operator getFilterOperator()
	{
		return support.getFilterOperator();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public void setReadOnly(boolean readOnly)
	{
		support.setReadOnly(readOnly);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public boolean isReadOnly()
	{
		return support.isReadOnly();
	}
	
	
	private void flush()
	{
		if(image != null)
		{
			GraphicUtils.flushImage(image);
			image = null;
		}
		
		if(resizedImage != null)
		{
			GraphicUtils.flushImage(resizedImage);
			resizedImage = null;
		}
		
		lastResizeCheck.setSize(0,0);
		
		if(isShowing())
		{
			forcePaint();
		}
	}
	
	private static ThreadPoolExecutor	executor;
	
	
	private static ThreadPoolExecutor getExecutor()
	{
		if(executor == null)
		{
			executor = new ThreadPoolExecutor(0,5,0L,TimeUnit.MILLISECONDS,
					new LinkedBlockingQueue<Runnable>())
			{
				@Override
				protected void beforeExecute(Thread t, Runnable r)
				{
					t.setName("XdevPicture.loader (" + t.getName() + ")");
					t.setPriority(Thread.MIN_PRIORITY);
				}
			};
		}
		return executor;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNotify()
	{
		if(!Beans.isDesignTime())
		{
			flush();
			valid = false;
		}
		
		super.removeNotify();
	}
	
	
	@Override
	protected void finalize() throws Throwable
	{
		// if(executor != null)
		// {
		// executor.shutdownNow();
		// executor = null;
		// }
		
		flush();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setForeground(Color fg)
	{
		super.setForeground(fg);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setFont(Font font)
	{
		super.setFont(font);
	}
}
