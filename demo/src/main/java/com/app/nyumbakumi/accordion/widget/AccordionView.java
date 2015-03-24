
package com.app.nyumbakumi.accordion.widget;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.nyumbakumi.R;
import com.app.nyumbakumi.utils.FontUtils;


public class AccordionView extends LinearLayout {

  private boolean initialized = false;
  
  // -- from xml parameter
  private int headerLayoutId;
  private int headerFoldButton;
  private int headerLabel;
  private int sectionContainer;
  private int sectionContainerParent;
  private int sectionBottom;
  
  private String[] sectionHeaders;
  
  private View[] children;
  private View[] wrappedChildren;
  private View[] headers;
  private View[] footers;
  private View[] sectionContainers;
  
  private Map<Integer, View> sectionByChildId = new HashMap<Integer, View>();
  
  private int[] sectionVisibilities = new int[0];
  
  public AccordionView(Context context, AttributeSet attrs) {
    super(context, attrs);
    
    if (attrs != null) {
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.accordion);
      headerLayoutId = a.getResourceId(R.styleable.accordion_header_layout_id, 0);
      headerFoldButton = a.getResourceId(R.styleable.accordion_header_layout_fold_button_id, 0);
      headerLabel = a.getResourceId(R.styleable.accordion_header_layout_label_id, 0);
      sectionContainer = a.getResourceId(R.styleable.accordion_section_container, 0);
      sectionContainerParent = a.getResourceId(R.styleable.accordion_section_container_parent, 0);
      sectionBottom = a.getResourceId(R.styleable.accordion_section_bottom, 0);
      int sectionHeadersResourceId = a.getResourceId(R.styleable.accordion_section_headers, 0);
      int sectionVisibilityResourceId = a.getResourceId(R.styleable.accordion_section_visibility, 0);
      
      if (sectionHeadersResourceId == 0) {
        throw new IllegalArgumentException("Please set section_headers as reference to strings array.");
      }
      
      sectionHeaders = getResources().getStringArray(sectionHeadersResourceId);
      
      if (sectionVisibilityResourceId != 0) {
        sectionVisibilities = getResources().getIntArray(sectionVisibilityResourceId);
      }
    }
    
    if (headerLayoutId == 0 || headerLabel == 0 || sectionContainer == 0 || sectionContainerParent == 0 || sectionBottom == 0) {
    	//if (headerLayoutId == 0 || headerLabel == 0 || sectionContainer == 0 || sectionContainerParent == 0) {
    	      throw new IllegalArgumentException(
          "Please set all header_layout_id,  header_layout_label_id, section_container, section_container_parent and section_bottom attributes.");
    }

    setOrientation(VERTICAL);
  }

  private void assertWrappedChildrenPosition(int position) {
    if (wrappedChildren == null || position >= wrappedChildren.length) {
      throw new IllegalArgumentException("Cannot toggle section " + position + ".");
    }
  }

  public View getChildById(int id) {
    for (int i = 0; i < wrappedChildren.length; i++) {
      View v = wrappedChildren[i].findViewById(id);
      if (v != null) {
        return v;
      }
    }
    return null;
  }

  public View getSectionByChildId(int id) {
    return sectionByChildId.get(id);
  }

  private View getView(final LayoutInflater inflater, int i, boolean hide) {
    final View container = inflater.inflate(sectionContainer, null);
    container.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
    
    final ViewGroup newParent = (ViewGroup) container.findViewById(sectionContainerParent);
    newParent.addView(children[i]);
    
    FontUtils.setCustomFont(container, AccordionView.this.getContext().getAssets());
    if (container.getId() == -1) {
      container.setId(i);
    }

    if (hide) {
      container.setVisibility(GONE);
    }
    return container;
  }

  private View getViewFooter(LayoutInflater inflater) {
    return inflater.inflate(sectionBottom, null);
  }
  
  private View getViewHeader(LayoutInflater inflater, final int position, boolean hide) {
    final View view = inflater.inflate(headerLayoutId, null);
    ((TextView) view.findViewById(headerLabel)).setText(sectionHeaders[position]);
    
    //Set the custom Font used for the header.
    FontUtils.setCustomFont(view, AccordionView.this.getContext().getAssets());

    // -- support for no fold button
    if (headerFoldButton == 0) {
      return view;
    }
    
    final View foldButton = view.findViewById(headerFoldButton);

    if (foldButton instanceof ToggleImageLabeledButton) {
      final ToggleImageLabeledButton toggleButton = (ToggleImageLabeledButton) foldButton;
      toggleButton.setState(wrappedChildren[position].getVisibility() == VISIBLE);
    }

    final OnClickListener onClickListener = new OnClickListener() {

      @Override
      public void onClick(View v) {
    	  /**
    	   * Toggle the items state{ON / OFF}
    	   */
        toggleSection(position);
      }
    };
    
    foldButton.setOnClickListener(onClickListener);
    
    view.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        onClickListener.onClick(v);

        if (foldButton instanceof ToggleImageLabeledButton) {
          final ToggleImageLabeledButton toggleButton = (ToggleImageLabeledButton) foldButton;
          toggleButton.setState(wrappedChildren[position].getVisibility() == VISIBLE);
        }
      }
    });

    return view;
  }

  @Override
  protected void onFinishInflate() {
    if (initialized) {
      super.onFinishInflate();
      return;
    }

    final int childCount = getChildCount();
    sectionContainers = new View[childCount];
    children = new View[childCount];
    headers = new View[childCount];
    footers = new View[childCount];
    wrappedChildren = new View[childCount];

    if (sectionHeaders.length != childCount) {
      throw new IllegalArgumentException("Section headers string array length must be equal to accordion view child count.");
    }

    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    for (int i = 0; i < childCount; i++) {
      children[i] = getChildAt(i);
    }
    removeAllViews();

    for (int i = 0; i < childCount; i++) {
      final boolean hide = sectionVisibilities.length > 0 && sectionVisibilities[i] == 0;

      wrappedChildren[i] = getView(inflater, i, hide);
      headers[i] = getViewHeader(inflater, i, hide);
      footers[i] = getViewFooter(inflater);
      footers[i].setVisibility(View.GONE);	//turn the footer to invisible
      final LinearLayout section = new LinearLayout(getContext());
      sectionContainers[i]=section;
      section.setOrientation(LinearLayout.VERTICAL);
      section.addView(headers[i]);
      section.addView(wrappedChildren[i]);
      section.addView(footers[i]);

      sectionByChildId.put(children[i].getId(), section);
      
      addView(section);
    }

    initialized = true;

    super.onFinishInflate();
  }

  /**
   * 
   * @param position
   * @return visibility
   *          {@link android.view.View#GONE} and {@link android.view.View#VISIBLE}
   */
  public int getSectionVisibility(int position) {
	  assertWrappedChildrenPosition(position);
	  return wrappedChildren[position].getVisibility();
  }
  
  /**
   * 
   * @param position
   * @param visibility
   *          {@link android.view.View#GONE} and {@link android.view.View#VISIBLE}
   */
  public void setSectionVisibility(int position, int visibility) {
    assertWrappedChildrenPosition(position);
    wrappedChildren[position].setVisibility(visibility);
    if (headerFoldButton != 0) {
    	
    	/**
    	 * Lets change the background as appropriate
    	 */
    	final View headerView = headers[position];
    	if(visibility == VISIBLE) {
    		headerView.findViewById(R.id.mainHeaderLayout).setBackgroundResource(R.drawable.header_selected_background);
    		// R.drawable.header_selected_background
    		//headers[position].setBackgroundResource(R.drawable.header_selected_background);
    		
    	}else if(visibility == GONE) {
    		headerView.findViewById(R.id.mainHeaderLayout).setBackgroundResource(R.drawable.header_bg);
    		// R.drawable.header_bg
    		//headers[position].setBackgroundResource(R.drawable.header_bg);
    		
    	}
    	
    	final View foldButton = headers[position].findViewById(headerFoldButton);
    	if (foldButton instanceof ToggleImageLabeledButton) {
    	  final ToggleImageLabeledButton toggleButton = (ToggleImageLabeledButton) foldButton;
    	  toggleButton.setState(wrappedChildren[position].getVisibility() == VISIBLE);
    	}
    }
  }
  
  /**
   * Toggle the visibility of a section. If its visible, then its set to gone.
   * If its Invisible, then its set to visible
   * @param position Position of the element we need to toggle
   */
  public void toggleSection(int position) {
    assertWrappedChildrenPosition(position);
    
    if (wrappedChildren[position].getVisibility() == VISIBLE) {
      setSectionVisibility(position, GONE);
      Log.i(VIEW_LOG_TAG, "toggle Selection->Gone: "+position);
    } else {
      hideAllSections(position);
      //setSectionVisibility(position, VISIBLE);
    }
  }
  
  /**
   * Some algorithm for hiding all the other sections that are open and haven't been closed.
   * @param position The Position of the section that is being activated.
   */
  private void hideAllSections(int position) {
	  for (int i = 0; i < wrappedChildren.length; i++) {
		  if(i == position) {
			  setSectionVisibility(i, VISIBLE);
		      Log.i(VIEW_LOG_TAG, "toggle Selection->Visible: "+position); 
		  }else {
			  setSectionVisibility(i, GONE);
		  }
	}
  }
  
  /**
   * 
   * @param position
   * @param visibility
   *          {@link android.view.View#GONE} and {@link android.view.View#VISIBLE}
   */
  public void setChildVisibility(int position, int visibility) {
	  assertWrappedChildrenPosition(position);
	  sectionContainers[position].setVisibility(visibility);
  }

}
