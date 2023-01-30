/*
 * XDEV Application Framework - XDEV Application Framework
 * Copyright © 2003 XDEV Software (https://xdev.software)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package xdev.ui;


import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultFocusTraversalPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class XdevFocusTraversalPolicy extends DefaultFocusTraversalPolicy
{
	private Container	focusCycleRoot;
	

	public XdevFocusTraversalPolicy(Container focusCycleRoot)
	{
		this.focusCycleRoot = focusCycleRoot;
		focusCycleRoot.setFocusCycleRoot(true);
		focusCycleRoot.setFocusTraversalPolicyProvider(true);
	}
	

	@Override
	public Component getComponentAfter(Container aContainer, Component aComponent)
	{
		if(aComponent instanceof XdevFocusCycleController)
		{
			return ((XdevFocusCycleController)aComponent).getFocusComponentAfter();
		}
		
		XdevFocusCycleComponent focusCycleComponent = getFocusCycleComponent(aComponent);
		if(focusCycleComponent != null)
		{
			return getComponent(focusCycleComponent,true);
		}
		
		return super.getComponentAfter(aContainer,aComponent);
	}
	

	@Override
	public Component getComponentBefore(Container aContainer, Component aComponent)
	{
		if(aComponent instanceof XdevFocusCycleController)
		{
			return ((XdevFocusCycleController)aComponent).getFocusComponentBefore();
		}
		
		XdevFocusCycleComponent focusCycleComponent = getFocusCycleComponent(aComponent);
		if(focusCycleComponent != null)
		{
			return getComponent(focusCycleComponent,false);
		}
		
		return super.getComponentBefore(aContainer,aComponent);
	}
	

	private XdevFocusCycleComponent getFocusCycleComponent(Component c)
	{
		while(c != null)
		{
			if(c instanceof XdevFocusCycleComponent)
			{
				return (XdevFocusCycleComponent)c;
			}
			
			c = c.getParent();
		}
		
		return null;
	}
	

	private Component getComponent(final XdevFocusCycleComponent currentFocusedComponent,
			boolean after)
	{
		FocusComponentInfo currentFocusInfo = new FocusComponentInfo(currentFocusedComponent);
		
		final List<FocusComponentInfo> componentInfos = new ArrayList();
		componentInfos.add(currentFocusInfo);
		
		final int targetIndex = currentFocusInfo.tabIndex < 0 ? 0
				: (after ? currentFocusInfo.tabIndex + 1 : currentFocusInfo.tabIndex - 1);
		
		Component component = UIUtils.lookupComponentTree(focusCycleRoot,
				new ComponentTreeVisitor<Component, Component>()
				{
					@Override
					public Component visit(Component cpn)
					{
						if(use(cpn))
						{
							XdevFocusCycleComponent focusCycleCpn = (XdevFocusCycleComponent)cpn;
							if(focusCycleCpn.getTabIndex() >= 0)
							{
								FocusComponentInfo componentInfo = new FocusComponentInfo(
										focusCycleCpn);
								
								if(componentInfo.tabIndex == targetIndex)
								{
									return cpn;
								}
								
								componentInfos.add(componentInfo);
							}
						}
						
						return null;
					}
					

					private boolean use(Component cpn)
					{
						if(cpn != currentFocusedComponent && cpn instanceof XdevFocusCycleComponent
								&& cpn.isVisible() && cpn.isEnabled() && cpn.isFocusable())
						{
							Component parent = cpn;
							while(parent != null)
							{
								try
								{
									if(parent instanceof DistinctChild
											&& !((DistinctChild)parent).isSelected())
									{
										return false;
									}
								}
								catch(Exception e)
								{
									// TabPane.removeTab can cause an AIOBE
									// see 12181
									return false;
								}
								
								parent = parent.getParent();
							}
							
							return true;
						}
						
						return false;
					}
				});
		if(component != null)
		{
			return component;
		}
		
		Collections.sort(componentInfos);
		int index = componentInfos.indexOf(currentFocusInfo);
		if(index != -1)
		{
			if(after)
			{
				if(index == componentInfos.size() - 1)
				{
					return componentInfos.get(0).component;
				}
				else
				{
					return componentInfos.get(index + 1).component;
				}
			}
			else
			{
				if(index == 0)
				{
					return componentInfos.get(componentInfos.size() - 1).component;
				}
				else
				{
					return componentInfos.get(index - 1).component;
				}
			}
		}
		
		return null;
	}
	


	private static class FocusComponentInfo implements Comparable<FocusComponentInfo>
	{
		Component	component;
		int			tabIndex;
		

		FocusComponentInfo(XdevFocusCycleComponent component)
		{
			this.component = (Component)component;
			tabIndex = component.getTabIndex();
			Component parent = this.component.getParent();
			while(parent != null)
			{
				if(parent instanceof XdevFocusCycleRoot)
				{
					tabIndex += ((XdevFocusCycleRoot)parent).getTabIndexOffset();
				}
				
				parent = parent.getParent();
			}
		}
		

		@Override
		public int compareTo(FocusComponentInfo other)
		{
			int index1 = tabIndex;
			int index2 = other.tabIndex;
			return index1 < index2 ? -1 : (index1 == index2 ? 0 : 1);
		}
	}
}
