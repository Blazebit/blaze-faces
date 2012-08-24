package com.blazebit.blazefaces.examples.touch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;

import com.blazebit.blazefaces.examples.domain.NewsEntry;
import com.blazebit.blazefaces.examples.domain.NewsGroup;
import com.blazebit.blazefaces.examples.service.NewsService;
import com.blazebit.blazefaces.examples.service.YAHOONewsService;

@Named
@ViewAccessScoped
public class NewsController implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<NewsGroup> groups = new ArrayList<NewsGroup>();

	private NewsService newsService = new YAHOONewsService();

	private NewsEntry selectedEntry;

	private NewsGroup selectedGroup;

	public NewsController() {
		groups = this.newsService.fetchNews();
	}

	public List<NewsGroup> getGroups() {
		return groups;
	}

	public NewsEntry getSelectedEntry() {
		return selectedEntry;
	}

	public void setSelectedEntry(NewsEntry selectedEntry) {
		this.selectedEntry = selectedEntry;
	}

	public NewsGroup getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(NewsGroup selectedGroup) {
		this.selectedGroup = selectedGroup;
	}
}
